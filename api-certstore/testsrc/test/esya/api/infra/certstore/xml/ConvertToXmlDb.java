package test.esya.api.infra.certstore.xml;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.GuvenlikSeviyesi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoKokSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCertificateOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreRootCertificateOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.CertificateSearchTemplate;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.xml.XMLStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.xml.XmlDepoSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.xml.XmlStoreUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ConvertToXmlDb {

    public String VERSION = "1.1";

    @Test
    public void testListXmlStoreCerts() throws Exception {
        XMLStore xmlStore = new XMLStore(new FileInputStream("T:\\api-certstore\\testdata\\SertifikaDeposu.xml"));
        List<DepoKokSertifika> trustedCertificates = xmlStore.getTrustedCertificates();
        System.out.println("############# KÖK SERTFİKALAR ###############");
        System.out.println("-----------------" + trustedCertificates.size() + " adet ----------------------");
        for (DepoKokSertifika xmlKok : trustedCertificates) {
            byte[] subjectName = xmlKok.getSubjectName();
            GuvenlikSeviyesi kokGuvenSeviyesi = xmlKok.getKokGuvenSeviyesi();
            EName sbName = new EName(subjectName);
            System.out.println("Sahibi :" + sbName.getCommonNameAttribute() + ", Güven Seviyesi:" + kokGuvenSeviyesi);
        }
        System.out.println("-----------------------------------------------");

        System.out.println("############# KİŞİSEL SERTİFİKALAR ###############");
        List<ECertificate> certificates = xmlStore.getCertificates();
        System.out.println("-----------------" + certificates.size() + " adet ----------------------");
        for (ECertificate eCertificate : certificates) {
            System.out.println("Sahibi :" + eCertificate.getSubject().getCommonNameAttribute());
        }
        System.out.println("-----------------------------------------------");
    }

    /**
     * {user.home}\.sertifikadeposu\SertifikaDeposu.svt yolundaki Sqlite dosyasından xml sertifika deposu yaratır.
     *
     * @throws Exception
     */
    @Test
    public void testConvertXmlDb() throws Exception {
        {
            CertStore certStore = new CertStore();
            CertStoreRootCertificateOps certStoreRootCertOps = new CertStoreRootCertificateOps(certStore);
            List<GuvenlikSeviyesi> guvenlikSeviyeleri = new ArrayList<GuvenlikSeviyesi>();
            guvenlikSeviyeleri.add(GuvenlikSeviyesi.LEGAL);
            guvenlikSeviyeleri.add(GuvenlikSeviyesi.ORGANIZATIONAL);
            List<DepoKokSertifika> ds = certStoreRootCertOps.listStoreRootCertificates(null, null,
                    guvenlikSeviyeleri.toArray(new GuvenlikSeviyesi[guvenlikSeviyeleri.size()]));

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
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


            Element sertifikalar = doc.createElement("sertifikalar");
            rootElement.appendChild(sertifikalar);

            CertificateSearchTemplate certSearchTemplate = new CertificateSearchTemplate();
            CertStoreCertificateOps certStoreCertOps = new CertStoreCertificateOps(certStore);
            ItemSource<DepoSertifika> sertifikaItemSource = certStoreCertOps.listStoreCertificate(certSearchTemplate);
            DepoSertifika depoSertifika = sertifikaItemSource.nextItem();
            while (depoSertifika != null) {
                XmlDepoSertifika depoXmlSertifika = new XmlDepoSertifika();
                depoXmlSertifika.mValue = depoSertifika.getValue();

                Element sertifika = doc.createElement("sertifika");

                XmlStoreUtil.createXmlFromObject(depoXmlSertifika, sertifika, doc);

                sertifikalar.appendChild(sertifika);

                depoSertifika = sertifikaItemSource.nextItem();
            }


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("T:\\api-certstore\\testdata\\SertifikaDeposu.xml"));
            transformer.transform(source, result);

            System.out.println("File saved!");
        }

    }
}
