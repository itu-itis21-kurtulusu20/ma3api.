package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.creator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/20/14
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractOcspConfigurationCreator implements IOcspConfigurationCreator {
	private Logger logger = LoggerFactory.getLogger(AbstractOcspConfigurationCreator.class);

	protected Document doc;
	protected Element config;

	public AbstractOcspConfigurationCreator() throws Exception{
		constructRoot();
	}

	public void createConfiguration(IOcspConfigHolder configHolder) throws ESYAException{
		String defaultSigningAlgorithm = configHolder.getDefaultSigningAlgorithm();
		if (defaultSigningAlgorithm !=null){
			new PreferredSignAlgElement(defaultSigningAlgorithm).constructElement(doc);
		}
		List<ECertificate> signingCertificates = configHolder.getSigningCertificates();
		if (signingCertificates !=null && !signingCertificates.isEmpty()){
			addOcspCertificatesEncoded(signingCertificates);
		}
		List<String> supportedSigningAlgorithms = configHolder.getSupportedSigningAlgorithms();
		if (supportedSigningAlgorithms !=null && !supportedSigningAlgorithms.isEmpty()){
			addSupportedSignAlgs(supportedSigningAlgorithms);
		}

		List<IOcspConfigElement> signers = configHolder.getSigners();
		if (signers !=null && !signers.isEmpty()){
			addListByRootName(OcspConfigTags.SIGNERS, signers);
		}
		List<IOcspConfigElement> responsibleCas = configHolder.getResponsibleCas();
		if (responsibleCas !=null && !responsibleCas.isEmpty()){
			addListByRootName(OcspConfigTags.RESPONSIBLECAS, responsibleCas);
		}
		IOcspConfigElement nonceControl = configHolder.getNonceControl();
		if (nonceControl != null){
			appendToConfig(nonceControl);
		}
		ECertificate encryptionCertificate = configHolder.getEncryptionCertificate();
		if (encryptionCertificate!=null){
			addOcspEncCertificate(encryptionCertificate);
		}
		String url = configHolder.getUrl();
		if (url!=null && !url.isEmpty()){
			setUrl(url);
		}
	}

	protected void addListByRootName(OcspConfigTags rootTag , List<IOcspConfigElement> elements){
		for (IOcspConfigElement element : elements) {
			addElementByElementRootName(rootTag, element);
		}
	}

	public void setUrl(String url){
		if (url!=null){
			new URLConfigElement(url).constructElement(doc);
		}
	}

	private void constructRoot() throws Exception{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.newDocument();
		Element rootElement = doc.createElement(OcspConfigTags.OCSPCONFIGROOT.getTagName());
		doc.appendChild(rootElement);
		config = doc.createElement(OcspConfigTags.OCSPCONFIG.getTagName());
		rootElement.appendChild(config);
	}

	protected void addOcspCertificatesEncoded(List<ECertificate> aOcspSignCertificateList) {
		for (ECertificate certificate: aOcspSignCertificateList){
			IOcspConfigElement element = new OcspSignCertificateConfigElement(certificate);
			appendToConfig(element);
		}
	}

	protected void addSupportedSignAlgs(List<String> aSupportedSignAlgList) {
		for (String supportedSignAlg: aSupportedSignAlgList){
			IOcspConfigElement element = new SupportedSignAlgElement(supportedSignAlg);
			appendToConfig(element);
		}
	}

	protected void addOcspEncCertificate(ECertificate ocspEncryptionCertificate){
		if (ocspEncryptionCertificate!=null){
			IOcspConfigElement element = new OcspEncCertificateConfigElement(ocspEncryptionCertificate);
			element.constructElement(doc);
		}
	}

	protected void appendToConfig(IOcspConfigElement element){
		Element value = element.constructElement(doc);
		config.appendChild(value);
	}

	protected void addElementByElementRootName(OcspConfigTags rootTag, IOcspConfigElement element){
		Element subRoot;
		if (element!=null){
			NodeList elementsByTagName = doc.getElementsByTagName(rootTag.getTagName());
			if (elementsByTagName!=null && elementsByTagName.getLength() >0){
				subRoot = (Element)elementsByTagName.item(0);
			}
			else {
				subRoot = doc.createElement(rootTag.getTagName());
				config.appendChild(subRoot);
			}

			//wrapSensitiveData(element);
			subRoot.appendChild(element.constructElement(doc));
		}
	}

	/*protected void wrapSensitiveData(IOcspConfigElement element) throws Exception{
		if (ocspEncryptionCertificate!=null){
			if (element instanceof ISensitiveConfigElement){
				((ISensitiveConfigElement) element).wrapSensitiveData(getEncryptorCipher());
			}
		}
	}*/

	/*protected BufferedCipher getEncryptorCipher() throws Exception{
		BufferedCipher encryptor = Crypto.getEncryptor(CipherAlg.RSA_PKCS1);
		encryptor.init(KeyUtil.decodePublicKey(ocspEncryptionCertificate.getSubjectPublicKeyInfo()), null);
		return encryptor;
	}*/

	public String send(String url) throws ESYAException{
		HttpURLConnection con=null;
		OutputStreamWriter outputStreamWriter=null;
		String result=null;
		try {
			URL urlServlet = new URL(url);
			con = (HttpURLConnection)urlServlet.openConnection();
			con.setAllowUserInteraction(true);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type", "text/xml");
			outputStreamWriter = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
			StreamResult streamResult = new StreamResult(outputStreamWriter);
			writeXmlAsStream(streamResult);
			outputStreamWriter.flush();
			outputStreamWriter.close();
			InputStream in = con.getInputStream();
			ObjectInputStream inputFromServlet = new ObjectInputStream(in);
			try {
				result = (String)inputFromServlet.readObject();
			} catch (ClassNotFoundException e) {
				logger.error("Servlet'ten gelen cevap okunamadı");
				throw new ESYAException("Error while reading answer", e);
			}
			inputFromServlet.close();
			in.close();
		}
		catch (IOException ex){
			logger.error("Baglantı esnasında io exception hatası", ex);
			throw new ESYAException("Connection Failed:IO Exception occurred", ex);
		}
		finally {
			if (con!=null){
				con.disconnect();
			}
		}
		return result;
	}

	public void saveToFile(File file) throws ESYAException{
		StreamResult result = new StreamResult(file.toURI().getPath());
		writeXmlAsStream(result);
	}

	private void writeXmlAsStream(StreamResult streamResult) throws ESYAException{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, streamResult);
		}
		catch (Exception ex){
			logger.error("Dom kaynagı xml yapısına donusturulemedi", ex);
			throw new ESYAException("Dom document cannot be converted to xml", ex);
		}

	}

	public byte [] getStreamAsByteArray() throws ESYAException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(out);
		writeXmlAsStream(result);
		return out.toByteArray();
	}

}
