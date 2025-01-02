using System;
using System.Reflection;
using log4net;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncyMAC : IMAC
    {
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private readonly IMac mMAC;
        private readonly MACAlg mMACAlg;
        private int mLength = 0;

        public BouncyMAC(MACAlg aMACAlg)
        {
            mMACAlg = aMACAlg;
            mMAC = MacUtilities.GetMac(aMACAlg.getName());
        }

        public void init(IKey aKey, IAlgorithmParams aParams)
        {
            init(aKey.getEncoded(), aParams);
        }

        public void init(byte[] aKey, IAlgorithmParams aParams)
        {
            if (aParams != null && !(aParams is ParamsWithLength))
            {
                throw new CryptoException("Invalid algorithm parameter, expected ParamWithLength, found " + aParams.GetType());
            }            
            mLength = (aParams == null) ? 0 : ((ParamsWithLength)aParams).getLength();

            KeyParameter keyParameter = null;
            if (mLength != 0)
                keyParameter = new KeyParameter(aKey, 0, mLength);
            else
                keyParameter = new KeyParameter(aKey);
            mMAC.Init(keyParameter);
        }


        public void process(byte[] aData)
        {
            try
            {
                mMAC.BlockUpdate(aData, 0, aData.Length);
            }
            catch (Exception x)
            {
                throw new CryptoException("Error updating MAC", x);
            }
        }

        public byte[] doFinal(byte[] aData)
        {
            if (aData != null)
                process(aData);
            byte[] outBytes = new byte[mMAC.GetMacSize()];
            mMAC.DoFinal(outBytes, 0);

            if (mLength > 0)
            {
                byte[] resultTrimmed = new byte[mLength];
                Array.Copy(outBytes, 0, resultTrimmed, 0, mLength);
                return resultTrimmed;
            }
            return outBytes;
        }

        public MACAlg getMACAlgorithm()
        {
            return mMACAlg;
        }

        public int getMACSize()
        {
            return mMAC.GetMacSize();
        }
    }
}
