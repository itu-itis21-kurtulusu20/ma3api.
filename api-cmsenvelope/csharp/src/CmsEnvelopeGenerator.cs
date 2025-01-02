using System;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.cms.envelope;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;

//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope
{
    /**
     * CmsEnvelopeGenerator is used to generate the envelopes in the memory
     * @author muratk
     *
     */
    public class CmsEnvelopeGenerator : GeneratorBase
    {
        private readonly byte[] mSifrelenecek;

        /**
         * Constructor for CmsEnvelopeGenerator
         * @param aPlainData The plain data that will be enveloped
         * @param aAlgorithm The symmetric algorithm that will be used while enveloped generation.
         * @throws ArgErrorException 
         */
        public CmsEnvelopeGenerator(byte[] aPlainData, CipherAlg aAlgorithm)
            : base(aAlgorithm)
        {
            if (aAlgorithm == null)
                throw new ArgErrorException("CipherAlg can not be null");
            mSifrelenecek = aPlainData;
            mSymmetricAlgorithm = aAlgorithm;
        }
    
        /**
         * AES256_CBC is used as default cipher algorithm.
         * @param aPlainData The plain data that will be enveloped
         */
        public CmsEnvelopeGenerator(byte[] aPlainData)
                : base(CipherAlg.AES256_CBC)
            {
                mSifrelenecek = aPlainData;
                mSymmetricAlgorithm = CipherAlg.AES256_CBC;
            }


        /**
         * Generates the envelope. A random symmetric key is generated.
         * @return Encoded ContentInfo for enveloped data
         * @throws CMSException If an error occurs while generating the envelope
         */
        public byte[] generate()
        {
            byte[] key = null;
            try
            {
                key = KeyUtil.generateKey(mSymmetricAlgorithm, KeyUtil.getKeyLength(mSymmetricAlgorithm));
            }
            catch (CryptoException e)
            {
                throw new CMSException("Can not randomize symmetric key", e);
            }
            
            if (ByteUtil.isAllZero(key))
                throw new CMSException("The key must be a non-zero value");
            return generate(key);        
        }

        /**
         * Generates the envelope. The given symmetric algorithm and key is used for symmetric encryption 
         * @param aKey The key of the symmetric algorithm
         * @return Encoded ContentInfo for enveloped data
         * @throws CMSException If an error occurs while generating the envelope
         */
        public byte[] generate(byte[] aKey)
        {
            byte[] sifreliVeri = null;
            try
            {
                EnvelopeConfig.checkSymmetricAlgorithmIsSafe(mSymmetricAlgorithm);

                _kriptoOrtakIsler(mSymmetricAlgorithm, aKey);

                sifreliVeri = mSymmetricCrypto.doFinal(mSifrelenecek);

                if (mSymmetricAlgorithm.getMod() == Mod.GCM)
                {
                    mEnvelopeData.setMac(((ParamsWithGCMSpec)mSymmetricAlgParams).getTag());
                }

                _setEncryptedContent(sifreliVeri);

                _setEnvelopedData(mEnvelopeData);

                return mContentInfo.getEncoded();
            }
            catch (Exception e)
            {
                throw new CMSException("Error in generating enveloped data", e);
            }
        }


        private void _setEnvelopedData(IEnvelopeData aZarfVeri)
        {
            mContentInfo.setContent(aZarfVeri.getEncoded());
        }


        private void _setEncryptedContent(byte[] aSifreliIcerik)
        {
            if (aSifreliIcerik != null)
            {
                mEnvelopeData.getEncryptedContentInfo().setEncryptedContent(aSifreliIcerik);
            }
        }
    }
}
