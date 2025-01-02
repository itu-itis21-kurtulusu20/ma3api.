package util;

import gnu.crypto.key.rsa.GnuRSAPublicKey;
import org.junit.Assert;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithECParameterSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPrivateKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by orcun.ertugrul on 17-Nov-17.
 */
public class CardTestUtil
{

    public static long getSlot(SmartCard sc) throws PKCS11Exception {
        long[] slots = sc.getTokenPresentSlotList();
        return slots[0];
    }

    public static void deleteKeyPair(SmartCard sc, long sid, String keyLabel) throws SmartCardException, PKCS11Exception{
        sc.deletePrivateObject(sid, keyLabel);
        sc.deletePublicObject(sid, keyLabel);

        boolean found1 = sc.isPrivateKeyExist(sid, keyLabel);
        boolean found2 = sc.isPublicKeyExist(sid, keyLabel);

        Assert.assertEquals(false, found1);
        Assert.assertEquals(false, found2);
    }

    public static void deleteKeyPairWithoutEx(SmartCard sc, long sid, String keyLabel) {
        try
        {
            boolean r1 = sc.isPrivateKeyExist(sid, keyLabel);
            if(r1 == true)
                sc.deletePrivateObject(sid, keyLabel);

            boolean r2 = sc.isPublicKeyExist(sid, keyLabel);
            if(r2 == true)
                sc.deletePublicObject(sid, keyLabel);

            boolean r3 = sc.isCertificateExist(sid, keyLabel);
            if(r3 == true)
                sc.deleteCertificate(sid, keyLabel);
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }
    }

    public static void deleteAllObjects(long sid, SmartCard sc) throws PKCS11Exception {
        CK_ATTRIBUTE[] objectParams = new CK_ATTRIBUTE[] {
            new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, PKCS11Constants.TRUE)
        };

        long[] objectIDs = sc.findObjects(sid, objectParams);
        for (long id : objectIDs) {
            sc.getPKCS11().C_DestroyObject(sid, id);
        }
    }

    public static void testCreateECKeys(SmartCard sc, String keyName, String curveName) throws Exception{
       testCreateECKeys(sc, keyName,curveName, SignatureAlg.DSA.ECDSA_SHA1, false);
    }

    public static void testCreateECKeys(SmartCard sc, String keyName, String curveName, SignatureAlg signatureAlg, boolean isSecret) throws Exception {
        String keyLabel = keyName + StringUtil.toHexString(RandomUtil.generateRandom(8));
        try
        {
            // get parameter spec
            ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec(curveName);

            // create key pair template
            ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate(keyLabel, ecParameterSpec);
            ecKeyPairTemplate.getAsTokenTemplate(true, false, false);
            ecKeyPairTemplate.getAsSecretECCurveTemplate(isSecret);

            // get public key
            ECPublicKeySpec spec = (ECPublicKeySpec) sc.createKeyPair(sc.getLatestSessionID(), ecKeyPairTemplate);

            KeyFactory keyFactory = KeyFactory.getInstance("EC", "SunEC");
            PublicKey pubKey = keyFactory.generatePublic(spec);

            // sign, verify
            _signAndVerifyECDSA(sc, signatureAlg, keyLabel, pubKey);

            deleteKeyPair(sc, sc.getLatestSessionID(), keyLabel);
        }
        finally
        {
            deleteKeyPairWithoutEx(sc, sc.getLatestSessionID(), keyLabel);
        }
    }

    public static void testCreateRSAKeyAndSign(SmartCard sc, String keyName, int keyLength) throws Exception {
        String keyLabel = keyName + StringUtil.toHexString(RandomUtil.generateRandom(8));
        try {
            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(keyLength, null);
            sc.createKeyPair(sc.getLatestSessionID(), keyLabel, spec, true, false);

            _signAtHSMVerifyAtLibRSAPKCS1(sc, keyLabel);
            _signAtHSMVerifyAtLibRSAPSS(sc, keyLabel);

            deleteKeyPair(sc,sc.getLatestSessionID(), keyLabel);
        } finally {
            deleteKeyPairWithoutEx(sc, sc.getLatestSessionID(), keyLabel);
        }
    }

    public static void testImportRSAKeyAndSign(SmartCard sc, String keyName, int keyLength) throws Exception{
        String keyLabel = keyName + StringUtil.toHexString(RandomUtil.generateRandom(8));
        try{

            KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, keyLength);
            RSAPublicKeyTemplate rsaPublicKeyTemplate = new RSAPublicKeyTemplate(keyLabel, (RSAPublicKey) keyPair.getPublic());
            RSAPrivateKeyTemplate rsaPrivateKeyTemplate = new RSAPrivateKeyTemplate(keyLabel, (RSAPrivateCrtKey)keyPair.getPrivate(),null);
            RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(rsaPublicKeyTemplate,rsaPrivateKeyTemplate);
            rsaKeyPairTemplate.getAsTokenTemplate(false, true, false);

            sc.importKeyPair(sc.getLatestSessionID(), rsaKeyPairTemplate);

            _signAtHSMVerifyAtLibRSAPKCS1(sc, keyLabel);
            _signAtHSMVerifyAtLibRSAPSS(sc, keyLabel);

            deleteKeyPair(sc,sc.getLatestSessionID(), keyLabel);
        }finally {
            deleteKeyPairWithoutEx(sc, sc.getLatestSessionID(), keyLabel);
        }
    }

    //SmartCard does not support verify
    public static void importCurveAndSignAndVerifyAtLibrary(SmartCard sc, String keyName, long sid, String curveName, SignatureAlg signatureAlg, boolean isSecret) throws Exception{
        String keyLabel = keyName + curveName+ StringUtil.toHexString(RandomUtil.generateRandom(8));
            try
            {
                ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec(curveName);

                ParamsWithECParameterSpec keyGenParams = new ParamsWithECParameterSpec(ecParameterSpec);
                KeyPair keyPair = Crypto.getKeyPairGenerator(AsymmetricAlg.ECDSA).generateKeyPair(keyGenParams);

                ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate(keyLabel, ecParameterSpec, keyPair.getPrivate(), keyPair.getPublic());
                ecKeyPairTemplate.getAsTokenTemplate(true, false, false);
                ecKeyPairTemplate.getAsSecretECCurveTemplate(isSecret);

                sc.importKeyPair(sid, ecKeyPairTemplate);

                _signAndVerifyECDSA(sc, signatureAlg, keyLabel, keyPair.getPublic());

                deleteKeyPair(sc,sc.getLatestSessionID(), keyLabel);
            }
            finally
            {
                deleteKeyPairWithoutEx(sc, sid, keyLabel);
            }
    }

    public static void importCurveAndSign(SmartCard sc, long sid, long slotNo, String curveName) throws Exception{
        String keyLabel = "ecImportKey_" + curveName + StringUtil.toHexString(RandomUtil.generateRandom(8));
        try
        {
            ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec(curveName);

            ParamsWithECParameterSpec keygenparams = new ParamsWithECParameterSpec(ecParameterSpec);
            KeyPair keyPair = Crypto.getKeyPairGenerator(AsymmetricAlg.ECDSA).generateKeyPair(keygenparams);

            ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate(keyLabel, ecParameterSpec, keyPair.getPrivate(), keyPair.getPublic());
            ecKeyPairTemplate.getAsTokenTemplate(true, false, false);

            sc.importKeyPair(sid, ecKeyPairTemplate);

            _signAtHSMAndVerifyECDSA(sc, sid, slotNo, SignatureAlg.DSA.ECDSA_SHA1, keyLabel, keyPair.getPublic());
            _signAtLibAndVerifyECDSA(sc, sid, slotNo, SignatureAlg.DSA.ECDSA_SHA1, keyLabel, keyPair);

            deleteKeyPair(sc,sc.getLatestSessionID(), keyLabel);
        }
        finally
        {
            deleteKeyPairWithoutEx(sc, sid, keyLabel);
        }
    }

    public static void _signAndVerifyECDSA(SmartCard sc, SignatureAlg signAlg, String keyLabel, PublicKey pubKey) throws Exception{
        int dataToBeSignedLen = 10 + new Random().nextInt(30);
        byte[] dataToBeSigned = RandomUtil.generateRandom(dataToBeSignedLen);

        byte[] signature = SmartOp.sign(sc, sc.getLatestSessionID(), sc.getLatestSlotID(), keyLabel, dataToBeSigned, signAlg.getName());

        boolean verifiedByAPI = SignUtil.verify(signAlg, dataToBeSigned, signature, pubKey);
        assertEquals(true, verifiedByAPI);
    }


    public static void _signAtHSMAndVerifyECDSA(SmartCard sc, long sid, long slotNo, SignatureAlg signAlg, String keyLabel, PublicKey pubKey) throws Exception{
        int dataToBeSignedLen = 10 + new Random().nextInt(30);
        byte[] dataToBeSigned = RandomUtil.generateRandom(dataToBeSignedLen);

        byte[] signature = SmartOp.sign(sc, sid, slotNo, keyLabel, dataToBeSigned, signAlg.getName());

        boolean verifiedByAPI = SignUtil.verify(signAlg, dataToBeSigned, signature, pubKey);
        assertEquals(true, verifiedByAPI);

        boolean verifiedByItself = SmartOp.verify(sc, sid, slotNo, keyLabel, dataToBeSigned, signature, signAlg.getName());
        assertEquals(true, verifiedByItself);
    }

    public static void _signAtLibAndVerifyECDSA(SmartCard sc, long sid, long slotNo, SignatureAlg signAlg, String keyLabel, KeyPair aKeyPair) throws Exception{
        int dataToBeSignedLen = 10 + new Random().nextInt(30);
        byte[] dataToBeSigned = RandomUtil.generateRandom(dataToBeSignedLen);

        byte [] signature = SignUtil.sign(signAlg, dataToBeSigned, aKeyPair.getPrivate());

        boolean verifiedByItself = SmartOp.verify(sc, sid, slotNo, keyLabel, dataToBeSigned, signature, signAlg.getName());
        assertEquals(true, verifiedByItself);

        boolean verifiedByAPI = SignUtil.verify(signAlg, dataToBeSigned, signature, aKeyPair.getPublic());
        assertEquals(true, verifiedByAPI);
    }

    public static void _signAtHSMVerifyAtLibRSAPKCS1(SmartCard sc, String keyLabel) throws Exception{

        int dataToBeSignedLen = 10 + new Random().nextInt(30);
        byte[] dataToBeSigned = RandomUtil.generateRandom(dataToBeSignedLen);

        byte[] signature = SmartOp.sign(sc, sc.getLatestSessionID(), sc.getLatestSlotID(), keyLabel, dataToBeSigned,  Algorithms.SIGNATURE_RSA);

        RSAPublicKeySpec spec = (RSAPublicKeySpec) sc.readPublicKeySpec(sc.getLatestSessionID(), keyLabel);
        GnuRSAPublicKey publicKey = new GnuRSAPublicKey(spec.getModulus(), spec.getPublicExponent());

        boolean verified = SignUtil.verify(SignatureAlg.RSA_NONE, dataToBeSigned, signature, publicKey);
        assertEquals(true, verified);
    }


    public static void _signAtHSMVerifyAtLibRSAPSS(SmartCard sc, String keyLabel) throws Exception {

        int dataToBeSignedLen = 10 + new Random().nextInt(30);
        byte[] tobesigned = RandomUtil.generateRandom(dataToBeSignedLen);

        RSAPSSParams rsapssParams = new RSAPSSParams();
        byte[] signature = SmartOp.sign(sc, sc.getLatestSessionID(), sc.getLatestSlotID(), keyLabel, tobesigned, Algorithms.SIGNATURE_RSA_PSS, rsapssParams);

        RSAPublicKeySpec spec = (RSAPublicKeySpec) sc.readPublicKeySpec(sc.getLatestSessionID(), keyLabel);
        GnuRSAPublicKey publicKey = new GnuRSAPublicKey(spec.getModulus(), spec.getPublicExponent());

        boolean verified = SignUtil.verify(SignatureAlg.RSA_PSS, rsapssParams, tobesigned, signature, publicKey);
        assertEquals(true, verified);
    }
}
