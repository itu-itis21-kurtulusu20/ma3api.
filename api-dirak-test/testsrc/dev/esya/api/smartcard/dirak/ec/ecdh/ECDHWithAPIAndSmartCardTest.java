package dev.esya.api.smartcard.dirak.ec.ecdh;

import dev.esya.api.smartcard.dirak.CardTestUtil;
import org.junit.Assert;
import org.junit.Test;
import sun.security.pkcs11.wrapper.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.KeyAgreement;
import tr.gov.tubitak.uekae.esya.api.crypto.KeyPairGenerator;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.KeyAgreementAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithECParameterSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithSharedInfo;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.PKCS11ConstantsExtended;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ECUtil;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyPair;
import java.security.spec.ECParameterSpec;

public class ECDHWithAPIAndSmartCardTest {

    @Test
    public void ECDHReadDerivedKeyInSmartCard()
            throws ESYAException, PKCS11Exception, IOException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {

        // Key generation in API
        ECParameterSpec secp384r1 = NamedCurve.getECParameterSpec("secp384r1");
        ParamsWithECParameterSpec paramsWithECParameterSpec = new ParamsWithECParameterSpec(secp384r1);

        KeyPairGenerator kpg = Crypto.getProvider().getKeyPairGenerator(AsymmetricAlg.ECDSA);
        KeyPair ephemeralKey1 = kpg.generateKeyPair(paramsWithECParameterSpec);
        KeyPair ephemeralKey2 = kpg.generateKeyPair(paramsWithECParameterSpec);
        // Key generation in API

        // Generate shared info bytes that will be used in key derivation
        WrapAlg wrapAlg = WrapAlg.AES256;
        int keyLength = KeyUtil.getKeyLength(WrapAlg.AES256);
        byte[] sharedInfoBytes = ECUtil.generateKeyAgreementSharedInfoBytes(wrapAlg.getOID(), keyLength, null);
        AlgorithmParams sharedInfoParam = new ParamsWithSharedInfo(sharedInfoBytes);
        //Generate shared info bytes that will be used in key derivation

        // ECDH in API
        KeyAgreement keyAgreement = Crypto.getProvider().getKeyAgreement(KeyAgreementAlg.ECCDH_SHA256KDF);
        keyAgreement.init(ephemeralKey1.getPrivate(), sharedInfoParam);
        SecretKey secretKeyBytes1 = keyAgreement.generateKey(ephemeralKey2.getPublic(), WrapAlg.AES256);
        // ECDH in API

        // Importing the key created in the API to Smart Card
        SmartCard sc = new SmartCard(CardType.AKIS);
        long slotNo = 1;
        long sid = sc.openSession(slotNo);
        sc.login(sid, "12345");

        ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate("ephemeralKey2ForECDH", secp384r1, ephemeralKey2.getPrivate(), ephemeralKey2.getPublic());
        ecKeyPairTemplate.getAsTokenTemplate(false, true, false);
        ecKeyPairTemplate.getAsSecretECCurveTemplate(false);

        long[] importKeyPair = sc.importKeyPair(sid, ecKeyPairTemplate);
        // Importing the key created in the API to Smart Card

        // ECDH in Smart Card
        ESubjectPublicKeyInfo eSubjectPublicKeyInfo = new ESubjectPublicKeyInfo(ephemeralKey1.getPublic().getEncoded());
        byte[] publicKeyByte = eSubjectPublicKeyInfo.getSubjectPublicKey();

        CK_MECHANISM mech = new CK_MECHANISM(PKCS11Constants.CKM_ECDH1_DERIVE);
        mech.pParameter = new CK_ECDH1_DERIVE_PARAMS(PKCS11ConstantsExtended.CKD_SHA256_KDF, sharedInfoBytes, publicKeyByte);

        KeyTemplate keyTemplate1 = new AESKeyTemplate("ECDH");
        keyTemplate1.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, false));
        keyTemplate1.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true));
        keyTemplate1.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE_LEN, 32));
        keyTemplate1.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false));

        long derivedKeyId = sc.getPKCS11().C_DeriveKey(sid, mech, importKeyPair[1], keyTemplate1.getAttributesAsArr());
        // ECDH in Smart Card

        // Read Derived Key
        CK_ATTRIBUTE[] derivedKeyAttrForValue = new CK_ATTRIBUTE[1];
        derivedKeyAttrForValue[0] = new CK_ATTRIBUTE();
        derivedKeyAttrForValue[0].type = PKCS11Constants.CKA_VALUE;

        sc.getAttributeValue(sid, derivedKeyId, derivedKeyAttrForValue);
        // Read Derived Key

        // Comparison of derived keys
        Assert.assertArrayEquals(derivedKeyAttrForValue[0].getByteArray(), secretKeyBytes1.getEncoded());

        // Delete key
        CardTestUtil.clearKeyPairIfExist(sc, sid, "ephemeralKey2ForECDH");
    }

    @Test
    public void ECDHUsingDerivedKeyInSmartCard()
            throws ESYAException, PKCS11Exception, IOException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        // Key generation in API
        ECParameterSpec secp384r1 = NamedCurve.getECParameterSpec("secp384r1");
        ParamsWithECParameterSpec paramsWithECParameterSpec = new ParamsWithECParameterSpec(secp384r1);

        KeyPairGenerator kpg = Crypto.getProvider().getKeyPairGenerator(AsymmetricAlg.ECDSA);
        KeyPair ephemeralKey1 = kpg.generateKeyPair(paramsWithECParameterSpec);
        KeyPair ephemeralKey2 = kpg.generateKeyPair(paramsWithECParameterSpec);
        // Key generation in API

        //Generate shared info bytes that will be used in key derivation
        WrapAlg wrapAlg = WrapAlg.AES256;
        int keyLength = KeyUtil.getKeyLength(WrapAlg.AES256);
        byte[] sharedInfoBytes = ECUtil.generateKeyAgreementSharedInfoBytes(wrapAlg.getOID(), keyLength, null);
        AlgorithmParams sharedInfoParam = new ParamsWithSharedInfo(sharedInfoBytes);
        //Generate shared info bytes that will be used in key derivation

        // ECDH in API
        KeyAgreement keyAgreement = Crypto.getProvider().getKeyAgreement(KeyAgreementAlg.ECCDH_SHA256KDF);
        keyAgreement.init(ephemeralKey1.getPrivate(), sharedInfoParam);
        SecretKey secretKeyBytes1 = keyAgreement.generateKey(ephemeralKey2.getPublic(), WrapAlg.AES256);
        // ECDH in API

        // Importing the key created in the API to Smart Card
        SmartCard sc = new SmartCard(CardType.DIRAKHSM);
        long slotNo = 1;
        long sid = sc.openSession(slotNo);
        sc.login(sid, "12345678");

        ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate("ephemeralKey2ForECDH", secp384r1, ephemeralKey2.getPrivate(), ephemeralKey2.getPublic());
        ecKeyPairTemplate.getAsTokenTemplate(false, true, false);
        ecKeyPairTemplate.getAsSecretECCurveTemplate(false);
        ecKeyPairTemplate.getAsTokenOrSessionTemplate(false);

        long[] importKeyPair = sc.importKeyPair(sid, ecKeyPairTemplate);
        // Importing the key created in the API to Smart Card

        // ECDH in Smart Card
        ESubjectPublicKeyInfo eSubjectPublicKeyInfo = new ESubjectPublicKeyInfo(ephemeralKey1.getPublic().getEncoded());
        byte[] publicKeyByte = eSubjectPublicKeyInfo.getSubjectPublicKey();

        CK_MECHANISM mech = new CK_MECHANISM(PKCS11Constants.CKM_ECDH1_DERIVE);
        mech.pParameter = new CK_ECDH1_DERIVE_PARAMS(PKCS11ConstantsExtended.CKD_SHA256_KDF, sharedInfoBytes, publicKeyByte);

        KeyTemplate keyTemplate1 = new AESKeyTemplate("tempKey1");
        keyTemplate1.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, false));
        keyTemplate1.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true));
        keyTemplate1.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE_LEN, 32));
        keyTemplate1.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false));
        keyTemplate1.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ENCRYPT, true));
        keyTemplate1.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, true));

        long derivedKeyId = sc.getPKCS11().C_DeriveKey(sid, mech, importKeyPair[1], keyTemplate1.getAttributesAsArr());
        // ECDH in Smart Card

        // Comparison of derived keys
        byte[] random = RandomUtil.generateRandom(16);

        byte[] encryptDataWithHSM = sc.encryptData(sid, derivedKeyId, random, new CK_MECHANISM(PKCS11Constants.CKM_AES_ECB));
        byte[] encryptDataWithAPI = CipherUtil.encrypt(CipherAlg.AES256_ECB_NOPADDING, null, random, secretKeyBytes1.getEncoded());

        byte[] decryptDataWithHSM = sc.decryptData(sid, derivedKeyId, encryptDataWithAPI, new CK_MECHANISM(PKCS11Constants.CKM_AES_ECB));
        byte[] decryptDataWithAPI = CipherUtil.decrypt(CipherAlg.AES256_ECB_NOPADDING, null, encryptDataWithHSM, secretKeyBytes1.getEncoded());

        Assert.assertArrayEquals(random, decryptDataWithHSM);
        Assert.assertArrayEquals(random, decryptDataWithAPI);
    }
}
