using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.crypto.util
{
    public static class SignUtil
    {        
        public static byte[] sign(SignatureAlg aSignatureAlg, IAlgorithmParams aParams, byte[] aToBeSigned,
                                  PrivateKey aSignPrivateKey)
        {
            Signer signer = Crypto.getSigner(aSignatureAlg);
            signer.init(aSignPrivateKey, aParams);
            return signer.sign(aToBeSigned);
        }

        public static byte[] sign(SignatureAlg aSignatureAlg, byte[] aToBeSigned, PrivateKey aSignPrivateKey)
        {
            return sign(aSignatureAlg, null, aToBeSigned, aSignPrivateKey);
        }
        
        public static bool verify(SignatureAlg aSignatureAlg, IAlgorithmParams aParams, byte[] aToBeSigned,
                                  byte[] aSigned, PublicKey aSignPublicKey)
        {
            IVerifier verifier = Crypto.getVerifier(aSignatureAlg);
            verifier.init(aSignPublicKey, aParams);
            verifier.update(aToBeSigned);
            return verifier.verifySignature(aSigned);
        }

        public static bool verify(SignatureAlg aSignatureAlg, byte[] aToBeSigned, byte[] aSigned,
                                  PublicKey aSignPublicKey)
        {
            return verify(aSignatureAlg, null, aToBeSigned, aSigned, aSignPublicKey);
        }

        public static bool verify(SignatureAlg aSignatureAlg, byte[] aImzalanan, byte[] aImzali,
                                  ECertificate aCertificate)
        {
            return verify(aSignatureAlg, null, aImzalanan, aImzali,
                          new PublicKey(aCertificate.getSubjectPublicKeyInfo()));
        }

        public static bool verify(SignatureAlg aSignatureAlg, IAlgorithmParams aParams, byte[] aImzalanan,
                                  byte[] aImzali, ECertificate aCertificate)
        {
            return verify(aSignatureAlg, aParams, aImzalanan, aImzali,
                          new PublicKey(aCertificate.getSubjectPublicKeyInfo()));
        }
    }
}