using System;
using System.Collections.Generic;
using System.IO;

namespace tr.gov.tubitak.uekae.esya.api.signature.impl
{
    /**
 * Signable byte array that will be used to add content to a signature.
 *
 * @see tr.gov.tubitak.uekae.esya.api.signature.Signable
 * @see tr.gov.tubitak.uekae.esya.api.signature.Signature#addContent(tr.gov.tubitak.uekae.esya.api.signature.Signable, boolean)
 *
 * @author ayetgin
 */
    public class SignableBytes : BaseSignable
    {
        private readonly byte[] bytes;
        private readonly String uri;
        private readonly String mime;

        public SignableBytes(byte[] aBytes, String aUri, String aMime)
        {
            bytes = aBytes;
            uri = aUri;
            mime = aMime;
        }

        public override Stream getContent()
        {
            content = new MemoryStream(bytes);
            return content;
        }

        public override String getURI()
        {
            return uri;
        }

        public override String getMimeType()
        {
            return mime;
        }
    }
}
