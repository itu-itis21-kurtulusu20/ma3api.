using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.issuer
{
    /**
     * Checks whether the Issuer field of the crl and the Subject field of the
     * issuer certificate are the same. 
     */
    public class CRLNameChecker : CRLIssuerChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        
        /**
         * SİL'deki issuer ile İmzalayan Sertifikasının subject alanları eşleşiyor mu kontrol eder
         */
        protected override PathValidationResult _check(IssuerCheckParameters aIssuerCheckParameters,
                                              ECRL aCRL, ECertificate aIssuerCertificate,
                                              CRLStatusInfo aCRLStatusInfo)
        {
            EName issuer = aCRL.getIssuer();
            EName subject = aIssuerCertificate.getSubject();

            if (!issuer.Equals(subject))
            {
                logger.Error("SM sertifikası subject-sertifika issuer isimleri uyuşmuyor");
                aCRLStatusInfo.addDetail(this, NameCheckStatus.ISSUER_SUBJECT_MISMATCH, false);
                return PathValidationResult.CRL_NAME_CONTROL_FAILURE;
            }
            else
            {
                aCRLStatusInfo.addDetail(this, NameCheckStatus.ISSUER_SUBJECT_MATCH_OK, true);
                return PathValidationResult.SUCCESS;
            }
        }

        public override String getCheckText()
        {
            return Resource.message(Resource.SIL_NAME_KONTROLU);
        }
    }
}
