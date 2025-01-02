package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.sessionpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class HSMSessionPool {
    private static Logger logger = LoggerFactory.getLogger(HSMSessionPool.class);

    public static int DEFAULT_MAX_CAPACITY_PER_KEY = 64;
    public static int DEFAULT_TIMEOUT_INSECONDS = 20;

    private ConcurrentHashMap<String, SessionQueue> hsmPool;
    private ArrayList<ObjectSessionInfo> allCreatedObjects;

    private SmartCard sc;
    private long slotNo;

    private int maxCapacityForEachLabel;
    private int timeoutInSeconds;

    private HSMSessionPool() {
        try {
            LV.getInstance().checkLD(LV.Urunler.AKILLIKART);
        } catch (LE e) {
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. " + e.getMessage(), e);
        }
    }

    public HSMSessionPool(CardType cardType, long slotNo, String pin, int maxCapacityPerKey, int timeoutInSeconds) throws PKCS11Exception, IOException {
        this();

        allCreatedObjects = new ArrayList<>();
        hsmPool = new ConcurrentHashMap();

        this.slotNo = slotNo;
        this.maxCapacityForEachLabel = maxCapacityPerKey;
        this.timeoutInSeconds = timeoutInSeconds;

        initialize(cardType, pin);
    }

    public HSMSessionPool(CardType cardType, long slotNo, String pin) throws PKCS11Exception, IOException {
        this(cardType, slotNo, pin, DEFAULT_MAX_CAPACITY_PER_KEY, DEFAULT_TIMEOUT_INSECONDS);
    }

    public CardType getCardType() {
        return sc.getCardType();
    }

    private void initialize(CardType cardType, String pin) throws PKCS11Exception, IOException {
        this.sc = new SmartCard(cardType);

        //login once.
        long session = sc.openSession(this.slotNo);

        try {
            sc.login(session, pin);
        } catch (PKCS11Exception ex) {
            if (ex.getErrorCode() != PKCS11Constants.CKR_USER_ALREADY_LOGGED_IN)
                throw ex;
        }

        //At the end of operation, to close the session, record is added to list.
        ObjectSessionInfo sessionPoolItem = new ObjectSessionInfo(session, 0, "");
        allCreatedObjects.add(sessionPoolItem);
    }

    private ObjectSessionInfo createPoolItem(SessionQueue sessionQueue, String label) throws PKCS11Exception, SmartCardException, InterruptedException {
        long objectId = 0;
        long session = sc.openSession(slotNo);

        if (!StringUtil.isNullorEmpty(label)) {
            objectId = findObjectId(session, label);
        }

        ObjectSessionInfo sessionInfo = new ObjectSessionInfo(session, objectId, label);

        synchronized (allCreatedObjects) {
            allCreatedObjects.add(sessionInfo);
        }

        sessionQueue.increaseActiveSessionCount();

        return sessionInfo;
    }


    private long findObjectId(long session, String label) throws SmartCardException, PKCS11Exception {
        CK_ATTRIBUTE[] labelAttrr = new CK_ATTRIBUTE[1];
        labelAttrr[0] = new CK_ATTRIBUTE();
        labelAttrr[0].type = PKCS11Constants.CKA_LABEL;
        labelAttrr[0].pValue = label;

        long[] objIds = sc.findObjects(session, labelAttrr);

        if (objIds.length == 1)
            return objIds[0];
        else if (objIds.length == 0)
            throw new SmartCardException(label + " key not found.");
        else
            return selectKey(label, session, objIds);
    }

    private long selectKey(String label, long session, long[] objIds) throws PKCS11Exception, SmartCardException {
        int convenientKeyCount = 0;
        long convenientId = 0;

        CK_ATTRIBUTE[] classAttr = new CK_ATTRIBUTE[1];
        classAttr[0] = new CK_ATTRIBUTE();
        classAttr[0].type = PKCS11Constants.CKA_CLASS;

        for (int i = 0; i < objIds.length; i++) {
            sc.getAttributeValue(session, objIds[i], classAttr);
            long objectType = classAttr[0].getLong();
            if (objectType == PKCS11Constants.CKO_PRIVATE_KEY || objectType == PKCS11Constants.CKO_SECRET_KEY) {
                convenientKeyCount++;
                convenientId = objIds[i];
            }
        }

        if (convenientKeyCount > 1)
            throw new SmartCardException("Too many key with same name: " + label);

        return convenientId;
    }

    /**
     * Returns session and object id for the given key label. To obtain session information regardless of the label,
     * provide empty string as the label parameter.
     *
     * @param label
     * @return
     * @throws PKCS11Exception
     * @throws ESYAException
     * @throws InterruptedException
     */
    public synchronized ObjectSessionInfo checkOutItem(String label) throws PKCS11Exception, ESYAException, InterruptedException {
        SessionQueue sessionQueue = hsmPool.get(label);
        if (sessionQueue == null) {
            sessionQueue = new SessionQueue(maxCapacityForEachLabel);
            hsmPool.put(label, sessionQueue);
        }
        return checkOutItem(sessionQueue, label);
    }

    private ObjectSessionInfo checkOutItem(SessionQueue sessionQueue, String label) throws ESYAException {
        try {
            ObjectSessionInfo poolItem = sessionQueue.poll();
            if (poolItem != null) {
                return poolItem;
            } else {
                if (sessionQueue.getActiveSessionCount() < maxCapacityForEachLabel) {
                    try {
                        poolItem = createPoolItem(sessionQueue, label);
                        return poolItem;
                    } catch (PKCS11Exception | SmartCardException e) {
                        throw new ESYAException("Hsm item can not be created!", e);
                    }
                } else {
                    poolItem = sessionQueue.poll(timeoutInSeconds, TimeUnit.SECONDS);
                    if (poolItem == null) {
                        throw new ESYAException("Hsm item pool returns no HSM after timeout!");
                    }
                    return poolItem;
                }
            }
        } catch (InterruptedException e) {
            throw new ESYAException("Hsm item pool returns no Hsm");
        }
    }

    public boolean offer(ObjectSessionInfo poolItem) {
        SessionQueue sessionQueue = hsmPool.get(poolItem.getObjectLabel());
        return sessionQueue.offer(poolItem);
    }

    public byte[] executeCryptoOp(String label, byte[] data, CryptoOp cryptoOp) throws Exception {
        ObjectSessionInfo sessionInfo = null;
        byte[] result = null;
        try {
            sessionInfo = checkOutItem(label);
            result = cryptoOp.execute(sessionInfo.getSession(), sessionInfo.getObjectId(), data);
            offer(sessionInfo);
            return result;
        } catch (Exception ex) {
            ObjectSessionInfo resetSession = resetFailedSession(sessionInfo, label);
            result = cryptoOp.execute(resetSession.getSession(), resetSession.getObjectId(), data);
            offer(resetSession);
            return result;
        }
    }

    public ObjectSessionInfo resetFailedSession(ObjectSessionInfo sessionInfo, String label) throws PKCS11Exception, InterruptedException, SmartCardException {
        if (sessionInfo != null)
            removeCheckedOutSessionInfo(sessionInfo);
        SessionQueue sessionQueue = hsmPool.get(label);
        sessionInfo = createPoolItem(sessionQueue, label);
        return sessionInfo;
    }

    private void removeCheckedOutSessionInfo(ObjectSessionInfo sessionInfo) {
        SessionQueue sessionQueue = hsmPool.get(sessionInfo.getObjectLabel());
        sessionQueue.decreaseActiveSessionCount();
        synchronized (allCreatedObjects) {
            allCreatedObjects.remove(sessionInfo);
        }
        try {
            sc.closeSession(sessionInfo.getSession());
        } catch (PKCS11Exception e) {
            logger.trace("Error while closing session.(Error is expected)", e);
        }
    }

    public void endPool() {
        synchronized (allCreatedObjects) {

            //Call logout until it is succedded.
            for (ObjectSessionInfo sessionInfo : allCreatedObjects) {
                try {
                    sc.logout(sessionInfo.getSession());
                    break;
                } catch (PKCS11Exception e) {
                    logger.debug("Error while logout session. Session: " + sessionInfo.getSession(), e);
                }
            }

            //Close all sessions.
            for (ObjectSessionInfo sessionInfo : allCreatedObjects) {
                try {
                    sc.closeSession(sessionInfo.getSession());
                } catch (PKCS11Exception e) {
                    logger.debug("Error while closing session.Session: " + sessionInfo.getSession(), e);
                }
            }

            hsmPool.clear();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.endPool();
        super.finalize();
    }

    public interface CryptoOp {
        byte[] execute(long session, long objectId, byte[] data) throws Exception;
    }

    public static class SignOperation implements CryptoOp {
        private SmartCard sc;
        private CK_MECHANISM mechanism;

        public SignOperation(SmartCard sc, CK_MECHANISM mechanism) {
            this.sc = sc;
            this.mechanism = mechanism;
        }

        @Override
        public byte[] execute(long session, long objectId, byte[] data) throws Exception {
            return sc.signDataWithKeyID(session, objectId, mechanism, data);
        }
    }

    public static class EncryptionOperation implements CryptoOp {
        private SmartCard sc;
        private CK_MECHANISM mechanism;

        public EncryptionOperation(SmartCard sc, CK_MECHANISM mechanism) {
            this.sc = sc;
            this.mechanism = mechanism;
        }

        @Override
        public byte[] execute(long session, long objectId, byte[] data) throws Exception {
            return sc.getCardType().getCardTemplate().getPKCS11Ops().encryptData(session, objectId, data, mechanism);
        }
    }

    public static class DecryptionOperation implements CryptoOp {
        private SmartCard sc;
        private CK_MECHANISM mechanism;

        public DecryptionOperation(SmartCard sc, CK_MECHANISM mechanism) {
            this.sc = sc;
            this.mechanism = mechanism;
        }

        @Override
        public byte[] execute(long session, long objectId, byte[] data) throws Exception {
            return sc.getCardType().getCardTemplate().getPKCS11Ops().decryptData(session, objectId, data, mechanism);
        }
    }
}
