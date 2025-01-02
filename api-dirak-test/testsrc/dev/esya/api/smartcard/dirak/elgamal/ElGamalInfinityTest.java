package dev.esya.api.smartcard.dirak.elgamal;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template.DirakHSMTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.elgamal.ElGamalEncryptedData;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.elgamal.ElGamalPlainData;

import java.io.IOException;
import java.math.BigInteger;
import java.security.spec.ECPoint;

public class ElGamalInfinityTest {

    static final CK_MECHANISM EL_GAMAL_MECH = new CK_MECHANISM(DirakHSMTemplate.CKM_ELGAMAL_EC);

    static final String publicKey = "0441049FF7D6465666F15EA54E4E4161F4668F17CF1E54397F19606084411ED1326356CFF055562BAF9DF9079882ABB001470DBB28C6F64ABFCFEE6411C584710B3AF3";

    static final String privateKey = "DD5BD367E0B00E274F1733BB2273DD4E75E3FAB43A09C17C64645B2B47B30BB1";

    static final ECPoint alpha = new ECPoint(
            new BigInteger("c640cda9d306bd19ea1ed8d02337755f0edafea1bdb625f6ebc470241317fe29", 16),
            new BigInteger("dfefd39c4a2e199f3ba7f76f92708350b93f5da725914c25a2784a24d855fefb", 16)
    );

    static final ECPoint beta = new ECPoint(
            new BigInteger("6339b924fe9bc61640ccf8ab961846027ef29494da503a9e9b2aef8a52211e15", 16),
            new BigInteger("55124aac6ce797601a176c1b39ebf97eae778a563c12e1b9be57d3c0a5aadc56", 16)
    );

    static final ECPoint infinity = ECPoint.POINT_INFINITY;

    static SmartCard sc;
    static long session;

    static final String pubKeyLabel = "keyPub_" + System.currentTimeMillis();
    static final String priKeyLabel = "keyPri_" + System.currentTimeMillis();

    static long pubKeyID;
    static long privKeyID;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        sc = new SmartCard(CardType.DIRAKHSM);
        long slot = sc.getSlotList()[0];
        session = sc.openSession(slot);
        sc.login(session, "12345678");

        createKey();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        sc.logout(session);
        sc.closeSession(session);
    }

    private static void createKey() throws PKCS11Exception {
        final byte[] ecParamsByte = new byte[]{(byte) 0x06, (byte) 0x05, (byte) 0x2B, (byte) 0x81, (byte) 0x04, (byte) 0x00, (byte) 0x0A};

        CK_ATTRIBUTE[] ECTemplatePub = new CK_ATTRIBUTE[]{
                new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_EC),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_EC_POINT, StringUtil.hexToByte(publicKey)),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_PUBLIC_KEY),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, pubKeyLabel),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_EC_PARAMS, ecParamsByte),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, false),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY, true)
        };

        CK_ATTRIBUTE[] ECTemplatePriv = new CK_ATTRIBUTE[]{
                new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_EC),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_EC_PARAMS, ecParamsByte),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE, StringUtil.hexToByte(privateKey)),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_PRIVATE_KEY),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, priKeyLabel),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN, true)
        };

        pubKeyID = sc.getPKCS11().C_CreateObject(session, ECTemplatePub);
        privKeyID = sc.getPKCS11().C_CreateObject(session, ECTemplatePriv);
    }

    @Test
    public void elGamalEncryptInfinity() throws Exception {
        ElGamalPlainData plainData = new ElGamalPlainData(infinity);
        ElGamalEncryptedData cipherText = encrypt(plainData, pubKeyID);

        Assert.assertNotNull(cipherText);
    }

    @Test
    public void elGamalDecryptInfinity() throws Exception {
        ElGamalEncryptedData cipherText = new ElGamalEncryptedData(alpha, beta);
        ElGamalPlainData returnedPlainText = decrypt(cipherText, privKeyID);

        Assert.assertNotNull(returnedPlainText);
    }

    @Test
    public void elGamalEncryptDecryptInfinity() throws Exception {
        ElGamalPlainData plainData = new ElGamalPlainData(infinity);
        ElGamalEncryptedData cipherText = encrypt(plainData, pubKeyID);
        ElGamalPlainData returnedPlainText = decrypt(cipherText, privKeyID);

        Assert.assertEquals(infinity, returnedPlainText.getPoints()[0]);
    }

    private ElGamalEncryptedData encrypt(ElGamalPlainData input, long pubKeyID) throws IOException, PKCS11Exception, ESYAException {
        byte[] plaintext = input.getEncoded();
        byte[] ciphertext = sc.encryptData(session, pubKeyID, plaintext, EL_GAMAL_MECH);
        return new ElGamalEncryptedData(ciphertext);
    }

    private ElGamalPlainData decrypt(ElGamalEncryptedData input, long privKeyID) throws IOException, PKCS11Exception, ESYAException {
        byte[] ciphertext = input.getEncoded();
        byte[] plaintext = sc.decryptData(session, privKeyID, ciphertext, EL_GAMAL_MECH);
        return new ElGamalPlainData(plaintext);
    }
}

