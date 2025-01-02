package tr.gov.tubitak.uekae.esya.api;


import junit.framework.TestCase;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.asn.pkcs1pkcs8.RSAPublicKey;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;


public class GemplusECCardTest extends TestCase {

    private long userSlotNumber;
    private SmartCard userCard;
    private long userSessionID;
    private RSAPublicKey userCardWrapPublicKey;
    private String userCardWrapKeyLabel;

    public void testGenerateRSA2048SignKeyInUserCard() throws Exception {
        login();
        String keyLabel = "rsaSignKey-"+System.currentTimeMillis();
        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel, spec);
        rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);

        userCard.createKeyPair(userSessionID, rsaKeyPairTemplate);
        // read PK_WRAP
        RSAPublicKeySpec keySpec = (RSAPublicKeySpec) userCard.readPublicKeySpec(userSessionID, userCardWrapKeyLabel);
        userCardWrapPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
        testAccesibilityOfSessionKey(userCardWrapKeyLabel);
    }

    public void testGenerateRSA2048SessionSignKeyInUserCard() throws Exception {
        login();
        String keyLabel = "rsaSignKey-"+System.currentTimeMillis();
        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
        RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel, spec);
        rsaKeyPairTemplate.getAsExtractableTemplate();

        userCard.createKeyPair(userSessionID, rsaKeyPairTemplate);
        // read PK_WRAP
        RSAPublicKeySpec keySpec = (RSAPublicKeySpec) userCard.readPublicKeySpec(userSessionID, userCardWrapKeyLabel);
        userCardWrapPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
        testAccesibilityOfSessionKey(userCardWrapKeyLabel);
    }

    public void testGenerateSessionKeyInUserCard() throws Exception {
        login();
        tryToDeleteOldObjects(userCardWrapKeyLabel);

        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);

        {     // **** KP_WRAP : Create Wrap/Unwrap Key Pair in User Token

            userCard.createKeyPair(userSessionID, new RSAKeyPairTemplate(userCardWrapKeyLabel, spec).getAsWrapperTemplate());
            // read PK_WRAP
            RSAPublicKeySpec keySpec = (RSAPublicKeySpec) userCard.readPublicKeySpec(userSessionID, userCardWrapKeyLabel);
            userCardWrapPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);

            testAccesibilityOfSessionKey(userCardWrapKeyLabel);
        }
    }

    private void testAccesibilityOfSessionKey(String keyLabel) throws Exception {
        CK_ATTRIBUTE[] tx =
                {
                        new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_PRIVATE_KEY),
                        new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL,keyLabel)
                };
        long[] longs = userCard.objeAra(userSessionID, tx);
        if (longs != null && longs.length > 0) {
            CK_ATTRIBUTE[] rsaPublicKeyTemplate =
                    {
                            new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE_EXPONENT),
                            new CK_ATTRIBUTE(PKCS11Constants.CKA_MODULUS)
                    };
            try {
                userCard.getAttributeValue(userSessionID, longs[0], rsaPublicKeyTemplate);
                throw new Exception("Fatal: We read Private Key....");
            } catch (PKCS11Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private void login() throws PKCS11Exception, IOException, SmartCardException {
        userSlotNumber = SmartOp.findSlotNumber(CardType.GEMPLUS, "User");
        System.out.println("userSlotNumber:" + userSlotNumber);

        userCard = new SmartCard(CardType.GEMPLUS);
        userSessionID = userCard.openSession(userSlotNumber);
        userCard.login(userSessionID, "1234");
    }


    private void tryToDeleteOldObjects(String keyLabel) {
        try {
            userCard.deletePrivateObject(userSessionID, keyLabel);
            userCard.deletePublicObject(userSessionID, keyLabel);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
