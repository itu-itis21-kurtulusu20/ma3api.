package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

/**
 * Checks the validity of the certificate according the constraints defined
 * by PolicyConstraints extension information. 
 */
public class PolicyConstraintChecker extends IssuerChecker {

    /**
     * SM Sertifikasının Politika Kısıtlamaları ile ilgili kontrollerini yapar
     */
    protected PathValidationResult _check(IssuerCheckParameters aIssuerCheckParameters,
                                          ECertificate aIssuerCertificate, ECertificate aCertificate,
                                          CertificateStatusInfo aCertStatusInfo)
    {
        /* NOT IMPLEMENTED YET */
        return PathValidationResult.SUCCESS;
    }

}
