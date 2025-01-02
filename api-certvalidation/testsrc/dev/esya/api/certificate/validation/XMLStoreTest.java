package dev.esya.api.certificate.validation;

import org.junit.*;
import org.junit.runners.MethodSorters;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.xml.XMLStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.xml.XMLStoreOps;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

// sort test methods so that deletion tests are called after addition tests
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class XMLStoreTest {

    final static String xmlStorePath = "T:\\api-certvalidation\\files\\SertifikaDeposu-test.xml";
    final String certPath = "T:\\api-certvalidation\\testdata\\certs\\ValidCertificatePathTest1EE.crt";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        XMLStoreOps.convertDBFromSQLiteToXML(null, xmlStorePath);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        Files.deleteIfExists(Paths.get(xmlStorePath));
    }

    @Test
    public void test_01_CountCertificates() throws ESYAException {
        XMLStore xmlStore = new XMLStore();

        int numOfCerts = xmlStore.getCertificates().size();
        Assert.assertEquals(20, numOfCerts);

        int numOfTrustedCerts = xmlStore.getTrustedCertificates().size();
        Assert.assertEquals(28, numOfTrustedCerts);
    }

    @Test
    public void test_02_AddCert() throws Exception {
        final int numOfCerts = XMLStoreOps.countCertificates(xmlStorePath, false);

        ECertificate cert = ECertificate.readFromFile(certPath);
        XMLStoreOps.writeCertificate(xmlStorePath, cert);

        Assert.assertEquals(numOfCerts + 1, XMLStoreOps.countCertificates(xmlStorePath, false));
    }

    @Test
    public void test_03_01_DeleteCert() throws Exception {
        final int numOfCerts = XMLStoreOps.countCertificates(xmlStorePath, false);

        ECertificate cert = ECertificate.readFromFile(certPath);
        XMLStoreOps.deleteCertificate(xmlStorePath, cert, false);

        Assert.assertEquals(numOfCerts - 1, XMLStoreOps.countCertificates(xmlStorePath, false));
    }

    @Test
    public void test_03_02_DeleteRootCert() throws Exception {
        final int numOfCerts = XMLStoreOps.countCertificates(xmlStorePath, true);

        XMLStoreOps.deleteCertificate(xmlStorePath, new Random().nextInt(numOfCerts), true);

        Assert.assertEquals(numOfCerts - 1, XMLStoreOps.countCertificates(xmlStorePath, true));
    }
}
