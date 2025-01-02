package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element;

import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/23/14
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupportedSignAlgElement extends AbstractConfigElement {
	private OcspConfigTags[] tags = new OcspConfigTags[]{OcspConfigTags.SUPPORTEDSIGNALG};

	public SupportedSignAlgElement(){

	}

	public SupportedSignAlgElement(String supportedSignAlg){
		map.put(OcspConfigTags.SUPPORTEDSIGNALG, supportedSignAlg);
	}

	public OcspConfigTags getRootTag() {
		return OcspConfigTags.SUPPORTEDSIGNALGS;
	}

	public OcspConfigTags[] getElementNames() {
		return tags;
	}

	public boolean isMultiple() {
		return false;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
