/**
 * @author yavuz.kahveci
 */

using System.IO;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace tr.gov.tubitak.uekae.esya.api.asic.core
{
    public class SignableEntry : Signable
    {
        readonly Signable internalSignable;
        readonly string pathInZip;

        public SignableEntry(Signable aInternalSignable, string aPathInZip)
        {
            internalSignable = aInternalSignable;
            pathInZip = aPathInZip;
        }

        public Stream getContent()
        {
            closeContentStream();
            return internalSignable.getContent();
        }

        public byte[] getDigest(DigestAlg aDigestAlg)
        {
            return internalSignable.getDigest(aDigestAlg);
        }

        public string getURI()
        {
            return pathInZip;
        }

        public string getResourceName()
        {
            return internalSignable.getResourceName();
        }

        public string getMimeType()
        {
            return internalSignable.getMimeType();
        }

        public void closeContentStream()
        {
            internalSignable.closeContentStream();
        }
    }
}
