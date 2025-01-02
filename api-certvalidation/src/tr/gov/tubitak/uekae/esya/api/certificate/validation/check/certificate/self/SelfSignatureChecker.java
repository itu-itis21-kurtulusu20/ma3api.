package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.CertificateSignatureChecker;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.IssuerCheckParameters;

/**
 * Checks the signature of self-signed certificates
 */
public class SelfSignatureChecker extends CertificateSelfChecker {

    private static Logger logger = LoggerFactory.getLogger(SelfSignatureChecker.class);

    protected PathValidationResult _check(CertificateStatusInfo aCertStatusInfo) {

        logger.debug("Self-Signature kontrolu yapilacak.");

        ECertificate cert = aCertStatusInfo.getCertificate();

        // Kok sertifika degilse imza kontrolu burada yapilmayacak
        if(!cert.isSelfIssued())
            return PathValidationResult.SUCCESS;

        PathValidationResult pvr = CertificateSignatureChecker.checkSignature(cert,cert, new IssuerCheckParameters(),aCertStatusInfo);

        if(pvr == PathValidationResult.SUCCESS)
            logger.debug("Kok sertifika imzasi gecerli");
        else
            logger.error("Sertifika imzasi gecersiz: Subject " + cert.getTBSCertificate().getSubject().stringValue());

        return pvr;
    }
}
