
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using AbstractCipher = tr.gov.tubitak.uekae.esya.api.crypto.Cipher;
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncyCryptoProvider : ICryptoProvider
    {
        public AbstractCipher getEncryptor(CipherAlg aCipherAlg)
        {
            return new BouncyEncryptor(aCipherAlg);
        }

        public AbstractCipher getDecryptor(CipherAlg aCipherAlg)
        {
            return new BouncyDecryptor(aCipherAlg);
        }

        public AbstractCipher getAsymmetricEncryptor(CipherAlg aCipherAlg)
        {
            return new BouncyAsymmetricEncryptor(aCipherAlg);
        }

        public AbstractCipher getAsymmetricDecryptor(CipherAlg aCipherAlg)
        {
            return new BouncyAsymmetricDecryptor(aCipherAlg);
        }

        public IDigester getDigester(DigestAlg aDigestAlg)
        {
            return new BouncyDigester(aDigestAlg);
        }

        public Signer getSigner(SignatureAlg aSignatureAlg)
        {
            return new BouncySigner(aSignatureAlg);
        }

        public IVerifier getVerifier(SignatureAlg aSignatureAlg)
        {
            return new BouncyVerifier(aSignatureAlg);
        }
        public IMAC getMAC(MACAlg aMACAlg)
        {
            return new BouncyMAC(aMACAlg);
        }

        public IWrapper getWrapper(WrapAlg aWrapAlg)
        {
            return new BouncyWrapper(aWrapAlg, true);
        }

        public IWrapper getUnwrapper(WrapAlg aWrapAlg)
        {
            return new BouncyWrapper(aWrapAlg, false);
        }
        //todo KeyFactory class'ina gerek kalacak mı?
        public IKeyFactory getKeyFactory()
        {
            return new BouncyKeyFactory();
        }
        public IKeyAgreement getKeyAgreement(KeyAgreementAlg aKeyAgreementAlg)
        {
            return new BouncyKeyAgreement(aKeyAgreementAlg);
        }
        public IKeyPairGenerator getKeyPairGenerator(AsymmetricAlg aAsymmetricAlg)
        {
            return new BouncyKeyPairGenerator(aAsymmetricAlg);
        }

        public IRandomGenerator getRandomGenerator()
        {
            return new BouncyRandomGenerator();
        }

        public IPfxParser getPfxParser()
        {
            return new BouncyPfxParser();
        }
    }
}
