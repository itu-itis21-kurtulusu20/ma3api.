package dev.esya.api.cmsenvelope.smartcard;

import org.junit.Ignore;
import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import test.esya.api.cmsenvelope.TestData;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsKeyEnvelopeGenerator;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.CmsKeyEnvelopeParser;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.EnvelopeConfig;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.SCKeyUnwrapperStore;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.MAC;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.KeyAgreementAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.SecretKeySpec;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu.GNUCryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.NSSCryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.NSSLoader;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ISmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplateFactory;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.HMACKeyTemplate;

import javax.crypto.SecretKey;
import java.io.File;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ramazan.girgin
 * Date: 10/3/13
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
@Ignore("Smartcard tests")
public class TestSmartCardKeyEnveloped {

    @Test
    public void testGenerateMemoryECKeyAgreement_NSS() throws Exception {

        EName name = new EName("CN=turkeek,ğoğko,lıkjhkljkldf,oıjhoıer,ğoıjdf,*ııu*,ğpooı0*ı,oıuy89uy,-*9*7089,ujoı8,ı090923klkmndf",true);
        String s = name.stringValue();
        // NSS Ilklendir
        NSSCryptoProvider cryptoProvider = (NSSCryptoProvider) NSSLoader.loadPlatformNSS(System.getProperty("user.dir") + File.separator + "nssTemp", true);
        Crypto.setProvider(cryptoProvider);

        String dataForEncrypt="123456";

        //HSM içerisinde secret anahtarı oluştur.

        SmartCard nssSmartCard = cryptoProvider.getNssSoftToken();
        Long nssSessionId = cryptoProvider.getSessionID();

        CipherAlg symetricAlg = CipherAlg.AES256_CBC;
        
        ///

        String hmacKeyLabel = "nssHMACkey-"+System.currentTimeMillis();                
        SecretKeySpec secretKeySpec= new SecretKeySpec(CipherAlg.AES256_CBC,hmacKeyLabel,256);
        SecretKey hmacSecretKey = Crypto.getKeyFactory().generateSecretKey(secretKeySpec);
        KeyTemplate hmacKeyTemplate = KeyTemplateFactory.getKeyTemplate(hmacKeyLabel, hmacSecretKey);
        MAC nssMac = Crypto.getMAC(MACAlg.HMAC_SHA256);
        nssMac.init(hmacKeyTemplate,null);
        byte[] beforeMAC = nssMac.doFinal("test".getBytes());
        System.out.println("Before MAC = "+StringUtil.toString(beforeMAC));
        ///

        SecretKey userSecretKey = KeyUtil.generateSecretKey(symetricAlg, KeyUtil.getKeyLength(symetricAlg));

        System.out.println("dataForEncrypt Baştaki hali = "+ StringUtil.toString(dataForEncrypt.getBytes()));

        /*BufferedCipher encryptor = Crypto.getEncryptor(symetricAlg);
        byte[] ivBytes = RandomUtil.generateRandom(16);
        System.out.println("IV Baştaki hali = "+ StringUtil.toString(ivBytes));

        AlgorithmParams params = new ParamsWithIV(ivBytes);
        encryptor.init(userSecretKey, params);
        byte[] encryptedData = encryptor.doFinal(dataForEncrypt.getBytes());
        System.out.println("encryptedData Baştaki hali = "+ StringUtil.toString(encryptedData));
        */

        byte[] agreedKey;
        {//Wrap key with key agreement
            Pair<Pair<SmartCard, Long>, List<ECertificate>> smartCardCertInfo = readCertForEnc(1);
            Pair<SmartCard, Long> smartCardInfo = smartCardCertInfo.getObject1();
            List<ECertificate> certList = smartCardCertInfo.getObject2();

            SmartCard smartCard = smartCardInfo.getObject1();
            Long sessionId = smartCardInfo.getObject2();

            EnvelopeConfig config = new EnvelopeConfig();
            config.setEcKeyAgreementAlg(KeyAgreementAlg.ECDH_SHA512KDF);
            TestData.configureCertificateValidation(config);

            ECertificate recipientCert = certList.get(0);
            CmsKeyEnvelopeGenerator generator = new CmsKeyEnvelopeGenerator(hmacSecretKey,CipherAlg.AES256_ECB);
            generator.addRecipients(config,recipientCert);
            agreedKey = generator.generate();
            smartCard.closeSession(sessionId);
        }
        Key unwrappedKey=null;
        ISmartCard smartCard;
        Long sessionId;
        HMACKeyTemplate laterKeyTemplate=null;
        String unwrappedKeyLabel=null;
        {//Unwrap key
            Pair<Pair<SmartCard, Long>, List<ECertificate>> smartCardCertInfo = readCertForEnc(1);
            Pair<SmartCard, Long> smartCardInfo = smartCardCertInfo.getObject1();
            List<ECertificate> certList = smartCardCertInfo.getObject2();
            smartCard = smartCardInfo.getObject1();
            sessionId = smartCardInfo.getObject2();

            ECertificate eCertificate = certList.get(0);
            String[] encryptionKeyLabels = smartCard.getEncryptionKeyLabels(sessionId);
            String decrytorKeyLabel=encryptionKeyLabels[0];

            CmsKeyEnvelopeParser wrappedCmsParser = new CmsKeyEnvelopeParser(agreedKey);

            ECertificate [] certArray = new ECertificate[1];
            certArray[0]=eCertificate;

            /*
            String keyLabel =  "SystemEncrytionKey-"+System.currentTimeMillis();
            SecretKeyTemplate unwrappedKeyTemplate = new AESKeyTemplate(keyLabel);
            unwrappedKeyTemplate.getAsDecryptorTemplate();
            unwrappedKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN,true));
            unwrappedKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ENCRYPT,false));
            unwrappedKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DERIVE,false));
            */

            unwrappedKeyLabel = "nssUnwrappedHMAC-"+System.currentTimeMillis();
            laterKeyTemplate = new HMACKeyTemplate(unwrappedKeyLabel, 16);
            laterKeyTemplate.getAsSignerTemplate();
            laterKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN,true));
            laterKeyTemplate.setKeyType(PKCS11Constants.CKK_AES);

            SCKeyUnwrapperStore decrytorStore = new SCKeyUnwrapperStore(smartCard, sessionId, certArray, decrytorKeyLabel);

           unwrappedKey  = wrappedCmsParser.open(decrytorStore, laterKeyTemplate);
        }

        {
            KeyTemplate abc = KeyTemplateFactory.getKeyTemplate(unwrappedKeyLabel, unwrappedKey);
            MAC nssMac2 = Crypto.getMAC(MACAlg.HMAC_SHA256);
            nssMac2.init(abc,null);
            byte[] afterMAC = nssMac2.doFinal("test".getBytes());
            System.out.println("After MAC = "+StringUtil.toString(afterMAC));
        }
        /*
        {
            CK_MECHANISM aMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC);
            aMechanism.pParameter = ivBytes;
            byte[] decryptedData = smartCard.decryptData(sessionId, ((AESKeyTemplate) unwrappedKey).getLabel(), encryptedData, aMechanism);
            System.out.println("Şifresiz hali = "+ new String(decryptedData));
        } */
        /*
        {
            System.out.println("IV sonraki hali = "+ StringUtil.toString(ivBytes));
            System.out.println("encryptedData sonraki hali = "+ StringUtil.toString(encryptedData));
            BufferedCipher decryptor2 = Crypto.getDecryptor(symetricAlg);
            AlgorithmParams params2 = new ParamsWithIV(ivBytes);
            decryptor2.init(unwrappedKey, params2);
            byte[] decryptedData = decryptor2.doFinal(encryptedData);
            System.out.println("Şifresiz hali = "+ new String(decryptedData));
        }*/
    }

    @Test
    public void testGenerateMemoryECKeyAgreement_WithoutNSS() throws Exception {
        //GNU yu provider olarak belirleyelim
        Crypto.setProvider(new GNUCryptoProvider());

        Pair<Pair<SmartCard, Long>, List<ECertificate>> smartCardCertInfo = readCertForEnc(1);
        Pair<SmartCard, Long> smartCardInfo = smartCardCertInfo.getObject1();
        List<ECertificate> certList = smartCardCertInfo.getObject2();

        SmartCard smartCard = smartCardInfo.getObject1();
        Long sessionId = smartCardInfo.getObject2();

        CipherAlg sourceKeyAlg = CipherAlg.AES256_CBC;
        String labelOfKeyToWrap = "keyForCMS-"+System.currentTimeMillis();

        SecretKey secretKey = KeyUtil.generateSecretKey(sourceKeyAlg, KeyUtil.getKeyLength(sourceKeyAlg));

        byte[] plainData = "plainDataToTest".getBytes();

        EnvelopeConfig config = new EnvelopeConfig();
        config.setEcKeyAgreementAlg(KeyAgreementAlg.ECDH_SHA512KDF);
        TestData.configureCertificateValidation(config);

        ECertificate recipientCert = certList.get(0);
        CmsKeyEnvelopeGenerator generator = new CmsKeyEnvelopeGenerator(secretKey,CipherAlg.AES256_ECB);
        generator.addRecipients(config,recipientCert);
        byte[] wrappedData = generator.generate();
        smartCard.closeSession(sessionId);
    }

    public static Pair<Pair<SmartCard,Long>,List<ECertificate>> readCertForEnc(int count) throws Exception {

        SmartCard smartCard = null;
        long sessionID =-1;
        List<ECertificate> yonCertList = null;
        for (int i = 0; i < count; i++) {
            CardType cardType = CardType.UTIMACO;
            yonCertList = new ArrayList<ECertificate>();
            long slotNumber = 11;// /*761406617*/SmartOp.findSlotNumber(cardType);
            smartCard = new SmartCard(cardType);

            /*long[] mechanismList = smartCard.getMechanismList(slotNumber);
            for (long mechanismId : mechanismList) {
                tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.pkcs11wrapper.SCMechanism scMechanism = new tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.pkcs11wrapper.SCMechanism(mechanismId);
                boolean isKeyDerivationMechanism = scMechanism.isKeyGenerationMechanism();
                if(isKeyDerivationMechanism)
                    System.out.println(scMechanism.toString());
            } */

            sessionID = smartCard.openSession(slotNumber);
            smartCard.login(sessionID, "123456");
            List<byte[]> encryptionCertificates = smartCard.getEncryptionCertificates(sessionID);
            ECertificate eCertificate = null;
            for (byte[] encryptionCertificate : encryptionCertificates) {
                eCertificate = new ECertificate(encryptionCertificate);
            }
            yonCertList.add(eCertificate);
        }
        return new Pair<Pair<SmartCard, Long>, List<ECertificate>>(new Pair<SmartCard, Long>(smartCard,sessionID),yonCertList);
    }
}
