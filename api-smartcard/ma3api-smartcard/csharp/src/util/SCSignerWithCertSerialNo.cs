using System;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
namespace tr.gov.tubitak.uekae.esya.api.smartcard.util
{
    public class SCSignerWithCertSerialNo : BaseSigner
    {
        protected SmartCard mAkilliKart = null;
        protected long mSessionID;
        protected long mSlotID;
        protected byte[] mSertifikaSeriNo = null;
        protected String mSigningAlg = Algorithms.SIGNATURE_RSA_SHA1;

        protected IAlgorithmParameterSpec mParams = null;


        /**
     * Implements BaseSigner interface to sign.
     * @param aSmartCard
     * @param aSessionID
     * @param aSlotID
     * @param aSertifikaSeriNo
     * @param aSigningAlg
     */
        public SCSignerWithCertSerialNo(SmartCard aSmartCard, long aSessionID, long aSlotID, byte[] aCertSerialNo, String aSigningAlg)
        {
            mAkilliKart = aSmartCard;
            mSessionID = aSessionID;
            mSlotID = aSlotID;
            mSertifikaSeriNo = aCertSerialNo;
            mSigningAlg = aSigningAlg;
        }
        /**
         * Implements BaseSigner interface to sign.
         * @param aSmartCard
         * @param aSessionID
         * @param aSlotID
         * @param aSertifikaSeriNo
         * @param aSigningAlg
         * @param aParams
         */
        public SCSignerWithCertSerialNo(SmartCard aSmartCard, long aSessionID, long aSlotID, byte[] aCertSerialNo, String aSigningAlg, IAlgorithmParameterSpec aParams)
        {
            mAkilliKart = aSmartCard;
            mSessionID = aSessionID;
            mSlotID = aSlotID;
            mSertifikaSeriNo = aCertSerialNo;
            mSigningAlg = aSigningAlg;
            mParams = aParams;
        }
        /**
          * Sign given byte array
          * @param aImzalanacak byte[]
          */
        public byte[] sign(byte[] aImzalanacak)
        {
            try
            {
                return SmartOp.sign(mAkilliKart, mSessionID, mSlotID, mSertifikaSeriNo, aImzalanacak, mSigningAlg, mParams);
            }
            catch (Exception aEx)
            {
                throw new ESYAException("Kartta Imzalama yapılırken hata oluştu", aEx);
            }
        }
        /**
         * Returns signature algorithm as string
         * @return
         */
        public String getSignatureAlgorithmStr()
        {
            return mSigningAlg;
        }
        /**
         * Returns algorithm parameter spec
         * @return
         */
        public IAlgorithmParameterSpec getAlgorithmParameterSpec()
        {
            return mParams;
        }
    }
}
