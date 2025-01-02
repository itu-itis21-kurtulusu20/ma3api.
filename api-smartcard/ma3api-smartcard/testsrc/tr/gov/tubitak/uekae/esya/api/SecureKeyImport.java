package tr.gov.tubitak.uekae.esya.api;








import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;




import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;




import java.security.KeyFactory;
import java.security.PublicKey;




import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;


/**
 * <b>Author</b>    : zeldal.ozdemir <br/>
 * <b>Project</b>   : MA3   <br/>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br/>
 * <b>Date</b>: 4/30/12 - 7:32 PM <p/>
 * <b>Description</b>: <br/>
 */
public class SecureKeyImport {

  /*  private static final String kp_wrap = "kp_wrap";
    private static final String s_wrap = "s_wrap";
    private static final String s_wrap_token = "s_wrap_token";
    private static final String userKey = "UserKey";
    private static final String userKey_token = "UserKey_Token";

    SmartCard hsm = null,userCard = null;
    long hsmSessionID = -1,userSessionID = -1;
    private  long hsmSlotNumber = 761406616;
    private  long userSlotNumber = 761406617;
    private RSAPublicKey pk_wrap;
    private RSAPublicKey pk_userToken;
    private byte[] pk_wrap_s_wrap;  // s_wrap(Symmetric Wrapping Key) wrapped by pk_wrap
    private byte[] s_wrap_userkey;  // userKey(User Token Private Key) wrapped by s_wrap
    private byte[] iv = RandomUtil.generateRandom(16);   // iv for  CKM_AES_CBC_PAD to create s_wrap_userkey


    public static void main(String[] args) {
        new SecureKeyImport().test();
//        new SecureKeyImport().speedtest();
    }

    public void test() {

        try {

            login();
            tryToDeleteOldObjects();


            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(1024, null);


            {     // **** KP_WRAP : Create Wrap/Unwrap Key Pair in User Token

                userCard.createKeyPair(userSessionID, new RSAKeyPairTemplate(kp_wrap, spec).getAsWrapperTemplate());
//                userCard.createWrappingKeyPair(userSessionID, kp_wrap, new RSAKeyGenParameterSpec(1024, null));
                // read PK_WRAP
                RSAPublicKeySpec keySpec = (RSAPublicKeySpec) userCard.readPublicKeySpec(userSessionID, kp_wrap);
                pk_wrap = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);

            }
            { // HSM Side
                // import PK_WRAP into hsm
                hsm.importKeyPair(hsmSessionID, new RSAKeyPairTemplate(new RSAPublicKeyTemplate(kp_wrap,pk_wrap).getAsWrapperTemplate(), null) );
//                hsm.importWrappingRSAPublicKey(hsmSessionID, kp_wrap, pk_wrap);
                SecretKeyTemplate template = new AESKeyTemplate(s_wrap, 16).getAsWrapperTemplate();
                hsm.createSecretKey(hsmSessionID, template);
//                hsm.createWrappingSecretKey(hsmSessionID, new AESSecretKey(s_wrap, 16));
                pk_wrap_s_wrap = hsm.wrapKey(hsmSessionID, new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS) , kp_wrap, s_wrap);

                hsm.createKeyPair(hsmSessionID,new RSAKeyPairTemplate(userKey,spec).getAsExtractableTemplate());
//                hsm.createExtractableKeyPair(hsmSessionID, userKey, new RSAKeyGenParameterSpec(1024, null));
                {     // create User Token Wrap/Unwrap Key Pair  kp_wrap
                    RSAPublicKeySpec keySpec = (RSAPublicKeySpec) hsm.readPublicKeySpec(hsmSessionID, userKey);
                    pk_userToken = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
                }
                s_wrap_userkey = hsm.wrapKey(hsmSessionID, new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, iv), s_wrap, userKey);
            }


            testAccesibilityOfKeys();



            KeyTemplate template = new AESKeyTemplate(s_wrap_token, 16).getAsUnwrapperTemplate();
            userCard.unwrapKey(userSessionID, new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS), kp_wrap, pk_wrap_s_wrap, template);

            RSAPublicKeyTemplate userPublicKeyTemplate = new RSAPublicKeyTemplate(userKey_token, pk_userToken);
            userPublicKeyTemplate.getAsTokenTemplate(false,true);   // convert to user token
            userCard.importKeyPair(userSessionID,new RSAKeyPairTemplate(userPublicKeyTemplate, null));


            RSAPrivateKeyTemplate tokenTemplate = new RSAPrivateKeyTemplate(userKey_token, null).getAsTokenTemplate(false, true);
            userCard.unwrapKey(userSessionID, new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC, iv), s_wrap_token, s_wrap_userkey, tokenTemplate);

            System.out.println("User Key Transferred successfully");

            byte[] encrypted = CipherUtil.encrypt(CipherAlg.RSA_PKCS1, null, "test".getBytes(), pk_userToken);
            byte[] test =SmartOp.decrypt(userCard, userSessionID, userSlotNumber, userKey_token, encrypted, CipherAlg.RSA_PKCS1.getName(), null);


            System.out.println("Verification Result:"+new String(test));
//            userCard.deletePrivateObject(userSessionID,s_wrap);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }  finally {
            try {
                hsm.logout(hsmSessionID);
            } catch (PKCS11Exception e) {
                e.printStackTrace();
            }
            try {
                userCard.logout(userSessionID);
            } catch (PKCS11Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void testAccesibilityOfKeys() throws Exception {
        CK_ATTRIBUTE[] tx =
                {
                        new CK_ATTRIBUTE(CKA_CLASS, CKO_PRIVATE_KEY),
                        new CK_ATTRIBUTE(CKA_TOKEN, true),
                        new CK_ATTRIBUTE(CKA_LABEL, userKey)
                };
        long[] longs = hsm.objeAra(hsmSessionID, tx);
        if (longs != null && longs.length > 0) {
            CK_ATTRIBUTE[] rsaPublicKeyTemplate =
                    {
                            new CK_ATTRIBUTE(CKA_PRIVATE_EXPONENT),
                            new CK_ATTRIBUTE(CKA_MODULUS)
                    };
            try {
                hsm.getAttributeValue(hsmSessionID, longs[0], rsaPublicKeyTemplate);
                throw new Exception("Fatal: We read Private Key....");
            } catch (PKCS11Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private void tryToDeleteOldObjects() {
        try {
            userCard.deletePrivateObject(userSessionID, kp_wrap);
            userCard.deletePublicObject(userSessionID, kp_wrap);
            hsm.deletePublicObject(hsmSessionID, kp_wrap);
            hsm.deletePrivateObject(hsmSessionID, s_wrap);
            hsm.deletePrivateObject(hsmSessionID, userKey);
            hsm.deletePublicObject(hsmSessionID, userKey);
            userCard.deletePrivateObject(userSessionID, s_wrap_token);
            userCard.deletePublicObject(userSessionID, userKey_token);
            userCard.deletePrivateObject(userSessionID, userKey_token);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void login() throws PKCS11Exception, IOException, SmartCardException {
        hsmSlotNumber = SmartOp.findSlotNumber(CardType.NCIPHER,"HSM");
        userSlotNumber = SmartOp.findSlotNumber(CardType.NCIPHER,"User");
        System.out.println("hsmSlotNumber:"+hsmSlotNumber);
        System.out.println("userSlotNumber:"+userSlotNumber);

        hsm = new SmartCard(CardType.NCIPHER);
        hsmSessionID = hsm.openSession(hsmSlotNumber);
        hsm.login(hsmSessionID, "123456");

        userCard = new SmartCard(CardType.NCIPHER);
        userSessionID = userCard.openSession(userSlotNumber);
        userCard.login(userSessionID, "123456");
    }
    */

    public static void createPublicKey(SmartCard hsm,long hsmSlotNumber,long hsmSessionID,String keyLabel) throws Exception{


        try{
            //  hsm.deletePrivateObject();
            hsm.deletePublicObject(hsmSessionID, keyLabel);
            hsm.deletePrivateObject(hsmSessionID, keyLabel);
        }catch (Exception exc){
            exc.printStackTrace();
        }

        String[] signatureKeyLabels = hsm.getSignatureKeyLabels(hsmSessionID);

        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(1024, null);
        hsm.createKeyPair(hsmSessionID,new RSAKeyPairTemplate(keyLabel,spec).getAsExtractableTemplate());

        signatureKeyLabels = hsm.getSignatureKeyLabels(hsmSessionID);
        for (String signatureKeyLabel : signatureKeyLabels) {
            System.out.println("Signature Key :"+signatureKeyLabel);
        }

        String[] encryptionKeyLabels = hsm.getEncryptionKeyLabels(hsmSessionID);
        encryptionKeyLabels = hsm.getEncryptionKeyLabels(hsmSessionID);
        for (String encryptionKeyLabel : encryptionKeyLabels) {
            System.out.println("Encryption Key :"+encryptionKeyLabel);
        }

        RSAPublicKeySpec keySpec = (RSAPublicKeySpec) hsm.readPublicKeySpec(hsmSessionID,keyLabel);
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
        System.out.println(publicKey);
    }

    public static void main(String[] args) throws Exception{
        long hsmSlotNumber = 761406617;//SmartOp.findSlotNumber(CardType.NCIPHER, "HSM");
        System.out.println("hsmSlotNumber:"+hsmSlotNumber);
        SmartCard hsm = new SmartCard(CardType.NCIPHER);
        long hsmSessionID = hsm.openSession(hsmSlotNumber);
        hsm.login(hsmSessionID, "123456");


        String userKey="userKeyPair";
        createPublicKey(hsm,hsmSlotNumber,hsmSessionID,userKey);
        createPublicKey(hsm,hsmSlotNumber,hsmSessionID,userKey);
        createPublicKey(hsm,hsmSlotNumber,hsmSessionID,userKey);




















































































































































































































    /*  RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(1024, null);
      int k=0;
      String userKey="userKeyPair";
      while(1==1){
            userKey+=k;
          //hsm.createKeyPair(hsmSessionID,new RSAKeyPairTemplate(userKey,spec).getAsExtractableTemplate());
            RSAPublicKeySpec keySpec = (RSAPublicKeySpec) hsm.readPublicKeySpec(hsmSessionID,userKey);
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
          System.out.println(publicKey);
            k++;
      } */
    }
}
