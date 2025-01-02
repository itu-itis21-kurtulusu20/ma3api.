package dev.esya.api.smartcard.dirak.aes;

import dev.esya.api.smartcard.dirak.CardTestUtil;
import dev.esya.api.smartcard.dirak.DirakTestConstants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_GCM_PARAMS;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteUtil;
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

public class DirakAESGCMNistTest {

    static final String PIN = "12345678";

    static final String basePath = new File("resources/unit-test-resources/crypto/gcm").getAbsolutePath() + "/";

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

    /**
     * NIST CSRC Cryptography Toolkit Test Vectors
     * https://github.com/coruus/nist-testvectors/tree/master/csrc.nist.gov/groups/STM/cavp/documents/mac/gcmtestvectors
     */

    @Test
    public void testGCMEnc128() throws Exception {
        testGCMFromFile(DirakAESGCMNistTest.basePath + "gcmEncryptExtIV256.rsp", true);
    }

    @Test
    public void testGCMDecrypt128() throws Exception {
        testGCMFromFile(DirakAESGCMNistTest.basePath + "gcmDecrypt128.rsp", false);
    }

    @Test
    public void testGCMEnc192() throws Exception {
        testGCMFromFile(DirakAESGCMNistTest.basePath + "gcmEncryptExtIV192.rsp", true);
    }

    @Test
    public void testGCMDecrypt192() throws Exception {
        testGCMFromFile(DirakAESGCMNistTest.basePath + "gcmDecrypt192.rsp", false);
    }

    @Test
    public void testGCMEnc256() throws Exception {
        testGCMFromFile(DirakAESGCMNistTest.basePath + "gcmEncryptExtIV256.rsp", true);
    }

    @Test
    public void testGCMDecrypt256() throws Exception {
        testGCMFromFile(DirakAESGCMNistTest.basePath + "gcmDecrypt256.rsp", false);
    }


    public void testGCMFromFile(String fileName, boolean isEncryption) throws Exception {

        byte[] data;
        byte[] key;
        byte[] iv;
        byte[] aad;
        byte[] cipherText;
        byte[] tag;
        boolean fail;

        //File file = new File(fileName);

        System.out.println("AES GCM Test File: " + fileName);

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String st;

        while((st=br.readLine())!= null){

            data = StringUtil.hexToByte("");
            key = StringUtil.hexToByte("");
            iv = StringUtil.hexToByte("");
            aad = StringUtil.hexToByte("");
            cipherText = StringUtil.hexToByte("");
            tag = StringUtil.hexToByte("");
            fail = false;

            if(st.startsWith("Count")){

                String count = st.split("=")[1].trim();

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
                        cipherText = StringUtil.hexToByte(values[1]);
                    }
                    else if(values[0].equals("AAD") && values.length == 2){
                        aad = StringUtil.hexToByte(values[1]);
                    }
                    else if(values[0].equals("Tag") && values.length == 2){
                        tag = StringUtil.hexToByte(values[1]);
                    }
                    else if(values[0].equals("PT") && values.length == 2) {
                        data = StringUtil.hexToByte(values[1]);
                    }
                    else if(values[0].equals("FAIL")) {
                        fail = true;
                    }
                }

                if (isEncryption)
                    //Nist'te Encrypt test vectorlerinde fail test senaryosu yok bu nedenle parametre olarak decryptte aldık sadece!
                    testGCMEncrypt(count, aad, data, key, iv, cipherText, tag);
                else
                    testGCMDecrypt(count, aad, data, key, iv, cipherText, tag, fail);
            }
        }
    }

    private void testGCMDecrypt(String count, byte[] aad, byte[] data, byte[] key, byte[] iv, byte[] cipherText, byte[] tag, boolean mustBeFailed) throws PKCS11Exception, SmartCardException {
        if(tag.length == 4) {
            System.out.println("Unsupported tag length: " + tag.length);
            return;
        }
        byte[] encryptedData = ByteUtil.concatAll(cipherText, tag);

        String keyLabel = "GCMTestKey_" + StringUtil.toHexString(RandomUtil.generateRandom(2)) + "_" + DirakTestConstants.osNameAndArch;

        SecretKeyTemplate secretKeyTemplate = new AESKeyTemplate(keyLabel);
        secretKeyTemplate.getAsImportTemplate(key);

        sc.importSecretKey(session, secretKeyTemplate);
        secretKeyTemplate.getAsImportTemplate(key);

        CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_GCM);
        CK_GCM_PARAMS mechParams = new CK_GCM_PARAMS(tag.length * 8, iv, aad);
        mechanism.pParameter = mechParams;


        byte[] decryptData = null;
        try {
            decryptData = sc.decryptData(session, keyLabel, encryptedData, mechanism);
            Assert.assertArrayEquals(data,  decryptData);
            System.out.println("Test passed!");
        }
        catch (Exception e) {
            if (mustBeFailed == true) {
                System.out.println("Test passed!");
            }
            else {
                System.out.println("Test failed!" + e.toString());
                Assert.assertFalse(true);
            }
        } finally {
            sc.deletePrivateObject(session, keyLabel);
        }
    }

    private void testGCMEncrypt(String count, byte[] aad, byte[] data, byte[] key, byte[] iv, byte[] expectedCT, byte[] expectedTag) throws PKCS11Exception, SmartCardException {

        if(expectedTag.length == 4) {
            System.out.println("Unsupported tag length");
            return;
        }
        String keyLabel = "GCMTestKey_" + StringUtil.toHexString(RandomUtil.generateRandom(2)) + "_" + DirakTestConstants.osNameAndArch;

        try {
            SecretKeyTemplate secretKeyTemplate = new AESKeyTemplate(keyLabel);
            secretKeyTemplate.getAsImportTemplate(key);

            sc.importSecretKey(session, secretKeyTemplate);

            CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_GCM);
            CK_GCM_PARAMS mechParams = new CK_GCM_PARAMS(expectedTag.length * 8, iv, aad);
            mechanism.pParameter = mechParams;

            byte[] cipherTextWithTag = sc.encryptData(session, keyLabel, data, mechanism);
            byte[] expectedCipherTextWithTag = ByteUtil.concatAll(expectedCT, expectedTag);

            Assert.assertArrayEquals(expectedCipherTextWithTag,  cipherTextWithTag);
        } finally {
            sc.deletePrivateObject(session, keyLabel);
        }

        System.out.println("Test passed!");
    }



}
