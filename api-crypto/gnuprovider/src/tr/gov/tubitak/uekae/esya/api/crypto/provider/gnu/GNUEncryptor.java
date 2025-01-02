package tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu;

import gnu.crypto.mode.GCM;
import gnu.crypto.mode.IMode;
import gnu.crypto.mode.ModeFactory;
import gnu.crypto.pad.IPad;
import gnu.crypto.pad.PadFactory;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.crypto.Cipher;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.AlreadyInitializedException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithGCMSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;

import java.security.InvalidKeyException;
import java.security.Key;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ayetgin
 */
public class GNUEncryptor extends Cipher
{

    private final CipherAlg mAlgoritma;
    private final IPad mPadding;
    private final IMode mMode;
    private int mEncryptedLen;

    private byte[] mKey;
    private byte[] mEncodedKey = null;
    private AlgorithmParams mParams;

    public GNUEncryptor(CipherAlg aAlgoritma) throws CryptoException
    {
        mAlgoritma = aAlgoritma;

        String paddingString = GNUProviderUtil.namePadding(aAlgoritma.getPadding());
        String algoritmaString = GNUProviderUtil.nameAlgorithm(aAlgoritma);
        String modString = GNUProviderUtil.nameMod(aAlgoritma.getMod());

        mPadding = PadFactory.getInstance(paddingString);
        mPadding.init(aAlgoritma.getBlockSize());

        try
        {
            mMode = ModeFactory.getInstance(modString, algoritmaString, aAlgoritma.getBlockSize());
        } catch (IllegalArgumentException ex)
        {
            throw new ArgErrorException(
                    GenelDil.mesaj(GenelDil.BLOKUZUNLUGU_0_ALGI_1_DESTEKLEMIYOR, new String[]
                            {algoritmaString, aAlgoritma.getBlockSize() + ""}
                    ), ex);
        }
        if (mMode == null)
        {
            throw new ArgErrorException();
        }
    }

    public int getBlockSize()
    {
        return mAlgoritma.getBlockSize();
    }

    public boolean isEncryptor()
    {
        return true;
    }

    public CipherAlg getCipherAlgorithm()
    {
        return mAlgoritma;
    }

    public void init(Key aKey, AlgorithmParams aParams) throws CryptoException
    {
        mEncodedKey = aKey.getEncoded();
        init(mEncodedKey, aParams);
    }

    public void init(byte[] aKey, AlgorithmParams aParams) throws CryptoException
    {
        mKey = aKey;
        mParams = aParams;

        int blockSize = mAlgoritma.getBlockSize();
        byte[] iv = null;

        if(aParams instanceof ParamsWithIV){
            iv = ((ParamsWithIV)aParams).getIV();
        } else if(aParams instanceof ParamsWithGCMSpec){
            iv = ((ParamsWithGCMSpec)aParams).getIV();

        }

        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(IMode.KEY_MATERIAL, aKey);
        attr.put(IMode.CIPHER_BLOCK_SIZE, blockSize);
        attr.put(IMode.STATE, IMode.ENCRYPTION);
        attr.put(IMode.IV, iv);
        attr.put(IMode.ALGORITHM_PARAMS, aParams);
        try
        {
            mMode.init(attr);
        } catch (IllegalStateException ex)
        {
            throw new AlreadyInitializedException(ex);
        } catch (InvalidKeyException ex)
        {
            throw new ArgErrorException(GenelDil.mesaj(GenelDil.ANAHTAR_HATALI), ex);
        }
    }

    @Override
    public void reset() throws CryptoException
    {
        mMode.reset();
        init(mKey, mParams);
        mEncryptedLen = 0;
    }

    public byte[] process(byte[] aData) throws CryptoException
    {
        int blockSize = mAlgoritma.getBlockSize();
        if(aData.length % blockSize != 0)
        {
            throw new CryptoException("Verilen veri uzunluğu blocksize kati olmalı");
        }

        byte[] sifreliVeri = new byte[aData.length];
        int blockNumber = aData.length / blockSize;
        byte[] sifrelenenBlock = new byte[blockSize];
        byte[] sifreliBlock = new byte[blockSize];
        for(int i=0; i<blockNumber; i++){
            System.arraycopy(aData, i*blockSize, sifrelenenBlock, 0, blockSize);
            mMode.update(sifrelenenBlock, 0, sifreliBlock, 0);
            System.arraycopy(sifreliBlock, 0, sifreliVeri, i*blockSize, blockSize);
        }
        mEncryptedLen += aData.length;
        return sifreliVeri;
    }

    public byte[] doFinal(byte[] aVeriSonu) throws CryptoException {
        int blockSize = mAlgoritma.getBlockSize();
        byte[] sifrelenen = null;
        if(mMode instanceof GCM) {
            sifrelenen = new byte[aVeriSonu.length];
            if(aVeriSonu != null && aVeriSonu.length != 0)
                mMode.update(aVeriSonu, 0, sifrelenen, 0);

            byte[] tagBytes = ((GCM) mMode).doFinal();
            ((ParamsWithGCMSpec)mParams).setTag(tagBytes);
        }
        else {
            sifrelenen = new byte[blockSize];
            int sonLength = aVeriSonu.length;
            byte[] pad = mPadding.pad(null, 0, mEncryptedLen + sonLength);

            if(pad == null || pad.length == 0){
                sifrelenen = new byte[0];
            } else {
                byte[] padli = new byte[blockSize];
                System.arraycopy(aVeriSonu, 0, padli, 0, sonLength);
                System.arraycopy(pad, 0, padli, sonLength, pad.length);
                mMode.update(padli, 0, sifrelenen, 0);
            }

            mMode.reset();
            mEncryptedLen = 0;
        }

        if(mEncodedKey != null)
            Arrays.fill(mEncodedKey, (byte)0xCC);

        return sifrelenen;
    }
}