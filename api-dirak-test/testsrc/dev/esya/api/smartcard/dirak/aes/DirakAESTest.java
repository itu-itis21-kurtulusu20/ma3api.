package dev.esya.api.smartcard.dirak.aes;

import dev.esya.api.smartcard.dirak.CardTestUtil;
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
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Mod;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Padding;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ConstantsUtil;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Parameterized.class)
public class DirakAESTest {

    static final String PASSWORD = "12345678";
    SmartCard sc = null;
    long sid = 0;
    long slotNo = 0;

    int keySize;
    CipherAlg cipherAlg;
    int testDataLength;
    int ivLength;

    // ---

    @Parameterized.Parameters(name = "{0}, {1}, {2}, {3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{

            // cbc

            {16, CipherAlg.AES128_CBC_NOPADDING, 1024,       16},
            {16, CipherAlg.AES128_CBC_NOPADDING, 1024 * 128, 16},

            {24, CipherAlg.AES192_CBC_NOPADDING, 1024,       16},
            {24, CipherAlg.AES192_CBC_NOPADDING, 1024 * 128, 16},

            {32, CipherAlg.AES256_CBC_NOPADDING, 1024,       16},
            {32, CipherAlg.AES256_CBC_NOPADDING, 1024 * 128, 16},

            // cbc-pad

            {16, CipherAlg.AES128_CBC, 1000,           16},
            {16, CipherAlg.AES128_CBC, 1024,           16},
            {16, CipherAlg.AES128_CBC, 1024 * 128,     16},
            {16, CipherAlg.AES128_CBC, 1024 * 127 + 1, 16},

            {24, CipherAlg.AES192_CBC, 1000,           16},
            {24, CipherAlg.AES192_CBC, 1024,           16},
            {24, CipherAlg.AES192_CBC, 1024 * 128,     16},
            {24, CipherAlg.AES192_CBC, 1024 * 127 + 1, 16},

            {32, CipherAlg.AES256_CBC, 1000,           16},
            {32, CipherAlg.AES256_CBC, 1024,           16},
            {32, CipherAlg.AES256_CBC, 1024 * 128,     16},
            {32, CipherAlg.AES256_CBC, 1024 * 127 + 1, 16},

            // ecb

            {16, CipherAlg.AES128_ECB_NOPADDING, 1024,       0},
         // {16, CipherAlg.AES128_ECB_NOPADDING, 1024 * 128, 0},

            {24, CipherAlg.AES192_ECB_NOPADDING, 1024,       0},
         // {24, CipherAlg.AES192_ECB_NOPADDING, 1024 * 128, 0},

            {32, CipherAlg.AES256_ECB_NOPADDING, 1024,       0},
         // {32, CipherAlg.AES256_ECB_NOPADDING, 1024 * 128, 0},

            // ecb-pad

            {16, CipherAlg.AES128_ECB, 1000,           0},
            {16, CipherAlg.AES128_ECB, 1024,           0},
         // {16, CipherAlg.AES128_ECB, 1024 * 128,     0},
         // {16, CipherAlg.AES128_ECB, 1024 * 127 + 1, 0},

            {24, CipherAlg.AES192_ECB, 1000,           0},
            {24, CipherAlg.AES192_ECB, 1024,           0},
         // {24, CipherAlg.AES192_ECB, 1024 * 128,     0},
         // {24, CipherAlg.AES192_ECB, 1024 * 127 + 1, 0},

            {32, CipherAlg.AES256_ECB, 1000,           0},
            {32, CipherAlg.AES256_ECB, 1024,           0},
         // {32, CipherAlg.AES256_ECB, 1024 * 128,     0},
         // {32, CipherAlg.AES256_ECB, 1024 * 127 + 1, 0}
        });
    }

    public DirakAESTest(int keySize, CipherAlg cipherAlg, int testDataLength, int ivLength) {
        this.keySize = keySize;
        this.cipherAlg = cipherAlg;
        this.testDataLength = testDataLength;
        this.ivLength = ivLength;
    }

    @Before
    public void setUp() throws Exception
    {
        sc = new SmartCard(CardType.DIRAKHSM);
        slotNo = CardTestUtil.getSlot(sc);
        System.out.println("Using slot " + slotNo);
        sid = sc.openSession(slotNo);
        sc.login(sid, PASSWORD);
    }

    @After
    public void cleanUp()  throws Exception
    {
        sc.logout(sid);
        sc.closeSession(sid);
    }

    // ---

    @Test
    public void test_AES_Create_1() throws Exception {
        test_AES_1(
            sc,
            keySize,
            testDataLength,
            true,
            cipherAlg,
            ivLength
        );
    }

    @Test
    public void test_AES_Import_1() throws Exception {
        test_AES_1(
            sc,
            keySize,
            testDataLength,
            false,
            cipherAlg,
            ivLength
        );
    }

    // ---

    public void test_AES_1(SmartCard sc, int keySize, int dataLen, boolean createKey, CipherAlg cipherAlg, int randomIVLength) throws Exception {
        byte[] iv = randomIVLength == 0 ? null : RandomUtil.generateRandom(randomIVLength);
        test_AES_1(sc, keySize, dataLen, createKey, cipherAlg, iv);
    }

    public void test_AES_1(SmartCard sc, int keySize, int dataLen, boolean createKey, CipherAlg cipherAlg, byte[] iv) throws Exception {
        String keyLabel = "aes_" + System.currentTimeMillis();

        boolean keyCreated = false;
        long mechanismNumber = ConstantsUtil.convertSymmetricAlgToPKCS11Constant(cipherAlg.getName());

        try {
            AESKeyTemplate aesKeyTemplate = new AESKeyTemplate(keyLabel, keySize);
            aesKeyTemplate.getAsTokenTemplate(false, true, false);

            byte[] keyBytes = null;

            long secretKeyId;
            if (createKey) {
                secretKeyId = sc.createSecretKey(sid, aesKeyTemplate);
                keyCreated = true;
            } else {
                keyBytes = RandomUtil.generateRandom(keySize);
                aesKeyTemplate.getAsImportTemplate(keyBytes);
                secretKeyId = sc.importSecretKey(sid, aesKeyTemplate);
                keyCreated = true;
            }

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
            if (cipherAlg.getMod() == Mod.ECB && cipherAlg.getPadding() == Padding.PKCS7) {
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
