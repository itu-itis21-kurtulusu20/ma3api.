package dev.esya.api.smartcard.dirak.wrap;

import dev.esya.api.smartcard.dirak.CardTestUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.*;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.DirakLibOps;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme.RSAPSS_SS;

import java.io.IOException;
import java.security.spec.RSAKeyGenParameterSpec;
import java.text.MessageFormat;
import java.util.Random;

public class DirakUnwrapAndOpFuncTest {

    SmartCard sc;
    long slotID;
    long sessionID;

    static final String PIN = "12345678";
    static final String wrapperKeyLabel = "wrapKey";
    static final String operationKeyLabel = "opKey";

    @Before
    public void setUp() throws PKCS11Exception, IOException {
        sc = new SmartCard(CardType.DIRAKHSM);
        slotID = CardTestUtil.getSlot(sc);
        // (debug)
        System.out.println(MessageFormat.format("Using slot {0} ({1})", slotID, Long.toHexString(slotID)));

        sessionID = sc.openSession(slotID);

        sc.login(sessionID, PIN);
    }

    @After
    public void cleanUp() throws PKCS11Exception {
        // delete keys created for test
        if (sc.isObjectExist(sessionID, operationKeyLabel)) {
            try {
                CardTestUtil.deleteObjects(sc, sessionID, operationKeyLabel);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (sc.isObjectExist(sessionID, wrapperKeyLabel)) {
            try {
                CardTestUtil.deleteObjects(sc, sessionID, wrapperKeyLabel);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // leave session
        sc.logout(sessionID);
        sc.closeSession(sessionID);
    }

    // ---

    @Test
    public void DirakUnwrapAndOPFuncTest_Sign() {
        boolean testSuccess = false;
        try {
            final long wrapperKeyID;
            {
                SecretKeyTemplate template = new AESKeyTemplate(wrapperKeyLabel, 32);
                template.getAsTokenTemplate(false, false, true);
                template.remove(PKCS11Constants.CKA_TOKEN);
                wrapperKeyID = sc.createSecretKey(sessionID, template);
            }

            // create operation key
            final long verifyingKeyID;
            {
                // asymmetric key
                RSAKeyPairTemplate template = new RSAKeyPairTemplate(operationKeyLabel, new RSAKeyGenParameterSpec(2048, null));
                template.getAsTokenTemplate(true, false, false);
                template.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true));
                template.getPrivateKeyTemplate().remove(PKCS11Constants.CKA_TOKEN);
                template.getPublicKeyTemplate().remove(PKCS11Constants.CKA_TOKEN);
                sc.createKeyPair(sessionID, template);

                verifyingKeyID = template.getPublicKeyTemplate().getKeyId();
            }

            final CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_KEY_WRAP_PAD);

            final byte[] wrappedKey = sc.wrapKey(sessionID, wrapMechanism, wrapperKeyLabel, operationKeyLabel);
            final CK_MECHANISM signMechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);

            CK_ATTRIBUTE[] unwrapTemplate = new CK_ATTRIBUTE[]{
                new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_PRIVATE_KEY),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_RSA),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN, true),
            };

            final byte[] operationData = RandomUtil.generateRandom(16 + new Random().nextInt(229)); // data can be a maximum size of 245 bytes due to padding

            final byte[] result = sc.unwrapAndOP(
                sessionID,
                wrapMechanism,
                wrapperKeyID,
                wrappedKey,
                unwrapTemplate,
                DirakLibOps.CryptoOperation.SIGN,
                signMechanism,
                operationData
            );

            sc.verifyData(sessionID, verifyingKeyID, operationData, result, signMechanism);
            testSuccess = true;
            System.out.println("Success");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert testSuccess;
    }

    @Test
    public void DirakUnwrapAndOPFuncTest_Sign_RSA_PSS() {
        boolean testSuccess = false;
        try {
            final long wrapperKeyID;
            {
                SecretKeyTemplate template = new AESKeyTemplate(wrapperKeyLabel, 32);
                template.getAsTokenTemplate(false, false, true);
                template.remove(PKCS11Constants.CKA_TOKEN);
                wrapperKeyID = sc.createSecretKey(sessionID, template);
            }

            // create operation key
            final long verifyingKeyID;
            {
                // asymmetric key
                RSAKeyPairTemplate template = new RSAKeyPairTemplate(operationKeyLabel, new RSAKeyGenParameterSpec(2048, null));
                template.getAsTokenTemplate(true, false, false);
                template.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true));
                template.getPrivateKeyTemplate().remove(PKCS11Constants.CKA_TOKEN);
                template.getPublicKeyTemplate().remove(PKCS11Constants.CKA_TOKEN);
                sc.createKeyPair(sessionID, template);

                verifyingKeyID = template.getPublicKeyTemplate().getKeyId();
            }

            final CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_KEY_WRAP_PAD);

            final byte[] wrappedKey = sc.wrapKey(sessionID, wrapMechanism, wrapperKeyLabel, operationKeyLabel);

            final CK_MECHANISM signMechanism = RSAPSS_SS.getDefaultMechanismForPSS(DigestAlg.SHA256);

            CK_ATTRIBUTE[] unwrapTemplate = new CK_ATTRIBUTE[]{
                new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_PRIVATE_KEY),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_RSA),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN, true),
            };

            final byte[] operationData = RandomUtil.generateRandom(16 + new Random().nextInt(1008));

            final byte[] result = sc.unwrapAndOP(
                sessionID,
                wrapMechanism,
                wrapperKeyID,
                wrappedKey,
                unwrapTemplate,
                DirakLibOps.CryptoOperation.SIGN,
                signMechanism,
                operationData
            );

            sc.verifyData(sessionID, verifyingKeyID, operationData, result, signMechanism);
            testSuccess = true;
            System.out.println("Success");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert testSuccess;
    }

    // todo create test for encrypt and decrypt (similar to this one) that uses symmetrical (e.g. AES) key
    @Test
    public void DirakUnwrapAndOPFuncTest_Decrypt() {
        boolean testSuccess = false;
        try {
            final String wrapperKeyLabel = "wrapKey";
            final String operationKeyLabel = "opKey";

            final long wrapperKeyID;
            {
                SecretKeyTemplate template = new AESKeyTemplate(wrapperKeyLabel, 32);
                template.getAsTokenTemplate(false, false, true);
                template.remove(PKCS11Constants.CKA_TOKEN);
                wrapperKeyID = sc.createSecretKey(sessionID, template);
            }

            // create operation key
            final long encryptKeyID;
            // final long decryptKeyID;
            {
                // asymmetric key
                RSAKeyPairTemplate template = new RSAKeyPairTemplate(operationKeyLabel, new RSAKeyGenParameterSpec(2048, null));
                template.getAsTokenTemplate(false, true, false);
                template.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true));
                template.getPrivateKeyTemplate().remove(PKCS11Constants.CKA_TOKEN);
                template.getPublicKeyTemplate().remove(PKCS11Constants.CKA_TOKEN);
                sc.createKeyPair(sessionID, template);

                encryptKeyID = template.getPublicKeyTemplate().getKeyId();
                // decryptKeyID = template.getPrivateKeyTemplate().getKeyId();
            }

            final CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_KEY_WRAP_PAD);

            // wrap key
            final byte[] wrappedKey = sc.wrapKey(sessionID, wrapMechanism, wrapperKeyLabel, operationKeyLabel);

            final CK_MECHANISM operationMechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);

            final byte[] data = RandomUtil.generateRandom(16 + new Random().nextInt(229)); // data can be a maximum size of 245 bytes due to padding

            final byte[] operationData = sc.encryptData(sessionID, encryptKeyID, data, operationMechanism);
            // final byte[] decryptedData = sc.decryptData(sessionID, decryptKeyID, result, operationMechanism);

            CK_ATTRIBUTE[] unwrapTemplate = new CK_ATTRIBUTE[]{
                new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_PRIVATE_KEY),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_RSA),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, true),
            };

            final byte[] result = sc.unwrapAndOP(
                sessionID,
                wrapMechanism,
                wrapperKeyID,
                wrappedKey,
                unwrapTemplate,
                DirakLibOps.CryptoOperation.DECRYPT,
                operationMechanism,
                operationData
            );

            // verify
            Assert.assertArrayEquals(data, result);
            testSuccess = true;
            System.out.println("Success");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert testSuccess;
    }

    @Test
    public void DirakUnwrapAndOPFuncTest_Decrypt_RSA_OAEP() {
        boolean testSuccess = false;
        try {
            final String wrapperKeyLabel = "wrapKey";
            final String operationKeyLabel = "opKey";

            final long wrapperKeyID;
            {
                SecretKeyTemplate template = new AESKeyTemplate(wrapperKeyLabel, 32);
                template.getAsTokenTemplate(false, false, true);
                template.remove(PKCS11Constants.CKA_TOKEN);
                wrapperKeyID = sc.createSecretKey(sessionID, template);
            }

            // create operation key
            final long encryptKeyID;
            // final long decryptKeyID;
            {
                // asymmetric key
                RSAKeyPairTemplate template = new RSAKeyPairTemplate(operationKeyLabel, new RSAKeyGenParameterSpec(2048, null));
                template.getAsTokenTemplate(false, true, false);
                template.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true));
                template.getPrivateKeyTemplate().remove(PKCS11Constants.CKA_TOKEN);
                template.getPublicKeyTemplate().remove(PKCS11Constants.CKA_TOKEN);
                sc.createKeyPair(sessionID, template);

                encryptKeyID = template.getPublicKeyTemplate().getKeyId();
                // decryptKeyID = template.getPrivateKeyTemplate().getKeyId();
            }

            final CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_KEY_WRAP_PAD);

            // wrap key
            final byte[] wrappedKey = sc.wrapKey(sessionID, wrapMechanism, wrapperKeyLabel, operationKeyLabel);

            final CK_MECHANISM operationMechanism;
            {
                CK_RSA_PKCS_OAEP_PARAMS params = new CK_RSA_PKCS_OAEP_PARAMS();

                params.hashAlg = PKCS11Constants.CKM_SHA224;
                params.mgf = PKCS11Constants.CKG_MGF1_SHA224;
                params.source = PKCS11Constants.CKZ_DATA_SPECIFIED;
                params.pSourceData = null;

                operationMechanism = new CK_MECHANISM(0L);
                operationMechanism.mechanism = PKCS11Constants.CKM_RSA_PKCS_OAEP;
                operationMechanism.pParameter = params;
            }

            final byte[] data = RandomUtil.generateRandom(16 + new Random().nextInt(229)); // data can be a maximum size of 245 bytes due to padding

            final byte[] operationData = sc.encryptData(sessionID, encryptKeyID, data, operationMechanism);
            // final byte[] decryptedData = sc.decryptData(sessionID, decryptKeyID, result, operationMechanism);

            CK_ATTRIBUTE[] unwrapTemplate = new CK_ATTRIBUTE[]{
                new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_PRIVATE_KEY),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_RSA),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, true),
            };

            final byte[] result = sc.unwrapAndOP(
                sessionID,
                wrapMechanism,
                wrapperKeyID,
                wrappedKey,
                unwrapTemplate,
                DirakLibOps.CryptoOperation.DECRYPT,
                operationMechanism,
                operationData
            );

            // verify
            Assert.assertArrayEquals(data, result);
            testSuccess = true;
            System.out.println("Success");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert testSuccess;
    }
}
