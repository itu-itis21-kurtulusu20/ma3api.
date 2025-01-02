using System;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.signature.impl;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.provider
{
    public class SignableDocument : BaseSignable
    {
        readonly Document document;

        public SignableDocument(Document aDocument)
        {
            document = aDocument;
        }

        public override Stream getContent()
        {
            content = document.Stream;
            return content;
        }

        public override String getURI()
        {
            return document.URI;
        }

        public override String getMimeType()
        {
            return document.MIMEType;
        }
    }
}
