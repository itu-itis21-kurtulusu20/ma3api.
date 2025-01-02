package dev.esya.api.smartcard.dirak.wrap;

import dev.esya.api.smartcard.dirak.CardTestUtil;
import gnu.crypto.key.rsa.GnuRSAPublicKey;
import org.junit.*;
import org.junit.rules.ExpectedException;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.*;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECPrivateKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.PKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.cardobject.P11Object;

import java.io.IOException;
import java.math.BigInteger;
import java.security.spec.ECParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.text.MessageFormat;
import java.util.Random;
import java.util.StringJoiner;

public class DirakWrapTest {

    static final String PASSWORD = "12345678";
    SmartCard sc;
    long slotNo;
    long sid = 0;

    // test variables

    String wrapperKeyLabel;
    static final int WRAPPER_KEY_SIZE = 16;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    public DirakWrapTest() throws PKCS11Exception, IOException {
        sc = new SmartCard(CardType.DIRAKHSM);
        slotNo = CardTestUtil.getSlot(sc);
    }

    // ---

    @Before
    public void setUp() throws Exception {
        sid = sc.openSession(slotNo);
        sc.login(sid, PASSWORD);

        wrapperKeyLabel = "aes_wrapper_" + System.currentTimeMillis();
    }

    @After
    public void cleanUp() throws Exception {
        sc.logout(sid);
        sc.closeSession(sid);
    }

    // ---

    // --- wrap and unwrap tests

    // AES
    @Test
    public void testAESKeyWrapAndVerify() throws Exception {

        long wrapperKeyID;
        long secretKeyID1 = 0;
        long secretKeyID2 = 0;
        long publicKeyID1 = 0;
        long publicKeyID2 = 0;
        long privateKeyID1 = 0;
        long privateKeyID2 = 0;

        wrapperKeyID = generateWrapperKeyInAPIAndImportToHSM(wrapperKeyLabel, WRAPPER_KEY_SIZE);

        try {

            // create key to be wrapped (the private key from an RSA key pair will be used)
            final String keyLabel = "created_aes_" + System.currentTimeMillis();

            AESKeyTemplate keyTemplate;
            {
                keyTemplate = new AESKeyTemplate(keyLabel, 16);

                keyTemplate.getAsTokenTemplate(false, true, false);
                keyTemplate.getAsExportableTemplate();
                // re-add token attribute which was removed inside the getAsExportableTemplate call
                keyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));

                secretKeyID1 = sc.createSecretKey(sid, keyTemplate);
            }

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);

            // wrap key
            final byte[] wrappedKey = sc.wrapKey(sid, wrapMechanism, wrapperKeyID, secretKeyID1);

            final String unwrapKeyLabel = keyLabel + "unwrapped";

            // unwrap key
            {
                AESKeyTemplate unwrapTemplate = new AESKeyTemplate(unwrapKeyLabel);

                // we want to do both encryption and decryption
                unwrapTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ENCRYPT, true));
                unwrapTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, true));

                // the unwrap
                secretKeyID2 = sc.unwrapKey(sid, wrapMechanism, wrapperKeyID, wrappedKey, unwrapTemplate);
            }

            // verify: encrypt and decrypt

            final byte[] randomData = RandomUtil.generateRandom(63 + new Random().nextInt(449)); // [64...512]

            final byte[] encDecIV = RandomUtil.generateRandom(16);
            CK_MECHANISM encDecMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, encDecIV);

            // encrypt with source key, decrypt with unwrapped key

            byte[] encryptedData1;
            byte[] encryptedData2;
            byte[] decryptedData1;
            byte[] decryptedData2;

            encryptedData1 = sc.encryptData(sid, secretKeyID1, randomData, encDecMechanism);
            decryptedData1 = sc.decryptData(sid, secretKeyID2, encryptedData1, encDecMechanism);

            // encrypt with unwrapped key, decrypt with source key

            encryptedData2 = sc.encryptData(sid, secretKeyID2, randomData, encDecMechanism);
            decryptedData2 = sc.decryptData(sid, secretKeyID1, encryptedData2, encDecMechanism);

            Assert.assertArrayEquals(decryptedData1, decryptedData2);
        } finally {
            // cleanup: delete the keys
            keyCleanup(wrapperKeyID, secretKeyID1, secretKeyID2, publicKeyID1, publicKeyID2, privateKeyID1, privateKeyID2);
        }
    }

    // RSA
    @Test
    public void testRSAKeyPairWrapAndVerify() throws Exception {

        long wrapperKeyID;
        long secretKeyID1 = 0;
        long secretKeyID2 = 0;
        long publicKeyID1 = 0;
        long publicKeyID2 = 0;
        long privateKeyID1 = 0;
        long privateKeyID2 = 0;

        wrapperKeyID = generateWrapperKeyInAPIAndImportToHSM(wrapperKeyLabel, WRAPPER_KEY_SIZE);

        try {

            // create key pair to be wrapped (the public key is "cloned" instead)
            final String keyPairLabel = "created_rsa_" + System.currentTimeMillis();
            {
                RSAKeyPairTemplate keyPairTemplate = new RSAKeyPairTemplate(keyPairLabel, new RSAKeyGenParameterSpec(1024, null));
                keyPairTemplate.getAsTokenTemplate(false, true, false);
                keyPairTemplate.getAsExtractableTemplate();
                keyPairTemplate.getPublicKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));
                keyPairTemplate.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));

                sc.createKeyPair(sid, keyPairTemplate);
                publicKeyID1 = keyPairTemplate.getPublicKeyTemplate().getKeyId();
                privateKeyID1 = keyPairTemplate.getPrivateKeyTemplate().getKeyId();
            }

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);

            // wrap private key
            final byte[] wrappedPrivateKey = sc.wrapKey(sid, wrapMechanism, wrapperKeyID, privateKeyID1);

            // unwrap
            final String unwrappedKeyPairLabel = keyPairLabel + "unwrapped";

            // unwrap private key
            RSAKeyPairTemplate unwrappedTemplate = new RSAKeyPairTemplate(unwrappedKeyPairLabel, new RSAKeyGenParameterSpec(1024, null));
            privateKeyID2 = sc.unwrapKey(sid, wrapMechanism, wrapperKeyID, wrappedPrivateKey, unwrappedTemplate.getPrivateKeyTemplate());

            // transfer public key
            {
                P11Object publicKeyInfo;
                {
                    // update label

                    // dummy template- attributes will be overridden
                    RSAPublicKeyTemplate publicKeyTemplate = new RSAPublicKeyTemplate(unwrappedKeyPairLabel, new GnuRSAPublicKey(BigInteger.ZERO, BigInteger.ZERO));
                    publicKeyInfo = new P11Object(publicKeyID1, publicKeyTemplate.getAttributesAsArr());
                }

                publicKeyID2 = transferPublicKeyIntoSameSlot(sc, sid, publicKeyInfo);
                Assert.assertTrue(sc.isPublicKeyExist(sid, unwrappedKeyPairLabel));
            }

            // verify: encrypt and decrypt

            final byte[] randomData = RandomUtil.generateRandom(95 + new Random().nextInt(25)); // [96...110]

            CK_MECHANISM encDecMechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_X_509);

            // encrypt with source key, decrypt with unwrapped key

            byte[] encryptedData1;
            byte[] encryptedData2;
            byte[] decryptedData1;
            byte[] decryptedData2;

            encryptedData1 = sc.encryptData(sid, publicKeyID1, randomData, encDecMechanism);
            decryptedData1 = sc.decryptData(sid, privateKeyID2, encryptedData1, encDecMechanism);

            // encrypt with unwrapped key, decrypt with source key

            encryptedData2 = sc.encryptData(sid, publicKeyID2, randomData, encDecMechanism);
            decryptedData2 = sc.decryptData(sid, privateKeyID1, encryptedData2, encDecMechanism);

            Assert.assertArrayEquals(decryptedData1, decryptedData2);
        } finally {
            // cleanup: delete the keys
            keyCleanup(wrapperKeyID, secretKeyID1, secretKeyID2, publicKeyID1, publicKeyID2, privateKeyID1, privateKeyID2);
        }
    }

    // EC
    @Test
    public void testECKeyPairWrapAndVerify() throws Exception {

        // define EC spec
        ECParameterSpec ecParameterSpec;
        {
            final String specName = "secp256r1";
            ecParameterSpec = NamedCurve.getECParameterSpec(specName);
        }

        long wrapperKeyID;
        long secretKeyID1 = 0;
        long secretKeyID2 = 0;
        long publicKeyID1 = 0;
        long publicKeyID2 = 0;
        long privateKeyID1 = 0;
        long privateKeyID2 = 0;

        wrapperKeyID = generateWrapperKeyInAPIAndImportToHSM(wrapperKeyLabel, WRAPPER_KEY_SIZE);

        try {

            // create key pair to be wrapped (the public key is "cloned" instead)
            final String keyPairLabel = "created_ec_" + System.currentTimeMillis();
            ECPrivateKeyTemplate privateKeyTemplate;
            {
                ECKeyPairTemplate keyPairTemplate = new ECKeyPairTemplate(keyPairLabel, ecParameterSpec);
                keyPairTemplate.getAsTokenTemplate(true, false, false);
                keyPairTemplate.getAsExtractableTemplate();
                keyPairTemplate.getPublicKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));
                keyPairTemplate.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));

                privateKeyTemplate = (ECPrivateKeyTemplate) keyPairTemplate.getPrivateKeyTemplate();

                sc.createKeyPair(sid, keyPairTemplate);
                publicKeyID1 = keyPairTemplate.getPublicKeyTemplate().getKeyId();
                privateKeyID1 = keyPairTemplate.getPrivateKeyTemplate().getKeyId();
            }

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);

            // wrap private key
            final byte[] wrappedPrivateKey = sc.wrapKey(sid, wrapMechanism, wrapperKeyID, privateKeyID1);

            // unwrap
            final String unwrappedKeyPairLabel = keyPairLabel + "unwrapped";

            // unwrap private key
            {
                ECPrivateKeyTemplate unwrapPrivateKeyTemplate = new ECPrivateKeyTemplate(unwrappedKeyPairLabel, ecParameterSpec);
                unwrapPrivateKeyTemplate.add(privateKeyTemplate.getAttributesAsArr());
                unwrapPrivateKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, unwrappedKeyPairLabel));

                privateKeyID2 = sc.unwrapKey(sid, wrapMechanism, wrapperKeyID, wrappedPrivateKey, unwrapPrivateKeyTemplate);
            }

            // transfer public key
            {
                P11Object publicKeyInfo;
                {
                    // update label

                    ECPublicKeyTemplate publicKeyTemplate = new ECPublicKeyTemplate(unwrappedKeyPairLabel, ecParameterSpec);
                    publicKeyInfo = new P11Object(publicKeyID1, publicKeyTemplate.getAttributesAsArr());
                }

                publicKeyID2 = transferPublicKeyIntoSameSlot(sc, sid, publicKeyInfo);
                Assert.assertTrue(sc.isPublicKeyExist(sid, unwrappedKeyPairLabel));
            }

            // verify: sign and verify (signature)

            final byte[] randomData = RandomUtil.generateRandom(63 + new Random().nextInt(449)); // [64...512]

            CK_MECHANISM signVerifyMechanism = new CK_MECHANISM(PKCS11Constants.CKM_ECDSA);

            // encrypt with source key, decrypt with unwrapped key

            byte[] signedData1;
            byte[] signedData2;

            signedData1 = sc.signData(sid, keyPairLabel, randomData, signVerifyMechanism);
            sc.verifyData(sid, publicKeyID2, randomData, signedData1, signVerifyMechanism);

            // encrypt with unwrapped key, decrypt with source key

            signedData2 = sc.signData(sid, unwrappedKeyPairLabel, randomData, signVerifyMechanism);
            sc.verifyData(sid, publicKeyID1, randomData, signedData2, signVerifyMechanism);

            // exception is thrown in case of verification failure- test is successful beyond this point.
        } finally {
            // cleanup: delete the keys
            keyCleanup(wrapperKeyID, secretKeyID1, secretKeyID2, publicKeyID1, publicKeyID2, privateKeyID1, privateKeyID2);
        }
    }

    // --- unextractable key extraction failure tests (with CKA_EXTRACTABLE set to false)

    // AES
    @Test
    public void testAESKeyWrapUnextractableWithExtractableSetToFalse() throws Exception {

        long wrapperKeyID;
        long secretKeyID1 = 0;
        long secretKeyID2 = 0;
        long publicKeyID1 = 0;
        long publicKeyID2 = 0;
        long privateKeyID1 = 0;
        long privateKeyID2 = 0;

        wrapperKeyID = generateWrapperKeyInAPIAndImportToHSM(wrapperKeyLabel, WRAPPER_KEY_SIZE);

        try {

            // create key to be wrapped (the private key from an RSA key pair will be used)
            final String keyLabel = "created_aes_" + System.currentTimeMillis();

            AESKeyTemplate keyTemplate;
            {
                keyTemplate = new AESKeyTemplate(keyLabel, 16);

                keyTemplate.getAsTokenTemplate(false, true, false);
                keyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, false));

                secretKeyID1 = sc.createSecretKey(sid, keyTemplate);
            }

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);

            // try wrapping the key: wrap should fail

            exceptionRule.expect(PKCS11Exception.class);
            exceptionRule.expectMessage(PKCS11ExceptionFactory.getPKCS11Exception(PKCS11Constants.CKR_KEY_UNEXTRACTABLE).getMessage());

            final byte[] wrappedKey = sc.wrapKey(sid, wrapMechanism, wrapperKeyID, secretKeyID1);

        } finally {
            // cleanup: delete the keys
            keyCleanup(wrapperKeyID, secretKeyID1, secretKeyID2, publicKeyID1, publicKeyID2, privateKeyID1, privateKeyID2);
        }
    }

    // RSA
    @Test
    public void testRSAKeyPairWrapUnextractableWithExtractableSetToFalse() throws Exception {

        long wrapperKeyID;
        long secretKeyID1 = 0;
        long secretKeyID2 = 0;
        long publicKeyID1 = 0;
        long publicKeyID2 = 0;
        long privateKeyID1 = 0;
        long privateKeyID2 = 0;

        wrapperKeyID = generateWrapperKeyInAPIAndImportToHSM(wrapperKeyLabel, WRAPPER_KEY_SIZE);

        try {

            // create key pair to be wrapped (the public key is "cloned" instead)
            final String keyPairLabel = "created_rsa_" + System.currentTimeMillis();
            {
                RSAKeyPairTemplate keyPairTemplate = new RSAKeyPairTemplate(keyPairLabel, new RSAKeyGenParameterSpec(1024, null));
                keyPairTemplate.getAsTokenTemplate(false, true, false);

                // CKA_EXTRACTABLE is already set to false in the getAsTokenTemplate method call above
                // keyPairTemplate.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, false));

                sc.createKeyPair(sid, keyPairTemplate);
                publicKeyID1 = keyPairTemplate.getPublicKeyTemplate().getKeyId();
                privateKeyID1 = keyPairTemplate.getPrivateKeyTemplate().getKeyId();
            }

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);

            // try wrapping the private key: wrap should fail

            exceptionRule.expect(PKCS11Exception.class);
            exceptionRule.expectMessage(PKCS11ExceptionFactory.getPKCS11Exception(PKCS11Constants.CKR_KEY_UNEXTRACTABLE).getMessage());

            final byte[] wrappedPrivateKey = sc.wrapKey(sid, wrapMechanism, wrapperKeyID, privateKeyID1);

        } finally {
            // cleanup: delete the keys
            keyCleanup(wrapperKeyID, secretKeyID1, secretKeyID2, publicKeyID1, publicKeyID2, privateKeyID1, privateKeyID2);
        }
    }

    // EC
    @Test
    public void testECKeyPairWrapUnextractableWithExtractableSetToFalse() throws Exception {

        // define EC spec
        ECParameterSpec ecParameterSpec;
        {
            final String specName = "secp256r1";
            ecParameterSpec = NamedCurve.getECParameterSpec(specName);
        }

        long wrapperKeyID;
        long secretKeyID1 = 0;
        long secretKeyID2 = 0;
        long publicKeyID1 = 0;
        long publicKeyID2 = 0;
        long privateKeyID1 = 0;
        long privateKeyID2 = 0;

        wrapperKeyID = generateWrapperKeyInAPIAndImportToHSM(wrapperKeyLabel, WRAPPER_KEY_SIZE);

        try {

            // create key pair to be wrapped (the public key is "cloned" instead)
            final String keyPairLabel = "created_ec_" + System.currentTimeMillis();
            {
                ECKeyPairTemplate keyPairTemplate = new ECKeyPairTemplate(keyPairLabel, ecParameterSpec);
                keyPairTemplate.getAsTokenTemplate(true, false, false);

                // CKA_EXTRACTABLE is already set to false in the getAsTokenTemplate method call above
                // keyPairTemplate.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, false));

                sc.createKeyPair(sid, keyPairTemplate);
                publicKeyID1 = keyPairTemplate.getPublicKeyTemplate().getKeyId();
                privateKeyID1 = keyPairTemplate.getPrivateKeyTemplate().getKeyId();
            }

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);

            // try wrapping the private key: wrap should fail

            exceptionRule.expect(PKCS11Exception.class);
            exceptionRule.expectMessage(PKCS11ExceptionFactory.getPKCS11Exception(PKCS11Constants.CKR_KEY_UNEXTRACTABLE).getMessage());

            final byte[] wrappedPrivateKey = sc.wrapKey(sid, wrapMechanism, wrapperKeyID, privateKeyID1);

        } finally {
            // cleanup: delete the keys
            keyCleanup(wrapperKeyID, secretKeyID1, secretKeyID2, publicKeyID1, publicKeyID2, privateKeyID1, privateKeyID2);
        }
    }

    // --- unextractable key extraction failure tests (without CKA_EXTRACTABLE being set to false)

    // AES
    @Test
    public void testAESKeyWrapUnextractable() throws Exception {

        long wrapperKeyID;
        long secretKeyID1 = 0;
        long secretKeyID2 = 0;
        long publicKeyID1 = 0;
        long publicKeyID2 = 0;
        long privateKeyID1 = 0;
        long privateKeyID2 = 0;

        wrapperKeyID = generateWrapperKeyInAPIAndImportToHSM(wrapperKeyLabel, WRAPPER_KEY_SIZE);

        try {

            // create key to be wrapped (the private key from an RSA key pair will be used)
            final String keyLabel = "created_aes_" + System.currentTimeMillis();

            AESKeyTemplate keyTemplate;
            {
                keyTemplate = new AESKeyTemplate(keyLabel, 16);
                keyTemplate.getAsTokenTemplate(false, true, false);

                secretKeyID1 = sc.createSecretKey(sid, keyTemplate);

                // make sure that extractability is false by default
                // todo verify the requirement
                boolean isExtractable;
                {
                    CK_ATTRIBUTE[] isExtractableTemplate = {new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE)};
                    sc.getPKCS11().C_GetAttributeValue(sid, secretKeyID1, isExtractableTemplate);
                    isExtractable = isExtractableTemplate[0].getBoolean();
                }
                Assert.assertFalse(isExtractable);
            }

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);

            // try wrapping the key: wrap should fail

            exceptionRule.expect(PKCS11Exception.class);
            exceptionRule.expectMessage(PKCS11ExceptionFactory.getPKCS11Exception(PKCS11Constants.CKR_KEY_UNEXTRACTABLE).getMessage());

            final byte[] wrappedKey = sc.wrapKey(sid, wrapMechanism, wrapperKeyID, secretKeyID1);

        } finally {
            // cleanup: delete the keys
            keyCleanup(wrapperKeyID, secretKeyID1, secretKeyID2, publicKeyID1, publicKeyID2, privateKeyID1, privateKeyID2);
        }
    }

    // RSA
    @Test
    public void testRSAKeyPairWrapUnextractable() throws Exception {

        long wrapperKeyID;
        long secretKeyID1 = 0;
        long secretKeyID2 = 0;
        long publicKeyID1 = 0;
        long publicKeyID2 = 0;
        long privateKeyID1 = 0;
        long privateKeyID2 = 0;

        wrapperKeyID = generateWrapperKeyInAPIAndImportToHSM(wrapperKeyLabel, WRAPPER_KEY_SIZE);

        try {

            // create key pair to be wrapped (the public key is "cloned" instead)
            final String keyPairLabel = "created_rsa_" + System.currentTimeMillis();
            {
                RSAKeyPairTemplate keyPairTemplate = new RSAKeyPairTemplate(keyPairLabel, new RSAKeyGenParameterSpec(1024, null));
                keyPairTemplate.getAsTokenTemplate(false, true, false);

                sc.createKeyPair(sid, keyPairTemplate);
                publicKeyID1 = keyPairTemplate.getPublicKeyTemplate().getKeyId();
                privateKeyID1 = keyPairTemplate.getPrivateKeyTemplate().getKeyId();

                // make sure that extractability is false by default
                // todo verify the requirement
                boolean isExtractable;
                {
                    CK_ATTRIBUTE[] isExtractableTemplate = {new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE)};
                    sc.getPKCS11().C_GetAttributeValue(sid, privateKeyID1, isExtractableTemplate);
                    isExtractable = isExtractableTemplate[0].getBoolean();
                }
                Assert.assertFalse(isExtractable);
            }

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);

            // try wrapping the private key: wrap should fail

            exceptionRule.expect(PKCS11Exception.class);
            exceptionRule.expectMessage(PKCS11ExceptionFactory.getPKCS11Exception(PKCS11Constants.CKR_KEY_UNEXTRACTABLE).getMessage());

            final byte[] wrappedPrivateKey = sc.wrapKey(sid, wrapMechanism, wrapperKeyID, privateKeyID1);

        } finally {
            // cleanup: delete the keys
            keyCleanup(wrapperKeyID, secretKeyID1, secretKeyID2, publicKeyID1, publicKeyID2, privateKeyID1, privateKeyID2);
        }
    }

    // EC
    @Test
    public void testECKeyPairWrapUnextractable() throws Exception {

        // define EC spec
        ECParameterSpec ecParameterSpec;
        {
            final String specName = "secp256r1";
            ecParameterSpec = NamedCurve.getECParameterSpec(specName);
        }

        long wrapperKeyID;
        long secretKeyID1 = 0;
        long secretKeyID2 = 0;
        long publicKeyID1 = 0;
        long publicKeyID2 = 0;
        long privateKeyID1 = 0;
        long privateKeyID2 = 0;

        wrapperKeyID = generateWrapperKeyInAPIAndImportToHSM(wrapperKeyLabel, WRAPPER_KEY_SIZE);

        try {

            // create key pair to be wrapped (the public key is "cloned" instead)
            final String keyPairLabel = "created_ec_" + System.currentTimeMillis();
            {
                ECKeyPairTemplate keyPairTemplate = new ECKeyPairTemplate(keyPairLabel, ecParameterSpec);
                keyPairTemplate.getAsTokenTemplate(true, false, false);

                sc.createKeyPair(sid, keyPairTemplate);
                publicKeyID1 = keyPairTemplate.getPublicKeyTemplate().getKeyId();
                privateKeyID1 = keyPairTemplate.getPrivateKeyTemplate().getKeyId();

                // make sure that extractability is false by default
                // todo verify the requirement
                boolean isExtractable;
                {
                    CK_ATTRIBUTE[] isExtractableTemplate = {new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE)};
                    sc.getPKCS11().C_GetAttributeValue(sid, privateKeyID1, isExtractableTemplate);
                    isExtractable = isExtractableTemplate[0].getBoolean();
                }
                Assert.assertFalse(isExtractable);
            }

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);

            // try wrapping the private key: wrap should fail

            exceptionRule.expect(PKCS11Exception.class);
            exceptionRule.expectMessage(PKCS11ExceptionFactory.getPKCS11Exception(PKCS11Constants.CKR_KEY_UNEXTRACTABLE).getMessage());

            final byte[] wrappedPrivateKey = sc.wrapKey(sid, wrapMechanism, wrapperKeyID, privateKeyID1);

        } finally {
            // cleanup: delete the keys
            keyCleanup(wrapperKeyID, secretKeyID1, secretKeyID2, publicKeyID1, publicKeyID2, privateKeyID1, privateKeyID2);
        }
    }

    // --- utility methods

    /**
     * Creates a symmetric wrapper key.
     *
     * @param label          Label for the key.
     * @param wrapperKeySize Key size in bytes.
     * @return ID of the created key.
     */
    private long generateWrapperKeyInAPIAndImportToHSM(String label, int wrapperKeySize) throws PKCS11Exception, SmartCardException {

        SecretKeyTemplate secretKeyTemplate;

        // create wrapper key
        final byte[] wrapperKeyBytes = RandomUtil.generateRandom(wrapperKeySize); // wrapperAesKeyTemplate = new AESKeyTemplate(wrapperKeyLabel);

        secretKeyTemplate = new AESKeyTemplate(label);
        secretKeyTemplate.getAsTokenTemplate(false, false, true);
        secretKeyTemplate.getAsWrapperTemplate();
        secretKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE, wrapperKeyBytes));
        secretKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));

        return sc.importSecretKey(sid, secretKeyTemplate);
    }

    private void keyCleanup(long wrapperKeyID, long secretKeyID1, long secretKeyID2, long publicKeyID1, long publicKeyID2, long privateKeyID1, long privateKeyID2) {
        // cleanup: delete the keys

        destroyObject(wrapperKeyID);
        destroyObject(secretKeyID1);
        destroyObject(secretKeyID2);
        destroyObject(publicKeyID1);
        destroyObject(publicKeyID2);
        destroyObject(privateKeyID1);
        destroyObject(privateKeyID2);
    }

    private void destroyObject(long objectID) {
        try {
            if (objectID != 0) {
                sc.getPKCS11().C_DestroyObject(sid, objectID);
            }
        } catch (Exception e) {
            System.err.println("Failed to destroy object with ID: " + objectID);
            e.printStackTrace();
        }
    }

    private static long transferPublicKeyIntoSameSlot(SmartCard sc, long sid, P11Object object) throws PKCS11Exception, SmartCardException {
        return transferPublicKey(sc, sid, sid, object);
    }

    /* RSA is tested. EC is not tested yet. */
    private static long transferPublicKey(SmartCard sc, long sid1, long sid2, P11Object object) throws PKCS11Exception, SmartCardException {
        GenericKeyTemplate publicRSAKeyAttributes = getKeyAttributes(sc, sid1, object);

        removeAttributesFromPublicKey(publicRSAKeyAttributes);

        // strip key label from control characters
        {
            String strippedLabel = stripControlCharacters(publicRSAKeyAttributes.getLabel());
            publicRSAKeyAttributes.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, strippedLabel));
        }

        return ((PKCS11Ops) sc.getCardType().getCardTemplate().getPKCS11Ops()).getmPKCS11().C_CreateObject(sid2, publicRSAKeyAttributes.getAttributesAsArr());
    }

    private static GenericKeyTemplate getKeyAttributes(SmartCard sc, long sid, P11Object objectInfo) throws SmartCardException {
        long objId = objectInfo.getObjectId();

        GenericKeyTemplate keyTemplate = new GenericKeyTemplate(objectInfo.getLabel());
        long[] attrTypes = PKCS11Names.getAttributeTypes();

        for (long attrType : attrTypes) {
            CK_ATTRIBUTE[] attrs = new CK_ATTRIBUTE[1];
            attrs[0] = new CK_ATTRIBUTE(attrType);

            try {
                sc.getAttributeValue(sid, objId, attrs);
                if (attrs[0].pValue != null)
                    keyTemplate.add(attrs[0]);
            } catch (PKCS11Exception ex) {
                if (ex.getErrorCode() == PKCS11Constants.CKR_OBJECT_HANDLE_INVALID) {
                    throw new SmartCardException(MessageFormat.format("Invalid object handle error for key {0} ({1})", objectInfo.getLabel(), objectInfo.getType()));
                } else if (ex.getErrorCode() == PKCS11Constants.CKR_KEY_HANDLE_INVALID) {
                    throw new SmartCardException(MessageFormat.format("Invalid key handle error for key {0} ({1})", objectInfo.getLabel(), objectInfo.getType()));
                }
            }
        }

        keyTemplate.setKeyId(objId);
        return keyTemplate;
    }

    private static void removeAttributesFromPublicKey(GenericKeyTemplate template) {
        template.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ENCRYPT, true));

        // remove attributes that commonly cause template exceptions
        template.remove(PKCS11Constants.CKA_MODULUS_BITS);
        template.remove(PKCS11Constants.CKA_KEY_GEN_MECHANISM);
        template.remove(PKCS11Constants.CKA_LOCAL);

        // remove some extraneous attributes as well
        template.remove(PKCS11Constants.CKA_SUBJECT);
        template.remove(PKCS11Constants.CKA_PRIVATE);
        template.remove(PKCS11Constants.CKA_EXTRACTABLE);
        template.remove(PKCS11Constants.CKA_WRAP);
        template.remove(PKCS11Constants.CKA_VERIFY_RECOVER);
        template.remove(PKCS11Constants.CKA_DERIVE);
        template.remove(PKCS11Constants.CKA_MODIFIABLE);
    }

    private static String stripControlCharacters(String input) {
        StringJoiner joiner = new StringJoiner("");

        for (int c = 0; c < input.length(); c++) {
            char ch = input.charAt(c);

            if (Character.isISOControl(input.charAt(c)))
                continue;

            joiner.add(String.valueOf(ch));
        }

        return joiner.toString();
    }
}
