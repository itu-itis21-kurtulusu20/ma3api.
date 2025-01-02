package tr.gov.tubitak.uekae.esya.api.crypto;

import gnu.crypto.key.ecdsa.ECDSAPrivateKey;
import gnu.crypto.key.ecdsa.ECDSAPublicKey;
import org.junit.*;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithECParameterSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.params.SecretKeySpec;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.NSSCryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplateFactory;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.KeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPrivateKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
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
public class AtikHSMKeyWrapUnwrapTest {
    static SmartCard sc = null;
    static long sessionId = -1;
    static long slotNo = 2;
    static CardType cardType = CardType.ATIKHSM;
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

    @After
    public void testMethodCleanUp(){
        for (String objectLabel : deleteLabelList) {
            smartCardHelper.deleteAllObjectWithLabel(objectLabel);
        }
        System.out.println("-------------------------- TEST  SONU ----------------------");
    }

    @Test
    public void testGeneratRSAWrapKeyInSmartCard() throws CryptoException, IOException, SmartCardException, PKCS11Exception {
        String keyLabel = "wrapUnwrapKey";
        RSAKeyGenParameterSpec rsaKeyGenParameterSpec = new RSAKeyGenParameterSpec(2048,null);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel,rsaKeyGenParameterSpec);
        rsaKeyPairTemplate.getAsTokenTemplate(false, false, true);
        rsaKeyPairTemplate.getAsExtractableTemplate();

        System.out.println(keyLabel + " etiketli rsa şifreleme/wrap anahtarı kart içerisinde oluşturulacak.");
        sc.createKeyPair(sessionId, rsaKeyPairTemplate);
        System.out.println(keyLabel + " etiketli rsa şifreleme/wrap anahtarı kart içerisinde oluşturuldu.");
        deleteLabelList.add(keyLabel);
    }

    @Test
    public void testWrapWithNSSUnwrapWithAtik() throws CryptoException, IOException, SmartCardException, PKCS11Exception {

        String keyLabel = "wrapUnwrapKey";

        KeySpec publicKeySpec = sc.readPublicKeySpec(sessionId, keyLabel);
        PublicKey wrapUnwrapPublicKey = KeyUtil.generatePublicKey(publicKeySpec);

        NSSCryptoProvider cryptoProvider = NSSTestUtil.constructProvider(null);
        Crypto.setProvider(cryptoProvider);

        KeyFactory keyFactory = Crypto.getKeyFactory();

        //   KeyFactory kf = Crypto.getKeyFactory();

        String encKeyLabel = "encKeyLabel";
        SecretKeySpec secretKeySpec= new SecretKeySpec(CipherAlg.AES256_CBC,encKeyLabel,256);
        SecretKey secretKey = keyFactory.generateSecretKey(secretKeySpec);


        Wrapper wrapper = Crypto.getWrapper(WrapAlg.RSA_PKCS1);
        wrapper.init(wrapUnwrapPublicKey,null);

        KeyTemplate secretKeyTemplate = KeyTemplateFactory.getKeyTemplate(encKeyLabel, secretKey);
        byte[] wrappedSecretKeyWithNssPublic = wrapper.wrap(secretKeyTemplate);

        String label = "cmskey-" + System.currentTimeMillis();
        AESKeyTemplate aesKeyTemplate = (AESKeyTemplate) new AESKeyTemplate(label).getAsUnwrapperTemplate();
        CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);


        //byte[] serial = eCertList.get(0).getSerialNumber().toByteArray();
        String[] encryptionKeyLabels = sc.getEncryptionKeyLabels(sessionId);
        sc.unwrapKey(sessionId, mechanism, encryptionKeyLabels[0], wrappedSecretKeyWithNssPublic, aesKeyTemplate);
        System.out.println("testWrapWithNSS_UnwrapWithSmartCard-Completed success");
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
                verified = SmartOp.verify(smartCard, sessionID, slotNo, keyLabel, dataForSign, signedData, signingAlgStr);
                return verified;
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return verified;
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
