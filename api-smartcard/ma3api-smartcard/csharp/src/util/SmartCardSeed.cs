using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Runtime.CompilerServices;
using System.Text;
using iaik.pkcs.pkcs11.wrapper;
using log4net;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.util
{
    public class SmartCardSeed : ISeed
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        protected SmartCard mSC;
        private readonly long mSlotNo;
        protected long mSessionID = -1;


        public SmartCardSeed(CardType aCardType, long aSlotNo)
        {
            mSC = new SmartCard(aCardType);
            mSlotNo = aSlotNo;
            openSession();
        }


        public byte[] getSeed(int aLength)
        {
            try
            {
                return mSC.getRandomData(mSessionID, aLength);
            }
            catch (Exception e)
            {
                logger.Warn("Cannot get seed from SmartCard:" + e.Message);
                validateSession();
                try
                {
                    return mSC.getRandomData(mSessionID, aLength);
                }
                catch (Exception newEx)
                {
                    throw new ESYARuntimeException("Cannot get seed from SmartCard:" + newEx.Message, newEx);
                }
            }
        }

        [MethodImpl(MethodImplOptions.Synchronized)]
        private void validateSession() // we dont want many thread come and validate simultaneously
        { 
            CK_SESSION_INFO sessionInfo = null;
            try
            {
                sessionInfo = mSC.getSessionInfo(mSessionID);
            }
            catch (PKCS11Exception e)
            {
                logger.Info("SmartCard seed Session is Invalid:" + e.Message + " Reopening Session");
                openSession();
            }
            if (sessionInfo == null)
            {
                logger.Info("SmartCard seed Session is Null, Opening Session");
                openSession();
            }
        }

        private void openSession()
        {
            try
            {
                mSessionID = mSC.openSession(mSlotNo);
                logger.Info("SmartCard Session is opened");
            }
            catch (PKCS11Exception e)
            {
                throw new ESYARuntimeException("Error While Getting SmartCard Seed:" + e.Message, e);
            }
        }

        public SmartCard getSmartcard()
        {
            return mSC;
        }

        public long getSlotNo()
        {
            return mSlotNo;
        }

        public override bool Equals(object obj)
        {
            if (obj is SmartCardSeed)
            {
                SmartCardSeed scObj = (SmartCardSeed) obj;
                if (this.getSmartcard().getCardType().Equals(scObj.getSmartcard().getCardType()) &&
                   this.getSlotNo() == scObj.getSlotNo())
                    return true;
            }
            return false;


            
        }
    }
}
