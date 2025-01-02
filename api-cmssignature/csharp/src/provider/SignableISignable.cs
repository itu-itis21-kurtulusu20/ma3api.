using System;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.provider
{
    public class SignableISignable : ISignable
    {
        readonly Signable signable;

        public SignableISignable(Signable aSignable)
        {
            signable = aSignable;
        }

        public byte[] getContentData()
        {
            try
            {
                return AsnIO.streamOku(signable.getContent());
            }
            catch (Exception x)
            {
                throw new SignatureRuntimeException(x);
            }
        }

        public byte[] getMessageDigest(DigestAlg aDigestAlg)
        {
            try
            {
                return signable.getDigest(aDigestAlg);
            }
            catch (Exception x)
            {
                throw new IOException(x.ToString());
            }
        }

        public Stream getAsInputStream()
        {
            try
            {
                return signable.getContent();
            }
            catch (Exception x)
            {
                throw new IOException(x.ToString());
            }

        }
    }
}
