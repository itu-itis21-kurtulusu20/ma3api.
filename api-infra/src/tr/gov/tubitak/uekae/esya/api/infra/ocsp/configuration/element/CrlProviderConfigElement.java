package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element;

import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/21/14
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrlProviderConfigElement extends AbstractConfigElement implements IStatusProviderConfigElement {
	private OcspConfigTags[] tags = new OcspConfigTags[]{OcspConfigTags.CRLADDRESSPATH, OcspConfigTags.PERIOD};

	public CrlProviderConfigElement(String crlAddressPath, long period) {
		map.put(OcspConfigTags.CRLADDRESSPATH, crlAddressPath);
		map.put(OcspConfigTags.PERIOD, Long.toString(period));
	}

	public CrlProviderConfigElement() {

	}

	public void wrapSensitiveData(BufferedCipher cipher) throws Exception {
		//do nothing
	}

	public OcspConfigTags getRootTag() {
		return OcspConfigTags.CRLPROVIDER;
	}

	public OcspConfigTags[] getElementNames() {
		return tags;
	}

	public boolean isMultiple() {
		return true;
	}

	public String getCrlAddressPath() {
		return map.get(OcspConfigTags.CRLADDRESSPATH);
	}

	public long getPeriod() {
		return Long.parseLong(map.get(OcspConfigTags.PERIOD));
	}
}
