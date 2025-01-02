package dev.esya.api.smartcard.dirak;

import gnu.crypto.key.rsa.GnuRSAPublicKey;
import gnu.crypto.util.Base64;
import org.junit.*;
import org.junit.rules.ExpectedException;
import sun.security.pkcs11.wrapper.*;
import tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8.EPrivateKeyInfo;
import tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8.ERSAPrivateKey;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EVersion;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme.RSAPSS_SS;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ConstantsUtil;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Random;

public class DirakRSATest {

    static final String PASSWORD = "12345678";
    SmartCard sc = null;
    long sid = 0;
    long slotNo;

    static byte[] signature = null;
    static byte[] tobesigned = null;

    static final String CREATED_KEY_LABEL_FOR_SIGNING = "SigningRSAKeyCreated";
    static final String CREATED_KEY_LABEL_FOR_ENCRYPTION = "EncryptionRSAKeyCreated";
    static final String IMPORTED_KEY_LABEL = "RSAKeyImported";

    @Rule
    public ExpectedException expectedExceptionRule = ExpectedException.none();

    // ---

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
    public void test_01_01_CreateRSAKeyAndPSS_Sign() throws Exception {
        _CreateRSAKeyAndPSS_Sign(1024, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_01_02_CreateRSAKeyAndPSS_Sign() throws Exception {
        _CreateRSAKeyAndPSS_Sign(1024, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_01_03_CreateRSAKeyAndPSS_Sign() throws Exception {
        _CreateRSAKeyAndPSS_Sign(1536, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_01_04_CreateRSAKeyAndPSS_Sign() throws Exception {
        _CreateRSAKeyAndPSS_Sign(1536, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_01_01_CreateRSAKeyAndPSS_Sign_P03() throws Exception {
        _CreateRSAKeyAndPSS_Sign(1536, Algorithms.DIGEST_SHA512);
    }

    @Test
    public void test_01_05_CreateRSAKeyAndPSS_Sign() throws Exception {
        _CreateRSAKeyAndPSS_Sign(2048, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_01_06_CreateRSAKeyAndPSS_Sign() throws Exception {
        _CreateRSAKeyAndPSS_Sign(2048, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_01_01_CreateRSAKeyAndPSS_Sign_P06() throws Exception {
        _CreateRSAKeyAndPSS_Sign(2048, Algorithms.DIGEST_SHA512);
    }

    @Test
    public void test_01_07_CreateRSAKeyAndPSS_Sign() throws Exception {
        _CreateRSAKeyAndPSS_Sign(3072, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_01_08_CreateRSAKeyAndPSS_Sign() throws Exception {
        _CreateRSAKeyAndPSS_Sign(3072, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_01_01_CreateRSAKeyAndPSS_Sign_P09() throws Exception {
        _CreateRSAKeyAndPSS_Sign(3072, Algorithms.DIGEST_SHA512);
    }

    @Test
    public void test_01_09_CreateRSAKeyAndPSS_Sign() throws Exception {
        _CreateRSAKeyAndPSS_Sign(4096, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_01_10_CreateRSAKeyAndPSS_Sign() throws Exception {
        _CreateRSAKeyAndPSS_Sign(4096, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_01_11_CreateRsaAndSignWithoutCloseSession() throws Exception {
        int keyLen = 2048;
        int sessionCount = 3000;

        CardTestUtil.clearKeyPairIfExist(sc, sid, CREATED_KEY_LABEL_FOR_SIGNING);

        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keyLen, null);
        sc.createKeyPair(sid, CREATED_KEY_LABEL_FOR_SIGNING, spec, true, false);
        boolean priFound = sc.isPrivateKeyExist(sid, CREATED_KEY_LABEL_FOR_SIGNING);
        boolean pubFound = sc.isPublicKeyExist(sid, CREATED_KEY_LABEL_FOR_SIGNING);

        Assert.assertTrue(priFound && pubFound);

        for (int i = 0; i < sessionCount; i++) {
            if (i % 10 == 0)
                System.out.println(i + ". session açılacak.");
            sid = sc.openSession(slotNo);
            _signAtHSMAndVerifyRSA_PSS(DigestAlg.SHA256, CREATED_KEY_LABEL_FOR_SIGNING);
        }

        CardTestUtil.clearKeyPairIfExist(sc, sid, CREATED_KEY_LABEL_FOR_SIGNING);
    }

    @Test
    public void test_01_2_CreateRSAKeyAndRead() throws Exception {
        int keyLength = 2048;

        byte[] key = sc.generateRSAPrivateKey(sid, keyLength);
        ERSAPrivateKey rsaPrivateKey = new ERSAPrivateKey(key);
        EPrivateKeyInfo privateKeyInfo = new EPrivateKeyInfo(EVersion.v1, new EAlgorithmIdentifier(AsymmetricAlg.RSA.getOID(), EAlgorithmIdentifier.ASN_NULL), key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyInfo.getEncoded());
        PrivateKey privateKey;
        privateKey = KeyFactory.getInstance(AsymmetricAlg.RSA.getName()).generatePrivate(keySpec);
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(rsaPrivateKey.getModulus(), rsaPrivateKey.getPublicExponent());
        PublicKey publicKey = KeyFactory.getInstance(AsymmetricAlg.RSA.getName()).generatePublic(publicKeySpec);
        new KeyPair(publicKey, privateKey);
    }

    //imzalama anahtari ile sifrelemeye izin vermemeli
    @Test
    public void test_12_EncryptionWithSigningKey() throws Exception {
        // create the key pair (for signing)
        int keyLen = 2048;
        _CreateRSASigningKey(keyLen);

        CK_MECHANISM mech = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);

        // try encryption with signing key- failure is expected
        expectedExceptionRule.expect(PKCS11Exception.class);
        expectedExceptionRule.expectMessage("CKR_KEY_FUNCTION_NOT_PERMITTED");

        sc.encryptData(sid, CREATED_KEY_LABEL_FOR_SIGNING, "test".getBytes(), mech);
    }

    @Test
    public void test_02_01_ImportRSAKeyAndPSS_Sign() throws Exception {
        _ImportRSAKeyAndPSS_Sign(1024, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_02_02_ImportRSAKeyAndPSS_Sign() throws Exception {
        _ImportRSAKeyAndPSS_Sign(1024, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_02_03_ImportRSAKeyAndPSS_Sign() throws Exception {
        _ImportRSAKeyAndPSS_Sign(1526, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_02_04_ImportRSAKeyAndPSS_Sign() throws Exception {
        _ImportRSAKeyAndPSS_Sign(1526, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_02_01_ImportRSAKeyAndPSS_SignP03() throws Exception {
        _ImportRSAKeyAndPSS_Sign(1526, Algorithms.DIGEST_SHA512);
    }

    @Test
    public void test_02_05_ImportRSAKeyAndPSS_Sign() throws Exception {
        _ImportRSAKeyAndPSS_Sign(2048, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_02_06_ImportRSAKeyAndPSS_Sign() throws Exception {
        _ImportRSAKeyAndPSS_Sign(2048, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_02_01_ImportRSAKeyAndPSS_SignP06() throws Exception {
        _ImportRSAKeyAndPSS_Sign(2048, Algorithms.DIGEST_SHA512);
    }

    @Test
    public void test_02_07_ImportRSAKeyAndPSS_Sign() throws Exception {
        _ImportRSAKeyAndPSS_Sign(3072, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_02_08_ImportRSAKeyAndPSS_Sign() throws Exception {
        _ImportRSAKeyAndPSS_Sign(3072, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_02_01_ImportRSAKeyAndPSS_SignP09() throws Exception {
        _ImportRSAKeyAndPSS_Sign(3072, Algorithms.DIGEST_SHA512);
    }

    @Test
    public void test_02_09_ImportRSAKeyAndPSS_Sign() throws Exception {
        _ImportRSAKeyAndPSS_Sign(4096, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_02_10_ImportRSAKeyAndPSS_Sign() throws Exception {
        _ImportRSAKeyAndPSS_Sign(4096, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_02_01_ImportRSAKeyAndPSS_SignP12() throws Exception {
        _ImportRSAKeyAndPSS_Sign(4096, Algorithms.DIGEST_SHA512);
    }

    @Test
    public void test_03_01_CreateRsaAndDecryptRsaOAEP() throws Exception {
        CreateRSAMakeDecrypt(1536, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_03_02_CreateRsaAndDecryptRsaOAEP() throws Exception {
        CreateRSAMakeDecrypt(1536, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_03_03_CreateRsaAndDecryptRsaOAEP() throws Exception {
        CreateRSAMakeDecrypt(1536, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_03_04_CreateRsaAndDecryptRsaOAEP() throws Exception {
        CreateRSAMakeDecrypt(1536, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_03_05_CreateRsaAndDecryptRsaOAEP() throws Exception {
        CreateRSAMakeDecrypt(2048, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_03_06_CreateRsaAndDecryptRsaOAEP() throws Exception {
        CreateRSAMakeDecrypt(2048, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_03_07_CreateRsaAndDecryptRsaOAEP() throws Exception {
        CreateRSAMakeDecrypt(3072, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_03_08_CreateRsaAndDecryptRsaOAEP() throws Exception {
        CreateRSAMakeDecrypt(3072, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_03_09_CreateRsaAndDecryptRsaOAEP() throws Exception {
        CreateRSAMakeDecrypt(4096, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_03_10_CreateRsaAndDecryptRsaOAEP() throws Exception {
        CreateRSAMakeDecrypt(4096, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_04_01_ImportRsaAndEncryptRsaOAEP() throws Exception {
        importRSAMakeEncrypt(1024, Algorithms.DIGEST_SHA1);
    }

    // It is not possible

    @Test
    public void test_04_02_ImportRsaAndEncryptRsaOAEP() throws Exception {
        importRSAMakeEncrypt(1024, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_04_03_ImportRsaAndEncryptRsaOAEP() throws Exception {
        importRSAMakeEncrypt(1536, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_04_04_ImportRsaAndEncryptRsaOAEP() throws Exception {
        importRSAMakeEncrypt(1536, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_04_05_ImportRsaAndEncryptRsaOAEP() throws Exception {
        importRSAMakeEncrypt(2048, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_04_06_ImportRsaAndEncryptRsaOAEP() throws Exception {
        importRSAMakeEncrypt(2048, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_04_07_ImportRsaAndEncryptRsaOAEP() throws Exception {
        importRSAMakeEncrypt(3072, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_04_08_ImportRsaAndEncryptRsaOAEP() throws Exception {
        importRSAMakeEncrypt(3072, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_04_09_ImportRsaAndEncryptRsaOAEP() throws Exception {
        importRSAMakeEncrypt(4096, Algorithms.DIGEST_SHA1);
    }

    @Test
    public void test_04_10_ImportRsaAndEncryptRsaOAEP() throws Exception {
        importRSAMakeEncrypt(4096, Algorithms.DIGEST_SHA256);
    }

    @Test
    public void test_04_11_EncryptionWithBadLengthInput()
        throws Exception {
        byte[] tobeencrypted = new byte[255];
        new Random().nextBytes(tobeencrypted);

        generateAndImportRSAEncKey(2048);

        try {
            CK_RSA_PKCS_OAEP_PARAMS params = new CK_RSA_PKCS_OAEP_PARAMS();

            params.hashAlg = PKCS11Constants.CKM_SHA_1;
            params.mgf = PKCS11Constants.CKG_MGF1_SHA1;
            params.source = PKCS11Constants.CKZ_DATA_SPECIFIED;
            params.pSourceData = null;

            CK_MECHANISM mechanism = new CK_MECHANISM(0L);
            mechanism.mechanism = PKCS11Constants.CKM_RSA_PKCS_OAEP;
            mechanism.pParameter = params;

            sc.encryptData(sid, IMPORTED_KEY_LABEL, tobeencrypted, mechanism);
        } catch (PKCS11Exception aEx) {
            return;
        } finally {
            CardTestUtil.deleteKeyPair(sc, sid, IMPORTED_KEY_LABEL);
        }

        Assert.fail();
    }

    //sifreleme anahtari ile imzalamaya izin vermemesi gerekiyor
    @Test
    public void test_04_12_SigningWithEncryptionKeys()
        throws Exception {

        generateAndImportRSAEncKey(2048);

        try {
            _signAtHSMAndVerifyRSA_PSS(DigestAlg.SHA224, IMPORTED_KEY_LABEL);
        } catch (PKCS11Exception aEx) {
            if (aEx.getErrorCode() == PKCS11Constants.CKR_KEY_FUNCTION_NOT_PERMITTED ||
                    aEx.getErrorCode() == PKCS11Constants.CKR_MECHANISM_INVALID) {
                Assert.assertTrue(true);
                return;
            } else {
                throw new Exception("Wrong Error is generated. CKR_KEY_FUNCTION_NOT_PERMITTED must be generated");
            }
        } finally {
            CardTestUtil.deleteKeyPair(sc, sid, IMPORTED_KEY_LABEL);
        }

        throw new Exception("No Error is generated. CKR_KEY_FUNCTION_NOT_PERMITTED must be generated");
    }

    @Test
    public void test_05_01_CreateRSA_2048_KeyAndExport() throws Exception {
        byte[] keyBytes = sc.generateRSAPrivateKey(sid, 2048);
        System.out.println(StringUtil.toHexString(keyBytes));
    }

    @Test
    public void test_05_02_CreateRSA_4096_KeyAndExport() throws Exception {
        byte[] keyBytes = sc.generateRSAPrivateKey(sid, 4096);
        System.out.println(StringUtil.toHexString(keyBytes));
    }

    @Test
    public void test_06_01_MultiplePSS_Sign() throws Exception {
        int keyLen = 2048;
        String pssHashAlg = Algorithms.DIGEST_SHA256;
        int signingCount = 10;

        CardTestUtil.clearKeyPairIfExist(sc, sid, CREATED_KEY_LABEL_FOR_SIGNING);

        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keyLen, null);
        sc.createKeyPair(sid, CREATED_KEY_LABEL_FOR_SIGNING, spec, true, false);
        boolean priFound = sc.isPrivateKeyExist(sid, CREATED_KEY_LABEL_FOR_SIGNING);
        boolean pubFound = sc.isPublicKeyExist(sid, CREATED_KEY_LABEL_FOR_SIGNING);

        Assert.assertTrue(priFound && pubFound);

        DigestAlg digestAlg = DigestAlg.fromName(pssHashAlg);
        Assert.assertNotNull(digestAlg);

        int dataToBeSignedLen = 10 + new Random().nextInt(30);
        tobesigned = RandomUtil.generateRandom(dataToBeSignedLen);

        CK_MECHANISM mech = RSAPSS_SS.getDefaultMechanismForPSS(digestAlg);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < signingCount; i++) {
            signature = sc.signData(sid, CREATED_KEY_LABEL_FOR_SIGNING, tobesigned, mech);
        }
        long endTime = System.currentTimeMillis();

        System.out.println(signingCount + " RSAPSS " + keyLen + " signature time: " + ((double) (endTime - startTime)) / 1000 + " Seconds");
        //System.out.println("ObjeAra Seconds: " + ((double)(PKCS11Ops.objeAraSure))/1000);
        //System.out.println("Signing Seconds: " + ((double)(PKCS11Ops.signingSure))/1000);

        CardTestUtil.deleteKeyPair(sc, sid, CREATED_KEY_LABEL_FOR_SIGNING);
    }

    @Test
    public void test_06_02_MultipleKeyCreation() throws Exception {
        int keyLen = 2048;
        int count = 10;
        String keyLabel = "KeyCreationPerformanceTest_";

        for (int i = 0; i < count; i++)
            CardTestUtil.clearKeyPairIfExist(sc, sid, keyLabel + count);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keyLen, null);
            sc.createKeyPair(sid, keyLabel + i, spec, true, false);
        }
        long endTime = System.currentTimeMillis();

        System.out.println("Total Seconds: " + ((double) (endTime - startTime)) / 1000);


        for (int i = 0; i < count; i++)
            CardTestUtil.deleteKeyPair(sc, sid, keyLabel + i);
    }

    @Test
    public void test_07_01_GNUSignHSMVerify() throws Exception {
        CardTestUtil.clearKeyPairIfExist(sc, sid, IMPORTED_KEY_LABEL);

        PublicKey publicKey = KeyUtil.decodePublicKey(AsymmetricAlg.RSA, Base64.decode(DirakTestConstants.testPublicKey));
        PrivateKey privateKey = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, Base64.decode(DirakTestConstants.testPrivateKey));
        KeyPair keyPair = new KeyPair(publicKey, privateKey);

        sc.importKeyPair(sid, IMPORTED_KEY_LABEL, keyPair, null, true, false);

        byte[] tobesigned = new byte[147];
        new Random().nextBytes(tobesigned);
        byte[] sig = SignUtil.sign(SignatureAlg.RSA_NONE, tobesigned, privateKey);

        sc.verifyData(sid, IMPORTED_KEY_LABEL, tobesigned, sig, PKCS11Constants.CKM_RSA_PKCS);
    }

    @Test
    public void test_08_CreateRSAKeysAndEncDecTest()
        throws Exception {
        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
        sc.createKeyPair(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION, spec, false, true);
        boolean priFound = sc.isPrivateKeyExist(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
        boolean pubFound = sc.isPublicKeyExist(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
        Assert.assertTrue(priFound && pubFound);

        // encryption test

        byte[] tobeencrypted = new byte[125];
        new Random().nextBytes(tobeencrypted);

        byte[] toBeDecrypted = testEncryption(tobeencrypted);

        CK_RSA_PKCS_OAEP_PARAMS params = new CK_RSA_PKCS_OAEP_PARAMS();

        params.hashAlg = PKCS11Constants.CKM_SHA224;
        params.mgf = PKCS11Constants.CKG_MGF1_SHA224;
        params.source = PKCS11Constants.CKZ_DATA_SPECIFIED;
        params.pSourceData = null;

        CK_MECHANISM mechanism = new CK_MECHANISM(0L);
        mechanism.mechanism = PKCS11Constants.CKM_RSA_PKCS_OAEP;
        mechanism.pParameter = params;

        byte[] result = sc.decryptData(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION, toBeDecrypted, mechanism);
        Assert.assertArrayEquals(tobeencrypted, result);
    }

    //gnu da encrypt edilen datayi decrypt yapma
    @Test
    public void test_31_2_CreateRSAKeysAndEncDecTest() throws Exception {
        {
            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
            sc.createKeyPair(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION, spec, false, true);
            boolean priFound = sc.isPrivateKeyExist(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
            boolean pubFound = sc.isPublicKeyExist(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
            Assert.assertTrue(priFound && pubFound);
        }

        // ---

        byte[] tobeencrypted = new byte[125];
        new Random().nextBytes(tobeencrypted);

        RSAPublicKeySpec spec = (RSAPublicKeySpec) sc.readPublicKeySpec(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
        GnuRSAPublicKey pubkey = new GnuRSAPublicKey(spec.getModulus(), spec.getPublicExponent());
        byte[] encrypted = CipherUtil.encrypt(CipherAlg.RSA_PKCS1, null, tobeencrypted, pubkey);

        byte[] result = sc.decryptData(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION, encrypted, new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS));

        Assert.assertArrayEquals(tobeencrypted, result);
    }

    // ---

    private void encryptAndDecryptAtHSM(String oaepHashAlg) throws Exception {
        byte[] tobeencrypted = new byte[32];
        new Random().nextBytes(tobeencrypted);

        CipherAlg cipherAlg;

        if (oaepHashAlg.equals(Algorithms.DIGEST_SHA1))
            cipherAlg = CipherAlg.RSA_OAEP;
        else if (oaepHashAlg.equals(Algorithms.DIGEST_SHA256))
            cipherAlg = CipherAlg.RSA_OAEP_SHA256;
        else
            throw new Exception("Unknown OAEP hash Alg!");

        RSAPublicKeySpec spec = (RSAPublicKeySpec) sc.readPublicKeySpec(sid, DirakRSATest.CREATED_KEY_LABEL_FOR_ENCRYPTION);
        GnuRSAPublicKey pk = new GnuRSAPublicKey(spec.getModulus(), spec.getPublicExponent());
        byte[] encrypted = CipherUtil.encrypt(cipherAlg, null, tobeencrypted, pk);

        CK_RSA_PKCS_OAEP_PARAMS params = new CK_RSA_PKCS_OAEP_PARAMS();
        params.hashAlg = ConstantsUtil.convertHashAlgToPKCS11Constant(oaepHashAlg);
        params.mgf = ConstantsUtil.getMGFAlgorithm(params.hashAlg);
        params.source = PKCS11Constants.CKZ_DATA_SPECIFIED;
        params.pSourceData = null;

        CK_MECHANISM mechanism = new CK_MECHANISM(0L);
        mechanism.mechanism = PKCS11Constants.CKM_RSA_PKCS_OAEP;
        mechanism.pParameter = params;

        byte[] result = sc.decryptData(sid, DirakRSATest.CREATED_KEY_LABEL_FOR_ENCRYPTION, encrypted, mechanism);
        Assert.assertArrayEquals(result, tobeencrypted);
    }

    private void CreateRSAMakeDecrypt(int keyLen, String oaepHashAlg) throws Exception {
        CardTestUtil.clearKeyPairIfExist(sc, sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);

        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keyLen, null);
        sc.createKeyPair(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION, spec, false, true);
        boolean priFound = sc.isPrivateKeyExist(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
        boolean pubFound = sc.isPublicKeyExist(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);

        Assert.assertTrue(priFound && pubFound);

        encryptAndDecryptAtHSM(oaepHashAlg);

        CardTestUtil.deleteKeyPair(sc, sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
    }

    public void _ImportRSAKeyAndPSS_Sign(int keyLen, String digestAlg) throws Exception {
        CardTestUtil.clearKeyPairIfExist(sc, sid, IMPORTED_KEY_LABEL);

        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, keyLen);
        sc.importKeyPair(sid, IMPORTED_KEY_LABEL, keyPair, null, true, false);

        boolean r3 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL);
        boolean r4 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL);

        Assert.assertTrue(r3 && r4);

        _signAtHSMAndVerifyRSA_PSS(DigestAlg.fromName(digestAlg), IMPORTED_KEY_LABEL);

        _signWithLibAndVerifyRSA_PSS(DigestAlg.fromName(digestAlg), keyPair, IMPORTED_KEY_LABEL);

        CardTestUtil.deleteKeyPair(sc, sid, IMPORTED_KEY_LABEL);
    }

    private void _signWithLibAndVerifyRSA_PSS(DigestAlg aDigestAlg, KeyPair aKeyPair, String importedKeyLabel) throws Exception {
        int dataToBeSignedLen = 10 + new Random().nextInt(30);

        RSAPSSParams rsapssParams = new RSAPSSParams(aDigestAlg);
        byte[] toBeSigned = RandomUtil.generateRandom(dataToBeSignedLen);
        byte[] signature = SignUtil.sign(SignatureAlg.RSA_PSS, rsapssParams, toBeSigned, aKeyPair.getPrivate());
        CK_MECHANISM mech = RSAPSS_SS.getDefaultMechanismForPSS(aDigestAlg);

        SignUtil.verify(SignatureAlg.RSA_PSS, rsapssParams, toBeSigned, signature, aKeyPair.getPublic());

        sc.verifyData(sid, importedKeyLabel, toBeSigned, signature, mech);
    }

    private void importRSAMakeEncrypt(int keySize, String oaepHashAlg) throws Exception {
        KeyPair kp = generateAndImportRSAEncKey(keySize);

        encryptAtHSMAndDecrypt(kp, oaepHashAlg);

        CardTestUtil.deleteKeyPair(sc, sid, IMPORTED_KEY_LABEL);
    }

    private KeyPair generateAndImportRSAEncKey(int keySize) throws Exception {

        CardTestUtil.clearKeyPairIfExist(sc, sid, IMPORTED_KEY_LABEL);

        KeyPair kp = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, keySize);

        sc.importKeyPair(sid, DirakRSATest.IMPORTED_KEY_LABEL, kp, null, false, true);
        boolean priFound = sc.isPrivateKeyExist(sid, DirakRSATest.IMPORTED_KEY_LABEL);
        boolean pubFound = sc.isPublicKeyExist(sid, DirakRSATest.IMPORTED_KEY_LABEL);

        Assert.assertTrue(priFound && pubFound);

        return kp;
    }

    private void encryptAtHSMAndDecrypt(KeyPair kp, String oaepHashAlg) throws Exception {
        byte[] tobeencrypted = new byte[32];
    // fixme data length constraint for (hash alg.: Algorithms.DIGEST_SHA256; key length: 1024)
    // fixme data length constraint for (hash alg.: Algorithms.DIGEST_SHA256; key length: 1024)
        new Random().nextBytes(tobeencrypted);

        CipherAlg cipherAlg;

        if (oaepHashAlg.equals(Algorithms.DIGEST_SHA1))
            cipherAlg = CipherAlg.RSA_OAEP;
        else if (oaepHashAlg.equals(Algorithms.DIGEST_SHA256))
            cipherAlg = CipherAlg.RSA_OAEP_SHA256;
        else
            throw new Exception("Unknown OAEP hash Alg!");

        CK_RSA_PKCS_OAEP_PARAMS params = new CK_RSA_PKCS_OAEP_PARAMS();
        params.hashAlg = ConstantsUtil.convertHashAlgToPKCS11Constant(oaepHashAlg);
        params.mgf = ConstantsUtil.getMGFAlgorithm(params.hashAlg);
        params.source = PKCS11Constants.CKZ_DATA_SPECIFIED;
        params.pSourceData = null;

        CK_MECHANISM mechanism = new CK_MECHANISM(0L);
        mechanism.mechanism = PKCS11Constants.CKM_RSA_PKCS_OAEP;
        mechanism.pParameter = params;

        byte[] encrypted = sc.encryptData(sid, DirakRSATest.IMPORTED_KEY_LABEL, tobeencrypted, mechanism);
        byte[] decrypted = CipherUtil.decrypt(cipherAlg, null, encrypted, kp.getPrivate());

        Assert.assertArrayEquals(decrypted, tobeencrypted);
    }


    public void _CreateRSASigningKey(int keyLen) throws Exception {
        CardTestUtil.clearKeyPairIfExist(sc, sid, CREATED_KEY_LABEL_FOR_SIGNING);

        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keyLen, null);

        // sc.createKeyPair(sid, CREATED_KEY_LABEL_FOR_SIGNING, spec, true, false);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(CREATED_KEY_LABEL_FOR_SIGNING, spec);
        rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);

        sc.createKeyPair(sid, rsaKeyPairTemplate);

        boolean priFound = sc.isPrivateKeyExist(sid, CREATED_KEY_LABEL_FOR_SIGNING);
        boolean pubFound = sc.isPublicKeyExist(sid, CREATED_KEY_LABEL_FOR_SIGNING);

        Assert.assertTrue(priFound && pubFound);
    }

    public void _CreateRSAKeyAndPSS_Sign(int keyLen, String pssHashAlg) throws Exception {
        _CreateRSASigningKey(keyLen);

        _signAtHSMAndVerifyRSA_PSS(DigestAlg.fromName(pssHashAlg), CREATED_KEY_LABEL_FOR_SIGNING);

        CardTestUtil.deleteKeyPair(sc, sid, CREATED_KEY_LABEL_FOR_SIGNING);
    }

    private void _signAtHSMAndVerifyRSA_PSS(DigestAlg digestAlg, String keyLabel) throws Exception {
        int dataToBeSignedLen = 10 + new Random().nextInt(30);
        tobesigned = RandomUtil.generateRandom(dataToBeSignedLen);

        CK_MECHANISM mech = RSAPSS_SS.getDefaultMechanismForPSS(digestAlg);

        signature = sc.signData(sid, keyLabel, tobesigned, mech);

        RSAPublicKeySpec spec = (RSAPublicKeySpec) sc.readPublicKeySpec(sid, keyLabel);

        RSAPSSParams rsapssParams = new RSAPSSParams(digestAlg);

        boolean result = _gnuVerify(rsapssParams, spec, tobesigned, signature);
        Assert.assertTrue(result);

        sc.verifyData(sid, keyLabel, tobesigned, signature, mech);
    }

    private boolean _gnuVerify(AlgorithmParams params, RSAPublicKeySpec aSpec, byte[] aData, byte[] aSignature)
        throws Exception {
        GnuRSAPublicKey pk = new GnuRSAPublicKey(aSpec.getModulus(), aSpec.getPublicExponent());
        return SignUtil.verify(SignatureAlg.RSA_PSS, params, aData, aSignature, pk);
    }

    private byte[] testEncryption(byte[] tobeencrypted) throws PKCS11Exception, SmartCardException {
        CK_RSA_PKCS_OAEP_PARAMS params = new CK_RSA_PKCS_OAEP_PARAMS();

        params.hashAlg = PKCS11Constants.CKM_SHA224;
        params.mgf = PKCS11Constants.CKG_MGF1_SHA224;
        params.source = PKCS11Constants.CKZ_DATA_SPECIFIED;
        params.pSourceData = null;

        CK_MECHANISM mechanism = new CK_MECHANISM(0L);
        mechanism.mechanism = PKCS11Constants.CKM_RSA_PKCS_OAEP;
        mechanism.pParameter = params;

        return sc.encryptData(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION, tobeencrypted, mechanism);
    }
}
