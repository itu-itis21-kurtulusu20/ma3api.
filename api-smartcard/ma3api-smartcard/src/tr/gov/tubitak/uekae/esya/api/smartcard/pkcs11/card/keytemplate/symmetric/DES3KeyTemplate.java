package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 10/12/12 - 12:20 AM <p>
 * <b>Description</b>: <br>
 * Secret Key Template for AES keys, it contains Secret Key Attributes plus
 * <pre>
 *      CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_DES3)
 * </pre>
 * with keys sizes fixed as 24
 */
public class DES3KeyTemplate extends SecretKeyTemplate {
    private static final int KEY_SIZE = 24;

    public DES3KeyTemplate(String label) {
        super(label, KEY_SIZE);
        this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_DES3));
    }

    public String getAlgorithm() {
        return "DES";
    }

    /**
     *  label and value to import DES3 key
     * @param label
     * @param value
     */
    public DES3KeyTemplate(String label, byte[] value) {
        super(label, KEY_SIZE);
        if(getKeySize() != KEY_SIZE)
            throw new IllegalArgumentException("AES Key Size can be "+KEY_SIZE);
        this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_DES3));
        getAsImportTemplate(value);
    }

    /**
     * CKM_DES3_KEY_GEN
     * @return   CKM_DES3_KEY_GEN
     */
    @Override
    public long getGenerationMechanism() {
        return PKCS11Constants.CKM_DES3_KEY_GEN;
    }
}
