package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.reader;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element.IOcspConfigElement;

import java.io.InputStream;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/22/14
 * Time: 9:11 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IOcspConfigurationReader {
	void read(InputStream inputStream) throws Exception;
	String getPreferredSignAlg();
	List<IOcspConfigElement> getSigners();
	List<String> getSupportedSignAlg();
	List<IOcspConfigElement> getResponsibleCas();
	List<ECertificate> getOcspSignCertificates();
	ECertificate getOcspEncCertificate();
	boolean getNonceCheck();
	int getNonceCacheTime();
	boolean getOcspLog();
	boolean getNextUpdate();
	int getNextUpdateMinute();
	boolean getSaveResponse();

}
