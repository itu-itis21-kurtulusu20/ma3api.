package dev.esya.api.cmssignature.performance;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.CertificateFinderFromHTTP;

import java.util.List;

public class CertificateDownloadPerformanceTest {

    @Test
    public void testCertificateFinderFromHTTP() throws Exception{

        long REQ_COUNT = 10;
        long total = 0;

        ECertificate certicate = ECertificate.readFromFile("T:\\api-parent\\resources\\unit-test-resources\\certificate\\QCA1_2.crt");

        for(int i=0; i < REQ_COUNT; i++) {
            long start = System.currentTimeMillis();

            CertificateFinderFromHTTP certificateFinderFromHTTP = new CertificateFinderFromHTTP();

            List<ECertificate> certificates = certificateFinderFromHTTP.findCertificate();

            long end = System.currentTimeMillis();

            total = total + end - start;
        }

        System.out.println("Average: " + total / REQ_COUNT);
    }

}
