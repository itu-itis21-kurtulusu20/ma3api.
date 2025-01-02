package tr.gov.tubitak.uekae.esya.api.mkencryptedpackage;

import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;

public class EncryptedPackageConfig {

    public static final EncryptedPackageConfig VERSION_ONE = new EncryptedPackageConfig(1, CipherAlg.AES256_GCM, WrapAlg.AES256, 96, 96, true);


    // instance variable

    private int version;

    private CipherAlg dataCipherAlg;
    private WrapAlg keyWrapAlg;

    private int tagLength;
    private int ivLength;

    private EncryptedPackageConfig(int version, CipherAlg dataCipherAlg, WrapAlg keyWrapAlg, int tagLength, int ivLength, boolean priv) {
        init(version, dataCipherAlg, keyWrapAlg, tagLength, ivLength);
    }

    public EncryptedPackageConfig(int version, CipherAlg dataCipherAlg, WrapAlg keyWrapAlg, int tagLength, int ivLength) {
        if (version < 128)
            throw new ESYARuntimeException("Version must be above 128. Below 128 is reserved for internal definitions.");
        init(version, dataCipherAlg, keyWrapAlg, tagLength, ivLength);
    }

    private void init(int version, CipherAlg dataCipherAlg, WrapAlg keyWrapAlg, int tagLength, int ivLength) {
        this.version = version;
        this.dataCipherAlg = dataCipherAlg;
        this.keyWrapAlg = keyWrapAlg;
        this.tagLength = tagLength;
        this.ivLength = ivLength;
    }

    public int getVersion() {
        return version;
    }

    public CipherAlg getDataCipherAlg() {
        return dataCipherAlg;
    }

    public WrapAlg getKeyWrapAlg() {
        return keyWrapAlg;
    }

    public int getTagLength() {
        return tagLength;
    }

    public int getIvLength() {
        return ivLength;
    }
}
