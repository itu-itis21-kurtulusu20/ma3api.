using System;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    public class BlockBuffer
    {
        private readonly int _mBlockSize;

        private byte[] _mRemaining;
        private readonly bool _mKeepLastBlockInRemaining;

        public BlockBuffer(int aBlockSize)
            : this(aBlockSize, false) { }

        public BlockBuffer(int aBlockSize, bool aKeepLastBlockInRemaining)
        {
            _mBlockSize = aBlockSize;
            _mKeepLastBlockInRemaining = aKeepLastBlockInRemaining;
        }

        public virtual byte[] getRemaining()
        {
            return _mRemaining;
        }

        public virtual byte[] update(byte[] aBytes)
        {
            return update(aBytes, 0, aBytes.Length);
        }

        public virtual byte[] update(byte[] aBytes, int aOffset, int aLength)
        {

            if (aBytes == null || (aOffset + aLength > aBytes.Length))
                throw new CryptoRuntimeException("Invalid argument for block update.");

            int remainingLen = _mRemaining != null ? _mRemaining.Length : 0;

            int readyBlocks = (aLength + remainingLen) / _mBlockSize;
            int newRemainingLen = (aLength + remainingLen) % _mBlockSize;
            if (_mKeepLastBlockInRemaining)
            {
                if (newRemainingLen == 0)
                {
                    readyBlocks--;
                    newRemainingLen = _mBlockSize;
                }
            }
            int incomingRemainIndex = aLength - newRemainingLen;

            if (readyBlocks > 0)
            {
                int readySize = readyBlocks * _mBlockSize;

                byte[] ready = new byte[readySize];
                if (_mRemaining != null && remainingLen > 0)
                    Array.Copy(_mRemaining, 0, ready, 0, remainingLen);

                Array.Copy(aBytes, aOffset, ready, remainingLen, incomingRemainIndex);


                // copy remaining
                _mRemaining = new byte[newRemainingLen];
                Array.Copy(aBytes, aOffset + incomingRemainIndex, _mRemaining, 0, newRemainingLen);


                return ready;
            }
            else
            {
                byte[] remainingNew = new byte[newRemainingLen];
                if(_mRemaining != null)
                    Array.Copy(_mRemaining, 0, remainingNew, 0, remainingLen);
                Array.Copy(aBytes, aOffset, remainingNew, remainingLen, aBytes.Length);
                _mRemaining = remainingNew;
            }
            return null;
        }
    }
}
