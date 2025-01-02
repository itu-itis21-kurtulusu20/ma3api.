package tr.gov.tubitak.uekae.esya.api.crypto;

import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithECParameterSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.NSSCryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.NSSLoader;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplateFactory;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECPrivateKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECPublicKeyTemplate;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ramazan.girgin
 * Date: 8/2/13
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class NSSTestUtil {
    static NSSCryptoProvider nssCryptoProvider;
    public static NSSCryptoProvider constructProvider(String nssPath){
        String nssLocalPath =System.getProperty("user.dir") + File.separator + "nssTemp";
        if(nssPath == null){
            try {
                NSSLoader.clearNSSDirectory(nssLocalPath);
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
        else
            nssLocalPath = nssPath;

        try {

            nssCryptoProvider = (NSSCryptoProvider) NSSLoader.loadPlatformNSS(nssLocalPath, true);

        } catch (Throwable t){
            t.printStackTrace();
        }
        return nssCryptoProvider;
    }



    public static Pair<Pair<SmartCard,Long>,List<ECertificate>> readCertForEnc(int count) throws Exception {

        SmartCard smartCard = null;
        long sessionID =-1;
        List<ECertificate> yonCertList = null;
        for (int i = 0; i < count; i++) {

            CardType cardType = CardType.NCIPHER;
            yonCertList = new ArrayList<ECertificate>();

            long slotNumber = SmartOp.findSlotNumber(cardType);
            smartCard = new SmartCard(cardType);
            sessionID = smartCard.openSession(slotNumber);
            smartCard.login(sessionID, "123456");
            List<byte[]> encryptionCertificates = smartCard.getEncryptionCertificates(sessionID);
            if(encryptionCertificates.size()>0){
                yonCertList.add(new ECertificate(encryptionCertificates.get(0)));
            }
        }
        return new Pair<Pair<SmartCard, Long>, List<ECertificate>>(new Pair<SmartCard, Long>(smartCard,sessionID),yonCertList);
    }

    public static Pair<Pair<SmartCard,Long>,List<Pair<String,PublicKey>>> readPublicKeyForEnc(int count) throws Exception {

        SmartCard smartCard = null;
        long sessionID =-1;
        List<Pair<String,PublicKey>> yonCertList = null;
        for (int i = 0; i < count; i++) {

            //CardType cardType = CardType.UTIMACO;
            CardType cardType = CardType.NCIPHER;
            yonCertList = new ArrayList<Pair<String,PublicKey>>();

            long slotNumber = SmartOp.findSlotNumber(cardType);
            smartCard = new SmartCard(cardType);
            sessionID = smartCard.openSession(slotNumber);
            smartCard.login(sessionID, "123456");

            //RSAKeyPairTemplate wrapper_rsa = new RSAKeyPairTemplate("wrapper_rsa2", new RSAKeyGenParameterSpec(1024, null)).getAsWrapperTemplate();
            //KeySpec publicKeySpec = smartCard.createKeyPair(sessionID, wrapper_rsa);

            //PublicKey publicKey = KeyUtil.generatePublicKey(publicKeySpec);
         //   String[] encryptionKeyLabels = smartCard.getEncryptionKeyLabels(sessionID);

          //  KeySpec publicKeySpec = smartCard.readPublicKeySpec(sessionID, "murat2test");
         //   PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);


            //yonCertList.add(new Pair<String,PublicKey>("wrapper_rsa2",publicKey));
            /*
            RSAKeyGenParameterSpec keyGenSpec = new RSAKeyGenParameterSpec(1024, null);
            KeyPairTemplate wrapperTemplate = new RSAKeyPairTemplate("abc", keyGenSpec).getAsWrapperTemplate();
            smartCard.createKeyPair(sessionID, wrapperTemplate);
            RSAPublicKeySpec keySpec = (RSAPublicKeySpec) smartCard.readPublicKeySpec(sessionID, "abc");
            PublicKey publicKey = (RSAPublicKey) java.security.KeyFactory.getInstance("RSA").generatePublic(keySpec);
            yonCertList.add(publicKey);
            */

            String[] encryptionKeyLabels = smartCard.getEncryptionKeyLabels(sessionID);
            RSAPublicKeySpec keySpec = (RSAPublicKeySpec) smartCard.readPublicKeySpec(sessionID, encryptionKeyLabels[0]);
            PublicKey publicKey = (RSAPublicKey) java.security.KeyFactory.getInstance("RSA").generatePublic(keySpec);

            yonCertList.add(new Pair<String,PublicKey>(encryptionKeyLabels[0],publicKey));
            /*List<byte[]> encryptionCertificates = smartCard.getEncryptionCertificates(sessionID);
            if(encryptionCertificates.size()>0){
                yonCertList.add(new ECertificate(encryptionCertificates.get(0)));
            }*/
        }
        return new Pair<Pair<SmartCard, Long>, List<Pair<String, PublicKey>>>(new Pair<SmartCard, Long>(smartCard,sessionID),yonCertList);
    }
}
