package tr.gov.tubitak.uekae.esya.api;

import gnu.crypto.key.ecdsa.ECDSAPrivateKey;
import gnu.crypto.key.ecdsa.ECDSAPublicKey;
import org.junit.*;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EKeyUsage;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithECParameterSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.PfxParser;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.KeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPrivateKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import util.CustomCurve;
import util.CustomECDSAPrivateKey;
import util.CustomECDSAPublicKey;
import util.EcAsHexHolder;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by ramazan.girgin on 12/8/2015.
 */
public class GemaltoECKeyTemplateTest {
    static SmartCard sc = null;
    static long sessionId = -1;
    static long slotNo = 1;
    static CardType cardType = CardType.GEMPLUS;
    static String slotPin = "1234";
    static SmartCardHelper smartCardHelper;
    List<String> deleteLabelList=new ArrayList<String>();


    @BeforeClass
    public static void setUp() throws PKCS11Exception, IOException {
        if (smartCardHelper == null) {
            smartCardHelper = new SmartCardHelper().login();
            sc = smartCardHelper.getSc();
            sessionId = smartCardHelper.getSessionId();
            slotNo = smartCardHelper.getSlotNo();
        }
    }

    @AfterClass
    public static void cleanUp() {
        if (sessionId != -1 && sc != null) {
            try {
                sc.closeSession(sessionId);
                System.out.println("Kartta oturum kapatıldı");
            } catch (PKCS11Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Before
    public void testMethodSetUp(){
        System.out.println("******************* TEST  BASLANGICI ****************");
    }

   /*@After
    public void testMethodCleanUp(){
        for (String objectLabel : deleteLabelList) {
            smartCardHelper.deleteAllObjectWithLabel(objectLabel);
        }
        System.out.println("-------------------------- TEST  SONU ----------------------");
    }*/

    @Test
    public void testGeneratRSASignKeyInSmartCard() throws CryptoException, IOException, SmartCardException, PKCS11Exception {
        String keyLabel = "signRSAKey-" + System.currentTimeMillis();
        RSAKeyGenParameterSpec rsaKeyGenParameterSpec = new RSAKeyGenParameterSpec(2048,null);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel,rsaKeyGenParameterSpec);
        rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);

        System.out.println(keyLabel + " etiketli rsa imzalama anahtarı kart içerisinde oluşturulacak.");
        sc.createKeyPair(sessionId, rsaKeyPairTemplate);
        System.out.println(keyLabel + " etiketli rsa imzalama anahtarı kart içerisinde oluşturuldu.");
        deleteLabelList.add(keyLabel);
        verifySignKey(keyLabel);
    }

    @Test
    public void testGeneratRSAEncKeyInSmartCard() throws CryptoException, IOException, SmartCardException, PKCS11Exception {
        String keyLabel = "encRSAKey-" + System.currentTimeMillis();
        RSAKeyGenParameterSpec rsaKeyGenParameterSpec = new RSAKeyGenParameterSpec(2048,null);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel,rsaKeyGenParameterSpec);
        rsaKeyPairTemplate.getAsTokenTemplate(false, true, false);

        System.out.println(keyLabel + " etiketli rsa şifreleme anahtarı kart içerisinde oluşturulacak.");
        sc.createKeyPair(sessionId, rsaKeyPairTemplate);
        System.out.println(keyLabel + " etiketli rsa şifreleme anahtarı kart içerisinde oluşturuldu.");
        deleteLabelList.add(keyLabel);
        verifyEncKey(keyLabel);
    }

    @Test
    public void testGeneratRSAEncWrapKeyInSmartCard() throws CryptoException, IOException, SmartCardException, PKCS11Exception {
        String keyLabel = "encRSAKey-" + System.currentTimeMillis();
        RSAKeyGenParameterSpec rsaKeyGenParameterSpec = new RSAKeyGenParameterSpec(2048,null);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel,rsaKeyGenParameterSpec);
        rsaKeyPairTemplate.getAsTokenTemplate(false, true, true);

        System.out.println(keyLabel + " etiketli rsa şifreleme/wrap anahtarı kart içerisinde oluşturulacak.");
        sc.createKeyPair(sessionId, rsaKeyPairTemplate);
        System.out.println(keyLabel + " etiketli rsa şifreleme/wrap anahtarı kart içerisinde oluşturuldu.");
        deleteLabelList.add(keyLabel);

        verifyEncKey(keyLabel);

        verifyWrapKey(keyLabel);
    }

    private void verifyEncKey(String keyLabel) throws PKCS11Exception, SmartCardException {
        System.out.println(keyLabel + " etiketli rsa şifreleme anahtarı ile test şifreleme yapılacak.");
        new EncKeyVerifier().encryptAndDecrypt(keyLabel, slotNo, sc, sessionId, CipherAlg.RSA_PKCS1);
        System.out.println(keyLabel + " etiketli rsa şifreleme anahtarı ile test şifreleme yapıldı.");
    }


    @Test
    public void tesImportRSASignKeyToSmartCardDirectly() throws CryptoException, IOException, SmartCardException, PKCS11Exception {
        String keyLabel = "signRSAKey-" + System.currentTimeMillis();

        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 2048);
        RSAPublicKeyTemplate rsaPublicKeyTemplate = new RSAPublicKeyTemplate(keyLabel, (RSAPublicKey) keyPair.getPublic());
        RSAPrivateKeyTemplate rsaPrivateKeyTemplate = new RSAPrivateKeyTemplate(keyLabel, (RSAPrivateCrtKey)keyPair.getPrivate(),null);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(rsaPublicKeyTemplate,rsaPrivateKeyTemplate);
        rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);

        System.out.println(keyLabel + " etiketli rsa imzalama anahtarı kart içerisine doğrudan import edilecek.");
        sc.importKeyPair(sessionId, rsaKeyPairTemplate);
        System.out.println(keyLabel + " etiketli rsa imzalama anahtarı kart içerisine doğrudan import edidi.");
        deleteLabelList.add(keyLabel);
        verifySignKey(keyLabel);
    }

    @Test
    public void tesImportRSAEncKeyToSmartCardDirectly() throws CryptoException, IOException, SmartCardException, PKCS11Exception {
        String keyLabel = "encRSAKey-" + System.currentTimeMillis();

        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 2048);
        RSAPublicKeyTemplate rsaPublicKeyTemplate = new RSAPublicKeyTemplate(keyLabel, (RSAPublicKey) keyPair.getPublic());
        RSAPrivateKeyTemplate rsaPrivateKeyTemplate = new RSAPrivateKeyTemplate(keyLabel, (RSAPrivateCrtKey)keyPair.getPrivate(),null);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(rsaPublicKeyTemplate,rsaPrivateKeyTemplate);
        rsaKeyPairTemplate.getAsTokenTemplate(false, true, false);

        System.out.println(keyLabel + " etiketli rsa şifreleme anahtarı kart içerisine doğrudan import edilecek.");
        sc.importKeyPair(sessionId, rsaKeyPairTemplate);
        System.out.println(keyLabel + " etiketli rsa şifreleme anahtarı kart içerisine doğrudan import edildi.");
        deleteLabelList.add(keyLabel);

        verifyEncKey(keyLabel);
    }

    @Test
    public void tesImportRSAWrapEncKeyToSmartCardDirectly() throws CryptoException, IOException, SmartCardException, PKCS11Exception {
        String keyLabel = "encWrapRSAKey-" + System.currentTimeMillis();

        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 2048);
        RSAPublicKeyTemplate rsaPublicKeyTemplate = new RSAPublicKeyTemplate(keyLabel, (RSAPublicKey) keyPair.getPublic());
        RSAPrivateKeyTemplate rsaPrivateKeyTemplate = new RSAPrivateKeyTemplate(keyLabel, (RSAPrivateCrtKey)keyPair.getPrivate(),null);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(rsaPublicKeyTemplate,rsaPrivateKeyTemplate);
        rsaKeyPairTemplate.getAsTokenTemplate(false, true, true);

        System.out.println(keyLabel + " etiketli rsa şifreleme/wrap anahtarı kart içerisine doğrudan import edilecek.");
        sc.importKeyPair(sessionId, rsaKeyPairTemplate);
        System.out.println(keyLabel + " etiketli rsa şifreleme/wrap anahtarı kart içerisine doğrudan import edildi.");
        deleteLabelList.add(keyLabel);

        verifyEncKey(keyLabel);
        verifyWrapKey(keyLabel);
    }

    private void verifyWrapKey(String keyLabel) throws PKCS11Exception, SmartCardException {
        System.out.println(keyLabel + " etiketli rsa şifreleme anahtarı ile wrap/unwrap işlemi yapılacak.");
        new WrapKeyVerifier().wrapAndUnwrap(keyLabel, slotNo, sc, sessionId, WrapAlg.RSA_PKCS1.RSA_PKCS1);
        System.out.println(keyLabel + " etiketli rsa şifreleme anahtarı ile wrap/unwrap işlemi yapıldı.");
    }

    @Test
    public void tesImportRSASignKeyToSmartCardWithWrapUnwrap() throws CryptoException, IOException, SmartCardException, PKCS11Exception {
        String keyLabel = "signRSAKey-" + System.currentTimeMillis();

        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 2048);
        RSAPublicKeyTemplate rsaPublicKeyTemplate = new RSAPublicKeyTemplate(keyLabel, (RSAPublicKey) keyPair.getPublic());
        RSAPrivateKeyTemplate rsaPrivateKeyTemplate = new RSAPrivateKeyTemplate(keyLabel, (RSAPrivateCrtKey)keyPair.getPrivate(),null);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(rsaPublicKeyTemplate,rsaPrivateKeyTemplate);
        rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);

        System.out.println(keyLabel + " etiketli rsa imzalama anahtarı kart içerisine wrap/unwrap ile  import edilecek.");

        new KeyImporterWithWrapUnwrap().importKeyPair(sc, sessionId, keyPair, rsaKeyPairTemplate);
        deleteLabelList.add(keyLabel);
        System.out.println(keyLabel + " etiketli rsa imzalama anahtarı kart içerisine wrap/unwrap ile import edidi.");

        verifySignKey(keyLabel);
    }

    @Test
    public void tesImportRSAEncKeyToSmartCardWithWrapUnwrap() throws CryptoException, IOException, SmartCardException, PKCS11Exception {
        String keyLabel = "encRSAKey-" + System.currentTimeMillis();

        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 2048);
        RSAPublicKeyTemplate rsaPublicKeyTemplate = new RSAPublicKeyTemplate(keyLabel, (RSAPublicKey) keyPair.getPublic());
        RSAPrivateKeyTemplate rsaPrivateKeyTemplate = new RSAPrivateKeyTemplate(keyLabel, (RSAPrivateCrtKey)keyPair.getPrivate(),null);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(rsaPublicKeyTemplate,rsaPrivateKeyTemplate);
        rsaKeyPairTemplate.getAsTokenTemplate(false, true, false);

        System.out.println(keyLabel + " etiketli rsa şifreleme anahtarı kart içerisine wrap/unwrap ile  import edilecek.");

        new KeyImporterWithWrapUnwrap().importKeyPair(sc, sessionId, keyPair, rsaKeyPairTemplate);
        deleteLabelList.add(keyLabel);
        System.out.println(keyLabel + " etiketli rsa şifreleme anahtarı kart içerisine wrap/unwrap ile import edidi.");

        verifyEncKey(keyLabel);
    }

    @Test
    public void tesImportRSAEncWrapKeyToSmartCardWithWrapUnwrap() throws CryptoException, IOException, SmartCardException, PKCS11Exception {
        String keyLabel = "encWrapRSAKey-" + System.currentTimeMillis();

        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 2048);
        RSAPublicKeyTemplate rsaPublicKeyTemplate = new RSAPublicKeyTemplate(keyLabel, (RSAPublicKey) keyPair.getPublic());
        RSAPrivateKeyTemplate rsaPrivateKeyTemplate = new RSAPrivateKeyTemplate(keyLabel, (RSAPrivateCrtKey)keyPair.getPrivate(),null);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(rsaPublicKeyTemplate,rsaPrivateKeyTemplate);
        rsaKeyPairTemplate.getAsTokenTemplate(false, true, true);

        System.out.println(keyLabel + " etiketli rsa şifreleme/wrap anahtarı kart içerisine wrap/unwrap ile  import edilecek.");

        new KeyImporterWithWrapUnwrap().importKeyPair(sc, sessionId, keyPair, rsaKeyPairTemplate);
        deleteLabelList.add(keyLabel);
        System.out.println(keyLabel + " etiketli rsa şifreleme/wrap anahtarı kart içerisine wrap/unwrap ile import edidi.");

        verifyEncKey(keyLabel);
        verifyWrapKey(keyLabel);
    }

    private void verifySignKey(String keyLabel) throws PKCS11Exception, SmartCardException {
        System.out.println(keyLabel + " etiketli rsa imzalama anahtarı ile test imza atılacak.");
        new SignKeyVerifier().verifySignKeyPair(keyLabel, slotNo, sc, sessionId, SignatureAlg.RSA_SHA256);
        System.out.println(keyLabel + " etiketli rsa imzalama anahtarı ile test imza atılıp, doğrulandı.");
    }


    @Test
    public void testGeneratNIST256SignKeyInSmartCard() throws CryptoException, IOException, SmartCardException, PKCS11Exception {
        ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec("secp384r1");

        String keyLabel = "ecKey" + System.currentTimeMillis();
        ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate(keyLabel, ecParameterSpec);
        ecKeyPairTemplate.getAsTokenTemplate(true, false, false);

        sc.createKeyPair(sessionId, ecKeyPairTemplate);
        deleteLabelList.add(keyLabel);
        new SignKeyVerifier().verifySignKeyPair(keyLabel, slotNo, sc, sessionId);
    }


    @Test
    public void testGeneratNIST384SignKeyInSmartCard() throws CryptoException, IOException, SmartCardException, PKCS11Exception {
        ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec("secp384r1");

        String keyLabel = "ecNIST384GeneratedInSmartCardKey" + System.currentTimeMillis();
        ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate(keyLabel, ecParameterSpec);
        ecKeyPairTemplate.getAsTokenTemplate(true,false,false);

        sc.createKeyPair(sessionId, ecKeyPairTemplate);
        deleteLabelList.add(keyLabel);
        new SignKeyVerifier().verifySignKeyPair(keyLabel, slotNo, sc, sessionId);
    }

    @Test
    public void testGeneratNIST384DeriveWrapKeyInSmartCard() throws CryptoException, IOException, SmartCardException, PKCS11Exception {
        ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec("secp384r1");

        String keyLabel = "ecKey" + System.currentTimeMillis();
        ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate(keyLabel, ecParameterSpec);
        ecKeyPairTemplate.getAsTokenTemplate(false, true, true);

        sc.createKeyPair(sessionId, ecKeyPairTemplate);
        deleteLabelList.add(keyLabel);
    }

    @Test
    public void testGenerateCustomECSignKeyInSmartCard() throws CryptoException, IOException, SmartCardException, PKCS11Exception, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, InvalidKeyException, ClassNotFoundException {

        CustomCurve ecParameterSpec = generateCustomECParams();
        String keyLabel = "ecKey" + System.currentTimeMillis();
        ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate(keyLabel,ecParameterSpec);
        ecKeyPairTemplate.getAsTokenTemplate(true, false, false);

        sc.createKeyPair(sessionId, ecKeyPairTemplate);
        deleteLabelList.add(keyLabel);
        new SignKeyVerifier().verifySignKeyPair(keyLabel, slotNo, sc, sessionId);
    }

    @Test
    public void testImportNIST384SignKeyToSmartCardDirectly() throws CryptoException, IOException, SmartCardException, PKCS11Exception, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String secp384r1 = "secp384r1";
        ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec(secp384r1);

        NamedCurve namedCurve=null;
        Map<String, NamedCurve> nameCurves = NamedCurve.getNameCurves();
        for (String curveName : nameCurves.keySet()) {
            if(curveName.equals(secp384r1)){
                namedCurve =nameCurves.get(curveName);
            }
        }

        ParamsWithECParameterSpec keygenparams = new ParamsWithECParameterSpec(ecParameterSpec);
        KeyPair keyPair = Crypto.getKeyPairGenerator(AsymmetricAlg.ECDSA).generateKeyPair(keygenparams);
        CustomECDSAPublicKey customECDSAPublicKey = new CustomECDSAPublicKey((ECDSAPublicKey) keyPair.getPublic(),namedCurve);
        CustomECDSAPrivateKey customECDSAPrivateKey = new CustomECDSAPrivateKey((ECDSAPrivateKey) keyPair.getPrivate(), namedCurve);
        keyPair = new KeyPair(customECDSAPublicKey, customECDSAPrivateKey);

        String keyLabel = "ecKey" + System.currentTimeMillis();
        ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate(keyLabel, ecParameterSpec, keyPair.getPrivate(), keyPair.getPublic());
        ecKeyPairTemplate.getAsTokenTemplate(true, false, false);

        sc.importKeyPair(sessionId, ecKeyPairTemplate);
        deleteLabelList.add(keyLabel);
        new SignKeyVerifier().verifySignKeyPair(keyLabel, slotNo, sc, sessionId);
    }

    @Test
    public void testImportEC_NIST384_PfxToSmartCard() throws Exception{
        PfxParser pfxParser = new PfxParser(new FileInputStream("D:\\INDEX\\pfx\\ec\\384\\murat.kubilay#ug.net_306442.pfx"), "306442".toCharArray());
        List<Pair<ECertificate, PrivateKey>> certificatesAndKeys = pfxParser.getCertificatesAndKeys();
        Pair<ECertificate, PrivateKey> certificateAndKey = certificatesAndKeys.get(1);
        ECertificate certificate = certificateAndKey.getObject1();
        PrivateKey privateKey = certificateAndKey.getObject2();
        String keyLabel = "ecPfxKey" + System.currentTimeMillis();
        sc.importCertificateAndKey(sessionId,keyLabel,keyLabel,privateKey,certificate.asX509Certificate());
        deleteLabelList.add(keyLabel);
        new SignKeyVerifier().verifySignKeyPairWithCertificate(certificate, slotNo, sc, sessionId);
    }

    @Test
    public void testImportExistingKeyCert_NIST384_ToSmartCard() throws Exception{
        String certDir = "D:\\INDEX\\cert\\ec\\384";
        File[] fileList = new File(certDir).listFiles();
        for (File file : fileList) {
            if(file.getName().endsWith(".cer")){
                ECertificate eCertificate = new ECertificate(new FileInputStream(file));
                String keyLabel = "ecExistingKeyCert-" +eCertificate.getSubject().toShortTitle();
                sc.importCertificate(sessionId,keyLabel,eCertificate.asX509Certificate());
                new SignKeyVerifier().verifySignKeyPairWithCertificate(eCertificate, slotNo, sc, sessionId);
            }
        }
    }


    @Test
    public void testImportCustomECSignKeyToSmartCard() throws CryptoException, IOException, SmartCardException, PKCS11Exception, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvalidKeyException {

        CustomCurve customCurve = generateCustomECParams();
        ParamsWithECParameterSpec keygenparams = new ParamsWithECParameterSpec(customCurve);
        KeyPair keyPair = Crypto.getKeyPairGenerator(AsymmetricAlg.ECDSA).generateKeyPair(keygenparams);
        CustomECDSAPublicKey customECDSAPublicKey = new CustomECDSAPublicKey((ECDSAPublicKey) keyPair.getPublic(), customCurve);
        CustomECDSAPrivateKey customECDSAPrivateKey = new CustomECDSAPrivateKey((ECDSAPrivateKey) keyPair.getPrivate(), customCurve);
        keyPair = new KeyPair(customECDSAPublicKey, customECDSAPrivateKey);

        String keyLabel = "ecKey" + System.currentTimeMillis();
        ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate(keyLabel, customCurve, keyPair.getPrivate(), keyPair.getPublic());
        ecKeyPairTemplate.getAsTokenTemplate(true, false, false);

        sc.importKeyPair(sessionId, ecKeyPairTemplate);
        deleteLabelList.add(keyLabel);
        new SignKeyVerifier().verifySignKeyPair(keyLabel, slotNo, sc, sessionId);
    }

    @Test
    public void testImportNIST384SignKeyToSmartCardWithWrapUnwrap() throws CryptoException, IOException, SmartCardException, PKCS11Exception, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String secp384r1 = "secp384r1";
        ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec(secp384r1);

        NamedCurve namedCurve=null;
        Map<String, NamedCurve> nameCurves = NamedCurve.getNameCurves();
        for (String curveName : nameCurves.keySet()) {
            if(curveName.equals(secp384r1)){
                namedCurve =nameCurves.get(curveName);
            }
        }

        ParamsWithECParameterSpec keygenparams = new ParamsWithECParameterSpec(ecParameterSpec);
        KeyPair keyPair = Crypto.getKeyPairGenerator(AsymmetricAlg.ECDSA).generateKeyPair(keygenparams);
        CustomECDSAPublicKey customECDSAPublicKey = new CustomECDSAPublicKey((ECDSAPublicKey) keyPair.getPublic(),namedCurve);
        CustomECDSAPrivateKey customECDSAPrivateKey = new CustomECDSAPrivateKey((ECDSAPrivateKey) keyPair.getPrivate(), namedCurve);
        keyPair = new KeyPair(customECDSAPublicKey, customECDSAPrivateKey);

        String keyLabel = "ecKey" + System.currentTimeMillis();
        ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate(keyLabel, ecParameterSpec,customECDSAPrivateKey,customECDSAPublicKey);
        ecKeyPairTemplate.getAsTokenTemplate(true, false, false);

        new KeyImporterWithWrapUnwrap().importKeyPair(sc, sessionId, keyPair, ecKeyPairTemplate);
        deleteLabelList.add(keyLabel);
        new SignKeyVerifier().verifySignKeyPair(keyLabel, slotNo, sc, sessionId);
    }


    private CustomCurve generateCustomECParams() throws CryptoException {
        EcAsHexHolder ecAsHexHolder = new EcAsHexHolder();

        String oidStr = "1.2.840.10045.3.1.55";

        ecAsHexHolder.setCurveType(EcAsHexHolder.Prime);
        ecAsHexHolder.setP("8CB91E82A3386D280F5D6F7E50E641DF152F7109ED5456B412B1DA197FB71123ACD3A729901D1A71874700133107EC53");
        ecAsHexHolder.setA("7BC382C63D8C150C3C72080ACE05AFA0C2BEA28E4FB22787139165EFBA91F90F8AA5814A503AD4EB04A8C7DD22CE2826");
        ecAsHexHolder.setB("4A8C7DD22CE28268B39B55416F0447C2FB77DE107DCD2A62E880EA53EEB62D57CB4390295DBC9943AB78696FA504C11");
        ecAsHexHolder.setGx("1D1C64F068CF45FFA2A63A81B7C13F6B8847A3E77EF14FE3DB7FCAFE0CBD10E8E826E03436D646AAEF87B2E247D4AF1E");
        ecAsHexHolder.setGy("8ABE1D7520F9C2A45CB1EB8E95CFD55262B70B29FEEC5864E19C054FF99129280E4646217791811142820341263C5315");
        ecAsHexHolder.setN("8CB91E82A3386D280F5D6F7E50E641DF152F7109ED5456B31F166E6CAC0425A7CF3AB6AF6B7FC3103B883202E9046565");
        ecAsHexHolder.setH(1);

        CustomCurve customCurve = CustomCurve.createCustomCurve("custom" + System.currentTimeMillis(), oidStr, CustomCurve.P,
                ecAsHexHolder.getP(), ecAsHexHolder.getA(), ecAsHexHolder.getB(), ecAsHexHolder.getGx(),
                ecAsHexHolder.getGy(), ecAsHexHolder.getN(), ecAsHexHolder.getH());

        return customCurve;
    }


    private static class SmartCardHelper {
        private SmartCard sc;
        private long sessionId;

        public SmartCard getSc() {
            return sc;
        }

        public long getSlotNo() {
            return slotNo;
        }

        public long getSessionId() {
            return sessionId;
        }

        public SmartCardHelper login() throws PKCS11Exception, IOException {
            sc = new SmartCard(cardType);
            try {
                sessionId = sc.openSession(slotNo);
                System.out.println(slotNo+" numaralı slotta oturum açıldı.");
                sc.login(sessionId, slotPin);
                System.out.println(slotNo + " numaralı slota login olundu..");
            } catch (PKCS11Exception exc) {
                if (exc.getErrorCode() != PKCS11Constants.CKR_USER_ALREADY_LOGGED_IN) {
                    throw exc;
                }
            }
            return this;
        }

        public void deleteAllObjectWithLabel(String objectLabel){
            try {
                sc.deletePublicObject(sessionId,objectLabel);
                System.out.println(objectLabel+" etiketli public obje karttan silindi");
            } catch (Exception e) {
            }
            try {
                sc.deletePrivateObject(sessionId, objectLabel);
                System.out.println(objectLabel + " etiketli private obje karttan silindi");
            } catch (Exception e) {
            }
            try {
                int deletedCount = sc.deleteCertificate(sessionId, objectLabel);
                if(deletedCount>0)
                    System.out.println(objectLabel + " etiketli sertifika karttan silindi");

            } catch (Exception e) {
            }
            try {
                sc.deletePrivateData(sessionId, objectLabel);
                System.out.println(objectLabel + " etiketli private data karttan silindi");

            } catch (Exception e) {
            }
            try {
                sc.deletePublicData(sessionId, objectLabel);
                System.out.println(objectLabel + " etiketli public data karttan silindi");

            } catch (Exception e) {
            }
        }
    }



    public class SignKeyVerifier{
        private void verifySignKeyPair(String keyLabel, Long slotNo, SmartCard smartCard, long sessionID,SignatureAlg preferredSignAlg) throws PKCS11Exception, SmartCardException {
            byte[] dataForSign = "test".getBytes();
            SignatureAlg[] signatureAlgs = null;
            if(preferredSignAlg == null)
                signatureAlgs = new SignatureAlg[]{SignatureAlg.ECDSA_SHA512, SignatureAlg.ECDSA_SHA384, SignatureAlg.ECDSA_SHA256, SignatureAlg.ECDSA_SHA224, SignatureAlg.ECDSA_SHA1};
            else
                signatureAlgs = new SignatureAlg[]{preferredSignAlg};
            for (SignatureAlg signatureAlg : signatureAlgs) {
                boolean signedAndVerified = signAndVerify(keyLabel, slotNo, smartCard, sessionID, dataForSign, signatureAlg.getName());
                if (signedAndVerified) {
                    System.out.println("Üretilen " + keyLabel + " etiketli anahtar ile " + signatureAlg.getName() + " kullanarak test imza atılıp doğrulandı.");
                    return;
                }
            }
            throw new ESYARuntimeException("Üretilen " + keyLabel + " etiketli imzalama anahtar çifti ile test imzalama/imza doğrulama işlemi yapılamadı.\nLütfen anahtar çiftini silip tekrar oluşturmayı deneyiniz.");
        }

        private void verifySignKeyPair(String keyLabel, Long slotNo, SmartCard smartCard, long sessionID) throws PKCS11Exception, SmartCardException {
            verifySignKeyPair(keyLabel,slotNo,smartCard,sessionID,null);
        }

        private boolean signAndVerify(String keyLabel, Long slotNo, SmartCard smartCard, long sessionID, byte[] dataForSign, String signingAlgStr) throws PKCS11Exception, SmartCardException {
            boolean verified = false;
            try {
                byte[] signedData = SmartOp.sign(smartCard, sessionID, slotNo, keyLabel, dataForSign, signingAlgStr);
                KeySpec keySpec = smartCard.readPublicKeySpec(sessionID, keyLabel);
                PublicKey publicKey = KeyUtil.generatePublicKey(keySpec);
                verified = SignUtil.verify(SignatureAlg.fromName(signingAlgStr),null,dataForSign,signedData,publicKey);
                //verified = SmartOp.verify(smartCard, sessionID, slotNo, keyLabel, dataForSign, signedData, signingAlgStr);
                return verified;
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return verified;
        }

        public void verifySignKeyPairWithCertificate(ECertificate certificate, Long slotNo, SmartCard smartCard, long sessionID) throws PKCS11Exception, SmartCardException {
            verifySignKeyPairTryAlgs(certificate,slotNo,smartCard,sessionID,null);
        }

        private boolean signAndVerify(ECertificate certificate, Long slotNo, SmartCard smartCard, long sessionID, byte[] dataForSign, String signingAlgStr) throws PKCS11Exception, SmartCardException {
            boolean verified = false;
            try {
                byte[] signedData = SmartOp.sign(smartCard, sessionID, slotNo,certificate.getSerialNumber().toByteArray(), dataForSign, signingAlgStr);
                KeySpec keySpec = smartCard.readPublicKeySpec(sessionID, certificate.getSerialNumber().toByteArray());
                PublicKey publicKey = KeyUtil.generatePublicKey(keySpec);
                verified = SignUtil.verify(SignatureAlg.fromName(signingAlgStr),null,dataForSign,signedData,publicKey);
                //verified = SmartOp.verify(smartCard, sessionID, slotNo, keyLabel, dataForSign, signedData, signingAlgStr);
                return verified;
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return verified;
        }

        private void verifySignKeyPairTryAlgs(ECertificate certificate, Long slotNo, SmartCard smartCard, long sessionID,SignatureAlg preferredSignAlg) throws PKCS11Exception, SmartCardException {
            byte[] dataForSign = "test".getBytes();
            SignatureAlg[] signatureAlgs = null;
            if(preferredSignAlg == null)
                signatureAlgs = new SignatureAlg[]{SignatureAlg.ECDSA_SHA512, SignatureAlg.ECDSA_SHA384, SignatureAlg.ECDSA_SHA256, SignatureAlg.ECDSA_SHA224, SignatureAlg.ECDSA_SHA1};
            else
                signatureAlgs = new SignatureAlg[]{preferredSignAlg};
            for (SignatureAlg signatureAlg : signatureAlgs) {
                boolean signedAndVerified = signAndVerify(certificate, slotNo, smartCard, sessionID, dataForSign, signatureAlg.getName());
                if (signedAndVerified) {
                    System.out.println(certificate.getSubject().toShortTitle() + " isimli sertifika ile " + signatureAlg.getName() + " kullanarak test imza atılıp doğrulandı.");
                    return;
                }
            }
            throw new ESYARuntimeException(certificate.getSubject().toShortTitle() + " isimli sertifika ile test imzalama/imza doğrulama işlemi yapılamadı.\nLütfen anahtar çiftini silip tekrar oluşturmayı deneyiniz.");
        }
    }

    public class EncKeyVerifier{
        private void encryptAndDecrypt(String keyLabel, Long slotNo, SmartCard smartCard, long sessionID,CipherAlg preferredCipherAlg) throws PKCS11Exception, SmartCardException {
            byte[] dataForEnc = "test".getBytes();
            CipherAlg[] cipherAlgList = null;
            if(preferredCipherAlg == null)
                cipherAlgList = new CipherAlg[]{CipherAlg.RSA_PKCS1,CipherAlg.RSA_RAW,CipherAlg.RSA_OAEP};
            else
                cipherAlgList = new CipherAlg[]{preferredCipherAlg};
            for (CipherAlg alg : cipherAlgList) {
                boolean encryptAndDecrypted = encryptAndDecrypt(keyLabel, slotNo, smartCard, sessionID, dataForEnc, alg);
                if (encryptAndDecrypted) {
                    System.out.println("Üretilen " + keyLabel + " etiketli anahtar çifti ile " + alg.getName() + " kullanarak test şifreleme/şifre çözme yapıldı.");
                    return;
                }
            }
            throw new ESYARuntimeException("Üretilen " + keyLabel + " etiketli anahtar çifti ile test şifreleme/şifre çözme işlemi yapılamadı.\nLütfen an" +
                    "ahtar çiftini silip tekrar oluşturmayı deneyiniz.");
        }

        private boolean encryptAndDecrypt(String keyLabel, Long slotNo, SmartCard smartCard, long sessionID, byte[] dataForEnc, CipherAlg cipherAlg) throws PKCS11Exception, SmartCardException {
            boolean isOk = false;
            try {
                byte[] encryptedData = SmartOp.encrypt(smartCard, sessionID, keyLabel, dataForEnc, cipherAlg.getName(), null);
                byte[] decrypted = SmartOp.decrypt(smartCard, sessionID, keyLabel, encryptedData);
                isOk = Arrays.equals(decrypted, dataForEnc);
                return isOk;
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return isOk;
        }
    }

    public class WrapKeyVerifier{
        private void wrapAndUnwrap(String keyLabel, Long slotNo, SmartCard smartCard, long sessionID,WrapAlg preferredAlg) throws PKCS11Exception, SmartCardException {
            WrapAlg[] wrapAlgList = null;
            if(wrapAlgList == null)
                wrapAlgList = new WrapAlg[]{WrapAlg.RSA_PKCS1,WrapAlg.RSA_OAEP};
            else
                wrapAlgList = new WrapAlg[]{preferredAlg};
            for (WrapAlg wrapAlg : wrapAlgList) {
                boolean wrappedAndUnwrapped = wrapAndUnwrapTestSecretKey(keyLabel, slotNo, smartCard, sessionID, wrapAlg);
                if (wrappedAndUnwrapped) {
                    System.out.println("Üretilen " + keyLabel + " etiketli anahtar çifti ile " + wrapAlg.getName() + " kullanarak test wrap/unwrap işlemi yapıldı.");
                    return;
                }
            }
            throw new ESYARuntimeException("Üretilen " + keyLabel + " etiketli anahtar çifti ile test wrap/unwrap işlemi işlemi yapılamadı.\nLütfen an" +
                    "ahtar çiftini silip tekrar oluşturmayı deneyiniz.");
        }

        private boolean wrapAndUnwrapTestSecretKey(String keyLabel, Long slotNo, SmartCard smartCard, long sessionID, WrapAlg wrapAlg) throws PKCS11Exception, SmartCardException {
            boolean isOk = false;
            try {

                String secretKeyLabel = "secretKey-"+System.currentTimeMillis();
                AESKeyTemplate secretKeyTemplate = new AESKeyTemplate(secretKeyLabel,32);
                secretKeyTemplate.getAsExportableTemplate();
                smartCard.createSecretKey(sessionID, secretKeyTemplate);
                deleteLabelList.add(secretKeyLabel);


                byte[] wrappedSecretKey = SmartOp.wrap(smartCard, sessionID, slotNo, wrapAlg.getName(), keyLabel, secretKeyLabel, null);

                String unwrappedKeyLabel = "unwrappedSecretKey-"+System.currentTimeMillis();
                AESKeyTemplate unwrappedKeyTemplate = new AESKeyTemplate(unwrappedKeyLabel,32);
                unwrappedKeyTemplate.getAsExportableTemplate();
                SmartOp.unwrap(smartCard, sessionID, slotNo, wrapAlg.getName(), keyLabel, wrappedSecretKey, unwrappedKeyTemplate, null);
                deleteLabelList.add(unwrappedKeyLabel);
                isOk=true;
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return isOk;
        }
    }

    class KeyImporterWithWrapUnwrap{
        public void importKeyPair(SmartCard smartCard,long sessionId,KeyPair keyPairToImport,KeyPairTemplate unwrappedKeyPairTemplate){
            try {
                String wrapperSessionKeyPairLabel = "wrapper_rsa_"+System.currentTimeMillis();
                System.out.println("Generating session key pair in destination hsm/smart card");
                RSAKeyPairTemplate destRSAUnwrapperKeyTemplate = new RSAKeyPairTemplate(wrapperSessionKeyPairLabel, new RSAKeyGenParameterSpec(2048, null));
                destRSAUnwrapperKeyTemplate.getAsWrapperTemplate();
                KeySpec publicKeySpec = smartCard.createKeyPair(sessionId, destRSAUnwrapperKeyTemplate);
                deleteLabelList.add(wrapperSessionKeyPairLabel);
                System.out.println("Reading session key attributes from destination hsm/smart card");
                PublicKey publicKey = KeyUtil.generatePublicKey(publicKeySpec);

                System.out.println("Generating AES256 bit symetric key in memory");
                SecretKey aesKey = KeyUtil.generateSecretKey(CipherAlg.AES256_CBC, 256);
                BufferedCipher publicKeyAesWrapper = Crypto.getEncryptor(CipherAlg.RSA_ECB_PKCS1);
                publicKeyAesWrapper.init(publicKey,null);
                System.out.println("Wrapping AES 256 symetric key with session wrap public key in memory");
                byte[] wrappedSymetricKey = publicKeyAesWrapper.doFinal(aesKey.getEncoded());

                BufferedCipher privateKeyAesWrapper = Crypto.getEncryptor(CipherAlg.AES256_CBC);
                byte[] ivForPrivateKeyWrap = RandomUtil.generateRandom(16);
                privateKeyAesWrapper.init(aesKey,new ParamsWithIV(ivForPrivateKeyWrap));
                System.out.println("Wrapping real private key with AES 256 symetric key in memory");
                byte[] wrappedPrivateKey = privateKeyAesWrapper.doFinal(keyPairToImport.getPrivate().getEncoded());


                String unwrapperSymetricKeyLabel = "unwrapper_aes_"+System.currentTimeMillis();
                SecretKeyTemplate unwrappedAesKeyTemplate = new AESKeyTemplate(unwrapperSymetricKeyLabel, 32);
                unwrappedAesKeyTemplate.getAsUnwrapperTemplate();
                CK_MECHANISM rsaUnwrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);
                System.out.println("Unwrapping AES256 bit key in destination hsm/smart card");
                smartCard.unwrapKey(sessionId, rsaUnwrapMechanism, destRSAUnwrapperKeyTemplate.getPrivateKeyTemplate(), wrappedSymetricKey, unwrappedAesKeyTemplate);
                deleteLabelList.add(unwrapperSymetricKeyLabel);

                System.out.println("Uwrapping real private  in destination hsm/smart card ");
                smartCard.unwrapKey(sessionId, new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, ivForPrivateKeyWrap), unwrappedAesKeyTemplate, wrappedPrivateKey, unwrappedKeyPairTemplate.getPrivateKeyTemplate());


                KeyPairTemplate publicKeyPairTemplate = null;
                if(unwrappedKeyPairTemplate instanceof RSAKeyPairTemplate){
                    publicKeyPairTemplate = new RSAKeyPairTemplate(((RSAKeyPairTemplate) unwrappedKeyPairTemplate).getPublicKeyTemplate(),null);
                }else {
                    publicKeyPairTemplate = new ECKeyPairTemplate((ECPublicKeyTemplate)unwrappedKeyPairTemplate.getPublicKeyTemplate(),null);
                }

                System.out.println("Importing real public key to destination hsm/smart card ");
                smartCard.importKeyPair(sessionId,publicKeyPairTemplate);
            } catch (Exception e) {
                throw new ESYARuntimeException("Error while importing RSA KeyPair in FipsMode:"+e.getMessage(),e);
            }
        }
    }
}
