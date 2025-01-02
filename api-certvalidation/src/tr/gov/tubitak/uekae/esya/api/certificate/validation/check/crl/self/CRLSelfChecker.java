package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.self;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.Checker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;

/**
 * <p>Base class for CRL self checkers.
 *
 * <p>CRL Self checkers perform the controls about the structure and the format
 * of the crl itself.
 *
 * @author IH
 */
public abstract class CRLSelfChecker extends Checker {

    public CRLSelfChecker()
    {
        super();
    }

    protected abstract PathValidationResult _check(ECRL aCRL, CRLStatusInfo aCRLStatusInfo);

    public PathValidationResult check(ECRL aCRL, CRLStatusInfo aCRLStatusInfo)
    {
        return _check(aCRL, aCRLStatusInfo);
    }

}

