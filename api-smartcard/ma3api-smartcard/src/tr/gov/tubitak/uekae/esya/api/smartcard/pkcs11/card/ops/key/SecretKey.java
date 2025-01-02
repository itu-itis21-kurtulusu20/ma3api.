/**
 * Abstract class for generating/importing secret keys.
 */
package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key;



import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;

import java.util.ArrayList;
import java.util.List;

/**
 *  @deprecated do no use or implements,
 *  @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate
 * @author aslihan.kubilay
 *
 */
@Deprecated
abstract public class SecretKey 
{
    String mLabel = null;
    byte[] mValue = null;
    int mKeySize = 0;


    /**
     * getCreationTemplate returns template for key generation in token
     *  @deprecated do no use or implements,
     *  @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate
     * @return template for key generation
     */
    @Deprecated
    public List<CK_ATTRIBUTE> getCreationTemplate() {
        List<CK_ATTRIBUTE> list = new ArrayList<CK_ATTRIBUTE>();

        list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));
        list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_SECRET_KEY));
        list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, getLabel()));
        list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE_LEN, getKeySize()));
        list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true));
        return list;

    }

    /**
     * getImportTemplate return template for importing key to token.
     *  @deprecated do no use or implements,
     *  @see tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate
     * @return template for importing key
     */
    @Deprecated
    public List<CK_ATTRIBUTE> getImportTemplate() {
        List<CK_ATTRIBUTE> list = new ArrayList<CK_ATTRIBUTE>();

        list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));
        list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_PRIVATE, true));
        list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_SECRET_KEY));
        list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, getLabel()));
        list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, getKeyType()));
        list.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE, getValue()));
        return list;

    }

    /**
     * getKeySize returns key length in bytes.
     *
     * @return key length in bytes
     */
    public int getKeySize() {
        return mKeySize;
    }

    /**
     * getLabel returns key label
     *
     * @return key label
     */
    public String getLabel() {
        return mLabel;
    }

    /**
     * getValue returns key value to be imported to token
     *
     * @return key value
     */
    public byte[] getValue() {
        return mValue;
    }

    abstract public long getGenerationMechanism();

    abstract public long getKeyType();
}
