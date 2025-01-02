package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.Checker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

/**
 * Base class for issuer checkers.
 *
 * <p>Issuer Checkers validates the contsraint about the certificate coming from
 * its issuer certificate.
 * 
 * @author IH
 */
public abstract class IssuerChecker extends Checker {

    public IssuerChecker()
    {
        super();
    }

    protected abstract PathValidationResult _check(IssuerCheckParameters aConstraint,
                                                   ECertificate aIssuerCertificate,
                                                   ECertificate aCertificate,
                                                   CertificateStatusInfo aCertStatusInfo);

    /**
     * Sertifikanın sertifika zinciri ile ilgili kontrollerini yapar
     */
    public PathValidationResult check(IssuerCheckParameters aConstraint,
                                      ECertificate aIssuerCertificate,
                                      ECertificate aCertificate,
                                      CertificateStatusInfo aCertStatusInfo)
    {
        return _check(aConstraint, aIssuerCertificate, aCertificate, aCertStatusInfo);
    }
}
