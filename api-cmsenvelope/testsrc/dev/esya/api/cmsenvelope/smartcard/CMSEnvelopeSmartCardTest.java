package dev.esya.api.cmsenvelope.smartcard;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import test.esya.api.cmsenvelope.TestData;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.*;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.IDecryptorStore;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.SCDecryptor;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;

import java.io.*;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by sura.emanet on 31.05.2017.
 */
@Ignore("Smartcard tests")
public class CMSEnvelopeSmartCardTest {

    static final String PIN = "123456";

    static SmartCard sc = null;
    static CardType cardType = CardType.DIRAKHSM;
    static boolean fipsMode = true;
    static long sessionId = 0;
    static long slotNo = 0;

    final File plainFile = new File("testdata\\cmsenvelope\\test.txt");

    @BeforeClass
    public static void initSmartcard() throws Exception{

        sc = new SmartCard(cardType);
        sc.setFipsMode(fipsMode);
        slotNo = sc.getSlotList()[0];
        sessionId = sc.openSession(slotNo);
        sc.login(sessionId, PIN);
    }

    @AfterClass
    public static void destroySmartcard() throws Exception{

        sc.logout(sessionId);
        sc.closeSession(sessionId);
    }

    @Test
    public void testKeyTransOAEPSHA1AES128() throws Exception {
        tryKeyTrans(CipherAlg.RSA_OAEP,CipherAlg.AES128_CBC);
    }
    
    @Test
    public void testKeyTransOAEPSHA1AES192() throws Exception {
        tryKeyTrans(CipherAlg.RSA_OAEP,CipherAlg.AES192_CBC);
    }

    @Test
    public void testKeyTransOAEPSHA1AES256() throws Exception {
        tryKeyTrans(CipherAlg.RSA_OAEP,CipherAlg.AES256_CBC);
    }

    @Test
    public void testKeyTransOAEPSHA256AES128() throws Exception {
        tryKeyTrans(CipherAlg.RSA_OAEP_SHA256, CipherAlg.AES128_CBC );
    }

    @Test
    public void testKeyTransOAEPSHA256AES192() throws Exception {
        tryKeyTrans(CipherAlg.RSA_OAEP_SHA256, CipherAlg.AES192_CBC );
    }

    @Test
    public void testKeyTransOAEPSHA256AES256() throws Exception {
        tryKeyTrans(CipherAlg.RSA_OAEP_SHA256, CipherAlg.AES256_CBC);
    }

    @Test
    public void testKeyTransPKCS1AES128() throws Exception {
        tryKeyTrans(CipherAlg.RSA_PKCS1, CipherAlg.AES128_CBC);
    }

    @Test
    public void testKeyTransPKCS1AES192() throws Exception {
        tryKeyTrans(CipherAlg.RSA_PKCS1, CipherAlg.AES192_CBC);
    }

    @Test
    public void testKeyTransPKCS1AES256() throws Exception {
        tryKeyTrans(CipherAlg.RSA_PKCS1, CipherAlg.AES256_CBC);
    }

    @Test(expected = CMSException.class)
    public void test3DESAlgorithmForKeyTrans() throws Exception {
        tryKeyTrans(CipherAlg.RSA_OAEP_SHA256, CipherAlg.DES_EDE3_CBC);
    }

    @Test(expected = CMSException.class)
    public void testRC2AlgorithmForKeyTrans() throws Exception {
        tryKeyTrans(CipherAlg.RSA_OAEP_SHA256, CipherAlg.RC2_CBC);
    }

    @Test (expected = CMSException.class)
    public void testInvalidModeForKeyTrans() throws Exception {
        tryKeyTrans( CipherAlg.RSA_OAEP_SHA256, CipherAlg.AES256_ECB);
    }

    @Test
    public void testAddingNewRecipientsToEnvelopeWithOAEPSHA1() throws Exception {
        addNewRecipientsToEnvelope(CipherAlg.RSA_OAEP);
    }

    @Test
    public void testAddingNewRecipientsToEnvelopeWithOAEPSHA256() throws Exception {
        addNewRecipientsToEnvelope(CipherAlg.RSA_OAEP_SHA256);
    }

    @Test
    public void testOAEPDefault() throws Exception {

        List<byte[]> certs = sc.getEncryptionCertificates(sessionId);
        CmsEnvelopeGenerator cmsGenerator = new CmsEnvelopeGenerator(TestData.plainString.getBytes());

        for(byte[] bs : certs){
            ECertificate cert = new ECertificate(bs);
            if(cert.isQualifiedCertificate())
                System.out.println(cert.getSubject().stringValue());
            cmsGenerator.addRecipients(TestData.ENVELOPE_CONFIG,cert);
        }

        byte[] encryptedBytes = cmsGenerator.generate();

        CmsEnvelopeParser cmsParser = new CmsEnvelopeParser(encryptedBytes);
        IDecryptorStore decryptor = new SCDecryptor(sc, sessionId);
        byte[] decryptedBytes = cmsParser.open(decryptor);

        assertEquals(TestData.plainString, new String(decryptedBytes));

    }

    @Test
    public void testKeyTransOAEPDefaultWithByteStream() throws Exception {

        List<byte[]> certs = sc.getEncryptionCertificates(sessionId);
        ECertificate cert = new ECertificate(certs.get(0));

        ByteArrayInputStream plainInputStream = new ByteArrayInputStream(TestData.plainString.getBytes());
        ByteArrayOutputStream encryptedOutputStream = new ByteArrayOutputStream();

        CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
        cmsGenerator.addRecipients(TestData.ENVELOPE_CONFIG,cert);
        cmsGenerator.generate(encryptedOutputStream);

        encryptedOutputStream.close();
        plainInputStream.close();

        ByteArrayInputStream EncryptedInputStream = new ByteArrayInputStream(encryptedOutputStream.toByteArray());
        ByteArrayOutputStream decryptedOutputStream = new ByteArrayOutputStream();

        CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(EncryptedInputStream);
        IDecryptorStore decryptor = new SCDecryptor(sc, sessionId);
        cmsParser.open(decryptedOutputStream, decryptor);

        assertEquals(TestData.plainString, decryptedOutputStream.toString());
    }

    @Test
    public void testKeyTransOAEPDefaultWithFileStream() throws Exception {

        List<byte[]> certs = sc.getEncryptionCertificates(sessionId);
        ECertificate cert = new ECertificate(certs.get(0));

        String encryptedFileName = plainFile.getParent() +  "\\Encrypted" + plainFile.getName();
        FileOutputStream encryptedOutputStream = new FileOutputStream(encryptedFileName);

        FileInputStream plainInputStream = new FileInputStream(plainFile);

        CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
        cmsGenerator.addRecipients(TestData.ENVELOPE_CONFIG,cert);
        cmsGenerator.generate(encryptedOutputStream);

        encryptedOutputStream.close();
        plainInputStream.close();

        String decryptedFileName = plainFile.getParent() +  "\\Decrypted" + plainFile.getName();

        FileInputStream EncryptedInputStream = new FileInputStream(encryptedFileName);
        FileOutputStream decryptedOutputStream = new FileOutputStream(decryptedFileName);

        CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(EncryptedInputStream);
        IDecryptorStore decryptor = new SCDecryptor(sc, sessionId);
        cmsParser.open(decryptedOutputStream, decryptor);

    }

    private void tryKeyTrans(CipherAlg cipherAlg, CipherAlg symmetricAlgorithm) throws Exception {

        List<byte[]> cert = sc.getEncryptionCertificates(sessionId);
        ECertificate cer = new ECertificate(cert.get(0));

        EnvelopeConfig config = new EnvelopeConfig();
        config.setRsaKeyTransAlg(cipherAlg);
        TestData.configureCertificateValidation(config);

        CmsEnvelopeGenerator cmsGenerator = new CmsEnvelopeGenerator(TestData.plainString.getBytes(), symmetricAlgorithm);
        cmsGenerator.addRecipients(config,cer);
        byte[] encryptedCMS = cmsGenerator.generate();

        CmsEnvelopeParser cmsParser = new CmsEnvelopeParser(encryptedCMS);
        IDecryptorStore decryptor = new SCDecryptor(sc, sessionId);
        byte[] decryptedBytes = cmsParser.open(decryptor);

        assertEquals(TestData.plainString, new String(decryptedBytes));

    }

    private void addNewRecipientsToEnvelope(CipherAlg cipherAlg)  throws Exception {

        List<byte[]> certs = sc.getEncryptionCertificates(sessionId);
        ECertificate cert = new ECertificate(certs.get(0));

        EnvelopeConfig config = new EnvelopeConfig();
        config.setRsaKeyTransAlg(cipherAlg);
        TestData.configureCertificateValidation(config);

        //Add first recipient
        InputStream plainInputStream = new ByteArrayInputStream(TestData.plainString.getBytes());
        ByteArrayOutputStream envelopeWithOneRecipient = new ByteArrayOutputStream();

        CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream );
        cmsGenerator.addRecipients(config,cert);
        cmsGenerator.generate(envelopeWithOneRecipient);
        //CMS Envelope with one recipient is generated.

        //Add a new recipient to envelope
        ByteArrayInputStream bais = new ByteArrayInputStream(envelopeWithOneRecipient.toByteArray());
        ByteArrayOutputStream envelopeWithTwoRecipient = new ByteArrayOutputStream();

        CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(bais);
        IDecryptorStore decryptor = new SCDecryptor(sc, sessionId);
        cmsParser.addRecipients(config, envelopeWithTwoRecipient, decryptor, cert);
    }

}