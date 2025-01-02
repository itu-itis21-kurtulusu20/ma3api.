package tr.gov.tubitak.uekae.esya.api.crypto.provider.nss;

import sun.security.pkcs11.wrapper.*;
import tr.gov.tubitak.uekae.esya.api.crypto.KeyAgreement;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.*;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithSharedInfo;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.pkcs11wrapper.SCMechanism;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.PKCS11Ops;
import tr.gov.tubitak.uekae.esya.asn.algorithms.ECParameters;

import javax.crypto.SecretKey;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPoint;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ayetgin
 */
public class NSSKeyAgreement implements KeyAgreement {

    private SmartCard nssCard;
    private long sessionID;
    private Long slotID;

    private KeyTemplate agreementKey;
    private AlgorithmParams agreementParams;

    private Key privateKey;
    KeyAgreementAlg keyAgreementAlg;

    public NSSKeyAgreement(KeyAgreementAlg aKeyAgreementAlg, SmartCard nssCard, Long slotID,long sessionID) throws CryptoException {
        this.nssCard = nssCard;
        this.sessionID = sessionID;
        this.keyAgreementAlg = aKeyAgreementAlg;
        this.slotID = slotID;
    }

    public void init(Key aKey) throws CryptoException {
        init(aKey, null);
    }

    public void init(Key aKey, final AlgorithmParams aParams) throws CryptoException {
        if(aKey instanceof PrivateKey){
            final AlgorithmParams localParams = aParams;
            try {
                privateKey = aKey;
            } catch (Exception e) {
                throw new CryptoException("Error while Initing KeyAgreement:"+e.getMessage(),e);
            }
            agreementParams = aParams;
        }
        else
            throw new CryptoException("Unsupported Key Type");
        /*if (aKey instanceof RSAPublicKey) {
            String label = "keyAgreementPublic-" + System.currentTimeMillis();
            agreementKey = new RSAPublicKeyTemplate(label, (RSAPublicKey) aKey);
            RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate((RSAPublicKeyTemplate) agreementKey, null);
            try {
                nssCard.importKeyPair(sessionID,rsaKeyPairTemplate);
                KeyStore pkcs11 = KeyStore.getInstance("PKCS11", keyAgreement.getProvider());
                pkcs11.load(null,null); // doesnt matter we already logged in
                Key nssKey = pkcs11.getKey(((KeyTemplate) aKey).getLabel(), null);
                keyAgreement.doPhase(nssKey, true);
            } catch (Exception e) {
                throw new CryptoException("Cannot Import PublicKey to token:" + e.getMessage(), e);
            }
        }
        else if(aKey instanceof PublicKey){
            try{
                keyAgreement.doPhase(aKey, true);
            } catch (Exception e) {
                throw new CryptoException("Cannot Import PublicKey to token:" + e.getMessage(), e);
            }
        }*/
        ///
    }

    public SecretKey generateKey(Key aKey, Algorithm alg) throws CryptoException {
        try {

            byte[] publicValue=null;
            ParamsWithSharedInfo paramsWithSharedInfo = (ParamsWithSharedInfo) agreementParams;
                try {
                    Class ecPublicKeyImpClass = Class.forName("sun.security.ec.ECPublicKeyImpl");
                    Constructor declaredConstructor  = ecPublicKeyImpClass.getConstructor(byte[].class);
                    ECPublicKey ecPublicKey = (ECPublicKey)declaredConstructor.newInstance(aKey.getEncoded());
                    ECPoint ecPoint = (ECPoint)ecPublicKey.getW();
                    publicValue = tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.ECParameters.encodePoint(ecPoint, ecPublicKey.getParams().getCurve());
                } catch (Exception e) {
                    throw new CryptoException("Public Key Encoded error", e);
                }

            //SecretKey secretKey = keyAgreement.generateSecret("TlsPremasterSecret");
            SecretKey secretKey = nativeGenerateSecret((PrivateKey) privateKey, paramsWithSharedInfo.getSharedInfo(), publicValue,alg);
            return secretKey;
        } catch (Exception e) {
            throw new CryptoException("Cannot generate Secret:"+e.getMessage(),e);
        }
    }

    long getKeyId(Key aKey) throws CryptoException {
        try {
            Class<?> p11KeyClass = Class.forName("sun.security.pkcs11.P11Key");
            if (p11KeyClass.isAssignableFrom(aKey.getClass())) {
                Field keyIDField = p11KeyClass.getDeclaredField("keyID");
                keyIDField.setAccessible(true);
                long keyID = keyIDField.getLong(aKey);
                return keyID;
            } else
                throw new CryptoException("Key Must be KeyFacade");
        } catch (Exception e) {
            throw new CryptoException("Key Must be KeyFacade P11Key:" + e.getMessage(), e);
        }
    }


    private long constructKDFMechanism(DerivationFunctionAlg derivationFunctionAlg){
        return PKCS11Constants.CKD_NULL;
        /*long kdf_mec=KeyAgreement.CKD_SHA512_KDF;
        DigestAlg digestAlg = derivationFunctionAlg.getDigestAlg();
        if(digestAlg == DigestAlg.SHA1){
            kdf_mec = PKCS11Constants.CKD_SHA1_KDF;
        }
        else if(digestAlg == DigestAlg.SHA224){
            kdf_mec = KeyAgreement.CKD_SHA224_KDF;
        } else if(digestAlg == DigestAlg.SHA256){
            kdf_mec = KeyAgreement.CKD_SHA256_KDF;
        } else if(digestAlg == DigestAlg.SHA384){
            kdf_mec = KeyAgreement.CKD_SHA384_KDF;
        }if(digestAlg == DigestAlg.SHA512){
            kdf_mec = KeyAgreement.CKD_SHA512_KDF;
        }
        return kdf_mec;
        */
    }

    private CK_MECHANISM constructMechanism(AgreementAlg agreementAlg,DerivationFunctionAlg derivationFunctionAlg,Algorithm intendedAlg,byte[] sharedInfoByte, byte[] publicValue) throws CryptoException {

        List<Long> supportedKeyDerivationMechList = getSupportedKeyDerivationMechList();
        CK_MECHANISM retMechanism = null;
        long deriveMec = PKCS11Constants.CKM_ECDH1_DERIVE;
        if(agreementAlg == AgreementAlg.ECDH){
            deriveMec = PKCS11Constants.CKM_ECDH1_DERIVE;
            DigestAlg digestAlg = derivationFunctionAlg.getDigestAlg();
            long kdf_mec = constructKDFMechanism(derivationFunctionAlg);
            CK_ECDH1_DERIVE_PARAMS ckParams = new CK_ECDH1_DERIVE_PARAMS(kdf_mec,null, publicValue);
            retMechanism = new CK_MECHANISM(deriveMec,ckParams);
        }
        else if(agreementAlg == AgreementAlg.ECCDH){
            throw new UnsupportedOperationException("Unsupported Yet");
        }
        else
            throw new CryptoException("Unknown Aggrement Algoritm");
        return retMechanism;
    }

    KeyTemplate constructDerivedKeyTemplate(Algorithm intendedAlg) throws InvalidKeyException {
        String derivedKeyLabel = "DeriveKey"+System.currentTimeMillis();
        WrapAlg wrapAlg = (WrapAlg)intendedAlg;
        if(wrapAlg.getName().startsWith("AES")){
            int keyLen = KeyUtil.getKeyLength(wrapAlg) / 8;
            AESKeyTemplate retTemplate = new AESKeyTemplate(derivedKeyLabel,keyLen);
            retTemplate.getAsWrapperTemplate();
            return retTemplate;
        }
        else
            throw  new InvalidKeyException("Unsupported Key :"+wrapAlg.getName());
    }

    List<Long> getSupportedKeyDerivationMechList(){
        List<Long> retMechanismList=new ArrayList<Long>();
        try {
            long[] mechanismList = nssCard.getMechanismList(slotID);
            for (long mechanismCode : mechanismList) {
                SCMechanism scMechanism = new SCMechanism(mechanismCode);
                if(scMechanism.isKeyDerivationMechanism()){
                    System.out.println(scMechanism.toString());
                }
            }
        } catch (PKCS11Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return retMechanismList;
    }

    private CK_MECHANISM constructSymetricDerivationMechanism(DerivationFunctionAlg derivationFunctionAlg){
        CK_MECHANISM retSymKeyDerivationMechanism=null;
        DigestAlg digestAlg = derivationFunctionAlg.getDigestAlg();
        if(digestAlg == DigestAlg.SHA1){
            retSymKeyDerivationMechanism = new CK_MECHANISM(PKCS11Constants.CKM_SHA1_KEY_DERIVATION);
        }
        else if(digestAlg == DigestAlg.SHA256){
            retSymKeyDerivationMechanism = new CK_MECHANISM(PKCS11Constants.CKM_SHA256_KEY_DERIVATION);
        } else if(digestAlg == DigestAlg.SHA384){
            retSymKeyDerivationMechanism = new CK_MECHANISM(PKCS11Constants.CKM_SHA384_KEY_DERIVATION);
        }if(digestAlg == DigestAlg.SHA512){
            retSymKeyDerivationMechanism = new CK_MECHANISM(PKCS11Constants.CKM_SHA512_KEY_DERIVATION);
        }
        return retSymKeyDerivationMechanism;
    }


    private SecretKey nativeGenerateSecret(PrivateKey privateKey, byte[] sharedInfoByte, byte[] publicValue, Algorithm intendedAlg)
            throws IllegalStateException, NoSuchAlgorithmException,
            InvalidKeyException {
        if ((privateKey == null) || (publicValue == null)) {
            throw new IllegalStateException("Not initialized correctly");
        }

        DerivationFunctionAlg derivationFunctionAlg = keyAgreementAlg.getDerivationFunctionAlg();
        AgreementAlg agreementAlg = keyAgreementAlg.getAgreementAlg();

        try {
            KeyTemplate derivedKeyTemplate = constructDerivedKeyTemplate(intendedAlg);

            KeyTemplate tempKeyTemplate = constructDerivedKeyTemplate(intendedAlg);

            tempKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_UNWRAP,false));
            tempKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DERIVE,true));
            tempKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE,PKCS11Constants.CKK_GENERIC_SECRET));

            List<CK_ATTRIBUTE> attributeList = tempKeyTemplate.getAttributes();
            List<CK_ATTRIBUTE> tmpAttributeList =new ArrayList<CK_ATTRIBUTE>();
            for(int k=0;k<attributeList.size();k++){
                CK_ATTRIBUTE attribute = attributeList.get(k);
                if(attribute.type != PKCS11Constants.CKA_VALUE_LEN)
                {
                    tmpAttributeList.add(attribute);
                }
            }
            CK_ATTRIBUTE[] tmpKeyAttributes = tmpAttributeList.toArray(new CK_ATTRIBUTE[0]);
            ///
            CK_MECHANISM mechanism = constructMechanism(agreementAlg,derivationFunctionAlg,intendedAlg,sharedInfoByte,publicValue);
            PKCS11Ops pkcs11Ops = (PKCS11Ops) nssCard.getCardType().getCardTemplate().getPKCS11Ops();
            long sourceKeyId = getKeyId(privateKey);
            long tmpKeyId =pkcs11Ops.getmPKCS11().C_DeriveKey(sessionID,mechanism,sourceKeyId,tmpKeyAttributes);

            derivedKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE,PKCS11Constants.CKK_GENERIC_SECRET));
            CK_MECHANISM symetricKeyDeriveMechanism = constructSymetricDerivationMechanism(derivationFunctionAlg);

            long[] mechList = pkcs11Ops.getmPKCS11().C_GetMechanismList(sessionID);
            for (long l : mechList) {
                SCMechanism scMechanism = new SCMechanism(l);
                System.out.println("Mechanism Code = "+l+" , Name = "+scMechanism.getName());
            }

            long generatedKeyId = pkcs11Ops.getmPKCS11().C_DeriveKey(sessionID,symetricKeyDeriveMechanism,tmpKeyId,derivedKeyTemplate.getAttributesAsArr());

            derivedKeyTemplate.setKeyId(generatedKeyId);
            return (SecretKey) derivedKeyTemplate;
            ///

            //CK_ATTRIBUTE[] attributes = derivedKeyTemplate.getAttributesAsArr();

            /*CK_ATTRIBUTE[] attributes = new CK_ATTRIBUTE[] {
                    new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_SECRET_KEY),
                    new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, keyType),
                    new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE_LEN,16),
                    new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL,derivedKeyLabel)
            };*/
           /* CK_MECHANISM mechanism = constructMechanism(agreementAlg,derivationFunctionAlg,intendedAlg,sharedInfoByte,publicValue);
            PKCS11Ops pkcs11Ops = (PKCS11Ops) nssCard.getCardType().getCardTemplate().getPKCS11Ops();
            long sourceKeyId = getKeyId(privateKey);
            long generatedKeyId =pkcs11Ops.getmPKCS11().C_DeriveKey(sessionID,mechanism,sourceKeyId,attributes);
            return (SecretKey) derivedKeyTemplate;*/
        } catch (PKCS11Exception e) {
            throw new InvalidKeyException("Could not derive key", e);
        } catch (CryptoException e) {
            throw new InvalidKeyException("Could not derive key", e);
        } finally {
        }
    }
}
