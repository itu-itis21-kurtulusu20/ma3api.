using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.provider;

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    /**
     * Central class for Crypto operations.
     *
     * @author ayetgin
     */
    public static class Crypto
    {
        private static readonly ILog _logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        public static readonly string PROVIDER_BOUNCY = "tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy.BouncyCryptoProvider";
        public static readonly string ASSEMBLY_NAME = "ma3api-crypto-bouncyprovider";

        private static ICryptoProvider _mProvider;


        static Crypto()
        {
            // todo make configurative
            //setProvider(new BouncyCryptoProvider());
            try
            {
                // todo make configurative
                setProvider(PROVIDER_BOUNCY);
            }
            catch (Exception x)
            {
                _logger.Error("Cant init crypto!", x);
            }

        }
        /**
         * Set Crypto provider with crypto provider
         * @param aProvider
         */
        public static void setProvider(ICryptoProvider aProvider)
        {
            _mProvider = aProvider;
        }
        /**
         * Set Crypto provider with Fully Qualified ClassName
         * @param aFullyQualifiedClassName
         */
        public static void setProvider(String aFullyQualifiedClassName)
        {
            try
            {
                //mProvider = (ICryptoProvider)Class.forName(aFullyQualifiedClassName).newInstance();
                //mProvider = (ICryptoProvider)Activator.CreateInstance(Type.GetType(aFullyQualifiedClassName));
                _mProvider = (ICryptoProvider)Activator.CreateInstance(ASSEMBLY_NAME, aFullyQualifiedClassName).Unwrap();
            }
            catch (Exception e)
            {
                throw new CryptoException("Cant set crypto provider : " + aFullyQualifiedClassName, e);
            }
        }

        public static IDigester getDigester(DigestAlg aDigestAlg)
        {
            return _mProvider.getDigester(aDigestAlg);
        }

        public static BufferedCipher getEncryptor(CipherAlg aSymmetricAlg)
        {
            return new BufferedCipher(_mProvider.getEncryptor(aSymmetricAlg));
        }

        public static BufferedCipher getDecryptor(CipherAlg aSymmetricAlg)
        {
            return new BufferedCipher(_mProvider.getDecryptor(aSymmetricAlg));
        }

        public static BufferedCipher getAsymmetricEncryptor(CipherAlg aAsymmetricAlg)
        {
            return new BufferedCipher(_mProvider.getAsymmetricEncryptor(aAsymmetricAlg));
        }

        public static BufferedCipher getAsymmetricDecryptor(CipherAlg aAsymmetricAlg)
        {
            return new BufferedCipher(_mProvider.getAsymmetricDecryptor(aAsymmetricAlg));
        }

        public static Signer getSigner(SignatureAlg aSignatureAlg)
        {
            return _mProvider.getSigner(aSignatureAlg);
        }

        public static IVerifier getVerifier(SignatureAlg aSignatureAlg)
        {
            return _mProvider.getVerifier(aSignatureAlg);
        }

        public static IMAC getMAC(MACAlg aMACAlg)
        {
            return _mProvider.getMAC(aMACAlg);
        }

        public static IWrapper getWrapper(WrapAlg aWrapAlg)
        {
            return _mProvider.getWrapper(aWrapAlg);
        }

        public static IWrapper getUnwrapper(WrapAlg aWrapAlg)
        {
            return _mProvider.getUnwrapper(aWrapAlg);
        }


        //todo KeyFactory classina ihtiyac var mi?
        public static IKeyFactory getKeyFactory()
        {
            return _mProvider.getKeyFactory();
        }

        public static IKeyAgreement getKeyAgreement(KeyAgreementAlg aKeyAgreementAlg)
        {
            return _mProvider.getKeyAgreement(aKeyAgreementAlg);
        }

        public static IKeyPairGenerator getKeyPairGenerator(AsymmetricAlg aAsymmetricAlg)
        {
            return _mProvider.getKeyPairGenerator(aAsymmetricAlg);
        }

        public static IRandomGenerator getRandomGenerator()
        {
            return _mProvider.getRandomGenerator();
        }

        public static IPfxParser getPfxParser()
        {
            return _mProvider.getPfxParser();
        }
    }
}
