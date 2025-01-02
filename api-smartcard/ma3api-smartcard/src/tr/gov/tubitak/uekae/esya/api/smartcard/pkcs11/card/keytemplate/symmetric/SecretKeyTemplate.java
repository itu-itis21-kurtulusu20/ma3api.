package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;

import javax.crypto.SecretKey;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 10/12/12 - 12:19 AM <p>
 * <b>Description</b>: <br>
 * Template for Secret Keys. Assuming all key have : <pre>
 * CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true);
 * CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_SECRET_KEY);
 * CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE_LEN, getKeySize();
 * </pre>
 */
public abstract class SecretKeyTemplate extends KeyTemplate implements SecretKey {
    private int keySize;

    protected SecretKeyTemplate(String label, int keySize) {
        this(label);
        this.keySize = keySize;
        this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE_LEN, getKeySize()));
    }

    protected SecretKeyTemplate(String label) {
        super(label);
        this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true));
        this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_SECRET_KEY));
    }

    /**
     * adds CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true)
     *
     * @return itself
     */
    public SecretKeyTemplate getAsCreationTemplate() {
        return (SecretKeyTemplate) this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));
    }

    /**
     * Adds  as token
     * <pre>
     *     CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true)
     *     CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE, value)
     * </pre>
     *
     * @param value to import as key
     * @return itself
     */
    public SecretKeyTemplate getAsImportTemplate(byte[] value) {
        return (SecretKeyTemplate) this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true))
                .add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE, value));
    }

    /**
     * convert to unwrapper key template,adds
     * <pre>
     * CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false)
     * CK_ATTRIBUTE(PKCS11Constants.CKA_UNWRAP, true) </pre>
     * Note: wrapper/unwrapper keys should not be token.
     *
     * @return
     */
    public SecretKeyTemplate getAsUnwrapperTemplate() {
        return (SecretKeyTemplate) this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false))
                .add(new CK_ATTRIBUTE(PKCS11Constants.CKA_UNWRAP, true));
    }

    /**
     * convert to wrapper key template,adds
     * <pre>
     * CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false)
     * CK_ATTRIBUTE(PKCS11Constants.CKA_WRAP, true) </pre>
     * Note: wrapper/unwrapper keys should not be token.
     *
     * @return
     */
    public SecretKeyTemplate getAsWrapperTemplate() {
        return (SecretKeyTemplate) this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false))
                .add(new CK_ATTRIBUTE(PKCS11Constants.CKA_WRAP, true));
    }

    public boolean isWrapperOrUnWrapper(){
        if(attributes.containsKey(PKCS11Constants.CKA_WRAP)){
            CK_ATTRIBUTE ck_attribute = attributes.get(PKCS11Constants.CKA_WRAP);
            if((Boolean)ck_attribute.pValue == true)
                return true;
        }

        if(attributes.containsKey(PKCS11Constants.CKA_UNWRAP)){
            CK_ATTRIBUTE ck_attribute = attributes.get(PKCS11Constants.CKA_UNWRAP);
            if((Boolean)ck_attribute.pValue == true)
                return true;
        }

        return false;
    }

    /**
     * convert to wrapper key template, adds
     * <pre>
     * CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false)
     * CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true) </pre>
     * Note: exportable shall not be token.
     * @return
     */
    public SecretKeyTemplate getAsExportableTemplate() {
        return (SecretKeyTemplate) this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false))
                .add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true));
    }

    public int getKeySize() {
        return keySize;
    }

    public SecretKeyTemplate getAsTokenTemplate(boolean signAndVerify, boolean encryptAndDecrypt, boolean wrapAndUnwrap){
        add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));
        add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN, signAndVerify));
        add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY, signAndVerify));
        add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ENCRYPT, encryptAndDecrypt));
        add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, encryptAndDecrypt));
        add(new CK_ATTRIBUTE(PKCS11Constants.CKA_WRAP, wrapAndUnwrap));
        add(new CK_ATTRIBUTE(PKCS11Constants.CKA_UNWRAP,wrapAndUnwrap));
        return this;
    }

    /**
     * Generation mechanism in PKCS11
     * @return
     */
    abstract public long getGenerationMechanism();

    public SecretKeyTemplate getAsDecryptorTemplate(){
        return (SecretKeyTemplate) this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false))
                .add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, true));
    }
}
