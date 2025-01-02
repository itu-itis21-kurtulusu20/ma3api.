package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element;

import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/23/14
 * Time: 2:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class URLConfigElement extends AbstractConfigElement {
	private OcspConfigTags[] tags = new OcspConfigTags[]{OcspConfigTags.URL};

	public URLConfigElement(){

	}

	public boolean isMultiple() {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}

	public URLConfigElement(String url){
		map.put(OcspConfigTags.URL, url);
	}

	public OcspConfigTags getRootTag() {
		return OcspConfigTags.OCSPCONFIG;
	}

	public OcspConfigTags[] getElementNames() {
		return tags;
	}
}
