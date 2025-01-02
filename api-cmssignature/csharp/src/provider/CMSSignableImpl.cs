using System;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.provider
{
    public class CMSSignableImpl : Signable
    {
        private readonly ISignable internalNET;

        public CMSSignableImpl(ISignable aInternal)
        {
            internalNET = aInternal;
        }

        public Stream getContent()
        {
            try
            {
                return internalNET.getAsInputStream();
            }
            catch (Exception x)
            {
                throw new SignatureException(x);
            }
        }

        public byte[] getDigest(DigestAlg aDigestAlg)
        {
            try
            {
                return internalNET.getMessageDigest(aDigestAlg);
            }
            catch (Exception x)
            {
                throw new SignatureException(x);
            }
        }

        public String getURI()
        {
            return null;  // todo
        }

        public String getResourceName()
        {
            return null;  // todo
        }

        public String getMimeType()
        {
            return null;  // todo
        }

        public void closeContentStream()
        {}

    }
}
