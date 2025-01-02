using NUnit.Framework;
using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation //.src.test.esya.api.certvalidation
{
    class ListTrustedCertsOfPolicy
    {
        [Test]
        public void listCerts()
        {
            ValidationPolicy policy = PolicyReader.readValidationPolicy("T:\\api-parent\\resources\\sample-policy\\certval-policy.xml");

            ValidationSystem vs = CertificateValidation.createValidationSystem(policy);

            vs.getFindSystem().findTrustedCertificates();
            List<ECertificate> trustedCertificates = vs.getFindSystem().getTrustedCertificates();
            foreach (ECertificate aCert in trustedCertificates)
            {
                Console.WriteLine(aCert.ToString());
            }
        }
    }
}
