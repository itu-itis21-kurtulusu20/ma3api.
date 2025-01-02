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

public class ElGamalKnownAnswerDecryptionTest {

    final static byte[] privateKey = StringUtil.hexToByte("ff2ca46460011556102b7af20e6fa020da54fdbc195a7037f711e6315c340fdd");

    final static ECPoint a = new ECPoint(
        new BigInteger("d5d6908f50e33529ee9c61c801467f1cb739181168927cd712a58e1debb66297", 16),
        new BigInteger("759102308c11306014125628b3f2771a697e7ec429b2191c3f06f5e116efcbdc", 16)
    );

    final static ECPoint[] aArray = {
        new ECPoint(
            new BigInteger("d5d6908f50e33529ee9c61c801467f1cb739181168927cd712a58e1debb66297", 16),
            new BigInteger("759102308c11306014125628b3f2771a697e7ec429b2191c3f06f5e116efcbdc", 16)
        ),
        new ECPoint(
            new BigInteger("d5d6908f50e33529ee9c61c801467f1cb739181168927cd712a58e1debb66297", 16),
            new BigInteger("759102308c11306014125628b3f2771a697e7ec429b2191c3f06f5e116efcbdc", 16)
        ),
        new ECPoint(
            new BigInteger("d5d6908f50e33529ee9c61c801467f1cb739181168927cd712a58e1debb66297", 16),
            new BigInteger("759102308c11306014125628b3f2771a697e7ec429b2191c3f06f5e116efcbdc", 16)
        )
    };

    final static ECPoint b = new ECPoint(
        new BigInteger("d0771805207051e618c96061d675b8015b678ed81051a46776684e63fce4192d", 16),
        new BigInteger("5e7dabd5dafc069feef0641f4f83408939e9f02e65982dee4669fe487db12147", 16)
    );

    final static ECPoint[] bArray = {
        new ECPoint(
            new BigInteger("d0771805207051e618c96061d675b8015b678ed81051a46776684e63fce4192d", 16),
            new BigInteger("5e7dabd5dafc069feef0641f4f83408939e9f02e65982dee4669fe487db12147", 16)
        ),
        new ECPoint(
            new BigInteger("d0771805207051e618c96061d675b8015b678ed81051a46776684e63fce4192d", 16),
            new BigInteger("5e7dabd5dafc069feef0641f4f83408939e9f02e65982dee4669fe487db12147", 16)
        ),
        new ECPoint(
            new BigInteger("d0771805207051e618c96061d675b8015b678ed81051a46776684e63fce4192d", 16),
            new BigInteger("5e7dabd5dafc069feef0641f4f83408939e9f02e65982dee4669fe487db12147", 16)
        )
    };

    final static ECPoint plaintext = new ECPoint(
        new BigInteger("ed3bace23c5e17652e174c835fb72bf53ee306b3406a26890221b4cef7500f88", 16),
        new BigInteger("e57a6f571288ccffdcda5e8a7a1f87bf97bd17be084895d0fce17ad5e335286e", 16)
    );

    final static ECPoint[] plaintextArray = {
        new ECPoint(
            new BigInteger("ed3bace23c5e17652e174c835fb72bf53ee306b3406a26890221b4cef7500f88", 16),
            new BigInteger("e57a6f571288ccffdcda5e8a7a1f87bf97bd17be084895d0fce17ad5e335286e", 16)
        ),
        new ECPoint(
            new BigInteger("ed3bace23c5e17652e174c835fb72bf53ee306b3406a26890221b4cef7500f88", 16),
            new BigInteger("e57a6f571288ccffdcda5e8a7a1f87bf97bd17be084895d0fce17ad5e335286e", 16)
        ),
        new ECPoint(
            new BigInteger("ed3bace23c5e17652e174c835fb72bf53ee306b3406a26890221b4cef7500f88", 16),
            new BigInteger("e57a6f571288ccffdcda5e8a7a1f87bf97bd17be084895d0fce17ad5e335286e", 16)
        )
    };

    static SmartCard sc;
    static long session;

    static final String priKeyLabel = "keyPri_" + System.currentTimeMillis();
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

    private static void createKey() throws PKCS11Exception {
        final byte[] ecParamsByte = new byte[]{(byte) 0x06, (byte) 0x05, (byte) 0x2B, (byte) 0x81, (byte) 0x04, (byte) 0x00, (byte) 0x0A};

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

        priKeyID = sc.getPKCS11().C_CreateObject(session, ECTemplatePriv);
    }

    @Test
    public void elGamalTest() throws Exception {
        ElGamalEncryptedData ciphertext = new ElGamalEncryptedData(a, b);
        ElGamalPlainData plaintext = decrypt(ciphertext);

        Assert.assertEquals(ElGamalKnownAnswerDecryptionTest.plaintext, plaintext.getPoint(0));
    }

    @Test
    public void elGamalMultipleTest() throws Exception {
        ElGamalEncryptedData ciphertext = new ElGamalEncryptedData(aArray, bArray);
        ElGamalPlainData plaintext = decrypt(ciphertext);

        ECPoint[] plaintextArray = plaintext.getPoints();

        for (int i = 0; i < plaintextArray.length; i++) {
            Assert.assertEquals(ElGamalKnownAnswerDecryptionTest.plaintextArray[i], plaintextArray[i]);
        }
    }

    private ElGamalPlainData decrypt(ElGamalEncryptedData input) throws IOException, PKCS11Exception, ESYAException {
        CK_MECHANISM mech = new CK_MECHANISM(DirakHSMTemplate.CKM_ELGAMAL_EC);

        byte[] ciphertext = input.getEncoded();
        byte[] plaintext = sc.decryptData(session, priKeyID, ciphertext, mech);
        ElGamalPlainData elGamalPlainData = new ElGamalPlainData(plaintext);
        return elGamalPlainData;
    }
}
