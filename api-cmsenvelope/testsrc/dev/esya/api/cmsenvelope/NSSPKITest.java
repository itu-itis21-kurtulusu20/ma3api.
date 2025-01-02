package dev.esya.api.cmsenvelope;

import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import test.esya.api.cmsenvelope.TestData;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeGenerator;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsEnvelopeParser;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsKeyEnvelopeGenerator;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsKeyEnvelopeParser;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.SCDecryptor;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.SCKeyUnwrapperStore;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.KeyFactory;
import tr.gov.tubitak.uekae.esya.api.crypto.MAC;
import tr.gov.tubitak.uekae.esya.api.crypto.Wrapper;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.*;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.CryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.NSSCryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.NSSLoader;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.HMACKeyTemplate;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.List;

/**
 * <b>Author</b>    : zeldal.ozdemir <br/>
 * <b>Project</b>   : MA3   <br/>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br/>
 * <b>Date</b>: 12/5/12 - 11:05 PM <p/>
 * <b>Description</b>: <br/>
 */
public class NSSPKITest {

    private static long sessionID;
    private static SmartCard smartCard;
    private static ECertificate recipient;
    private static ECertificate signer;

    public static void main(String[] args) throws Exception{
/*

        ECRL ecrl = new ECRL(new File("Q:\\ocspfalsecases3\\OCSP-K\\OcspCrls\\OCSPK.crl"));
        byte[] tbsEncodedBytes = ecrl.getTBSEncodedBytes();
        ECertificate root = ECertificate.readFromFile("D:\\Projects\\OCSP\\ocspfalsecases\\RootCerts\\RootK.crt");
        System.out.println(SignUtil.verify(SignatureAlg.RSA_SHA1, tbsEncodedBytes,ecrl.getSignature(),root));
*/


        initCard();
//        cryptoCmsTest();
        cryptoCMSKeyTest();
//        initHmac();
//        reloadHMac();
//        reloadHMACwithJSS();

//        reloadHMACWithPkcs11Softtoken();


    }


    private static void reloadHMACWithPkcs11Softtoken() throws Exception{
        NSSCryptoProvider cryptoProvider = (NSSCryptoProvider) NSSLoader.loadPlatformNSS("D:\\Projects\\MA3API\\nss\\temp", true);
        Crypto.setProvider(cryptoProvider);

        byte[] wrappedHmac = AsnIO.dosyadanOKU("D:\\Projects\\MA3API\\nss\\temp\\wrappedhmac.bin");
        smartCard.unwrapKey(sessionID, new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS),"yetkili_sifre_yonetici1",wrappedHmac,new AESKeyTemplate("hmac").getAsExportableTemplate());


        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 1024);


//        RSAPublicKey pk_wrap = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);


//        smartCard.importKeyPair(sessionID, new RSAKeyPairTemplate(new RSAPublicKeyTemplate("yetkili_sifre_yonetici1_pub", (RSAPublicKey) KeyUtil.decodePublicKey(recipient.getSubjectPublicKeyInfo())).getAsWrapperTemplate(), null ));
//        smartCard.importKeyPair(sessionID, "temp",temp,null,true,true );
        smartCard.importKeyPair(sessionID, new RSAKeyPairTemplate(new RSAPublicKeyTemplate("wrapper", (RSAPublicKey) keyPair.getPublic()).getAsWrapperTemplate(), null));
        byte[] wrappedKey = smartCard.wrapKey(sessionID, new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS), "wrapper", "hmac");

        Wrapper unwrapper = Crypto.getUnwrapper(WrapAlg.RSA_PKCS1);
        unwrapper.init(keyPair.getPrivate());

        KeyTemplate hmacTemplate = new HMACKeyTemplate("hmac", 32).getAsSignerTemplate();
        Key unwrap = unwrapper.unwrap(wrappedKey, hmacTemplate);


        MAC mac = Crypto.getMAC(MACAlg.HMAC_SHA256);

        mac.init(unwrap,null);

        System.out.println(StringUtil.toString(mac.doFinal("testsign".getBytes())));


//        nssCard.unwrapKey(nssSessionID, new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS), "wrapper", wrappedKey, hmacTemplate);
//        nssCard.signData(nssSessionID,"hmac","asdasds".getBytes(),new CK_MECHANISM(PKCS11Constants.CKM_SHA256_HMAC));



/*        SunPKCS11 sunPKCS11= (SunPKCS11) ((NSSCryptoProvider) cryptoProvider).getmProvider();
        Field p11Field = SunPKCS11.class.getDeclaredField("p11");
        p11Field.setAccessible(true);
        PKCS11 pkcs11 = (PKCS11) p11Field.get(sunPKCS11);
        p11Field.setAccessible(false);

        Field slotIDField = SunPKCS11.class.getDeclaredField("slotID");
        slotIDField.setAccessible(true);
        long slotID = slotIDField.getLong(sunPKCS11);
        slotIDField.setAccessible(false);

        Field tokenField = SunPKCS11.class.getDeclaredField("token");
        tokenField.setAccessible(true);
        Object token = tokenField.get(sunPKCS11);
        tokenField.setAccessible(false);

        Class<?> tokenClass = Class.forName("sun.security.pkcs11.Token");
        Method getOpSession = tokenClass.getDeclaredMethod("getOpSession");
        getOpSession.setAccessible(true);
        Object session = getOpSession.invoke(token);

        Class<?> sessionClass = Class.forName("sun.security.pkcs11.Session");
        Method idMethod = sessionClass.getDeclaredMethod("id");
        idMethod.setAccessible(true);
        Long nssSessionID = (Long) idMethod.invoke(session);


        Secmod.Module module = Secmod.getInstance().getModule(Secmod.ModuleType.FIPS);

        SmartCard nssCard = new SmartCard(new NSSCardType(pkcs11));*/








    }

/*    private static void reloadHMACwithJSS() throws Exception {
        CryptoManager.initialize("D:\\Projects\\MA3API\\nss\\temp\\nssdb-2131058440");
        CryptoManager cryptoManager = CryptoManager.getInstance();
        cryptoManager.setPasswordCallback(new Password("Test'!123456".toCharArray()));


        byte[] wrappedHmac = AsnIO.dosyadanOKU("D:\\Projects\\MA3API\\nss\\temp\\wrappedhmac.bin");
        smartCard.unwrapKey(sessionID, new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS),"yetkili_sifre_yonetici1",wrappedHmac,new AESKeyTemplate("hmac").getAsExportableTemplate());



//        Provider provider = cryptoManager.getInternalCryptoToken().getmProvider();
//        KeyStore keyStore = ((NSSCryptoProvider) cryptoProvider).getKeyStore();


        CryptoToken cryptoToken = cryptoManager.getInternalCryptoToken();
        org.mozilla.jss.crypto.KeyPairGenerator generator = cryptoToken.getKeyPairGenerator(KeyPairAlgorithm.RSA);
        generator.initialize(1024);
        KeyPair keyPair = generator.genKeyPair();
        RSAPublicKeyImpl publicKey = new RSAPublicKeyImpl(keyPair.getPublic().getEncoded());
//        smartCard.importKeyPair(sessionID, new RSAKeyPairTemplate(new RSAPublicKeyTemplate("yetkili_sifre_yonetici1_pub", (RSAPublicKey) KeyUtil.decodePublicKey(recipient.getSubjectPublicKeyInfo())).getAsWrapperTemplate(), null ));
//        smartCard.importKeyPair(sessionID, "temp",temp,null,true,true );
        smartCard.importKeyPair(sessionID, new RSAKeyPairTemplate(new RSAPublicKeyTemplate("wrapper", publicKey).getAsWrapperTemplate(), null));
        byte[] wrappedKey = smartCard.wrapKey(sessionID, new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS), "wrapper", "hmac");

        KeyWrapper keyWrapper = cryptoToken.getKeyWrapper(KeyWrapAlgorithm.RSA);
        keyWrapper.initUnwrap((org.mozilla.jss.crypto.PrivateKey) keyPair.getPrivate(),null );
        SymmetricKey symmetricKey = keyWrapper.unwrapSymmetric(wrappedKey, SymmetricKey.Type.SHA1_HMAC, SymmetricKey.Usage.ENCRYPT, 256);



        JSSMessageDigest digestContext = cryptoToken.getDigestContext(HMACAlgorithm.SHA256);
        digestContext.initHMAC(symmetricKey);
        byte[] digest = digestContext.digest("awreawew".getBytes());


    }*/

    private static void reloadHMac() throws Exception{

        CryptoProvider cryptoProvider = NSSLoader.loadPlatformNSS("D:\\Projects\\MA3API\\nss\\temp", true);
        Crypto.setProvider(cryptoProvider);

        byte[] wrappedHmac = AsnIO.dosyadanOKU("D:\\Projects\\MA3API\\nss\\temp\\wrappedhmac.bin");
        smartCard.unwrapKey(sessionID, new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS),"yetkili_sifre_yonetici1",wrappedHmac,new AESKeyTemplate("hmac").getAsExportableTemplate());



        Provider provider = ((NSSCryptoProvider) cryptoProvider).getmProvider();


        KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA", provider);
        rsa.initialize(1024);
        KeyPair keyPair = rsa.generateKeyPair();

        X509EncodedKeySpec rsaPublicKeySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());

        java.security.KeyFactory instance = java.security.KeyFactory.getInstance("RSA", "SunRsaSign");
        RSAPublicKey publicKey = (RSAPublicKey) instance.generatePublic(rsaPublicKeySpec);


//        smartCard.importKeyPair(sessionID, new RSAKeyPairTemplate(new RSAPublicKeyTemplate("yetkili_sifre_yonetici1_pub", (RSAPublicKey) KeyUtil.decodePublicKey(recipient.getSubjectPublicKeyInfo())).getAsWrapperTemplate(), null ));
//        smartCard.importKeyPair(sessionID, "temp",temp,null,true,true );
        smartCard.importKeyPair(sessionID, new RSAKeyPairTemplate(new RSAPublicKeyTemplate("wrapper", publicKey).getAsWrapperTemplate(), null));
        byte[] wrappedKey = smartCard.wrapKey(sessionID, new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS), "wrapper", "hmac");


        Cipher unwrapper = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
        unwrapper.init(Cipher.UNWRAP_MODE, keyPair.getPrivate());
        Key unwrap = unwrapper.unwrap(wrappedKey, "HmacSHA256", Cipher.SECRET_KEY);


        Mac mac = Mac.getInstance("HmacSHA256", provider);
        mac.init(unwrap);
        byte[] bytes1 = mac.doFinal("test".getBytes());
        byte[] bytes2 = mac.doFinal("test".getBytes());
        System.out.println(Arrays.equals(bytes1,bytes2));

    }

    private static void initHmac() throws Exception{
        CryptoProvider cryptoProvider = NSSLoader.loadPlatformNSS("D:\\Projects\\MA3API\\nss\\temp", true);
        Crypto.setProvider(cryptoProvider);



        Provider provider = ((NSSCryptoProvider) cryptoProvider).getmProvider();



        KeyFactory keyFactory = Crypto.getKeyFactory();
        SecretKey secretKey = keyFactory.generateSecretKey(CipherAlg.AES256_CBC, 256);



        Wrapper wrapper = Crypto.getWrapper(WrapAlg.RSA_ECB_PKCS1);
        wrapper.init(KeyUtil.decodePublicKey(recipient.getSubjectPublicKeyInfo()),null);

        byte[] wrapedData = wrapper.wrap(secretKey);

/*

        Cipher wrapper = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
        wrapper.init(Cipher.WRAP_MODE, KeyUtil.decodePublicKey(recipient.getSubjectPublicKeyInfo()));
        byte[] wrapedData = wrapper.wrap(secretKey);*/
        AsnIO.dosyayaz(wrapedData, "D:\\Projects\\MA3API\\nss\\temp\\wrappedhmac.bin");

/*        KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA", provider);
rsa.initialize(1024);
KeyPair keyPair = rsa.generateKeyPair();

KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", provider);
keyGenerator.init(256);
SecretKey secretKey = keyGenerator.generateKey();

Cipher wrapper = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
wrapper.init(Cipher.WRAP_MODE, keyPair.getPublic());
byte[] wrap = wrapper.wrap(secretKey);

Cipher unwrapper = Cipher.getInstance("RSA/ECB/PKCS1Padding", provider);
unwrapper.init(Cipher.UNWRAP_MODE, keyPair.getPrivate());
Key unwrap = unwrapper.unwrap(wrap, "RSA/ECB/PKCS1Padding", Cipher.SECRET_KEY);

SecretKeyFactory aes = SecretKeyFactory.getInstance("AES", provider);
SecretKey hmac = aes.generateSecret(new SecretKeySpec(unwrap.getEncoded(), "AES"));*/

/*        KeyStore keyStore = KeyStore.getInstance("PKCS11", provider);
        keyStore.load(null);
        keyStore.setEntry("hmac", new KeyStore.SecretKeyEntry(secretKey),new  );*/


/*        Mac mac = Mac.getInstance("HmacSHA256", provider);
        mac.init(hmackey);
        byte[] bytes1 = mac.doFinal("test".getBytes());
        byte[] bytes2 = mac.doFinal("test".getBytes());
        System.out.println(Arrays.equals(bytes1,bytes2));*/


    }

    private static void cryptoCmsTest() throws Exception {
        CryptoProvider cryptoProvider = NSSLoader.loadPlatformNSS("D:\\Projects\\MA3API\\nss\\temp", true);
        Crypto.setProvider(cryptoProvider);

        byte[] plainData = "plainDataToTest".getBytes();
        CmsEnvelopeGenerator generator = new CmsEnvelopeGenerator(plainData, CipherAlg.AES256_CBC);
        generator.addRecipients(TestData.ENVELOPE_CONFIG,recipient);
        byte[] encrypted = generator.generate();
        byte[] signed = SmartOp.sign(smartCard, sessionID, 1, "yetkili_imza_yonetici1", plainData, SignatureAlg.RSA_SHA256.getName());


//        Crypto.setProvider(new GNUCryptoProvider());


        CmsEnvelopeParser parser = new CmsEnvelopeParser(encrypted);
        byte[] open = parser.open(new SCDecryptor(smartCard, sessionID));

        System.out.println("opened:"+new String(open)+" result:"+ Arrays.equals(plainData, open));


        System.out.println("Sign Result:" + SignUtil.verify(SignatureAlg.RSA_SHA256, null, plainData, signed, signer));
    }
    private static void cryptoCMSKeyTest() throws Exception {
        NSSCryptoProvider cryptoProvider = (NSSCryptoProvider) NSSLoader.loadPlatformNSS("D:\\Projects\\MA3API\\nss\\temp", true);
        Crypto.setProvider(cryptoProvider);

        byte[] plainData = "plainDataToTest".getBytes();

        KeyFactory keyFactory = Crypto.getKeyFactory();
        SecretKey secretKey = keyFactory.generateSecretKey(CipherAlg.AES256_CBC, 256);


        MAC mac1 = Crypto.getMAC(MACAlg.HMAC_SHA256);

        mac1.init(secretKey,null);
        System.out.println(StringUtil.toString(mac1.doFinal("testsign".getBytes())));


        CmsKeyEnvelopeGenerator generator = new CmsKeyEnvelopeGenerator(secretKey, CipherAlg.AES256_CBC);
        generator.addRecipients(TestData.ENVELOPE_CONFIG,recipient);
        byte[] encrypted = generator.generate();
//        byte[] signed = SmartOp.sign(smartCard, sessionID, 1, "yetkili_imza_yonetici1", plainData, SignatureAlg.RSA_SHA256.getName());


//        Crypto.setProvider(new GNUCryptoProvider());


        CmsKeyEnvelopeParser parser = new CmsKeyEnvelopeParser(encrypted);
        KeyTemplate hmac = new HMACKeyTemplate("hmac").getAsSignerTemplate();
        Key open = parser.open(new SCKeyUnwrapperStore(smartCard, sessionID), hmac);

        MAC mac2 = Crypto.getMAC(MACAlg.HMAC_SHA256);

        mac2.init(open,null);

        System.out.println(StringUtil.toString(mac2.doFinal("testsign".getBytes())));

//        System.out.println("opened:"+new String(open)+" result:"+ Arrays.equals(plainData, open));


//        System.out.println("Sign Result:" + SignUtil.verify(SignatureAlg.RSA_SHA256, null, plainData, signed, signer));
    }

    private static void initCard() throws Exception {
        smartCard = new SmartCard(CardType.UTIMACO);
        sessionID = smartCard.openSession(1);
        smartCard.login(sessionID, "123456");
        List<byte[]> list = smartCard.readCertificate(sessionID, "yetkili_cer_sifre_yonetici1");
        recipient = new ECertificate(list.get(0));
        list = smartCard.readCertificate(sessionID, "yetkili_cer_imza_yonetici1");
        signer = new ECertificate(list.get(0));

        try {
            smartCard.deletePrivateObject(sessionID,"temp");
            smartCard.deletePublicObject(sessionID,"temp");
        } catch (Exception e) {
        }
    }
}
