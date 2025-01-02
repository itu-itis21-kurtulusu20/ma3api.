/**
 * @author yavuz.kahveci
 */
using tr.gov.tubitak.uekae.esya.api.asic.core;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest
{
    using Element = System.Xml.XmlElement;
    using Document = System.Xml.XmlDocument;

    public class SignatureReference : XMLElement
    {
        private readonly Element element;
        private readonly string uri;   // required
        private readonly string mimeType; // optional

        public SignatureReference(Document document, string uri) {
            element = document.CreateElement("asic:" + ASiCConstants.TAG_SIGREFERENCE, ASiCConstants.NS_ASiC);
            this.uri = uri;
            element.SetAttribute(ASiCConstants.ATTR_URI, uri);
        }


        public SignatureReference(Element element)
        {
            this.element = element;
            uri = element.GetAttribute(ASiCConstants.ATTR_URI);
            if (uri==null)
                throw new SignatureException("Signature URI not found in ASiC Manifest");

            mimeType = element.GetAttribute(ASiCConstants.ATTR_MIME);
        }

        public string getUri()
        {
            return uri;
        }

        public string getMimeType()
        {
            return mimeType;
        }

        public Element getElement()
        {
            return element;
        }
    }
}