using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer
{
    /**
     * Checks the validity of the certificate according to the constraints defined
     * in the Name Constraints extension information in the certificate.
     */
    public class NameConstraintChecker : IssuerChecker
    {
        /**
         * SM Sertifikanın isim kısıtlamaları ile ilgili kontrollerini yapar
         */
        protected override PathValidationResult _check(IssuerCheckParameters aIssuerCheckParameters,
                                              ECertificate aIssuerCertificate, ECertificate aCertificate,
                                              CertificateStatusInfo aCertificateStatusInfo)
        {
            /* todo NOT IMPLEMENTED YET */

            return PathValidationResult.SUCCESS;
        }
    }
}
