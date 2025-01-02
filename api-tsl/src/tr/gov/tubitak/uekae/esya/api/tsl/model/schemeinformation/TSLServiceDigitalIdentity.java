package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Base64;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLServiceDigitalIdentity extends BaseElement {
	private String certificateBase64Str;
	private ECertificate certificate;

	private Element digitalIdElement;
	private Element x509CertificateElement;

	public TSLServiceDigitalIdentity(Document document,
			ECertificate iCertificate) {
		super(document);
		certificate = iCertificate;
		 certificateBase64Str = Base64.encode(certificate.getEncoded());

		x509CertificateElement = createElement(Constants.NS_TSL,
				Constants.TAG_X509CERTIFICATE);
		x509CertificateElement.setTextContent(certificateBase64Str);

		digitalIdElement = createElement(Constants.NS_TSL,
				Constants.TAG_DIGITALID);
		digitalIdElement.appendChild(document.createTextNode("\n"));
		digitalIdElement.appendChild(x509CertificateElement);
		digitalIdElement.appendChild(document.createTextNode("\n"));

		addLineBreak();
		mElement.appendChild(digitalIdElement);
		addLineBreak();
	}

	public TSLServiceDigitalIdentity(Element aElement) throws TSLException, ESYAException {
		super(aElement);
		digitalIdElement = XmlUtil.getNextElement(aElement.getFirstChild());
		x509CertificateElement = XmlUtil.getNextElement(digitalIdElement
				.getFirstChild());

		certificateBase64Str = XmlUtil.getText(x509CertificateElement);
		 certificate = new ECertificate(certificateBase64Str);
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_SERVICEDIGITALIDENTITY;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSL;
	}

	public ECertificate getX509Certificate() {
		return certificate;
	}

	public String getBase64EncodedCertificate() {
		return certificateBase64Str;
	}
}
