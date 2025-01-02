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
 *      CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_AES)
 * </pre>
 * with keys sizes can be 16, 24, 32
 */
public class AESKeyTemplate extends SecretKeyTemplate {

    public AESKeyTemplate(String label, int keySize)  {
        super(label, keySize);
        if(keySize != 16 && keySize != 24 && keySize != 32)
            throw new IllegalArgumentException("AES Key Size can be 16, 24 or 32 byte");
        this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_AES));
    }

    public AESKeyTemplate(String label)  {
        super(label);
        this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_AES));
    }

    public String getAlgorithm() {
        return "AES";
    }

    /**
     * label and value to import AES key
     * @param label
     * @param value
     */
    public AESKeyTemplate(String label, byte[] value)  {
        this(label, value.length);
        getAsImportTemplate(value);
    }

    /**
     * CKM_AES_KEY_GEN as mechanism
     * @return
     */
    @Override
    public long getGenerationMechanism() {
        return PKCS11Constants.CKM_AES_KEY_GEN;
    }
}
