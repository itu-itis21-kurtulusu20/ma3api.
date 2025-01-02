package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.KeyPairTemplate;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 10/12/12 - 6:52 PM <p>
 * <b>Description</b>: <br>
 */
public class ECKeyPairTemplate<Public extends ECPublicKeyTemplate, Private extends ECPrivateKeyTemplate> extends KeyPairTemplate<Public, Private> {
    public ECKeyPairTemplate(Public publicKeyTemplate, Private privateKeyTemplate) {
        super(publicKeyTemplate, privateKeyTemplate);
    }

    public ECKeyPairTemplate(String keyLabel,ECParameterSpec parameterSpec,ECPrivateKey ecPrivateKey,ECPublicKey ecPublicKey) throws IOException, SmartCardException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        super((Public)new ECPublicKeyTemplate(keyLabel,parameterSpec,ecPublicKey),(Private)new ECPrivateKeyTemplate(keyLabel,parameterSpec,ecPrivateKey, ecPublicKey));
    }

    public ECKeyPairTemplate(String keyLabel,ECParameterSpec parameterSpec) throws IOException, SmartCardException {
        super((Public)new ECPublicKeyTemplate(keyLabel, parameterSpec),(Private)new ECPrivateKeyTemplate(keyLabel, parameterSpec));
    }

    public ECKeyPairTemplate(String keyLabel,ECParameterSpec parameterSpec, PrivateKey aPrivate, PublicKey aPublic) throws IOException, NoSuchMethodException, SmartCardException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        super((Public)new ECPublicKeyTemplate(keyLabel, parameterSpec,aPublic),(Private)new ECPrivateKeyTemplate(keyLabel, parameterSpec,aPrivate, aPublic));
    }

    public ECKeyPairTemplate getAsSignerAndVerifierTemplate(){
        getPublicKeyTemplate().getAsVerifierTemplate();
        getPrivateKeyTemplate().getAsSignerTemplate();
        return this;
    }

    public ECKeyPairTemplate getAsTokenOrSessionTemplate(boolean isToken){
        getPublicKeyTemplate().getAsTokenOrSessionTemplate(isToken);
        getPrivateKeyTemplate().getAsTokenOrSessionTemplate(isToken);
        return this;
    }

    public ECKeyPairTemplate getAsTokenTemplate( boolean sign, boolean encrypt,boolean isWrapUnwrapKey){
        getPublicKeyTemplate().getAsTokenTemplate(sign, encrypt, isWrapUnwrapKey);
        getPrivateKeyTemplate().getAsTokenTemplate(sign, encrypt, isWrapUnwrapKey);
        return this;
    }

    public ECKeyPairTemplate getAsSecretECCurveTemplate(boolean isSecretECCurve) {
        getPublicKeyTemplate().getAsSecretECCurve(isSecretECCurve);
        getPrivateKeyTemplate().getAsSecretECCurve(isSecretECCurve);
        return this;
    }
}
