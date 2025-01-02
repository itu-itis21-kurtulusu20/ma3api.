package dev.esya.api.smartcard.dirak.looped;

import dev.esya.api.smartcard.dirak.CardTestUtil;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;

import java.util.Arrays;
import java.util.Collection;

import static dev.esya.api.smartcard.dirak.CardTestUtil.importCurveAndSignAndVerifyAtLibrary;


@RunWith(Parameterized.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DirakECLoopedTest {

    static final String PIN = "12345678";
    static final int NUM_OF_LOOPS = 200;
    static final SignatureAlg signatureAlg = SignatureAlg.ECDSA_SHA256;


    SmartCard sc;
    long sid;
    long slotNo;

    String curveName;
    boolean isSecret;

    @Parameterized.Parameters(name = "{0}, {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"secp192r1", false},
                {"secp224r1", false},
                {"secp256r1", false},
                {"secp384r1", false},
                {"secp521r1", false},
                {"secp192r1", true},
                {"secp224r1", true},
                {"secp256r1", true},
                {"secp384r1", true},
                {"secp521r1", true}
        });
    }

    public DirakECLoopedTest(String curveName, boolean isSecret) {
        this.curveName = curveName;
        this.isSecret = isSecret;
    }

    @Before
    public void setUp() throws Exception {
        sc = new SmartCard(CardType.DIRAKHSM);
        slotNo = CardTestUtil.getSlot(sc);
        sid = sc.openSession(slotNo);
        sc.login(sid, PIN);
    }

    @After
    public void cleanUp() throws PKCS11Exception {
        sc.logout(sid);
        sc.closeSession(sid);
    }

    // create
    @Test
    public void test_01_CreateECKey_Looped_MultipleCreation() throws Exception {
        String keyPrefix = "ecCreateKey_" + curveName;

        for (int i = 0; i < NUM_OF_LOOPS; i++) {
            if (i % CardTestUtil.PRINT_INTERVAL == 0) {
                System.out.println("i: " + i);
            }

            CardTestUtil.testCreateECKeysAndSign(sc, sid, slotNo, keyPrefix, curveName, signatureAlg, isSecret);
        }

    }

    @Test
    public void test_02_CreateECKey_Looped_SingleCreation() throws Exception {
        String keyPrefix = "ecCreateKey_" + curveName;
        CardTestUtil.testCreateECKeysAndSign(sc, sid, slotNo, keyPrefix, curveName, signatureAlg, isSecret, NUM_OF_LOOPS);
    }

    // import
    @Test
    public void test_03_ImportECKey_Looped() throws Exception {
        String keyPrefix = "ecImportKey_" + curveName;

        for (int i = 0; i < NUM_OF_LOOPS; i++) {
            if (i % CardTestUtil.PRINT_INTERVAL == 0)
                System.out.println("i: " + i);

            importCurveAndSignAndVerifyAtLibrary(sc, sid, keyPrefix, curveName, signatureAlg, isSecret);
        }
    }

    @Test
    public void test_03_ImportECKey_Looped_SingleImport() throws Exception {
        String keyPrefix = "ecImportKey_" + curveName;

        importCurveAndSignAndVerifyAtLibrary(sc, sid, keyPrefix, curveName, signatureAlg, isSecret, NUM_OF_LOOPS);
    }
}
