package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

/**
 * Checks the validity of the certificate according to the constraints defined
 * in the Name Constraints extension information in the certificate.
 */
public class NameConstraintChecker extends IssuerChecker {

    /**
     * SM Sertifikanın isim kısıtlamaları ile ilgili kontrollerini yapar
     */
    protected PathValidationResult _check(IssuerCheckParameters aIssuerCheckParameters,
                                          ECertificate aIssuerCertificate, ECertificate aCertificate,
                                          CertificateStatusInfo aCertificateStatusInfo)
    {
        /* todo NOT IMPLEMENTED YET */

        return PathValidationResult.SUCCESS;
    }

}
