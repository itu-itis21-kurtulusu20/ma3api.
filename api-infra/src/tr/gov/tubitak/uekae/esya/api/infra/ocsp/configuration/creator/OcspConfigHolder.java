package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.creator;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 6/16/14
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class OcspConfigHolder implements IOcspConfigHolder {
	private ECertificate encryptionCertificate;
	private String url;
	private String defaultSigningAlgorithm;
	private List<String> supportedSigningAlgoritms;
	private List<ECertificate> signingCertificates;
	private List<IOcspConfigElement> signers;
	private List<IOcspConfigElement> responsibleCas;
	private IOcspConfigElement nonceControl;


	public OcspConfigHolder(ECertificate encryptionCertificate, String url, String defaultSigningAlgorithm, List<String> supportedSigningAlgoritms, List<ECertificate> signingCertificates, List<IOcspConfigElement> signers, List<IOcspConfigElement> responsibleCas, IOcspConfigElement nonceControl) {
		this.encryptionCertificate = encryptionCertificate;
		this.url = url;
		this.defaultSigningAlgorithm = defaultSigningAlgorithm;
		this.supportedSigningAlgoritms = supportedSigningAlgoritms;
		this.signingCertificates = signingCertificates;
		this.signers = signers;
		this.responsibleCas = responsibleCas;
		this.nonceControl = nonceControl;
	}

	public List<ECertificate> getSigningCertificates() {
		return signingCertificates;
	}

	public String getUrl() {
		return url;
	}

	public String getDefaultSigningAlgorithm() {
		return defaultSigningAlgorithm;
	}

	public List<String> getSupportedSigningAlgorithms() {
		return supportedSigningAlgoritms;
	}

	public List<IOcspConfigElement> getSigners() {
		return signers;
	}

	public List<IOcspConfigElement> getResponsibleCas() {
		return responsibleCas;
	}

	public ECertificate getEncryptionCertificate() {
		return encryptionCertificate;
	}

	public IOcspConfigElement getNonceControl() {
		return nonceControl;
	}
}
