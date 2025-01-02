using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.tsl.util
{
    public class TSLUtil
    {
        private static TSLUtil instance;
        private string _configPath = Application.StartupPath + "\\tsl-config.xml";

        public static TSLUtil getInstance()
        {
            if (instance == null)
            {
                instance = new TSLUtil();
            }
            return instance;
        }

        //public enum TSLTypeEnum
        //{
        //    Generic,
        //    Schemes
        //}

        //public enum StatusDetApproachEnum
        //{
        //    Active,
        //    Passive,
        //    Delinquent,
        //    List
        //}

        //public enum ServiceTypeIdEnum
        //{
        //    PKC,
        //    QC,
        //    TSA,
        //    OCSP,
        //    CRL,
        //    RA,
        //    IdV,
        //    CGen,
        //    ACA,
        //    Archiv,
        //    REM,
        //    KEscrow,
        //    PPwd,
        //    SignaturePolicyAuthority,
        //    Supervision,
        //    Voluntary,
        //    TSLIssuer,
        //    Unspecified
        //}

        //public enum ServiceCurrentStatusEnum
        //{
        //    InAccord,
        //    Expired,
        //    Suspended,
        //    Revoked,
        //    NotInAccord
        //}

        //public enum SchemeTypeCommRulesEnum
        //{
        //    Supervision,
        //    Volapproval
        //}

        private readonly XmlDocument mDocument = new XmlDocument();

        readonly IList<string> languageList = new List<string>();
        readonly IList<string> countryList = new List<string>();
        readonly IList<string> tslTypeList = new List<string>();
        readonly IList<string> statusDetApproachList = new List<string>();
        readonly IList<string> serviceTypeIdList = new List<string>();
        readonly IList<string> serviceCurrentStatusList = new List<string>();
        readonly IList<string> schemeTypeCommRulesList = new List<string>();

        public TSLUtil()
        {
            loadTSLConfigFile();
            initlanguageList();
            initCountryList();
        }
        public void setConfigPath(String path)
        {
            _configPath = path;
        }
        public String getConfigPath()
        {
            return _configPath;
        }
        private void loadTSLConfigFile()
        {
            mDocument.Load(getConfigPath());

            XmlNodeList nodeList = null;

            nodeList = mDocument.SelectNodes("tsl-config");
            if (nodeList != null && nodeList.Count == 1)
            {
                XmlElement rootElement = (XmlElement)nodeList[0];

                nodeList = null;

                nodeList = rootElement.SelectNodes("TSLType");
                if (nodeList != null && nodeList.Count == 1)
                {
                    XmlElement tslTypeElement = (XmlElement)nodeList[0];
                    initTSLTypeList(tslTypeElement);
                }
                nodeList = null;

                nodeList = rootElement.SelectNodes("StatusDeterminationApproach");
                if (nodeList != null && nodeList.Count == 1)
                {
                    XmlElement statusDetApprElement = (XmlElement)nodeList[0];
                    initStatusDetApproachList(statusDetApprElement);
                }
                nodeList = null;

                nodeList = rootElement.SelectNodes("ServiceTypeIdentifier");
                if (nodeList != null && nodeList.Count == 1)
                {
                    XmlElement serviceTypeIdElement = (XmlElement)nodeList[0];
                    initServiceTypeIdList(serviceTypeIdElement);
                }
                nodeList = null;

                nodeList = rootElement.SelectNodes("ServiceCurrentStatus");
                if (nodeList != null && nodeList.Count == 1)
                {
                    XmlElement serviceCurrentStatusElement = (XmlElement)nodeList[0];
                    initServiceCurrentStatusList(serviceCurrentStatusElement);
                }
                nodeList = null;

                nodeList = rootElement.SelectNodes("SchemeTypeCommunityRules");
                if (nodeList != null && nodeList.Count == 1)
                {
                    XmlElement schemeTypeCommRulesElement = (XmlElement)nodeList[0];
                    initSchemeTypeCommRulesList(schemeTypeCommRulesElement);
                }
                nodeList = null;
            }
            
        }

        private void initlanguageList()
        {
            languageList.Add("tr");
            languageList.Add("en");
            languageList.Add("bg");
            languageList.Add("cs");
            languageList.Add("da");
            languageList.Add("de");
            languageList.Add("el");
            languageList.Add("es");
            languageList.Add("et");
            languageList.Add("fi");
            languageList.Add("fr");
            languageList.Add("ga");
            languageList.Add("hu");
            languageList.Add("is");
            languageList.Add("it");
            languageList.Add("lb");
            languageList.Add("lt");
            languageList.Add("lv");
            languageList.Add("mt");
            languageList.Add("nb");
            languageList.Add("nl");
            languageList.Add("nn");
            languageList.Add("no");
            languageList.Add("pl");
            languageList.Add("pt");
            languageList.Add("ro");
            languageList.Add("sk");
            languageList.Add("sl");
            languageList.Add("sv");
        }

        public IList<string> LanguageList()
        {
            return languageList;
        }

        private void initCountryList()
        {
            countryList.Add("TR");
            countryList.Add("EU");
            countryList.Add("AT");
            countryList.Add("BE");
            countryList.Add("BG");
            countryList.Add("CY");
            countryList.Add("CZ");
            countryList.Add("DE");
            countryList.Add("DK");
            countryList.Add("EE");
            countryList.Add("EL");
            countryList.Add("ES");
            countryList.Add("FI");
            countryList.Add("FR");
            countryList.Add("HU");
            countryList.Add("IE");
            countryList.Add("IS");
            countryList.Add("IT");
            countryList.Add("LI");
            countryList.Add("LT");
            countryList.Add("LU");
            countryList.Add("LV");
            countryList.Add("MT");
            countryList.Add("NL");
            countryList.Add("NO");
            countryList.Add("PL");
            countryList.Add("PT");
            countryList.Add("RO");
            countryList.Add("SE");
            countryList.Add("SI");
            countryList.Add("SK");
            countryList.Add("UK");
        }

        public IList<string> CountryList()
        {
            return countryList;
        }

        private void initTSLTypeList(XmlElement iElement)
        {
            XmlNodeList nodeList = iElement.ChildNodes;
            foreach (var node in nodeList)
            {
                tslTypeList.Add(((XmlElement)node).InnerText);
            }
        }

        public IList<string> TSLTypeList()
        {
            return tslTypeList;
        }

        public string TSLTypeValue(int pos)
        {
            if (pos >= 0 && pos < tslTypeList.Count)
            {
                return tslTypeList[pos];
            }
            return null;
        }

        private void initStatusDetApproachList(XmlElement iElement)
        {
            XmlNodeList nodeList = iElement.ChildNodes;
            foreach (var node in nodeList)
            {
                statusDetApproachList.Add(((XmlElement)node).InnerText);
            }
        }

        public IList<string> StatusDeterminationApproach()
        {
            return statusDetApproachList;
        }

        public string StatusDeterminationApproachValue(int pos)
        {
            if (pos >= 0 && pos < statusDetApproachList.Count)
            {
                return statusDetApproachList[pos];
            }
            return null;
        }

        private void initServiceTypeIdList(XmlElement iElement)
        {
            XmlNodeList nodeList = iElement.ChildNodes;
            foreach (var node in nodeList)
            {
                serviceTypeIdList.Add(((XmlElement)node).InnerText);
            }
        }

        public IList<string> ServiceTypeId()
        {
            return serviceTypeIdList;
        }

        public string ServiceTypeIdValue(int pos)
        {
            if (pos >= 0 && pos < serviceTypeIdList.Count)
            {
                return serviceTypeIdList[pos];
            }
            return null;
        }

        private void initServiceCurrentStatusList(XmlElement iElement)
        {
            XmlNodeList nodeList = iElement.ChildNodes;
            foreach (var node in nodeList)
            {
                serviceCurrentStatusList.Add(((XmlElement)node).InnerText);
            }
        }

        public IList<string> ServiceCurrentStatus()
        {
            return serviceCurrentStatusList;
        }

        public string ServiceCurrentStatusValue(int pos)
        {
            if (pos >= 0 && pos < serviceCurrentStatusList.Count)
            {
                return serviceCurrentStatusList[pos];
            }
            return null;
        }

        private void initSchemeTypeCommRulesList(XmlElement iElement)
        {
            XmlNodeList nodeList = iElement.ChildNodes;
            foreach (var node in nodeList)
            {
                schemeTypeCommRulesList.Add(((XmlElement)node).InnerText);
            }
        }

        public IList<string> SchemeTypeCommunityRules()
        {
            return schemeTypeCommRulesList;
        }

        public string SchemeTypeComminityRulesValue(int pos)
        {
            if (pos >= 0 && pos < schemeTypeCommRulesList.Count)
            {
                return schemeTypeCommRulesList[pos];
            }
            return null;
        }

        public XmlNamespaceManager getNamespaceManager(XmlDocument iDocument)
        {
            XmlNameTable nsTable = iDocument.NameTable;
            XmlNamespaceManager nsManager = new XmlNamespaceManager(nsTable);
            nsManager.AddNamespace("tslx", Constants.NS_TSLX);
            nsManager.AddNamespace("tsl", Constants.NS_TSL);
            nsManager.AddNamespace("ds", Constants.NS_XMLDSIG);

            return nsManager;
        }

    }
}
