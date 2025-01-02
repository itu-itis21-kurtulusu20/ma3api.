using System;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
namespace tr.gov.tubitak.uekae.esya.api.smartcard.util
{
    public class SCSignerWithKeyLabel : BaseSigner
    {
        protected SmartCard mAkilliKart = null;
        protected long mSessionID;
        protected long mSlotID;
        protected String mAnahtarAdi = "";
        protected String mSigningAlg = Algorithms.SIGNATURE_RSA_SHA1;

        protected IAlgorithmParameterSpec mParams = null;
        /**
	 * Implements BaseSigner interface to sign.
	 * @param aSmartCard 
	 * @param aSessionID
	 * @param aSlotID
	 * @param aKeyLabel
	 * @param aSigningAsymAlg
	 * @param aDigestAlg
	 */
        public SCSignerWithKeyLabel(SmartCard aSmartCard, long aSessionID, long aSlotID, String aKeyLabel, String aSigningAlg)
        {
            mAkilliKart = aSmartCard;
            mSessionID = aSessionID;
            mSlotID = aSlotID;
            mAnahtarAdi = aKeyLabel;
            mSigningAlg = aSigningAlg;
        }
        public SCSignerWithKeyLabel(SmartCard aSmartCard, long aSessionID, long aSlotID, String aKeyLabel, String aSigningAlg, IAlgorithmParameterSpec aParams)
        {
            mAkilliKart = aSmartCard;
            mSessionID = aSessionID;
            mSlotID = aSlotID;
            mAnahtarAdi = aKeyLabel;
            mSigningAlg = aSigningAlg;
            mParams = aParams;
        }
        public byte[] sign(byte[] aImzalanacak)
        {
            try
            {
                return SmartOp.sign(mAkilliKart, mSessionID, mSlotID, mAnahtarAdi, aImzalanacak, mSigningAlg, mParams);
            }
            catch (Exception aEx)
            {
                throw new ESYAException("Kartta imzalama yapılırken hata olustu", aEx);
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

        public IAlgorithmParameterSpec getAlgorithmParameterSpec()
        {
            return mParams;
        }
    }
}
