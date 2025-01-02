package tr.gov.tubitak.uekae.esya.api.crypto;

import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoRuntimeException;

/**
 * @author ayetgin
 */
class BlockBuffer
{
    private int mBlockSize;

    private byte[] mRemaining;
    private boolean mKeepLastBlockInRemaining;

    public BlockBuffer(int aBlockSize)
    {
        this(aBlockSize, false);
    }

    public BlockBuffer(int aBlockSize, boolean aKeepLastBlockInRemaining)
    {
        mBlockSize = aBlockSize;
        mKeepLastBlockInRemaining = aKeepLastBlockInRemaining;
    }

    public byte[] getRemaining(){
        return mRemaining;
    }

    public byte[] update(byte[] aBytes){
        return update(aBytes, 0, aBytes.length);
    }

    public byte[] update(byte[] aBytes, int aOffset, int aLength){

        if (aBytes==null || (aOffset+aLength > aBytes.length))
            throw new CryptoRuntimeException("Invalid argument for block update.");

        int remainingLen = mRemaining != null ? mRemaining.length : 0;

        int readyBlocks =  (aLength + remainingLen) / mBlockSize;
        int newRemainingLen =  (aLength + remainingLen) % mBlockSize;
        if (mKeepLastBlockInRemaining){
            if (newRemainingLen==0){
                readyBlocks--;
                newRemainingLen = mBlockSize;
            }
        }
        int incomingRemainIndex = aLength - newRemainingLen;

        if (readyBlocks>0){
            int readySize = readyBlocks * mBlockSize;

            byte[] ready = new byte[readySize];
            if (mRemaining!=null && remainingLen>0)
                System.arraycopy(mRemaining, 0, ready, 0, remainingLen);

            System.arraycopy(aBytes, aOffset, ready, remainingLen, incomingRemainIndex);


            // copy remaining
            mRemaining = new byte[newRemainingLen];
            System.arraycopy(aBytes, aOffset + incomingRemainIndex, mRemaining, 0, newRemainingLen);


            return ready;
        } else {
            byte[] remainingNew = new byte[newRemainingLen];
            if (mRemaining!=null)
                System.arraycopy(mRemaining, 0, remainingNew, 0, remainingLen);
            System.arraycopy(aBytes, aOffset, remainingNew, remainingLen, aBytes.length);
            mRemaining = remainingNew;
        }
        return null;
    }

}
