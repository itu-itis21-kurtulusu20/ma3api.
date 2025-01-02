package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.reader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.ElementFactory;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element.*;
import tr.gov.tubitak.uekae.esya.api.infra.util.HashMultiMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/22/14
 * Time: 9:54 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractOcspConfigurationReader implements IOcspConfigurationReader {

	protected Document document;
	protected HashMultiMap<OcspConfigTags, IOcspConfigElement> multiMap = new HashMultiMap<OcspConfigTags, IOcspConfigElement>();
	public static OcspConfigTags[] elementNames = new OcspConfigTags[]{OcspConfigTags.SIGNER, OcspConfigTags.PREFERREDSIGNALG, OcspConfigTags.SUPPORTEDSIGNALG, OcspConfigTags.OCSPSIGNCERT, OcspConfigTags.OCSPENCRCERT, OcspConfigTags.RESPONSIBLECA, OcspConfigTags.URL, OcspConfigTags.NONCECONTROL};
	public static OcspConfigTags[] providerType = new OcspConfigTags[]{OcspConfigTags.CRLPROVIDER, OcspConfigTags.DBPROVIDER};
	public static int NEXTUPDATEMIN = 10;
	public static int NONCECACHETIME = 1;

	public void read(InputStream inputStream) throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		document = docBuilder.parse(inputStream);
		document.getDocumentElement().normalize();
		for (OcspConfigTags configTags : elementNames) {
			putValueByTagNameToMap(configTags);
		}
	}

	protected void putValueByTagNameToMap(OcspConfigTags tag) throws Exception {
		NodeList nodeList = findElementByTagNameFromDocument(tag);
		if (nodeList != null && nodeList.getLength() > 0) {
			for (IOcspConfigElement element : readValuesOfGivenElement(tag, nodeList)) {
				multiMap.put(tag, element);
			}
		}
	}

	private List<IOcspConfigElement> readValuesOfGivenElement(OcspConfigTags tag, NodeList list) throws Exception {
		List<IOcspConfigElement> elements = new ArrayList<IOcspConfigElement>();
		for (int i = 0; i < list.getLength(); i++) {
			Element element = (Element) list.item(i);
			IOcspConfigElement configElement = ElementFactory.createConfigElement(tag);
			if (element != null && configElement != null) {
				if (configElement instanceof IResponsibleCaElement) {
					((IResponsibleCaElement) configElement).addStatusProviders(findStatusProviders(element));
				}
				if (element.getNodeType() == Node.ELEMENT_NODE && configElement.getElementNames() != null) {
					for (OcspConfigTags configTags : configElement.getElementNames()) {
						String tagValue = getTagValue(configTags, element);
						if (tagValue != null) {
							configElement.addElement(configTags, tagValue);
						}
					}
				}
				elements.add(configElement);
			}
		}
		return elements;
	}

	private List<IOcspConfigElement> findStatusProviders(Element element) throws Exception {
		List<IOcspConfigElement> statusProviders = new ArrayList<IOcspConfigElement>();

		List<OcspConfigTags> remainingProviders = new ArrayList<OcspConfigTags>(Arrays.asList(providerType));

		NodeList statusProviderNodeList = element.getElementsByTagName(OcspConfigTags.STATUSPROVIDERS.getTagName());
		if (statusProviderNodeList != null && statusProviderNodeList.getLength() > 0) {
			Node statusProvidersNode = statusProviderNodeList.item(0); // ignore others if any
			String nodeName = "";
			OcspConfigTags configTag = null;
			for (int n = 0; n < statusProvidersNode.getChildNodes().getLength(); n++) {
				nodeName = statusProvidersNode.getChildNodes().item(n).getNodeName();
				configTag = OcspConfigTags.fromValue(nodeName);
				if (!remainingProviders.contains(configTag)) {
					continue;
				}
				NodeList statusProvidersNodeList = element.getElementsByTagName(nodeName);
				List<IOcspConfigElement> providerList = readValuesOfGivenElement(configTag, statusProvidersNodeList);
				if (providerList != null) {
					statusProviders.addAll(providerList);
					remainingProviders.remove(configTag);
				}
			}
		}
		return statusProviders;
	}

	protected NodeList findElementByTagNameFromDocument(OcspConfigTags tags) {
		return document.getElementsByTagName(tags.getTagName());
	}

	protected String getTagValue(OcspConfigTags tags, Element rootElement) {
		Node item;
		String value;
		if (rootElement.getNodeName().equals(tags.getTagName())) {
			item = rootElement.getChildNodes() != null ? rootElement.getChildNodes().item(0) : null;
			value = item != null ? item.getNodeValue() : null;
		} else {
			NodeList list = rootElement.getElementsByTagName(tags.getTagName());
			item = list != null ? list.item(0) : null;
			NodeList nlList = item != null ? item.getChildNodes() : null;
			Node nValue = nlList != null ? nlList.item(0) : null;
			value = nValue != null ? nValue.getNodeValue() : null;
		}
		return value;
	}

	public String getPreferredSignAlg() {
		return getStringValue(OcspConfigTags.PREFERREDSIGNALG);
	}

	public List<IOcspConfigElement> getSigners() {
		return multiMap.get(OcspConfigTags.SIGNER);
	}

	public List<String> getSupportedSignAlg() {
		return getStringValues(OcspConfigTags.SUPPORTEDSIGNALG);
	}

	protected List<String> getStringValues(OcspConfigTags tag) {
		List<String> values = new ArrayList<String>();
		List<IOcspConfigElement> elements = multiMap.get(tag);
		if (elements != null) {
			for (IOcspConfigElement element : elements) {
				values.add(element.getElementValueByTagName(tag));
			}
		}
		return values;
	}

	public String getUrl() {
		return getStringValue(OcspConfigTags.URL);
	}

	protected String getStringValue(OcspConfigTags tag) {
		List<String> list = getStringValues(tag);
		return !list.isEmpty() ? list.get(0) : null;
	}

	public List<IOcspConfigElement> getResponsibleCas() {
		return multiMap.get(OcspConfigTags.RESPONSIBLECA);
	}

	public List<ECertificate> getOcspSignCertificates() {
		List<ECertificate> signCertificate = new ArrayList<ECertificate>();
		for (IOcspConfigElement element : multiMap.get(OcspConfigTags.OCSPSIGNCERT)) {
			try {
				signCertificate.add(new ECertificate(element.getElementValueByTagName(OcspConfigTags.OCSPSIGNCERT)));
			} catch (ESYAException e) {

			}
		}
		return signCertificate;
	}

	public ECertificate getOcspEncCertificate() {
		try {
			return new ECertificate(getStringValue(OcspConfigTags.OCSPENCRCERT));
		} catch (ESYAException e) {

		}
		return null;
	}

	public boolean getNonceCheck(){
		List<IOcspConfigElement> list = multiMap.get(OcspConfigTags.NONCECONTROL);
		if (list != null && !list.isEmpty()){
			return ((NonceControlConfigElement)list.get(0)).isNonceCheck();
		} else {
			return false;
		}
	}

	public int getNonceCacheTime(){
		List<IOcspConfigElement> list = multiMap.get(OcspConfigTags.NONCECONTROL);
		if (list != null && !list.isEmpty()){
			return ((NonceControlConfigElement)list.get(0)).getNonceCacheTime();
		} else {
			return NONCECACHETIME;
		}
	}

	public boolean getOcspLog(){
		List<IOcspConfigElement> configElements = multiMap.get(OcspConfigTags.RESPONSIBLECA);
		for (IOcspConfigElement configElement : configElements){
			for (IOcspConfigElement statusProvider : ((ResponsibleCaElement)configElement).getStatusProviders()){
				if (statusProvider instanceof DbProviderConfigElement){
					return ((DbProviderConfigElement)statusProvider).isOcspLog();
				}
			}
		}
		return false;
	}

	public boolean getNextUpdate(){
		List<IOcspConfigElement> configElements = multiMap.get(OcspConfigTags.RESPONSIBLECA);
		for (IOcspConfigElement configElement : configElements){
			for (IOcspConfigElement statusProvider : ((ResponsibleCaElement)configElement).getStatusProviders()){
				if (statusProvider instanceof DbProviderConfigElement){
					return ((DbProviderConfigElement)statusProvider).isNextUpdate();
				}
			}
		}
		return false;
	}

	public int getNextUpdateMinute(){
		List<IOcspConfigElement> configElements = multiMap.get(OcspConfigTags.RESPONSIBLECA);
		for (IOcspConfigElement configElement : configElements){
			for (IOcspConfigElement statusProvider : ((ResponsibleCaElement)configElement).getStatusProviders()){
				if (statusProvider instanceof DbProviderConfigElement){
					return ((DbProviderConfigElement)statusProvider).getNextUpdateMinute();
				}
			}
		}
		return NEXTUPDATEMIN;
	}

	public boolean getSaveResponse(){
		List<IOcspConfigElement> configElements = multiMap.get(OcspConfigTags.RESPONSIBLECA);
		for (IOcspConfigElement configElement : configElements){
			for (IOcspConfigElement statusProvider : ((ResponsibleCaElement)configElement).getStatusProviders()){
				if (statusProvider instanceof DbProviderConfigElement){
					return ((DbProviderConfigElement)statusProvider).isSaveResponse();
				}
			}
		}
		return false;
	}
}