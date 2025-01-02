package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.ECParameters;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ECUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;

import static sun.security.pkcs11.wrapper.PKCS11Constants.*;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 10/11/12 - 12:34 AM <p>
 * <b>Description</b>: <br>
 */
public abstract class ECKeyTemplate extends AsymmKeyTemplate<ECParameterSpec> {

    protected boolean secretECCurve = false;

    public ECKeyTemplate(String label, ECParameterSpec spec) {
        super(label, spec);
        add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_EC));
    }


    public String getAlgorithm() {
        return "EC";
    }

    public ECKeyTemplate getAsTokenOrSessionTemplate(boolean isToken){
        return (ECKeyTemplate) this
                .add(new CK_ATTRIBUTE(CKA_TOKEN, isToken));
    }

    public ECKeyTemplate getAsExplicitECParameters() {
        return initECParamsAttribute(spec);
    }

    protected ECKeyTemplate initECParamsAttribute(ECParameterSpec ecParameterSpec) {
        byte[] paramsencoded = ECParameters.encodeECParameterSpec(ecParameterSpec);
        add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EC_PARAMS, paramsencoded));
        return this;
    }

    public void initIDAttribute(ECPublicKey ecPublicKey){
        try {
            // add encoded params
            byte[] encodedparams = ECParameters.encodeParameters(ecPublicKey.getParams());
            add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ECDSA_PARAMS,encodedparams));

            byte[] id = ECUtil.generatePKCS11ID(ecPublicKey);

            add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ID,id));
        }
        catch (Exception exc){
            throw  new RuntimeException("Error while setting ID from ECPoint",exc);
        }
    }

    protected ECPublicKey getECPublicKey(PublicKey aPublic) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if(aPublic instanceof ECPublicKey){
            return (ECPublicKey) aPublic;
        } else {
            Class ecPublicKeyImpClass = Class.forName("sun.security.ec.ECPublicKeyImpl");
            Constructor declaredConstructor = ecPublicKeyImpClass.getConstructor(byte[].class);
            return (ECPublicKey) declaredConstructor.newInstance(aPublic.getEncoded());
        }
    }

    protected ECPrivateKey getECPrivateKey(PrivateKey aPrivate) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if(aPrivate instanceof ECPrivateKey){
            return (ECPrivateKey) aPrivate;
        }
        else {
            Class ecPrivateKeyImpClass = Class.forName("sun.security.ec.ECPrivateKeyImpl");
            Constructor declaredConstructor = ecPrivateKeyImpClass.getConstructor(byte[].class);
            return (ECPrivateKey) declaredConstructor.newInstance(aPrivate.getEncoded());
        }
    }

    public ECKeyTemplate getAsSecretECCurve(boolean isSecretECCurve) {
        this.secretECCurve = isSecretECCurve;
        return this;
    }

    public boolean isSecretECCurve() {
        return secretECCurve;
    }

}
