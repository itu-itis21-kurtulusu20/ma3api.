package dev.esya.api.smartcard.dirak;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme.RSAPSS_SS;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPublicKeySpec;
import java.util.List;
import java.util.Random;

public class DirakCertificateTest {

    static final String PASSWORD = "12345678";
    static final String IMPORTED_KEY_LABEL_2 = "RSAKeyImported2";
    static final String IMPORTED_CERT_LABEL = "CertImported";
    static final String IMPORTED_CERT_LABEL_2 = "CertImported2";

    SmartCard sc = null;
    long sid = 0;
    long slotNo = 0;

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

    @Test
    public void test_01_ImportCertificate()
        throws Exception {
        //Import
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(Base64.decode(DirakTestConstants.testCer)));

        boolean r1 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL);
        sc.importCertificate(sid, IMPORTED_CERT_LABEL, cert);
        boolean r2 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL);
        Assert.assertTrue(!r1 && r2);

        //Read
        List<byte[]> certs = sc.readCertificate(sid, IMPORTED_CERT_LABEL);

        ECertificate cer = new ECertificate(certs.get(0));
        ECertificate cer2 = new ECertificate(DirakTestConstants.testCer);
        Assert.assertEquals(cer, cer2);

        //Delete
        r1 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL);
        sc.deletePublicObject(sid, IMPORTED_CERT_LABEL);
        r2 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL);
        Assert.assertTrue(r1 && !r2);
    }

    @Test
    public void test_02_ImportCertificateWithKeysAndSign()
        throws Exception {
        //Import
        PrivateKey prikey = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, Base64.decode(DirakTestConstants.testPrivateKey));
        ECertificate cert = new ECertificate(DirakTestConstants.testCer);

        boolean r1 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL_2);
        boolean r2 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL_2);
        boolean r3 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL_2);
        sc.importCertificateAndKey(sid, IMPORTED_CERT_LABEL_2, IMPORTED_KEY_LABEL_2, prikey, cert.asX509Certificate());
        boolean r4 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL_2);
        boolean r5 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL_2);
        boolean r6 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL_2);

        Assert.assertTrue(!r1 && !r2 && !r3 && r4 && r5 && r6);

        //Sign
        byte[] tobesigned = new byte[123];
        new Random().nextBytes(tobesigned);

        CK_MECHANISM mech = RSAPSS_SS.getDefaultMechanismForPSS(DigestAlg.SHA256);

        RSAPSSParams rsapssParams = new RSAPSSParams(DigestAlg.SHA256);

        RSAPublicKeySpec spec = (RSAPublicKeySpec) sc.readPublicKeySpec(sid, IMPORTED_KEY_LABEL_2);
        PublicKey publicKey = KeyUtil.generatePublicKey(spec);

        byte[] sig = sc.signDataWithCertSerialNo(sid, cert.getSerialNumber().toByteArray(), mech, tobesigned);

        boolean result = SignUtil.verify(SignatureAlg.RSA_PSS, rsapssParams, tobesigned, sig, publicKey);
        Assert.assertTrue(result);

        //Delete
        sc.deletePrivateObject(sid, IMPORTED_KEY_LABEL_2);
        sc.deletePublicObject(sid, IMPORTED_KEY_LABEL_2);
        sc.deletePublicObject(sid, IMPORTED_CERT_LABEL_2);
    }
}
