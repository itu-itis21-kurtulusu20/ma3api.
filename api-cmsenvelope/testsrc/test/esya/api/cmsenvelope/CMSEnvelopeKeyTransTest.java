package test.esya.api.cmsenvelope;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeGenerator;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeStreamGenerator;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.EnvelopeConfig;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;

import java.io.*;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Collection;

import static test.esya.api.cmsenvelope.CmsEnvelopeTestUtil.compareFiles;

@RunWith(Enclosed.class)
public class CMSEnvelopeKeyTransTest {

    @RunWith(Parameterized.class)
    public static class CMSEnvelopeKeyTrans{

        private CipherAlg asymmetricAlg;
        private CipherAlg symmetricAlg;

        @Parameterized.Parameters(name = "{0}, {1}")
        public static Collection<Object[]> data(){
            return Arrays.asList(new Object[][]{{CipherAlg.RSA_OAEP,CipherAlg.AES128_CBC},
                                                {CipherAlg.RSA_OAEP,CipherAlg.AES192_CBC},
                                                {CipherAlg.RSA_OAEP,CipherAlg.AES256_CBC},
                                                {CipherAlg.RSA_OAEP,CipherAlg.AES128_GCM},
                                                {CipherAlg.RSA_OAEP,CipherAlg.AES192_GCM},
                                                {CipherAlg.RSA_OAEP,CipherAlg.AES256_GCM},

                                                {CipherAlg.RSA_OAEP_SHA256,CipherAlg.AES128_CBC},
                                                {CipherAlg.RSA_OAEP_SHA256,CipherAlg.AES192_CBC},
                                                {CipherAlg.RSA_OAEP_SHA256,CipherAlg.AES256_CBC},
                                                {CipherAlg.RSA_OAEP_SHA256,CipherAlg.AES128_GCM},
                                                {CipherAlg.RSA_OAEP_SHA256,CipherAlg.AES192_GCM},
                                                {CipherAlg.RSA_OAEP_SHA256,CipherAlg.AES256_GCM},

                                                {CipherAlg.RSA_PKCS1, CipherAlg.AES128_CBC},
                                                {CipherAlg.RSA_PKCS1, CipherAlg.AES192_CBC},
                                                {CipherAlg.RSA_PKCS1, CipherAlg.AES256_CBC},
                                                {CipherAlg.RSA_PKCS1, CipherAlg.AES128_GCM},
                                                {CipherAlg.RSA_PKCS1, CipherAlg.AES192_GCM},
                                                {CipherAlg.RSA_PKCS1, CipherAlg.AES256_GCM}
            });
        }

        public CMSEnvelopeKeyTrans(CipherAlg asymmetricAlg, CipherAlg symmetricAlg){
           this.asymmetricAlg = asymmetricAlg;
           this.symmetricAlg = symmetricAlg;
        }

        @Test
        public void testKeyTrans() throws Exception {
            tryKeyTrans(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), asymmetricAlg,symmetricAlg);
        }

        @Test
        public void testKeyTransUpTo2048LengthData() throws Exception {

            for(int i=1; i<2048; i++){

                byte[] dataToBeEncrypted = RandomUtil.generateRandom(i);
                tryKeyTrans(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), asymmetricAlg, symmetricAlg, dataToBeEncrypted);
            }
        }

        @Test
        public void  testKeyTransWithStream() throws Exception {
            tryKeyTransWithStream(TestData.plainFile, TestData.recipientCert, TestData.recipientKeyPair.getPrivate(),asymmetricAlg, symmetricAlg);
        }

        @Test
        public void testKeyTransWithStreamUpTo2048LengthData() throws Exception {

            for(int i=1; i<2048; i++){

                byte[] dataToBeEncrypted = RandomUtil.generateRandom(i);
                tryKeyTransWithStream(dataToBeEncrypted, TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), asymmetricAlg, symmetricAlg);
            }
        }
    }

    public static class DeprecatedAlgorithmsTest{

            @Test(expected = CMSException.class)
            public void test3DESAlgorithmForKeyTrans() throws Exception {
                tryKeyTrans(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), CipherAlg.RSA_OAEP_SHA256, CipherAlg.DES_EDE3_CBC);
            }

            @Test(expected = CMSException.class)
            public void test3DESAlgorithmForKeyTransWithStream() throws Exception {
                tryKeyTransWithStream(TestData.plainFile, TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), CipherAlg.RSA_OAEP_SHA256, CipherAlg.DES_EDE3_CBC);
            }

            @Test(expected = CMSException.class)
            public void testRC2AlgorithmForKeyTrans() throws Exception {
                tryKeyTrans(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), CipherAlg.RSA_OAEP_SHA256, CipherAlg.RC2_CBC);
            }

            @Test(expected = CMSException.class)
            public void testRC2AlgorithmForKeyTransWithStream() throws Exception {
                tryKeyTransWithStream(TestData.plainFile, TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), CipherAlg.RSA_OAEP_SHA256, CipherAlg.RC2_CBC);
            }

            @Test(expected = CMSException.class)
            public void testInvalidModeForKeyTrans() throws Exception {
                tryKeyTrans(TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), CipherAlg.RSA_OAEP_SHA256, CipherAlg.AES256_ECB);
            }

            @Test(expected = CMSException.class)
            public void testInvalidModeForKeyTransWithStream() throws Exception {
                tryKeyTransWithStream(TestData.plainFile, TestData.recipientCert, TestData.recipientKeyPair.getPrivate(), CipherAlg.RSA_OAEP_SHA256, CipherAlg.AES256_ECB);
            }
    }

    private static void tryKeyTrans(ECertificate recipientCert, PrivateKey aPrivate, CipherAlg cipherAlg, CipherAlg symmetricAlg) throws Exception {

        tryKeyTrans(recipientCert, aPrivate, cipherAlg, symmetricAlg, TestData.plainString.getBytes());
    }

    private static void tryKeyTrans(ECertificate cert, PrivateKey privKey, CipherAlg cipherAlg, CipherAlg symmetricAlgorithm, byte[] dataToBeEncrypted) throws Exception {

        EnvelopeConfig config = new EnvelopeConfig();
        config.setRsaKeyTransAlg(cipherAlg);
        TestData.configureCertificateValidation(config);

        CmsEnvelopeGenerator cmsGenerator = new CmsEnvelopeGenerator(dataToBeEncrypted, symmetricAlgorithm);
        cmsGenerator.addRecipients(config, cert);
        byte[] encryptedCMS = cmsGenerator.generate();

        byte[] plainData = CmsEnvelopeTestUtil.decryptInMemory(cert, privKey, encryptedCMS);
        Assert.assertArrayEquals(dataToBeEncrypted, plainData);
    }

    private static void tryKeyTransWithStream(File file, ECertificate cert, PrivateKey privKey, CipherAlg cipherAlg, CipherAlg symmetricAlgorithm)throws Exception
    {

        String encryptedFileName = file.getParent() +  "\\Encrypted" + file.getName();
        FileOutputStream encryptedOutputStream = new FileOutputStream(encryptedFileName);
        FileInputStream plainInputStream = new FileInputStream(file);

        EnvelopeConfig config = new EnvelopeConfig();
        config.setRsaKeyTransAlg(cipherAlg);
        TestData.configureCertificateValidation(config);

        CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream, symmetricAlgorithm);
        cmsGenerator.addRecipients(config, cert);
        cmsGenerator.generate(encryptedOutputStream);

        encryptedOutputStream.close();
        plainInputStream.close();

        String decryptedFileName = file.getParent() +  "\\Decrypted" + file.getName();

        //Decrypt to check
        CmsEnvelopeTestUtil.decryptWithStream(privKey, cert, encryptedFileName, decryptedFileName);
        boolean result = compareFiles(file.getAbsolutePath(), decryptedFileName);

        Assert.assertTrue(result);
    }

    private static void tryKeyTransWithStream(byte[] dataToBeEncrypted, ECertificate recipientCert, PrivateKey aPrivate, CipherAlg cipherAlg, CipherAlg symmetricAlg)throws Exception
    {
        InputStream plainInputStream = new ByteArrayInputStream(dataToBeEncrypted);
        ByteArrayOutputStream encryptedOutputStream = new ByteArrayOutputStream();

        EnvelopeConfig config = new EnvelopeConfig();
        config.setRsaKeyTransAlg(cipherAlg);
        TestData.configureCertificateValidation(config);

        CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream,symmetricAlg);
        cmsGenerator.addRecipients(config, recipientCert);
        cmsGenerator.generate(encryptedOutputStream);

        //Decrypt to check
        byte[] decryptedBytes = CmsEnvelopeTestUtil.decryptWithStream(aPrivate, recipientCert, new ByteArrayInputStream(encryptedOutputStream.toByteArray()));
        Assert.assertArrayEquals(dataToBeEncrypted, decryptedBytes);
    }
}

