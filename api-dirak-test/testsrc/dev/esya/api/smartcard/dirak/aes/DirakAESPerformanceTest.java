package dev.esya.api.smartcard.dirak.aes;

import dev.esya.api.smartcard.dirak.CardTestUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;

import java.text.MessageFormat;

public class DirakAESPerformanceTest {

    static final String PASSWORD = "12345678";
    SmartCard sc = null;
    long sid = 0;
    long slotNo = 0;

    @Before
    public void setUp() throws Exception {
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

    // ---

    @Test
    public void test_02_AES_CBC_PAD_EncryptAndWrapPerformance() throws PKCS11Exception, SmartCardException {
        System.out.println("Will test for 100 MB of random data");
        System.out.println("---");
        System.out.println("Generating data");
        byte[] dataToBeEncrypted = RandomUtil.generateRandom(100000000);
        System.out.println("Generated data");
        aes_CBC_PAD_EncryptAndWrapPerformance(dataToBeEncrypted);
    }

    // ---

    public void aes_CBC_PAD_EncryptAndWrapPerformance(byte[] dataToBeEncrypted) throws PKCS11Exception, SmartCardException {
        final int keySize = 32;

        final String keyLabel;
        final String masterKeyLabel;

        Long keyID = null;
        Long masterKeyID = null;
        {
            long currentTimeMillis = System.currentTimeMillis();
            keyLabel = "aes_cbc_pad_" + currentTimeMillis;
            masterKeyLabel = "master_key_" + currentTimeMillis;
        }

        try {
            System.out.println("Creating master key");
            {
                byte[] mkBytes = RandomUtil.generateRandom(keySize);
                AESKeyTemplate mkTemplate = new AESKeyTemplate(masterKeyLabel);
                mkTemplate.getAsImportTemplate(mkBytes);

                mkTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_WRAP, true));
                mkTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_UNWRAP, true));

                masterKeyID = sc.importSecretKey(sid, mkTemplate);
            }
            System.out.println(MessageFormat.format("Created master key ({0})", masterKeyLabel));

            CK_MECHANISM encryptMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD);
            byte[] iv = RandomUtil.generateRandom(16);
            encryptMechanism.pParameter = iv;

            AESKeyTemplate keyTemplate = new AESKeyTemplate(keyLabel);
            keyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false));
            keyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true));
            keyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE_LEN, keySize));
            keyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ENCRYPT, true));
            keyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, true));
            keyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true));

            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_KEY_WRAP);

            System.out.println("Begin: create key, encrypt data, wrap key, delete key");
            long millis = System.currentTimeMillis();

            // create key
            keyID = sc.createSecretKey(sid, keyTemplate);

            // encrypt data
            byte[] encryptedData = sc.encryptData(sid, keyID, dataToBeEncrypted, encryptMechanism);

            // wrap key
            byte[] wrappedKey = sc.wrapKey(sid, wrapMechanism, masterKeyLabel, keyLabel);

            // delete key
            sc.getPKCS11().C_DestroyObject(sid, keyID);

            millis = System.currentTimeMillis() - millis;
            System.out.println(MessageFormat.format("End ({0} ms)", millis));

            // the key will not be existing at the moment- reflect this change
            keyID = null;

            System.out.println("---");

            KeyTemplate unwrapTemplate = new AESKeyTemplate(keyLabel + "-unwrapped");
            // keyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, keyLabel + "-unwrapped"));
            unwrapTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, true));

            System.out.println("Begin: unwrap key, decrypt data, delete key");
            millis = System.currentTimeMillis();

            // unwrap the wrapped key
            keyID = sc.unwrapKey(sid, wrapMechanism, masterKeyLabel, wrappedKey, unwrapTemplate); // keyTemplate

            // decrypt the encrypted data
            byte[] decryptedData = sc.decryptData(sid, keyID, encryptedData, encryptMechanism);

            // delete unwrapped key
            sc.getPKCS11().C_DestroyObject(sid, keyID);

            millis = System.currentTimeMillis() - millis;
            System.out.println(MessageFormat.format("End ({0} ms)", millis));

            // the key was deleted- reflect this change
            keyID = null;

            // verify
            Assert.assertArrayEquals("Mismatching original data and decrypted data", dataToBeEncrypted, decryptedData);

            System.out.println("Success: matching original data and decrypted data");

        } finally {
            // delete keys

            if (keyID != null) {
                try {
                    sc.getPKCS11().C_DestroyObject(sid, keyID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (masterKeyID != null) {
                try {
                    sc.getPKCS11().C_DestroyObject(sid, masterKeyID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
