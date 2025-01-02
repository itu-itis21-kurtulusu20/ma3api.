using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using System.IO;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature
{
    /**
     * Encapsulates data to be signed, when in byte[] format.
     *
     * @see tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable
     */
    public class SignableByteArray : ISignable
    {
        private readonly byte[] mBytes;
        private readonly Dictionary<DigestAlg, byte[]> mOzetTablo = new Dictionary<DigestAlg, byte[]>();

        public SignableByteArray(byte[] aBytes)
        {
            mBytes = aBytes;
        }

        /**
        * @return data to be signed
        */
        public byte[] getContentData()
        {

            return mBytes;
        }

        /**
        * @return digest of the data according to digest algorithm
        */
        public byte[] getMessageDigest(DigestAlg aOzetAlg)
        {
            byte[] ozet = null;
            mOzetTablo.TryGetValue(aOzetAlg, out ozet);
            if (ozet == null)
            {
                ozet = DigestUtil.digest(aOzetAlg, mBytes);
                mOzetTablo[aOzetAlg] = ozet;

            }

            return ozet;
        }

        //@Override
        public Stream getAsInputStream()
        {
            return new MemoryStream(mBytes);
        }

    }
}
