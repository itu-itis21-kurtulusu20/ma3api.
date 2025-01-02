package tr.gov.tubitak.uekae.esya.api.crypto;

import junit.framework.TestCase;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.*;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.params.SecretKeySpec;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.NSSCryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.ECParameters;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplateFactory;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.KeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECPrivateKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPrivateKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.HMACKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.PKCS11Ops;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.Arrays;
import java.util.List;

import static sun.security.pkcs11.wrapper.PKCS11Constants.*;

/**
 * Created with IntelliJ IDEA.
 * User: ramazan.girgin
 * Date: 8/1/13
 * Time: 9:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class NSSTest extends TestCase {

    /*public void testMACCreateAndUse() throws CryptoException {
        NSSCryptoProvider cryptoProvider = NSSTestUtil.constructProvider(null);
        Crypto.setProvider(cryptoProvider);
        Provider p =    cryptoProvider.getmProvider();


        KeyFactory kf = Crypto.getKeyFactory();
        tr.gov.tubitak.uekae.esya.api.crypto.params.SecretKeySpec secretKeySpec = new tr.gov.tubitak.uekae.esya.api.crypto.params.SecretKeySpec(CipherAlg.AES256_CBC,"TabloImzalama",256);

        //SecretKey tabloImzalama = kf.generateSecretKey(CipherAlg.AES256_CBC,256)   ;
        SecretKey tabloImzalama = kf.generateSecretKey(secretKeySpec)   ;
        System.out.print("basarılı");


        HMACKeyTemplate keyTemplate = new HMACKeyTemplate("TabloImzalama",256,"SHA256");

        MAC mac = Crypto.getMAC(MACAlg.HMAC_SHA256);
        mac.init(keyTemplate,null);
        //Mac mac = Mac.getInstance("HMACSHA256", p);
        //mac.init(ske.getSecretKey());

        byte[] value = mac.doFinal("test".getBytes());
        System.out.println(Arrays.toString(value));
    }*/

    public void testWrapWithNSS_UnwrapWithNSS2() throws Exception{

        NSSCryptoProvider cryptoProvider = NSSTestUtil.constructProvider(null);
        Crypto.setProvider(cryptoProvider);

        KeyFactory keyFactory = Crypto.getKeyFactory();

        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 1024);
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();


        SecretKey secretKey = keyFactory.generateSecretKey(CipherAlg.AES256_CBC, 256);

        Wrapper wrapper = Crypto.getWrapper(WrapAlg.RSA_ECB_PKCS1);
        wrapper.init(publicKey,null);
        byte[] wrappedSecretKeyWithNssPublic = wrapper.wrap(secretKey);

        Crypto.setProvider(Crypto.PROVIDER_SUN);
        Wrapper unwrapper = Crypto.getUnwrapper(WrapAlg.RSA_ECB_PKCS1);
        unwrapper.init(privateKey);

        SecretKeyTemplate template = new AESKeyTemplate("test2", 16).getAsWrapperTemplate();
        Key secretUnwrapped = unwrapper.unwrap(wrappedSecretKeyWithNssPublic, template);
        System.out.println("testWrapWithNSS_UnwrapWithNSS-Completed success");

    }


    public void testWrapWithNSS_UnwrapWithNSS() throws Exception{

        NSSCryptoProvider cryptoProvider = NSSTestUtil.constructProvider(null);
        Crypto.setProvider(cryptoProvider);

        KeyFactory keyFactory = Crypto.getKeyFactory();

        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 1024);
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();


        SecretKey secretKey = keyFactory.generateSecretKey(CipherAlg.AES256_CBC, 256);

        Wrapper wrapper = Crypto.getWrapper(WrapAlg.RSA_ECB_PKCS1);
        wrapper.init(publicKey,null);
        byte[] wrappedSecretKeyWithNssPublic = wrapper.wrap(secretKey);
        Wrapper unwrapper = Crypto.getUnwrapper(WrapAlg.RSA_ECB_PKCS1);
        unwrapper.init(privateKey);

        SecretKeyTemplate template = new AESKeyTemplate("test2", 16).getAsWrapperTemplate();
        Key secretUnwrapped = unwrapper.unwrap(wrappedSecretKeyWithNssPublic, template);
        System.out.println("testWrapWithNSS_UnwrapWithNSS-Completed success");

    }

    public void testWrapWithNSS_UnwrapWithSmartCardMYK() throws Exception {

        SmartCard smartCard = null;
        long sessionId = 0;
        try {
            NSSCryptoProvider cryptoProvider = NSSTestUtil.constructProvider(null);
            Crypto.setProvider(cryptoProvider);

            KeyFactory keyFactory = Crypto.getKeyFactory();

            //   KeyFactory kf = Crypto.getKeyFactory();

            String encKeyLabel = "encKeyLabel";
            SecretKeySpec secretKeySpec= new SecretKeySpec(CipherAlg.AES256_CBC,encKeyLabel,256);
            SecretKey secretKey = keyFactory.generateSecretKey(secretKeySpec);


            Pair<Pair<SmartCard, Long>, List<Pair<String, PublicKey>>> pair = NSSTestUtil.readPublicKeyForEnc(1);
            smartCard= pair.getObject1().getObject1();
            sessionId = pair.getObject1().getObject2();
            List<Pair<String, PublicKey>> eCertList = pair.getObject2();

            Wrapper wrapper = Crypto.getWrapper(WrapAlg.RSA_PKCS1);
            wrapper.init(eCertList.get(0).getObject2(),null);

            KeyTemplate secretKeyTemplate = KeyTemplateFactory.getKeyTemplate(encKeyLabel, secretKey);
            byte[] wrappedSecretKeyWithNssPublic = wrapper.wrap(secretKeyTemplate);


            {
                String labelOfKeyToWrap = "fakeSecretKey-"+System.currentTimeMillis();
                SecretKeyTemplate fakeSecretKey = new AESKeyTemplate(labelOfKeyToWrap,16);
                fakeSecretKey.getAsUnwrapperTemplate();
                smartCard.createSecretKey(sessionId,fakeSecretKey);
            }

            String label = "cmskey-" + System.currentTimeMillis();
            AESKeyTemplate aesKeyTemplate = (AESKeyTemplate) new AESKeyTemplate(label).getAsUnwrapperTemplate();
            CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);


            //byte[] serial = eCertList.get(0).getSerialNumber().toByteArray();
            String[] encryptionKeyLabels = smartCard.getEncryptionKeyLabels(sessionId);
            smartCard.unwrapKey(sessionId,mechanism,encryptionKeyLabels[0],wrappedSecretKeyWithNssPublic,aesKeyTemplate);
            System.out.println("testWrapWithNSS_UnwrapWithSmartCard-Completed success");

        }
        catch(Exception aEx)
        {
            System.out.println("unwrapde hata");
            aEx.printStackTrace();
        }
        finally {

            try {
                //  smartCard.closeSession(sessionId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static CK_MECHANISM findMech(CipherAlg cipherAlg,AlgorithmParams algorithmParams,boolean asymmetric) throws CryptoException {
        CK_MECHANISM retMechanism=null;
        if (cipherAlg.equals(CipherAlg.RSA_PKCS1) || cipherAlg.equals(CipherAlg.RSA_ECB_PKCS1))
            retMechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);
        else if (cipherAlg.equals(CipherAlg.AES128_ECB) || cipherAlg.equals(CipherAlg.AES192_ECB) || cipherAlg.equals(CipherAlg.AES256_ECB)) {
            if (algorithmParams instanceof ParamsWithIV)
                retMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC, ((ParamsWithIV) algorithmParams).getIV());  // **** TODO check
            else
                throw new CryptoException("ParamsWithIV expected as AlgorithmParams :" + algorithmParams);
        } else if (cipherAlg.equals(CipherAlg.AES128_CBC) || cipherAlg.equals(CipherAlg.AES192_CBC) || cipherAlg.equals(CipherAlg.AES256_CBC)) {
            if (algorithmParams instanceof ParamsWithIV) {
                if (asymmetric)
                    retMechanism  = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC, ((ParamsWithIV) algorithmParams).getIV());  // **** TODO check
                else
                    retMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, ((ParamsWithIV) algorithmParams).getIV());  // **** TODO check
            }
        } else
            throw new CryptoException("Unknown Wrap Alg:" + cipherAlg);
        return retMechanism;
    }

    static protected byte[] toByteArray(BigInteger aX)
    {
        byte[] xx = aX.toByteArray();
        if (xx[0] == 0)
        {
            byte[] temp = new byte[xx.length - 1];
            System.arraycopy(xx,
                    1,
                    temp,
                    0,
                    temp.length);
            xx = temp;
        }
        return xx;
    }

    static KeyTemplate getPrivateKeyTemplate(PublicKey publicKey,String keyLabel,boolean isSigningKey,boolean isEncryptKey,X509Certificate x509Certificate) throws NoSuchAlgorithmException, CryptoException, IOException {
        if(publicKey instanceof RSAPublicKey){
            byte[] subject = x509Certificate.getSubjectX500Principal().getEncoded();
            RSAPrivateKeyTemplate privateKeyTemplate = new RSAPrivateKeyTemplate(keyLabel, null).getAsTokenTemplate(isSigningKey,isEncryptKey, false);
            if(subject != null)
                privateKeyTemplate.add(new CK_ATTRIBUTE(CKA_SUBJECT,subject));

            PublicKey pubkey = x509Certificate.getPublicKey();
            byte[] id = null;
            MessageDigest digester = MessageDigest.getInstance("SHA-1");
            if(pubkey.getAlgorithm().contains(Algorithms.ASYM_ALGO_RSA))
            {
                byte[] modulus = toByteArray(((RSAPublicKey)pubkey).getModulus());
                id = DigestUtil.digest(DigestAlg.SHA1, modulus);
            }
            else
            {

                id = DigestUtil.digest(DigestAlg.SHA1, pubkey.getEncoded());
            }
            privateKeyTemplate.add(new CK_ATTRIBUTE(CKA_ID,id));
            privateKeyTemplate.add(new CK_ATTRIBUTE(CKA_LABEL,keyLabel));
            privateKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN,isSigningKey));
            privateKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT,isEncryptKey));
            privateKeyTemplate.add(new CK_ATTRIBUTE(CKA_PRIVATE,true));
            privateKeyTemplate.add(new CK_ATTRIBUTE(CKA_SENSITIVE,true));
            /*if(isUnwrapperKey){
                privateKeyTemplate.add(new CK_ATTRIBUTE(CKA_UNWRAP,true));
            }*/
            return privateKeyTemplate;
        }
        else if(publicKey instanceof ECPublicKey){
            ECPublicKey ecPublicKey = (ECPublicKey) publicKey;
            ECParameterSpec params = ecPublicKey.getParams();
            String curveName = ECParameters.getCurveName(params);
            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(curveName);

            ECGenParameterSpec spec = new ECGenParameterSpec(curveName);
            ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec(curveName);


            ECPrivateKeyTemplate ecPrivateKeyTemplate = new ECPrivateKeyTemplate(keyLabel, ecParameterSpec);
            ecPrivateKeyTemplate.initIDAttribute(ecPublicKey);
            ecPrivateKeyTemplate.getAsTokenOrSessionTemplate(true);
            if(isSigningKey)
                ecPrivateKeyTemplate.getAsSignerTemplate();
            else if(isEncryptKey)
            {
                /*ecPrivateKeyTemplate.add(new CK_ATTRIBUTE(CKA_SENSITIVE, true));
                ecPrivateKeyTemplate.add(new CK_ATTRIBUTE(CKA_DECRYPT, false));
                ecPrivateKeyTemplate.add(new CK_ATTRIBUTE(CKA_SIGN, false));
                ecPrivateKeyTemplate.add(new CK_ATTRIBUTE(CKA_DERIVE, true));
                */
                ecPrivateKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN,true));
                ecPrivateKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE,true));
                ecPrivateKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS,CKO_PRIVATE_KEY));
                ecPrivateKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE,PKCS11Constants.CKK_ECDSA));
                //ecPrivateKeyTemplate.add(ecPrivateKeyTemplate.getAttributes().get(6));
                //ecPrivateKeyTemplate.add(ecPrivateKeyTemplate.getAttributes().get(9));
                //ecPrivateKeyTemplate.add(ecPrivateKeyTemplate.getAttributes().get(0));
                ecPrivateKeyTemplate.add(new CK_ATTRIBUTE(CKA_DECRYPT,false));
                ecPrivateKeyTemplate.add(new CK_ATTRIBUTE(CKA_SIGN,false));
            }
            return ecPrivateKeyTemplate;
        }
        return null;
    }

    public void testWrapWithUTIMACO_UnwrapWithNCIPHER_ELLIPTIC_CURVE() throws Exception{

        NSSCryptoProvider cryptoProvider = NSSTestUtil.constructProvider(null);
        Crypto.setProvider(cryptoProvider);

        SmartCard smartCardSource = null;
        long sessionIdSource = 0;

        SmartCard smartCardDest = null;
        long sessionIdDest = 0;

        try {

            {
                CardType sourceCardTypee = CardType.UTIMACO;
                String sourceKeyLabel = "yetkili_sifre_kay5";
                long sourceKeySlot = 11;

                //CardType destCardType = CardType.UTIMACO;long destKeySlot = 11;//761406622;
                CardType destCardType = CardType.NCIPHER;long destKeySlot = 761406622;String destPin="123456";
                //CardType destCardType = CardType.SAFENET;long destKeySlot = 1;String destPin="12345678";
                String destKeyLabel = "yetkili_sifre_kay5_Wrap"+System.currentTimeMillis();



                smartCardSource = new SmartCard(sourceCardTypee);
                sessionIdSource = smartCardSource.openSession(sourceKeySlot);
                smartCardSource.login(sessionIdSource,"123456");

                smartCardDest = new SmartCard(destCardType);
                sessionIdDest = smartCardDest.openSession(destKeySlot);
                try {
                    smartCardDest.login(sessionIdDest,destPin);
                }
                catch (Exception exc){

                }
                //Kaynak HSM içerisinde wrap anahtarlarını oluştur.
                String labelWrapperSecretKey = "kp_wrap"+System.currentTimeMillis();
                RSAKeyGenParameterSpec keyGenSpec = new RSAKeyGenParameterSpec(1024, null);
                KeyPairTemplate wrapperTemplate = new RSAKeyPairTemplate(labelWrapperSecretKey, keyGenSpec).getAsWrapperTemplate();
                wrapperTemplate.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(CKA_DERIVE,true));

                smartCardDest.createKeyPair(sessionIdDest, wrapperTemplate);
                RSAPublicKeySpec keySpec = (RSAPublicKeySpec) smartCardDest.readPublicKeySpec(sessionIdDest, labelWrapperSecretKey);
                PublicKey wrapPublicKey = (RSAPublicKey) java.security.KeyFactory.getInstance("RSA").generatePublic(keySpec);

                //1-Protocol encrytion Key'i HSM'e import ediyoruz.
                String protEncKeyLabel = "protEncKeyLabel"+System.currentTimeMillis();
                smartCardSource.importKeyPair(sessionIdSource, new RSAKeyPairTemplate(new RSAPublicKeyTemplate(protEncKeyLabel, (RSAPublicKey) wrapPublicKey).getAsWrapperTemplate(), null) );

                //2-HSM içerisinde secret key oluşturuyoruz.
                String keyWrapSecretLabel = "keyWrapSecret-"+System.currentTimeMillis();
                int simKeyLenght=16;
                SecretKeyTemplate template = new AESKeyTemplate(keyWrapSecretLabel, simKeyLenght).getAsWrapperTemplate().getAsExportableTemplate();
                smartCardSource.createSecretKey(sessionIdSource, template);

                //4-İstenilen anahtarı Secret Key ile wrap ediyoruz.
                byte[] iv = RandomUtil.generateRandom(16);
                CipherAlg protEncKeySmyAlg = CipherAlg.AES256_CBC;
                ParamsWithIV paramsWithIV = new ParamsWithIV(iv);
                CK_MECHANISM symMechanism = findMech(protEncKeySmyAlg,paramsWithIV, false);
                byte[] wrappedUserPrivateKey = smartCardSource.wrapKey(sessionIdSource, symMechanism, keyWrapSecretLabel, sourceKeyLabel);

                CK_MECHANISM asymMechanism = findMech(CipherAlg.RSA_ECB_PKCS1, null,true);
                //3-Secret Key'i bizde gönderilen Protocol Enc key ile wrap ediyoruz.
                byte[] wrappedSecretkey = smartCardSource.wrapKey(sessionIdSource, asymMechanism , protEncKeyLabel, keyWrapSecretLabel);


                KeySpec publicKey = smartCardSource.readPublicKeySpec(sessionIdSource, sourceKeyLabel);
                PublicKey destPublicKey = KeyUtil.generatePublicKey(publicKey);

                // ALICI TARAFI
                String destUnwrapKey = "userKeyUnwrapper"+System.currentTimeMillis();
                AESKeyTemplate destUnwrapperTemplate = new AESKeyTemplate(destUnwrapKey, 16);
                destUnwrapperTemplate.getAsUnwrapperTemplate();
                destUnwrapperTemplate.getAsDecryptorTemplate();
                destUnwrapperTemplate.add(new CK_ATTRIBUTE(CKA_DERIVE,true));
                //5-Secret Key'i kullanıcı kartı içerisinde unwrap et.
                smartCardDest.unwrapKey(sessionIdDest,asymMechanism,labelWrapperSecretKey,wrappedSecretkey,destUnwrapperTemplate);

                boolean isSignKey = true;
                boolean isEncKey = false;
                KeyTemplate privateKeyTemplateSign = getPrivateKeyTemplate(destPublicKey, destKeyLabel, isSignKey,isEncKey, null);
                KeyTemplate privateKeyTemplateEnc = getPrivateKeyTemplate(destPublicKey, destKeyLabel+"_1", false,true, null);

                //Kullanıcı private key'ini unwrap et.
                //Pair<CipherAlg,AlgorithmParams> cipherAlgAndParam = CipherAlg.fromAlgorithmIdentifier(symAlgId);
                //CK_MECHANISM symMechanism = findMech(protEncKeySmyAlg, paramsWithIV,false);
                smartCardDest.unwrapKey(sessionIdDest, symMechanism, destUnwrapperTemplate, wrappedUserPrivateKey, privateKeyTemplateSign);
                smartCardDest.unwrapKey(sessionIdDest, symMechanism, destUnwrapperTemplate, wrappedUserPrivateKey, privateKeyTemplateEnc);

                if(privateKeyTemplateEnc instanceof ECPrivateKeyTemplate){
                    PKCS11Ops pkcs11Ops = (PKCS11Ops) smartCardDest.getCardType().getCardTemplate().getPKCS11Ops();
                    pkcs11Ops.getmPKCS11().C_SetAttributeValue(sessionIdDest,privateKeyTemplateEnc.getKeyId(),new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(CKA_DERIVE, true)});

                }
            }
        }
        catch(Exception aEx)
        {
            System.out.println("unwrapde hata");
            aEx.printStackTrace();
        }
        finally {

            try {
                //  smartCard.closeSession(sessionId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void testWrapWithNSS_UnwrapWithSmartCard() throws Exception{

        SmartCard smartCard = null;
        long sessionId = 0;
        try {
            NSSCryptoProvider cryptoProvider = NSSTestUtil.constructProvider(null);
            Crypto.setProvider(cryptoProvider);

            KeyFactory keyFactory = Crypto.getKeyFactory();

         //   KeyFactory kf = Crypto.getKeyFactory();

            String encKeyLabel = "encKeyLabel";
            SecretKeySpec secretKeySpec= new SecretKeySpec(CipherAlg.AES256_CBC,encKeyLabel,256);
            SecretKey secretKey = keyFactory.generateSecretKey(secretKeySpec);


            Pair<Pair<SmartCard, Long>, List<Pair<String, PublicKey>>> pair = NSSTestUtil.readPublicKeyForEnc(1);
            smartCard= pair.getObject1().getObject1();
            sessionId = pair.getObject1().getObject2();
            List<Pair<String, PublicKey>> eCertList = pair.getObject2();


            Wrapper wrapper = Crypto.getWrapper(WrapAlg.RSA_PKCS1);
            wrapper.init(eCertList.get(0).getObject2(),null);

            KeyTemplate secretKeyTemplate = KeyTemplateFactory.getKeyTemplate(encKeyLabel, secretKey);
            byte[] wrappedSecretKeyWithNssPublic = wrapper.wrap(secretKeyTemplate);

            String label = "cmskey-" + System.currentTimeMillis();
            AESKeyTemplate aesKeyTemplate = (AESKeyTemplate) new AESKeyTemplate(label).getAsUnwrapperTemplate();
            CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);

            //byte[] serial = eCertList.get(0).getSerialNumber().toByteArray();
            String[] encryptionKeyLabels = smartCard.getEncryptionKeyLabels(sessionId);
            smartCard.unwrapKey(sessionId,mechanism,encryptionKeyLabels[0],wrappedSecretKeyWithNssPublic,aesKeyTemplate);
            //smartCard.unwrapKey(sessionId,mechanism,"NSS_ENC",wrappedSecretKeyWithNssPublic,aesKeyTemplate);
            System.out.println("testWrapWithNSS_UnwrapWithSmartCard-Completed success");

        }
        catch(Exception aEx)
        {
        System.out.println("unwrapde hata");
            aEx.printStackTrace();
        }
        finally {

                try {
                  //  smartCard.closeSession(sessionId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
    }


    public void testWrapWithNSS_UnwrapWithSmartCard2() throws Exception{

        SmartCard smartCard = null;
        long sessionId = 0;
        try {

            NSSCryptoProvider cryptoProvider = NSSTestUtil.constructProvider(null);
            Crypto.setProvider(cryptoProvider);
            KeyFactory keyFactory = Crypto.getKeyFactory();

            String encKeyLabel = "encKeyLabel-"+System.currentTimeMillis();
            SecretKeySpec secretKeySpec= new SecretKeySpec(CipherAlg.AES256_CBC,encKeyLabel,256);
            SecretKey secretKey = keyFactory.generateSecretKey(secretKeySpec);


            Pair<Pair<SmartCard, Long>, List<Pair<String, PublicKey>>> pair = NSSTestUtil.readPublicKeyForEnc(1);
            smartCard= pair.getObject1().getObject1();
            sessionId = pair.getObject1().getObject2();
            List<Pair<String, PublicKey>> eCertList = pair.getObject2();


            Wrapper wrapper = Crypto.getWrapper(WrapAlg.RSA_PKCS1);
            wrapper.init(eCertList.get(0).getObject2(),null);

            KeyTemplate secretKeyTemplate = KeyTemplateFactory.getKeyTemplate(encKeyLabel, secretKey);
            byte[] wrappedSecretKeyWithNssPublic = wrapper.wrap(secretKeyTemplate);

            String label = "cmskey-" + System.currentTimeMillis();
            AESKeyTemplate aesKeyTemplate = (AESKeyTemplate) new AESKeyTemplate(label).getAsDecryptorTemplate();
            CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);

            smartCard.unwrapKey(sessionId,mechanism,"wrapper_rsa2",wrappedSecretKeyWithNssPublic,aesKeyTemplate);
            System.out.println("testWrapWithNSS_UnwrapWithSmartCard-Completed success");

        }
        catch(Exception aEx)
        {
            System.out.println("unwrapde hata");
            aEx.printStackTrace();
        }
        finally {

            try {
                //  smartCard.closeSession(sessionId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /*public void testWrapWithNSS_UnwrapWithSmartCard_StatikKey() throws Exception{

        SmartCard smartCard = null;
        long sessionId = 0;
        try {
            KeyFactory keyFactoryGNU = Crypto.getKeyFactory();
            String tmpAesKeyLabel = "tmpPersistAES"+System.currentTimeMillis();
            byte[] tmpKeyByte = keyFactoryGNU.generateKey(CipherAlg.AES256_CBC, 256);
            AESSecretKey tmpPersistAES = new AESSecretKey(tmpAesKeyLabel,tmpKeyByte);

            NSSCryptoProvider cryptoProvider = NSSTestUtil.constructProvider(null);
            Crypto.setProvider(cryptoProvider);
            KeyFactory keyFactory = Crypto.getKeyFactory();

            SmartCard nssSmartCard = cryptoProvider.getNssSoftToken();
            Long nssSessionId = cryptoProvider.getSessionID();
            nssSmartCard.importSecretKey(nssSessionId, tmpPersistAES);

            Pair<Pair<SmartCard, Long>, List<Pair<String, PublicKey>>> pair = NSSTestUtil.readPublicKeyForEnc(1);
            smartCard= pair.getObject1().getObject1();
            sessionId = pair.getObject1().getObject2();
            List<Pair<String, PublicKey>> eCertList = pair.getObject2();

            Wrapper wrapper = Crypto.getWrapper(WrapAlg.RSA_PKCS1);
            wrapper.init(eCertList.get(0).getObject2(),null);
            //KeyTemplate secretKeyTemplate = KeyTemplateFactory.getKeyTemplate(tmpAesKeyLabel,);
            KeyTemplate secretKeyTemplate = new AESKeyTemplate(tmpAesKeyLabel,tmpKeyByte);
            //NSS ile wrap yaptık.
            byte[] nssWrapped = wrapper.wrap(secretKeyTemplate);

            String label = "cmskey-" + System.currentTimeMillis();
            AESKeyTemplate aesKeyTemplate = (AESKeyTemplate) new AESKeyTemplate(label).getAsDecryptorTemplate();
            CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);

            smartCard.importSecretKey(sessionId,tmpPersistAES);
            String[] encryptionKeyLabels = smartCard.getEncryptionKeyLabels(sessionId);
            byte[] ncipherWrapped = smartCard.wrapKey(sessionId, mechanism, encryptionKeyLabels[0], tmpAesKeyLabel);

            System.out.println("testWrapWithNSS_UnwrapWithSmartCard_StatikKey-Completed success");

        } finally {

            try {
                smartCard.closeSession(sessionId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }*/


    public void testWrapWithSmartCard_UnwrapWithNSS() throws Exception{

        SmartCard smartCard = null;
        long sessionId = 0;
        try {
            NSSCryptoProvider nssProvider = NSSTestUtil.constructProvider(null);
            Crypto.setProvider(nssProvider);

            String labelOfKeyToWrap = "encKeyLabel-"+System.currentTimeMillis();

            Pair<Pair<SmartCard,Long>, List<Pair<String,PublicKey>>> pair = NSSTestUtil.readPublicKeyForEnc(1);
            smartCard= pair.getObject1().getObject1();
            sessionId = pair.getObject1().getObject2();
            List<Pair<String,PublicKey>> eCardKeyInfoList = pair.getObject2();

            //Smartcard içinde HMAC anahtarı oluştur.
            HMACKeyTemplate hmacKeyTemplate = new HMACKeyTemplate(labelOfKeyToWrap,16);
            hmacKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE,true));
            smartCard.createSecretKey(sessionId,hmacKeyTemplate);

            byte[] scHmacData = smartCard.signData(sessionId, labelOfKeyToWrap, "test".getBytes(), new CK_MECHANISM(PKCS11Constants.CKM_SHA256_HMAC));
            System.out.println("Smart ile mac degeri = "+StringUtil.toString(scHmacData));

            KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 1024);
            PublicKey nssPublicKey = keyPair.getPublic();
            PrivateKey nssPrivateKey = keyPair.getPrivate();

            String wrapperKeyLabel = "nssPublic-"+System.currentTimeMillis();
            RSAPublicKeyTemplate publicKeyTemplate = new RSAPublicKeyTemplate(wrapperKeyLabel, (RSAPublicKey) nssPublicKey);
            publicKeyTemplate = publicKeyTemplate.getAsWrapperTemplate();
            RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(publicKeyTemplate,null);
            smartCard.importKeyPair(sessionId,rsaKeyPairTemplate);

            CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);

            byte[] smartCardWrapped = smartCard.wrapKey(sessionId,mechanism, wrapperKeyLabel,labelOfKeyToWrap);

            Wrapper unwrapper = Crypto.getUnwrapper(WrapAlg.RSA_ECB_PKCS1);
            unwrapper.init(nssPrivateKey);

            String unwrappedKeyLabel = "nssUnwrappedHMAC-"+System.currentTimeMillis();
            HMACKeyTemplate nssHMacKeyTemplate = new HMACKeyTemplate(unwrappedKeyLabel, 16);
            nssHMacKeyTemplate.setKeyType(PKCS11Constants.CKK_AES);

            Key secretUnwrapped = unwrapper.unwrap(smartCardWrapped, nssHMacKeyTemplate.getAsSignerTemplate());
            System.out.println("testWrapWithSmartCard_UnwrapWithNSS-Completed success");


            //NSS içinde bu anahtarla MAC işlemi yapıyoruz.


            Long nssSessionId = nssProvider.getSessionID();
            SmartCard nssSmartCard = nssProvider.getNssSoftToken();
            byte[] nssHMAcData = nssSmartCard.signData(nssSessionId, unwrappedKeyLabel, "test".getBytes(), new CK_MECHANISM(PKCS11Constants.CKM_SHA256_HMAC));
            System.out.println("NSS ile mac degeri(Smart Card) = "+StringUtil.toString(nssHMAcData));

            MAC nssMac = Crypto.getMAC(MACAlg.HMAC_SHA256);
            nssMac.init(secretUnwrapped,null);
            nssHMAcData = nssMac.doFinal("test".getBytes());
            System.out.println("NSS ile mac degeri = "+StringUtil.toString(nssHMAcData));


            Wrapper wrapper = Crypto.getWrapper(WrapAlg.RSA_PKCS1);
            wrapper.init(nssPublicKey,null);
            KeyTemplate nssWrappedKeyTemplate = KeyTemplateFactory.getKeyTemplate(unwrappedKeyLabel,secretUnwrapped);

            byte[] nssWrapped = wrapper.wrap(nssWrappedKeyTemplate);
            boolean equals = Arrays.equals(scHmacData,nssHMAcData);
            if(equals){
                System.out.println("wrap halleri aynı degil");
            }else{
                System.out.println("wrap halleri aynı degil");
            }
        } finally {
            try {
                smartCard.closeSession(sessionId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void testWrapWithSmartCard_UnwrapWithSmartCard() throws Exception{

        NSSCryptoProvider cryptoProvider = NSSTestUtil.constructProvider(null);
        Crypto.setProvider(cryptoProvider);
        SmartCard smartCard = null;
        long sessionId = 0;

        try {

            String labelOfKeyToWrap = "encKeyLabel-"+System.currentTimeMillis();

            Pair<Pair<SmartCard,Long>, List<Pair<String,PublicKey>>> pair = NSSTestUtil.readPublicKeyForEnc(1);
            smartCard= pair.getObject1().getObject1();
            sessionId = pair.getObject1().getObject2();
            List<Pair<String,PublicKey>> eCardKeyInfoList = pair.getObject2();
            Pair<String, PublicKey> publicKeyInfo = eCardKeyInfoList.get(0);

            SecretKeyTemplate secretKeyTemplate = new AESKeyTemplate(labelOfKeyToWrap,16);
            smartCard.createSecretKey(sessionId,secretKeyTemplate);

            CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);
            String unwrapperKeyLabel = publicKeyInfo.getObject1();


            byte[] certValue = Base64.decode("MIIFfjCCBGagAwIBAgICRgIwDQYJKoZIhvcNAQELBQAwGDEKMAgGA1UEBRMBYjEKMAgGA1UEAwwB" +
                    "YTAgFw0xMzEwMDIxMTM1NDNaGA8yMDk3MDIwMjEyMzU0M1owLTEKMAgGA1UEBRMBYjEfMB0GA1UE" +
                    "AwwWYUVuY3J5cHRpb25DZXJ0aWZpY2F0ZTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB" +
                    "AKb/5Wj3RLgR9OMZW7y/eogRCOOrs4ZoHKfGa/PjuuK4fbCAuhOe8uXw/mEI/ugxtrNaP9SHsazT" +
                    "RhoZ2PUQzFDq4sHfrURpI+RVTZtshQG/yyczgXACEeB5UDzu9sZhdDz58xsTFiXVnrURLvdH08XE" +
                    "HlIVFl0CBNxV/91pi5/MwVarTgtRLYaUF5wvl9oW0kBLQgKL+bQML698WS4wSOpd2BzL/UaL2H+d" +
                    "CAHtd7/Sx5n8lHqkGM4eCdUl0gknGBflsiQbUgGHXkfdr9Gknh75Bx9fPdbaz4VviFg42A290olL" +
                    "uKuf1JD826MpY1ldK1HmxiC6ro+O28muVp2A26kCAwEAAaOCArkwggK1MB8GA1UdIwQYMBaAFF7S" +
                    "PMx17KhVGmyHozTQjnDb0+U/MB0GA1UdDgQWBBSMeYk6KsWa1qgsKUWLsE+D1U8FtzAOBgNVHQ8B" +
                    "Af8EBAMCBSAwggE0BgNVHSAEggErMIIBJzCCASMGC2CGGAECAQEFBwEBMIIBEjAvBggrBgEFBQcC" +
                    "ARYjaHR0cDovL3d3dy50ZXN0c20ubmV0LnRyL1RFU1RTTV9TVUUwgd4GCCsGAQUFBwICMIHRHoHO" +
                    "AEIAdQAgAHMAZQByAHQAaQBmAGkAawBhACwAIAAuAC4ALgAuACAAcwBhAHkBMQBsATEAIABFAGwA" +
                    "ZQBrAHQAcgBvAG4AaQBrACABMABtAHoAYQAgAEsAYQBuAHUAbgB1AG4AYQAgAGcA9gByAGUAIABv" +
                    "AGwAdQFfAHQAdQByAHUAbABtAHUBXwAgAG4AaQB0AGUAbABpAGsAbABpACAAZQBsAGUAawB0AHIA" +
                    "bwBuAGkAawAgAHMAZQByAHQAaQBmAGkAawBhAGQBMQByAC4wCQYDVR0TBAIwADB0BgNVHR8EbTBr" +
                    "MCygKqAohiZodHRwOi8vd3d3LnRlc3RzbS5uZXQudHIvVEVTVFNNU0lMLmNybDA7oDmgN4Y1bGRh" +
                    "cDovL2RpemluLnRlc3RzbS5nb3YudHIvQz1UUixPPVRFU1RTTSxDTj1URVNUU01TSUwwgakGCCsG" +
                    "AQUFBwEBBIGcMIGZMC8GCCsGAQUFBzAChiNodHRwOi8vd3d3LnRlc3RzbS5uZXQudHIvVEVTVFNN" +
                    "LmNydDA+BggrBgEFBQcwAoYybGRhcDovL2RpemluLnRlc3RzbS5uZXQudHIvQz1UUixPPVRFU1RT" +
                    "TSxDTj1URVNUU00wJgYIKwYBBQUHMAGGGmh0dHA6Ly9vY3NwLnRlc3RzbS5uZXQudHIvMA0GCSqG" +
                    "SIb3DQEBCwUAA4IBAQByp+QrwTQw/MorSxhL4199RTOTAGaC7MdN4eys5XzMwVxHYNLWnDyLZz3I" +
                    "ArySvZOY6tQ5D7vPWusYDhBtNs75iPqoqY/ESU2aXmuLpYohi5Y1jap43OuGAJvF+qw8D8vFfGTp" +
                    "wd0wTNl6aXyXxhFD8m8Q8VFdtWDagl/qLXioZ2JTIk/taaPb/Klp616ER7uqU73VEGNWpGpMy1FN" +
                    "RGD+EHFc6NhQlX2S6Olee5obO/Ge6A7Bm3fmAqMynfbin6hqYCmBtBLnHftkdZyg0bbW1V0Ar7ya" +
                    "VSLRGfuzLrSmhm8/MFaSsoZQ1iMvnqttjLYgyvFaszKeqrFCZNzWXJ6H");

            ECertificate eCertificate=new ECertificate(certValue);
            PublicKey publicKey = KeyUtil.decodePublicKey(eCertificate.getSubjectPublicKeyInfo());

            String wrapperKeyLabel = "tmpRecipientPublic-"+System.currentTimeMillis();
            RSAPublicKeyTemplate publicKeyTemplate = new RSAPublicKeyTemplate(wrapperKeyLabel, (RSAPublicKey) publicKey);
            publicKeyTemplate = publicKeyTemplate.getAsWrapperTemplate();
            RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(publicKeyTemplate,null);
            smartCard.importKeyPair(sessionId,rsaKeyPairTemplate);

            byte[] wrappedTmpKey = smartCard.wrapKey(sessionId,mechanism, wrapperKeyLabel,labelOfKeyToWrap);


            String label = "cmskey-" + System.currentTimeMillis();
            AESKeyTemplate aesKeyTemplate = (AESKeyTemplate) new AESKeyTemplate(label).getAsDecryptorTemplate();
            smartCard.unwrapKey(sessionId,mechanism,unwrapperKeyLabel,wrappedTmpKey,aesKeyTemplate);
            System.out.println("testWrapWithSmartCard_UnwrapWithSmartCard-Completed success");

        } finally {

            try {
                smartCard.closeSession(sessionId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
