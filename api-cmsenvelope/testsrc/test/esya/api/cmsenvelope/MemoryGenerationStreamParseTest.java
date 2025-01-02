package test.esya.api.cmsenvelope;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeGenerator;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.EnvelopeConfig;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MemoryGenerationStreamParseTest {

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

    public MemoryGenerationStreamParseTest(CipherAlg asymmetricAlg, CipherAlg symmetricAlg){
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

    private void tryKeyTrans(ECertificate recipientCert, PrivateKey aPrivate, CipherAlg cipherAlg, CipherAlg symmetricAlg) throws Exception {

        tryKeyTrans(recipientCert, aPrivate, cipherAlg, symmetricAlg, TestData.plainString.getBytes());
    }

    private void tryKeyTrans(ECertificate recipientCert, PrivateKey aPrivate, CipherAlg asymmetricAlg, CipherAlg symmetricAlg, byte[] dataToBeEncrypted) throws Exception {

        EnvelopeConfig config = new EnvelopeConfig();
        config.setRsaKeyTransAlg(asymmetricAlg);
        TestData.configureCertificateValidation(config);

        CmsEnvelopeGenerator cmsGenerator = new CmsEnvelopeGenerator(dataToBeEncrypted, symmetricAlg);
        cmsGenerator.addRecipients(config, recipientCert);
        byte[] encryptedCMS = cmsGenerator.generate();

        byte[] decryptedBytes = CmsEnvelopeTestUtil.decryptWithStream(aPrivate, recipientCert, new ByteArrayInputStream(encryptedCMS));
        Assert.assertArrayEquals(dataToBeEncrypted, decryptedBytes);
    }

}
