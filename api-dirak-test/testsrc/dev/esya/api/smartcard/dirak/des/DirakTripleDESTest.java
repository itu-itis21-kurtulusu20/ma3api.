package dev.esya.api.smartcard.dirak.des;

import dev.esya.api.smartcard.dirak.CardTestUtil;
import gnu.crypto.cipher.TripleDES;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.DES3KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ConstantsUtil;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class DirakTripleDESTest {

    static final String PASSWORD = "12345678";
    static final int keySize = 24;

    SmartCard sc = null;
    long sid = 0;
    long slotNo = 0;

    CipherAlg cipherAlg;
    int testDataLength;
    int ivLength;

    @Parameterized.Parameters(name = "{0}, {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {CipherAlg.DES_EDE3_CBC_NOPADDING, 1024,           8},
            {CipherAlg.DES_EDE3_CBC_NOPADDING, 1024 * 128,     8},

            {CipherAlg.DES_EDE3_CBC,           1000,           8},
            {CipherAlg.DES_EDE3_CBC,           1024,           8},
            {CipherAlg.DES_EDE3_CBC,           1024 * 128,     8},
            {CipherAlg.DES_EDE3_CBC,           1024 * 127 + 1, 8},

            {CipherAlg.DES_EDE3_ECB_NOPADDING, 1024,           0},
         // {CipherAlg.DES_EDE3_ECB_NOPADDING, 1024 * 128,     0},

            {CipherAlg.DES_EDE3_ECB,           1000,           0},
            {CipherAlg.DES_EDE3_ECB,           1024,           0},
         // {CipherAlg.DES_EDE3_ECB,           1024 * 128,     0},
         // {CipherAlg.DES_EDE3_ECB,           1024 * 127 + 1, 0}
        });
    }

    public DirakTripleDESTest(CipherAlg cipherAlg, int testDataLength, int ivLength) {
        this.cipherAlg = cipherAlg;
        this.testDataLength = testDataLength;
        this.ivLength = ivLength;
    }

    @Before
    public void setUpClass() throws Exception {
        sc = new SmartCard(CardType.DIRAKHSM);
        slotNo = CardTestUtil.getSlot(sc);
        sid = sc.openSession(slotNo);
        sc.login(sid, PASSWORD);
    }

    @After
    public void cleanUp() throws Exception {
        sc.logout(sid);
        sc.closeSession(sid);
    }

    static void fixDESParity(byte[] kb, int offset) {
        for (int i = 0; i < 8; ++i) {
            int a = kb[offset] & 254;
            a |= Integer.bitCount(a) & 1 ^ 1;
            kb[offset++] = (byte) a;
        }
    }

    static void fixTripleDESParity(byte[] kb, int offset) {
        fixDESParity(kb, offset);
        fixDESParity(kb, offset + 8);
        fixDESParity(kb, offset + 16);
    }

    // ----------------------------------------------------------------------------------------------------------------

    @Test
    public void test_TripleDES_Create_1() throws Exception {
        test_TripleDES_1(
            sc,
            testDataLength,
            true,
            cipherAlg,
            ivLength
        );
    }

    @Test
    public void test_TripleDES_Import_1() throws Exception {
        test_TripleDES_1(
            sc,
            testDataLength,
            false,
            cipherAlg,
            ivLength
        );
    }

    // ----------------------------------------------------------------------------------------------------------------

    public void test_TripleDES_1(SmartCard sc, int dataLen, boolean createKey, CipherAlg cipherAlg, int randomIVLength) throws Exception {
        byte[] iv = randomIVLength == 0 ? null : RandomUtil.generateRandom(randomIVLength);
        test_TripleDES_1(sc, dataLen, createKey, cipherAlg, iv);
    }

    public void test_TripleDES_1(SmartCard sc, int dataLen, boolean createKey, CipherAlg cipherAlg, byte[] iv) throws Exception {
        String keyLabel = "des3_" + System.currentTimeMillis();

        boolean keyCreated = false;
        long mechanismNumber = ConstantsUtil.convertSymmetricAlgToPKCS11Constant(cipherAlg.getName());

        try {
            DES3KeyTemplate des3KeyTemplate = new DES3KeyTemplate(keyLabel);
            des3KeyTemplate.getAsTokenTemplate(false, true, false);

            byte[] keyBytes = null;

            long secretKeyId;
            if (createKey) {
                secretKeyId = sc.createSecretKey(sid, des3KeyTemplate);
            } else {
                keyBytes = RandomUtil.generateRandom(keySize);
                if (!TripleDES.isParityAdjusted(keyBytes, 0)) {
                    fixTripleDESParity(keyBytes, 0);
                }
                des3KeyTemplate.getAsImportTemplate(keyBytes);
                secretKeyId = sc.importSecretKey(sid, des3KeyTemplate);
            }
            keyCreated = true;

            try {
                CK_ATTRIBUTE[] template = new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE)};
                sc.getAttributeValue(sid, secretKeyId, template);

                throw new SmartCardException("Key value is readable- is expected not to be readable");
            } catch (PKCS11Exception e) {
                if (e.getErrorCode() != PKCS11Constants.CKR_ATTRIBUTE_SENSITIVE) {
                    throw e;
                }
            }

            byte[] dataToBeEncrypted = RandomUtil.generateRandom(dataLen);
            byte[] dataToBeEncryptedPadded;

            // PKCS11 Pad'li ECB'i desteklemediği için kendimiz PKCS7 pad yaptık.
            if (cipherAlg == CipherAlg.DES_EDE3_ECB) {
                dataToBeEncryptedPadded = CardTestUtil.pkcs7Padding(dataToBeEncrypted, cipherAlg.getBlockSize());
            } else {
                dataToBeEncryptedPadded = dataToBeEncrypted;
            }

            CK_MECHANISM mechanism = new CK_MECHANISM(mechanismNumber, iv);

            byte[] encryptedDataByHSM = sc.encryptData(sid, keyLabel, dataToBeEncryptedPadded, mechanism);
            byte[] plainDataOfHSMEncryptionHSMDecryption = sc.decryptData(sid, keyLabel, encryptedDataByHSM, mechanism);
            assertArrayEquals(dataToBeEncryptedPadded, plainDataOfHSMEncryptionHSMDecryption);

            if (keyBytes != null) {
                ParamsWithIV paramsWithIV = iv != null ? new ParamsWithIV(iv) : null;

                byte[] decryptedDataByCrypto = CipherUtil.decrypt(
                    cipherAlg,
                    paramsWithIV,
                    encryptedDataByHSM,
                    keyBytes
                );
                assertArrayEquals(dataToBeEncrypted, decryptedDataByCrypto);

                byte[] encryptedDataByCrypto = CipherUtil.encrypt(
                    cipherAlg,
                    paramsWithIV,
                    dataToBeEncrypted,
                    keyBytes
                );
                byte[] decryptedDataByHSM = sc.decryptData(sid, keyLabel, encryptedDataByCrypto, mechanism);
                assertArrayEquals(decryptedDataByHSM, dataToBeEncryptedPadded);
            }
        } finally {
            if (keyCreated)
                sc.deletePrivateObject(sid, keyLabel);
        }
    }
}
