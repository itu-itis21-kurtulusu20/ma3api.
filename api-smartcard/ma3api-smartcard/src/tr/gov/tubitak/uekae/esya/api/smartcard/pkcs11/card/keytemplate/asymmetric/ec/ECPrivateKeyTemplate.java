package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate;

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
 * <b>Date</b>: 10/12/12 - 6:52 PM <p>
 * <b>Description</b>: <br>
 */
public class ECPrivateKeyTemplate extends ECKeyTemplate{

    public ECPrivateKeyTemplate(String label, ECParameterSpec ecParameterSpec) {
        super(label, ecParameterSpec);
        initECPrivateKeyBaseFields();
    }

    public ECPrivateKeyTemplate(String keyLabel, ECParameterSpec parameterSpec, ECPrivateKey aPrivate, ECPublicKey aPublic) {
        this(keyLabel, parameterSpec);
        initKeyRelatedFields(aPrivate, aPublic);
    }

    public ECPrivateKeyTemplate(String keyLabel, ECParameterSpec ecParameterSpec, PrivateKey aPrivate, PublicKey aPublicKey) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        this(keyLabel, ecParameterSpec);
        ECPrivateKey ecPrivateKey = getECPrivateKey(aPrivate);
        ECPublicKey ecPublicKey = getECPublicKey(aPublicKey);
        initKeyRelatedFields(ecPrivateKey, ecPublicKey);
    }
    
    public void initKeyRelatedFields(ECPrivateKey aPrivate, ECPublicKey aPublic) {
        initIDAttribute(aPublic);
        initECPrivateKeyValueAttribute(aPrivate);
    }

    private void initECPrivateKeyBaseFields() {
        add(new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_PRIVATE_KEY));
        add(new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true));
        add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, true));
        add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, false));
    }

    private void initECPrivateKeyValueAttribute(ECPrivateKey ecPrivateKey) {
        add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE, ecPrivateKey.getS()));
    }

    public ECKeyTemplate getAsSignerTemplate(){
        add(new CK_ATTRIBUTE(CKA_SIGN, true));
        return this;
    }

    public ECKeyTemplate getAsDecryptorTemplate(){
        add(new CK_ATTRIBUTE(CKA_DECRYPT, true));
        return this;
    }



    public ECPrivateKeyTemplate getAsTokenTemplate(boolean sign, boolean decrypt, boolean isUnwrap) {
        add(new CK_ATTRIBUTE(CKA_TOKEN, true));
        add(new CK_ATTRIBUTE(CKA_SENSITIVE, true));
        add(new CK_ATTRIBUTE(CKA_EXTRACTABLE, false));
        add(new CK_ATTRIBUTE(CKA_DECRYPT, decrypt));
        add(new CK_ATTRIBUTE(CKA_DERIVE, decrypt));
        add(new CK_ATTRIBUTE(CKA_SIGN, sign));
        add(new CK_ATTRIBUTE(CKA_UNWRAP,isUnwrap));
        return this;
    }

    public ECPrivateKeyTemplate getAsUnwrapperTemplate(){
        add(new CK_ATTRIBUTE(CKA_TOKEN, false));
        add(new CK_ATTRIBUTE(CKA_UNWRAP, true));
        return this;

    }

    @Override
    public AsymmKeyTemplate.KeyType getKeyType() {
        return KeyType.PRIVATE;
    }

}
