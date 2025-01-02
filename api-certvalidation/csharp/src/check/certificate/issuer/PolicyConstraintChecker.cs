using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer
{
    /**
     * Checks the validity of the certificate according the constraints defined
     * by PolicyConstraints extension information. 
     */
    public class PolicyConstraintChecker : IssuerChecker
    {
        /**
          * SM Sertifikasının Politika Kısıtlamaları ile ilgili kontrollerini yapar
          */
        protected override PathValidationResult _check(IssuerCheckParameters aIssuerCheckParameters,
                                               ECertificate aIssuerCertificate, ECertificate aCertificate,
                                               CertificateStatusInfo aCertStatusInfo)
        {
            /* NOT IMPLEMENTED YET */
            return PathValidationResult.SUCCESS;
        }
    }
}
