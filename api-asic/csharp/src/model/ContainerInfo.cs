/**
 * @author yavuz.kahveci
 */
using System.IO;

namespace tr.gov.tubitak.uekae.esya.api.asic.model
{
    using Element = System.Xml.XmlElement;

    public class ContainerInfo : BaseASiCXMLDocument
    {
        public ContainerInfo(Stream aStream)
        {
            read(aStream);
            setASiCDocumentName("META-INF/content.xml");
        }

        protected override void init(Element element)
        {
            // todo
        }
    }
}
