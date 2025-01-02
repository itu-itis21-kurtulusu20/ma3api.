using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Security;
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncyDigester : IDigester
    {
        /*IMessageDigest*/
        readonly IDigest mDigest;

        public BouncyDigester(DigestAlg aAlg)
        {
            mDigest = DigestUtilities.GetDigest(/*BouncyProviderUtil.resolveDigestName(aAlg)*/BouncyProviderUtil.resolveDigestAlg(aAlg));
            //mDigest = DigestUtilities.GetDigest(aAlg.getName());
        }

        public void update(byte[] aData)
        {
            mDigest.BlockUpdate(aData, 0, aData.Length);
        }

        public void update(byte[] aData, int aOffset, int aLength)
        {
            mDigest.BlockUpdate(aData, aOffset, aLength);
        }

        public byte[] digest()
        {
            byte[] result = new byte[mDigest.GetDigestSize()];
            mDigest.DoFinal(result, 0);
            return result;
        }
    }
}
