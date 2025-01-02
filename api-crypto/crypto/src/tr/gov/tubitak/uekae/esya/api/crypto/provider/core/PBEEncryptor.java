package tr.gov.tubitak.uekae.esya.api.crypto.provider.core;

import tr.gov.tubitak.uekae.esya.api.crypto.Cipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.PBEAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

import javax.crypto.interfaces.PBEKey;
import java.security.Key;

/**
* @author ayetgin
*/
public class PBEEncryptor extends Cipher
{
    private PBEAlg mPBEAlg;
    private Cipher mCipher;


    public PBEEncryptor(PBEAlg aPBEAlg) throws CryptoException
    {
        mPBEAlg = aPBEAlg;
        mCipher = Crypto.getEncryptor(mPBEAlg.getCipherAlg()).getInternalCipher();
    }

    public void init(Key aKey, AlgorithmParams aParams) throws CryptoException
    {
        if (aKey==null || !(aKey instanceof PBEKey))
            throw new ArgErrorException("Invalid PBE key: "+aKey);

        mCipher.init(aKey, aParams);
    }

    public void init(byte[] aKey, AlgorithmParams aParams) throws CryptoException
    {
        throw new ArgErrorException("Invalid key, expected PBKey: ");
    }

    @Override
    public void reset() throws CryptoException
    {
        mCipher.reset();
    }

    public byte[] process(byte[] aData) throws CryptoException
    {
        return mCipher.process(aData);
    }

    public byte[] doFinal(byte[] aData) throws CryptoException
    {
        return mCipher.doFinal(aData);
    }

    public int getBlockSize()
    {
        return mCipher.getBlockSize();
    }

    public boolean isEncryptor()
    {
        return true;
    }

    public CipherAlg getCipherAlgorithm()
    {
        return mPBEAlg;
    }
}
