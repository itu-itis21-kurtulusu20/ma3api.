/**
 * @author yavuz.kahveci
 */
using System.IO;

namespace tr.gov.tubitak.uekae.esya.api.asic.model
{
    using Element = System.Xml.XmlElement;

    public class Manifest : BaseASiCXMLDocument
    {
        public Manifest(Stream aStream)
        {
            read(aStream);
            setASiCDocumentName("META-INF/manifest.xml");
        }

        protected override void init(Element element)
        {
            // todo
        }
    }
}
