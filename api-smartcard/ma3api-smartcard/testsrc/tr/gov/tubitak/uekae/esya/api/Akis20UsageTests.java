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
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;

import javax.crypto.spec.OAEPParameterSpec;
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
public class Akis20UsageTests extends BaseCardTest {

    @BeforeClass
    public static void setUp() throws Exception {
        BaseCardTest.setUp();
    }

    @Override
    public void testMethodSetUp() {
        super.testMethodSetUp();
    }

    @Test
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
            if (signatureKeyLabel.equals(firstKeyLabel)) {
                signKeyLabelSc = signatureKeyLabel;
                break;
            } else if (signatureKeyLabel.equals(secondKeyLabel)) {
                signKeyLabelSc = signatureKeyLabel;
                break;
            }
        }
        for (String encKeyLabel : encryptionKeyLabels) {
            if (encKeyLabel.equals(firstKeyLabel)) {
                encKeyLabelSc = encKeyLabel;
                break;
            } else if (encKeyLabel.equals(secondKeyLabel)) {
                encKeyLabelSc = encKeyLabel;
                break;
            }
        }
        Assert.assertTrue(encKeyLabelSc != null);
        Assert.assertTrue(signKeyLabelSc != null);

        signAndVerifyWithKeyLabelPKCS15(signKeyLabelSc);
        encryptAndDecryptWithKeyLabelPKCS15(encKeyLabelSc);
    }

    private List<String> importKeyAndCertificate(int count, boolean addKeysAndCertsToDeleteList, boolean importCertificate, boolean importKey) throws CryptoException, IOException, PKCS11Exception, SmartCardException {
        List<String> importedKeyLabelList = new ArrayList<String>();
        int importedCount = 0;
        while (importedCount < count) {
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
                if (importCertificate && importKey) {
                    sc.importCertificateAndKey(sessionId, keyLabel, keyLabel, privateKey, certificate.asX509Certificate());
                    System.out.println(importedCount + 1 + ". sertifika ve anahtar import edildi.");
                } else if (importKey) {
                    sc.importKeyPair(sessionId, keyLabel, keyPair, certificate.getSubject().getEncoded(), digitalSignature, !digitalSignature);
                    System.out.println(importedCount + 1 + ". anahtar import edildi.");
                } else if (importCertificate) {
                    sc.importCertificate(sessionId, keyLabel, certificate.asX509Certificate());
                    System.out.println(importedCount + 1 + ". sertifika import edildi.");
                }
                importedKeyLabelList.add(keyLabel);
                if (addKeysAndCertsToDeleteList)
                    deleteLabelList.add(keyLabel);
                importedCount++;
                if (importedCount >= count)
                    return importedKeyLabelList;
            }
        }
        return importedKeyLabelList;
    }

    @Test
    public void testDeleteCertificateWithoutLogin() throws PKCS11Exception, SmartCardException {
        String[] signKeyLabels = sc.getSignatureKeyLabels(sessionId);
        sc.logout(sessionId);
        sc.deleteCertificate(sessionId, signKeyLabels[0]);
    }

    @Test
    public void testGenerateRandomNumber() throws PKCS11Exception {
        byte[] randomData = sc.getRandomData(sessionId, 2048);
        Assert.assertTrue(randomData.length == 2048);
    }

    @Test
    public void testGenerateSignKeyInSmartCard() throws PKCS11Exception, ESYAException, IOException {
        String keyLabel = "rsaSignKey" + System.currentTimeMillis();
        RSAKeyGenParameterSpec rsaKeyGenParameterSpec = new RSAKeyGenParameterSpec(2048, null);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel, rsaKeyGenParameterSpec);
        rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);

        System.out.println("Kartta imzalama anahtarı oluşturulacak. Label :" + keyLabel);
        KeySpec keyPair = sc.createKeyPair(sessionId, rsaKeyPairTemplate);
        System.out.println("Kartta imzalama anahtarı oluşturuldu.");
        signAndVerifyWithKeyLabelPKCS15(keyLabel);
    }

    @Test
    public void testGenerateEncKeyInSmartCard() throws PKCS11Exception, ESYAException, IOException {
        String keyLabel = "rsaEncKey" + System.currentTimeMillis();
        RSAKeyGenParameterSpec rsaKeyGenParameterSpec = new RSAKeyGenParameterSpec(2048, null);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel, rsaKeyGenParameterSpec);
        rsaKeyPairTemplate.getAsTokenTemplate(false, true, false);

        System.out.println("Kartta şifreleme anahtarı oluşturulacak. Label :" + keyLabel);
        KeySpec keyPair = sc.createKeyPair(sessionId, rsaKeyPairTemplate);
        System.out.println("Kartta şifreleme anahtarı oluşturuldu.");
        encryptAndDecryptWithKeyLabelPKCS15(keyLabel);
    }

    private void signAndVerifyWithKeyLabelPKCS15(String signKeyLabel) throws PKCS11Exception, SmartCardException, CryptoException {
        KeySpec keySpec = sc.readPublicKeySpec(sessionId, signKeyLabel);
        PublicKey publicKey = KeyUtil.generatePublicKey(keySpec);
        signAndVerifyWithKeyLabelPKCS15(signKeyLabel, publicKey);
    }

    private void signAndVerifyWithKeyLabelPKCS15(String signKeyLabel, PublicKey publicKey) throws PKCS11Exception, SmartCardException, CryptoException {

        byte[] dataForSign = "Data For Sign".getBytes();
        System.out.println("Kartta İmzalama işlemi yapılacak.Label :" + signKeyLabel);
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

    private void encryptAndDecryptWithKeyLabelPKCS15(String encryptionKeyLabel, PublicKey publicKey) throws PKCS11Exception, ESYAException, IOException {
        byte[] dataForEnc = "Data For Enc".getBytes();
        System.out.println("(PKCS15)Bellekte Şifreleme işlemi yapılacak.Key Label :" + encryptionKeyLabel);
        byte[] encryptedData = CipherUtil.encrypt(CipherAlg.RSA_PKCS1, null, dataForEnc, publicKey);
        System.out.println("(PKCS15)Bellekte Şifreleme işlemi yapıldı.");
        System.out.println("(PKCS15)Kartta Şifre Çözme işlemi yapılacak.");
        byte[] decryptedData = SmartOp.decrypt(sc, sessionId, slotNo, encryptionKeyLabel, encryptedData, CipherAlg.RSA_PKCS1.getName(), null);
        System.out.println("(PKCS15)Kartla Şifre Çözme işlemi yapıldı.");
        boolean isEqual = Arrays.equals(decryptedData, dataForEnc);
        Assert.assertTrue("(PKCS15)Orijinal data and decrypted data must be equal", isEqual);
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
    public void testSignWithSmartCardAndVerifyWithMemoryPSS() throws PKCS11Exception, ESYAException {
        String[] signatureKeyLabels = sc.getSignatureKeyLabels(sessionId);
        String signKeyLabel = signatureKeyLabels[0];

        List<byte[]> signCerts = sc.readCertificate(sessionId, signKeyLabel);
        byte[] signCertByte = signCerts.get(0);
        ECertificate signECert = new ECertificate(signCertByte);

        byte[] dataForSign = "Data For Sign".getBytes();
        System.out.println("Kartta İmzalama işlemi yapılacak.");
        RSAPSSParams rsapssParams = new RSAPSSParams(DigestAlg.SHA256, MGF.MGF1, 32, 0);
        byte[] signature = SmartOp.sign(sc, sessionId, slotNo, signKeyLabel, dataForSign, SignatureAlg.RSA_PSS.getName(), rsapssParams.toPSSParameterSpec());
        System.out.println("Kartta İmzalama işlemi yapıldı.");
        System.out.println("Bellekte imza doğrulama işlemi yapılacak.");
        PublicKey publicKey = KeyUtil.decodePublicKey(signECert.getSubjectPublicKeyInfo());
        boolean verified = SignUtil.verify(SignatureAlg.RSA_PSS, rsapssParams, dataForSign, signature, publicKey);
        Assert.assertTrue("Imzalanan veri doğrulanamadı.", verified);
        System.out.println("Bellekte imza doğrulama işlemi yapıldı.");
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
    public void testEncryptWithMemoryAndDecryptWithSmartCardOAEP() throws PKCS11Exception, ESYAException, IOException {
        String[] encryptionKeyLabels = sc.getEncryptionKeyLabels(sessionId);
        String encryptionKeyLabel = encryptionKeyLabels[0];

        List<byte[]> encKeyLabels = sc.readCertificate(sessionId, encryptionKeyLabel);
        byte[] encCertByte = encKeyLabels.get(0);
        ECertificate encECert = new ECertificate(encCertByte);

        byte[] dataForEnc = "Data For Enc".getBytes();
        OAEPParameterSpec oaepParameterSpec = OAEPParameterSpec.DEFAULT;
        RSAPSSParams rsapssParams = new RSAPSSParams();
        System.out.println("(OAEP)Bellekte Şifreleme işlemi yapılacak.");
        byte[] encryptedData = CipherUtil.encrypt(CipherAlg.RSA_OAEP, rsapssParams, dataForEnc, encECert);
        System.out.println("(OAEP)Bellekte Şifreleme işlemi yapıldı.");
        System.out.println("(OAEP)Kartta Şifre Çözme işlemi yapılacak.");
        byte[] decryptedData = SmartOp.decrypt(sc, sessionId, slotNo, encryptionKeyLabel, encryptedData, CipherAlg.RSA_OAEP.getName(), oaepParameterSpec);
        System.out.println("(OAEP)Kartla Şifre Çözme işlemi yapıldı.");
        boolean isEqual = Arrays.equals(decryptedData, dataForEnc);
        Assert.assertTrue("(OAEP)Orijinal data and decrypted data must be equal", isEqual);
    }

/*    public void testResetChangePin_ChangePuk() throws Exception {
        testResetPinWithAkisCIF();
        testChangePukWithAkisCIF();
        testChangePinWithPkcs11();
    }*/


/*
    @Test
    public void testResetPinWithAkisCIF() throws Exception{
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
            KartParolaDegistirici kartParolaDegistirici = ParolaFabrika.parolaciAl(ParolaDegistirmeYontemi.APDU20USR);
            kartParolaDegistirici.init(parametre);
            System.out.println("Kart pin i " + newPin + " olarak belirlenecek");
            kartParolaDegistirici.pinDegistir(newPin.getBytes());
            System.out.println("Kart pin i " + newPin + " olarak belirlendi");
        }
        {
            KartParolaDegistirici kartParolaDegistirici = ParolaFabrika.parolaciAl(ParolaDegistirmeYontemi.APDU20USR);
            kartParolaDegistirici.init(parametre);
            System.out.println("Kart pin i " + oldPin + " olarak belirlenecek");
            kartParolaDegistirici.pinDegistir(oldPin.getBytes());
            System.out.println("Kart pin i " + oldPin + " olarak belirlendi");
        }
    }*/


    @Test
    public void testChangePinWithPkcs11() throws Exception {
        String oldPin = "12345";
        String newPin = "12345678";
        System.out.println("Kart pin i " + newPin + " olarak değiştirilecek(PKCS11 changePassword ile)");
        sc.changePassword(oldPin, newPin, sessionId);
        System.out.println("Kart pin i " + newPin + " olarak degistirildi(PKCS11 changePassword ile)");

        System.out.println("Kart pin i " + newPin + " olarak değiştirilecek(PKCS11 changePassword ile)");
        sc.changePassword(newPin, oldPin, sessionId);
        System.out.println("Kart pin i " + oldPin + " olarak degistirildi(PKCS11 changePassword ile)");
    }

    @Test
    public void testResetPinWithPkcs11() throws Exception {
        String puk = "12345";
        String oldPin = "12345";
        String newPin = "12345678";
        System.out.println("Kart pin i " + newPin + " olarak resetlenecek(PKCS11 changeUserPin ile)");
        sc.changeUserPin(puk.getBytes(), newPin.getBytes(), sessionId);
        System.out.println("Kart pin i " + newPin + " olarak resetlendi(PKCS11 changeUserPin ile)");

        System.out.println("Kart pin i " + oldPin + " olarak resetlenecek(PKCS11 changeUserPin ile)");
        sc.changeUserPin(puk.getBytes(), oldPin.getBytes(), sessionId);
        System.out.println("Kart pin i " + oldPin + " olarak resetlendi(PKCS11 changeUserPin ile)");
    }
}
    /*
    @Test
    public void testChangePukWithAkisCIF() throws Exception{
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
}*/
