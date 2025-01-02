package test.esya.api.certificate.validation.trustedCerts;

import org.junit.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;

import java.util.List;

public class TrustedCertTest {

    @Test
    public void TRCertStoreWithSVTFinderTest() throws Exception { // Kıbrıs farklı bir jar ile test edilmeli.
        ValidationPolicy policy = PolicyReader.readValidationPolicy("T:\\api-certvalidation\\testdata\\trustedCerts\\tr-certval-policy.xml");
        ValidationSystem vs = CertificateValidation.createValidationSystem(policy);

        vs.getFindSystem().findTrustedCertificates();
        List<ECertificate> trustedCertificates = vs.getFindSystem().getTrustedCertificates();

        Assert.assertEquals(27, trustedCertificates.size());
    }

    @Test
    public void TBTKCertStoreWithXMLFinderTest() throws Exception {
        ValidationPolicy policy = PolicyReader.readValidationPolicy("T:\\api-certvalidation\\testdata\\trustedCerts\\tbtk-certval-policy-xmlFinder.xml");
        ValidationSystem vs = CertificateValidation.createValidationSystem(policy);

        vs.getFindSystem().findTrustedCertificates();
        List<ECertificate> trustedCertificates = vs.getFindSystem().getTrustedCertificates();

        Assert.assertEquals(1, trustedCertificates.size());
    }

    @Test
    public void TBTKCertStoreWithSVTFinderTest() throws Exception {
        ValidationPolicy policy = PolicyReader.readValidationPolicy("T:\\api-certvalidation\\testdata\\trustedCerts\\tbtk-certval-policy-svtFinder.xml");
        ValidationSystem vs = CertificateValidation.createValidationSystem(policy);

        vs.getFindSystem().findTrustedCertificates();
        List<ECertificate> trustedCertificates = vs.getFindSystem().getTrustedCertificates();

        Assert.assertEquals(1, trustedCertificates.size());
    }
}
