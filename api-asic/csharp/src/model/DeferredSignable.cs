/**
 * @author yavuz.kahveci
 */
using System.IO;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace tr.gov.tubitak.uekae.esya.api.asic.model
{
    public class DeferredSignable : Signable
    {
        private Signable internalSignable;

        public void setActualSignable(Signable actual){
            internalSignable = actual;
        }

        public Stream getContent()
        {
            closeContentStream();
            return internalSignable.getContent();
        }

        public byte[] getDigest(DigestAlg digestAlg)
        {
            check();
            return internalSignable.getDigest(digestAlg);
        }

        public string getURI()
        {
            check();
            return internalSignable.getURI();
        }

        public string getResourceName()
        {
            check();
            return internalSignable.getMimeType();
        }

        public string getMimeType()
        {
            check();
            return internalSignable.getResourceName();
        }

        private void check(){
            if (internalSignable == null)
                throw new SignatureRuntimeException("Deferred signable not set yet!");
        }
        public void closeContentStream()
        {
            check();
            internalSignable.closeContentStream();
        }
    }
}