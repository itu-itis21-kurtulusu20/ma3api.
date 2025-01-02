package dev.esya.api.smartcard.dirak;

import org.junit.*;
import org.junit.runners.MethodSorters;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key.HMACSecretKey;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Random;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DirakHMACTest {

    SmartCard sc;
    long sid = -1;

    static final String PASSWORD = "12345678";

    static final String CREATED_HMAC_KEY_LABEL = "HMACKeyCreated";
    static final String IMPORTED_HMAC_KEY_LABEL = "HMACKeyImported";

    // ---

    @Before
    public void setUp() throws PKCS11Exception, IOException {
        sc = new SmartCard(CardType.DIRAKHSM);
        long slot = CardTestUtil.getSlot(sc);
        sid = sc.openSession(slot);
        sc.login(sid, PASSWORD);
    }

    @After
    public void cleanUp() throws PKCS11Exception {
        sc.logout(sid);

        if (sid != -1)
            sc.closeSession(sid);
    }

    // ---

    @Test
    public void test_01_HmacKeyGeneration() throws Exception {
        if (sc.isObjectExist(sid, CREATED_HMAC_KEY_LABEL)) {
            throw new SmartCardException(MessageFormat.format("A key with the label \"{0}\" already exists", CREATED_HMAC_KEY_LABEL));
        }

        HMACSecretKey secretkey = new HMACSecretKey(CREATED_HMAC_KEY_LABEL, 32);
        sc.createSecretKey(sid, secretkey);

        boolean isObjectExist = sc.isObjectExist(sid, CREATED_HMAC_KEY_LABEL);
        Assert.assertTrue(isObjectExist);
    }

    @Test
    public void test_02_HmacSign() throws Exception {
        byte[] tobesigned = new byte[27];
        new Random().nextBytes(tobesigned);
        byte[] sig = sc.signData(sid, CREATED_HMAC_KEY_LABEL, tobesigned, PKCS11Constants.CKM_SHA256_HMAC);

        sc.getCardType().getCardTemplate().getPKCS11Ops().verifyData(sid, CREATED_HMAC_KEY_LABEL, tobesigned, sig, PKCS11Constants.CKM_SHA256_HMAC);
    }

    @Test
    public void test_03_HmacImport() throws Exception {
        byte[] key = new byte[32];
        new Random().nextBytes(key);

        HMACSecretKey secretkey = new HMACSecretKey(IMPORTED_HMAC_KEY_LABEL, key);
        boolean r1 = sc.isObjectExist(sid, IMPORTED_HMAC_KEY_LABEL);
        sc.importSecretKey(sid, secretkey);
        boolean r2 = sc.isObjectExist(sid, IMPORTED_HMAC_KEY_LABEL);
        Assert.assertTrue(!r1 && r2);
    }

    @Test
    public void test_04_HmacSignWithImportedKeys() throws Exception {
        byte[] tobesigned = new byte[231];
        new Random().nextBytes(tobesigned);

        byte[] sig = sc.signData(sid, IMPORTED_HMAC_KEY_LABEL, tobesigned, PKCS11Constants.CKM_SHA256_HMAC);
    }

    @Test
    public void test_99_DeleteHmacKeys() throws Exception {
        sc.deletePrivateObject(sid, CREATED_HMAC_KEY_LABEL);
        sc.deletePrivateObject(sid, IMPORTED_HMAC_KEY_LABEL);
    }
}
