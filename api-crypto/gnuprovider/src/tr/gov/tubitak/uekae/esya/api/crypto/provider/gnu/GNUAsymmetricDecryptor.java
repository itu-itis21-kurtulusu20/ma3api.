package tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu;

import gnu.crypto.sig.rsa.EME_OAEP;
import gnu.crypto.sig.rsa.EME_PKCS1_V1_5;
import gnu.crypto.sig.rsa.RSA;
import tr.gov.tubitak.uekae.esya.api.crypto.Cipher;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.*;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.interfaces.RSAPrivateKey;

/**
 * @author ayetgin
 */
public class GNUAsymmetricDecryptor extends Cipher
{
    private CipherAlg mCipherAlg;
    private ByteArrayOutputStream mOutputStream;
    private RSAPrivateKey mPrivateKey;
    private AlgorithmParams mParams;

    public GNUAsymmetricDecryptor(CipherAlg aCipherAlg) throws CryptoException
    {
        mCipherAlg = aCipherAlg;
        Padding padding = mCipherAlg.getPadding();
        if (padding == Padding.PKCS1 || padding == Padding.NONE){
            //
        }
        else if (padding instanceof OAEPPadding){
            // todo
            OAEPPadding opadding = (OAEPPadding)padding;
            MGF mgf = opadding.getMaskGenerationFunction();
            if (mgf != MGF.MGF1){
                throw new ArgErrorException("Unsupported mask geenration function! "+mgf);
            }
        }
        else {
            throw new ArgErrorException("Unsupported padding scheme! "+padding);
        }
        mOutputStream = new ByteArrayOutputStream();
    }

    public void init(Key aKey, AlgorithmParams aParams) throws CryptoException
    {
         if (!(aKey instanceof RSAPrivateKey))
             throw new ArgErrorException("Expected private key!");

        mPrivateKey = (RSAPrivateKey)aKey;
        mParams = aParams;
    }

    public void init(byte[] aKey, AlgorithmParams aParams) throws CryptoException
    {
         init((new GNUKeyFactory()).decodePrivateKey(AsymmetricAlg.RSA, aKey), aParams);
    }

    @Override
    public void reset() throws CryptoException
    {
        //mOutputStream.reset(); reset keeps internal buffer
        mOutputStream = new ByteArrayOutputStream();
        init(mPrivateKey, mParams);
    }

    public byte[] process(byte[] aData) throws CryptoException
    {
        try {
            mOutputStream.write(aData);
        } catch (IOException x){
            x.printStackTrace();
        }
        return null;
    }

    public byte[] doFinal(byte[] aData) throws CryptoException
    {
        try {
            mOutputStream.write(aData);
            byte[] data = mOutputStream.toByteArray();
            mOutputStream = null;

            byte[] cozoncesi = RSA.decrypt(mPrivateKey, new BigInteger(1, data)).toByteArray();
            int blockSize = (mPrivateKey.getModulus().bitLength() + 7) / 8;
            int diff = blockSize - cozoncesi.length;
            byte[] coz = new byte[blockSize];
            System.arraycopy(cozoncesi, 0, coz, diff, cozoncesi.length);
           
            Padding padding = mCipherAlg.getPadding();
            if(padding == Padding.NONE){
                return coz;
            } else if (padding == Padding.PKCS1){
                EME_PKCS1_V1_5 pad = EME_PKCS1_V1_5.getInstance(mPrivateKey);
                return pad.decode(coz);
            }
            else if (padding instanceof OAEPPadding){
                OAEPPadding opadding = (OAEPPadding)padding;
                String mdName = GNUProviderUtil.resolveDigestName(opadding.getDigestAlg());
                EME_OAEP pad = EME_OAEP.getInstance(mdName, blockSize);
                return pad.decode(coz);
            }
        } catch (Exception x){
            throw new CryptoException("RSA decryption error", x);
        }
        return new byte[0];
    }

    public int getBlockSize()
    {
        return 0;
    }

    public boolean isEncryptor()
    {
        return true;
    }

    public CipherAlg getCipherAlgorithm()
    {
        return mCipherAlg;
    }
}