using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy;
using Org.BouncyCastle.Crypto.Parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncyWrapper : IWrapper
    {
        readonly Org.BouncyCastle.Crypto.IWrapper mWrapper;

        byte[] mWrapperKey;
        readonly bool mForWrapping;


        public BouncyWrapper(WrapAlg aWrapAlg, bool aForWrapping)
        {
            //if (aWrapAlg.equals(WrapAlg.AES128) || aWrapAlg.equals(WrapAlg.AES192) || aWrapAlg.equals(WrapAlg.AES256))
            //    mWrapper = new AESWrapper();
            //else
            //    throw new UnknownElement(GenelDil.mesaj(GenelDil.WRAPALG_BILINMIYOR));

            //mForWrapping = aForWrapping;
            mWrapper = WrapperUtilities.GetWrapper(BouncyProviderUtil.resolveWrapAlg(aWrapAlg));
            mForWrapping = aForWrapping;
        }

        public void init(byte[] aKey)
        {
            mWrapperKey = aKey;
            
            KeyParameter wKey = ParameterUtilities.CreateKeyParameter(mWrapper.AlgorithmName, mWrapperKey);
            mWrapper.Init(mForWrapping, wKey);
        }

        public byte[] process(byte[] aKey)
        {
            try
            {
                if (mForWrapping)
                {
                    //return mWrapper.wrap(aKey, 0, aKey.length, mWrapperKey);
                    return mWrapper.Wrap(aKey, 0, aKey.Length);
                }
                else
                {
                    //return mWrapper.unwrap(aKey, 0, aKey.length, mWrapperKey);
                    return mWrapper.Unwrap(aKey, 0, aKey.Length);
                }
            }
            catch (Exception e)
            {
                throw new CryptoException("wrap error", e);
            }
        }

        public bool isWrapper()
        {
            return mForWrapping;
        }
    }
}
