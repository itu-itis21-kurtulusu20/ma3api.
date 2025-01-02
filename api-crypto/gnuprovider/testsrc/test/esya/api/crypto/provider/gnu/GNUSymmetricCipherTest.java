package test.esya.api.crypto.provider.gnu;

import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;

import java.util.Arrays;

/**
 * @author ayetgin
 */
public class GNUSymmetricCipherTest extends TestCase
{
    private static String TEST_DATA =  "Hey, privacy is not a perfume!";

    static {
        try {
            Crypto.setProvider(Crypto.PROVIDER_GNU);
        } catch (Throwable t){
            t.printStackTrace();
        }
    }

    public void aes(byte[] iv) throws Exception {
        byte[] secretKey = new byte[32];
        AlgorithmParams params = new ParamsWithIV(iv);

        BufferedCipher cipher = Crypto.getEncryptor(CipherAlg.AES256_CBC);

        cipher.init(secretKey, params);


        byte[] encrypted = cipher.doFinal(TEST_DATA.getBytes());

        byte[] decrypted = CipherUtil.decrypt(CipherAlg.AES256_CBC, params, encrypted, secretKey);

        assertEquals(TEST_DATA, new String(decrypted));
    }

    public void testAESNoIV() throws Exception {
        aes(null);
    }

    public void testAESWithIV() throws Exception {
        aes(RandomUtil.generateRandom(16));
    }

    public void testAesReset() throws Exception {
        byte[] secretKey = new byte[32];
        AlgorithmParams params = new ParamsWithIV(RandomUtil.generateRandom(16));

        BufferedCipher cipher = Crypto.getEncryptor(CipherAlg.AES256_CBC);

        cipher.init(secretKey, params);


        byte[] encrypted = cipher.doFinal(TEST_DATA.getBytes());

        cipher.reset();
        byte[] encryptedAgain = cipher.doFinal(TEST_DATA.getBytes());

        assertTrue(Arrays.equals(encrypted, encryptedAgain));

        BufferedCipher decryptor = Crypto.getDecryptor(CipherAlg.AES256_CBC);
        decryptor.init(secretKey, params);

        byte[] decrypted = decryptor.doFinal(encrypted);
        decryptor.reset();
        byte[] decryptedAgain = decryptor.doFinal(encrypted);
        assertTrue(Arrays.equals(decrypted, decryptedAgain));

        assertEquals(TEST_DATA, new String(decrypted));
    }


}
