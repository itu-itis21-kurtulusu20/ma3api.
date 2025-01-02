package tr.gov.tubitak.uekae.esya.api.smartcard.example.smartcardmanager;

import org.junit.Assert;
import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.SampleBase;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.DirakLibOps;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.DirakLibOps.CryptoOperation;

import java.io.IOException;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Random;

public class DirakLibTest extends SampleBase {

    String dirakUsername = null;
    String dirakPassword = null;

    @Test
    public void testDirakUserLogin() {
        DirakLibOps.userLogin(dirakUsername, dirakPassword);
    }

    @Test
    public void testDirakUserLogout() {
        DirakLibOps.userLogout(dirakUsername);
    }

    @Test
    public void testDirakUnwrapAndOP_Sign_CKM_AES_KEY_WRAP() throws PKCS11Exception, IOException {
        dirakUnwrapAndOP_Sign(new CK_MECHANISM(PKCS11Constants.CKM_AES_KEY_WRAP_PAD));
    }

    @Test
    public void testDirakUnwrapAndOP_Sign_CKM_AES_CBC_PAD() throws PKCS11Exception, IOException {
        dirakUnwrapAndOP_Sign(new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, RandomUtil.generateRandom(16)));
    }

    public void dirakUnwrapAndOP_Sign(CK_MECHANISM wrapUnwrapMechanism) throws PKCS11Exception, IOException {
        SmartCard sc = new SmartCard(CardType.DIRAKHSM);
        long sessionID = 0;

        String keyLabel = "SignKey";
        String wrapperKeyLabel = "WrapperKey";

        boolean testSuccess;

        try {
            // get slot and open session
            sessionID = sc.openSession(getSlot(sc));

            sc.login(sessionID, "123456");

            // create keys
            // final long signingKeyID; // private or secret key
            final long verifyingKeyID;
            final long wrapperKeyID;
            {
                // create operation key
                {
                    // // symmetric key
                    // SecretKeyTemplate template = new AESKeyTemplate(keyLabel, 32);
                    // template.getAsExportableTemplate();
                    // template.getAsTokenTemplate(true, false, false);
                    // signingKeyID = sc.createSecretKey(sessionID, template);
                    // sc.createSecretKey(sessionID, template);

                    // asymmetric key
                    RSAKeyPairTemplate template = new RSAKeyPairTemplate(keyLabel, new RSAKeyGenParameterSpec(2048, null));
                    template.getAsTokenTemplate(true, false, false);
                    template.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true));
                    template.getPrivateKeyTemplate().remove(PKCS11Constants.CKA_TOKEN);
                    template.getPublicKeyTemplate().remove(PKCS11Constants.CKA_TOKEN);
                    sc.createKeyPair(sessionID, template);

                    // signingKeyID = template.getPrivateKeyTemplate().getKeyId();
                    verifyingKeyID = template.getPublicKeyTemplate().getKeyId();
                }

                // create wrapper key
                {
                    SecretKeyTemplate template = new AESKeyTemplate(wrapperKeyLabel, 32);
                    template.getAsTokenTemplate(false, false, true);
                    template.remove(PKCS11Constants.CKA_TOKEN);
                    wrapperKeyID = sc.createSecretKey(sessionID, template);
                }
            }

            // wrap mechanism and key
            final byte[] wrappedKey = sc.wrapKey(sessionID, wrapUnwrapMechanism, wrapperKeyLabel, keyLabel);

            // get data to be operated on
            final byte[] operationData = RandomUtil.generateRandom(16 + new Random().nextInt(229)); // 229 == (245 - 16)
            // (debug)
            // System.out.println(MessageFormat.format("operationData ({0}): {1}", operationData.length, StringUtil.toHexString(operationData)));

            final CK_MECHANISM signMechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);

            CK_ATTRIBUTE[] unwrapTemplate = new CK_ATTRIBUTE[]{
                new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_PRIVATE_KEY),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_RSA),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN, true),
            };

            final byte[] signedData = sc.unwrapAndOP(
                sessionID,
                wrapUnwrapMechanism,
                wrapperKeyID,
                wrappedKey,
                unwrapTemplate,
                CryptoOperation.SIGN,
                signMechanism,
                operationData
            );

            // (debug)
            // System.out.println(MessageFormat.format("signedData ({0}): {1}", signedData.length, StringUtil.toHexString(signedData)));

            // verify signature
            sc.verifyData(sessionID, verifyingKeyID, operationData, signedData, signMechanism);

            // since an exception is thrown when signature verification fails, passing beyond this point should indicate verification success
            testSuccess = true;
            System.out.println("Test successful");
        } catch (Exception e) {
            testSuccess = false;
            e.printStackTrace();
        } finally {
            // delete keys created for test
            if (sc.isObjectExist(sessionID, keyLabel)) {
                try {
                    deleteObject(sc, sessionID, keyLabel);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (sc.isObjectExist(sessionID, wrapperKeyLabel)) {
                try {
                    deleteObject(sc, sessionID, wrapperKeyLabel);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            // leave the slot
            if (sessionID != 0) {
                sc.closeSession(sessionID);
            }
        }

        assert testSuccess;
    }

    @Test
    public void testDirakUnwrapAndOP_EncryptDecrypt_CKM_AES_KEY_WRAP() throws PKCS11Exception, IOException {
        dirakUnwrapAndOP_EncryptDecrypt(new CK_MECHANISM(PKCS11Constants.CKM_AES_KEY_WRAP));
    }

    @Test
    public void testDirakUnwrapAndOP_EncryptDecrypt_CKM_AES_CBC_PAD() throws PKCS11Exception, IOException {
        dirakUnwrapAndOP_EncryptDecrypt(new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, RandomUtil.generateRandom(16)));
    }

    public void dirakUnwrapAndOP_EncryptDecrypt(CK_MECHANISM wrapUnwrapMechanism) throws PKCS11Exception, IOException {
        SmartCard sc = new SmartCard(CardType.DIRAKHSM);
        long sessionID = 0;

        String keyLabel = "EncryptDecryptKey";
        String wrapperKeyLabel = "WrapperKey";

        boolean testSuccess;

        try {
            // get slot and open session
            sessionID = sc.openSession(getSlot(sc));

            sc.login(sessionID, "123456");

            // create keys
            final long wrapperKeyID;
            {
                // create operation key
                {
                    // symmetric key
                    SecretKeyTemplate template = new AESKeyTemplate(keyLabel, 32);
                    template.getAsExportableTemplate();
                    template.getAsTokenTemplate(true, true, false);
                    template.remove(PKCS11Constants.CKA_TOKEN);
                    sc.createSecretKey(sessionID, template);

                    // asymmetric key
                    // RSAKeyPairTemplate template = new RSAKeyPairTemplate(keyLabel, new RSAKeyGenParameterSpec(1024, null));
                    // template.getAsTokenTemplate(true, false, false);
                    // template.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true));
                    // sc.createKeyPair(sessionID, template);
                }

                // create wrapper key
                {
                    SecretKeyTemplate template = new AESKeyTemplate(wrapperKeyLabel, 32);
                    template.getAsTokenTemplate(false, false, true);
                    template.remove(PKCS11Constants.CKA_TOKEN);
                    wrapperKeyID = sc.createSecretKey(sessionID, template);
                }
            }

            // wrap mechanism and key
            final byte[] wrappedKey = sc.wrapKey(sessionID, wrapUnwrapMechanism, wrapperKeyLabel, keyLabel);

            // get data to be operated on
            final byte[] operationData = RandomUtil.generateRandom(16 + new Random().nextInt(240));
            // (debug)
            // System.out.println(MessageFormat.format("operationData ({0}): {1}", operationData.length, StringUtil.toHexString(operationData)));

            CK_ATTRIBUTE[] unwrapTemplateEncrypt = new CK_ATTRIBUTE[]{
                new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_SECRET_KEY),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_AES),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_ENCRYPT, true),
            };

            CK_ATTRIBUTE[] unwrapTemplateDecrypt = new CK_ATTRIBUTE[]{
                new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_SECRET_KEY),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_AES),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, true),
            };

            final CK_MECHANISM encryptDecryptMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_KEY_WRAP_PAD);

            final byte[] decryptedData;
            {
                byte[] encryptedData = sc.unwrapAndOP(
                    sessionID,
                    wrapUnwrapMechanism,
                    wrapperKeyID,
                    wrappedKey,
                    unwrapTemplateEncrypt,
                    CryptoOperation.ENCRYPT,
                    encryptDecryptMechanism,
                    operationData
                );

                // (debug)
                // System.out.println(MessageFormat.format("encryptedData ({0}): {1}", encryptedData.length, StringUtil.toHexString(encryptedData)));

                decryptedData = sc.unwrapAndOP(
                    sessionID,
                    wrapUnwrapMechanism,
                    wrapperKeyID,
                    wrappedKey,
                    unwrapTemplateDecrypt,
                    CryptoOperation.DECRYPT,
                    encryptDecryptMechanism,
                    encryptedData
                );
            }

            // (debug)
            // System.out.println(MessageFormat.format("decryptedData ({0}): {1}", decryptedData.length, StringUtil.toHexString(decryptedData)));

            // verify that the original data is obtained
            testSuccess = true;
            Assert.assertArrayEquals(operationData, decryptedData);

            System.out.println("Test successful");
        } catch (Exception e) {
            testSuccess = false;
            e.printStackTrace();
        } finally {
            // delete keys created for test
            if (sc.isObjectExist(sessionID, keyLabel)) {
                try {
                    deleteObject(sc, sessionID, keyLabel);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (sc.isObjectExist(sessionID, wrapperKeyLabel)) {
                try {
                    deleteObject(sc, sessionID, wrapperKeyLabel);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            // leave the slot
            if (sessionID != 0) {
                sc.closeSession(sessionID);
            }
        }

        assert testSuccess;
    }

    private static long getSlot(SmartCard sc) throws PKCS11Exception {
        long[] slots = sc.getTokenPresentSlotList();
        return slots[0];
    }

    protected void deleteObject(SmartCard sc, long aSessionID, String aLabel)
        throws PKCS11Exception {
        CK_ATTRIBUTE[] template = {new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, aLabel)};

        long[] objectList = sc.findObjects(aSessionID, template);

        for (int i = 0; i < objectList.length; i++) {
            sc.getPKCS11().C_DestroyObject(aSessionID, objectList[i]);
        }
    }
}
