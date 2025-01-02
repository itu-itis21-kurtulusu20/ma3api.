package tr.gov.tubitak.uekae.esya.api.infra.certstore.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreUtil;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoKokSertifika;

public class XMLStore 
{
	private static final String DEFAULT_DEPO_DOSYA_ADI = "SertifikaDeposu.xml";
	Node rootNode;

	public XMLStore(InputStream is) throws ESYAException
	{
		init(is);
	}


	public XMLStore() throws ESYAException
	{
		String filePath = System.getProperty("user.home") +
				System.getProperty("file.separator") +
				CertStoreUtil.DEPO_DIZIN_ADI +
				System.getProperty("file.separator")+
				DEFAULT_DEPO_DOSYA_ADI;
		try
		{
			init(new FileInputStream(filePath));
		} 
		catch (FileNotFoundException e) 
		{
			throw new ESYAException("Sertifika Deposu XML dosyası bulunamadı." ,e);
		}
	}

	private void init(InputStream is) throws ESYAException
	{
		try 
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(is);

			rootNode = doc.getChildNodes().item(0);

		} 
		catch (ParserConfigurationException e) 
		{
			throw new ESYAException("Sertifika deposu icin XML dosyası çözümlenemedi." ,e);
		} 
		catch (SAXException e) 
		{
			throw new ESYAException("Sertifika deposu icin XML dosyası çözümlenemedi." ,e);
		} 
		catch (IOException e) 
		{
			throw new ESYAException("Sertifika deposu icin XML dosyası çözümlenemedi." ,e);
		}

	}

	public List<DepoKokSertifika> getTrustedCertificates() throws ESYAException
	{
		List<DepoKokSertifika> depoKokCertList = new ArrayList<DepoKokSertifika>();
		try
		{
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expression = xpath.compile("/depo/koksertifikalar/koksertifika");
			NodeList kokSertifikaNodeList = (NodeList) expression.evaluate(rootNode, XPathConstants.NODESET);
			for(int i=0; i < kokSertifikaNodeList.getLength(); i++)
			{
				DepoKokSertifika depoKokCert = new DepoKokSertifika();
				Node node = kokSertifikaNodeList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE)
				{
					Element element = (Element)node;
					XmlStoreUtil.fillObjectFromXml(depoKokCert, element);
					depoKokCertList.add(depoKokCert);
				}
			}
		}
		catch(Exception ex)
		{
			throw new ESYAException(ex);
		}

		return depoKokCertList;
	}


	public List<ECertificate> getCertificates() throws ESYAException
	{
		List<ECertificate> certList = new ArrayList<ECertificate>();
		try
		{
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expression = xpath.compile("/depo/sertifikalar/sertifika");
			NodeList sertifikaNodeList = (NodeList) expression.evaluate(rootNode, XPathConstants.NODESET);
			for(int i=0; i < sertifikaNodeList.getLength(); i++)
			{
				XmlDepoSertifika depoXmlCert = new XmlDepoSertifika();
				Node node = sertifikaNodeList.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE)
				{
					Element element = (Element)node;
					XmlStoreUtil.fillObjectFromXml(depoXmlCert, element);
					certList.add(new ECertificate(depoXmlCert.mValue));
				}
			}
		}
		catch(Exception ex)
		{
			throw new ESYAException(ex);
		}

		return certList;
	}


	public static void main(String[] args) throws Exception
	{
		XMLStore store = new XMLStore(new FileInputStream("C:\\file.xml"));
		List<DepoKokSertifika> trustedCertificates = store.getTrustedCertificates();
		System.out.println(trustedCertificates.size());
	}
}
