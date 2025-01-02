package tr.gov.tubitak.uekae.esya.api.crypto;

import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.NSSCryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.NSSLoader;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplateFactory;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.HMACKeyTemplate;

import javax.crypto.*;
import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Provider;
import java.util.Arrays;

/**
 * @author ayetgin
 */
public class NSSSymmetricCipherTest extends TestCase
{
    private static String TEST_DATA =  "Hey, privacy is not a perfume!";
    private static NSSCryptoProvider NSS_PROVIDER;

    static {
        NSS_PROVIDER = NSSTestUtil.constructProvider(null);
    }

    public void testNSSAesEncrypAndDecrypt()throws Exception {
        Provider p = NSS_PROVIDER.getmProvider();
        KeyStore ks = KeyStore.getInstance("PKCS11", p);
        ks.load(null, "Test'!123456".toCharArray());
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", p);
        SecretKey sk = keyGenerator.generateKey();
        javax.crypto.Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding",p);
        cipher.init(Cipher.ENCRYPT_MODE,sk);
        byte[] value = cipher.doFinal("test".getBytes());
        System.out.println(Arrays.toString(value));
    }

    public void testAesEncrypAndDecrypt() throws Exception {
        Crypto.setProvider(NSS_PROVIDER);
        AlgorithmParams params = new ParamsWithIV(RandomUtil.generateRandom(16));
        String keyLabel = "EncKey";
        int keyLenght=256;
        KeyFactory kf = Crypto.getKeyFactory();
        tr.gov.tubitak.uekae.esya.api.crypto.params.SecretKeySpec secretKeySpec = new tr.gov.tubitak.uekae.esya.api.crypto.params.SecretKeySpec(CipherAlg.AES256_CBC,keyLabel,keyLenght);
        SecretKey secretKey = kf.generateSecretKey(secretKeySpec);
        KeyTemplate keyTemplate = KeyTemplateFactory.getKeyTemplate(keyLabel,secretKey);
        byte[] encrypted = null;
        byte[] decrypted = null;
        {
            BufferedCipher encryptor = Crypto.getEncryptor(CipherAlg.AES256_CBC);
            encryptor.init(keyTemplate,params);
            encrypted = encryptor.doFinal(TEST_DATA.getBytes());
        }
        {
            BufferedCipher decryptor = Crypto.getDecryptor(CipherAlg.AES256_CBC);
            decryptor.init(keyTemplate, params);
            decrypted = decryptor.doFinal(encrypted);
            System.out.println(new String(decrypted));
        }
        assertEquals(TEST_DATA, new String(decrypted));
    }
}
