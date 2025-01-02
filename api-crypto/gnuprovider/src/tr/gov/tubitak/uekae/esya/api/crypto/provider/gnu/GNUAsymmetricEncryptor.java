package tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu;

import gnu.crypto.sig.rsa.EME_OAEP;
import gnu.crypto.sig.rsa.EME_PKCS1_V1_5;
import gnu.crypto.sig.rsa.RSA;
import tr.gov.tubitak.uekae.esya.api.crypto.Cipher;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.*;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

/**
 * @author ayetgin
 */
public class GNUAsymmetricEncryptor extends Cipher
{
    private CipherAlg mCipherAlg;
    private ByteArrayOutputStream mOutputStream;
    private RSAPublicKey mPublicKey;
    private AlgorithmParams mParams;

    public GNUAsymmetricEncryptor(CipherAlg aCipherAlg) throws CryptoException
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
         if (!(aKey instanceof PublicKey))
             throw new ArgErrorException("Expected public key!");

        mPublicKey = (RSAPublicKey)aKey;
    }

    public void init(byte[] aKey, AlgorithmParams aParams) throws CryptoException
    {
         init((new GNUKeyFactory()).decodePublicKey(AsymmetricAlg.RSA, aKey), aParams);
    }

    @Override
    public void reset() throws CryptoException
    {
        // mOutputStream.reset();  reset keeps internal buffer
        mOutputStream = new ByteArrayOutputStream();
        init(mPublicKey, mParams);
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

            //it was done due to meet the requirements of crypto analysis
            mOutputStream.reset();
            byte [] dummyData = new byte[data.length];
            Arrays.fill(dummyData, (byte)0xCC);
            mOutputStream.write(dummyData);
            mOutputStream = null;

            Padding padding = mCipherAlg.getPadding();

            if (padding == Padding.NONE){
                byte[] rsaBytes = RSA.encrypt(mPublicKey, new BigInteger(data)).toByteArray();
                if(rsaBytes[0] == 0 && rsaBytes.length * 8 > KeyUtil.getKeyLength(mPublicKey))
                    rsaBytes = Arrays.copyOfRange(rsaBytes, 1, rsaBytes.length);

                return rsaBytes;
            } else if (padding == Padding.PKCS1){
                EME_PKCS1_V1_5 pad = EME_PKCS1_V1_5.getInstance(mPublicKey);
                // Örnek olarak, SUN Crypto Provider 1024 anahtar boyutundaki anahtarlar için 129 byte uzunluğunda
                // input aldığında hata vermektedir. GNU negatif çıkan RSA sonuçları için başına işaret
                // byte'ı olarak 0 eklemektedir. Bu byte varsa çıkartıldı.
                byte[] rsaBytes = RSA.encrypt(mPublicKey, new BigInteger(pad.encode(data))).toByteArray();
                if(rsaBytes[0] == 0 && rsaBytes.length * 8 > KeyUtil.getKeyLength(mPublicKey))
                    rsaBytes = Arrays.copyOfRange(rsaBytes, 1, rsaBytes.length);

                return rsaBytes;
            }
            else if (padding instanceof OAEPPadding){
                int blockSize = (mPublicKey.getModulus().bitLength() + 7) / 8;
                OAEPPadding opadding = (OAEPPadding)padding;
                String mdName = GNUProviderUtil.resolveDigestName(opadding.getDigestAlg());
                EME_OAEP pad = EME_OAEP.getInstance(mdName, blockSize);
                // Örnek olarak, SUN Crypto Provider 1024 anahtar boyutundaki anahtarlar için 129 byte uzunluğunda
                // input aldığında hata vermektedir. GNU negatif çıkan RSA sonuçları için başına işaret
                // byte'ı olarak 0 eklemektedir. Bu byte varsa çıkartıldı.
                byte[] rsaBytes =  RSA.encrypt(mPublicKey, new BigInteger(pad.encode(data))).toByteArray();
                if(rsaBytes[0] == 0 && rsaBytes.length * 8 > KeyUtil.getKeyLength(mPublicKey))
                    rsaBytes = Arrays.copyOfRange(rsaBytes, 1, rsaBytes.length);

                return rsaBytes;
            }
        } catch (Exception x){
            throw new CryptoException("RSA encryption error", x);
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
