package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;

import java.security.spec.AlgorithmParameterSpec;

import static sun.security.pkcs11.wrapper.PKCS11Constants.*;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 10/11/12 - 12:26 AM <p>
 * <b>Description</b>: <br>
 * Asymmetric Keys template generify EC,RSA,Public,Private etc...
 */
public abstract class AsymmKeyTemplate<T extends AlgorithmParameterSpec> extends KeyTemplate {
    protected T spec;

    public enum KeyType{
        PUBLIC,
        PRIVATE
    }

    /**
     * create AsymmKeyTemplate with label and spec
     * @param label
     * @param spec
     */
    public AsymmKeyTemplate(String label, T spec) {
        super(label);
        this.spec = spec;
    }

    public T getSpec() {
        return spec;
    }

    abstract public KeyType getKeyType();

    public KeyTemplate getAsExtractableTemplate() {
        KeyType keyType = getKeyType();
        if(keyType == KeyType.PUBLIC) {
            add(new CK_ATTRIBUTE(CKA_TOKEN, false));
        }  else if(keyType == KeyType.PRIVATE) {
            add(new CK_ATTRIBUTE(CKA_TOKEN, false));
            add(new CK_ATTRIBUTE(CKA_EXTRACTABLE, true));
            add(new CK_ATTRIBUTE(CKA_SENSITIVE, false));
        } else {
            throw new ESYARuntimeException("Unkown Asymmetric Key Type");
        }

        return this;
    }
}
