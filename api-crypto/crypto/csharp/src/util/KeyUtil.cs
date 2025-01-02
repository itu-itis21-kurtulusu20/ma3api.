using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.util
{
    public static class KeyUtil
    {
        private static readonly Dictionary<IAlgorithm, int> msAlgoKeyLength = new Dictionary<IAlgorithm, int>();
        private static readonly Dictionary<CipherAlg, WrapAlg> msCipherAlgToWrapAlg = new Dictionary<CipherAlg, WrapAlg>();

        static KeyUtil()
        {
            msAlgoKeyLength[CipherAlg.AES128_CBC] = 128;
            msAlgoKeyLength[CipherAlg.AES128_CFB] = 128;
            msAlgoKeyLength[CipherAlg.AES128_OFB] = 128;
            msAlgoKeyLength[CipherAlg.AES128_ECB] = 128;
            msAlgoKeyLength[CipherAlg.AES128_GCM] = 128;
            msAlgoKeyLength[CipherAlg.AES192_CBC] = 192;
            msAlgoKeyLength[CipherAlg.AES192_CFB] = 192;
            msAlgoKeyLength[CipherAlg.AES192_OFB] = 192;
            msAlgoKeyLength[CipherAlg.AES192_ECB] = 192;
            msAlgoKeyLength[CipherAlg.AES192_GCM] = 192;
            msAlgoKeyLength[CipherAlg.AES256_CBC] = 256;
            msAlgoKeyLength[CipherAlg.AES256_CFB] = 256;
            msAlgoKeyLength[CipherAlg.AES256_OFB] = 256;
            msAlgoKeyLength[CipherAlg.AES256_ECB] = 256;
            msAlgoKeyLength[CipherAlg.AES256_GCM] = 256;
            msAlgoKeyLength[CipherAlg.RC2_CBC] = 128;    //It can be different.This is default value.
            msAlgoKeyLength[CipherAlg.DES_EDE3_CBC] = 192;
            msAlgoKeyLength[WrapAlg.AES128] = 128;
            msAlgoKeyLength[WrapAlg.AES192] = 192;
            msAlgoKeyLength[WrapAlg.AES256] = 256;

            msCipherAlgToWrapAlg[CipherAlg.AES128_CBC] = WrapAlg.AES128;
            msCipherAlgToWrapAlg[CipherAlg.AES128_CFB] = WrapAlg.AES128;
            msCipherAlgToWrapAlg[CipherAlg.AES128_OFB] = WrapAlg.AES128;
            msCipherAlgToWrapAlg[CipherAlg.AES128_ECB] = WrapAlg.AES128;
            msCipherAlgToWrapAlg[CipherAlg.AES192_CBC] = WrapAlg.AES192;
            msCipherAlgToWrapAlg[CipherAlg.AES192_CFB] = WrapAlg.AES192;
            msCipherAlgToWrapAlg[CipherAlg.AES192_OFB] = WrapAlg.AES192;
            msCipherAlgToWrapAlg[CipherAlg.AES192_ECB] = WrapAlg.AES192;
            msCipherAlgToWrapAlg[CipherAlg.AES256_CBC] = WrapAlg.AES256;
            msCipherAlgToWrapAlg[CipherAlg.AES256_CFB] = WrapAlg.AES256;
            msCipherAlgToWrapAlg[CipherAlg.AES256_OFB] = WrapAlg.AES256;
            msCipherAlgToWrapAlg[CipherAlg.AES256_ECB] = WrapAlg.AES256;
        }
        public static IPublicKey decodePublicKey(AsymmetricAlg aAsymmetricAlg, byte[] aBytes)
        {
            return Crypto.getKeyFactory().decodePublicKey(aAsymmetricAlg, aBytes);
        }

        public static IPublicKey decodePublicKey(ESubjectPublicKeyInfo aSubjectPublicKeyInfo)
        {
            AsymmetricAlg alg = AsymmetricAlg.fromOID(aSubjectPublicKeyInfo.getAlgorithm().getAlgorithm().mValue);

            return decodePublicKey(alg, aSubjectPublicKeyInfo.getBytes());
        }

        public static IPrivateKey decodePrivateKey(AsymmetricAlg aAsymmetricAlg, byte[] aBytes)
        {
            return Crypto.getKeyFactory().decodePrivateKey(aAsymmetricAlg, aBytes);
        }

        public static int getKeyLength(IPublicKey aKey)
        {
            IKeyFactory factory = Crypto.getKeyFactory();
            return factory.getKeyLength(aKey);
        }

        public static int getKeyLength(ECertificate certificate)
        {
            IPublicKey publicKey = KeyUtil.decodePublicKey(certificate.getSubjectPublicKeyInfo());
            return KeyUtil.getKeyLength(publicKey);
        }

        public static KeyPair generateKeyPair(AsymmetricAlg aAsymmetricAlg, int aLength)
        {
            //return Crypto.getKeyPairGenerator(aAsymmetricAlg).generateKeyPair(aLength);
            ParamsWithLength lengthParam = new ParamsWithLength(aLength);
            return Crypto.getKeyPairGenerator(aAsymmetricAlg).generateKeyPair(lengthParam);
        }
        public static ISecretKey generateSecretKey(IKeySpec aSpec)
        {
            return Crypto.getKeyFactory().generateSecretKey(aSpec);
        }

        public static byte[] generateKey(CipherAlg aAlg, int aBitLength)
        {
            return Crypto.getKeyFactory().generateKey(aAlg, aBitLength);
        }
        
        /**
         * @return required known key length in bits if applicable like some Cipher and Wrap algs.
         *  -1 if algo not known
         */
        public static int getKeyLength(IAlgorithm aCipheralg)
        {
            if (msAlgoKeyLength.ContainsKey(aCipheralg))
                return msAlgoKeyLength[aCipheralg];
            return -1;
        }

        public static WrapAlg getConvenientWrapAlg(CipherAlg aCipherAlg)
        {
            if (msCipherAlgToWrapAlg.ContainsKey(aCipherAlg))
                return msCipherAlgToWrapAlg[aCipherAlg];
            return null;
        }

        /*
        public static int getKeyLength(ECertificate certificate)
        {
            SignatureAlg publicKeyAlg = SignatureAlg.fromAlgorithmIdentifier(certificate.getPublicKeyAlgorithm()).getmObj1();
            int pubKeyLength = 0;
            if (publicKeyAlg.getAsymmetricAlg().Equals(AsymmetricAlg.RSA))
            {
                pubKeyLength = getRSAPublicKeyLength(certificate);
            }
            else if (publicKeyAlg.getAsymmetricAlg().Equals(AsymmetricAlg.ECDSA))
            {
                pubKeyLength = getECPublicKeyLength(certificate);
            }
            return pubKeyLength;
        }

        private static int getRSAPublicKeyLength(ECertificate certificate)
        {
            int pubKeyLength;
            ERSAPublicKey rsaPublicKey = new ERSAPublicKey(certificate.getSubjectPublicKeyInfo().getSubjectPublicKey());
            byte [] modulusBytes = rsaPublicKey.getModulus().mValue.GetData();

            if (modulusBytes.Length % 8 == 1 && modulusBytes[0] == 0)
            {
                pubKeyLength = (modulusBytes.Length - 1) * 8;
            }
            else if (modulusBytes.Length % 8 > 1 )
            {
                int byteCount = ((modulusBytes.Length + 8) / 8) * 8;
                pubKeyLength = byteCount * 8;
            }
            else
            {
                pubKeyLength = modulusBytes.Length * 8;
            }

            return pubKeyLength;
        }
        
        private static int getECPublicKeyLength(ECertificate signerCertificate)
        {
            EcpkParameters ecPublicKeyParameters = new EcpkParameters();
            UtilOpenType.fromOpenType(signerCertificate.getSubjectPublicKeyInfo().getAlgorithm().getParameters(), ecPublicKeyParameters);

            int pubKeyLength;
            if (ecPublicKeyParameters.ChoiceID == EcpkParameters._NAMEDCURVE)
            {
                Asn1ObjectIdentifier objectIdentifier = (Asn1ObjectIdentifier) ecPublicKeyParameters.GetElement();
                NamedCurve curve = NamedCurve.getCurveParametersFromOid(objectIdentifier);
                pubKeyLength = curve.getKeyLen();
            }
            else
            {
                pubKeyLength = 8 * (signerCertificate.asX509Certificate2().PublicKey.EncodedKeyValue.RawData.Length - 1) / 2;    
            }

            return pubKeyLength;
        }
		*/
    }
}
