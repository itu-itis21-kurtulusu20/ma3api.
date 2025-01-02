package tr.gov.tubitak.uekae.esya.api.tsl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation.TSLSchemeInformation;
import tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.TrustServiceProvider;
import tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.TrustServiceProviderList;
import tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service.TSPService;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResult;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.ValidationResultType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.Config;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.FileDocument;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TSL {
	protected static Logger logger = LoggerFactory.getLogger(TSL.class);
	private Document mDocument;
	private TrustServiceStatusList mTSL;
	private static String configPath="./config/xmlsignature-config.xml";
	
	private TSL(Document iDocument) throws TSLException,
			XPathExpressionException, ESYAException {
		mDocument = iDocument;
		parseTSLFile();
	}

	private TSL(String iTSLFilePath) throws TSLException,
			XPathExpressionException, ESYAException {
		try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
			mDocument = db.parse(iTSLFilePath);
		} catch (ParserConfigurationException e) {
			throw new TSLException(" XML dosyası çözümlenemedi.", e);
		} catch (SAXException e) {
			throw new TSLException("XML dosyası çözümlenemedi.", e);
		} catch (IOException e) {
			throw new TSLException("XML dosyası çözümlenemedi.", e);
		}
		parseTSLFile();
	}

	private TSL(InputStream iStream) throws TSLException,
			XPathExpressionException, ESYAException {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			mDocument = db.parse(iStream);
		} catch (ParserConfigurationException e) {
			throw new TSLException(" XML dosyası çözümlenemedi.", e);
		} catch (SAXException e) {
			throw new TSLException("XML dosyası çözümlenemedi.", e);
		} catch (IOException e) {
			throw new TSLException("XML dosyası çözümlenemedi.", e);
		}
		parseTSLFile();
	}

	/*
	 * TextReader? private TSL(TextReader iTxtReader) { mDocument = new
	 * Document(); mDocument.Load(iTxtReader); parseTSLFile(); }
	 */
	private void parseTSLFile() throws TSLException, XPathExpressionException, ESYAException {
		Element rootElement2=mDocument.getDocumentElement();
		mTSL = new TrustServiceStatusList(rootElement2);
	}

	public static TSL parse(Document iDocument) throws TSLException,
			XPathExpressionException, ESYAException {
		return new TSL(iDocument);
	}

	public static TSL parse(String iTSLFilePath) throws TSLException,
			XPathExpressionException, ESYAException {
		return new TSL(iTSLFilePath);
	}

	public static TSL parse(InputStream iStream) throws TSLException,
			XPathExpressionException, ESYAException {
		return new TSL(iStream);
	}

	/*
	 * public static TSL parse(TextReader iReader) { return new TSL(iReader); }
	 */
	public TrustServiceStatusList getTSLNode() {
		return mTSL;
	}

	public TSLSchemeInformation getSchemeInformation() {
		return mTSL.getSchemeInformation();
	}

	public TrustServiceProviderList getTrustServiceProviderList() {
		return mTSL.getTSPList();
	}

	public Element getSignature() {
		return mTSL.getSignatureElement();
	}

	public boolean validateTSL() throws TSLException, XMLSignatureException {
		if (isTSLUptoDate()) {
			ValidationResult vr;
			try {
				vr = validateSignature();
			} catch (UnsupportedEncodingException e) {
				logger.error("Error in TSL", e);
				return false;
			}
			if (vr.getType() == ValidationResultType.VALID) {
				return true;
			}
		}
		return false;
	}

	public boolean isSigned() {
		return mTSL.isSigned();
	}
	public void setConfigPath(String path) {
		configPath=path;
	}
	public String getConfigPath() {
		return configPath;
	}
	public ValidationResult validateSignature()
			throws UnsupportedEncodingException, TSLException,
			XMLSignatureException {
		if (mTSL.isSigned()) {
			Context context = new Context();
			//String path = TSL.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			//String decodepath = "./config/xmlsignature-config.xml";//URLDecoder.decode(path, "UTF-8");
			Config config = new Config(getConfigPath());//+ "\\xmlsignature-config.xml");
			context.setConfig(config);
			//config.getValidationConfig().getCertificateValidationPolicyFile();
			context.setDocument(mDocument);
			XMLSignature signature = XMLSignature.parse(new FileDocument(new File(mDocument.getBaseURI().substring(8))), context);

			// no params, use the certificate in key info
			ValidationResult result = signature.verify();

			return result;
		} else {
			throw new TSLException("TSL Document is not signed!");
		}
	}

	public boolean isTSLUptoDate() {
		Date issueDateTime = mTSL.getSchemeInformation().getListIssueDateTime()
				.getCalendar().toGregorianCalendar().getTime(); // ToUniversalTime();
		Date nextUpdate;
		Date now = new Date();

		if (mTSL.getSchemeInformation().getNextUpdate().isClosed()) {
			int result = issueDateTime.compareTo(now);
			if (result <= 0) {
				return true;
			} else {
				return false;
			}
		} else {
			nextUpdate = mTSL.getSchemeInformation().getNextUpdate()
					.getNextUpdate().toGregorianCalendar().getTime();
			int result = issueDateTime.compareTo(now);
			if (result <= 0) {
				result = now.compareTo(nextUpdate);
				if (result <= 0) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}

		}
	}

	public List<ECertificate> getAllCertificates() {
		List<ECertificate> certificates = new ArrayList<ECertificate>();
		for (TrustServiceProvider tsp : mTSL.getTSPList().getTSPList()) {
			for (TSPService service : tsp.getTSPServices().getTSPSerivceList()) {
				certificates.add(service.getServiceInformation()
						.getServiceDigitalIdentity().getX509Certificate());
			}
		}
		return certificates;
	}

	public List<ECertificate> getValidCertificates() {
		List<ECertificate> certificates = new ArrayList<ECertificate>();
		for (TrustServiceProvider tsp : mTSL.getTSPList().getTSPList()) {
			for (TSPService service : tsp.getTSPServices().getTSPSerivceList()) {
				if (service.getServiceInformation().getServiceStatus().getTextContent().contains("accredited")) {
					certificates.add(service.getServiceInformation().getServiceDigitalIdentity().getX509Certificate());
				}
			}
		}
		return certificates;
	}
	public List<ECertificate> getValidCACertificates() {
		List<ECertificate> certificates = new ArrayList<ECertificate>();
		for (TrustServiceProvider tsp : mTSL.getTSPList().getTSPList()) {
			for (TSPService service : tsp.getTSPServices().getTSPSerivceList()) {
				if (service.getServiceInformation().getServiceStatus().getTextContent().contains("accredited")) {
					if(service.getServiceInformation().getServiceDigitalIdentity().getX509Certificate().isCACertificate()){
					certificates.add(service.getServiceInformation().getServiceDigitalIdentity().getX509Certificate());
					}
				}
			}
		}
		return certificates;
	}
}
