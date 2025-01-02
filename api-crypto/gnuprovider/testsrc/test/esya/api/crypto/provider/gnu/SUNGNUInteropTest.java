package test.esya.api.crypto.provider.gnu;

import org.junit.AfterClass;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * @author ayetgin
 */
public class SUNGNUInteropTest
{

    private static String TEST_DATA =  "Hey, privacy is not a perfume!";


    @AfterClass
    public static void afterClass() throws CryptoException {
        Crypto.setProvider(Crypto.PROVIDER_GNU);
    }

    @Test
    public void testCipherInterop() throws Exception {
        byte[] secretKey = new byte[16];
        AlgorithmParams params = new ParamsWithIV(RandomUtil.generateRandom(16));
        CipherAlg alg = CipherAlg.AES128_CBC;

        // gnu encrypt / sun decrypt
        Crypto.setProvider(Crypto.PROVIDER_GNU);

        byte[] encrypted = CipherUtil.encrypt(alg, params, TEST_DATA.getBytes(), secretKey);

        Crypto.setProvider(Crypto.PROVIDER_SUN);

        byte[] decrypted = CipherUtil.decrypt(alg, params, encrypted, secretKey);

        assertEquals(TEST_DATA, new String(decrypted));

        // sun encrypt / gnu decrypt
        Crypto.setProvider(Crypto.PROVIDER_SUN);

        encrypted = CipherUtil.encrypt(alg, params, TEST_DATA.getBytes(), secretKey);

        Crypto.setProvider(Crypto.PROVIDER_GNU);

        decrypted = CipherUtil.decrypt(alg, params, encrypted, secretKey);

        assertEquals(TEST_DATA, new String(decrypted));
    }

    @Test
    public void testAsymetricCipherInterop() throws Exception {
        AlgorithmParams params = null;

        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 1024);

        byte[] encodedPrivate  = keyPair.getPrivate().getEncoded();
        byte[] encodedPublic   = keyPair.getPublic().getEncoded();

        CipherAlg alg = CipherAlg.RSA_ECB_PKCS1;

        // gnu encrypt / sun decrypt
        Crypto.setProvider(Crypto.PROVIDER_GNU);

        byte[] encrypted = CipherUtil.encrypt(alg, params, TEST_DATA.getBytes(), keyPair.getPublic());

        Crypto.setProvider(Crypto.PROVIDER_SUN);

        PrivateKey sunPrivateKey = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, encodedPrivate);
        byte[] decrypted = CipherUtil.decrypt(alg, params, encrypted, sunPrivateKey);

        assertEquals(TEST_DATA, new String(decrypted));

        // sun encrypt / gnu decrypt
        Crypto.setProvider(Crypto.PROVIDER_SUN);

        PublicKey sunPublicKey= KeyUtil.decodePublicKey(AsymmetricAlg.RSA, encodedPublic);
        encrypted = CipherUtil.encrypt(alg, params, TEST_DATA.getBytes(), sunPublicKey);

        Crypto.setProvider(Crypto.PROVIDER_GNU);

        decrypted = CipherUtil.decrypt(alg, params, encrypted, keyPair.getPrivate());

        assertEquals(TEST_DATA, new String(decrypted));
    }

    @Test
    public void testsignatureInterop() throws Exception
    {

        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 1024);

        byte[] encodedPrivate  = keyPair.getPrivate().getEncoded();
        byte[] encodedPublic   = keyPair.getPublic().getEncoded();

        SignatureAlg alg = SignatureAlg.RSA_SHA256;

        // gnu encrypt / sun decrypt
        Crypto.setProvider(Crypto.PROVIDER_GNU);

        byte[] signed = SignUtil.sign(alg, TEST_DATA.getBytes(), keyPair.getPrivate());

        Crypto.setProvider(Crypto.PROVIDER_SUN);

        PublicKey sunPublicKey= KeyUtil.decodePublicKey(AsymmetricAlg.RSA, encodedPublic);
        PrivateKey sunPrivateKey = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, encodedPrivate);

        boolean verified = SignUtil.verify(alg, TEST_DATA.getBytes(), signed, sunPublicKey);

        assertTrue(verified);

        // sun encrypt / gnu decrypt
        Crypto.setProvider(Crypto.PROVIDER_SUN);

        signed = SignUtil.sign(alg, TEST_DATA.getBytes(), sunPrivateKey);

        Crypto.setProvider(Crypto.PROVIDER_GNU);

        verified = SignUtil.verify(alg, TEST_DATA.getBytes(), signed, keyPair.getPublic());

        assertTrue(verified);
    }

}
