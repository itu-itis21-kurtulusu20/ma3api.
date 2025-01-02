package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa;

import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.KeyPairTemplate;

import java.io.Serializable;
import java.security.spec.RSAKeyGenParameterSpec;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 10/11/12 - 11:40 PM <p>
 * <b>Description</b>: <br>
 */
public class RSAKeyPairTemplate extends KeyPairTemplate<RSAPublicKeyTemplate, RSAPrivateKeyTemplate> implements Serializable{

    public RSAKeyPairTemplate(String label, RSAKeyGenParameterSpec spec) {
        super(new RSAPublicKeyTemplate(label,spec), new RSAPrivateKeyTemplate(label,spec));
    }

    public RSAKeyPairTemplate(RSAPublicKeyTemplate publicKeyTemplate, RSAPrivateKeyTemplate privateKeyTemplate) {
        super(publicKeyTemplate, privateKeyTemplate);
    }

    public RSAKeyPairTemplate getAsTokenTemplate( boolean sign, boolean encrypt,boolean isWrapUnwrapKey){
        getPublicKeyTemplate().getAsTokenTemplate(sign,encrypt,isWrapUnwrapKey);
        getPrivateKeyTemplate().getAsTokenTemplate(sign, encrypt,isWrapUnwrapKey);
        return this;
    }

/*    public static RSAKeyPairTemplate getUserImportTemplate(String label, RSAPrivateCrtKey privKey, X509Certificate certificate,  BigInteger modulus, boolean sign, boolean encrypt){
        RSAKeyGenParameterSpec parameterSpec = new RSAKeyGenParameterSpec(privKey.getModulus().bitLength(), privKey.getPublicExponent());
        return new RSAKeyPairTemplate(
                RSAPublicKeyTemplate.getTokenImportTemplate(label, parameterSpec, modulus, sign, encrypt),
                RSAPrivateKeyTemplate.getTokenImportTemplate(label, privKey, certificate, sign, encrypt)
        );
    }*/

    public RSAKeyPairTemplate getAsWrapperTemplate(){
        getPublicKeyTemplate().getAsWrapperTemplate();
        getPrivateKeyTemplate().getAsUnwrapperTemplate();
        return this;
    }
}
