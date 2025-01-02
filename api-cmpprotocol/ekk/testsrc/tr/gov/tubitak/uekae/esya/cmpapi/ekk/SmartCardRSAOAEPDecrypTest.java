package tr.gov.tubitak.uekae.esya.cmpapi.ekk;

import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCCipherWithKeyLabel;

import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.util.Arrays;

/**
 * Created by ramazan.girgin on 4/11/2014.
 */
public class SmartCardRSAOAEPDecrypTest {
    private static void testSafeNetLUNAHSM_OEAEP_Decrypt() throws Exception {
        long slot=1;
        SmartCard smartCard = new SmartCard(CardType.SAFENET);
        long sessionId = smartCard.openSession(slot);
        smartCard.login(sessionId,"12345678");

        String userKeyLabel ="rsaoaep_test_enc";

        KeySpec keySpec = smartCard.readPublicKeySpec(sessionId, userKeyLabel);

        PublicKey publicKey = KeyUtil.generatePublicKey(keySpec);


        BufferedCipher cipher = Crypto.getEncryptor(CipherAlg.RSA_OAEP);

        //OAEPParameterSpec oaepParameterSpec=new OAEPParameterSpec("SHA-256","MGF1",MGF1ParameterSpec.SHA256,PSource.PSpecified.DEFAULT);
        cipher.init(publicKey, null);

        byte[] simAnahtar = RandomUtil.generateRandom(32);

        byte[] encryptedData = cipher.doFinal(simAnahtar);

        SCCipherWithKeyLabel scCipherWithKeyLabel = new SCCipherWithKeyLabel(smartCard,sessionId,slot,userKeyLabel, CipherAlg.RSA_OAEP.getName(),null);
        byte[] decryptedData = scCipherWithKeyLabel.doFinal(encryptedData);
        smartCard.closeSession(sessionId);
        boolean isEquals = Arrays.equals(simAnahtar, decryptedData);
        if(!isEquals){
            throw new Exception("Çözülen veri düzgün değil.");
        }
        System.out.println("LUNAHSM-Veri düzgün şekilde çözüldü.");
    }

    private static void testUtimacoFizikHSM_OEAEP_Decrypt() throws Exception {
        long slot=0;
        SmartCard smartCard = new SmartCard(CardType.UTIMACO);
        long sessionId = smartCard.openSession(slot);
        smartCard.login(sessionId,"123456");

        String userKeyLabel ="rsaoaep_test";

        KeySpec keySpec = smartCard.readPublicKeySpec(sessionId, userKeyLabel);

        PublicKey publicKey = KeyUtil.generatePublicKey(keySpec);


        BufferedCipher cipher = Crypto.getEncryptor(CipherAlg.RSA_OAEP);
        cipher.init(publicKey, null);

        byte[] simAnahtar = RandomUtil.generateRandom(32);

        byte[] encryptedData = cipher.doFinal(simAnahtar);

        SCCipherWithKeyLabel scCipherWithKeyLabel = new SCCipherWithKeyLabel(smartCard,sessionId,slot,userKeyLabel, CipherAlg.RSA_OAEP.getName(),null);
        byte[] decryptedData = scCipherWithKeyLabel.doFinal(encryptedData);
        smartCard.closeSession(sessionId);
        boolean isEquals = Arrays.equals(simAnahtar, decryptedData);
        if(!isEquals){
            throw new Exception("Çözülen veri düzgün değil.");
        }
        System.out.println("UTIMACO-Veri düzgün şekilde çözüldü.");

    }

    private static void testWrapUnwrap() throws Exception {
        long slot=1;
        SmartCard smartCard = new SmartCard(CardType.SAFENET);
        long sessionId = smartCard.openSession(slot);
        smartCard.login(sessionId,"12345678");

        String userKeyLabel ="rsaoaep_test_enc";

        //Kart içerisinden bir wrap public key oluştur.

        KeySpec publicKeySpec = smartCard.readPublicKeySpec(sessionId, userKeyLabel);
        PublicKey publicKey = KeyUtil.generatePublicKey(publicKeySpec);

        /*
            String pubKeyStr = "30819F300D06092A864886F70D010101050003818D0030818902818100BBC27EA70097239D4ADAF36923B5A4C23388042CDAC1B2F5C769421615B756DD8B39CAD2A8E2B6C98193B61E8E638E35E3ABD9474BBD9E614AD20F98FC5F4739C9FDBEDEB30C3219A6DDD2EC2FA7BF31F652CFC23401B2DE62F3B749686B7BBAB77912F11591300195C3A46048146FA717E124ED2A2BC643FC3AD5058F8C60510203010001";
            byte[] pubKeyBytes = StringUtil.toByteArray(pubKeyStr);
            PublicKey protEncKeyRSA = KeyUtil.decodePublicKey(AsymmetricAlg.RSA, pubKeyBytes);
            String protEncKeyLabel = "protEncKeyLabel"+System.currentTimeMillis();
            RSAPublicKeyTemplate asWrapperTemplate = new RSAPublicKeyTemplate(protEncKeyLabel, (RSAPublicKey) publicKey).getAsWrapperTemplate();
            smartCard.importKeyPair(sessionId, new RSAKeyPairTemplate(asWrapperTemplate, null) );
        */

        String keyWrapSecretLabel = "keyWrapSecret-"+System.currentTimeMillis();
        int simKeyLenght=16;
        SecretKeyTemplate template = new AESKeyTemplate(keyWrapSecretLabel, simKeyLenght).getAsWrapperTemplate().getAsExportableTemplate();
        smartCard.createSecretKey(sessionId, template);

        byte[] wrappedKey = SmartOp.wrap(smartCard, sessionId, slot, CipherAlg.RSA_OAEP.getName(), userKeyLabel, keyWrapSecretLabel, null);
        /*
        String destUnwrappedKeyLabel = "importedKey-"+System.currentTimeMillis();
        AESKeyTemplate destUnwrapperTemplate = new AESKeyTemplate(destUnwrappedKeyLabel, 16);
        destUnwrapperTemplate.add(new CK_ATTRIBUTE(CKA_TOKEN,true));
        SmartOp.unwrap(smartCard,sessionId,slot, CipherAlg.RSA_OAEP.getName(),labelWrapperSecretKey,wrappedKey,destUnwrapperTemplate,null);
         */

    }

    public static void main(String[] args) throws Exception {
        //testWrapUnwrap();
        try {
            testSafeNetLUNAHSM_OEAEP_Decrypt();
        }catch (Exception exc){
            exc.printStackTrace();
        }

        try {
            testUtimacoFizikHSM_OEAEP_Decrypt();
        }catch (Exception exc){
            exc.printStackTrace();
        }
    }
}
