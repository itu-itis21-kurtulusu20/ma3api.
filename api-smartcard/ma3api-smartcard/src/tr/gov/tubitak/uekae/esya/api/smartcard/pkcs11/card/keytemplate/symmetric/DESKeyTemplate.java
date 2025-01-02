package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;

public class DESKeyTemplate extends SecretKeyTemplate {
    private static final int KEY_SIZE = 8;

    public DESKeyTemplate(String label) {
        super(label, KEY_SIZE);
        this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_DES));
    }

    public String getAlgorithm() {
        return "DES";
    }

    public DESKeyTemplate(String label, byte[] value) {
        super(label, KEY_SIZE);
        if (getKeySize() != KEY_SIZE)
            throw new IllegalArgumentException("DES key size can be " + KEY_SIZE);
        this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_DES));
        getAsImportTemplate(value);
    }

    @Override
    public long getGenerationMechanism() {
        return PKCS11Constants.CKM_DES_KEY_GEN;
    }
}
