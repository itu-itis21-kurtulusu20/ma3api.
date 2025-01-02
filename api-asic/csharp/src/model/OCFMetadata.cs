/**
 * @author yavuz.kahveci
 */
using System.IO;

namespace tr.gov.tubitak.uekae.esya.api.asic.model
{
    using Element = System.Xml.XmlElement;

    public class OCFMetadata : BaseASiCXMLDocument
    {

        public OCFMetadata(Stream aStream)
        {
            read(aStream);
            setASiCDocumentName("META-INF/metadata.xml");
        }

        protected override void init(Element element)
        {
            // todo
        }
    }
}