package tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ParameterList;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.FindingPolicy;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.save.SavePolicy;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Validation Policy read operations
 */
public class PolicyReader
{
    private static Logger logger = LoggerFactory.getLogger(PolicyReader.class);

    private static final String CLASS = "class";
    private static final String PARAM = "param";
    private static final String NAME = "name";
    private static final String VALUE = "value";

    private static final String KAYDETME = "save";
    private static final String DOGRULAMA = "validate";
    private static final String PARAMETRELER = "parameters";
    private static final String FIND = "find";
    private static final String MATCH = "match";

    private static final String TRUSTED_CERTIFICATE = "trustedcertificate";
    private static final String SERTIFIKA = "certificate";
    private static final String SIL = "crl";
    private static final String OCSP = "ocsp";
    private static final String CAPRAZ = "cross";
    private static final String DELTA_SIL = "deltacrl";

    private static final String UST_SM = "issuer";
    private static final String IPTAL = "revocation";
    private static final String SIL_SM = "crlissuer";
    private static final String TEK_SIL = "crlself";
    private static final String TEK_SERTIFIKA = "self";


    private static final String ST_USERPOLICYSET = "UserPolicySet";
    private static final String ST_INITIALEXPLICITPOLICY = "InitialExplicitPolicy";
    private static final String ST_INITIALANYPOLICYINHIBIT = "InitialAnyPolicyInhibit";
    private static final String ST_INITIALPOLICYMAPPINGINHIBIT = "InitialPolicyMappingInhibit";
    private static final String ST_DEFAULTSTOREPATH = "DefaultStorePath";

    private static final String ST_POLICY_SEPERATOR = " ";

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
    private static void _readFindPolicy(Node aFindPolicyNode, FindingPolicy aFindingPolicy)
    {
        NodeList findRoot = aFindPolicyNode.getChildNodes();
        for (int b = 0; b < findRoot.getLength(); b++) {
            NodeList finders = findRoot.item(b).getChildNodes();
            if (findRoot.item(b).getNodeName().equals(SERTIFIKA)) {
                aFindingPolicy.setCertificateFinders(_createClassInfos(finders));
            }
            else if (findRoot.item(b).getNodeName().equals(TRUSTED_CERTIFICATE)) {
                aFindingPolicy.setTrustedCertificateFinders(_createClassInfos(finders));
            }
            else if (findRoot.item(b).getNodeName().equals(SIL)) {
                aFindingPolicy.setCRLFinders(_createClassInfos(finders));
            }
            else if (findRoot.item(b).getNodeName().equals(OCSP)) {
                aFindingPolicy.setOCSPResponseFinders(_createClassInfos(finders));
            }
            else if (findRoot.item(b).getNodeName().equals(CAPRAZ)) {
                aFindingPolicy.setCrossCertificateFinders(_createClassInfos(finders));
            }
            else if (findRoot.item(b).getNodeName().equals(DELTA_SIL)) {
                aFindingPolicy.setDeltaCRLFinders(_createClassInfos(finders));
            }
        }
    }

    /**
     * XML DOMElementinden eşleştirme politika sınıflarını okur.
     */
    private static void _readMatchPolicy(Node aMatchPolicyNode, MatchingPolicy aMatchingPolicy)
    {
        NodeList aMatchRoot = aMatchPolicyNode.getChildNodes();
        for (int b = 0; b < aMatchRoot.getLength(); b++) {
            NodeList matchers = aMatchRoot.item(b).getChildNodes();
            if (aMatchRoot.item(b).getNodeName().equals(SERTIFIKA)) {
                aMatchingPolicy.setCertificateMatchers(_createClassInfos(matchers));
            }
            else if (aMatchRoot.item(b).getNodeName().equals(SIL)) {
                aMatchingPolicy.setCRLMatchers(_createClassInfos(matchers));
            }
            else if (aMatchRoot.item(b).getNodeName().equals(CAPRAZ)) {
                aMatchingPolicy.setCrossCertificateMatchers(_createClassInfos(matchers));
            }
            else if (aMatchRoot.item(b).getNodeName().equals(DELTA_SIL)) {
                aMatchingPolicy.setDeltaCRLMatchers(_createClassInfos(matchers));
            }
            else if (aMatchRoot.item(b).getNodeName().equals(OCSP)) {
                aMatchingPolicy.setOCSPResponseMatchers(_createClassInfos(matchers));
            }
        }
    }


    /**
     * XML DOMElementinden kaydetme politika sınıflarını okur.
     */
    private static void _readSavePolicy(Node aSaversNode, SavePolicy aSavePolicy)
    {
        NodeList saverNodes = aSaversNode.getChildNodes();
        for (int b = 0; b < saverNodes.getLength(); b++) {
            NodeList savers = saverNodes.item(b).getChildNodes();
            if (saverNodes.item(b).getNodeName().equals(SERTIFIKA)) {
                aSavePolicy.setCertificateSavers(_createClassInfos(savers));
            }
            else if (saverNodes.item(b).getNodeName().equals(SIL)) {
                aSavePolicy.setCRLSavers(_createClassInfos(savers));
            }
            else if (saverNodes.item(b).getNodeName().equals(OCSP)) {
                aSavePolicy.setOCSPResponseSavers(_createClassInfos(savers));
            }
        }
    }


    /**
     * XML DOMElementinden sertifika kontrolcu politika sınıflarını okur.
     */
    private static void _readCertificateCheckers(Node aCertCheckersNode, ValidationPolicy aValidationPolicy)
    {
        NodeList certCheckersNodes = aCertCheckersNode.getChildNodes();
        for (int sk = 0; sk < certCheckersNodes.getLength(); sk++) {
            NodeList certificateCheckers = certCheckersNodes.item(sk).getChildNodes();
            if (certCheckersNodes.item(sk).getNodeName().equals(TRUSTED_CERTIFICATE)) {
                aValidationPolicy.setTrustedCertificateCheckers(_createClassInfos(certificateCheckers));
            }
            if (certCheckersNodes.item(sk).getNodeName().equals(TEK_SERTIFIKA)) {
                aValidationPolicy.setCertificateSelfCheckers(_createClassInfos(certificateCheckers));
            }
            else if (certCheckersNodes.item(sk).getNodeName().equals(UST_SM)) {
                aValidationPolicy.setCertificateIssuerCheckers(_createClassInfos(certificateCheckers));
            }
            else if (certCheckersNodes.item(sk).getNodeName().equals(IPTAL)) {
                aValidationPolicy.setRevocationCheckers(_createRevocationClassInfos(certificateCheckers));
            }


        }
    }

    /**
     * XML DOMElementinden SİL kontrolcu politika sınıflarını okur.
     */
    private static void _readCRLCheckers(Node aCRLCheckersNode, ValidationPolicy aValidationPolicy)
    {
        NodeList crlCheckersNodes = aCRLCheckersNode.getChildNodes();
        for (int sk = 0; sk < crlCheckersNodes.getLength(); sk++) {
            NodeList crlCheckers = crlCheckersNodes.item(sk).getChildNodes();
            if (crlCheckersNodes.item(sk).getNodeName().equals(TEK_SIL)) {
                aValidationPolicy.setCRLSelfCheckers(_createClassInfos(crlCheckers));
            }
            else if (crlCheckersNodes.item(sk).getNodeName().equals(SIL_SM)) {
                aValidationPolicy.setCRLIssuerCheckers(_createClassInfos(crlCheckers));
            }
        }
    }

    /**
     * XML DOMElementinden ocsp kontrolcu politika sınıflarını okur.
     */
    private static void _readOCSPCheckers(Node aOCSPCheckersNode, ValidationPolicy aValidationPolicy)
    {
        NodeList ocspCheckers = aOCSPCheckersNode.getChildNodes();
        aValidationPolicy.setOCSPResponseCheckers(_createClassInfos(ocspCheckers));
    }

    /**
     * XML DOMElementinden delta-SİL kontrolcu politika sınıflarını okur.
     */
    private static void _readDeltaCRLCheckers(Node aDeltaCRLCheckersNode, ValidationPolicy aValidationPolicy)
    {
        NodeList deltaCRLCheckers = aDeltaCRLCheckersNode.getChildNodes();
        aValidationPolicy.setDeltaCRLCheckers(_createClassInfos(deltaCRLCheckers));
    }


    /**
     * XML DOMElementinden kontrolcu politika sınıflarını okur.
     */
    private static void _readCheckers(Node aCheckersNode, ValidationPolicy aValidationPolicy)

    {
        NodeList checkerNodes = aCheckersNode.getChildNodes();
        for (int d = 0; d < checkerNodes.getLength(); d++) {
            Node checkerNode = checkerNodes.item(d);
            if (checkerNode.getNodeName().equals(SERTIFIKA)) {
                _readCertificateCheckers(checkerNode, aValidationPolicy);
            }
            else if (checkerNode.getNodeName().equals(SIL)) {
                _readCRLCheckers(checkerNode, aValidationPolicy);
            }
            else if (checkerNode.getNodeName().equals(OCSP)) {
                _readOCSPCheckers(checkerNode, aValidationPolicy);
            }
            else if (checkerNode.getNodeName().equals(DELTA_SIL)) {
                _readDeltaCRLCheckers(checkerNode, aValidationPolicy);
            }
        }
    }


    /**
     * XML DOMElementinden parametreleri okur.
     */
    static void _readParameters(Element aParametreler, ValidationPolicy aValidationPolicy)
    {
        Element deUserPolicySet = (Element)aParametreler.getElementsByTagName(ST_USERPOLICYSET).item(0);
        if (deUserPolicySet != null) {
            String userPolicySet = deUserPolicySet.getAttribute(VALUE);
            aValidationPolicy.setUserInitialPolicySet(Arrays.asList(userPolicySet.split(ST_POLICY_SEPERATOR)));
        }
        
        Element deDefaultStorePath = (Element)aParametreler.getElementsByTagName(ST_DEFAULTSTOREPATH).item(0);
        if (deDefaultStorePath != null) {
        	String defaultStorePath = deDefaultStorePath.getAttribute(VALUE);
            aValidationPolicy.setDefaultStorePath(defaultStorePath);
        }

        Element deInhibitExplicitpolicy = (Element)aParametreler.getElementsByTagName(ST_INITIALEXPLICITPOLICY).item(0);
        if (deUserPolicySet != null) {
            String inhibitExplicitpolicy = deInhibitExplicitpolicy.getAttribute(VALUE);
            aValidationPolicy.setInitialExplicitPolicy(inhibitExplicitpolicy.equalsIgnoreCase("TRUE"));
        }

        Element deInhibitAnyPolicyInhibit = (Element)aParametreler.getElementsByTagName(ST_INITIALANYPOLICYINHIBIT).item(0);
        if (deUserPolicySet != null) {
            String inhibitAnyPolicyInhibit = deInhibitAnyPolicyInhibit.getAttribute(VALUE);
            aValidationPolicy.setInitialAnyPolicyInhibit(inhibitAnyPolicyInhibit.equalsIgnoreCase("TRUE"));
        }

        Element deInhibitPolicyMappingInhibit = (Element)aParametreler.getElementsByTagName(ST_INITIALPOLICYMAPPINGINHIBIT).item(0);
        if (deUserPolicySet != null) {
            String inhibitPolicyMappingInhibit = deInhibitPolicyMappingInhibit.getAttribute(VALUE);
            aValidationPolicy.setInitialPolicyMappingInhibit(inhibitPolicyMappingInhibit.equalsIgnoreCase("TRUE"));
        }

    }

    /**
     * XML bilgisinden politika sınıflarını oluşturur.
     */
    private static List<PolicyClassInfo> _createClassInfos(NodeList aClassTags)
    {
        List<PolicyClassInfo> classInfos = new ArrayList<PolicyClassInfo>();
        int i = 0;
        while ((aClassTags.item(i) != null)) {
            Node classNode = aClassTags.item(i);
            if (classNode.getNodeName().equals(CLASS)) {

                NodeList parametreler = classNode.getChildNodes();
                HashMap<String, Object> params = new HashMap<String, Object>();
                if (parametreler != null) {
                    for (int j = 0; j < parametreler.getLength(); j++) {
                        if (parametreler.item(j).getNodeName().equals(PARAM)) {
                            String paramName = parametreler.item(j).getAttributes().item(0).getNodeValue();
                            String paramValue = parametreler.item(j).getAttributes().item(1).getNodeValue();
                            params.put(paramName, paramValue);

                        }
                    }
                }
                classInfos.add(new PolicyClassInfo(classNode.getAttributes().item(0).getNodeValue(), new ParameterList(params)));
            }
            i++;
        }
        return classInfos;
    }

    /**
    * XML bilgisinden politika sınıflarını oluşturur.
    */
    private static List<RevocationPolicyClassInfo> _createRevocationClassInfos(NodeList aSinifTagleri)
    {
        List<RevocationPolicyClassInfo> revocationClassInfos = new ArrayList<RevocationPolicyClassInfo>();
        int i = 0;

        while(aSinifTagleri.item(i) !=null )
        {
            Node sinif = aSinifTagleri.item(i);
            if(sinif.getNodeName().equals(CLASS))
            {
                NodeList childNodes = sinif.getChildNodes();
                HashMap<String, Object> params = new HashMap<String, Object>();
                List<PolicyClassInfo> finderInfos = new ArrayList<PolicyClassInfo>();

                for(int j = 0; j < childNodes.getLength(); j++)
                {
                    if(childNodes.item(j).getNodeName().equals(PARAM))
                    {
                        String paramName = childNodes.item(j).getAttributes().item(0).getNodeValue();
                        String paramValue = childNodes.item(j).getAttributes().item(1).getNodeValue();
                        params.put(paramName, paramValue);
                    }
                    else if(childNodes.item(j).getNodeName().equals(FIND))
                    {
                        finderInfos.addAll(_createClassInfos(childNodes.item(j).getChildNodes()));
                    }
                }
                String sinifAdi = sinif.getAttributes().getNamedItem(NAME).getNodeValue();
                revocationClassInfos.add(new RevocationPolicyClassInfo(sinifAdi , new ParameterList(params),finderInfos));
            }
            i++;
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
            throws ESYAException
    {
        try {
            logger.debug("Read cert validation policy from : "+aPolicyFileName);
            FileInputStream inputStream = new FileInputStream(aPolicyFileName);
            return readValidationPolicy(inputStream);
        } catch (FileNotFoundException x){
            throw new ESYAException("Policy config file not found: "+aPolicyFileName, x);
        }
    }


    /**
     * Verilen politika dosyasından doğrulama işleminde kullanılacak
     * {@link ValidationPolicy} oluşturur.
     * @param aPolicyStream xml file of policy
     * @return policy
     */
    public static ValidationPolicy readValidationPolicy(InputStream aPolicyStream)
            throws ESYAException
    {
        ValidationPolicy validationPolicy = new ValidationPolicy();
        FindingPolicy findingPolicy = new FindingPolicy();
        MatchingPolicy matchingPolicy = new MatchingPolicy();
        SavePolicy savePolicy = new SavePolicy();

        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document dosya = db.parse(aPolicyStream);

            NodeList politika = dosya.getChildNodes().item(0).getChildNodes();

            for (int i = 0; i < politika.getLength(); i++) {
                Node node = politika.item(i);
                //bulucular
                if (node.getNodeName().equals(FIND)) {
                    _readFindPolicy(node, findingPolicy);
                    validationPolicy.setFindingPolicy(findingPolicy);
                }
                //eslestiriciler
                else if (node.getNodeName().equals(MATCH)) {
                    _readMatchPolicy(node, matchingPolicy);
                    findingPolicy.setMatchingPolicy(matchingPolicy);
                }
                //kaydediciler
                else if (node.getNodeName().equals(KAYDETME)) {
                    _readSavePolicy(node, savePolicy);
                    findingPolicy.setSavePolicy(savePolicy);
                }
                //kontrolcular
                else if (node.getNodeName().equals(DOGRULAMA)) {
                    _readCheckers(node, validationPolicy);
                }
                //parametreler
                else if (node.getNodeName().equals(PARAMETRELER)) {
                    _readParameters((Element)node, validationPolicy);
                }
            }
        }
        catch (Exception x) {
            logger.error("Cant process policy ", x);
            throw new ESYAException("Cant process policy : "+x.getMessage());
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
            throws ESYAException
    {
        ValidationPolicy validationPolicy = new ValidationPolicy();
        FindingPolicy findingPolicy = new FindingPolicy();
        MatchingPolicy matchingPolicy = new MatchingPolicy();
        SavePolicy savePolicy = new SavePolicy();

        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document dosya = db.parse(aPolicyURL);

            NodeList politika = dosya.getChildNodes().item(0).getChildNodes();

            for (int i = 0; i < politika.getLength(); i++) {
                Node node = politika.item(i);
                //bulucular
                if (node.getNodeName().equals(FIND)) {
                    _readFindPolicy(node, findingPolicy);
                    validationPolicy.setFindingPolicy(findingPolicy);
                }
                //eslestiriciler
                else if (node.getNodeName().equals(MATCH)) {
                    _readMatchPolicy(node, matchingPolicy);
                    findingPolicy.setMatchingPolicy(matchingPolicy);
                }
                //kaydediciler
                else if (node.getNodeName().equals(KAYDETME)) {
                    _readSavePolicy(node, savePolicy);
                    findingPolicy.setSavePolicy(savePolicy);
                }
                //kontrolcular
                else if (node.getNodeName().equals(DOGRULAMA)) {
                    _readCheckers(node, validationPolicy);
                }
                //parametreler
                else if (node.getNodeName().equals(PARAMETRELER)) {
                    _readParameters((Element)node, validationPolicy);
                }
            }
        }
        catch (Exception x) {
            logger.error("Cant process policy ", x);
            throw new ESYAException("Cant process policy : "+x.getMessage());
        }
        return validationPolicy;
    }

    public static void main(String[] args) throws Exception
    {
        PolicyReader.readValidationPolicy("T:\\api-xmlsignature\\config\\certval-pt2010-scok-config.xml");
    }

}

