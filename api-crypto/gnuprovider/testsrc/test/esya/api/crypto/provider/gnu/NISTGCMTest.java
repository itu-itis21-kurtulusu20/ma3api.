package test.esya.api.crypto.provider.gnu;

import org.junit.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithGCMSpec;

import java.io.*;
import java.util.Arrays;

/**
 * Created by sura.emanet on 23.05.2018.
 */
public class NISTGCMTest {

    /**
     * NIST CSRC Cryptography Toolkit Test Vectors
     * https://github.com/coruus/nist-testvectors/tree/master/csrc.nist.gov/groups/STM/cavp/documents/mac/gcmtestvectors
     */

    @Test
    public void testGCM() throws Exception {
        testGCMFromFile("T:\\api-parent\\resources\\unit-test-resources\\crypto\\gcm\\gcmEncryptExtIV128.rsp", CipherAlg.AES128_GCM, true);
    }

    @Test
    public void testGCMDecrypt() throws Exception {
        testGCMFromFile("T:\\api-parent\\resources\\unit-test-resources\\crypto\\gcm\\gcmDecrypt128.rsp", CipherAlg.AES128_GCM, false);
    }

    public void testGCMFromFile(String fileName, CipherAlg cipherAlg, boolean isEncryption) throws Exception {

        byte[] data;
        byte[] key;
        byte[] iv;
        byte[] aad;
        byte[] expectedCT;
        byte[] expectedTag;
        boolean fail;

        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;

        while((st=br.readLine())!= null){

            data = StringUtil.hexToByte("");
            key = StringUtil.hexToByte("");
            iv = StringUtil.hexToByte("");
            aad = StringUtil.hexToByte("");
            expectedCT = StringUtil.hexToByte("");
            expectedTag = StringUtil.hexToByte("");
            fail = false;

            if(st.startsWith("Count")){
                while(true){
                    st = br.readLine();

                    if(st == null || st.isEmpty())
                        break;

                    st = st.replaceAll(" ", "");
                    String[] values = st.split("=");

                    if(values[0].equals("Key") && values.length == 2){
                        key = StringUtil.hexToByte(values[1]);
                    }
                    else if(values[0].equals("IV") && values.length == 2){
                        iv = StringUtil.hexToByte(values[1]);
                    }
                    else if(values[0].equals("CT") && values.length == 2){
                        expectedCT = StringUtil.hexToByte(values[1]);
                    }
                    else if(values[0].equals("AAD") && values.length == 2){
                        aad = StringUtil.hexToByte(values[1]);
                    }
                    else if(values[0].equals("Tag") && values.length == 2){
                        expectedTag = StringUtil.hexToByte(values[1]);
                    }
                    else if(values[0].equals("PT") && values.length == 2) {
                        data = StringUtil.hexToByte(values[1]);
                    }
                    else if(values[0].equals("FAIL")) {
                        fail = true;
                    }
                }

                 if(isEncryption)
                     testGCM(aad, data, key, iv, expectedCT, expectedTag, fail, cipherAlg);
                 else
                     testGCMDecrypt(aad, data, key, iv, expectedCT, expectedTag, fail, cipherAlg);
            }
        }
    }

    public void testGCM(byte [] aad, byte [] plaintext, byte [] key, byte [] iv, byte []expectedCT, byte []expectedTag, boolean fail, CipherAlg cipherAlg) throws Exception
    {
//        System.out.println("AAD        : " +  StringUtil.toString(aad));
//        System.out.println("plaintext  : " +  StringUtil.toString(plaintext));
//        System.out.println("key        : " +  StringUtil.toString(key));
//        System.out.println("iv         : " +  StringUtil.toString(iv));
//        System.out.println("expectedCT : " +  StringUtil.toString(expectedCT));
//        System.out.println("expectedTag: " +  StringUtil.toString(expectedTag));
//        System.out.println("******************************");

        InputStream aadStream = new ByteArrayInputStream(aad);
        ParamsWithGCMSpec params = new ParamsWithGCMSpec(iv, aadStream);

        BufferedCipher encryptor = Crypto.getEncryptor(cipherAlg);
        encryptor.init(key, params);

        byte[] encrypted = encryptor.doFinal(plaintext);
        byte[] tag = params.getTag();

        Assert.assertArrayEquals(expectedTag, Arrays.copyOfRange(tag,0,expectedTag.length));

//        System.out.println("ExpectedText: "+ StringUtil.toString(encrypted));
        Assert.assertArrayEquals(expectedCT,encrypted);

        InputStream aadStreamDec = new ByteArrayInputStream(aad);

        BufferedCipher decryptor = Crypto.getDecryptor(cipherAlg);
        decryptor.init(key, new ParamsWithGCMSpec(iv,aadStreamDec,tag));
        byte[] decrypted = decryptor.doFinal(encrypted);

//        System.out.println("DecryptedText: "+ StringUtil.toString(decrypted));
        Assert.assertArrayEquals(decrypted, plaintext);
    }

    public void testGCMDecrypt(byte [] aad, byte [] plaintext, byte [] key, byte [] iv, byte [] CT, byte []expectedTag, boolean fail, CipherAlg cipherAlg) throws Exception
    {
//        System.out.println("AAD        : " +  StringUtil.toString(aad));
//        System.out.println("plaintext  : " +  StringUtil.toString(plaintext));
//        System.out.println("key        : " +  StringUtil.toString(key));
//        System.out.println("iv         : " +  StringUtil.toString(iv));
//        System.out.println("expectedCT : " +  StringUtil.toString(expectedCT));
//        System.out.println("expectedTag: " +  StringUtil.toString(expectedTag));
//        System.out.println("******************************");

        InputStream aadStreamDec = new ByteArrayInputStream(aad);
        BufferedCipher decryptor = Crypto.getDecryptor(cipherAlg);

        decryptor.init(key, new ParamsWithGCMSpec(iv,aadStreamDec,expectedTag));

        try
        {
            byte[] decrypted = decryptor.doFinal(CT);
             Assert.assertArrayEquals(plaintext,decrypted);
        }
        catch (CryptoException e)
        {
            if(fail)
                Assert.assertEquals("Tags are not matched..",e.getCause().getMessage());
            else
                throw e;
        }
    }
}
