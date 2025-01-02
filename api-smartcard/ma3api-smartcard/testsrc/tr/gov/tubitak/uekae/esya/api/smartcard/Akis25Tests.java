package tr.gov.tubitak.uekae.esya.api.smartcard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import util.CardTestUtil;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by sura.emanet on 24.07.2019.
 */
@RunWith(Parameterized.class)
public class Akis25Tests {

    static SmartCard sc = null;
    static long sid = 0;
    static long slotNo = 0;
    static final String PASSWORD = "12345";
    SignatureAlg signatureAlg;

    public Akis25Tests(SignatureAlg signatureAlg) {
        this.signatureAlg = signatureAlg;
    }

    /*ECDSA_SHA224 algoritması ile ilgili CKR_DATA_LEN_RANGE hatası alınıyor.
      AKIS ECDSA ve RSA için SHA1, SHA256, SHA384 ve SHA512 desteklendiğini belirtti.*/

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
    public void setUp() throws Exception
    {
        sc = new SmartCard(CardType.AKIS);
        slotNo = sc.getTokenPresentSlotList()[0];
        sid = sc.openSession(slotNo);
        sc.login(sid, PASSWORD);
    }

    @After
    public void cleanUp()  throws Exception
    {
        sc.logout(sid);
        sc.closeSession(sid);
    }


    //Creating RSA key pairs in v2.5.2 cards
    @Test
    public void test_07_01_CreateRSAPKCS1KeyAndSign() throws Exception {
        CardTestUtil.testCreateRSAKeyAndSign(sc, "rsaCreateKey_", 1024);
    }

    @Test
    public void test_07_02_CreateRSAPKCS1KeyAndSign() throws Exception {
        CardTestUtil.testCreateRSAKeyAndSign(sc, "rsaCreateKey_", 2048);
    }

    //Importing RSA key pairs created in memory to v2.5.2 cards
    @Test
    public void test_08_01_ImportRSAPKCS1KeyAndSign() throws Exception {
        CardTestUtil.testImportRSAKeyAndSign(sc, "rsaImportKey_", 1024);
    }

    @Test
    public void test_08_02_ImportRSAPKCS1KeyAndSign() throws Exception {
        CardTestUtil.testImportRSAKeyAndSign(sc, "rsaImportKey_", 2048);
    }

    /*ECDSA_SHA224 algoritması ile ilgili CKR_DATA_LEN_RANGE hatası alınıyor.
      AKIS ECDSA ve RSA için SHA1, SHA256, SHA384 ve SHA512 desteklendiğini belirtti.*/

    //Creating EC key pairs in v2.5.2 cards
    @Test
    public void test_09_01_CreateECKey_secp192() throws Exception {
        CardTestUtil.testCreateECKeys(sc, "ecCreateKey_", "secp192r1", signatureAlg, false);
    }

    @Test
    public void test_09_02_CreateECKey_secp224() throws Exception {
        CardTestUtil.testCreateECKeys(sc, "ecCreateKey_", "secp224r1", signatureAlg, false);
    }

    @Test
    public void test_09_03_CreateECKey_secp256() throws Exception {
        CardTestUtil.testCreateECKeys(sc, "ecCreateKey_", "secp256r1", signatureAlg, false);
    }

    @Test
    public void test_09_04_CreateECKey_secp384() throws Exception {
        CardTestUtil.testCreateECKeys(sc, "ecCreateKey_", "secp384r1", signatureAlg, false);
    }

    @Test
    public void test_09_05_CreateECKey_secp521() throws Exception {
        CardTestUtil.testCreateECKeys(sc, "ecCreateKey_", "secp521r1", signatureAlg, false);
    }


    /*ECDSA_SHA224 algoritması ile ilgili CKR_DATA_LEN_RANGE hatası alınıyor.
      AKIS ECDSA ve RSA için SHA1, SHA256, SHA384 ve SHA512 desteklendiğini belirtti.*/

    //Importing EC key pairs created in memory to v2.5.2 cards
    @Test
    public void test_10_01_ImportECKey_secp192() throws Exception {
        CardTestUtil.importCurveAndSignAndVerifyAtLibrary(sc, "ecImportKey_", sid, "secp192r1", signatureAlg, false);
    }

    @Test
    public void test_10_02_ImportECKey_secp224() throws Exception {
        CardTestUtil.importCurveAndSignAndVerifyAtLibrary(sc, "ecImportKey_", sid, "secp224r1", signatureAlg, false);
    }

    @Test
    public void test_10_03_ImportECKey_secp256() throws Exception {
        CardTestUtil.importCurveAndSignAndVerifyAtLibrary(sc, "ecImportKey_", sid, "secp256r1", signatureAlg, false);
    }

    @Test
    public void test_10_04_ImportECKey_secp384() throws Exception {
        CardTestUtil.importCurveAndSignAndVerifyAtLibrary(sc, "ecImportKey_", sid, "secp384r1", signatureAlg, false);
    }

    @Test
    public void test_10_05_ImportECKey_secp521() throws  Exception {
        CardTestUtil.importCurveAndSignAndVerifyAtLibrary(sc, "ecImportKey_", sid, "secp521r1", signatureAlg, false);
    }

    @Test
    public void test_11_01_CreateECKey_With_SecretCurve_secp192() throws Exception {
        final String curveName = "secp192r1";
        CardTestUtil.testCreateECKeys(sc, "ecCreateKeySecret_", curveName, signatureAlg, true);
    }

    @Test
    public void test_11_02_CreateECKey_With_SecretCurve_secp224() throws Exception {
        final String curveName = "secp224r1";
        CardTestUtil.testCreateECKeys(sc, "ecCreateKeySecret_", curveName, signatureAlg, true);
    }

    @Test
    public void test_11_03_CreateECKey_With_SecretCurve_secp256() throws Exception {
        final String curveName = "secp256r1";
        CardTestUtil.testCreateECKeys(sc, "ecCreateKeySecret_", curveName, signatureAlg, true);
    }

    @Test
    public void test_11_04_CreateECKey_With_SecretCurve_secp384() throws Exception {
        final String curveName = "secp384r1";
        CardTestUtil.testCreateECKeys(sc, "ecCreateKeySecret_", curveName, signatureAlg, true);
    }

    @Test
    public void test_11_05_CreateECKey_With_SecretCurve_secp521() throws Exception {
        final String curveName = "secp521r1";
        CardTestUtil.testCreateECKeys(sc, "ecCreateKeySecret_", curveName, signatureAlg, true);
    }

    @Test
    public void test_12_01_ImportECKey_With_SecretCurve_secp192() throws Exception {
        CardTestUtil.importCurveAndSignAndVerifyAtLibrary(sc, "ecImportKey_", sid, "secp192r1", signatureAlg, true);
    }

    @Test
    public void test_12_02_ImportECKey_With_SecretCurve_secp224() throws Exception {
        CardTestUtil.importCurveAndSignAndVerifyAtLibrary(sc, "ecImportKey_", sid, "secp224r1", signatureAlg, true);
    }

    @Test
    public void test_12_03_ImportECKey_With_SecretCurve_secp256() throws Exception {
        CardTestUtil.importCurveAndSignAndVerifyAtLibrary(sc, "ecImportKey_", sid, "secp256r1", signatureAlg, true);
    }

    @Test
    public void test_12_04_ImportECKey_With_SecretCurve_secp384() throws Exception {
        CardTestUtil.importCurveAndSignAndVerifyAtLibrary(sc, "ecImportKey_", sid, "secp384r1", signatureAlg, true);
    }

    @Test
    public void test_12_05_ImportECKey_With_SecretCurve_secp521() throws Exception {
        CardTestUtil.importCurveAndSignAndVerifyAtLibrary(sc, "ecImportKey_", sid, "secp521r1", signatureAlg, true);
    }

    @Test
    public void test_deleteAllObjects() throws PKCS11Exception {
        CardTestUtil.deleteAllObjects(sc.getLatestSessionID(), sc);
    }
}
