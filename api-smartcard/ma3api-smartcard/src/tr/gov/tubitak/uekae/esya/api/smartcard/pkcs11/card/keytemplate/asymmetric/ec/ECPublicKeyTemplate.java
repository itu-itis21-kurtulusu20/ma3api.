package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.asn.sun.security.util.DerValue;
import tr.gov.tubitak.uekae.esya.api.asn.sun.security.util.ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.ECParameters;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.AsymmKeyTemplate;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.util.Map;

import static sun.security.pkcs11.wrapper.PKCS11Constants.*;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 10/12/12 - 6:52 PM <p>
 * <b>Description</b>: <br>
 */
public class ECPublicKeyTemplate extends ECKeyTemplate{

    protected ECPublicKey ecPublicKey;

    public ECPublicKeyTemplate(String label, ECParameterSpec ecParameterSpec) throws SmartCardException {
        super(label, ecParameterSpec);
        initECPublicKeyBaseFields();
        initECParamsAttribute(ecParameterSpec);
    }



    public ECPublicKeyTemplate(String keyLabel, ECParameterSpec parameterSpec, ECPublicKey ecPublicKey) throws SmartCardException {
        this(keyLabel, parameterSpec);
        this.ecPublicKey = ecPublicKey;
        initKeyRelatedFields(ecPublicKey);
    }

    public ECPublicKeyTemplate(String keyLabel,ECParameterSpec parameterSpec, PublicKey aPublic) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException, SmartCardException {
        this(keyLabel, parameterSpec);
        this.ecPublicKey = getECPublicKey(aPublic);
        initKeyRelatedFields(ecPublicKey);
    }

    private void initECPublicKeyBaseFields() {
        add(new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_PUBLIC_KEY));
        add(new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, false));
    }

    private void initKeyRelatedFields(ECPublicKey publicKey) {
        initECParamsAttribute(this.spec);
        initECPointAttribute(publicKey.getW(), this.spec);
        initIDAttribute(publicKey);


        // Buranın tam olarak nasıl çalıştığı bilinmediği için düzenlenmedi.
        // Secret Curve Özelliği eklendi.
        boolean isPrivate=false;
        ECParameterSpec publicParams = spec;
        if(publicParams instanceof NamedCurve){
            isPrivate=true;//Eğer bilinen bir curve değil ise public key i private yapıyoruz.
            Map<String, NamedCurve> namedCurves = NamedCurve.getNameCurves();
            ObjectIdentifier oid = ((NamedCurve) publicParams).getObjectIdentifier();


            for (NamedCurve c : namedCurves.values()) {
                if(c.getObjectIdentifier().toString().equals(oid.toString())) {
                    isPrivate = false;
                    break;
                }
            }
        }
        if(isPrivate)
            add(new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE,isPrivate));
    }


    public ECKeyTemplate getAsSecretECCurve(boolean isSecretECCurve) {
        super.getAsSecretECCurve(isSecretECCurve);
        if (isSecretECCurve && ecPublicKey != null) {
            initECPointAttribute(ecPublicKey.getW(), this.spec);
        }
        return this;
    }

    protected ECKeyTemplate initECPointAttribute(ECPoint ecpoint, ECParameterSpec spec) {
        byte[] encodedPoint = ECParameters.encodePoint(ecpoint, spec.getCurve(), secretECCurve);

        DerValue pkecpoint = new DerValue(DerValue.tag_OctetString, encodedPoint);
        try {
            add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EC_POINT, pkecpoint.toByteArray()));
        } catch (IOException e) {
            throw new ESYARuntimeException(e);
        }
        return this;
    }



    public ECKeyTemplate getAsVerifierTemplate() {
        add(new CK_ATTRIBUTE(CKA_VERIFY, true));
        return this;
    }

    public ECKeyTemplate getAsEcnryptorTemplate(){
        add(new CK_ATTRIBUTE(CKA_ENCRYPT, true));
        return this;
    }

    public ECPublicKeyTemplate getAsTokenTemplate(boolean verify, boolean encrypt, boolean isWrap){
        add(new CK_ATTRIBUTE(CKA_TOKEN, true));
        add(new CK_ATTRIBUTE(CKA_ENCRYPT, encrypt));
        add(new CK_ATTRIBUTE(CKA_DERIVE, encrypt));
        add(new CK_ATTRIBUTE(CKA_VERIFY, verify));
        add(new CK_ATTRIBUTE(CKA_WRAP,isWrap));
        return this;
    }

    public ECPublicKey getEcPublicKey() {
        return ecPublicKey;
    }

    @Override
    public AsymmKeyTemplate.KeyType getKeyType() {
        return KeyType.PUBLIC;
    }
}
