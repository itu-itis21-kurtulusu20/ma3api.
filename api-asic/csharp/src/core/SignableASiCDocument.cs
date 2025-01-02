/**
 * @author yavuz.kahveci
 */
using System.IO;
using tr.gov.tubitak.uekae.esya.api.asic.model;
using tr.gov.tubitak.uekae.esya.api.signature.impl;

namespace tr.gov.tubitak.uekae.esya.api.asic.core
{
    public class SignableASiCDocument : BaseSignable
    {
        readonly ASiCDocument document;

        public SignableASiCDocument(ASiCDocument aDocument)
        {
            document = aDocument;
        }

        public override Stream getContent()
        {
            MemoryStream baos = new MemoryStream();
            document.write(baos);
            content = new MemoryStream(baos.ToArray());
            return content;
        }

        public override string getURI()
        {
            return document.getASiCDocumentName();
        }

        public override string getMimeType()
        {
            return "text/xml";
        }
    }
}
