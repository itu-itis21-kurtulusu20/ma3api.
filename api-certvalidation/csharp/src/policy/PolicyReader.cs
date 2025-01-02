using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Xml;
using System.Xml.Linq;
using log4net;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.find;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.save;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.policy
{
    /**
     * Validation Policy read operations
     */
    public class PolicyReader
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private static readonly String CLASS = "class";
        private static readonly String PARAM = "param";
        private static readonly String NAME = "name";
        private static readonly String VALUE = "value";

        private static readonly String KAYDETME = "save";
        private static readonly String DOGRULAMA = "validate";
        private static readonly String PARAMETRELER = "parameters";
        private static readonly String FIND = "find";
        private static readonly String MATCH = "match";

        private static readonly String TRUSTED_CERTIFICATE = "trustedcertificate";
        private static readonly String SERTIFIKA = "certificate";
        private static readonly String SIL = "crl";
        private static readonly String OCSP = "ocsp";
        private static readonly String CAPRAZ = "cross";
        private static readonly String DELTA_SIL = "deltacrl";

        private static readonly String UST_SM = "issuer";
        private static readonly String IPTAL = "revocation";
        private static readonly String SIL_SM = "crlissuer";
        private static readonly String TEK_SIL = "crlself";
        private static readonly String TEK_SERTIFIKA = "self";


        private static readonly String ST_USERPOLICYSET = "UserPolicySet";
        private static readonly String ST_INITIALEXPLICITPOLICY = "InitialExplicitPolicy";
        private static readonly String ST_INITIALANYPOLICYINHIBIT = "InitialAnyPolicyInhibit";
        private static readonly String ST_INITIALPOLICYMAPPINGINHIBIT = "InitialPolicyMappingInhibit";
        private static readonly String ST_DEFAULTSTOREPATH = "DefaultStorePath";

        private static readonly String ST_POLICY_SEPERATOR = " ";

        /*
        private static final String ERR_XML_FILE_OPEN			="XML Dosyası açılamadı.";
        private static final String ERR_XML_PARSE_ERROR			="Politika Dosyası çözümleme hatası satır: %1, kolon: %2:\n%3";
        private static final String ERR_XML_BULMAPOLITIKASI		="Bulma Politikasi bulunamadi veya okunamadi.";
        private static final String ERR_XML_DOGRULAMAPOLITIKASI	="Dogrulama Politikasi bulunamadi veya okunamadi.";
        private static final String ERR_XML_KONTROLCULAR		="Kontrolcular bulunamadi veya okunamadi.";
        private static final String ERR_XML_PARAMETRELER		="Parametreler bulunamadi veya okunamadi.";
        private static final String ERR_XML_KAYDEDICILER		="Kaydediciler bulunamadi veya okunamadi.";
        private static final String ERR_XML_ESLESTIRICILER		="Eslestiriciler bulunamadi veya okunamadi.";
        */

        /**
         * XML DOMElementinden bulma politika sınıflarını okur.
         */
        private static void _readFindPolicy(/*Node*/XElement aFindPolicyNode, FindingPolicy aFindingPolicy)
        {
            //NodeList findRoot = aFindPolicyNode.getChildNodes();
            IEnumerable<XElement> findRoot = aFindPolicyNode.Elements();
            //for (int b = 0; b < findRoot.getLength(); b++) {
            foreach (XElement element in findRoot)
            {
                //NodeList finders = findRoot.item(b).getChildNodes();   
                IEnumerable<XElement> finders = element.Elements();

                if (element.Name.LocalName.Equals(SERTIFIKA))
                {
                    aFindingPolicy.setCertificateFinders(_createClassInfos(finders));
                }
                else if (element.Name.LocalName.Equals(TRUSTED_CERTIFICATE))
                {
                    aFindingPolicy.setTrustedCertificateFinders(_createClassInfos(finders));
                }
                else if (element.Name.LocalName.Equals(SIL))
                {
                    aFindingPolicy.setCRLFinders(_createClassInfos(finders));
                }
                else if (element.Name.LocalName.Equals(OCSP))
                {
                    aFindingPolicy.setOCSPResponseFinders(_createClassInfos(finders));
                }
                else if (element.Name.LocalName.Equals(CAPRAZ))
                {
                    aFindingPolicy.setCrossCertificateFinders(_createClassInfos(finders));
                }
                else if (element.Name.LocalName.Equals(DELTA_SIL))
                {
                    aFindingPolicy.setDeltaCRLFinders(_createClassInfos(finders));
                }
            }
        }

        /**
         * XML DOMElementinden eşleştirme politika sınıflarını okur.
         */
        private static void _readMatchPolicy(/*Node*/XElement aMatchPolicyNode, MatchingPolicy aMatchingPolicy)
        {
            //NodeList aMatchRoot = aMatchPolicyNode.getChildNodes();
            IEnumerable<XElement> aMatchRoot = aMatchPolicyNode.Elements();
            //for (int b = 0; b < aMatchRoot.getLength(); b++)
            foreach (XElement element in aMatchRoot)
            {
                //NodeList matchers = aMatchRoot.item(b).getChildNodes();
                IEnumerable<XElement> matchers = element.Elements();
                if (element.Name.LocalName.Equals(SERTIFIKA))
                {
                    aMatchingPolicy.setCertificateMatchers(_createClassInfos(matchers));
                }
                else if (element.Name.LocalName.Equals(SIL))
                {
                    aMatchingPolicy.setCRLMatchers(_createClassInfos(matchers));
                }
                else if (element.Name.LocalName.Equals(CAPRAZ))
                {
                    aMatchingPolicy.setCrossCertificateMatchers(_createClassInfos(matchers));
                }
                else if (element.Name.LocalName.Equals(DELTA_SIL))
                {
                    aMatchingPolicy.setDeltaCRLMatchers(_createClassInfos(matchers));
                }
                else if (element.Name.LocalName.Equals(OCSP))
                {
                    aMatchingPolicy.setOCSPResponseMatchers(_createClassInfos(matchers));
                }

            }
        }


        /**
         * XML DOMElementinden kaydetme politika sınıflarını okur.
         */
        private static void _readSavePolicy(/*Node */XElement aSaversNode, SavePolicy aSavePolicy)
        {
            //NodeList saverNodes = aSaversNode.getChildNodes();
            IEnumerable<XElement> saverNodes = aSaversNode.Descendants();
            //for (int b = 0; b < saverNodes.getLength(); b++)
            foreach (XElement saverNode in saverNodes)
            {
                //NodeList savers = saverNodes.item(b).getChildNodes();
                IEnumerable<XElement> savers = saverNode.Descendants();
                if (saverNode.Name.LocalName.Equals(SERTIFIKA))
                {
                    aSavePolicy.setCertificateSavers(_createClassInfos(savers));
                }
                else if (saverNode.Name.LocalName.Equals(SIL))
                {
                    aSavePolicy.setCRLSavers(_createClassInfos(savers));
                }
                else if (saverNode.Name.LocalName.Equals(OCSP))
                {
                    aSavePolicy.setOCSPResponseSavers(_createClassInfos(savers));
                }
            }
        }


        /**
         * XML DOMElementinden sertifika kontrolcu politika siniflarini okur.
         */
        private static void _readCertificateCheckers(/*Node*/XElement aCertCheckersNode, ValidationPolicy aValidationPolicy)
        {
            //NodeList certCheckersNodes = aCertCheckersNode.getChildNodes();
            IEnumerable<XElement> certCheckersNodes = aCertCheckersNode.Elements();
            //for (int sk = 0; sk < certCheckersNodes.getLength(); sk++)
            foreach (XElement certCheckersNode in certCheckersNodes)
            {
                //NodeList certificateCheckers = certCheckersNodes.item(sk).getChildNodes();
                IEnumerable<XElement> certificateCheckers = certCheckersNode.Elements();
                if (certCheckersNode.Name.LocalName.Equals(TRUSTED_CERTIFICATE))
                {
                    aValidationPolicy.setTrustedCertificateCheckers(_createClassInfos(certificateCheckers));
                }
                if (certCheckersNode.Name.LocalName.Equals(TEK_SERTIFIKA))
                {
                    aValidationPolicy.setCertificateSelfCheckers(_createClassInfos(certificateCheckers));
                }
                else if (certCheckersNode.Name.LocalName.Equals(UST_SM))
                {
                    aValidationPolicy.setCertificateIssuerCheckers(_createClassInfos(certificateCheckers));
                }
                else if (certCheckersNode.Name.LocalName.Equals(IPTAL))
                {
                    //aValidationPolicy.setRevocationCheckers(_createClassInfos(certificateCheckers));
                    aValidationPolicy.setRevocationCheckers(_createRevocationClassInfos(certificateCheckers));

                }


            }
        }

        /**
         * XML DOMElementinden SİL kontrolcu politika sınıflarını okur.
         */
        private static void _readCRLCheckers(/*Node*/XElement aCRLCheckersNode, ValidationPolicy aValidationPolicy)
        {
            //NodeList crlCheckersNodes = aCRLCheckersNode.getChildNodes();
            IEnumerable<XElement> crlCheckersNodes = aCRLCheckersNode.Elements();
            //for (int sk = 0; sk < crlCheckersNodes.getLength(); sk++)
            foreach (XElement crlCheckersNode in crlCheckersNodes)
            {
                //NodeList crlCheckers = crlCheckersNodes.item(sk).getChildNodes();
                IEnumerable<XElement> crlCheckers = crlCheckersNode.Elements();
                if (crlCheckersNode.Name.LocalName.Equals(TEK_SIL))
                {
                    aValidationPolicy.setCRLSelfCheckers(_createClassInfos(crlCheckers));
                }
                else if (crlCheckersNode.Name.LocalName.Equals(SIL_SM))
                {
                    aValidationPolicy.setCRLIssuerCheckers(_createClassInfos(crlCheckers));
                }
            }
        }

        /**
         * XML DOMElementinden ocsp kontrolcu politika sınıflarını okur.
         */
        private static void _readOCSPCheckers(/*Node*/XElement aOCSPCheckersNode, ValidationPolicy aValidationPolicy)
        {
            //NodeList ocspCheckers = aOCSPCheckersNode.getChildNodes();
            IEnumerable<XElement> ocspCheckers = aOCSPCheckersNode.Elements();
            aValidationPolicy.setOCSPResponseCheckers(_createClassInfos(ocspCheckers));
        }

        /**
         * XML DOMElementinden delta-SİL kontrolcu politika sınıflarını okur.
         */
        private static void _readDeltaCRLCheckers(/*Node*/XElement aDeltaCRLCheckersNode, ValidationPolicy aValidationPolicy)
        {
            //NodeList deltaCRLCheckers = aDeltaCRLCheckersNode.getChildNodes();
            IEnumerable<XElement> deltaCRLCheckers = aDeltaCRLCheckersNode.Elements();
            aValidationPolicy.setDeltaCRLCheckers(_createClassInfos(deltaCRLCheckers));
        }


        /**
         * XML DOMElementinden kontrolcu politika sınıflarını okur.
         */
        private static void _readCheckers(/*Node*/XElement aCheckersNode, ValidationPolicy aValidationPolicy)
        {
            //NodeList checkerNodes = aCheckersNode.getChildNodes();
            IEnumerable<XElement> checkerNodes = aCheckersNode.Elements();
            //for (int d = 0; d < checkerNodes.getLength(); d++)
            foreach (XElement checkerNode in checkerNodes)
            {
                //Node checkerNode = checkerNodes.item(d);
                if (checkerNode.Name.LocalName.Equals(SERTIFIKA))
                {
                    _readCertificateCheckers(checkerNode, aValidationPolicy);
                }
                else if (checkerNode.Name.LocalName.Equals(SIL))
                {
                    _readCRLCheckers(checkerNode, aValidationPolicy);
                }
                else if (checkerNode.Name.LocalName.Equals(OCSP))
                {
                    _readOCSPCheckers(checkerNode, aValidationPolicy);
                }
                else if (checkerNode.Name.LocalName.Equals(DELTA_SIL))
                {
                    _readDeltaCRLCheckers(checkerNode, aValidationPolicy);
                }
            }
        }


        /**
         * XML DOMElementinden parametreleri okur.
         */
        static void _readParameters(XElement aParametreler, ValidationPolicy aValidationPolicy)
        {
            IEnumerable<XElement> parameters = aParametreler.Descendants();
            foreach (XElement parameter in parameters)
            {
                if (parameter.Name.LocalName.Equals(ST_USERPOLICYSET))
                {
                    String userPolicySet = parameter.Attribute(XName.Get(VALUE)).Value;

                    //aValidationPolicy.setUserInitialPolicySet(new List<String>(userPolicySet.Split(new Char[]{' '})));
                    aValidationPolicy.setUserInitialPolicySet(new List<String>(userPolicySet.Split(ST_POLICY_SEPERATOR.ToCharArray())));
                }
                else if (parameter.Name.LocalName.Equals(ST_INITIALEXPLICITPOLICY))
                {
                    String inhibitExplicitpolicy = parameter.Attribute(XName.Get(VALUE)).Value;
                    aValidationPolicy.setInitialExplicitPolicy(inhibitExplicitpolicy.Equals("TRUE", StringComparison.OrdinalIgnoreCase));
                }
                else if (parameter.Name.LocalName.Equals(ST_INITIALANYPOLICYINHIBIT))
                {
                    String inhibitAnyPolicyInhibit = parameter.Attribute(XName.Get(VALUE)).Value;
                    aValidationPolicy.setInitialAnyPolicyInhibit(inhibitAnyPolicyInhibit.Equals("TRUE", StringComparison.OrdinalIgnoreCase));
                }
                else if (parameter.Name.LocalName.Equals(ST_INITIALPOLICYMAPPINGINHIBIT))
                {
                    String inhibitPolicyMappingInhibit = parameter.Attribute(XName.Get(VALUE)).Value;
                    aValidationPolicy.setInitialPolicyMappingInhibit(inhibitPolicyMappingInhibit.Equals("TRUE", StringComparison.OrdinalIgnoreCase));
                }
                else if (parameter.Name.LocalName.Equals(ST_DEFAULTSTOREPATH))
                {
                    String defaultStorePath = parameter.Attribute(XName.Get(VALUE)).Value;
                    aValidationPolicy.setDefaultStorePath(defaultStorePath);
                }
            }
        }

        /**
         * XML bilgisinden politika sınıflarını olusturur.
         */
        private static List<PolicyClassInfo> _createClassInfos(/*NodeList*/IEnumerable<XElement> aClassTags)
        {
            List<PolicyClassInfo> classInfos = new List<PolicyClassInfo>();
            //while ((aClassTags.item(i) != null)) {
            foreach (XElement element in aClassTags)
            {
                //Node classNode = aClassTags.item(i);
                //if (classNode.getNodeName().equals(CLASS)) {
                if (element.Name.LocalName.Equals(CLASS))
                {
                    //NodeList parametreler = classNode.getChildNodes();
                    IEnumerable<XElement> parametreler = element.Elements();
                    Dictionary<String, Object> params_ = new Dictionary<String, Object>();
                    if (parametreler != null)
                    {
                        //for (int j = 0; j < parametreler.getLength(); j++) {
                        foreach (XElement parametre in parametreler)
                        {
                            if (/*parametreler.item(j).getNodeName().equals(PARAM)*/parametre.Name.LocalName.Equals(PARAM))
                            {
                                //String paramName = parametreler.item(j).getAttributes().item(0).getNodeValue();                           
                                //String paramValue = parametreler.item(j).getAttributes().item(1).getNodeValue();

                                String paramName = parametre.Attributes().ElementAt<XAttribute>(0).Value;
                                String paramValue = parametre.Attributes().ElementAt<XAttribute>(1).Value;
                                params_[paramName] = paramValue;

                                //foreach (XAttribute attribute in parametre.Attributes())
                                //{
                                //    params_.Add(attribute.Name.LocalName, attribute.Value);
                                //}
                            }
                        }
                    }
                    //classInfos.Add(new PolicyClassInfo(classNode.getAttributes().item(0).getNodeValue(), new ParameterList(params_)));
                    classInfos.Add(new PolicyClassInfo(element.Attributes().ElementAt<XAttribute>(0).Value, new ParameterList(params_)));
                }
            }
            return classInfos;
        }

        /**
        * XML bilgisinden politika sınıflarını olusturur.
        */
        //todo Test edilmesi gerekiyor 10.06.2011
        private static List<RevocationPolicyClassInfo> _createRevocationClassInfos(/*NodeList*/IEnumerable<XElement> aSinifTagleri)
        {
            List<RevocationPolicyClassInfo> revocationClassInfos = new List<RevocationPolicyClassInfo>();
            //int i = 0;

            //while(aSinifTagleri.item(i) !=null )
            foreach (XElement element in aSinifTagleri)
            {
                //Node sinif = aSinifTagleri.item(i);
                //if(sinif.getNodeName().equals(CLASS))
                if (element.Name.LocalName.Equals(CLASS))
                {
                    //NodeList childNodes = sinif.getChildNodes();
                    IEnumerable<XElement> parametreler = element.Elements();
                    //HashMap<String, Object> params = new HashMap<String, Object>();
                    Dictionary<String, Object> params_ = new Dictionary<String, Object>();
                    List<PolicyClassInfo> finderInfos = new List<PolicyClassInfo>();

                    //for(int j = 0; j < childNodes.getLength(); j++)
                    foreach (XElement parametre in parametreler)
                    {
                        //if(childNodes.item(j).getNodeName().equals(PARAM))
                        if (parametre.Name.LocalName.Equals(PARAM))
                        {
                            //String paramName = childNodes.item(j).getAttributes().item(0).getNodeValue();
                            //String paramValue = childNodes.item(j).getAttributes().item(1).getNodeValue();
                            String paramName = parametre.Attributes().ElementAt<XAttribute>(0).Value;
                            String paramValue = parametre.Attributes().ElementAt<XAttribute>(1).Value;
                            params_[paramName] = paramValue;
                        }
                        else if (parametre.Name.LocalName.Equals(FIND))/*if(childNodes.item(j).getNodeName().equals(FIND))*/
                        {
                            finderInfos.AddRange(_createClassInfos(parametre.Elements()/*childNodes.item(j).getChildNodes()*/));
                        }
                    }
                    //String sinifAdi = sinif.getAttributes().getNamedItem(NAME).getNodeValue();
                    String sinifAdi = element.Attribute(NAME).Value;    //TODO BURADA hata olabilir test et!!! 10.06.2011
                    revocationClassInfos.Add(new RevocationPolicyClassInfo(sinifAdi, new ParameterList(params_), finderInfos));
                }
                //i++;
            }
            return revocationClassInfos;
        }


        /**
         * Read validation policy from file
         * @param aPolicyFileName policy file name
         * @return validation policy
         * @throws ESYAException if file not found
         */
        public static ValidationPolicy readValidationPolicy(String aPolicyFileName)
        {
            try
            {
                logger.Debug("Read cert validation policy from : " + aPolicyFileName);
                using (FileStream inputStream = new FileStream(aPolicyFileName, FileMode.Open, FileAccess.Read))
                {
                    return readValidationPolicy(inputStream);
                }
            }
            catch (FileNotFoundException x)
            {
                throw new ESYAException("Policy config file not found: " + aPolicyFileName, x);
            }
        }

        /**
         * Verilen politika dosyasından doğrulama işleminde kullanılacak
         * {@link ValidationPolicy} oluşturur.
         * @param aPolicyStream xml file of policy
         * @return policy
         */
        public static ValidationPolicy readValidationPolicy(/*String*/Stream aPolicyStream)
        {
            ValidationPolicy validationPolicy = new ValidationPolicy();
            FindingPolicy findingPolicy = new FindingPolicy();
            MatchingPolicy matchingPolicy = new MatchingPolicy();
            SavePolicy savePolicy = new SavePolicy();

            try
            {
                /*DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                FileInputStream input = new FileInputStream(aPolicyFile);
                Document dosya = db.parse(input);
                */
                XmlReader reader = XmlReader.Create(aPolicyStream);
                XDocument dosya = XDocument.Load(reader);
                //IEnumerable<XElement> politika = dosya.Elements();
                IEnumerable<XElement> politika = dosya.Elements().Elements();
                //politika.Elements();
                //NodeList politika = dosya.getChildNodes().item(0).getChildNodes();
                foreach (XElement element in politika)
                {
                    //bulucular
                    if (element.Name.LocalName.Equals(FIND))
                    {
                        _readFindPolicy(element, findingPolicy);
                        validationPolicy.setFindingPolicy(findingPolicy);
                    }
                    //eslestiriciler
                    else if (element.Name.LocalName.Equals(MATCH))
                    {
                        _readMatchPolicy(element, matchingPolicy);
                        findingPolicy.setMatchingPolicy(matchingPolicy);
                    }
                    //kaydediciler
                    else if (element.Name.LocalName.Equals(KAYDETME))
                    {
                        _readSavePolicy(element, savePolicy);
                        findingPolicy.setSavePolicy(savePolicy);
                    }
                    //kontrolcular
                    else if (element.Name.LocalName.Equals(DOGRULAMA))
                    {
                        _readCheckers(element, validationPolicy);
                    }
                    //parametreler
                    else if (element.Name.LocalName.Equals(PARAMETRELER))
                    {
                        _readParameters(element, validationPolicy);
                    }
                }
            }
            catch (Exception x)
            {
                //x.printStackTrace();
                logger.Error("Cant process policy ", x);
                throw new ESYAException("Cant process policy : " + x.ToString());
                //Console.WriteLine(x.StackTrace);
            }
            return validationPolicy;
        }

        /**
         * Verilen politika url'inden doğrulama işleminde kullanılacak
         * {@link ValidationPolicy} oluşturur.
         * @param aPolicyURL xml file url of policy
         * @return policy
         */
        public static ValidationPolicy readValidationPolicyFromURL(String aPolicyURL)
        {
            ValidationPolicy validationPolicy = new ValidationPolicy();
            FindingPolicy findingPolicy = new FindingPolicy();
            MatchingPolicy matchingPolicy = new MatchingPolicy();
            SavePolicy savePolicy = new SavePolicy();

            try
            {
                /*DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                FileInputStream input = new FileInputStream(aPolicyFile);
                Document dosya = db.parse(input);
                */
                XmlReader reader = XmlReader.Create(aPolicyURL);
                XDocument dosya = XDocument.Load(reader);
                //IEnumerable<XElement> politika = dosya.Elements();
                IEnumerable<XElement> politika = dosya.Elements().Elements();
                //politika.Elements();
                //NodeList politika = dosya.getChildNodes().item(0).getChildNodes();
                foreach (XElement element in politika)
                {
                    //bulucular
                    if (element.Name.LocalName.Equals(FIND))
                    {
                        _readFindPolicy(element, findingPolicy);
                        validationPolicy.setFindingPolicy(findingPolicy);
                    }
                    //eslestiriciler
                    else if (element.Name.LocalName.Equals(MATCH))
                    {
                        _readMatchPolicy(element, matchingPolicy);
                        findingPolicy.setMatchingPolicy(matchingPolicy);
                    }
                    //kaydediciler
                    else if (element.Name.LocalName.Equals(KAYDETME))
                    {
                        _readSavePolicy(element, savePolicy);
                        findingPolicy.setSavePolicy(savePolicy);
                    }
                    //kontrolcular
                    else if (element.Name.LocalName.Equals(DOGRULAMA))
                    {
                        _readCheckers(element, validationPolicy);
                    }
                    //parametreler
                    else if (element.Name.LocalName.Equals(PARAMETRELER))
                    {
                        _readParameters(element, validationPolicy);
                    }
                }
            }
            catch (Exception x)
            {
                //x.printStackTrace();
                logger.Error("Cant process policy ", x);
                throw new ESYAException("Cant process policy : " + x.ToString());
                //Console.WriteLine(x.StackTrace);
            }
            return validationPolicy;
        }
    }
}
