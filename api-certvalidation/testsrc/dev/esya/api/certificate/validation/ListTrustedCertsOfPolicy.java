package dev.esya.api.certificate.validation;

import org.junit.Ignore;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;

import java.util.List;

/**
 * Created by orcun.ertugrul on 19-Feb-18.
 */
@Ignore("Development tests")
public class ListTrustedCertsOfPolicy {

    @Test
    public void listCerts() throws Exception{
        ValidationPolicy policy = PolicyReader.readValidationPolicy("T:\\api-parent\\resources\\sample-policy\\certval-policy.xml");

        ValidationSystem vs = CertificateValidation.createValidationSystem(policy);

        vs.getFindSystem().findTrustedCertificates();
        List<ECertificate> trustedCertificates = vs.getFindSystem().getTrustedCertificates();
        for (ECertificate aCert: trustedCertificates ) {
            System.out.println(aCert.toString());
        }
    }
}
