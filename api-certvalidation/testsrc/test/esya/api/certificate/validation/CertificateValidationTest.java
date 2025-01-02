/**
 * Created by orcun.ertugrul on 11-Oct-17.
 */
package test.esya.api.certificate.validation;

import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateController;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;

import java.io.File;

import static junit.framework.TestCase.assertEquals;

public class CertificateValidationTest {

    @Test
    public void testValidateCert() throws Exception {
        ECertificate cert = ECertificate.readFromFile("T:\\api-certvalidation\\testdata\\QCA11.crt");

        ValidationPolicy policy = PolicyReader.readValidationPolicy("T:\\api-test\\config\\policy\\testsuite-policy.xml");
        ValidationSystem validationSystem = CertificateValidation.createValidationSystem(policy);

        CertificateController checker = new CertificateController();
        CertificateStatusInfo csi = checker.check(validationSystem, cert);

        assertEquals(csi.getCertificateStatus(), CertificateStatus.VALID);
    }
}
