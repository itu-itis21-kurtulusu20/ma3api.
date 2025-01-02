using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;
using tr.gov.tubitak.uekae.esya.api.tsl.model.tsp;
using tr.gov.tubitak.uekae.esya.api.tsl.util;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.config;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;

/*
 * Written by Ali Yavuz Kahveci
 * yavuz.kahveci@tubitak.gov.tr
 * 1040
 */

namespace tr.gov.tubitak.uekae.esya.api.tsl
{
    public class TSL
    {
        private XmlDocument mDocument;
        private TrustServiceStatusList mTSL;
        private String _configPath = Application.StartupPath + "\\xmlsignature-config.xml";

        private TSL(XmlDocument iDocument)
        {
            mDocument = iDocument;
            parseTSLFile();
        }

        private TSL(string iTSLFilePath)
        {
            mDocument = new XmlDocument();
            mDocument.Load(iTSLFilePath);
            parseTSLFile();
        }

        private TSL(Stream iStream)
        {
            mDocument = new XmlDocument();
            mDocument.Load(iStream);
            parseTSLFile();
        }

        private TSL(TextReader iTxtReader)
        {
            mDocument = new XmlDocument();
            mDocument.Load(iTxtReader);
            parseTSLFile();
        }

        private void parseTSLFile()
        {
            XmlNameTable nsTable = mDocument.NameTable;
            XmlNamespaceManager nsManager = new XmlNamespaceManager(nsTable);
            nsManager.AddNamespace("tslx", Constants.NS_TSLX);
            nsManager.AddNamespace("tsl", Constants.NS_TSL);
            nsManager.AddNamespace("ds", Constants.NS_XMLDSIG);

            XmlNodeList nodeList = mDocument.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_TRUSTSERVICESTATUSLIST, nsManager);
            XmlElement rootElement = (XmlElement)nodeList[0];
            ///
            mTSL = new TrustServiceStatusList(rootElement);
        }

        public static TSL parse(XmlDocument iDocument)
        {
            return new TSL(iDocument);
        }

        public static TSL parse(string iTSLFilePath)
        {
            return new TSL(iTSLFilePath);
        }

        public static TSL parse(Stream iStream)
        {
            return new TSL(iStream);
        }

        public static TSL parse(TextReader iReader)
        {
            return new TSL(iReader);
        }

        public TrustServiceStatusList TSLNode
        {
            get { return mTSL; }
        }

        public TSLSchemeInformation SchemeInformation
        {
            get { return mTSL.SchemeInformation; }
        }

        public TrustServiceProviderList TrustServiceProviderList
        {
            get { return mTSL.TSPList; }
        }

        public XmlElement Signature
        {
            get { return mTSL.SignatureElement; }
        }

        public bool validateTSL()
        {
            if(isTSLUptoDate())
            {
                ValidationResult vr = validateSignature();
                if(vr.getType() == ValidationResultType.VALID)
                {
                    return true;
                }
            }
            return false;
        }

        public bool isSigned()
        {
            return mTSL.isSigned;
        }
        public void setConfigPath(String path)
        {
            _configPath = path;
        }
        public String getConfigPath()
        {
            return _configPath;
        }
        public ValidationResult validateSignature()
        {
            if(mTSL.isSigned)
            {
                Context context = new Context();
                context.Config = new Config(getConfigPath());
                context.Document = mDocument;
                XMLSignature signature = XMLSignature.parse(new FileDocument(new FileInfo(mDocument.BaseURI.Substring(8))), context);

                // no params, use the certificate in key info
                ValidationResult result = signature.verify();

                return result;
            }
            else
            {
                throw new TSLException("TSL Document is not signed!");
            }
        }

        public bool isTSLUptoDate()
        {
            DateTime issueDateTime = mTSL.SchemeInformation.ListIssueDateTime.DateTime.ToUniversalTime();
            DateTime now = DateTime.Now.ToUniversalTime();
            
            if(mTSL.SchemeInformation.NextUpdate.isClosed)
            {
                int result = DateTime.Compare(issueDateTime, now);
                if(result<=0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                var nextUpdate = mTSL.SchemeInformation.NextUpdate.NextUpdate.ToUniversalTime();
                int result = DateTime.Compare(issueDateTime, now);
                if (result <= 0)
                {
                    result = DateTime.Compare(now, nextUpdate);
                    if(result<=0)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }

            }
        }

        public IList<ECertificate> getAllCertificates()
        {
            IList<ECertificate> certificates = new List<ECertificate>();
            foreach (var tsp in mTSL.TSPList.TSPList)
            {
                foreach (var service in tsp.TSPServices.TSPSerivceList)
                {
                    certificates.Add(service.ServiceInformation.ServiceDigitalIdentity.X509Certificate);
                }
            }
            return certificates;
        }

        public IList<ECertificate> getValidCertificates()
        {
            IList<ECertificate> certificates = new List<ECertificate>();
            foreach (var tsp in mTSL.TSPList.TSPList)
            {
                foreach (var service in tsp.TSPServices.TSPSerivceList)
                {
                    if (service.ServiceInformation.ServiceStatus.InnerText.Contains("accredited"))
                    {
                        certificates.Add(service.ServiceInformation.ServiceDigitalIdentity.X509Certificate);
                    }
                }
            }
            return certificates;
        }
        public IList<ECertificate> getValidCACertificates()
        {
            IList<ECertificate> certificates = new List<ECertificate>();
            foreach (var tsp in mTSL.TSPList.TSPList)
            {
                foreach (var service in tsp.TSPServices.TSPSerivceList)
                {
                    if (service.ServiceInformation.ServiceStatus.InnerText.Contains("accredited"))
                    {
                        if(service.ServiceInformation.ServiceDigitalIdentity.X509Certificate.isCACertificate()){
                        certificates.Add(service.ServiceInformation.ServiceDigitalIdentity.X509Certificate);
                    }
                   }
                }
            }
            return certificates;
        }
        
    }
}
