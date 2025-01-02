using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.unit.help
{
    public class XmlSignatureTestHelper
    {
        private static XmlSignatureTestHelper instance;
        public static XmlSignatureTestHelper getInstance()
        {
            if (instance == null)
            {
                instance = new XmlSignatureTestHelper();
            }
            return instance;
        }

        private string baseDir = null;
        private string rootDir = null;
        public XmlSignatureTestHelper()
        {
            //Basedir and rootDir
            rootDir = "T:\\api-xmlsignature";
            baseDir = rootDir + "\\docs\\samples\\signatures";

            
        }


        public void loadFreeLicense()
        {
            string lisansFilePath = getRootDir() + "\\docs\\config\\lisans\\lisansFree.xml";
            FileStream lisansStream = new FileStream(lisansFilePath, FileMode.Open, FileAccess.Read);
            LicenseUtil.setLicenseXml(lisansStream);
            lisansStream.Close();
        }

        public void loadTestLicense()
        {
            string lisansFilePath = getRootDir() + "\\docs\\config\\lisans\\lisansTest.xml";
            FileStream lisansStream = new FileStream(lisansFilePath, FileMode.Open, FileAccess.Read);
            LicenseUtil.setLicenseXml(lisansStream);
            lisansStream.Close();
        }

       public string getBaseDir()
        {
            return baseDir;
        }

       public string getRootDir()
        {
            return rootDir;
        }

        public string getSignatureOutputDir()
        {
            string outputDir = "T:\\api-xmlsignature\\test-output\\dotnet\\";
            if(!Directory.Exists(outputDir))
                Directory.CreateDirectory(outputDir);
            return outputDir;
        }
    }
}
