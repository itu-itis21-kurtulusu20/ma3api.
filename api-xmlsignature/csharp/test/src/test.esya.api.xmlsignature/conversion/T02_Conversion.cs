

using System;
using System.IO;
using System.Xml;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.signature;


namespace test.esya.api.xmlsignature.conversion
{
    class T02_Conversion : XMLSignatureTestBase
    {
        private readonly String DIRECTORY = "T:\\api-xmlsignature\\test-output\\csharp\\ma3\\conversion\\";

        private static object[] UpgradeCases =
        {
            new object[]{"BES_Enveloping.xml",SignatureType.ES_T, "EST_Enveloping.xml"},
            new object[]{"BES_Enveloped.xml", SignatureType.ES_T, "EST_Enveloped.xml"},
            new object[]{"BES_Detached.xml", SignatureType.ES_T, "EST_Detached.xml"},

            new object[]{"BES_Enveloping.xml", SignatureType.ES_XL, "BES_ESXL_Enveloping.xml"},
            new object[]{"BES_Enveloped.xml", SignatureType.ES_XL, "BES_ESXL_Enveloped.xml"},
            new object[]{"BES_Detached.xml", SignatureType.ES_XL, "BES_ESXL_Detached.xml"},

            new object[]{"BES_Enveloping.xml", SignatureType.ES_A, "BES_ESA_Enveloping.xml"},
            new object[]{"BES_Enveloped.xml", SignatureType.ES_A, "BES_ESA_Enveloped.xml"},
            new object[]{"BES_Detached.xml", SignatureType.ES_A, "BES_ESA_Detached.xml"},

            new object[]{"EST_Enveloping.xml", SignatureType.ES_C, "ESC_Enveloping.xml"},
            new object[]{"EST_Enveloped.xml", SignatureType.ES_C, "ESC_Enveloped.xml"},
            new object[]{"EST_Detached.xml", SignatureType.ES_C, "ESC_Detached.xml"},

            new object[]{"ESC_Enveloping.xml", SignatureType.ES_X_Type1, "ESX_Enveloping.xml"},
            new object[]{"ESC_Enveloped.xml", SignatureType.ES_X_Type1, "ESX_Enveloped.xml"},
            new object[]{"ESC_Detached.xml", SignatureType.ES_X_Type1, "ESX_Detached.xml"},

            new object[]{"ESX_Enveloping.xml", SignatureType.ES_XL, "ESXL_Enveloping.xml"},
            new object[]{"ESX_Enveloped.xml", SignatureType.ES_XL, "ESXL_Enveloped.xml"},
            new object[]{"ESX_Detached.xml", SignatureType.ES_XL, "ESXL_Detached.xml"},

            new object[]{"ESXL_Enveloping.xml", SignatureType.ES_A, "ESA_Enveloping.xml"},
            new object[]{"ESXL_Enveloped.xml", SignatureType.ES_A, "ESA_Enveloped.xml"},
            new object[]{"ESXL_Detached.xml", SignatureType.ES_A, "ESA_Detached.xml"}
        };

        [Test, TestCaseSource("UpgradeCases")]
        public void TestUpgrade(string inputFile, tr.gov.tubitak.uekae.esya.api.signature.SignatureType  outputType, string outputFile)
        {
            tr.gov.tubitak.uekae.esya.api.xmlsignature.Context context = CreateContext(DIRECTORY);

            XmlDocument xmlDoc = new XmlDocument();
            xmlDoc.PreserveWhitespace = true;

            FileStream inFileStream = new FileStream(DIRECTORY + inputFile, FileMode.Open);
            XmlReader reader = XmlReader.Create(inFileStream);
            xmlDoc.Load(reader);
            inFileStream.Close();

            tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature signature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature.parse(xmlDoc, context);
            signature.upgrade(outputType);

            FileStream fs = new FileStream(DIRECTORY + outputFile, FileMode.Create);
            xmlDoc.Save(fs);           
            fs.Close();
        }
    }
}
