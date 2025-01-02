package dev.esya.api.smartcard.dirak.aes;

import dev.esya.api.smartcard.dirak.CardTestUtil;
import dev.esya.api.smartcard.dirak.DirakTestConstants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class DirakAESKeyWrapTest {
    static final String PIN = "12345678";
    static final String basePath = new File("resources/unit-test-resources/crypto/aes_key_wrap").getAbsolutePath() + "/";

    SmartCard sc;
    long session;


    @Before
    public void setUp() throws PKCS11Exception, IOException {
        System.out.println("SetUp Starts");
        sc = new SmartCard(CardType.DIRAKHSM);
        long slot = CardTestUtil.getSlot(sc);
        session = sc.openSession(slot);
        sc.login(session, PIN);
        System.out.println("SetUp Ended");
    }

    @After
    public void cleanUp() throws Exception {
        System.out.println("Clean Up Starts");
        sc.logout(session);
        sc.closeSession(session);
        System.out.println("Clean Up Ended");
    }

    @Test
    public void testAESKeyWrapEnc128() throws Exception {
        testAESKeyWrapFromFile(basePath + "KW_AE_128.txt", PKCS11Constants.CKM_AES_KEY_WRAP, true);
    }

    @Test
    public void testAESKeyWrapEnc192() throws Exception {
        testAESKeyWrapFromFile(basePath + "KW_AE_192.txt", PKCS11Constants.CKM_AES_KEY_WRAP, true);
    }

    @Test
    public void testAESKeyWrapEnc256() throws Exception {
        testAESKeyWrapFromFile(basePath + "KW_AE_256.txt", PKCS11Constants.CKM_AES_KEY_WRAP, true);
    }

    @Test
    public void testAESKeyWrapDec128() throws Exception {
        testAESKeyWrapFromFile(basePath + "KW_AD_128.txt", PKCS11Constants.CKM_AES_KEY_WRAP, false);
    }

    @Test
    public void testAESKeyWrapDec192() throws Exception {
        testAESKeyWrapFromFile(basePath + "KW_AD_192.txt", PKCS11Constants.CKM_AES_KEY_WRAP, false);
    }

    @Test
    public void testAESKeyWrapDec256() throws Exception {
        testAESKeyWrapFromFile(basePath + "KW_AD_256.txt", PKCS11Constants.CKM_AES_KEY_WRAP, false);
    }

    @Test
    public void testAESKeyWrapPadEnc128() throws Exception {
        testAESKeyWrapFromFile(basePath + "KWP_AE_128.txt", PKCS11Constants.CKM_AES_KEY_WRAP_PAD, true);
    }

    @Test
    public void testAESKeyWrapPadEnc192() throws Exception {
        testAESKeyWrapFromFile(basePath + "KWP_AE_192.txt", PKCS11Constants.CKM_AES_KEY_WRAP_PAD, true);
    }

    @Test
    public void testAESKeyWrapPadEnc256() throws Exception {
        testAESKeyWrapFromFile(basePath + "KWP_AE_256.txt", PKCS11Constants.CKM_AES_KEY_WRAP_PAD, true);
    }

    @Test
    public void testAESKeyWrapPadDec128() throws Exception {
        testAESKeyWrapFromFile(basePath + "KWP_AD_128.txt", PKCS11Constants.CKM_AES_KEY_WRAP_PAD, false);
    }

    @Test
    public void testAESKeyWrapPadDec192() throws Exception {
        testAESKeyWrapFromFile(basePath + "KWP_AD_192.txt", PKCS11Constants.CKM_AES_KEY_WRAP_PAD, false);
    }

    @Test
    public void testAESKeyWrapPadDec256() throws Exception {
        testAESKeyWrapFromFile(basePath + "KWP_AD_256.txt", PKCS11Constants.CKM_AES_KEY_WRAP_PAD, false);
    }


    public void testAESKeyWrapFromFile(String fileName, long cipherAlg, boolean isEncryption) throws Exception {

        byte[] data;
        byte[] key;
        byte[] cipherText;
        boolean fail;

        System.out.println("AES Key Wrap File: " + fileName);

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String st;

        while((st=br.readLine())!= null){

            data = StringUtil.hexToByte("");
            key = StringUtil.hexToByte("");
            cipherText = StringUtil.hexToByte("");
            fail = false;

            if(st.startsWith("COUNT")){

                String count = st.split("=")[1].trim();

                while(true){
                    st = br.readLine();

                    if(st == null || st.isEmpty())
                        break;

                    st = st.replaceAll(" ", "");
                    String[] values = st.split("=");

                    if(values[0].equals("K") && values.length == 2){
                        key = StringUtil.hexToByte(values[1]);
                    }
                    else if(values[0].equals("C") && values.length == 2){
                        cipherText = StringUtil.hexToByte(values[1]);
                    }
                    else if(values[0].equals("P") && values.length == 2) {
                        data = StringUtil.hexToByte(values[1]);
                    }
                    else if(values[0].equals("FAIL")) {
                        fail = true;
                    }
                }

                if (isEncryption)
                    //Nist'te Encrypt test vectorlerinde fail test senaryosu yok bu nedenle parametre olarak decryptte aldık sadece!
                    testAESKeyWrapEncrypt(count, data, key,  cipherText, cipherAlg);

                else
                    testAESKeyWrapDecrypt(count, data, key, cipherText, fail, cipherAlg);
            }
        }
    }

    private void testAESKeyWrapDecrypt( String count, byte[] data, byte[] key,  byte[] cipherText, boolean mustBeFailed, long cipherAlg) throws PKCS11Exception, SmartCardException {


        System.out.println("AES Key Wrap Decrypt Test! " + count);
        String keyLabel = "AESKeyWrapTestKey_" + StringUtil.toHexString(RandomUtil.generateRandom(2)) + "_" + DirakTestConstants.osNameAndArch;

        SecretKeyTemplate secretKeyTemplate = new AESKeyTemplate(keyLabel);
        secretKeyTemplate.getAsImportTemplate(key);

        sc.importSecretKey(session, secretKeyTemplate);

        CK_MECHANISM mechanism = new CK_MECHANISM(cipherAlg);

        byte[] decryptData = null;
        try {
            decryptData = sc.decryptData(session, keyLabel, cipherText, mechanism);
        }
        catch (Exception e) {
            sc.deletePrivateObject(session, keyLabel);
            if (mustBeFailed == true) {
                System.out.println("Test passed!");
                return;
            }
            else {
                System.out.println("Test failed!" + e.toString());
                Assert.assertFalse(true);
                return;
            }
        }
        sc.deletePrivateObject(session, keyLabel);
        Assert.assertArrayEquals(data,  decryptData);

        if(mustBeFailed == true)
            System.out.println("Test failed! It must give error. but it finished successfully");
        else
            System.out.println("Test passed!");

    }

    private void testAESKeyWrapEncrypt(String count, byte[] data, byte[] key, byte[] expectedCT,  long cipherAlg) throws PKCS11Exception, SmartCardException {


        System.out.println("AES Key Wrap Encrypt Test! " + count);
        String keyLabel = "AESKeyWrapTestKey_" + StringUtil.toHexString(RandomUtil.generateRandom(2)) + "_" + DirakTestConstants.osNameAndArch;

        SecretKeyTemplate secretKeyTemplate = new AESKeyTemplate(keyLabel);
        secretKeyTemplate.getAsImportTemplate(key);

        sc.importSecretKey(session, secretKeyTemplate);

        CK_MECHANISM mechanism = new CK_MECHANISM(cipherAlg);

        byte[] cipherText = sc.encryptData(session, keyLabel, data, mechanism);

        sc.deletePrivateObject(session, keyLabel);
        Assert.assertArrayEquals(expectedCT,  cipherText);
        System.out.println("Test passed!");

    }

}
