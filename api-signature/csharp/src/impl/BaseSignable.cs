using System;
using System.Collections.Generic;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace tr.gov.tubitak.uekae.esya.api.signature.impl
{
    public abstract class BaseSignable : Signable
    {
        private static readonly int DEFAULT_BUF_SIZE = 32 * 1024;

        private readonly Dictionary<DigestAlg, byte[]> mDigestTable = new Dictionary<DigestAlg, byte[]>(1);

        private readonly byte[] mBuffer;
        protected Stream content;

        protected BaseSignable()
        {
            mBuffer = new byte[DEFAULT_BUF_SIZE];
        }

        public byte[] getDigest(DigestAlg aDigestAlg)
        {
            byte[] ozet = null;
            if(mDigestTable.ContainsKey(aDigestAlg)){
                ozet = mDigestTable[aDigestAlg];
            }
            if (ozet == null)
            {
                try
                {
                    ozet = DigestUtil.digestStream(aDigestAlg, getContent(), mBuffer.Length);
                    mDigestTable.Add(aDigestAlg, ozet);
                }
                catch (Exception x)
                {
                    throw new SignatureException("Cant digest signable!", x);
                }
            }

            return ozet;
        }
        public String getResourceName()
        {
            String uri = getURI();
            int lastSlashIndex = uri.LastIndexOf("/");
            return uri.Substring(lastSlashIndex + 1, uri.Length);

        }

        public void closeContentStream()
        {
            if (content != null)
                content.Close();
        }

        public abstract Stream getContent();

        public abstract string getURI();

        public abstract string getMimeType();


    }
}
