package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/20/14
 * Time: 3:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResponsibleCaElement extends AbstractConfigElement implements IResponsibleCaElement {

	private List<IOcspConfigElement> statusProviders;
	private OcspConfigTags[] tags = new OcspConfigTags[]{OcspConfigTags.NAME, OcspConfigTags.CACERTIFICATE};

	public ResponsibleCaElement(){

	}

	public ResponsibleCaElement(ECertificate caCertificate, String name, List<IOcspConfigElement> statusProviders) {
		map.put(OcspConfigTags.NAME, name);
		map.put(OcspConfigTags.CACERTIFICATE, Base64.encode(caCertificate.getEncoded()));
		this.statusProviders = statusProviders;
	}

	public Element constructElement(Document document) {
		Element rootElement = super.constructElement(document);
		Element statusProvidersRootElement = document.createElement(OcspConfigTags.STATUSPROVIDERS.getTagName());
		rootElement.appendChild(statusProvidersRootElement);
		for (IOcspConfigElement element: statusProviders){
			statusProvidersRootElement.appendChild(element.constructElement(document));

		}
		return rootElement;
	}

	public OcspConfigTags getRootTag() {
		return OcspConfigTags.RESPONSIBLECA;
	}

	public OcspConfigTags[] getElementNames() {
		return tags;
	}

	public boolean isMultiple() {
		return true;
	}

	/*public void wrapSensitiveData(BufferedCipher cipher) throws Exception {
		for (IOcspConfigElement statusProvider : statusProviders) {
			((IStatusProviderConfigElement)statusProvider).wrapSensitiveData(cipher);
		}
	}*/

	public ECertificate getCaCertificate() {
		try {
			return new ECertificate(map.get(OcspConfigTags.CACERTIFICATE));
		} catch (ESYAException e) {

		}
		return null;
	}

	public String getName() {
		return map.get(OcspConfigTags.NAME);
	}

	public List<IOcspConfigElement> getStatusProviders() {
		return statusProviders;
	}

	public void addStatusProviders(List<IOcspConfigElement> configElement) {
		this.statusProviders = configElement;
	}
}
