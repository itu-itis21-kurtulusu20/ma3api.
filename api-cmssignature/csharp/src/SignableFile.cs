using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature
{
    /**
     * Encapsulates data to be signed, when in byte[] forSmat.
     *
     * @see tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable
     */
    public class SignableFile : ISignable
    {
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);


        private static readonly int DEFAULT_BUF_SIZE = 32 * 1024;

        private readonly byte[] mBuffer;
        private readonly FileInfo mFile;
        private readonly Dictionary<DigestAlg, byte[]> mOzetTablo = new Dictionary<DigestAlg, byte[]>();

        public SignableFile(FileInfo aFile)
            : this(aFile, DEFAULT_BUF_SIZE)
        {
        }

        public SignableFile(FileInfo aFile, int aBufferSize)
        {
            mFile = aFile;
            mBuffer = new byte[aBufferSize];
        }

        public byte[] getMessageDigest(DigestAlg aDigestAlg)
        {
            byte[] ozet = null;
            mOzetTablo.TryGetValue(aDigestAlg, out ozet);
            if (ozet == null)
            {
                using (FileStream fs = new FileStream(mFile.FullName, FileMode.Open))
                {
                    ozet = DigestUtil.digestStream(aDigestAlg, fs, mBuffer.Length);
                }
                mOzetTablo[aDigestAlg] = ozet;
            }

            return ozet;
        }

        public byte[] getContentData()
        {
            return File.ReadAllBytes(mFile.FullName);
        }

        //@Override
        public Stream getAsInputStream()
        {
            return new FileStream(mFile.FullName, FileMode.Open, FileAccess.Read);
        }

    }
}
