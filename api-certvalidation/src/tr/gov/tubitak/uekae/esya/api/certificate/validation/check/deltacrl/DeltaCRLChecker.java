package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.deltacrl;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.self.CRLSelfChecker;

/**
 * Base class for delta-CRL checkers
 *
 * Delta-CRL checkers validates delta-CRL by checking different features of
 *  delta-CRLs specified in RFC 5280.
 *
 * @author IH
 */
public abstract class DeltaCRLChecker extends CRLSelfChecker
{
}
