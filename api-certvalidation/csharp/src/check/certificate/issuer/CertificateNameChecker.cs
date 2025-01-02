using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer
{
    /**
     * Checks whether issuer field of the certificate and subject field of the
     * issuer certificate are the same. 
     */
    public class CertificateNameChecker : IssuerChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        
        /**
         * Sertifikanın issuer alanı ile SM Sertifikasının subject alanları eşleşiyor mu kontrol eder.
         */
        protected override PathValidationResult _check(IssuerCheckParameters aConstraintCheckParam,
                                               ECertificate aIssuerCertificate, ECertificate aCertificate,
                                               CertificateStatusInfo aCertStatusInfo)
        {

            EName issuer = aCertificate.getIssuer();
            if (issuer.stringValue().Length == 0)
            {
                logger.Error("Sertifikada issuer alanı yok");
                aCertStatusInfo.addDetail(this, NameCheckStatus.CERTIFICATE_NO_ISSUER, false);
                return PathValidationResult.NAME_CONTROL_FAILURE;
            }
            EName subject = aIssuerCertificate.getSubject();
            if (subject.stringValue().Length == 0)
            {
                logger.Error("Sertifikada subject alanı yok");
                aCertStatusInfo.addDetail(this, NameCheckStatus.ISSUERCERTIFICATE_NO_SUBJECT, false);
                return PathValidationResult.NAME_CONTROL_FAILURE;
            }
            if (!issuer.Equals(subject))
            {
                logger.Error("SM sertifikası subject-sertifika issuer isimleri uyuşmuyor: '" + issuer + "' - '" + subject + "'");
                aCertStatusInfo.addDetail(this, NameCheckStatus.ISSUER_SUBJECT_MISMATCH, false);
                return PathValidationResult.NAME_CONTROL_FAILURE;
            }
            else
            {
                aCertStatusInfo.addDetail(this, NameCheckStatus.ISSUER_SUBJECT_MATCH_OK, true);
                return PathValidationResult.SUCCESS;
            }

        }

        public override String getCheckText()
        {
            return Resource.message(Resource.SERTIFIKA_NAME_KONTROLU);
        }
    }
}
