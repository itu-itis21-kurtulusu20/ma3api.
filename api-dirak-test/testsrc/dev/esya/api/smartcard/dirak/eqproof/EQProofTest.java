package dev.esya.api.smartcard.dirak.eqproof;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.template.DirakHSMTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.eqproof.EQProofData;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.eqproof.EQProofSignResult;

import java.math.BigInteger;
import java.security.spec.ECPoint;

public class EQProofTest {

    static SmartCard sc;
    static long session;

    static final String pubKeyLabel = "keyPub_" + System.currentTimeMillis();
    static final String priKeyLabel = "keyPri_" + System.currentTimeMillis();

    static long pubKeyID;
    static long priKeyID;

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

    @Test
    public void eqproofSignTest() throws Exception {

        ECPoint inputA = new ECPoint(
                new BigInteger("b04c155aa44fb1471b912324885da16a56cd922fd97e25610d5fe6651ceee8e5", 16),
                new BigInteger("f815404091c4f234b721faa6dfc4e95ed344ede3419aa25f0ba1980d210b31db", 16)
        );
        ECPoint inputB = new ECPoint(
                new BigInteger("a0a78998312afba2d2ee1a526ba4ef9129748b7570b062c2d84d82ca9182ac33", 16),
                new BigInteger("076b20f46bebfc10c7d95333167db2e74d4760710b1a1ea0312784c878866698", 16)
        );

        EQProofSignResult proof = sign(inputA, inputB);
        verify(proof, inputA, inputB);
    }

    private static void createKey() throws PKCS11Exception {
        final byte[] ecParamsByte = new byte[]{(byte) 0x06, (byte) 0x05, (byte) 0x2B, (byte) 0x81, (byte) 0x04, (byte) 0x00, (byte) 0x0A};
        final byte[] publicKey = StringUtil.hexToByte("044104FB99ED53566508A97B8BF018011DD466C975996107D1D01E4E1D052E243FC7BEF5ABF89C238C99A3B57115D083123C54325FF6E7EB70EF9A41DB89683C3E98EB");
        final byte[] privateKey = StringUtil.hexToByte("BA292FD8E927E879E05DF06663523BDBEDE6F636511BE197E60D67019CC1A35B");

        CK_ATTRIBUTE[] ECTemplatePub = new CK_ATTRIBUTE[]{
                new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_EC),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_EC_POINT, publicKey),
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
                new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE, privateKey),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_PRIVATE_KEY),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, priKeyLabel),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, true),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN, true)
        };

        pubKeyID = sc.getPKCS11().C_CreateObject(session, ECTemplatePub);
        priKeyID = sc.getPKCS11().C_CreateObject(session, ECTemplatePriv);
    }

    private EQProofSignResult sign(ECPoint inputA, ECPoint inputB) throws Exception {
        EQProofData params = new EQProofData(inputA, inputB);
        CK_MECHANISM mech = new CK_MECHANISM(DirakHSMTemplate.CKM_EQPROOF);

        byte[] resultBytes = sc.signDataWithKeyID(session, priKeyID, mech, params.getEncoded());

        return new EQProofSignResult(resultBytes);
    }

    private void verify(EQProofSignResult proof, ECPoint inputA, ECPoint inputB) throws Exception {
        byte[] signature = proof.getEncoded();

        EQProofData verifyData = new EQProofData(inputA, inputB);
        byte[] data = verifyData.getEncoded();

        CK_MECHANISM mech = new CK_MECHANISM(DirakHSMTemplate.CKM_EQPROOF);

        sc.verifyData(session, pubKeyID, data, signature, mech);
    }
}
