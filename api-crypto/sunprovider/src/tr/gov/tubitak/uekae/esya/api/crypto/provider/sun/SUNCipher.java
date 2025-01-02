package tr.gov.tubitak.uekae.esya.api.crypto.provider.sun;

import tr.gov.tubitak.uekae.esya.api.crypto.Cipher;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.PBEAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;

import javax.crypto.SecretKey;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Provider;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author ayetgin
 */
public class SUNCipher extends Cipher
{
    private javax.crypto.Cipher mCipher;
    private CipherAlg mCipherAlg;
    private boolean mEncryptor;
    private boolean mDoFinalCalled;

    public SUNCipher(CipherAlg aCipherAlg, boolean aEncryptor) throws CryptoException
    {
        this(aCipherAlg,null,aEncryptor);
    }


    public SUNCipher(CipherAlg aCipherAlg, Provider aProvider,boolean aEncryptor) throws CryptoException
    {
        mCipherAlg = aCipherAlg;
        mEncryptor = aEncryptor;
        String alg = null;

        try {
            if (aCipherAlg instanceof PBEAlg) {
                alg = SUNProviderUtil.getPBEName((PBEAlg)mCipherAlg);
            }
            else {
                alg = SUNProviderUtil.getTransformName(mCipherAlg);
            }

            if(aProvider!=null)
                mCipher = javax.crypto.Cipher.getInstance(alg,aProvider);
            else
                mCipher = javax.crypto.Cipher.getInstance(alg);
        } catch (Exception x){
            throw new CryptoException("Can't construct cipher "+alg, x);
        }
    }

    public void init(Key aKey, AlgorithmParams aParams) throws CryptoException
    {
        AlgorithmParameterSpec spec = null;
        // todo params conversion ..
        if (aParams instanceof ParamsWithIV){
            byte[] iv = ((ParamsWithIV) aParams).getIV();

            if (iv!=null)
                spec = new IvParameterSpec(iv);
            else
                spec = new IvParameterSpec(new byte[getBlockSize()]);

        }

        try {
            if (aKey instanceof PBEKey){
                aKey = new SecretKeySpec(aKey.getEncoded(), SUNProviderUtil.getCipherName(mCipherAlg));
            }
            if (mEncryptor)
                mCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, aKey, spec);
            else
                mCipher.init(javax.crypto.Cipher.DECRYPT_MODE, aKey, spec);
        } catch (Exception x){
            throw new CryptoException("Init error", x);
        }
    }

    public void init(byte[] aKey, AlgorithmParams aParams) throws CryptoException
    {
        try {

            SecretKey key = new SecretKeySpec(aKey, SUNProviderUtil.getCipherName(mCipherAlg));
            init(key, aParams);

        } catch (Exception x){
            throw new CryptoException("Key construction error", x);
        }
    }

    @Override
    public void reset() throws CryptoException
    {
        try {
            if (!mDoFinalCalled)
                mCipher.doFinal();  // this should reset underlying cipher
            mDoFinalCalled = false;
        } catch (Exception x){
            throw new CryptoException("Error resetting cipher ", x);
        }
    }

    public byte[] process(byte[] aData) throws CryptoException
    {
            return mCipher.update(aData);
    }

    public int getBlockSize()
    {
        return mCipher.getBlockSize();
    }

    public boolean isEncryptor()
    {
        return mEncryptor;
    }

    public byte[] doFinal(byte[] aData) throws CryptoException
    {
        try {
            byte[] result =  mCipher.doFinal(aData);
            mDoFinalCalled = true;
            return result;
        } catch (Exception x){
            throw new CryptoException("Error on cipher doFinal", x);
        }
    }

    public CipherAlg getCipherAlgorithm()
    {
        return mCipherAlg;
    }
}
