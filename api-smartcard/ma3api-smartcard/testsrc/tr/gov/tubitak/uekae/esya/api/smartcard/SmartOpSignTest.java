package tr.gov.tubitak.uekae.esya.api.smartcard;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;

import java.security.PublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Random;


public class SmartOpSignTest {

    CardType CARD_TYPE = CardType.DIRAKHSM;

    int SLOT_INDEX = 0;
    String PIN = "12345678";
    String KEY_LABEL = "SmartOpTest";

    SmartCard sc;
    long slotNo;
    long session;


    @Before
    public void init() throws Exception
    {
        sc = new SmartCard(CARD_TYPE);
        slotNo = sc.getSlotList()[SLOT_INDEX];
        session = sc.openSession(slotNo);
        sc.login(session, PIN);
    }

    @After
    public void after() throws PKCS11Exception, SmartCardException
    {
        sc.deletePublicObject(session, KEY_LABEL);
        sc.deletePrivateObject(session, KEY_LABEL);
        sc.logout(session);
        sc.closeSession(session);
    }

    @Test
    public void test_toRemoveRSAPKCS() throws Exception {
        PublicKey publicKey = createRSAKey();

        sc.setMechanismsToBeRemoved(new long[] {PKCS11Constants.CKM_RSA_PKCS});

        int dataLen = 10 + new Random().nextInt(512);
        byte [] dtbs = RandomUtil.generateRandom(dataLen);

        byte[] signature = SmartOp.sign(sc, session, slotNo, KEY_LABEL, dtbs, Algorithms.SIGNATURE_RSA_SHA256);

        boolean verify = SignUtil.verify(SignatureAlg.RSA_SHA256, null, dtbs, signature, publicKey);

        Assert.assertTrue(verify);
    }

    @Test
    public void testRSAPKCS() throws Exception {
        PublicKey publicKey = createRSAKey();

        int dataLen = 10 + new Random().nextInt(512);
        byte [] dtbs = RandomUtil.generateRandom(dataLen);

        byte[] signature = SmartOp.sign(sc, session, slotNo, KEY_LABEL, dtbs, Algorithms.SIGNATURE_RSA_SHA256);

        boolean verify = SignUtil.verify(SignatureAlg.RSA_SHA256, null, dtbs, signature, publicKey);

        Assert.assertTrue(verify);
    }


    @Test
    public void test_toRemoveRSAPSS() throws Exception {
        PublicKey publicKey = createRSAKey();

        sc.setMechanismsToBeRemoved(new long[] {PKCS11Constants.CKM_RSA_PKCS_PSS});

        int dataLen = 10 + new Random().nextInt(512);
        byte [] dtbs = RandomUtil.generateRandom(dataLen);

        byte[] signature = SmartOp.sign(sc, session, slotNo, KEY_LABEL, dtbs, Algorithms.SIGNATURE_RSA_PSS, new RSAPSSParams(DigestAlg.SHA256));

        boolean verify = SignUtil.verify(SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), dtbs, signature, publicKey);

        Assert.assertTrue(verify);
    }

    @Test
    public void test_RSAPSS() throws Exception {
        PublicKey publicKey = createRSAKey();

        int dataLen = 10 + new Random().nextInt(512);
        byte [] dtbs = RandomUtil.generateRandom(dataLen);

        byte[] signature = SmartOp.sign(sc, session, slotNo, KEY_LABEL, dtbs, Algorithms.SIGNATURE_RSA_PSS, new RSAPSSParams(DigestAlg.SHA256));

        boolean verify = SignUtil.verify(SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256), dtbs, signature, publicKey);

        Assert.assertTrue(verify);
    }

    @Test
    public void test_toRemoveECDSA() throws Exception {
        PublicKey publicKey = createECDSAKey();

        int dataLen = 10 + new Random().nextInt(512);
        byte [] dtbs = RandomUtil.generateRandom(dataLen);

        sc.setMechanismsToBeRemoved(new long[] {PKCS11Constants.CKM_ECDSA});

        byte[] signature = SmartOp.sign(sc, session, slotNo, KEY_LABEL, dtbs, Algorithms.SIGNATURE_ECDSA_SHA256, null);

        boolean verify = SignUtil.verify(SignatureAlg.ECDSA_SHA256, null, dtbs, signature, publicKey);

        Assert.assertTrue(verify);
    }


    @Test
    public void test_ECDSA() throws Exception {
        PublicKey publicKey = createECDSAKey();

        int dataLen = 10 + new Random().nextInt(512);
        byte [] dtbs = RandomUtil.generateRandom(dataLen);

        byte[] signature = SmartOp.sign(sc, session, slotNo, KEY_LABEL, dtbs, Algorithms.SIGNATURE_ECDSA_SHA256, null);

        boolean verify = SignUtil.verify(SignatureAlg.ECDSA_SHA256, null, dtbs, signature, publicKey);

        Assert.assertTrue(verify);
    }

    private PublicKey createECDSAKey() throws Exception {
        ECParameterSpec secp384r1 = NamedCurve.getECParameterSpec("secp384r1");

        ECKeyPairTemplate keyPairTemplate = new ECKeyPairTemplate(KEY_LABEL, secp384r1);
        keyPairTemplate.getAsTokenTemplate(true, false, false);
        KeySpec publicKeySpec = sc.createKeyPair(session, keyPairTemplate);

        PublicKey publicKey = KeyUtil.generatePublicKey(publicKeySpec);

        return publicKey;
    }

    public PublicKey createRSAKey() throws Exception {
        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
        RSAKeyPairTemplate keyPairTemplate = new RSAKeyPairTemplate(KEY_LABEL, spec);
        keyPairTemplate.getAsTokenTemplate(true, false, false);
        KeySpec publicKeySpec = sc.createKeyPair(session, keyPairTemplate);

        PublicKey publicKey = KeyUtil.generatePublicKey(publicKeySpec);

        return publicKey;
    }

}
