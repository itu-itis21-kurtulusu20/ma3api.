package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric;

import java.io.Serializable;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 10/12/12 - 1:26 AM <p>
 * <b>Description</b>: <br>
 * Template to carry KeyPair with same type
 */
public class KeyPairTemplate<Public extends AsymmKeyTemplate, Private extends AsymmKeyTemplate> implements Serializable {
    private Public publicKeyTemplate;
    private Private privateKeyTemplate;

    /**
     * Template creation, parameters can be null
     * @param publicKeyTemplate
     * @param privateKeyTemplate
     */
    public KeyPairTemplate(Public publicKeyTemplate, Private privateKeyTemplate) {
        this.publicKeyTemplate = publicKeyTemplate;
        this.privateKeyTemplate = privateKeyTemplate;
    }

    public Public getPublicKeyTemplate() {
        return publicKeyTemplate;
    }

    public Private getPrivateKeyTemplate() {
        return privateKeyTemplate;
    }

    public KeyPairTemplate getAsExtractableTemplate(){
        getPublicKeyTemplate().getAsExtractableTemplate();
        getPrivateKeyTemplate().getAsExtractableTemplate();
        return this;
    }
}
