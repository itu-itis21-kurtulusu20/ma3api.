using System.IO;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.util
{
    // todo java versiyonundan bayagi farkli oldu
    // ayni isi gorup gormedigi test edilmeli
    public class XmlSigDetector
    {
        public bool isSignature(Stream stream)
        {
            XmlReader reader = XmlReader.Create(stream);

            while(reader.Read())
            {
                if (reader.IsStartElement())
                {
                    if (reader.LocalName.Equals("envelope") || reader.LocalName.Equals("Signature"))
                        return true;
                    // i guess no need to check prefix
                }
            }

            return false;
        }
    }
}
