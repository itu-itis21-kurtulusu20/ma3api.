package tr.gov.tubitak.uekae.esya.api.infra.certstore.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.GuvenlikSeviyesi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoKokSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCertificateOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreRootCertificateOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.CertificateSearchTemplate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

public class XMLStoreOps {

    private static Logger logger = LoggerFactory.getLogger(XMLStoreOps.class);

    protected static String VERSION = "1.1";

    public static void convertDBFromSQLiteToXML(String xmlStorePath) throws CertStoreException, ESYAException, ParserConfigurationException, TransformerException {
        convertDBFromSQLiteToXML(null, xmlStorePath);
    }

    // see test.esya.api.infra.certstore.xml.ConvertToXmlDb.testConvertXmlDb
    public static void convertDBFromSQLiteToXML(String sqlStorePath, String xmlStorePath) throws ParserConfigurationException, CertStoreException, ESYAException, TransformerException {
        // create the XML DB freshly
        Document doc;
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();
        }

        CertStore certStore;
        if (sqlStorePath != null) {
            certStore = new CertStore(sqlStorePath, null, null);
        } else { // default
            certStore = new CertStore();
        }

        Element sertifikalar;
        {
            List<DepoKokSertifika> ds;
            {
                CertStoreRootCertificateOps certStoreRootCertOps = new CertStoreRootCertificateOps(certStore);
                ds = certStoreRootCertOps.listStoreRootCertificates(null, null, GuvenlikSeviyesi.values());
            }

            Element rootElement = doc.createElement("depo");
            doc.appendChild(rootElement);

            Element version = doc.createElement("Versiyon");
            version.appendChild(doc.createTextNode(VERSION));
            rootElement.appendChild(version);

            Element koksertifikalar = doc.createElement("koksertifikalar");
            rootElement.appendChild(koksertifikalar);

            for (DepoKokSertifika depoKokSertifika : ds) {
                Element koksertifika = doc.createElement("koksertifika");
                XmlStoreUtil.createXmlFromObject(depoKokSertifika, koksertifika, doc);
                koksertifikalar.appendChild(koksertifika);
            }

            sertifikalar = doc.createElement("sertifikalar");
            rootElement.appendChild(sertifikalar);
        }

        ItemSource<DepoSertifika> sertifikaItemSource;
        {
            CertStoreCertificateOps certStoreCertOps = new CertStoreCertificateOps(certStore);
            CertificateSearchTemplate certSearchTemplate = new CertificateSearchTemplate();
            sertifikaItemSource = certStoreCertOps.listStoreCertificate(certSearchTemplate);
        }

        for (DepoSertifika depoSertifika = sertifikaItemSource.nextItem(); depoSertifika != null; depoSertifika = sertifikaItemSource.nextItem()) {
            XmlDepoSertifika depoXmlSertifika;
            depoXmlSertifika = new XmlDepoSertifika();
            depoXmlSertifika.mValue = depoSertifika.getValue();

            Element sertifika;
            sertifika = doc.createElement("sertifika");
            XmlStoreUtil.createXmlFromObject(depoXmlSertifika, sertifika, doc);

            sertifikalar.appendChild(sertifika);
        }

        saveDocument(xmlStorePath, doc);
    }

    public static void writeCertificate(String xmlStorePath, ECertificate certificate) throws ESYAException, ParserConfigurationException, IOException, TransformerException, SAXException {
        writeCertificate(xmlStorePath, certificate.getEncoded());
    }

    public static void writeCertificate(String xmlStorePath, byte[] certificateBytes) throws TransformerException, ESYAException, ParserConfigurationException, IOException, SAXException {
        Document doc = buildDocument(xmlStorePath);

        XmlDepoSertifika depoXmlSertifika;
        {
            depoXmlSertifika = new XmlDepoSertifika();
            depoXmlSertifika.mValue = certificateBytes;
        }

        Element sertifikalar = (Element) doc.getElementsByTagName("sertifikalar").item(0);

        Element sertifika = doc.createElement("sertifika");
        XmlStoreUtil.createXmlFromObject(depoXmlSertifika, sertifika, doc);
        sertifikalar.appendChild(sertifika);

        saveDocument(xmlStorePath, doc);
    }

    public static void deleteCertificate(String xmlStorePath, ECertificate certificate, boolean isRoot) throws ESYAException, ParserConfigurationException, IOException, SAXException, TransformerException {
        Document doc = buildDocument(xmlStorePath);

        String tagPrefix = (isRoot ? "kok" : "");

        Element sertifikalar = (Element) doc.getElementsByTagName(tagPrefix + "sertifikalar").item(0);
        NodeList sertifikaList = sertifikalar.getElementsByTagName(tagPrefix + "sertifika");

        logger.debug(sertifikaList.getLength() + (isRoot ? " root" : "") + " certificates");

        for (int i = 0; i < sertifikaList.getLength(); i++) {
            logger.debug(MessageFormat.format("{0} out of {1}", i + 1, sertifikaList.getLength()));

            Element sertifika = (Element) sertifikalar.getElementsByTagName(tagPrefix + "sertifika").item(i);
            Node mValue = sertifika.getElementsByTagName("mValue").item(0);
            byte[] certBytes = Base64.decode(mValue.getTextContent());
            if (Arrays.equals(certBytes, certificate.getEncoded())) {
                logger.debug("Found the certificate; removing...");

                sertifikalar.removeChild(sertifika);

                logger.debug("Removed certificate");
            }
        }

        saveDocument(xmlStorePath, doc);
    }

    public static void deleteCertificate(String xmlStorePath, int index, boolean isRoot) throws ESYAException, ParserConfigurationException, IOException, SAXException, TransformerException {
        Document doc = buildDocument(xmlStorePath);

        String tagPrefix = (isRoot ? "kok" : "");

        Element sertifikalar = (Element) doc.getElementsByTagName(tagPrefix + "sertifikalar").item(0);
        Element sertifika = (Element) sertifikalar.getElementsByTagName(tagPrefix + "sertifika").item(index);

        logger.debug("Removing certificate...");

        sertifikalar.removeChild(sertifika);
        saveDocument(xmlStorePath, doc);

        logger.debug("Removed certificate");
    }

    public static int countCertificates(String xmlStorePath, boolean isTrusted) throws ESYAException, IOException {
        InputStream is = Files.newInputStream(Paths.get(xmlStorePath));
        XMLStore xmlStore = new XMLStore(is);
        return (isTrusted ? xmlStore.getTrustedCertificates() : xmlStore.getCertificates()).size();
    }

    protected static Document buildDocument(String xmlStorePath) throws ParserConfigurationException, IOException, SAXException, ESYAException {
        Document doc;
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            if (Files.exists(Paths.get(xmlStorePath))) {
                doc = db.parse(xmlStorePath);
            } else {
                throw new ESYAException("Cannot find XML certificate store at the given path: " + xmlStorePath);
            }
        }
        return doc;
    }

    protected static void saveDocument(String xmlStorePath, Document doc) throws TransformerException {
        Transformer transformer;
        {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
        }

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(xmlStorePath));

        transformer.transform(source, result);
    }
}
