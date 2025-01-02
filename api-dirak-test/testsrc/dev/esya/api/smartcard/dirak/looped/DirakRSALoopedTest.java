package dev.esya.api.smartcard.dirak.looped;

import dev.esya.api.smartcard.dirak.CardTestUtil;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DirakRSALoopedTest {

    static final String PIN = "12345678";
    static final int NUM_OF_LOOPS = 200;

    static SmartCard sc;
    static long slotNo;
    static long sid;

    int keyLength;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {1024},
            {2048},
            {3072},
            {4096}
        });
    }

    public DirakRSALoopedTest(int keyLength) {
        this.keyLength = keyLength;
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        sc = new SmartCard(CardType.DIRAKHSM);
        slotNo = CardTestUtil.getSlot(sc);
    }

    @AfterClass
    public static void cleanUpClass() {
        logoutAndCloseSession();
    }

    private static void setUp() throws PKCS11Exception {
        sid = sc.openSession(slotNo);
        sc.login(sid, PIN);
    }

    private static void cleanUp() {
        logoutAndCloseSession();
    }

    private static void logoutAndCloseSession() {
        if (sid != 0) {
            try {
                sc.logout(sid);
                sc.closeSession(sid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            sid = 0;
        }
    }

    @Before
    public void setUpBefore() throws PKCS11Exception {
        setUp();
    }

    @After
    public void cleanUpAfter() {
        cleanUp();
    }

    @Test
    public void createRSAKeyTest_Digest_MultipleCreation() throws Exception {
        String keyPrefix = MessageFormat.format("rsaPKCSLooped_{0}_Create", keyLength);

        for (int i = 0; i < NUM_OF_LOOPS; i++) {

            if (i % CardTestUtil.PRINT_INTERVAL == 0) {
                System.out.println("i: " + i);
            }

            CardTestUtil.testCreateRSAKeyAndSign(sc, sid, slotNo, keyPrefix, keyLength, SignatureAlg.RSA_SHA256, null,1);
        }
    }

    @Test
    public void createRSAKeyTest_PSS_MultipleCreation() throws Exception {
        String keyPrefix = MessageFormat.format("rsaPKCSLooped_{0}_Create", keyLength);

        for (int i = 0; i < NUM_OF_LOOPS; i++) {

            if (i % CardTestUtil.PRINT_INTERVAL == 0) {
                System.out.println("i: " + i);
            }

            CardTestUtil.testCreateRSAKeyAndSign(sc, sid, slotNo, keyPrefix, keyLength, SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), 1);
        }
    }

    @Test
    public void createRSAKeyTest_Digest_SingleCreation() throws Exception {
        String keyPrefix = MessageFormat.format("rsaPKCSLooped_{0}_Create", keyLength);
        CardTestUtil.testCreateRSAKeyAndSign(sc, sid, slotNo, keyPrefix, keyLength, SignatureAlg.RSA_SHA256, null, NUM_OF_LOOPS);
    }

    @Test
    public void createRSAKeyTest_PSS_SingleCreation() throws Exception {
        String keyPrefix = MessageFormat.format("rsaPKCSLooped_{0}_Create", keyLength);
        CardTestUtil.testCreateRSAKeyAndSign(sc, sid, slotNo, keyPrefix, keyLength, SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), NUM_OF_LOOPS);
    }

    @Test
    public void importRSAKeyTest_Digest_MultipleCreation() throws Exception {
        String keyPrefix = MessageFormat.format("rsaPKCSLooped_{0}_Import", keyLength);

        for (int i = 0; i < NUM_OF_LOOPS; i++) {
            if (i % CardTestUtil.PRINT_INTERVAL == 0) {
                System.out.println("i: " + i);
            }

            CardTestUtil.testImportRSAKeyAndSign(sc, sid, slotNo, keyPrefix, keyLength, SignatureAlg.RSA_SHA256, null, 1);
        }
    }

    @Test
    public void importRSAKeyTest_PSS_MultipleCreation() throws Exception {
        String keyPrefix = MessageFormat.format("rsaPKCSLooped_{0}_Import", keyLength);

        for (int i = 0; i < NUM_OF_LOOPS; i++) {
            if (i % CardTestUtil.PRINT_INTERVAL == 0) {
                System.out.println("i: " + i);
            }

            CardTestUtil.testImportRSAKeyAndSign(sc, sid, slotNo, keyPrefix, keyLength, SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), 1);
        }
    }

    @Test
    public void importRSAKeyTest_Digest_SingleCreation() throws Exception {
        String keyPrefix = MessageFormat.format("rsaPKCSLooped_{0}_Import", keyLength);
        CardTestUtil.testImportRSAKeyAndSign(sc, sid, slotNo, keyPrefix, keyLength, SignatureAlg.RSA_SHA256, null, NUM_OF_LOOPS);
    }

    @Test
    public void importRSAKeyTest_PSS_SingleCreation() throws Exception {
        String keyPrefix = MessageFormat.format("rsaPKCSLooped_{0}_Import", keyLength);
        CardTestUtil.testImportRSAKeyAndSign(sc, sid, slotNo, keyPrefix, keyLength, SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1), NUM_OF_LOOPS);
    }

}
