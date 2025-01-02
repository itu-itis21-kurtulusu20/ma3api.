package tr.gov.tubitak.uekae.esya.api.tsl.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.xml.NodeNamespaceContext;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_NAMESPACESPEC;

public class TSLUtil {
	protected static Logger logger = LoggerFactory.getLogger(TSLUtil.class);
	private static TSLUtil instance;
	private static String configPath="./config/tsl-config.xml";
	public static TSLUtil getInstance() {
		if (instance == null) {
			instance = new TSLUtil();
		}
		return instance;
	}

	private Document mDocument; // = new Document();

	List<String> languageList = new ArrayList<String>();
	List<String> countryList = new ArrayList<String>();
	List<String> tslTypeList = new ArrayList<String>();
	List<String> statusDetApproachList = new ArrayList<String>();
	List<String> serviceTypeIdList = new ArrayList<String>();
	List<String> serviceCurrentStatusList = new ArrayList<String>();
	List<String> schemeTypeCommRulesList = new ArrayList<String>();

	public TSLUtil() {
		try {
			loadTSLConfigFile();
		} catch (XPathExpressionException e) {
			logger.error("Error in TSLUtil", e);
		} catch (ParserConfigurationException e) {
			logger.error("Error in TSLUtil", e);
		} catch (SAXException e) {
			logger.error("Error in TSLUtil", e);
		} catch (IOException e) {
			logger.error("Error in TSLUtil", e);
		}
		initlanguageList();
		initCountryList();
	}
	public void setConfigPath(String path) {
		configPath=path;
	}
	public String getConfigPath() {
		return configPath;
	}
	private void loadTSLConfigFile() throws ParserConfigurationException,
			XPathExpressionException, SAXException, IOException {
		
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		//String path =TSL.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		//String decodepath = "./config/tsl-config.xml";//URLDecoder.decode(path, "UTF-8");
		mDocument = db.parse(getConfigPath()); //+ "\\tsl-config.xml");
		// mDocument.Load(Application.StartupPath + "\\tsl-config.xml");
		
		Element rootElement=mDocument.getDocumentElement();
		
		NodeList nodeList = rootElement.getElementsByTagName("TSLType");		
		if (nodeList != null && nodeList.getLength() == 1) {
			Element tslTypeElement=(Element) nodeList.item(0);
			initTSLTypeList(tslTypeElement);
		}
		nodeList = null;
		
		nodeList = rootElement.getElementsByTagName("StatusDeterminationApproach");		
		if (nodeList != null && nodeList.getLength() == 1) {
			Element statusDetApprElement = (Element) nodeList.item(0);
			initStatusDetApproachList(statusDetApprElement);
		}
		nodeList = null;
		
		nodeList = rootElement.getElementsByTagName("ServiceTypeIdentifier");		
		if (nodeList != null && nodeList.getLength() == 1) {
			Element statusDetApprElement = (Element) nodeList.item(0);
			initStatusDetApproachList(statusDetApprElement);
		}
		nodeList = null;
		
		nodeList = rootElement.getElementsByTagName("ServiceCurrentStatus");		
		if (nodeList != null && nodeList.getLength() == 1) {
			Element serviceCurrentStatusElement = (Element) nodeList.item(0);
			initServiceCurrentStatusList(serviceCurrentStatusElement);
		}
		nodeList = null;
		
		nodeList = rootElement.getElementsByTagName("SchemeTypeCommunityRules");		
		if (nodeList != null && nodeList.getLength() == 1) {
			Element schemeTypeCommRulesElement = (Element) nodeList.item(0);
			initSchemeTypeCommRulesList(schemeTypeCommRulesElement);
		}
	}

	private void initlanguageList() {
		languageList.add("tr");
		languageList.add("en");
		languageList.add("bg");
		languageList.add("cs");
		languageList.add("da");
		languageList.add("de");
		languageList.add("el");
		languageList.add("es");
		languageList.add("et");
		languageList.add("fi");
		languageList.add("fr");
		languageList.add("ga");
		languageList.add("hu");
		languageList.add("is");
		languageList.add("it");
		languageList.add("lb");
		languageList.add("lt");
		languageList.add("lv");
		languageList.add("mt");
		languageList.add("nb");
		languageList.add("nl");
		languageList.add("nn");
		languageList.add("no");
		languageList.add("pl");
		languageList.add("pt");
		languageList.add("ro");
		languageList.add("sk");
		languageList.add("sl");
		languageList.add("sv");
	}

	public List<String> LanguageList() {
		return languageList;
	}

	private void initCountryList() {
		countryList.add("TR");
		countryList.add("EU");
		countryList.add("AT");
		countryList.add("BE");
		countryList.add("BG");
		countryList.add("CY");
		countryList.add("CZ");
		countryList.add("DE");
		countryList.add("DK");
		countryList.add("EE");
		countryList.add("EL");
		countryList.add("ES");
		countryList.add("FI");
		countryList.add("FR");
		countryList.add("HU");
		countryList.add("IE");
		countryList.add("IS");
		countryList.add("IT");
		countryList.add("LI");
		countryList.add("LT");
		countryList.add("LU");
		countryList.add("LV");
		countryList.add("MT");
		countryList.add("NL");
		countryList.add("NO");
		countryList.add("PL");
		countryList.add("PT");
		countryList.add("RO");
		countryList.add("SE");
		countryList.add("SI");
		countryList.add("SK");
		countryList.add("UK");
	}

	public List<String> CountryList() {
		return countryList;
	}

	private void initTSLTypeList(Element iElement) {
		NodeList nodeList = iElement.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			tslTypeList.add(((Element) node).getTextContent());
		}
	}

	public List<String> TSLTypeList() {
		return tslTypeList;
	}

	public String TSLTypeValue(int pos) {
		if (pos >= 0 && pos < tslTypeList.size()) {
			return tslTypeList.get(pos);
		}
		return null;
	}

	private void initStatusDetApproachList(Element iElement) {
		NodeList nodeList = iElement.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			statusDetApproachList.add(((Element) node).getTextContent());
		}
	}

	public List<String> StatusDeterminationApproach() {
		return statusDetApproachList;
	}

	public String StatusDeterminationApproachValue(int pos) {
		if (pos >= 0 && pos < statusDetApproachList.size()) {
			return statusDetApproachList.get(pos);
		}
		return null;
	}

	private void initServiceTypeIdList(Element iElement) {
		NodeList nodeList = iElement.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			serviceTypeIdList.add(((Element) node).getTextContent());
		}
	}

	public List<String> ServiceTypeId() {
		return serviceTypeIdList;
	}

	public String ServiceTypeIdValue(int pos) {
		if (pos >= 0 && pos < serviceTypeIdList.size()) {
			return serviceTypeIdList.get(pos);
		}
		return null;
	}

	private void initServiceCurrentStatusList(Element iElement) {
		NodeList nodeList = iElement.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			serviceCurrentStatusList.add(((Element) node).getTextContent());
		}
	}

	public List<String> ServiceCurrentStatus() {
		return serviceCurrentStatusList;
	}

	public String ServiceCurrentStatusValue(int pos) {
		if (pos >= 0 && pos < serviceCurrentStatusList.size()) {
			return serviceCurrentStatusList.get(pos);
		}
		return null;
	}

	private void initSchemeTypeCommRulesList(Element iElement) {
		NodeList nodeList = iElement.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			schemeTypeCommRulesList.add(((Element) node).getTextContent());
		}
	}

	public List<String> SchemeTypeCommunityRules() {
		return schemeTypeCommRulesList;
	}

	public String SchemeTypeComminityRulesValue(int pos) {
		if (pos >= 0 && pos < schemeTypeCommRulesList.size()) {
			return schemeTypeCommRulesList.get(pos);
		}
		return null;
	}

	public XPath getNamespaceManager(Document iDocument) {
		// not sure
		Element nscontext = XmlUtil.createDSctx(mDocument, "tsl",
				Constants.NS_TSL);

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		xpath.setNamespaceContext(new NodeNamespaceContext(nscontext));
		return xpath;
	}
	public XPath getNamespaceManagerDS(Document iDocument) {
		// not sure
		Element nscontext = XmlUtil.createDSctx(mDocument, "ds",
				Constants.NS_XMLDSIG);

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		xpath.setNamespaceContext(new NodeNamespaceContext(nscontext));

		return xpath;
	}
    public static Element createDSMultiple(Document doc, String prefix, String namespace)
    {
        Element ctx = doc.createElementNS(null, "namespaceContext");

        ctx.setAttributeNS(NS_NAMESPACESPEC, "xmlns:" + prefix.trim(), namespace);

        return ctx;
    }
}
