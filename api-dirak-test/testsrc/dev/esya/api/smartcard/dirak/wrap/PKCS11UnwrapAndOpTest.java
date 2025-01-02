package dev.esya.api.smartcard.dirak.wrap;

import dev.esya.api.smartcard.dirak.CardTestUtil;
import org.junit.*;
import org.junit.rules.ExpectedException;
import sun.security.pkcs11.wrapper.*;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPrivateKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.PKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ECSignatureTLVUtil;

import java.io.IOException;
import java.security.PublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.text.MessageFormat;

public class PKCS11UnwrapAndOpTest {

    SmartCard sc;
    long slotID;
    long sessionID;

    static final String PIN = "12345678";

    static final String wrapperKeyLabel = "wrapKey";
    static final String operationKeyLabel = "opKey";

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

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

    /*
     * Unwrap an RSA key and sign with it.
     * */
    @Test
    public void test_01_01_PKCS11UnWrapAndSignRSA() throws Exception {
        final byte[] iv = RandomUtil.generateRandom(16);

        CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, iv);

        //------------Creating a temporary RSA key pair------------
        long publicKeyID;
        long privateKeyID;
        KeySpec publicKeySpec;
        CK_ATTRIBUTE[] privateKeyAttrs;
        {
            RSAKeyPairTemplate rsaKeyPairTemplate;
            {
                RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
                rsaKeyPairTemplate = new RSAKeyPairTemplate("rsa_temp", spec);
                rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);
                rsaKeyPairTemplate.getAsExtractableTemplate();
            }

            RSAPublicKeyTemplate publicKeyTemplate = rsaKeyPairTemplate.getPublicKeyTemplate();
            RSAPrivateKeyTemplate privateKeyTemplate = rsaKeyPairTemplate.getPrivateKeyTemplate();

            publicKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false));
            privateKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false));

            privateKeyAttrs = privateKeyTemplate.getAttributesAsArr();

            publicKeySpec = sc.createKeyPair(sessionID, rsaKeyPairTemplate);
            publicKeyID = publicKeyTemplate.getKeyId();
            privateKeyID = privateKeyTemplate.getKeyId();
        }
        //----------------------------------------------------

        //------------Creating a wrapper key------------
        long wrapperKeyID;
        {
            AESKeyTemplate aesKeyTemplate = new AESKeyTemplate("wrapper_key", 32);
            SecretKeyTemplate secretKeyTemplate = aesKeyTemplate.getAsTokenTemplate(false, false, true);
            secretKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false));
            wrapperKeyID = sc.createSecretKey(sessionID, secretKeyTemplate);
        }
        //----------------------------------------------

        //------------Wrap RSA key------------
        PKCS11 pkcs11 = ((PKCS11Ops) sc.getCardType().getCardTemplate().getPKCS11Ops()).getmPKCS11();
        byte[] wrappedBytes = pkcs11.C_WrapKey(sessionID, wrapMechanism, wrapperKeyID, privateKeyID); //Write to DB
        pkcs11.C_DestroyObject(sessionID, publicKeyID);
        pkcs11.C_DestroyObject(sessionID, privateKeyID);
        //------------------------------------

        //------------Unwrap and sign------------
        byte[] testData = RandomUtil.generateRandom(50);
        byte[] signature;
        {
            long unwrappedKeyID = pkcs11.C_UnwrapKey(sessionID, wrapMechanism, wrapperKeyID, wrappedBytes, privateKeyAttrs);
            CK_MECHANISM signMech = new CK_MECHANISM(PKCS11Constants.CKM_SHA256_RSA_PKCS);

            pkcs11.C_SignInit(sessionID, signMech, unwrappedKeyID);
            signature = pkcs11.C_Sign(sessionID, testData);

            pkcs11.C_DestroyObject(sessionID, unwrappedKeyID);
        }
        //---------------------------------------

        //------------Verification------------
        PublicKey publicKey = KeyUtil.generatePublicKey(publicKeySpec);
        boolean verified = SignUtil.verify(SignatureAlg.RSA_SHA256, testData, signature, publicKey);
        Assert.assertTrue(verified);
        //------------------------------------
    }

    /*
     * Unwrap an RSA key and sign with it.
     */
    @Test
    public void test_01_02_PKCS11UnWrapAndSignRSA_WrapPad() throws Exception {
        CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_KEY_WRAP_PAD);

        //------------Creating a temporary RSA key pair------------
        long publicKeyID;
        long privateKeyID;
        KeySpec publicKeySpec;
        CK_ATTRIBUTE[] privateKeyAttrs;
        {
            RSAKeyPairTemplate rsaKeyPairTemplate;
            {
                RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
                rsaKeyPairTemplate = new RSAKeyPairTemplate("rsa_temp", spec);
                rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);
                rsaKeyPairTemplate.getAsExtractableTemplate();
            }

            RSAPublicKeyTemplate publicKeyTemplate = rsaKeyPairTemplate.getPublicKeyTemplate();
            RSAPrivateKeyTemplate privateKeyTemplate = rsaKeyPairTemplate.getPrivateKeyTemplate();

            publicKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false));
            privateKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false));

            privateKeyAttrs = privateKeyTemplate.getAttributesAsArr();

            publicKeySpec = sc.createKeyPair(sessionID, rsaKeyPairTemplate);
            publicKeyID = publicKeyTemplate.getKeyId();
            privateKeyID = privateKeyTemplate.getKeyId();
        }
        //----------------------------------------------------

        //------------Creating a wrapper key------------
        AESKeyTemplate aesKeyTemplate = new AESKeyTemplate("wrapper_key", 32);
        SecretKeyTemplate secretKeyTemplate = aesKeyTemplate.getAsTokenTemplate(false, false, true);
        secretKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false));
        long wrapperKeyID = sc.createSecretKey(sessionID, secretKeyTemplate);
        //----------------------------------------------

        //------------Wrap RSA key------------
        PKCS11 pkcs11 = ((PKCS11Ops) sc.getCardType().getCardTemplate().getPKCS11Ops()).getmPKCS11();
        byte[] wrappedBytes = pkcs11.C_WrapKey(sessionID, wrapMechanism, wrapperKeyID, privateKeyID); //Write to DB
        pkcs11.C_DestroyObject(sessionID, publicKeyID);
        pkcs11.C_DestroyObject(sessionID, privateKeyID);
        //------------------------------------

        //------------Unwrap and sign------------
        long unwrappedKeyID = pkcs11.C_UnwrapKey(sessionID, wrapMechanism, wrapperKeyID, wrappedBytes, privateKeyAttrs);
        CK_MECHANISM signMech = new CK_MECHANISM(PKCS11Constants.CKM_SHA256_RSA_PKCS);
        byte[] testData = RandomUtil.generateRandom(50);

        pkcs11.C_SignInit(sessionID, signMech, unwrappedKeyID);
        byte[] signature = pkcs11.C_Sign(sessionID, testData);

        pkcs11.C_DestroyObject(sessionID, unwrappedKeyID);
        //---------------------------------------

        //------------Verification------------
        PublicKey publicKey = KeyUtil.generatePublicKey(publicKeySpec);
        boolean verified = SignUtil.verify(SignatureAlg.RSA_SHA256, testData, signature, publicKey);
        Assert.assertTrue(verified);
        //------------------------------------
    }

    /*
     * Unwrap an EC key and sign with it.
     * */
    @Test
    public void test_02_01_PKCS11UnWrapAndSignEC() throws Exception {
        final byte[] iv = RandomUtil.generateRandom(16);
        CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, iv);

        //-----------------Creating a temporary EC key pair----------
        long publicKeyID;
        long privateKeyID;
        KeySpec publicKeySpec;
        CK_ATTRIBUTE[] privateKeyAttrs;
        {
            ECKeyPairTemplate ecKeyPairTemplate;
            {
                ECParameterSpec spec = NamedCurve.getECParameterSpec("secp256r1");
                ecKeyPairTemplate = new ECKeyPairTemplate<>("ec_temp", spec);
                ecKeyPairTemplate.getAsTokenTemplate(true, false, false);
                ecKeyPairTemplate.getAsExtractableTemplate();
            }

            AsymmKeyTemplate publicKeyTemplate = ecKeyPairTemplate.getPublicKeyTemplate();
            AsymmKeyTemplate privateKeyTemplate = ecKeyPairTemplate.getPrivateKeyTemplate();

            publicKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false));
            privateKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false));

            privateKeyAttrs = privateKeyTemplate.getAttributesAsArr();

            publicKeySpec = sc.createKeyPair(sessionID, ecKeyPairTemplate);
            publicKeyID = publicKeyTemplate.getKeyId();
            privateKeyID = privateKeyTemplate.getKeyId();
        }
        //------------------------------------------------------

        //---------------Creating a wrapper key-----------------
        long wrapperKeyID;
        {
            AESKeyTemplate aesKeyTemplate = new AESKeyTemplate("wrapper_key", 32);
            SecretKeyTemplate secretKeyTemplate = aesKeyTemplate.getAsTokenTemplate(false, false, true);
            secretKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false));
            wrapperKeyID = sc.createSecretKey(sessionID, secretKeyTemplate);
        }
        //------------------------------------------------------

        //----------------Wrap EC key---------------------------
        PKCS11 pkcs11 = ((PKCS11Ops) sc.getCardType().getCardTemplate().getPKCS11Ops()).getmPKCS11();
        byte[] wrappedBytes = pkcs11.C_WrapKey(sessionID, wrapMechanism, wrapperKeyID, privateKeyID); // write to DB
        pkcs11.C_DestroyObject(sessionID, publicKeyID);
        pkcs11.C_DestroyObject(sessionID, privateKeyID);
        //------------------------------------------------------

        //----------------Unwrap and sign-----------------------
        byte[] testData = RandomUtil.generateRandom(50);
        byte[] signature;
        {
            long unwrappedKeyID = pkcs11.C_UnwrapKey(sessionID, wrapMechanism, wrapperKeyID, wrappedBytes, privateKeyAttrs);
            CK_MECHANISM signMech = new CK_MECHANISM(PKCS11Constants.CKM_ECDSA);

            pkcs11.C_SignInit(sessionID, signMech, unwrappedKeyID);
            signature = pkcs11.C_Sign(sessionID, testData);
            signature = ECSignatureTLVUtil.addTLVToSignature(signature);

            pkcs11.C_DestroyObject(sessionID, unwrappedKeyID);
        }
        //------------------------------------------------------

        //------------Verification-----------
        PublicKey publicKey = KeyUtil.generatePublicKey(publicKeySpec);
        boolean verified = SignUtil.verify(SignatureAlg.ECDSA, testData, signature, publicKey);
        Assert.assertTrue(verified);
        //-----------------------------------
    }

    /*
     * Unwrap an EC key and sign with it.
     * */
    @Test
    public void test_02_02_PKCS11UnWrapAndSignEC_WrapPad() throws Exception {
        CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_KEY_WRAP_PAD);

        //---------------Creating a temporary EC key------------
        long publicKeyID;
        long privateKeyID;
        KeySpec publicKeySpec;
        CK_ATTRIBUTE[] privateKeyAttrs;
        {
            ECKeyPairTemplate ecKeyPairTemplate;
            {
                ECParameterSpec spec = NamedCurve.getECParameterSpec("secp256r1");
                ecKeyPairTemplate = new ECKeyPairTemplate<>("ec_temp", spec);
                ecKeyPairTemplate.getAsTokenTemplate(true, false, false);
                ecKeyPairTemplate.getAsExtractableTemplate();
            }

            AsymmKeyTemplate publicKeyTemplate = ecKeyPairTemplate.getPublicKeyTemplate();
            AsymmKeyTemplate privateKeyTemplate = ecKeyPairTemplate.getPrivateKeyTemplate();

            publicKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false));
            privateKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false));

            privateKeyAttrs = privateKeyTemplate.getAttributesAsArr();

            publicKeySpec = sc.createKeyPair(sessionID, ecKeyPairTemplate);
            publicKeyID = publicKeyTemplate.getKeyId();
            privateKeyID = privateKeyTemplate.getKeyId();
        }
        //------------------------------------------------------

        //---------------Creating a wrapper key-----------------
        long wrapperKeyId;
        {
            AESKeyTemplate aesKeyTemplate = new AESKeyTemplate("wrapper_key", 32);
            SecretKeyTemplate secretKeyTemplate = aesKeyTemplate.getAsTokenTemplate(false, false, true);
            secretKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false));
            wrapperKeyId = sc.createSecretKey(sessionID, secretKeyTemplate);
        }
        //------------------------------------------------------

        //----------------Wrap EC key---------------------------
        PKCS11 pkcs11 = ((PKCS11Ops) sc.getCardType().getCardTemplate().getPKCS11Ops()).getmPKCS11();
        byte[] wrappedBytes = pkcs11.C_WrapKey(sessionID, wrapMechanism, wrapperKeyId, privateKeyID); // write to DB
        pkcs11.C_DestroyObject(sessionID, publicKeyID);
        pkcs11.C_DestroyObject(sessionID, privateKeyID);
        //------------------------------------------------------

        //----------------Unwrap and sign-----------------------
        byte[] testData = RandomUtil.generateRandom(50);
        byte[] signature;
        {
            long unwrappedKeyID = pkcs11.C_UnwrapKey(sessionID, wrapMechanism, wrapperKeyId, wrappedBytes, privateKeyAttrs);
            CK_MECHANISM signMech = new CK_MECHANISM(PKCS11Constants.CKM_ECDSA);

            pkcs11.C_SignInit(sessionID, signMech, unwrappedKeyID);
            signature = pkcs11.C_Sign(sessionID, testData);
            signature = ECSignatureTLVUtil.addTLVToSignature(signature);

            pkcs11.C_DestroyObject(sessionID, unwrappedKeyID);
        }
        //------------------------------------------------------

        //------------Verification-----------
        PublicKey publicKey = KeyUtil.generatePublicKey(publicKeySpec);
        boolean verified = SignUtil.verify(SignatureAlg.ECDSA, testData, signature, publicKey);
        Assert.assertTrue(verified);
        //-----------------------------------
    }
}
