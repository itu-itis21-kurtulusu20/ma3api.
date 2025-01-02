
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider
{
    public interface ICryptoProvider
    {
        Cipher getEncryptor(CipherAlg aCipherAlg);
        Cipher getDecryptor(CipherAlg aCipherAlg);
        Cipher getAsymmetricEncryptor(CipherAlg aCipherAlg);
        Cipher getAsymmetricDecryptor(CipherAlg aCipherAlg);
        IDigester getDigester(DigestAlg aDigestAlg);
        Signer getSigner(SignatureAlg aSignatureAlg);
        IVerifier getVerifier(SignatureAlg aSignatureAlg);
        //todo KeyFactory classina ihtiyaç olacak mı?
        IKeyFactory getKeyFactory();  //decode(Private/Public)Key işlemi ve generateSecretKey işlemi yapıyor SecretKey oluşturmayı biz yapmadığımız için sadece parametrelerini vermek (password, salt, iteration count) yeterli olacağından gerek duymadık, java implementasyonunda PBEKey oluşturuluyor, Biz oluşturmuyor sadece parametrelerini veriyoruz
        IMAC getMAC(MACAlg aMACAlg);
        IWrapper getWrapper(WrapAlg aWrapAlg);
        IWrapper getUnwrapper(WrapAlg aWrapAlg);
        IKeyAgreement getKeyAgreement(KeyAgreementAlg aKeyAgreementAlg);
        IKeyPairGenerator getKeyPairGenerator(AsymmetricAlg aAsymmetricAlg);
        IRandomGenerator getRandomGenerator();
        IPfxParser getPfxParser();
    }
}
