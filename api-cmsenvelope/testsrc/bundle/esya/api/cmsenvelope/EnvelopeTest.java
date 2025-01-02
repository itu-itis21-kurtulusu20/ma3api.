package bundle.esya.api.cmsenvelope;

import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeStreamGenerator;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeStreamParser;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.EnvelopeConfig;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.IDecryptorStore;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.SCDecryptor;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SmartCardSeed;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;

@Ignore("Bundle tests")
public class EnvelopeTest {

    @BeforeClass
    public static void loadLicense() throws Exception {
        TestConstants.setLicense();
    }

    @Test
    public void testToEncrypt() throws Exception {

        InputStream plainInputStream = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream encryptedOutputStream = new ByteArrayOutputStream();
        ECertificate cert = TestConstants.getReceiverCert();

        EnvelopeConfig config = new EnvelopeConfig();
        config.setPolicy(TestConstants.getPolicyFile());

        CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
        cmsGenerator.addRecipients(config,cert);
        cmsGenerator.generate(encryptedOutputStream);

        ByteArrayInputStream bais = new ByteArrayInputStream(encryptedOutputStream.toByteArray());

        Assert.assertEquals("test", new String(testToDecrypt(bais)));
    }

    @Test
    public void testAddingNewRecipientsToEnvelope() throws Exception {

        //Add first recipient
        InputStream plainInputStream = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream envelopeWithOneRecipient = new ByteArrayOutputStream();
        ECertificate cert = TestConstants.getReceiverCert();

        EnvelopeConfig config = new EnvelopeConfig();
        config.setPolicy(TestConstants.getPolicyFile());

        CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
        cmsGenerator.addRecipients(config,cert);
        cmsGenerator.generate(envelopeWithOneRecipient);
        //CMS Envelope with one recipient is generated.

        //Add a new recipient to envelope.
        ByteArrayInputStream bais = new ByteArrayInputStream(envelopeWithOneRecipient.toByteArray());
        ByteArrayOutputStream envelopeWithTwoRecipient = new ByteArrayOutputStream();

        SmartCard sc = new SmartCard(CardType.AKIS);
        long slot = sc.getSlotList()[0];
        long session = sc.openSession(slot);
        sc.login(session, TestConstants.getPIN());

        CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(bais);
        IDecryptorStore decryptor = new SCDecryptor(sc, session);
        cmsParser.addRecipients(config,envelopeWithTwoRecipient, decryptor, cert);

        sc.logout(session);
        sc.closeSession(session);
    }

    @Test
    public void testGettingCertificateFromLDAP() throws Exception {
        ECertificate originalCert = TestConstants.getReceiverCert();
        ECertificate adcert = LDAPUtil.readEncCertificatesFromDirectory("test@test.net")[0];
        assertEquals(Arrays.equals(originalCert.getEncoded(), adcert.getEncoded()), true);
    }


    @Test
    public void testSmartCardRandomSeed() throws Exception{
        //SmartCard'ın seed olarak atanması uygulama yaşam döngüsünde bir kere yapılmalıdır!!!
        SmartCard sc = new SmartCard(CardType.AKIS);
        long slot = sc.getSlotList()[0];
        SmartCardSeed scSeed = new SmartCardSeed(CardType.AKIS, slot);
        Crypto.getRandomGenerator().removeAllSeeders();
        Crypto.getRandomGenerator().addSeeder(scSeed);
        //Atama İşlemi Sonu

        //Test
        byte []randomBytes = new byte[256];
        Crypto.getRandomGenerator().nextBytes(randomBytes);

        System.out.println("RandomBytes:" + StringUtil.toHexString(randomBytes) );
    }

    private byte[] testToDecrypt(InputStream encryptedInputStream) throws Exception {
        ByteArrayOutputStream decryptedOutputStream = new ByteArrayOutputStream();

        SmartCard sc = new SmartCard(CardType.AKIS);
        long slot = sc.getSlotList()[0];
        long session = sc.openSession(slot);
        sc.login(session, TestConstants.getPIN());

        CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(encryptedInputStream);
        IDecryptorStore decryptor = new SCDecryptor(sc, session);
        cmsParser.open(decryptedOutputStream, decryptor);

        sc.logout(session);

        return decryptedOutputStream.toByteArray();
    }

}
