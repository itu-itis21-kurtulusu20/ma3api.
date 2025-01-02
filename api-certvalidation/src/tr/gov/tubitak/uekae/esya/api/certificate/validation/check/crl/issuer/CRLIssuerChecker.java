package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.issuer;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.Checker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.IssuerCheckParameters;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

/**
 * Base class for crl issuer checkers.
 *
 * CRL Issuer Checkers perform controls about the features of the crl related
 * with its issuer.
 *
 * @author IH
 */
public abstract class CRLIssuerChecker extends Checker {

    protected abstract PathValidationResult _check(IssuerCheckParameters aIssuerCheckParameters,
                                                   ECRL aCRL,
                                                   ECertificate aIssuerCertificate,
                                                   CRLStatusInfo aCRLStatusInfo);

    /**
     * SİL'in imzalayan sertifikası ile ilgili kontrollerini yapar
     */
    public PathValidationResult check(IssuerCheckParameters aIssuerCheckParameters,
                                      ECRL aCRL,
                                      ECertificate aIssuerCertificate,
                                      CRLStatusInfo aCRLStatusInfo)
    {
        return _check(aIssuerCheckParameters, aCRL, aIssuerCertificate, aCRLStatusInfo);
    }

}
