using System;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

namespace tr.gov.tubitak.uekae.esya.api.smartcard.util
{
    public class SCCipherWithCertSerialNo : BaseCipher
    {
        private readonly SmartCard mAkilliKart;
        private readonly long mSessionID;
        private readonly byte[] mSertifikaSeriNo;
        private readonly String mAlgorithm;

        /**
	    * 
	    * @param aSmartCard
	    * @param aSessionID
	    * @param aCertSerialNo
	    */

        public SCCipherWithCertSerialNo(SmartCard aSmartCard, long aSessionID, byte[] aCertSerialNo)
        {
            mAkilliKart = aSmartCard;
            mSessionID = aSessionID;
            mSertifikaSeriNo = aCertSerialNo;
            mAlgorithm = Algorithms.CIPHER_RSA_PKCS1;
        }

        public byte[] doFinal(byte[] aData)
        {
            try
            {
                return SmartOp.decrypt(mAkilliKart, mSessionID, mSertifikaSeriNo, aData);
            }
            catch (Exception aEx)
            {
                throw new ESYAException("Kartta sifre cozulurken hata olustu", aEx);
            }
        }
        /**
         * Returns cipher algorithm as string
         * @return
         */
        public String getCipherAlgorithmStr()
        {
            return mAlgorithm;
        }
    }
}