package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.Checker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * Base class for OCSP Response Checkers 
 *
 * @author IH
 */
public abstract class OCSPResponseChecker extends Checker {

    abstract PathValidationResult _check(EOCSPResponse aOCSPResponse,
                                         OCSPResponseStatusInfo aOCSPResponseInfo)
            throws ESYAException;

    /**
     * OCSP Cevabının geçerliliği ile ilgili kontrolları yapar.
     */
    public PathValidationResult check(OCSPResponseStatusInfo aOCSPResponseStatusInfo,
                                      EOCSPResponse aOCSPResponse)
            throws ESYAException
    {
        return _check(aOCSPResponse, aOCSPResponseStatusInfo);
    }

}
