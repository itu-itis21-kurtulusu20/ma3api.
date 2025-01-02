package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.creator;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 6/16/14
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IOcspConfigHolder {
	List<ECertificate> getSigningCertificates();
	String getUrl();
	String getDefaultSigningAlgorithm();
	List<String> getSupportedSigningAlgorithms();
	List<IOcspConfigElement> getSigners();
	List<IOcspConfigElement> getResponsibleCas();
	ECertificate getEncryptionCertificate();
	IOcspConfigElement getNonceControl();
}
