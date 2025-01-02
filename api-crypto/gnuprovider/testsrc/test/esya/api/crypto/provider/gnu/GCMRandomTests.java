package test.esya.api.crypto.provider.gnu;

import org.junit.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithGCMSpec;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by sura.emanet on 25.05.2018.
 */
public class GCMRandomTests {

    @Test
    public void randomTests() {
        Random rng = new Random();
        rng.setSeed(System.nanoTime());
        double counter = 0;

        try {
            for(int i = 0; i<10; i++){
                randomTests(rng);
                System.out.println("Counter:" + counter);
                counter++;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void randomTests(Random rng) throws Exception {

        int keyLength = 16;
        byte[] key = new byte[keyLength];
        rng.nextBytes(key);

        int pLength = rng.nextInt(65000);
        byte[] data = new byte[pLength];
        rng.nextBytes(data);

        int aLength = rng.nextInt(256);
        byte[] aad = new byte[aLength];
        rng.nextBytes(aad);

        int ivLength = 1 + (rng.nextInt(256));
        byte[] iv = new byte[ivLength];
        rng.nextBytes(iv);

        testGCM(aad, data, key, iv, CipherAlg.AES128_GCM);
    }

    public void testGCM(byte [] aad, byte [] plaintext, byte [] key, byte [] iv, CipherAlg cipherAlg) throws Exception
    {
//        System.out.println("AAD        : " +  StringUtil.toString(aad));
//        System.out.println("plaintext  : " +  StringUtil.toString(plaintext));
//        System.out.println("key        : " +  StringUtil.toString(key));
//        System.out.println("iv         : " +  StringUtil.toString(iv));

        InputStream aadStream = new ByteArrayInputStream(aad);
        ParamsWithGCMSpec params = new ParamsWithGCMSpec(iv, aadStream);

        BufferedCipher encryptor = Crypto.getEncryptor(cipherAlg);
        encryptor.init(key, params);

        byte[] encrypted = encryptor.doFinal(plaintext);
        byte[] tag = params.getTag();

//        System.out.println("Tag: " + StringUtil.toString(tag));
//        System.out.println("EncryptedText: "+ StringUtil.toString(encrypted));
//        System.out.println("******************************");

        InputStream aadStreamDec = new ByteArrayInputStream(aad);
        BufferedCipher decryptor = Crypto.getDecryptor(cipherAlg);

        decryptor.init(key, new ParamsWithGCMSpec(iv,aadStreamDec,tag));
        byte[] decrypted = decryptor.doFinal(encrypted);

//        System.out.println("DecryptedText: "+ StringUtil.toString(decrypted));
        Assert.assertArrayEquals(decrypted, plaintext);
    }

}
