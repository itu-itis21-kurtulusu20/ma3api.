package test.esya.api.cmsenvelope;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeGenerator;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeStreamGenerator;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.EnvelopeConfig;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.KeyAgreementAlg;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static test.esya.api.cmsenvelope.CmsEnvelopeTestUtil.compareFiles;

/**
 * Created by sura.emanet on 31.07.2018.
 */

@RunWith(Parameterized.class)
public class CMSEnvelopeKeyAgreementTest {

    private EnvelopeConfig config = new EnvelopeConfig();

    @Parameterized.Parameters()
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{{KeyAgreementAlg.ECDH_SHA1KDF},
                                            {KeyAgreementAlg.ECDH_SHA224KDF},
                                            {KeyAgreementAlg.ECDH_SHA256KDF},
                                            {KeyAgreementAlg.ECDH_SHA384KDF},
                                            {KeyAgreementAlg.ECDH_SHA512KDF},
                                            {KeyAgreementAlg.ECCDH_SHA1KDF},
                                            {KeyAgreementAlg.ECCDH_SHA224KDF},
                                            {KeyAgreementAlg.ECCDH_SHA256KDF},
                                            {KeyAgreementAlg.ECCDH_SHA512KDF}
        });
    }

    public CMSEnvelopeKeyAgreementTest(KeyAgreementAlg keyAgreementAlg){
        config.setEcKeyAgreementAlg(keyAgreementAlg);
    }

    @Test
    public void testKeyAgreement() throws Exception {
        tryKeyAgreement(TestData.recipientEcCert, TestData.recipientEcKeyPair.getPrivate());
    }

    @Test
    public void testKeyAgreementWithStream() throws Exception {
        tryKeyAgreementWithStream(TestData.recipientEcCert, TestData.recipientEcKeyPair.getPrivate());
    }

    private void tryKeyAgreement(ECertificate cert, PrivateKey privKey) throws Exception {

        TestData.configureCertificateValidation(config);

        CmsEnvelopeGenerator cmsGenerator = new CmsEnvelopeGenerator(TestData.plainString.getBytes());
        cmsGenerator.addRecipients(config, cert);
        byte[] encryptedCMS = cmsGenerator.generate();

        byte[] plainData = CmsEnvelopeTestUtil.decryptInMemory(cert, privKey, encryptedCMS);
        assertEquals(TestData.plainString, new String(plainData));
    }

    private void tryKeyAgreementWithStream( ECertificate cert, PrivateKey privateKey) throws Exception {

        //add key agreement recipient
        String encryptedFileName = TestData.plainFile.getParent() +  "\\Encrypted" + TestData.plainFile.getName();

        FileOutputStream encryptedOutputStream = new FileOutputStream(encryptedFileName);
        FileInputStream plainInputStream = new FileInputStream(TestData.plainFile);

        TestData.configureCertificateValidation(config);

        CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
        cmsGenerator.addRecipients(config, cert);
        cmsGenerator.generate(encryptedOutputStream);

        encryptedOutputStream.close();
        plainInputStream.close();

        String decryptedFileName = TestData.plainFile.getParent() +  "\\Decrypted" + TestData.plainFile.getName();

        //Decrypt to check
        CmsEnvelopeTestUtil.decryptWithStream(privateKey, cert, encryptedFileName, decryptedFileName);
        boolean result = compareFiles(TestData.plainFile.getAbsolutePath(), decryptedFileName);

        Assert.assertTrue(result);
    }

}
