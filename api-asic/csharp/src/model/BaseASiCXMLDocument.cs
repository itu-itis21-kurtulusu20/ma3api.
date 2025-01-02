/**
 * @author yavuz.kahveci
 */
using System;
using System.IO;
using System.Reflection;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.signature;
using log4net;


namespace tr.gov.tubitak.uekae.esya.api.asic.model
{
    using Document = XmlDocument;
    using Element = XmlElement;

    public abstract class BaseASiCXMLDocument : ASiCDocument, XMLElement
    {
        //public static readonly string XML_PREAMBLE_STR = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n";
        //public static readonly byte[] XML_PREAMBLE = Encoding.UTF8.GetBytes(XML_PREAMBLE_STR);
   
        private static readonly ILog LOGGER = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        protected Document document;
        protected byte[] initialBytes;

        protected bool documentChanged=false;

        protected string documentName;

        public void read(Stream inputStream)
        {
            try
            {
                initialBytes = StreamUtil.readAll(inputStream);
                XmlDocument domDocument = new XmlDocument();
                XmlReader reader = XmlReader.Create(new MemoryStream(initialBytes));
                domDocument.PreserveWhitespace = true;
                domDocument.Load(reader);
                init(domDocument.DocumentElement);
            }catch (Exception x){
                throw new SignatureException("Error in reading XML Signature", x);//todo
            }

        }

        public void write(Stream outputStream)
        {
            try {
                if (initialBytes!=null){
                    outputStream.Write(initialBytes,0,initialBytes.Length);
                    String dbgStr = Convert.ToBase64String(initialBytes);
                    LOGGER.Debug("From Bytes: " + dbgStr);
                    return;
                }

                // whitespace ayari gerekli olabilir
                document.PreserveWhitespace = true;
                document.Save(outputStream);

                /*** malum asic hatasi ile ugrasirken denemistim
                 * ilerde lazim olursa bu sekliyle de kullanilabilir
                // write with CR+LF
                XmlWriterSettings settings = new XmlWriterSettings();
                settings.NewLineChars = "\r\n";
                settings.Indent = true;
                XmlWriter writer = XmlWriter.Create(outputStream, settings);

                // write to file
                document.WriteTo(writer);
                writer.Close();
                //***/

                if (this.GetType() == typeof(ASiCManifest))
                {
                    MemoryStream stream = new MemoryStream();
                    document.Save(stream);
                    byte[] byteArry = stream.ToArray();
                    String dbgStr = Convert.ToBase64String(byteArry);
                    LOGGER.Debug("From Stream: " + dbgStr);
                }
            }
            catch (Exception x){
                throw new SignatureException("Cannot output signature.", x);
            }
        }

        protected abstract void init(Element element);

        protected Document createDocument(){
            try {
                /*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setNamespaceAware(true);
                DocumentBuilder db = factory.newDocumentBuilder();
                return db.newDocument();
                */
                Document document = new XmlDocument();
                return document;
            } catch (Exception x){
                throw new SignatureRuntimeException(x);
            }
        }


        public String getASiCDocumentName()
        {
            return documentName;
        }

        public void setASiCDocumentName(String aName)
        {
            documentName = aName;
        }

        public Element getElement()
        {
            return document.DocumentElement;
        }

        public byte[] getInitialBytes()
        {
            return initialBytes;
        }
    }
}