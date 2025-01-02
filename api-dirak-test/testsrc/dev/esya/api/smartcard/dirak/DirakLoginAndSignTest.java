package dev.esya.api.smartcard.dirak;

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

import java.io.IOException;
import java.security.spec.RSAKeyGenParameterSpec;

public class DirakLoginAndSignTest {

    SmartCard sc;
    long sid;
    static final String CREATED_KEY_LABEL_FOR_SIGNING = "SigningRSAKeyCreated" + "_" + StringUtil.toHexString(RandomUtil.generateRandom(2)) + "_" + DirakTestConstants.osNameAndArch;
    static final byte[] tobesigned = RandomUtil.generateRandom(32);
    static final CK_MECHANISM mech = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);
    static final String correctPIN = "12345678";

    @Before
    public void setUp() throws PKCS11Exception, IOException {
        sc = new SmartCard(CardType.DIRAKHSM);
        long slot = CardTestUtil.getSlot(sc);
        sid = sc.openSession(slot);

        CardTestUtil.clearKeyPairIfExist(sc, sid, CREATED_KEY_LABEL_FOR_SIGNING);
    }

    @After
    public void cleanUp() {
        CardTestUtil.clearKeyPairIfExist(sc, sid, CREATED_KEY_LABEL_FOR_SIGNING);

        try {
            // when cleaning up, do a correct login to reset wrong PIN counter
            sc.login(sid, correctPIN);
            sc.logout(sid);

            sc.closeSession(sid);
        } catch (Exception ignored) {
        }
    }

    @Test
    public void dirakLoginEmptyTest() {
        try {
            sc.login(sid, "");
            Assert.fail("login did not fail as expected");
        } catch (Exception ignored) {
            System.out.println("Test successful: empty login fails");
        } finally {
            try {
                sc.logout(sid);
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    public void dirakLoginWrongTest() {
        try {
            sc.login(sid, "231231");
            Assert.fail("login did not fail as expected");
        } catch (Exception ignored) {
            System.out.println("Test successful: wrong login fails");
        } finally {
            try {
                sc.logout(sid);
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    public void dirakLoginEmptyAndSignTest() throws PKCS11Exception, IOException {
        try {
            sc.login(sid, "");
        } catch (PKCS11Exception e) {
            if (e.getErrorCode() == PKCS11Constants.CKR_PIN_INCORRECT) {
                System.err.println("Test inconclusive: not a failure but cannot perform test signing since empty login fails");
                return;
            }
        }

        // if empty login works, try signing- signing may be expected not to work in such a case
        try {
            createKeyPair();

            sc.signData(sid, CREATED_KEY_LABEL_FOR_SIGNING, tobesigned, mech);
            Assert.fail("signData did not fail as expected");
        } catch (SmartCardException ignored) {
            System.out.println("Test successful: sign fails with empty login");
        } finally {
            try {
                sc.logout(sid);
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    public void dirakLoginWrongAndSignTest() throws PKCS11Exception, IOException {
        try {
            sc.login(sid, "987654");
        } catch (PKCS11Exception e) {
            if (e.getErrorCode() == PKCS11Constants.CKR_PIN_INCORRECT) {
                System.err.println("Test inconclusive: not a failure but cannot perform test signing since wrong login fails");
                return;
            }
        }

        // if wrong login works, try signing- signing may be expected not to work in such a case
        try {
            createKeyPair();

            sc.signData(sid, CREATED_KEY_LABEL_FOR_SIGNING, tobesigned, mech);
            Assert.fail("signData did not fail as expected");
        } catch (SmartCardException ignored) {
            System.out.println("Test successful: sign fails with wrong login");
        } finally {
            try {
                sc.logout(sid);
            } catch (Exception ignored) {
            }
        }
    }

    public void createKeyPair() throws PKCS11Exception, SmartCardException, IOException {
        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
        sc.createKeyPair(sid, CREATED_KEY_LABEL_FOR_SIGNING, spec, true, false);
    }
}
