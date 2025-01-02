package tr.gov.tubitak.uekae.esya.api.crypto;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Padding;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoRuntimeException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Key;
import java.util.Arrays;

/**
 * @author ayetgin
 */

public class BufferedCipher implements BaseCipher
{
    private Cipher mInternal;
    private OutputStream mExternalStream;
    private ByteArrayOutputStream mOutputStream;
    private BlockBuffer mBlockBuffer;
    private boolean mProcessStarted;

    public BufferedCipher(Cipher aCipher)
    {
        mInternal = aCipher;
        mOutputStream = new ByteArrayOutputStream();
    }

    public Cipher getInternalCipher()
    {
        return mInternal;
    }

    public CipherAlg getCipherAlg(){
        return mInternal.getCipherAlgorithm();
    }
    /**
     * This method changes behaviour of the BufferedCipher and must be used before
     * calling any of the update methods.
     *
     * @param aOutputStream stream that processes data will be output. 
     */
    public void setStream(OutputStream aOutputStream)
    {
        if (mProcessStarted)
            throw new CryptoRuntimeException("Cipher process allready started, you cant decide to use a stream at this phase.");
        mExternalStream = aOutputStream;
    }

    public void init(Key aKey, AlgorithmParams aParams) throws CryptoException
    {
        mInternal.init(aKey, aParams);
        setupBlockBuffer();
    }

    public void init(byte[] aKey, AlgorithmParams aParams) throws CryptoException
    {
        mInternal.init(aKey, aParams);
        setupBlockBuffer();

    }

    /**
     * If user supplied stream is used, it should be set again, cause
     * OutputStream dont have reset method 
     *
     * @throws CryptoException
     */
    public void reset() throws CryptoException
    {
        mInternal.reset();
        setupBlockBuffer();
        mProcessStarted = false;
        if (mOutputStream!=null){
            mOutputStream.reset();            
        }
    }

    private void setupBlockBuffer(){
        int blocksize = mInternal.getBlockSize();
        if (blocksize>0)
            mBlockBuffer = new BlockBuffer(blocksize, !mInternal.isEncryptor());
        else
            mBlockBuffer = new BlockNoBuffer();
    }

    public void process(byte[] aData) throws CryptoException
    {
        process(aData, 0, aData.length);
    }

    public void process(byte[] aData, int aOffset, int aLength) throws CryptoException
    {
        mProcessStarted = true;

        if (aData==null)
            return;
        
        byte[] readyBlocks = mBlockBuffer.update(aData, aOffset, aLength);
        if (readyBlocks!=null){
            byte[] processed =  mInternal.process(readyBlocks);
            if (processed!=null){
                try {
                    if (mExternalStream!=null)
                        mExternalStream.write(processed);
                    else
                        mOutputStream.write(processed);
                } catch (IOException x){
                    // todo i18n
                    throw new CryptoException("IO Exception", x);
                }
            }
        }
    }

    /**
     * Last step of decryption.
     *
     * @param aData final part for the decrption.
     * @return decryption result returns null if the setStream approach is used, because the result
     *      is already written to that stream
     * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException if anything goes wrong
     */
    public byte[] doFinal(byte[] aData) throws CryptoException
    {
        process(aData);

        byte[] remain = mBlockBuffer.getRemaining();
        if (remain != null) {

            try {
            	byte[] finalBytes = mInternal.doFinal(remain);
                if (mExternalStream!=null) {
                    mExternalStream.write(finalBytes);
                    return null;
                }
                mOutputStream.write(finalBytes);
            } catch (IOException x){
                // todo i18n
                throw new CryptoException("IO Exception", x);
            } catch (ESYAException e) {
            	 throw new CryptoException("Crypto exception", e);
			}
        }

        //it was done due to meet the requirements of crypto analysis
        byte[] data = mOutputStream.toByteArray();
        mOutputStream.reset();
        byte [] dummyData = new byte[data.length];
        Arrays.fill(dummyData, (byte)0xCC);
        try {
            mOutputStream.write(dummyData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public CipherAlg getCipherAlgorithm()
    {
        return mInternal.getCipherAlgorithm();  
    }

	public String getCipherAlgorithmStr() {
		return getCipherAlg().getName();
	}
}
