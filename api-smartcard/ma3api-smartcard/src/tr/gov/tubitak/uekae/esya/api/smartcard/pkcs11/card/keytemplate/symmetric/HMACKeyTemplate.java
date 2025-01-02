package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric;

import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 10/12/12 - 12:21 AM <p>
 * <b>Description</b>: <br>
 * Secret Key Template for HMAC keys, it contains Secret Key Attributes plus
 * <pre>
 *      CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, PKCS11Constants.CKK_GENERIC_SECRET) // or user/card specified
 * </pre>
 * with keys sizes can be 16, 24, 32
 */
public class HMACKeyTemplate extends SecretKeyTemplate {

    /**
     * Digest alg to hmac operations
     */
    String digestAlg = Algorithms.DIGEST_SHA1;
    long keyType = PKCS11Constants.CKK_GENERIC_SECRET;
    long generationMechanism = PKCS11Constants.CKM_GENERIC_SECRET_KEY_GEN;

    /**
     * create HMACKeyTemplate with label and keysize
     * @param label
     */
    public HMACKeyTemplate(String label) {
        super(label);
        this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, keyType));
    }

    /**
     * create HMACKeyTemplate with label and keysize
     * @param label
     * @param keySize
     */
    public HMACKeyTemplate(String label, int keySize) {
        super(label, keySize);
        this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, keyType));
    }

    /**
     * HMACKeyTemplate importer with key
     * @param label
     * @param key
     */
    public HMACKeyTemplate(String label, byte[] key) {
        this(label, key.length);
        this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE, key));
    }

    /**
     * create HMACKeyTemplate with digestAlg, DigestAlg matters in some HSMs
     * @param label
     * @param keySize
     * @param digestAlg
     */
    public HMACKeyTemplate(String label, int keySize, String digestAlg) {
        this(label, keySize);
        this.digestAlg = digestAlg;
    }

    /**
     *  create HMACKeyTemplate importer with digestAlg, DigestAlg matters in some HSMs
     * @param label
     * @param key
     * @param digestAlg
     */
    public HMACKeyTemplate(String label, byte[] key, String digestAlg) {
        this(label, key.length);
        this.digestAlg = digestAlg;
        this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE, key));
    }

    /**
     * convert as HMAC Signer Template, HMACKeyTemplate plus:
     * <pre>
     *  CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN,true)
     *  CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY,true)
     * </pre>
     * @return
     */
    public KeyTemplate getAsSignerTemplate() {
        return super.getAsCreationTemplate()
                .add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN,true))
                .add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY,true));
    }

    @Override
    public long getGenerationMechanism() {
        return generationMechanism;
    }

    public void setGenerationMechanism(long generationMechanism) {
        this.generationMechanism = generationMechanism;
    }

    public long getKeyType() {
        return keyType;
    }

    public void setKeyType(long keyType)
    {
        this.keyType = keyType;
        this.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_KEY_TYPE, keyType));
    }

    public String getAlgorithm() {
        return "Generic"; // or HMAC?
    }

    public String getDigestAlg()
    {
        return digestAlg;
    }


}
