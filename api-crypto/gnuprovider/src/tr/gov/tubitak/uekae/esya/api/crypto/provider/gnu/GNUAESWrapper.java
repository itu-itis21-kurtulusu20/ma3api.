package tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu;

import gnu.crypto.wrapper.AESWrapper;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.crypto.Cipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Wrapper;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Algorithm;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.UnknownElement;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * @author ayetgin
 */
public class GNUAESWrapper implements Wrapper {
    AESWrapper mWrapper;
    Cipher cipher;

    Key mWrapperKey;
    boolean mForWrapping;


    public GNUAESWrapper(Algorithm aWrapAlg, boolean aForWrapping) throws CryptoException {
        if ( aWrapAlg.getName().startsWith("AES"))
            mWrapper = new AESWrapper();
        else if(aWrapAlg instanceof CipherAlg || aWrapAlg instanceof WrapAlg)  {
            CipherAlg cipherAlg  ;
            if(aWrapAlg instanceof WrapAlg)
                cipherAlg = ((WrapAlg) aWrapAlg).getCipherAlg();
            else
                cipherAlg = (CipherAlg) aWrapAlg;
            if(aForWrapping)
                cipher = new GNUCryptoProvider().getEncryptor(cipherAlg);
            else
                cipher =  new GNUCryptoProvider().getDecryptor(cipherAlg);
        }else
            throw new UnknownElement(GenelDil.mesaj(GenelDil.WRAPALG_BILINMIYOR));

        mForWrapping = aForWrapping;
    }

    public void init(Key aKey) throws CryptoException {
        if(mWrapper != null)
            mWrapperKey = aKey;
        else
            cipher.init(aKey, null);
    }

    public void init(Key aKey, AlgorithmParams aParams) throws CryptoException {
        mWrapperKey = aKey;
        if(mWrapper != null) {
            if(aParams instanceof ParamsWithIV)
                mWrapper.setIv(((ParamsWithIV) aParams).getIV());
            else
                throw new IllegalArgumentException("Only ParamsWithIV is supported for ");
        }
        else
            cipher.init(aKey, aParams);



    }

    public byte[] wrap(Key aKey) throws CryptoException {
        try {
            byte[] encodedKey = aKey.getEncoded();
            if(mWrapper != null){
                return mWrapper.wrap(encodedKey, 0, encodedKey.length, mWrapperKey.getEncoded());
            } else
                return cipher.doFinal(encodedKey);
        } catch (Exception e) {
            throw new CryptoException("wrap error", e);
        }
    }

    public Key unwrap(byte[] data) throws CryptoException {
        try {
            byte[] unwrap;
            if(mWrapper != null)
                unwrap = mWrapper.unwrap(data, 0, data.length, mWrapperKey.getEncoded());
            else
                unwrap = cipher.doFinal(mWrapperKey.getEncoded());
            return new SecretKeySpec(unwrap,"AES");
        } catch (Exception e) {
            throw new CryptoException("wrap error", e);
        }
    }

    public Key unwrap(byte[] data, Object keyTemplateObj) throws CryptoException {
        return unwrap(data);
    }

    public boolean isWrapper() {
        return mForWrapping;
    }
}
