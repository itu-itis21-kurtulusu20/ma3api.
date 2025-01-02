package tr.gov.tubitak.uekae.esya.api.smartcard.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.CK_SESSION_INFO;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.ISeed;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.io.IOException;

public class SmartCardSeed implements ISeed {

    private static final Logger logger = LoggerFactory.getLogger(SmartCardSeed.class);

    private SmartCard mSC = null;
    private long mSlotNo;
    private long mSessionID = -1;

    //static long count = 0;

    public SmartCardSeed(CardType aCardType, long aSlotNo) throws PKCS11Exception, IOException, SmartCardException {
        mSC = new SmartCard(aCardType);
        mSlotNo = aSlotNo;
        openSession();
    }

    public byte[] getSeed(int aLength) {
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
            mSessionID = mSC.openSession(mSlotNo);
            logger.info("SmartCard Session is opened");
        } catch (PKCS11Exception e) {
            logger.warn("Error While Getting SmartCard Seed. It will try again", e);
            try {
                Thread.sleep(500);
                mSessionID = mSC.openSession(mSlotNo);
            } catch (Exception ex) {
                throw new ESYARuntimeException("Error While Getting SmartCard Seed. It will not tried again:" + e.getMessage(), e);
            }
        }
    }

    public SmartCard getSmartcard() {
        return mSC;
    }

    public long getSlotNo() {
        return mSlotNo;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  SmartCardSeed) {
            SmartCardSeed scObj = (SmartCardSeed) obj;
            if (this.getSmartcard().getCardType().equals(scObj.getSmartcard().getCardType()) &&
                    this.getSlotNo() == scObj.getSlotNo())
                return true;
        }
        else{
            return false;
        }

        return false;
    }

}
