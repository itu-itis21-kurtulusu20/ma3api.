package test.esya.api.cmsenvelope;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.*;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.IDecryptorStore;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.MemoryDecryptor;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.KeyAgreementAlg;

import java.io.*;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static test.esya.api.cmsenvelope.CmsEnvelopeTestUtil.compareFiles;

/**
 * Created by sura.emanet on 1.08.2018.
 */
@RunWith(Parameterized.class)
public class CMSEnvelopeAddRecipientToParserTest {

    private EnvelopeConfig config = new EnvelopeConfig();
    private Pair<ECertificate, PrivateKey> ECEncEntries;
    private Pair<ECertificate, PrivateKey> RSAEncEntries;

    @Parameterized.Parameters()
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{{KeyAgreementAlg.ECDH_SHA1KDF, CipherAlg.RSA_OAEP},
                                            {KeyAgreementAlg.ECDH_SHA1KDF, CipherAlg.RSA_OAEP_SHA256},
                                            {KeyAgreementAlg.ECDH_SHA1KDF, CipherAlg.RSA_PKCS1},
                                            {KeyAgreementAlg.ECDH_SHA224KDF, CipherAlg.RSA_OAEP},
                                            {KeyAgreementAlg.ECDH_SHA224KDF, CipherAlg.RSA_OAEP_SHA256},
                                            {KeyAgreementAlg.ECDH_SHA224KDF, CipherAlg.RSA_PKCS1},
                                            {KeyAgreementAlg.ECDH_SHA256KDF, CipherAlg.RSA_OAEP},
                                            {KeyAgreementAlg.ECDH_SHA256KDF, CipherAlg.RSA_OAEP_SHA256},
                                            {KeyAgreementAlg.ECDH_SHA256KDF, CipherAlg.RSA_PKCS1},
                                            {KeyAgreementAlg.ECDH_SHA384KDF, CipherAlg.RSA_OAEP},
                                            {KeyAgreementAlg.ECDH_SHA384KDF, CipherAlg.RSA_OAEP_SHA256},
                                            {KeyAgreementAlg.ECDH_SHA384KDF, CipherAlg.RSA_PKCS1},
                                            {KeyAgreementAlg.ECDH_SHA512KDF, CipherAlg.RSA_OAEP},
                                            {KeyAgreementAlg.ECDH_SHA512KDF, CipherAlg.RSA_OAEP_SHA256},
                                            {KeyAgreementAlg.ECDH_SHA512KDF, CipherAlg.RSA_PKCS1},
                                            {KeyAgreementAlg.ECCDH_SHA1KDF, CipherAlg.RSA_OAEP},
                                            {KeyAgreementAlg.ECCDH_SHA1KDF, CipherAlg.RSA_OAEP_SHA256},
                                            {KeyAgreementAlg.ECCDH_SHA1KDF, CipherAlg.RSA_PKCS1},
                                            {KeyAgreementAlg.ECCDH_SHA224KDF, CipherAlg.RSA_OAEP},
                                            {KeyAgreementAlg.ECCDH_SHA224KDF, CipherAlg.RSA_OAEP_SHA256},
                                            {KeyAgreementAlg.ECCDH_SHA224KDF, CipherAlg.RSA_PKCS1},
                                            {KeyAgreementAlg.ECCDH_SHA256KDF, CipherAlg.RSA_OAEP},
                                            {KeyAgreementAlg.ECCDH_SHA256KDF, CipherAlg.RSA_OAEP_SHA256},
                                            {KeyAgreementAlg.ECCDH_SHA256KDF, CipherAlg.RSA_PKCS1},
                                            {KeyAgreementAlg.ECCDH_SHA512KDF, CipherAlg.RSA_OAEP},
                                            {KeyAgreementAlg.ECCDH_SHA512KDF, CipherAlg.RSA_OAEP_SHA256},
                                            {KeyAgreementAlg.ECCDH_SHA512KDF, CipherAlg.RSA_PKCS1}
        });
    }

    public CMSEnvelopeAddRecipientToParserTest(KeyAgreementAlg keyAgreementAlg, CipherAlg keyTransAlg){
        config.setEcKeyAgreementAlg(keyAgreementAlg);
        config.setRsaKeyTransAlg(keyTransAlg);
    }

    @Test
    public void testKeyAgreementAndKeyTrans() throws Exception {
        tryKeyAgreementAndKeyTrans();
    }

    @Test
    public void testKeyAgreementAndKeyTransWithStream() throws Exception {
        tryKeyAgreementAndKeyTransWitStream();
    }

    @Test
    public void testAddingRecipientToParser()throws Exception {
        tryAddingRecipientToParser();
    }

    @Test
    public void testAddingRecipientToParserWithStream()throws Exception {
        tryAddingRecipientToParserWithStream();
    }

    @Test
    public void testKeyTransOAEPDefaultWithByteStream() throws Exception {
        tryKeyTransOAEPDefaultWithByteStream();
    }

    private void tryAddingRecipientToParser() throws Exception {

        TestData.configureCertificateValidation(config);

        ECEncEntries = CertificateTestConstants.getECEncCertificateAndKey();
        RSAEncEntries  = CertificateTestConstants.getRSAEncCertificateAndKey();

        CmsEnvelopeGenerator cmsGenerator = new CmsEnvelopeGenerator(TestData.plainString.getBytes());
        cmsGenerator.addRecipients(config, ECEncEntries.first());
        byte[] encryptedCMS = cmsGenerator.generate();

        Pair<ECertificate, PrivateKey> recipient = new Pair<ECertificate, PrivateKey>(ECEncEntries.first(), ECEncEntries.second());
        IDecryptorStore decryptor = new MemoryDecryptor(recipient);
        CmsEnvelopeParser cmsParser = new CmsEnvelopeParser(encryptedCMS);
        encryptedCMS = cmsParser.addRecipients(config, decryptor, RSAEncEntries.first());

        recipient = new Pair<ECertificate, PrivateKey>(RSAEncEntries.first(),RSAEncEntries.second());
        decryptor = new MemoryDecryptor(recipient);
        cmsParser = new CmsEnvelopeParser(encryptedCMS);
        byte[] plainData = cmsParser.open(decryptor);

        assertEquals(TestData.plainString, new String(plainData));
    }

    private void tryAddingRecipientToParserWithStream()throws Exception {

        //Create new encrypted file
        String encryptedFileName = TestData.plainFile.getParent() +  "\\Encrypted" + TestData.plainFile.getName();
        FileOutputStream encryptedOutputStream = new FileOutputStream(encryptedFileName);

        RSAEncEntries = CertificateTestConstants.getRSAEncCertificateAndKey();
        ECEncEntries  = CertificateTestConstants.getECEncCertificateAndKey();

        FileInputStream plainInputStream = new FileInputStream(TestData.plainFile);

        TestData.configureCertificateValidation(config);

        CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
        cmsGenerator.addRecipients(config,ECEncEntries.first());
        cmsGenerator.generate(encryptedOutputStream);

        encryptedOutputStream.close();
        plainInputStream.close();

        //Add new recipient to encrypted file
        FileInputStream encryptedInputStream = new FileInputStream(encryptedFileName);

        String newRecAddEncFileName = TestData.plainFile.getParent() +  "\\RecAddedEnc" + TestData.plainFile.getName();
        FileOutputStream  newRecAddOutputStream = new FileOutputStream(newRecAddEncFileName);

        Pair<ECertificate,PrivateKey> recipient = new Pair<ECertificate,PrivateKey>(ECEncEntries.first(),ECEncEntries.second());
        IDecryptorStore decryptor = new MemoryDecryptor(recipient);
        CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(encryptedInputStream);
        cmsParser.addRecipients(config, newRecAddOutputStream, decryptor, RSAEncEntries.first());

        newRecAddOutputStream.close();
        encryptedInputStream.close();

        //Decrypt to check new recipient added correctly
        String decryptedFileName = TestData.plainFile.getParent() +  "\\Decrypted" + TestData.plainFile.getName();

        boolean result;

        //Decrypt to check
        CmsEnvelopeTestUtil.decryptWithStream(ECEncEntries.second(),ECEncEntries.first(), newRecAddEncFileName, decryptedFileName);
        result = compareFiles(TestData.plainFile.getAbsolutePath(), decryptedFileName);
        Assert.assertTrue(result);

        CmsEnvelopeTestUtil.decryptWithStream(RSAEncEntries.second(), RSAEncEntries.first(), newRecAddEncFileName, decryptedFileName);
        result = compareFiles(TestData.plainFile.getAbsolutePath(), decryptedFileName);
        Assert.assertTrue(result);
    }

    private void tryKeyAgreementAndKeyTrans() throws Exception {

        byte[] plainData;

        TestData.configureCertificateValidation(config);

        CmsEnvelopeGenerator cmsGenerator = new CmsEnvelopeGenerator(TestData.plainString.getBytes());
        cmsGenerator.addRecipients(config, TestData.recipientCert);
        cmsGenerator.addRecipients(config, TestData.recipientEcCert);

        byte[] encryptedCMS = cmsGenerator.generate();

        plainData = CmsEnvelopeTestUtil.decryptInMemory(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), encryptedCMS);
        assertEquals(TestData.plainString, new String(plainData));

        plainData = CmsEnvelopeTestUtil.decryptInMemory(TestData.recipientEcCert, TestData.recipientEcKeyPair.getPrivate(), encryptedCMS);
        assertEquals(TestData.plainString, new String(plainData));
    }

    private void tryKeyAgreementAndKeyTransWitStream() throws Exception {

        String encryptedFileName = TestData.plainFile.getParent() +  "\\Encrypted" + TestData.plainFile.getName();
        FileOutputStream encryptedOutputStream = new FileOutputStream(encryptedFileName);

        FileInputStream plainInputStream = new FileInputStream(TestData.plainFile);

        TestData.configureCertificateValidation(config);

        CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
        cmsGenerator.addRecipients(config, TestData.recipientCert);
        cmsGenerator.addRecipients(config, TestData.recipientEcCert);
        cmsGenerator.generate(encryptedOutputStream);

        encryptedOutputStream.close();
        plainInputStream.close();

        String decryptedFileName = TestData.plainFile.getParent() +  "\\Decrypted" + TestData.plainFile.getName();

        boolean result;

        //Decrypt to check
        CmsEnvelopeTestUtil.decryptWithStream(TestData.recipientKeyPair.getPrivate(), TestData.recipientCert, encryptedFileName, decryptedFileName);
        result = compareFiles(TestData.plainFile.getAbsolutePath(), decryptedFileName);
        Assert.assertTrue(result);

        CmsEnvelopeTestUtil.decryptWithStream(TestData.recipientEcKeyPair.getPrivate(), TestData.recipientEcCert, encryptedFileName, decryptedFileName);
        result = compareFiles(TestData.plainFile.getAbsolutePath(), decryptedFileName);
        Assert.assertTrue(result);
    }

    private void tryKeyTransOAEPDefaultWithByteStream() throws Exception {

        InputStream plainInputStream = new ByteArrayInputStream(TestData.plainString.getBytes());
        ByteArrayOutputStream encryptedOutputStream = new ByteArrayOutputStream();

        CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
        cmsGenerator.addRecipients(TestData.ENVELOPE_CONFIG,TestData.recipientCert);
        cmsGenerator.generate(encryptedOutputStream);

        ByteArrayInputStream bais = new ByteArrayInputStream(encryptedOutputStream.toByteArray());
        byte[] decryptedBytes = CmsEnvelopeTestUtil.decryptWithStream(TestData.recipientKeyPair.getPrivate(),TestData.recipientCert, bais);

        assertArrayEquals(TestData.plainString.getBytes(), decryptedBytes);
    }
}
