using tr.gov.tubitak.uekae.esya.api.asn.ocsp;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp
{
    /**
     * Base class for OCSP Response Checkers 
     *
     * @author IH
     */
    public abstract class OCSPResponseChecker : Checker
    {
       protected abstract PathValidationResult _check(EOCSPResponse aOCSPResponse,
                                         OCSPResponseStatusInfo aOCSPResponseInfo);

        /**
         * OCSP Cevabının geçerliliği ile ilgili kontrolları yapar.
         */
        public PathValidationResult check(OCSPResponseStatusInfo aOCSPResponseStatusInfo,
                                          EOCSPResponse aOCSPResponse)
        {
            return _check(aOCSPResponse, aOCSPResponseStatusInfo);
        }
    }
}
