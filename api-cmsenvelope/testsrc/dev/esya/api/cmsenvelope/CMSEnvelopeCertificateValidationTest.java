package dev.esya.api.cmsenvelope;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import test.esya.api.cmsenvelope.TestData;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.exception.CertValidationException;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeGenerator;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeParser;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.EnvelopeConfig;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.IDecryptorStore;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.MemoryDecryptor;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.PfxParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.PrivateKey;

import static org.junit.Assert.assertEquals;

/**
 * Created by orcun.ertugrul on 27-Jul-17.
 */
@Ignore("Development tests")
public class CMSEnvelopeCertificateValidationTest
{
    static String valid_pfx_path = "T:\\api-cmsenvelope\\testdata\\cmsenvelope\\orcun.ertugrul@ug.net_231291.pfx";
    static String valid_pfx_pass = "231291";

    static String sign_pfx_path = "T:\\api-cmsenvelope\\testdata\\cmsenvelope\\orcun.ertugrul@ug.net_231291.pfx";
    static String sign_pfx_pass = "231291";

    static String revoked_bydate_pfx_path = "T:\\api-cmsenvelope\\testdata\\cmsenvelope\\orcun.ertugrul@ug.net_231291.pfx";
    static String revoked_bydate_pfx_pass = "231291";

    static PfxParser validPfx;
    static PfxParser signPfx;
    static PfxParser revokedPfx;

    @BeforeClass
    public static void  initTest() throws IOException, CryptoException
    {
        validPfx = new PfxParser(new FileInputStream(valid_pfx_path), valid_pfx_pass);
        signPfx = new PfxParser(new FileInputStream(sign_pfx_path), sign_pfx_pass);
        revokedPfx = new PfxParser(new FileInputStream(revoked_bydate_pfx_path), revoked_bydate_pfx_pass);
    }

    @Test
    public void testEncryptionWithCertificateValidationValid() throws ESYAException
    {
        ECertificate cert = validPfx.getCertificatesAndKeys().get(0).getObject1();
        PrivateKey privKey = validPfx.getCertificatesAndKeys().get(0).getObject2();

        testEncryptionWithCertificateValidation(cert, privKey);

    }

    @Test(expected = CertValidationException.class)
    public void testEncryptionWithCertificateValidationKeyUsageFail() throws ESYAException {

        ECertificate cert = signPfx.getCertificatesAndKeys().get(0).getObject1();
        PrivateKey privKey = signPfx.getCertificatesAndKeys().get(0).getObject2();

        testEncryptionWithCertificateValidation(cert, privKey);
    }


    @Test(expected = CertValidationException.class)
    public void testEncryptionWithCertificateValidationRevokeFail() throws ESYAException {

        ECertificate cert = revokedPfx.getCertificatesAndKeys().get(0).getObject1();
        PrivateKey privKey = revokedPfx.getCertificatesAndKeys().get(0).getObject2();

        testEncryptionWithCertificateValidation(cert, privKey);
    }

    public void testEncryptionWithCertificateValidation(ECertificate cert, PrivateKey privKey) throws ESYAException
    {
        EnvelopeConfig config = new EnvelopeConfig();
        config.setPolicy(TestData.EncryptionPolicyFile);

        CmsEnvelopeGenerator envelopeGenerator = new CmsEnvelopeGenerator(TestData.plainString.getBytes());
        envelopeGenerator.addRecipients(config,cert);
        byte [] envelopedData = envelopeGenerator.generate();

        Pair<ECertificate,PrivateKey> recipient = new Pair<ECertificate,PrivateKey>(cert, privKey);
        IDecryptorStore decryptor = new MemoryDecryptor(recipient);
        CmsEnvelopeParser cmsParser = new CmsEnvelopeParser(envelopedData);
        byte [] plainData = cmsParser.open(decryptor);

        assertEquals(TestData.plainString, new String(plainData));
    }

}
