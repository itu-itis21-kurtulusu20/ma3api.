namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self
{
    /**
     * Base class for certificate self checkers.
     *
     * <p>Certificate Self checkers perform the controls about the structure and the 
     * format of the certificate itself.
     */
    public abstract class CertificateSelfChecker : Checker
    {
        protected abstract PathValidationResult _check(CertificateStatusInfo aCertStatusInfo);

        /**
         * Tek Sertifika Kontrollerini (Sertifika yapısı ile ilgili kontroller) gerçekleştirir
         */
        public PathValidationResult check(CertificateStatusInfo aCertStatusInfo)
        {
            return _check(aCertStatusInfo);
        }
    }
}
