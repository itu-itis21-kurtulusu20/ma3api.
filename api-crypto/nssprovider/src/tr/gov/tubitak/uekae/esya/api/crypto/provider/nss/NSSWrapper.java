package tr.gov.tubitak.uekae.esya.api.crypto.provider.nss;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.crypto.Wrapper;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplateFactory;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.security.Key;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 12/15/12 - 12:52 AM <p>
 * <b>Description</b>: <br>
 */
public class NSSWrapper implements Wrapper {
    private WrapAlg wrapAlg;
    private boolean wrapMode;
    private SmartCard nssCard;
    private long sessionID;
    private KeyTemplate wrapperKey;
    private AlgorithmParams wrapParams;
    private CK_MECHANISM mechanism;

    public NSSWrapper(WrapAlg aWrapAlg, boolean wrapMode, SmartCard nssCard, long sessionID) throws CryptoException {
        this.wrapAlg = aWrapAlg;
        this.wrapMode = wrapMode;
        this.nssCard = nssCard;
        this.sessionID = sessionID;
    }


    public void init(Key aKey) throws CryptoException {
        init(aKey, null);
    }

    public void init(Key aKey, AlgorithmParams aParams) throws CryptoException {
        if (aKey instanceof RSAPublicKey) {
            String label = "wrapper-" + System.currentTimeMillis();
            wrapperKey = new RSAPublicKeyTemplate(label, (RSAPublicKey) aKey);
            try {
                nssCard.importKeyPair(sessionID, new RSAKeyPairTemplate((RSAPublicKeyTemplate) wrapperKey, null));
            } catch (PKCS11Exception e) {
                throw new CryptoException("Cannot Import PublicKey to token:" + e.getMessage(), e);
            }
            catch (SmartCardException e) {
                throw new CryptoException("Cannot Import PublicKey to token:" + e.getMessage(), e);
            }

        }else if (aKey instanceof KeyTemplate){
            wrapperKey = (KeyTemplate) aKey;
        }
         else if (aKey instanceof PrivateKey || aKey instanceof SecretKey) {
            wrapperKey = translateKey(aKey);
        }
        else
            throw new CryptoException("Key Must be KeyFacade");

        wrapParams = aParams;

    }

    public byte[] wrap(Key aKey) throws CryptoException {
        Key translatedKey=aKey;
        if (!(aKey instanceof KeyTemplate)) {
            translatedKey = translateKey(aKey);
        }
        findMech(aKey instanceof AsymmKeyTemplate);
        try {
            return nssCard.wrapKey(sessionID, mechanism, wrapperKey, (KeyTemplate) translatedKey);
        } catch (Exception x) {
            throw new CryptoException("Cant wrap:" + x.getMessage(), x);
        }
    }

    private KeyTemplate translateKey(Key aKey) throws CryptoException {
        try {
            Class<?> p11KeyClass = Class.forName("sun.security.pkcs11.P11Key");
            if (p11KeyClass.isAssignableFrom(aKey.getClass())) {
                Field keyIDField = p11KeyClass.getDeclaredField("keyID");
                keyIDField.setAccessible(true);
                long keyID = keyIDField.getLong(aKey);
                CK_ATTRIBUTE[] aTemplate = {new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL)};
                nssCard.getAttributeValue(sessionID, keyID, aTemplate);
                String label = "key"+System.currentTimeMillis();
                if(aTemplate[0].pValue != null)
                    label = String.valueOf(aTemplate[0].pValue);
                KeyTemplate keyTemplate = KeyTemplateFactory.getKeyTemplate(label, aKey);
                keyTemplate.setKeyId(keyID);
                return keyTemplate;
            } else
                throw new CryptoException("Key Must be KeyFacade");
        } catch (Exception e) {
            throw new CryptoException("Key Must be KeyFacade P11Key:" + e.getMessage(), e);
        }
    }

    private void findMech(boolean asymmetric) throws CryptoException {
        if (wrapAlg.equals(WrapAlg.RSA_PKCS1) || wrapAlg.equals(WrapAlg.RSA_ECB_PKCS1))
            mechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);
        else if (wrapAlg.equals(WrapAlg.AES128) || wrapAlg.equals(WrapAlg.AES192) || wrapAlg.equals(WrapAlg.AES256)) {
            if (wrapParams instanceof ParamsWithIV)
                mechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC, ((ParamsWithIV) wrapParams).getIV());  // **** check
            else
                mechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_ECB);  // **** check
                //throw new CryptoException("ParamsWithIV expected as AlgorithmParams :" + wrapParams);
        } else if (wrapAlg.equals(WrapAlg.AES128_CBC) || wrapAlg.equals(WrapAlg.AES192_CBC) || wrapAlg.equals(WrapAlg.AES256_CBC)) {
            if (wrapParams instanceof ParamsWithIV) {
                if (asymmetric)
                    mechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, ((ParamsWithIV) wrapParams).getIV());  // **** check
                else
                    mechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC, ((ParamsWithIV) wrapParams).getIV());  // **** check
            }
        } else
            throw new CryptoException("Unknown Wrap Alg: " + wrapAlg.getName());
    }

    public Key unwrap(byte[] data) throws CryptoException {
        throw new CryptoException("NSS does not support direct unwrap");
    }

    public Key unwrap(byte[] data, Object keyTemplateObj) throws CryptoException {
        if (!(keyTemplateObj instanceof KeyTemplate))
            throw new CryptoException("Unknown Key Template:" + keyTemplateObj);
        KeyTemplate unwrappedKeyTemplate = (KeyTemplate) keyTemplateObj;
        findMech(unwrappedKeyTemplate instanceof AsymmKeyTemplate);
        try {
            nssCard.unwrapKey(sessionID, mechanism, wrapperKey, data, unwrappedKeyTemplate);
            return unwrappedKeyTemplate;
        } catch (Exception x) {
            throw new CryptoException("Cant unwrap:", x);
        }
    }


    public boolean isWrapper() {
        return wrapMode;
    }
}
