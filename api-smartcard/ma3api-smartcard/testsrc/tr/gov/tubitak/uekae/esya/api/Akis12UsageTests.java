package tr.gov.tubitak.uekae.esya.api;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EKeyUsage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.LicenseUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.MGF;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.PfxParser;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.apdu.APDUSmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.apdu.PCTerminalHandler;
import tr.gov.tubitak.uekae.esya.api.smartcard.apdu.TerminalHandler;
import tr.gov.tubitak.uekae.esya.api.smartcard.pin.APDUPP;
import tr.gov.tubitak.uekae.esya.api.smartcard.pin.KartParolaDegistirici;
import tr.gov.tubitak.uekae.esya.api.smartcard.pin.ParolaDegistirmeYontemi;
import tr.gov.tubitak.uekae.esya.api.smartcard.pin.ParolaFabrika;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;

import javax.crypto.spec.OAEPParameterSpec;
import javax.smartcardio.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ramazan.girgin on 12/17/2015.
 */
public class Akis12UsageTests extends BaseCardTest{

    @BeforeClass
    public static void setUp() throws Exception {
        BaseCardTest.slotPin = "12345";
        BaseCardTest.slotNo = 1;
        BaseCardTest.cardType = CardType.AKIS;
        BaseCardTest.setUp();
        LicenseUtil.setLicenseXml(new FileInputStream("G:\\APIFreeLisans\\lisans\\lisans.xml"));
    }

    @Override
    public void testMethodSetUp() {
        super.testMethodSetUp();
    }


    @Test
    public void testGenerateRandomNumber() throws PKCS11Exception {
        byte[] randomData = sc.getRandomData(sessionId, 2048);
        Assert.assertTrue(randomData.length == 2048);
    }


    private void signAndVerifyWithKeyLabelPKCS15(String signKeyLabel) throws PKCS11Exception, SmartCardException, CryptoException {
        KeySpec keySpec = sc.readPublicKeySpec(sessionId, signKeyLabel);
        PublicKey publicKey = KeyUtil.generatePublicKey(keySpec);
        signAndVerifyWithKeyLabelPKCS15(signKeyLabel, publicKey);
    }

    private void signAndVerifyWithKeyLabelPKCS15(String signKeyLabel,PublicKey publicKey) throws PKCS11Exception, SmartCardException, CryptoException {

        byte[] dataForSign = "Data For Sign".getBytes();
        System.out.println("Kartta İmzalama işlemi yapılacak.Label :"+signKeyLabel);
        byte[] signature = sc.signData(sessionId, signKeyLabel, dataForSign, new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS));
        System.out.println("Kartta İmzalama işlemi yapıldı.");

        System.out.println("Bellekte imza doğrulama işlemi yapılacak.");
        boolean verified = SignUtil.verify(SignatureAlg.RSA_NONE, dataForSign, signature, publicKey);
        Assert.assertTrue("Imzalanan veri doğrulanamadı.", verified);
        System.out.println("Bellekte imza doğrulama işlemi yapıldı.");
    }

    private void encryptAndDecryptWithKeyLabelPKCS15(String encKeyLabel) throws PKCS11Exception, ESYAException, IOException {
        KeySpec keySpec = sc.readPublicKeySpec(sessionId, encKeyLabel);
        PublicKey publicKey = KeyUtil.generatePublicKey(keySpec);
        encryptAndDecryptWithKeyLabelPKCS15(encKeyLabel, publicKey);
    }

    private void encryptAndDecryptWithKeyLabelPKCS15(String encryptionKeyLabel,PublicKey publicKey) throws PKCS11Exception, ESYAException, IOException {
        byte[] dataForEnc = "Data For Enc".getBytes();
        System.out.println("(PKCS15)Bellekte Şifreleme işlemi yapılacak.Key Label :"+encryptionKeyLabel);
        byte[] encryptedData = CipherUtil.encrypt(CipherAlg.RSA_PKCS1, null, dataForEnc, publicKey);
        System.out.println("(PKCS15)Bellekte Şifreleme işlemi yapıldı.");
        System.out.println("(PKCS15)Kartta Şifre Çözme işlemi yapılacak.");
        byte[] decryptedData = SmartOp.decrypt(sc, sessionId, slotNo, encryptionKeyLabel, encryptedData, CipherAlg.RSA_PKCS1.getName(), null);
        System.out.println("(PKCS15)Kartla Şifre Çözme işlemi yapıldı.");
        boolean isEqual = Arrays.equals(decryptedData, dataForEnc);
        Assert.assertTrue("(PKCS15)Orijinal data and decrypted data must be equal", isEqual);
    }

    @Test
    public void testSignWithSmartCardAndVerifyWithMemoryPKCSForever() throws PKCS11Exception, ESYAException {
        while (true) {
            try {
                testSignWithSmartCardAndVerifyWithMemoryPKCS();
            }finally {
                System.out.println("Önceki session kapatılacak.");
                sc.closeSession(sessionId);
                System.out.println("Önceki session kapatılacak.");
                System.out.println("Yeni session açılacak.");
                sessionId = sc.openSession(slotNo);
                System.out.println("Yeni session açıldı.");
                System.out.println("Tekrar login olunacak");
                sc.login(sessionId, slotPin);
                System.out.println("Tekrar login olundu");
            }
        }
    }
    @Test
    public void testSignWithSmartCardAndVerifyWithMemoryPKCS() throws PKCS11Exception, ESYAException {
        String[] signatureKeyLabels = sc.getSignatureKeyLabels(sessionId);
        String signKeyLabel = signatureKeyLabels[0];

        List<byte[]> signCerts = sc.readCertificate(sessionId, signKeyLabel);
        byte[] signCertByte = signCerts.get(0);
        ECertificate signECert = new ECertificate(signCertByte);
        PublicKey publicKey = KeyUtil.decodePublicKey(signECert.getSubjectPublicKeyInfo());

        signAndVerifyWithKeyLabelPKCS15(signKeyLabel, publicKey);
    }

    @Test
    public void testSignWithAPDUSmartCardAndVerifyWithMemoryPKCS() throws PKCS11Exception, ESYAException, CardException {
        APDUSmartCard apduSmartCard = new APDUSmartCard(new PCTerminalHandler());
        try {
            apduSmartCard.setDisableSecureMessaging(true);
            apduSmartCard.setCommandLoggingPath("C:/tmp/AkisAPDUSign.log");
            apduSmartCard.openSession(slotNo);
            apduSmartCard.login(slotPin);
            List<byte[]> signatureCertificates = apduSmartCard.getSignatureCertificates();
            byte[] signCertByte = signatureCertificates.get(0);
            ECertificate signECert = new ECertificate(signCertByte);
            PublicKey publicKey = KeyUtil.decodePublicKey(signECert.getSubjectPublicKeyInfo());

            byte[] dataForSign = "Data For Sign".getBytes();
            System.out.println("Kartta İmzalama işlemi yapılacak.");
            byte[] signature = apduSmartCard.sign(dataForSign, signECert.asX509Certificate(), SignatureAlg.RSA_SHA256.getName());
            System.out.println("Kartta İmzalama işlemi yapıldı.");

            System.out.println("Bellekte imza doğrulama işlemi yapılacak.");
            boolean verified = SignUtil.verify(SignatureAlg.RSA_SHA256, dataForSign, signature, publicKey);
            Assert.assertTrue("Imzalanan veri doğrulanamadı.", verified);
            System.out.println("Bellekte imza doğrulama işlemi yapıldı.");

        }finally {
            apduSmartCard.closeSession();
        }
    }


    @Test
     public void testEncryptWithMemoryAndDecryptWithSmartCardPKCS15() throws PKCS11Exception, ESYAException, IOException {
        String[] encryptionKeyLabels = sc.getEncryptionKeyLabels(sessionId);
        String encryptionKeyLabel = encryptionKeyLabels[0];

        List<byte[]> encKeyLabels = sc.readCertificate(sessionId, encryptionKeyLabel);
        byte[] encCertByte = encKeyLabels.get(0);
        ECertificate encECert = new ECertificate(encCertByte);

        byte[] dataForEnc = "Data For Enc".getBytes();
        System.out.println("(PKCS15)Bellekte Şifreleme işlemi yapılacak.");
        byte[] encryptedData = CipherUtil.encrypt(CipherAlg.RSA_PKCS1, null, dataForEnc, encECert);
        System.out.println("(PKCS15)Bellekte Şifreleme işlemi yapıldı.");
        System.out.println("(PKCS15)Kartta Şifre Çözme işlemi yapılacak.");
        byte[] decryptedData = SmartOp.decrypt(sc, sessionId, slotNo, encryptionKeyLabel, encryptedData, CipherAlg.RSA_PKCS1.getName(), null);
        System.out.println("(PKCS15)Kartla Şifre Çözme işlemi yapıldı.");
        boolean isEqual = Arrays.equals(decryptedData, dataForEnc);
        Assert.assertTrue("(PKCS15)Orijinal data and decrypted data must be equal", isEqual);
    }

    @Test
    public void testGenerateSignKeyInSmartCard() throws PKCS11Exception, ESYAException, IOException {
        String keyLabel = "rsaSignKey" + System.currentTimeMillis();
        RSAKeyGenParameterSpec rsaKeyGenParameterSpec = new RSAKeyGenParameterSpec(2048,null);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel, rsaKeyGenParameterSpec);
        rsaKeyPairTemplate.getAsTokenTemplate(true,false,false);

        System.out.println("Kartta imzalama anahtarı oluşturulacak. Label :"+keyLabel);
        KeySpec keyPair = sc.createKeyPair(sessionId, rsaKeyPairTemplate);
        System.out.println("Kartta imzalama anahtarı oluşturuldu.");
        signAndVerifyWithKeyLabelPKCS15(keyLabel);
    }

    @Test
    public void testGenerateEncKeyInSmartCard() throws PKCS11Exception, ESYAException, IOException {
        String keyLabel = "rsaEncKey" + System.currentTimeMillis();
        RSAKeyGenParameterSpec rsaKeyGenParameterSpec = new RSAKeyGenParameterSpec(2048,null);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel, rsaKeyGenParameterSpec);
        rsaKeyPairTemplate.getAsTokenTemplate(false,true,false);

        System.out.println("Kartta şifreleme anahtarı oluşturulacak. Label :"+keyLabel);
        KeySpec keyPair = sc.createKeyPair(sessionId, rsaKeyPairTemplate);
        System.out.println("Kartta şifreleme anahtarı oluşturuldu.");
        encryptAndDecryptWithKeyLabelPKCS15(keyLabel);
    }

    public void testResetPKCS15PinWithAkisCIF() throws Exception{
        String oldPin = "12345";
        String newPin = "12345678";
        String puk = "12345";
        char[] slotDescriptionChar = sc
                .getSlotInfo(slotNo).slotDescription;
        String mTerminalAdi = new String(slotDescriptionChar).trim();
        CardTerminal initializationTerminal = TerminalFactory.getDefault()
                .terminals().getTerminal(mTerminalAdi);


        Card card = initializationTerminal.connect("*");
        String KART_DIZIN = "3D00";
        APDUPP parametre = new APDUPP(initializationTerminal,card,KART_DIZIN, puk.getBytes("ASCII"));
        {
            KartParolaDegistirici kartParolaDegistirici = ParolaFabrika.parolaciAl(ParolaDegistirmeYontemi.APDU12USRWITHAKISCIF);
            kartParolaDegistirici.init(parametre);
            System.out.println("Kart pin i " + newPin + " olarak belirlenecek");
            kartParolaDegistirici.pinDegistir(newPin.getBytes());
            System.out.println("Kart pin i " + newPin + " olarak belirlendi");
        }
        {
            KartParolaDegistirici kartParolaDegistirici = ParolaFabrika.parolaciAl(ParolaDegistirmeYontemi.APDU12USRWITHAKISCIF);
            kartParolaDegistirici.init(parametre);
            System.out.println("Kart pin i " + oldPin + " olarak belirlenecek");
            kartParolaDegistirici.pinDegistir(oldPin.getBytes());
            System.out.println("Kart pin i " + oldPin + " olarak belirlendi");
        }
    }

    @Test
    public void testResetMFPinWithAkisCIF() throws Exception{
        String oldPin = "12345";
        String newPin = "12345678";
        String puk = "44444444";
        char[] slotDescriptionChar = sc
                .getSlotInfo(slotNo).slotDescription;
        String mTerminalAdi = new String(slotDescriptionChar).trim();
        CardTerminal initializationTerminal = TerminalFactory.getDefault()
                .terminals().getTerminal(mTerminalAdi);


        Card card = initializationTerminal.connect("*");
        String KART_DIZIN = "3F00";
        APDUPP parametre = new APDUPP(initializationTerminal,card,KART_DIZIN, puk.getBytes("ASCII"));
        {
            KartParolaDegistirici kartParolaDegistirici = ParolaFabrika.parolaciAl(ParolaDegistirmeYontemi.APDU12USRWITHAKISCIF);
            kartParolaDegistirici.init(parametre);
            System.out.println("Kart pin i " + newPin + " olarak belirlenecek");
            kartParolaDegistirici.pinDegistir(newPin.getBytes());
            System.out.println("Kart pin i " + newPin + " olarak belirlendi");
        }
        {
            KartParolaDegistirici kartParolaDegistirici = ParolaFabrika.parolaciAl(ParolaDegistirmeYontemi.APDU12USRWITHAKISCIF);
            kartParolaDegistirici.init(parametre);
            System.out.println("Kart pin i " + oldPin + " olarak belirlenecek");
            kartParolaDegistirici.pinDegistir(oldPin.getBytes());
            System.out.println("Kart pin i " + oldPin + " olarak belirlendi");
        }
    }

    @Test
    public void testResetPKCS15PinWithAkisAPDU() throws Exception{
        String oldPin = "12345";
        String newPin = "12345678";
        String puk = "12345";

        {
            char[] slotDescriptionChar = sc
                    .getSlotInfo(slotNo).slotDescription;
            String mTerminalAdi = new String(slotDescriptionChar).trim();
            CardTerminal initializationTerminal = TerminalFactory.getDefault()
                    .terminals().getTerminal(mTerminalAdi);


            Card card = initializationTerminal.connect("*");
            String KART_DIZIN = "3D00";
            APDUPP parametre = new APDUPP(initializationTerminal,card,KART_DIZIN, puk.getBytes("ASCII"));

            KartParolaDegistirici kartParolaDegistirici = ParolaFabrika.parolaciAl(ParolaDegistirmeYontemi.APDU12USRWITHAKISCIF);
            kartParolaDegistirici.init(parametre);
            System.out.println("Kart pin i " + newPin + " olarak belirlenecek");
            kartParolaDegistirici.pinDegistir(newPin.getBytes());
            System.out.println("Kart pin i " + newPin + " olarak belirlendi");
            card.disconnect(false);
        }
        {
            char[] slotDescriptionChar = sc
                    .getSlotInfo(slotNo).slotDescription;
            String mTerminalAdi = new String(slotDescriptionChar).trim();
            CardTerminal initializationTerminal = TerminalFactory.getDefault()
                    .terminals().getTerminal(mTerminalAdi);


            Card card = initializationTerminal.connect("*");
            String KART_DIZIN = "3D00";
            APDUPP parametre = new APDUPP(initializationTerminal,card,KART_DIZIN, puk.getBytes("ASCII"));

            KartParolaDegistirici kartParolaDegistirici = ParolaFabrika.parolaciAl(ParolaDegistirmeYontemi.APDU12USRWITHAKISCIF);
            kartParolaDegistirici.init(parametre);
            System.out.println("Kart pin i " + oldPin + " olarak belirlenecek");
            kartParolaDegistirici.pinDegistir(oldPin.getBytes());
            System.out.println("Kart pin i " + oldPin + " olarak belirlendi");
            card.disconnect(false);
        }
    }

    @Test
    public void testResetMFPinWithAkisAPDU() throws Exception{
        String oldPin = "12345";
        String newPin = "12345678";
        String puk = "12345";

        {
            char[] slotDescriptionChar = sc
                    .getSlotInfo(slotNo).slotDescription;
            String mTerminalAdi = new String(slotDescriptionChar).trim();
            CardTerminal initializationTerminal = TerminalFactory.getDefault()
                    .terminals().getTerminal(mTerminalAdi);


            Card card = initializationTerminal.connect("*");
            String KART_DIZIN = "3F00";
            APDUPP parametre = new APDUPP(initializationTerminal,card,KART_DIZIN, newPin.getBytes("ASCII"));

            KartParolaDegistirici kartParolaDegistirici = ParolaFabrika.parolaciAl(ParolaDegistirmeYontemi.APDU12SYS);
            kartParolaDegistirici.init(parametre);
            System.out.println("Kart pin i " + newPin + " olarak belirlenecek");
            kartParolaDegistirici.pinDegistir(oldPin.getBytes());
            System.out.println("Kart pin i " + newPin + " olarak belirlendi");
            card.disconnect(false);
        }
        {
            char[] slotDescriptionChar = sc
                    .getSlotInfo(slotNo).slotDescription;
            String mTerminalAdi = new String(slotDescriptionChar).trim();
            CardTerminal initializationTerminal = TerminalFactory.getDefault()
                    .terminals().getTerminal(mTerminalAdi);


            Card card = initializationTerminal.connect("*");
            String KART_DIZIN = "3D00";
            APDUPP parametre = new APDUPP(initializationTerminal,card,KART_DIZIN, puk.getBytes("ASCII"));

            KartParolaDegistirici kartParolaDegistirici = ParolaFabrika.parolaciAl(ParolaDegistirmeYontemi.APDU12SYS);
            kartParolaDegistirici.init(parametre);
            System.out.println("Kart pin i " + oldPin + " olarak belirlenecek");
            kartParolaDegistirici.pinDegistir(oldPin.getBytes());
            System.out.println("Kart pin i " + oldPin + " olarak belirlendi");
            card.disconnect(false);
        }
    }

    @Test
    public void testChangePKCS15PukWithAkisCIF() throws Exception{
        String oldPuk = "12345";
        String newPuk = "12345678";
        String puk = "12345";
        char[] slotDescriptionChar = sc
                .getSlotInfo(slotNo).slotDescription;
        String mTerminalAdi = new String(slotDescriptionChar).trim();
        CardTerminal initializationTerminal = TerminalFactory.getDefault()
                .terminals().getTerminal(mTerminalAdi);


        Card card = initializationTerminal.connect("*");
        String KART_DIZIN = "3D00";
        {
            APDUPP parametre = new APDUPP(initializationTerminal,card,KART_DIZIN, puk.getBytes("ASCII"));
            KartParolaDegistirici kartParolaDegistirici = ParolaFabrika.parolaciAl(ParolaDegistirmeYontemi.APDU12USRWITHAKISCIF);
            kartParolaDegistirici.init(parametre);
            System.out.println("Kart puk u " + newPuk + " olarak belirlenecek");
            kartParolaDegistirici.pukDegistir(newPuk.getBytes());
            System.out.println("Kart puk u " + newPuk + " olarak belirlendi");
        }
        {
            APDUPP parametre = new APDUPP(initializationTerminal,card,KART_DIZIN, newPuk.getBytes("ASCII"));
            KartParolaDegistirici kartParolaDegistirici = ParolaFabrika.parolaciAl(ParolaDegistirmeYontemi.APDU12USRWITHAKISCIF);
            kartParolaDegistirici.init(parametre);

            System.out.println("Kart puk u " + oldPuk + " olarak belirlenecek");
            kartParolaDegistirici.pukDegistir(oldPuk.getBytes());
            System.out.println("Kart puk u " + oldPuk + " olarak belirlendi");
        }
    }

    @Test
    public void testChangePKCS15PukWithAPDU() throws Exception{
        String oldPuk = "12345";
        String newPuk = "12345678";
        String puk = "12345";

        String KART_DIZIN = "3D00";
        {
            char[] slotDescriptionChar = sc
                    .getSlotInfo(slotNo).slotDescription;
            String mTerminalAdi = new String(slotDescriptionChar).trim();
            CardTerminal initializationTerminal = TerminalFactory.getDefault()
                    .terminals().getTerminal(mTerminalAdi);


            Card card = initializationTerminal.connect("*");
            APDUPP parametre = new APDUPP(initializationTerminal,card,KART_DIZIN, puk.getBytes("ASCII"));
            KartParolaDegistirici kartParolaDegistirici = ParolaFabrika.parolaciAl(ParolaDegistirmeYontemi.APDU12USR);
            kartParolaDegistirici.init(parametre);
            System.out.println("Kart puk u " + newPuk + " olarak belirlenecek");
            kartParolaDegistirici.pukDegistir(newPuk.getBytes());
            System.out.println("Kart puk u " + newPuk + " olarak belirlendi");
            card.disconnect(false);
        }
        {
            char[] slotDescriptionChar = sc
                    .getSlotInfo(slotNo).slotDescription;
            String mTerminalAdi = new String(slotDescriptionChar).trim();
            CardTerminal initializationTerminal = TerminalFactory.getDefault()
                    .terminals().getTerminal(mTerminalAdi);


            Card card = initializationTerminal.connect("*");
            APDUPP parametre = new APDUPP(initializationTerminal,card,KART_DIZIN, puk.getBytes("ASCII"));
            KartParolaDegistirici kartParolaDegistirici = ParolaFabrika.parolaciAl(ParolaDegistirmeYontemi.APDU12USR);
            kartParolaDegistirici.init(parametre);

            System.out.println("Kart puk u " + oldPuk + " olarak belirlenecek");
            kartParolaDegistirici.pukDegistir(oldPuk.getBytes());
            System.out.println("Kart puk u " + oldPuk + " olarak belirlendi");
            card.disconnect(false);
        }
    }


    public void testImportPfx() throws PKCS11Exception, ESYAException, IOException {
        System.out.println("Pfx import edilecek");
        List<String> importedKeyLabelList = importKeyAndCertificate(2, false, true, true);
        System.out.println("Pfx import edildi.");
        Assert.assertTrue(importedKeyLabelList.size() == 2);
        String firstKeyLabel = importedKeyLabelList.get(0);
        String secondKeyLabel = importedKeyLabelList.get(1);
        String[] encryptionKeyLabels = sc.getEncryptionKeyLabels(sessionId);
        String[] signatureKeyLabels = sc.getSignatureKeyLabels(sessionId);
        String signKeyLabelSc = null;
        String encKeyLabelSc = null;
        for (String signatureKeyLabel : signatureKeyLabels) {
            if(signatureKeyLabel.equals(firstKeyLabel)){
                signKeyLabelSc = signatureKeyLabel;
                break;
            }
            else if(signatureKeyLabel.equals(secondKeyLabel)){
                signKeyLabelSc = signatureKeyLabel;
                break;
            }
        }
        for (String encKeyLabel : encryptionKeyLabels) {
            if(encKeyLabel.equals(firstKeyLabel)){
                encKeyLabelSc = encKeyLabel;
                break;
            }
            else if(encKeyLabel.equals(secondKeyLabel)){
                encKeyLabelSc = encKeyLabel;
                break;
            }
        }
        Assert.assertTrue(encKeyLabelSc != null);
        Assert.assertTrue(signKeyLabelSc != null);

        signAndVerifyWithKeyLabelPKCS15(signKeyLabelSc);
        encryptAndDecryptWithKeyLabelPKCS15(encKeyLabelSc);
    }

    private List<String> importKeyAndCertificate(int count,boolean addKeysAndCertsToDeleteList,boolean importCertificate,boolean importKey) throws CryptoException, IOException, PKCS11Exception, SmartCardException {
        List<String> importedKeyLabelList = new ArrayList<String>();
        int importedCount = 0;
        while (importedCount<count) {
            PfxParser pfxParser = new PfxParser(new FileInputStream("C:/tmp/ramazan.girgin_327147@ug.net.pfx"), "327147".toCharArray());
            List<Pair<ECertificate, PrivateKey>> certificatesAndKeys = pfxParser.getCertificatesAndKeys();
            for (Pair<ECertificate, PrivateKey> certificateAndKey : certificatesAndKeys) {
                ECertificate certificate = certificateAndKey.getObject1();
                PrivateKey privateKey = certificateAndKey.getObject2();
                PublicKey publicKey = KeyUtil.decodePublicKey(certificate.getSubjectPublicKeyInfo());
                KeyPair keyPair = new KeyPair(publicKey, privateKey);
                String keyLabel = "key-" + System.currentTimeMillis();
                EKeyUsage keyUsage = certificate.getExtensions().getKeyUsage();
                boolean digitalSignature = keyUsage.isDigitalSignature();
                if(importCertificate && importKey) {
                    sc.importCertificateAndKey(sessionId, keyLabel, keyLabel, privateKey, certificate.asX509Certificate());
                    System.out.println(importedCount+1+". sertifika ve anahtar import edildi.");
                }
                else if(importKey){
                    sc.importKeyPair(sessionId,keyLabel,keyPair,certificate.getSubject().getEncoded(),digitalSignature,!digitalSignature);
                    System.out.println(importedCount + 1 + ". anahtar import edildi.");
                }else if(importCertificate){
                    sc.importCertificate(sessionId,keyLabel,certificate.asX509Certificate());
                    System.out.println(importedCount + 1 + ". sertifika import edildi.");
                }
                importedKeyLabelList.add(keyLabel);
                if(addKeysAndCertsToDeleteList)
                    deleteLabelList.add(keyLabel);
                importedCount++;
                if (importedCount >= count)
                    return importedKeyLabelList;
            }
        }
        return importedKeyLabelList;
    }

   /* @Test
    public void testDeleteCertificateWithoutLogin() throws PKCS11Exception, SmartCardException {
        String[] signKeyLabels = sc.getSignatureKeyLabels(sessionId);
        sc.logout(sessionId);
        try {
            sc.deleteCertificate(sessionId, signKeyLabels[0]);
        }catch (Exception exc) {
            Assert.assertTrue(exc!=null);
        }finally
        {
            try {
                sc.login(sessionId, slotPin);
            }catch (Exception exc){

            }
        }
    }*/
}
