using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self
{
    /**
     * Checks the signature of self-signed certificates
     */
    public class SelfSignatureChecker : CertificateSelfChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        protected override PathValidationResult _check(CertificateStatusInfo aCertStatusInfo) {

            logger.Debug("Self-Signature kontrolu yapilacak.");

            ECertificate cert = aCertStatusInfo.getCertificate();

            // Kok sertifika degilse imza kontrolu burada yapilmayacak
            if(!cert.isSelfIssued())
                return PathValidationResult.SUCCESS;

            PathValidationResult pvr = CertificateSignatureChecker.checkSignature(cert,cert, new IssuerCheckParameters(),aCertStatusInfo);

            if(pvr == PathValidationResult.SUCCESS)
                logger.Debug("Kok sertifika imzasi gecerli");
            else
                logger.Error("Sertifika imzasi gecersiz: Subject " + cert.getTBSCertificate().getSubject().stringValue());

            return pvr;
        }
    }
}
