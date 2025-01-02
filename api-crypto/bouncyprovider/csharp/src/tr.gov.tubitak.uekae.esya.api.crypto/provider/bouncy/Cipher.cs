using System;
using Org.BouncyCastle.Crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public abstract class Cipher : tr.gov.tubitak.uekae.esya.api.crypto.Cipher
    {
        protected readonly CipherAlg _mCipherAlg;
        protected IBufferedCipher _mCipher;        

        public Cipher(CipherAlg aCipherAlg)
        {
            _mCipherAlg = aCipherAlg;
        }
        //public abstract void  init(IKey aKey, IAlgorithmParams aParams);

        //public abstract void init(byte[] aKey, IAlgorithmParams aParams);
        
        public override byte[] process(byte[] aData)
        {
            //return 
            return _mCipher.ProcessBytes(aData);
            //return sonuc;
        }

        public override byte[] doFinal(byte[] aData)
        {
            return _mCipher.DoFinal(aData);
        }

        //override public int getBlockSize()
        //{
        //    if (_mCipher.GetBlockSize() != _mCipherAlg.getBlockSize())
        //    {
        //        Console.WriteLine("Block sizes are not equal. (" + _mCipher.GetBlockSize() + ", " + _mCipherAlg.getBlockSize() + ")");               
        //    }            
        //    return _mCipher.GetBlockSize();

        //}
        //public abstract int getBlockSize();

        //public abstract bool isEncryptor();

        public override CipherAlg getCipherAlgorithm()
        {
            return _mCipherAlg;
        }

        public override void reset()
        {
            _mCipher.Reset();
        }
    }
}
