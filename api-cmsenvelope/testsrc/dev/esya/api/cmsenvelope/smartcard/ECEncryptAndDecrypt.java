package dev.esya.api.cmsenvelope.smartcard;

import bundle.esya.api.cmsenvelope.TestConstants;
import org.junit.Assert;
import org.junit.Test;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeGenerator;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeParser;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.EnvelopeConfig;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.IDecryptorStore;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.MemoryDecryptor;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.SCDecryptor;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.util.PfxParser;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.List;

public class ECEncryptAndDecrypt {

    private static final String DIRAK_PIN = "12345678";
    private static final String AKIS_PIN = "12345";
    private static SmartCard sc = null;
    private static long slotNo = 0;
    private static long sessionId = 0;

    public void initSmartCardForAKIS() throws PKCS11Exception, IOException {
        sc = new SmartCard(CardType.AKIS);
        slotNo = sc.getSlotList()[0];
        sessionId = sc.openSession(slotNo);
        sc.login(sessionId, AKIS_PIN);
    }

    public void initSmartCardForDIRAK() throws PKCS11Exception, IOException {
        sc = new SmartCard(CardType.DIRAKHSM);
        slotNo = sc.getSlotList()[0];
        sessionId = sc.openSession(slotNo);
        sc.login(sessionId, DIRAK_PIN);
    }

    public void destroySmartCard() throws PKCS11Exception {
        sc.logout(sessionId);
        sc.closeSession(sessionId);
    }

    @Test
    public void encryptDataUsingPFX() throws ESYAException, IOException {
        byte[] contentToBeEncrypted = RandomUtil.generateRandom(1000);
        FileInputStream pfxFileInputStream = new FileInputStream("T:\\api-cmsenvelope\\testdata\\cmsenvelope\\ENC_pfx.p12");
        PfxParser pfxParser = new PfxParser(pfxFileInputStream, "123456");
        List<Pair<ECertificate, PrivateKey>> entries = pfxParser.getCertificatesAndKeys();
        pfxFileInputStream.close();

        ECertificate cert = entries.get(0).getObject1();
        EnvelopeConfig config = new EnvelopeConfig();
        config.setPolicy(TestConstants.getPolicyFile());
        config.skipCertificateValidation();

        CmsEnvelopeGenerator cmsGen = new CmsEnvelopeGenerator(contentToBeEncrypted);
        cmsGen.addRecipients(config, cert);

        byte[] encryptedData = cmsGen.generate();

        CmsEnvelopeParser cmsParser = new CmsEnvelopeParser(encryptedData);
        IDecryptorStore decryptor = new MemoryDecryptor(entries.get(0));

        byte[] decryptedContent = cmsParser.open(decryptor);

        Assert.assertArrayEquals(contentToBeEncrypted, decryptedContent);
    }

    @Test
    public void encryptDataUsingAKIS() throws IOException, ESYAException, PKCS11Exception {
        initSmartCardForAKIS();

        byte[] contentToBeEncrypted = RandomUtil.generateRandom(1000);

        EnvelopeConfig config = new EnvelopeConfig();
        config.setPolicy(TestConstants.getPolicyFile());
        config.skipCertificateValidation();

        List<byte[]> certs = sc.getEncryptionCertificates(sessionId);
        ECertificate cert = new ECertificate(certs.get(0));

        CmsEnvelopeGenerator cmsGen = new CmsEnvelopeGenerator(contentToBeEncrypted);
        cmsGen.addRecipients(config, cert);

        byte[] encryptedData = cmsGen.generate();

        CmsEnvelopeParser cmsParser = new CmsEnvelopeParser(encryptedData);
        IDecryptorStore decryptor = new SCDecryptor(sc, sessionId, Crypto.getProvider());
        byte[] decryptedBytes = cmsParser.open(decryptor);

        destroySmartCard();

        Assert.assertArrayEquals(contentToBeEncrypted, decryptedBytes);
    }

    @Test
    public void encryptDataUsingDIRAK() throws IOException, ESYAException, PKCS11Exception {
        initSmartCardForDIRAK();

        byte[] contentToBeEncrypted = RandomUtil.generateRandom(1000);

        EnvelopeConfig config = new EnvelopeConfig();
        config.setPolicy(TestConstants.getPolicyFile());
        config.skipCertificateValidation();

        List<byte[]> certs = sc.getEncryptionCertificates(sessionId);
        ECertificate cert = new ECertificate(certs.get(0));

        CmsEnvelopeGenerator cmsGen = new CmsEnvelopeGenerator(contentToBeEncrypted);
        cmsGen.addRecipients(config, cert);

        byte[] encryptedData = cmsGen.generate();

        CmsEnvelopeParser cmsParser = new CmsEnvelopeParser(encryptedData);
        IDecryptorStore decryptor = new SCDecryptor(sc, sessionId, Crypto.getProvider());
        byte[] decryptedBytes = cmsParser.open(decryptor);

        destroySmartCard();

        Assert.assertArrayEquals(contentToBeEncrypted, decryptedBytes);
    }
}
