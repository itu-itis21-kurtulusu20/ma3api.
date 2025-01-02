package dev.esya.api.smartcard.dirak.ec;

import dev.esya.api.smartcard.dirak.CardTestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DirakECTest {

    static final String PASSWORD = "12345678";

    SmartCard sc;
    long sid;
    long slotNo;

    SignatureAlg signatureAlg;

    public DirakECTest(SignatureAlg signatureAlg) {
        this.signatureAlg = signatureAlg;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {SignatureAlg.ECDSA_SHA1},
            {SignatureAlg.ECDSA_SHA256},
            {SignatureAlg.ECDSA_SHA384},
            {SignatureAlg.ECDSA_SHA512}
        });
    }

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
    public void test_01_01_CreateECKey_secp192() throws Exception {
        final String curveName = "secp192r1";
        CardTestUtil.testCreateECKeysAndSign(sc, sid, slotNo, "ecCreateKey_", curveName, signatureAlg, false);
    }

    @Test
    public void test_01_02_CreateECKey_secp224() throws Exception {
        final String curveName = "secp224r1";
        CardTestUtil.testCreateECKeysAndSign(sc, sid, slotNo, "ecCreateKey_", curveName, signatureAlg, false);
    }

    @Test
    public void test_01_03_CreateECKey_secp256() throws Exception {
        final String curveName = "secp256r1";
        CardTestUtil.testCreateECKeysAndSign(sc, sid, slotNo, "ecCreateKey_", curveName, signatureAlg, false);
    }

    @Test
    public void test_01_04_CreateECKey_secp384() throws Exception {
        final String curveName = "secp384r1";
        CardTestUtil.testCreateECKeysAndSign(sc, sid, slotNo, "ecCreateKey_", curveName, signatureAlg, false);
    }

    @Test
    public void test_01_05_CreateECKey_secp521() throws Exception {
        final String curveName = "secp521r1";
        CardTestUtil.testCreateECKeysAndSign(sc, sid, slotNo, "ecCreateKey_", curveName, signatureAlg, false);
    }

    @Test
    public void test_02_01_ImportECKey_secp192() throws Exception {
        final String curveName = "secp192r1";
        CardTestUtil.importCurveAndSignAndVerifyAtLibrary(sc, sid, "ecImportKey_", curveName, signatureAlg, false);
    }

    @Test
    public void test_02_02_ImportECKey_secp224() throws Exception {
        final String curveName = "secp224r1";
        CardTestUtil.importCurveAndSignAndVerifyAtLibrary(sc, sid, "ecImportKey_", curveName, signatureAlg, false);
    }

    @Test
    public void test_02_03_ImportECKey_secp256() throws Exception {
        final String curveName = "secp256r1";
        CardTestUtil.importCurveAndSignAndVerifyAtLibrary(sc, sid, "ecImportKey_", curveName, signatureAlg, false);
    }

    @Test
    public void test_02_04_ImportECKey_secp384() throws Exception {
        final String curveName = "secp384r1";
        CardTestUtil.importCurveAndSignAndVerifyAtLibrary(sc, sid, "ecImportKey_", curveName, signatureAlg, false);
    }

    @Test
    public void test_02_05_ImportECKey_secp521() throws Exception {
        final String curveName = "secp521r1";
        CardTestUtil.importCurveAndSignAndVerifyAtLibrary(sc, sid, "ecImportKey_", curveName, signatureAlg, false);
    }
}
