package dev.esya.api.cmssignature.smartcard;

import gnu.crypto.key.rsa.GnuRSAPublicKey;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.OAEPPadding;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.PfxParser;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPrivateKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;

import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.FileInputStream;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.List;
import java.util.Random;

public class SmartCardBasicTest {

    SmartCard sc = null;
    long sid = 0;
    long slotNo = 0;

    final String PASSWORD = "12345";

    String label = "QC A1 1";

    @Before
    public void setUpClass() throws Exception
    {
        sc = new SmartCard(CardType.AKIS);
        slotNo = getSlot();
        sid = sc.openSession(slotNo);
        sc.login(sid, PASSWORD);
    }

    @After
    public void cleanUp()  throws Exception
    {
        sc.logout(sid);
        sc.closeSession(sid);
    }

    private long getSlot()
            throws PKCS11Exception
    {
        long[] slots = sc.getTokenPresentSlotList();
        return slots[0];
    }

    @Test
    public void test_01_LoadPfx() throws Exception{
        String PFX_PATH ="T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_1.p12";
        String PFX_PIN = "123456";

        PfxParser parser = new PfxParser(new FileInputStream(PFX_PATH),PFX_PIN);
        List<Pair<ECertificate, PrivateKey>> entries = parser.getCertificatesAndKeys();

        Pair<ECertificate, PrivateKey> entry = entries.get(0);
        ECertificate cert = entry.first();

        if(sc.isCertificateExist(sid, label) == true)
            throw new Exception("Certificate is already loaded!");

        String privKeyLabel = label;
        String pubKeyLabel  = label;
        String certLabel = label;

        RSAPrivateKeyTemplate privateKeyTemplate = new RSAPrivateKeyTemplate(privKeyLabel, (RSAPrivateCrtKey) entry.second(), null);
        privateKeyTemplate.getAsTokenTemplate(true, true, false);

        RSAPublicKeyTemplate publicKeyTemplate = new RSAPublicKeyTemplate(pubKeyLabel, (java.security.interfaces.RSAPublicKey) cert.asX509Certificate().getPublicKey());
        publicKeyTemplate.getAsTokenTemplate(true, true, false);

        RSAKeyPairTemplate keyPairTemplate = new RSAKeyPairTemplate(publicKeyTemplate, privateKeyTemplate);

        sc.importKeyPair(sid, keyPairTemplate);
        sc.importCertificate(sid,certLabel,cert.asX509Certificate());
    }

    @Test
    public void test_02_Sign_PKCS()throws Exception{
        int dataToBeSignedLen = 10 + new Random().nextInt(30);
        byte []tobesigned = RandomUtil.generateRandom(dataToBeSignedLen);

        byte [] signature = SmartOp.sign(sc, sid, slotNo, label, tobesigned, Algorithms.SIGNATURE_RSA_SHA256);

        RSAPublicKeySpec spec = (RSAPublicKeySpec) sc.readPublicKeySpec(sid, label);
        GnuRSAPublicKey pk = new GnuRSAPublicKey(spec.getModulus(), spec.getPublicExponent());
        SignUtil.verify(SignatureAlg.RSA_SHA256, null, tobesigned, signature, pk);
    }

    @Test
    public void test_02_Sign_PSS()throws Exception{
        int dataToBeSignedLen = 10 + new Random().nextInt(30);
        byte []tobesigned = RandomUtil.generateRandom(dataToBeSignedLen);

        RSAPSSParams rsapssParams = new RSAPSSParams(DigestAlg.SHA256);
        byte [] signature = SmartOp.sign(sc, sid, slotNo, label, tobesigned, Algorithms.SIGNATURE_RSA_PSS, rsapssParams);

        RSAPublicKeySpec spec = (RSAPublicKeySpec) sc.readPublicKeySpec(sid, label);
        GnuRSAPublicKey pk = new GnuRSAPublicKey(spec.getModulus(), spec.getPublicExponent());
        SignUtil.verify(SignatureAlg.RSA_PSS, rsapssParams, tobesigned, signature, pk);
    }

    @Test
    public void test_03_Decryption_PKCS()throws Exception{
        CipherAlg cipherAlg = CipherAlg.RSA_PKCS1;

        int dataToBeSignedLen = 10 + new Random().nextInt(30);
        byte []tobeencrypted = RandomUtil.generateRandom(dataToBeSignedLen);

        RSAPublicKeySpec spec = (RSAPublicKeySpec) sc.readPublicKeySpec(sid, label);
        GnuRSAPublicKey pk = new GnuRSAPublicKey(spec.getModulus(), spec.getPublicExponent());
        byte [] encrypted = CipherUtil.encrypt(cipherAlg, null, tobeencrypted, pk);

        byte [] decrypted = SmartOp.decrypt(sc, sid, slotNo, label, encrypted, Algorithms.CIPHER_RSA_PKCS1, null);

        Assert.assertArrayEquals(tobeencrypted, decrypted);
    }

    @Test
    public void test_03_Decryption_OAEP()throws Exception{
        CipherAlg cipherAlg = CipherAlg.RSA_OAEP_SHA256;

        int dataToBeSignedLen = 10 + new Random().nextInt(30);
        byte []tobeencrypted = RandomUtil.generateRandom(dataToBeSignedLen);

        RSAPublicKeySpec spec = (RSAPublicKeySpec) sc.readPublicKeySpec(sid, label);
        GnuRSAPublicKey pk = new GnuRSAPublicKey(spec.getModulus(), spec.getPublicExponent());
        byte [] encrypted = CipherUtil.encrypt(cipherAlg, null, tobeencrypted, pk);

        OAEPPadding paddingAlg = OAEPPadding.OAEP_SHA256_MGF1;
        OAEPParameterSpec oaepParameterSpec = new OAEPParameterSpec(paddingAlg.getDigestAlg().getName(),
                paddingAlg.getMaskGenerationFunction().getName(),MGF1ParameterSpec.SHA1,PSource.PSpecified.DEFAULT);

        byte [] decrypted = SmartOp.decrypt(sc, sid, slotNo, label, encrypted, Algorithms.CIPHER_RSAOAEP, oaepParameterSpec);

        Assert.assertArrayEquals(tobeencrypted, decrypted);
    }


}
