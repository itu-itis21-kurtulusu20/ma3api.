package tr.gov.tubitak.uekae.esya.api.crypto.provider.sun;

import tr.gov.tubitak.uekae.esya.api.crypto.Wrapper;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Algorithm;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Provider;

/**
 * @author ayetgin
 */
public class SUNWrapper implements Wrapper {
    private Cipher mWrapper;
    private Algorithm mWrapAlg;
    private boolean mWrapMode;

    public SUNWrapper(Algorithm aWrapAlg, boolean aWrapMode) {
        mWrapAlg = aWrapAlg;
        mWrapMode = aWrapMode;
    }

    public SUNWrapper(Algorithm aWrapAlg, Provider aProvider, boolean aWrapMode) throws CryptoException {
        mWrapAlg = aWrapAlg;
        mWrapMode = aWrapMode;
        try {
            if (aProvider != null)
                mWrapper = Cipher.getInstance(mWrapAlg.getName(), aProvider);
            else
                mWrapper = Cipher.getInstance(mWrapAlg.getName());
        } catch (Exception e) {
            throw new CryptoException("Cannot init Wrapper:" + e.getMessage(), e);
        }

    }

    public void init(Key aKey) throws CryptoException {
        try {
            int mode = mWrapMode ? Cipher.WRAP_MODE : Cipher.UNWRAP_MODE;
            mWrapper.init(mode, aKey);
        } catch (Exception x) {
            throw new CryptoException("Cant init wrapper", x);
        }
    }

    public void init(Key aKey, AlgorithmParams aParams) throws CryptoException {
        try {
            int mode = mWrapMode ? Cipher.WRAP_MODE : Cipher.UNWRAP_MODE;
            mWrapper.init(mode, aKey, aParams);
        } catch (Exception x) {
            throw new CryptoException("Cant init wrapper", x);
        }
    }

    public byte[] wrap(Key aKey) throws CryptoException {
        try {
            return mWrapper.wrap(aKey);
        } catch (Exception x) {
            throw new CryptoException("Cant wrap", x);
        }
    }

    public Key unwrap(byte[] data) throws CryptoException {
        try {
            return mWrapper.unwrap(data, mWrapAlg.getName(), Cipher.SECRET_KEY);
        } catch (Exception x) {
            throw new CryptoException("Cant wrap", x);
        }
    }

    public Key unwrap(byte[] data, Object keyTemplateObj) throws CryptoException {
        return unwrap(data);
    }

    public byte[] process(byte[] aKey) throws CryptoException {
        try {
            if (mWrapMode) {
                return mWrapper.wrap(new SecretKeySpec(aKey, mWrapAlg.getName()));
            } else {
                Key key = mWrapper.unwrap(aKey, mWrapAlg.getName(), Cipher.SECRET_KEY);
                return key.getEncoded();
            }
        } catch (Exception x) {
            throw new CryptoException("Cant wrap", x);
        }
    }

    public boolean isWrapper() {
        return mWrapMode;
    }
}
