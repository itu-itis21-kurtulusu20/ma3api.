package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/23/14
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class OcspSignCertificateConfigElement extends AbstractConfigElement {
	private OcspConfigTags[] tags = new OcspConfigTags[]{OcspConfigTags.OCSPSIGNCERT};

	public OcspSignCertificateConfigElement(){

	}

	public OcspSignCertificateConfigElement(ECertificate certificate){
		map.put(OcspConfigTags.OCSPSIGNCERT, Base64.encode(certificate.getEncoded()));
	}

	public OcspConfigTags getRootTag() {
		return OcspConfigTags.OCSPSIGNCERTS;
	}

	public OcspConfigTags[] getElementNames() {
		return tags;
	}

	public boolean isMultiple() {
		return false;
	}
}
