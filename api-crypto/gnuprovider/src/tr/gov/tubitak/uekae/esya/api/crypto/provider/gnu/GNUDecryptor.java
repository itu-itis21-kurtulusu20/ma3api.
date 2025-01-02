package tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu;

import gnu.crypto.mode.GCM;
import gnu.crypto.mode.IMode;
import gnu.crypto.mode.ModeFactory;
import gnu.crypto.pad.IPad;
import gnu.crypto.pad.PadFactory;
import gnu.crypto.pad.WrongPaddingException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.GenelDil;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteUtil;
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
public class GNUDecryptor extends Cipher
{
    private byte[] mEncodedKey = null;

    private final CipherAlg mAlgoritma;
    private final IPad mPadding;
    private final IMode mMode;

    private byte[] mKey;
    private AlgorithmParams mParams;

    /**
     * Verilen algoritmayi kullanan bir instanse olusturur.
     * @param algorithm Kullanilacak algoritmanin parametreleri.
     * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException Verilen algoritmayi olustururken bir hata cikarsa atilir.
     */
    public GNUDecryptor(CipherAlg algorithm) throws CryptoException
    {
        this.mAlgoritma = algorithm;

        String paddingString = GNUProviderUtil.namePadding(algorithm.getPadding());
        String algoritmaString = GNUProviderUtil.nameAlgorithm(algorithm);
        String modString = GNUProviderUtil.nameMod(algorithm.getMod());

        mPadding = PadFactory.getInstance(paddingString);
        mPadding.init(this.mAlgoritma.getBlockSize());

        try
        {
            mMode = ModeFactory.getInstance(modString, algoritmaString,
                    this.mAlgoritma.getBlockSize());
        } catch (IllegalArgumentException ex)
        {
            throw new ArgErrorException(
                    GenelDil.mesaj(GenelDil.BLOKUZUNLUGU_0_ALGI_1_DESTEKLEMIYOR,
                            new String[]
                                    {algoritmaString, this.mAlgoritma.getBlockSize() + ""}
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
        return false;
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
        if(aParams instanceof ParamsWithGCMSpec){
            iv = ((ParamsWithGCMSpec)aParams).getIV();
        } else if (aParams instanceof ParamsWithIV){
            iv = ((ParamsWithIV)aParams).getIV();
        }

        Map<String, Object> attr = new HashMap<String, Object>();
        attr.put(IMode.KEY_MATERIAL, aKey);
        attr.put(IMode.CIPHER_BLOCK_SIZE, blockSize);
        attr.put(IMode.STATE, IMode.DECRYPTION);
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
    }


    public byte[] process(byte[] aData, int index, int len)  throws CryptoException
    {
        int blockSize = mAlgoritma.getBlockSize();

        if( len % blockSize != 0)
        {
            throw new CryptoException("Verilen veri uzunluğu blocksize kati olmalı");
        }

        byte[] cozulmusVeri = new byte[len];

        int blockNumber = len / blockSize;

        byte[] sifreliBlock = new byte[blockSize];

        byte[] cozulmusBlock = new byte[blockSize];

        for(int i=0; i<blockNumber;i++){

            System.arraycopy(aData, index+i*blockSize, sifreliBlock, 0, blockSize);

            mMode.update(sifreliBlock, 0, cozulmusBlock, 0);

            System.arraycopy(cozulmusBlock, 0, cozulmusVeri, i*blockSize, blockSize);
        }

        return cozulmusVeri;
    }

    public byte[] process(byte[] aData) throws CryptoException
    {
        return process(aData, 0, aData.length);
    }

    public byte[] doFinal(byte[] aData) throws CryptoException
    {
        byte[] cozulmus = new byte[0];

        if(mMode instanceof GCM){
            if(aData != null && aData.length != 0 && !(aData.length == mMode.currentBlockSize() && ByteUtil.isAllZero(aData))){
                int blockSize = mAlgoritma.getBlockSize();
                int blockNumber = aData.length / blockSize;

                if(blockNumber > 0){
                    int byteCountInBlocks = blockNumber*blockSize;
                    byte [] decryptedBlock = process(aData, 0, byteCountInBlocks);
                    int remainingLen = aData.length - byteCountInBlocks;
                    cozulmus = decryptedBlock;

                    if(remainingLen > 0) {
                        byte[] remainingDecryptedBlock = new byte[remainingLen];
                        mMode.update(aData, byteCountInBlocks, remainingDecryptedBlock, 0);
                        cozulmus = ByteUtil.concatAll(decryptedBlock, remainingDecryptedBlock);
                    }
                }
                else {
                    cozulmus = new byte[aData.length];
                    mMode.update(aData, 0, cozulmus, 0);
                }
            }

            byte[] tagBytes = ((GCM) mMode).doFinal();

            if(((ParamsWithGCMSpec) mParams).getTag() == null )
                ((ParamsWithGCMSpec) mParams).setTag(tagBytes);
            else{
                if(!Arrays.equals(Arrays.copyOfRange(tagBytes,0,tagBytes.length),((ParamsWithGCMSpec)mParams).getTag()))
                    throw new CryptoException("Tags are not matched..");
            }
        }
        else{
            cozulmus = process(aData);
            int unpad = 0;
            if (cozulmus != null && cozulmus.length > 0) {
                try {
                    unpad = mPadding.unpad(cozulmus, 0, cozulmus.length);
                } catch (WrongPaddingException ex) {
                    throw new ArgErrorException(GenelDil.mesaj(GenelDil.PADDING_HATALI), ex);
                }
            }
            byte[] unPadded = new byte[aData.length - unpad];
            System.arraycopy(cozulmus, 0, unPadded, 0, unPadded.length);
            mMode.reset();

            cozulmus = unPadded;
        }

        if(mEncodedKey != null)
            Arrays.fill(mEncodedKey, (byte)0xCC);

        return cozulmus;
    }

}
