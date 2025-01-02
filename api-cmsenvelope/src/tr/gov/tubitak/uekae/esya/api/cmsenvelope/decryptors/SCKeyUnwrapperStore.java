package tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors;

import com.objsys.asn1j.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.ECDHDecryptorParams;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.IDecryptorParams;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Wrapper;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.*;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithSharedInfo;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ISmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.PKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ECUtil;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectPublicKeyInfo;

import javax.crypto.SecretKey;
import java.lang.reflect.Constructor;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECPoint;
import java.util.ArrayList;
import java.util.List;

public class SCKeyUnwrapperStore extends SCDecryptor {

    private static Logger logger = LoggerFactory.getLogger(SCKeyUnwrapperStore.class);

    String encKeyLabel;

    public String getEncKeyLabel() {
        return encKeyLabel;
    }

    public SCKeyUnwrapperStore(ISmartCard aSmartCard, long aSessionID) throws CryptoException {
        super(aSmartCard, aSessionID);
        if(! Crypto.isFipsMode())
            throw new CryptoException("SCKeyUnwrapperStore can be used only in FipsMode");
    }

    public SCKeyUnwrapperStore(ISmartCard aSmartCard, long aSessionID,ECertificate[] certificates,String encKeyLabel) throws CryptoException {
        super(aSmartCard, aSessionID);
        if(! Crypto.isFipsMode())
            throw new CryptoException("SCKeyUnwrapperStore can be used only in FipsMode");
        mCerts = certificates;
        this.encKeyLabel=encKeyLabel;
    }

    public SCKeyUnwrapperStore(ISmartCard aSmartCard, long aSessionID,ECertificate[] certificates) throws CryptoException {
        super(aSmartCard, aSessionID);
        if(! Crypto.isFipsMode())
            throw new CryptoException("SCKeyUnwrapperStore can be used only in FipsMode");
        mCerts = certificates;
    }

    protected SecretKey unwrapSymetricKey(byte[] wrappedData,IDecryptorParams params,byte[] certSerial) throws CryptoException{
        try{
            String label = "cmskey-" + System.currentTimeMillis();
            AESKeyTemplate aesKeyTemplate = (AESKeyTemplate) new AESKeyTemplate(label).getAsDecryptorTemplate();
            aesKeyTemplate.getAsUnwrapperTemplate();
            if(encKeyLabel != null)
                mSmartCard.unwrapKey(mSessionID, new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS), encKeyLabel, wrappedData, aesKeyTemplate); // 24 ?
            else
                mSmartCard.unwrapKey(mSessionID, new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS), certSerial, wrappedData, aesKeyTemplate); // 24 ?
            return aesKeyTemplate;

        } catch (Exception aEx) {
            throw new CryptoException("Error while unwrapping symetric encryption key", aEx);
        }
    }


    //Generate sender public key from receiver private key algorithm identifier and sender public key bytes
    private PublicKey _getPublicKey(AlgorithmIdentifier senderPublicKeyAlgId, byte[] senderPublicKeyBytes) throws CryptoException
    {
        PublicKey senderPublicKey = null;
        try
        {
            Asn1BitString subjectPublicKey = new Asn1BitString(senderPublicKeyBytes.length * 8, senderPublicKeyBytes);
            SubjectPublicKeyInfo publicKeyInfo = new SubjectPublicKeyInfo(senderPublicKeyAlgId, subjectPublicKey);
            Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
            publicKeyInfo.encode(buff);
            senderPublicKey = KeyUtil.decodePublicKey(new ESubjectPublicKeyInfo(publicKeyInfo));
        }
        catch(Exception e)
        {
            throw new CryptoException("Can not encode PublicKey", e);
        }
        return senderPublicKey;
    }

  /*  protected SecretKey moveKeyToNSS(){

    }*/

    protected SecretKey unwrapAgreedKey(IDecryptorParams params, byte[] serial) throws CryptoException {
        ECDHDecryptorParams ecdhParams= (ECDHDecryptorParams) params;
        PublicKey senderPublicKey = _getPublicKey(ecdhParams.getSenderPublicKeyAlgId(), ecdhParams.getSenderPublicKey());
        int keyLength = KeyUtil.getKeyLength(WrapAlg.fromOID(ecdhParams.getKeyWrapAlgOid()));
        byte []sharedInfoBytes = ECUtil.generateKeyAgreementSharedInfoBytes(ecdhParams.getKeyWrapAlgOid(), keyLength, ecdhParams.getukm());

        WrapAlg wrapAlg = WrapAlg.fromOID(ecdhParams.getKeyWrapAlgOid());
        KeyAgreementAlg keyAgreementAlg = KeyAgreementAlg.fromOID(ecdhParams.getKeyEncAlgOid());
        AlgorithmParams sharedInfoParam = new ParamsWithSharedInfo(sharedInfoBytes);

        byte[] publicValue=null;
        try {
            Class ecPublicKeyImpClass = Class.forName("sun.security.ec.ECPublicKeyImpl");
            Constructor declaredConstructor  = ecPublicKeyImpClass.getConstructor(byte[].class);
            ECPublicKey ecPublicKey = (ECPublicKey)declaredConstructor.newInstance(senderPublicKey.getEncoded());
            ECPoint ecPoint = (ECPoint)ecPublicKey.getW();
            publicValue = tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.ECParameters.encodePoint(ecPoint, ecPublicKey.getParams().getCurve());
        } catch (Exception e) {
            throw new CryptoException("Public Key Encoded error", e);
        }

        SecretKey secretKey;
        try {
             secretKey= nativeGenerateSecret(serial, sharedInfoBytes, keyAgreementAlg, publicValue, wrapAlg);
        } catch (Exception e) {
            throw new CryptoException("Key agreement error", e);
        }


        AESKeyTemplate unwrappingKeyTemplate = (AESKeyTemplate) secretKey;
        //////////////////
        try{
            String label = "cmskey-" + System.currentTimeMillis();
            AESKeyTemplate aesKeyTemplate = (AESKeyTemplate) new AESKeyTemplate(label).getAsDecryptorTemplate();
            aesKeyTemplate.getAsUnwrapperTemplate();
            byte[] wrappedKey = ecdhParams.getWrappedKey();
            if(encKeyLabel != null)
                mSmartCard.unwrapKey(mSessionID, new CK_MECHANISM(PKCS11Constants.CKM_AES_ECB),unwrappingKeyTemplate , wrappedKey, aesKeyTemplate);
            return aesKeyTemplate;

        } catch (Exception aEx) {
            throw new CryptoException("Error while unwrapping symetric encryption key", aEx);
        }
        ////////////////////
    }

    private long constructKDFMechanism(DerivationFunctionAlg derivationFunctionAlg){
        return PKCS11Constants.CKD_NULL;
        /*
        long kdf_mec=KeyAgreement.CKD_SHA512_KDF;
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

    KeyTemplate constructDerivedKeyTemplate(Algorithm intendedAlg) throws InvalidKeyException {
        String derivedKeyLabel = "DeriveKey"+System.currentTimeMillis();
        WrapAlg wrapAlg = (WrapAlg)intendedAlg;
        if(wrapAlg.getName().startsWith("AES")){
            int keyLen = KeyUtil.getKeyLength(wrapAlg) / 8;
            AESKeyTemplate retTemplate = new AESKeyTemplate(derivedKeyLabel,keyLen);
            retTemplate.getAsUnwrapperTemplate();
            return retTemplate;
        }
        else
            throw  new InvalidKeyException("Unsupported Key :"+wrapAlg.getName());
    }

    private SecretKey nativeGenerateSecret(byte[] certSerial, byte[] sharedInfoByte, KeyAgreementAlg keyAgreementAlg,byte[] publicValue, Algorithm intendedAlg)
            throws IllegalStateException, NoSuchAlgorithmException,
            InvalidKeyException {
        if ((certSerial == null) || (publicValue == null)) {
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
                //if(attribute.type != PKCS11Constants.CKA_VALUE_LEN)
                {
                    tmpAttributeList.add(attribute);
                }
            }
            CK_ATTRIBUTE[] tmpKeyAttributes = tmpAttributeList.toArray(new CK_ATTRIBUTE[0]);

            CK_MECHANISM mechanism = constructMechanism(agreementAlg,derivationFunctionAlg,intendedAlg,sharedInfoByte,publicValue);
            PKCS11Ops pkcs11Ops = (PKCS11Ops) mSmartCard.getCardType().getCardTemplate().getPKCS11Ops();
            long sourceKeyId = mSmartCard.getPrivateKeyObjIDFromCertificateSerial(mSessionID, certSerial);
            long tmpKeyId =pkcs11Ops.getmPKCS11().C_DeriveKey(mSessionID,mechanism,sourceKeyId,tmpKeyAttributes);


            CK_MECHANISM symetricKeyDeriveMechanism = constructSymetricDerivationMechanism(derivationFunctionAlg);

            long generatedKeyId = pkcs11Ops.getmPKCS11().C_DeriveKey(mSessionID,symetricKeyDeriveMechanism,tmpKeyId,derivedKeyTemplate.getAttributesAsArr());
            derivedKeyTemplate.setKeyId(generatedKeyId);
            return (SecretKey) derivedKeyTemplate;
        } catch (PKCS11Exception e) {
            throw new InvalidKeyException("Could not derive key", e);
        } catch (CryptoException e) {
            throw new InvalidKeyException("Could not derive key", e);
        } catch (SmartCardException e) {
            throw new InvalidKeyException("Could not derive key", e);
        } finally {
        }
    }



    public Key unwrapKey(EAlgorithmIdentifier simAlg, byte[] encryptedContent, SecretKey anahtar, KeyTemplate unwrappedKeyTemplate) throws PKCS11Exception, SmartCardException, CryptoException, InvalidKeyException {

//        SecretKeyTemplate hmac = new AESKeyTemplate("hmac").getAsExportableTemplate();

        Pair<CipherAlg, AlgorithmParams> pair = CipherAlg.fromAlgorithmIdentifier(simAlg);
        AlgorithmParams params = pair.getObject2();

        CK_MECHANISM aMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC);
        if (params instanceof ParamsWithIV)
            aMechanism.pParameter = ((ParamsWithIV) params).getIV();


        String tmpKey = "tmpKey-"+System.currentTimeMillis();
        AESKeyTemplate aesKeyTemplate = (AESKeyTemplate) new AESKeyTemplate(tmpKey);
        aesKeyTemplate.getAsExportableTemplate();
        aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN,true));
        //aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN,true));
        mSmartCard.unwrapKey(mSessionID, aMechanism, (KeyTemplate) anahtar, encryptedContent, aesKeyTemplate);

       /*
         byte[] signedByte = mSmartCard.signDataWithKeyID(mSessionID, aesKeyTemplate.getKeyId(), new CK_MECHANISM(PKCS11Constants.CKM_AES_MAC), "test".getBytes());
         System.out.println("SC MAC = "+ StringUtil.toString(signedByte));
        */

        //Smart kart içinde açtığımız anahtarı NSS'e taşıyoruz.
        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 1024);
        String wrapperLabel = "wrapper-"+System.currentTimeMillis();
        RSAPublicKeyTemplate wrapper = new RSAPublicKeyTemplate(wrapperLabel, (RSAPublicKey) keyPair.getPublic());
        mSmartCard.importKeyPair(mSessionID, new RSAKeyPairTemplate(wrapper.getAsWrapperTemplate(), null));
        byte[] wrappedKey = mSmartCard.wrapKey(mSessionID, new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS), wrapperLabel,tmpKey);
        Wrapper unwrapper = Crypto.getUnwrapper(WrapAlg.RSA_PKCS1);
        unwrapper.init(keyPair.getPrivate());
       return unwrapper.unwrap(wrappedKey, unwrappedKeyTemplate);
        //return unwrappedKeyTemplate;

    }
}
