package dev.esya.api.smartcard.dirak;

import gnu.crypto.key.rsa.GnuRSAPublicKey;
import gnu.crypto.pad.IPad;
import gnu.crypto.pad.PadFactory;
import org.junit.Assert;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithECParameterSpec;
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
import java.security.spec.*;
import java.util.Random;

/**
 * Created by orcun.ertugrul on 17-Nov-17.
 */
public class CardTestUtil {

    public static int PRINT_INTERVAL = 20;

    public static long getSlot(SmartCard sc) throws PKCS11Exception {
        long[] slots = sc.getTokenPresentSlotList();
        return slots[0];
    }

    public static void deleteKeyPair(SmartCard sc, long sid, String keyLabel) throws SmartCardException, PKCS11Exception {
        sc.deletePrivateObject(sid, keyLabel);
        sc.deletePublicObject(sid, keyLabel);

        boolean found1 = sc.isPrivateKeyExist(sid, keyLabel);
        boolean found2 = sc.isPublicKeyExist(sid, keyLabel);

        Assert.assertFalse(found1);
        Assert.assertFalse(found2);
    }

    public static void clearKeyPairIfExist(SmartCard sc, long sid, String keyLabel){
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

    public static void deleteObjects(SmartCard sc, long sid, String aLabel) throws PKCS11Exception {
        CK_ATTRIBUTE[] template = {new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, aLabel)};

        long[] objectList = sc.findObjects(sid, template);

        for (int i = 0; i < objectList.length; i++) {
            sc.getPKCS11().C_DestroyObject(sid, objectList[i]);
        }
    }

    public static void testCreateECKeysAndSign(SmartCard sc, long sid, long slotNo, String keyPrefix, String curveName,
                                               SignatureAlg signatureAlg, boolean isSecret) throws Exception {
        testCreateECKeysAndSign(sc, sid, slotNo, keyPrefix, curveName, signatureAlg, isSecret, 1);
    }

    public static void testCreateECKeysAndSign(SmartCard sc, long sid, long slotNo, String keyPrefix, String curveName,
                                               SignatureAlg signatureAlg, boolean isSecret, int signCount) throws Exception {
        String keyLabel = keyPrefix + StringUtil.toHexString(RandomUtil.generateRandom(8));
        try {
            // get parameter spec
            ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec(curveName);

            // create key pair template
            ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate(keyLabel, ecParameterSpec);
            ecKeyPairTemplate.getAsTokenTemplate(true, false, false);
            ecKeyPairTemplate.getAsSecretECCurveTemplate(isSecret);

            ECPublicKeySpec spec = (ECPublicKeySpec) sc.createKeyPair(sc.getLatestSessionID(), ecKeyPairTemplate);

            KeyFactory keyFactory = KeyFactory.getInstance("EC", "SunEC");
            PublicKey pubKey = keyFactory.generatePublic(spec);

            _signAtHSMAndVerifyLooped(sc, sid, slotNo, signatureAlg, null, keyLabel, pubKey, signCount);
        } finally {
            deleteObjects(sc, sid, keyLabel);
        }
    }

    public static void testCreateRSAKeyAndSign(SmartCard sc, long sid, long slotNo, String keyName, int keyLength, SignatureAlg signAlg, AlgorithmParameterSpec algorithmParameterSpec, int signCount) throws Exception {
        String keyLabel = keyName + StringUtil.toHexString(RandomUtil.generateRandom(8));
        try {
            RSAKeyGenParameterSpec generatorSpec = new RSAKeyGenParameterSpec(keyLength, null);
            sc.createKeyPair(sid, keyLabel, generatorSpec, true, false);

            RSAPublicKeySpec spec = (RSAPublicKeySpec) sc.readPublicKeySpec(sid, keyLabel);
            GnuRSAPublicKey publicKey = new GnuRSAPublicKey(spec.getModulus(), spec.getPublicExponent());

            _signAtHSMAndVerifyLooped(sc, sid, slotNo, signAlg, algorithmParameterSpec, keyLabel, publicKey, signCount); // SignatureAlg.RSA_PSS // SignatureAlg.RSA_SHA256
        } finally {
            deleteObjects(sc, sid, keyLabel);
        }
    }

    public static void testImportRSAKeyAndSign(SmartCard sc, long sid, long slotNo, String keyName, int keyLength, SignatureAlg signAlg, AlgorithmParameterSpec algorithmParameterSpec, int signCount) throws Exception {
        String keyLabel = keyName + StringUtil.toHexString(RandomUtil.generateRandom(8));
        try {
            KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, keyLength);
            PublicKey publicKey = keyPair.getPublic();
            RSAPublicKeyTemplate rsaPublicKeyTemplate = new RSAPublicKeyTemplate(keyLabel, (RSAPublicKey) publicKey);
            RSAPrivateKeyTemplate rsaPrivateKeyTemplate = new RSAPrivateKeyTemplate(keyLabel, (RSAPrivateCrtKey) keyPair.getPrivate(), null);
            RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(rsaPublicKeyTemplate, rsaPrivateKeyTemplate);
            rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);

            sc.importKeyPair(sid, rsaKeyPairTemplate);

            _signAtHSMAndVerifyLooped(sc, sid, slotNo, signAlg, algorithmParameterSpec, keyLabel, publicKey, signCount);

        } finally {
            deleteObjects(sc, sid, keyLabel);
        }
    }

    public static void importCurveAndSignAndVerifyAtLibrary(SmartCard sc, long sid, String keyName, String curveName, SignatureAlg signatureAlg, boolean isSecret) throws Exception {
        importCurveAndSignAndVerifyAtLibrary(sc, sid, keyName, curveName, signatureAlg, isSecret,1);
    }

    // SmartCard does not support verify
    public static void importCurveAndSignAndVerifyAtLibrary(SmartCard sc, long sid, String keyName, String curveName, SignatureAlg signatureAlg, boolean isSecret, int signCount) throws Exception {
        String keyLabel = keyName + curveName + StringUtil.toHexString(RandomUtil.generateRandom(8));

        try {
            ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec(curveName);

            ParamsWithECParameterSpec keyGenParams = new ParamsWithECParameterSpec(ecParameterSpec);
            KeyPair keyPair = Crypto.getKeyPairGenerator(AsymmetricAlg.ECDSA).generateKeyPair(keyGenParams);

            ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate(keyLabel, ecParameterSpec, keyPair.getPrivate(), keyPair.getPublic());
            ecKeyPairTemplate.getAsTokenTemplate(true, false, false);
            ecKeyPairTemplate.getAsSecretECCurveTemplate(isSecret);

            sc.importKeyPair(sid, ecKeyPairTemplate);

            _signAtHSMAndVerifyLooped(sc, sid, sc.getLatestSlotID(), signatureAlg, null, keyLabel, keyPair.getPublic(), signCount);

        } finally {
            deleteObjects(sc, sid, keyLabel);
        }
    }


    public static void _signAtHSMAndVerifyLooped(SmartCard sc, long sid, long slotNo, SignatureAlg signAlg, AlgorithmParameterSpec algorithmParameterSpec, String keyLabel, PublicKey pubKey, int signCount) throws Exception {
        for(int i = 0; i < signCount; i++) {
            if (signCount != 1 && i % CardTestUtil.PRINT_INTERVAL == 0)
                System.out.println("i: " + i);

            int dataToBeSignedLen = 10 + new Random().nextInt(30);
            byte[] dataToBeSigned = RandomUtil.generateRandom(dataToBeSignedLen);

            byte[] signature = SmartOp.sign(sc, sid, slotNo, keyLabel, dataToBeSigned, signAlg.getName(), algorithmParameterSpec);

            boolean verifiedByAPI = SignUtil.verify(signAlg, (AlgorithmParams) algorithmParameterSpec, dataToBeSigned, signature, pubKey);
            Assert.assertTrue(verifiedByAPI);

            boolean verifiedByItself = SmartOp.verify(sc, sid, slotNo, keyLabel, dataToBeSigned, signature, signAlg.getName());
            Assert.assertTrue(verifiedByItself);
        }
    }

    public static byte[] pkcs7Padding(byte[] dataToBeEncrypted, int blockSize) {
        IPad padOps = PadFactory.getInstance("pkcs7");
        padOps.init(blockSize);
        byte[] pad = padOps.pad(dataToBeEncrypted, 0, dataToBeEncrypted.length);
        return ByteUtil.concatAll(dataToBeEncrypted, pad);
    }
}
