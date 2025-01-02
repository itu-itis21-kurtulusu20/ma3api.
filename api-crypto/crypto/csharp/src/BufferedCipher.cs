using System;
using System.IO;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    public class BufferedCipher : BaseCipher
    {
        protected IAlgorithmParams mAlgorithmParams;
        private readonly Cipher _mInternal;
        private /*Output*/Stream _mExternalStream;
        private readonly /*ByteArrayOutput*/MemoryStream _mOutputStream;
        private BlockBuffer _mBlockBuffer;
        private bool _mProcessStarted;

        public BufferedCipher(Cipher aCipher)
        {
            _mInternal = aCipher;
            _mOutputStream = new MemoryStream();
        }

        public Cipher getInternalCipher()
        {
            return _mInternal;
        }

        public CipherAlg getCipherAlg()
        {
            return _mInternal.getCipherAlgorithm();
        }
        /**
         * This method changes behaviour of the BufferedCipher and must be used before
         * calling any of the update methods.
         *
         * @param aOutputStream stream that processes data will be output. 
         */
        public void setStream(Stream aOutputStream)
        {
            if (_mProcessStarted)
                throw new CryptoRuntimeException("Cipher process already started, you cant decide to use a stream at this phase.");
            _mExternalStream = aOutputStream;
        }

        public void init(IKey aKey, IAlgorithmParams aParams)
        {
            mAlgorithmParams = aParams;
            _mInternal.init(aKey, aParams);
            setupBlockBuffer();

        }

        public void init(byte[] aKey, IAlgorithmParams aParams)
        {
            mAlgorithmParams = aParams;
            _mInternal.init(aKey, aParams);
            setupBlockBuffer();

        }

        /**
        * If user supplied stream is used, it should be set again, cause
        * OutputStream dont have reset method 
        *
        * @throws CryptoException
        */
        public void reset()
        {
            _mInternal.reset();
            setupBlockBuffer();
            _mProcessStarted = false;
            if (_mOutputStream != null)
            {
                _mOutputStream.Position= 0;
            }
        }

        private void setupBlockBuffer()
        {
            int blocksize = _mInternal.getBlockSize();
            if (blocksize > 0)
                _mBlockBuffer = new BlockBuffer(blocksize, !_mInternal.isEncryptor());
            else
                _mBlockBuffer = new BlockNoBuffer();
        }


        public void process(byte[] aData)
        {
            process(aData, 0, aData.Length);
        }

        public void process(byte[] aData, int aOffset, int aLength)
        {
            _mProcessStarted = true;
            if (aData == null)
                return;

            byte[] readyBlocks = _mBlockBuffer.update(aData, aOffset, aLength);
            if (readyBlocks != null)
            {
                byte[] processed = _mInternal.process(readyBlocks);
                if (processed != null)      //PaddedBufferedBlockCipher için bu değer null dönüyordu
                {
                    try
                    {
                        if (_mExternalStream != null)
                            _mExternalStream.Write(processed, 0, processed.Length);
                        else
                            _mOutputStream.Write(processed, 0, processed.Length);
                    }
                    catch (IOException x)
                    {
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
        public byte[] doFinal(byte[] aData)
        {
            byte[] finalBytes = null;
            process(aData);
            byte[] remain = _mBlockBuffer.getRemaining();
            if (remain != null)
            {
                try
                {
                    if (mAlgorithmParams is ParamsWithGCMSpec)
                    {
                        finalBytes = processGCMOperations(remain);
                    }
                    else
                    {
                        finalBytes = _mInternal.doFinal(remain);
                    }

                    if (_mExternalStream != null)
                    {
                        _mExternalStream.Write(finalBytes, 0, finalBytes.Length);
                        return null;
                    }
                    _mOutputStream.Write(finalBytes, 0, finalBytes.Length);
                }
                catch (IOException x)
                {
                    // todo i18n
                    throw new CryptoException("IO Exception", x);
                }
                catch (ESYAException e)
                {
                    throw new CryptoException("Crypto exception", e);
                }
            }

            return _mOutputStream.ToArray();
        }
        public CipherAlg getCipherAlgorithm()
        {
            return _mInternal.getCipherAlgorithm();
        }
        public String getCipherAlgorithmStr()
        {
            return getCipherAlg().getName();
        }

        public byte[] processGCMOperations(byte[] remain)
        {
            byte[] finalBytes;
            if (_mInternal.isEncryptor())
            {
                finalBytes = _mInternal.doFinal(remain);
                int blockSize = _mInternal.getBlockSize();

                if (finalBytes.Length < blockSize)
                  throw new ESYAException("Remaining bytes must be greater than block size");

                byte[] tag = new byte[blockSize];
                byte[] data = new byte[finalBytes.Length - blockSize];
                Array.Copy(finalBytes, data, data.Length);
                Array.Copy(finalBytes, data.Length, tag, 0, tag.Length);
                finalBytes = data;
                ((ParamsWithGCMSpec)mAlgorithmParams).setTag(tag);
            }
            else
            {
                byte[] tag = ((ParamsWithGCMSpec)mAlgorithmParams).getTag();

                if (remain != null && remain.Length == _mInternal.getBlockSize() && ByteUtil.isAllZero(remain))
                {
                    finalBytes = _mInternal.doFinal(tag);
                }
                else
                {
                    byte[] tempArray = new byte[remain.Length + tag.Length];
                    Array.Copy(remain, 0, tempArray, 0, remain.Length);
                    Array.Copy(tag, 0, tempArray, remain.Length, tag.Length);
                    finalBytes = _mInternal.doFinal(tempArray);
                }
            }
            return finalBytes;
        }
    }
}
