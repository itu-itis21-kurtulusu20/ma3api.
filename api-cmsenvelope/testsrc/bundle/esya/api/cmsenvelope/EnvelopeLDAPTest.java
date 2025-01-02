package bundle.esya.api.cmsenvelope;

import org.junit.Ignore;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeStreamGenerator;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeStreamParser;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.SCDecryptor;
import org.junit.BeforeClass;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.EnvelopeConfig;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.IDecryptorStore;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

@Ignore("Bundle tests")
public class EnvelopeLDAPTest {

    @BeforeClass
    public static void loadLicense() throws Exception {
        TestConstants.setLicense();
    }

    @Test
    public void testToEncryptWithLDAP() throws Exception {
        int size = 50;
        byte[] data = new byte[size];
        Random rand = new Random();
        rand.nextBytes(data);

        System.out.println("Plain Data:\t" + Arrays.toString(data));

        //Get certificate
        ECertificate[] certs = LDAPUtil.readEncCertificatesFromDirectory("test@test.net");

        if (certs == null || certs.length == 0)
            throw new Exception("Certificate Can not find");

        ECertificate cert = certs[0];

        InputStream plainInputStream = new ByteArrayInputStream(data);
        ByteArrayOutputStream encryptedOutputStream = new ByteArrayOutputStream();

        EnvelopeConfig config = new EnvelopeConfig();
        config.setPolicy(TestConstants.getPolicyFile());

        CmsEnvelopeStreamGenerator cmsGenerator = new CmsEnvelopeStreamGenerator(plainInputStream);
        cmsGenerator.addRecipients(config,cert);
        cmsGenerator.generate(encryptedOutputStream);

        ByteArrayInputStream bais = new ByteArrayInputStream(encryptedOutputStream.toByteArray());

        byte[] decrypted = _decrypt(bais);
        System.out.println("Decypted Data:\t" + Arrays.toString(decrypted));

        if (Arrays.equals(data, decrypted) == false) {
            System.out.println(false);
            throw new Exception("Not equal");
        }
    }

    private static byte[] _decrypt(ByteArrayInputStream encryptedInputStream) throws Exception {
        ByteArrayOutputStream decryptedOutputStream = new ByteArrayOutputStream();

        SmartCard sc = new SmartCard(CardType.AKIS);
        long slot = sc.getSlotList()[0];
        long session = sc.openSession(slot);
        sc.login(session, TestConstants.getPIN());

        CmsEnvelopeStreamParser cmsParser = new CmsEnvelopeStreamParser(encryptedInputStream);
        IDecryptorStore decryptor = new SCDecryptor(sc, session);
        cmsParser.open(decryptedOutputStream, decryptor);

        sc.logout(session);
        sc.closeSession(session);

        return decryptedOutputStream.toByteArray();
    }
}
