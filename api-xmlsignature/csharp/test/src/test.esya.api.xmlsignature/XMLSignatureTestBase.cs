using System;
using System.IO;
using System.Xml;
using test.esya.api.cmssignature;
using tr.gov.tubitak.esya.api.xmlsignature.unit.test;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.config;

namespace test.esya.api.xmlsignature
{
    public class XMLSignatureTestBase : CMSSignatureTest
    {
        public static string PROJECT_DIRECTORY;

        public static string BASE_DIR = "T:\\api-xmlsignature\\test-output\\csharp\\ma3\\";

        public static string CONFIG_FILE =
            "T:\\api-parent\\resources\\ug\\config\\xmlsignature-config.xml";

        private const String ENVELOPE_XML =
               "<envelope>\n" +
                       "  <data id=\"data1\">\n" +
                       "    <item>Item 1</item>\n" +
                       "    <item>Item 2</item>\n" +
                       "    <item>Item 3</item>\n" +
                       "  </data>\n" +
                       "</envelope>\n";

        public const string PLAINFILENAME = UnitTestParameters.PLAINFILENAME;
        public const string PLAINFILEMIMETYPE = UnitTestParameters.PLAINFILEMIMETYPE;

        public MemoryStream signatureBytes = new MemoryStream();

        public XMLSignatureTestBase()
        {
            PROJECT_DIRECTORY = "C:\\GitRepo\\ma3api\\api-xmlsignature\\csharp\\ma3\\";
        }

        public Context CreateContext()
        {
            return CreateContext(BASE_DIR);
        }


        public Context CreateContext(String baseDirectory)
        {
            Context context = new Context(baseDirectory);
            context.Config = new Config(CONFIG_FILE);
            return context;
        }




        // create sample envelope xml
        // that will contain signature inside
        protected XmlDocument newEnvelope()
        {
            XmlDocument xmlDocument = new XmlDocument();
            xmlDocument.PreserveWhitespace = true;
            xmlDocument.LoadXml(ENVELOPE_XML);
            return xmlDocument;
        }
    }

}
