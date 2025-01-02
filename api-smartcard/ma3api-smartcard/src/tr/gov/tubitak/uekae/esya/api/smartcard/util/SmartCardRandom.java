package tr.gov.tubitak.uekae.esya.api.smartcard.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.CK_SESSION_INFO;
import sun.security.pkcs11.wrapper.PKCS11;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BasePRNG;
import tr.gov.tubitak.uekae.esya.api.common.crypto.LimitReachedException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.io.IOException;
import java.util.Map;

public class SmartCardRandom extends BasePRNG {

    private static final Logger logger = LoggerFactory.getLogger(SmartCardRandom.class);

    private SmartCard mSC = null;
    private long mSessionID = -1;
    private long slotNo;
    private String PIN = "";

    private static final int CARD_DEFAULT_RANDOM_SIZE = 2048;

    public int getRandomSize() {
        return randomSize;
    }

    public void setRandomSize(int randomSize) {
        this.randomSize = randomSize;
    }

    private int randomSize=CARD_DEFAULT_RANDOM_SIZE;




    public SmartCardRandom(CardType aCardType, long slotNo) throws PKCS11Exception, IOException, SmartCardException {
        this(aCardType,slotNo,MIN_BUFFER_SIZE);
    }


    public SmartCardRandom(CardType aCardType, long slotNo,int bufferSize) throws PKCS11Exception, IOException, SmartCardException {
        this(aCardType, slotNo, bufferSize, null);
    }

    public SmartCardRandom(CardType aCardType, long slotNo,int bufferSize, String PIN) throws PKCS11Exception, IOException, SmartCardException {
        super("smartcard_prng");
        mSC = new SmartCard(aCardType);
        this.slotNo = slotNo;
        this.PIN = PIN;
        openSession();
        setBufferSize(bufferSize);
    }



    @Override
    public void setup(Map attributes) {
        // yes nothing...
    }

    private byte[] getRandom(int aLength) {
        if(aLength<=randomSize){
            //logger.info("Toplam " + aLength + " uzunluğunda random üretilecek.");
            //Chronometer c1 = new Chronometer("aaa");
            //c1.start();
            byte[] retBytes = getRandomCore(aLength);
           // c1.stop();
            //logger.info(aLength + " uzunluğunda verininin üretilme süresi => " + c1.toString());
            return retBytes;
        }

        byte[] retRandom = new byte[aLength];
        int currentPos=0;
        int partCount = aLength / randomSize;
       // logger.info("Toplam " + aLength + " uzunluğunda random üretilecek.");
        //Chronometer c1 = new Chronometer("aaa");
       // c1.start();
        for (int i = 0; i < partCount; i++) {
           // Chronometer c2 = new Chronometer("bbb");
           // c2.start();
            byte[] random = getRandomCore(randomSize);
           // c2.stop();
           // logger.info(randomSize + " uzunluğunda verininin üretilme süresi => " + c2.toString());
            System.arraycopy(random,0,retRandom,currentPos,random.length);
            currentPos+=random.length;
        }

        int remaining = aLength % randomSize;
        if(remaining>0){
            byte[] random = getRandomCore(remaining);
            System.arraycopy(random,0,retRandom,currentPos,random.length);
            currentPos+=random.length;
        }

      //  c1.stop();
      //  logger.info(aLength + " uzunluğunda verininin üretilme süresi => " + c1.toString());

        return retRandom;
    }


    @Override
    public void fillBlock() throws LimitReachedException {
        buffer = getRandom(buffer.length);
    }

    private byte[] getRandomCore(int aLength) {
        try {
            return mSC.getRandomData(mSessionID, aLength);
        } catch (Exception e) {
            logger.warn("Cannot get seed from SmartCard:"+e.getMessage(), e);
            // in case we fail validate session
            validateSession();
            try {
                return mSC.getRandomData(mSessionID, aLength);
            } catch (Exception newEx) {
                throw new ESYARuntimeException("Cannot get seed from SmartCard:"+newEx.getMessage(),newEx);
            }
        }

    }

    private synchronized void validateSession() { // we dont want many thread come and validate simultaneously
        CK_SESSION_INFO sessionInfo = null;
        try {
            sessionInfo = mSC.getSessionInfo(mSessionID);
        } catch (PKCS11Exception e) {
            logger.info("SmartCard seed Session is Invalid:"+e.getMessage()+" Reopening Session", e);
            openSession();
        }
        if(sessionInfo == null){
            logger.info("SmartCard seed Session is Null, Opening Session");
            openSession();
        }
    }

    private void openSession() {
        try {
            mSessionID = mSC.openSession(slotNo);
            logger.info("SmartCard Session is opened");
            if(PIN != null && !PIN.isEmpty())
            {
                logger.info("Trying to Login to smartcard");
                try
                {
                    mSC.login(mSessionID, PIN);
                }
                catch (PKCS11Exception ex)
                {
                    if(ex.getErrorCode() != PKCS11Constants.CKR_USER_ALREADY_LOGGED_IN)
                        throw ex;
                }
                logger.info("Login to smartcard succeded");
            }
        } catch (PKCS11Exception e) {
            throw new ESYARuntimeException("Error while openning smartcard session or login to smartcard"+e.getMessage(), e);
        }
    }

    public SmartCard getSmartcard() {
        return mSC;
    }

    public long getSlotNo() {
        return slotNo;
    }

    @Override
    public Object clone()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
