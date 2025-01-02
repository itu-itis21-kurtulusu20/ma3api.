package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/22/14
 * Time: 9:35 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IResponsibleCaElement extends ISensitiveConfigElement {
	ECertificate getCaCertificate();
	String getName();
	List<IOcspConfigElement> getStatusProviders();
	void addStatusProviders(List<IOcspConfigElement> configElement);
}
