package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 6/10/14
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class OcspEncCertificateConfigElement extends AbstractConfigElement {
	private OcspConfigTags[] tags = new OcspConfigTags[]{OcspConfigTags.OCSPENCRCERT};

	public OcspEncCertificateConfigElement(){

	}

	public OcspEncCertificateConfigElement(ECertificate certificate){
		map.put(OcspConfigTags.OCSPENCRCERT, Base64.encode(certificate.getEncoded()));
	}

	public OcspConfigTags getRootTag() {
		return OcspConfigTags.OCSPCONFIG;
	}

	public OcspConfigTags[] getElementNames() {
		return tags;
	}

	public boolean isMultiple() {
		return false;
	}
}
