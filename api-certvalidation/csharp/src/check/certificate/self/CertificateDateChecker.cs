using System;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self
{
    /**
     * Checks the validity of date information in the Certificate 
     */
    public class CertificateDateChecker : CertificateSelfChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        
        /**
         * Sertifikanin geçerlilik süresini kontrol eder.
         * 6.1.3 Basic Certificate Processing (a)
         * (2) The certificate validity period includes the current time.
         */
        protected override PathValidationResult _check(CertificateStatusInfo aCertStatusInfo)
        {
            if (logger.IsDebugEnabled)
                logger.Debug("Sertifika tarihi kontrol edilecek");
            
            ECertificate cert = aCertStatusInfo.getCertificate();

            DateTime? notBefore = cert.getNotBefore();
            DateTime? notAfter = cert.getNotAfter();
            DateTime? baseValidationTime = null;

            DateTime? now = mParentSystem.getTimeProvider() != null ? mParentSystem.getTimeProvider().getCurrentTime() : DateTime.UtcNow;

            if (mParentSystem != null)
                baseValidationTime = mParentSystem.getBaseValidationTime();

            if (baseValidationTime == null)
                baseValidationTime = DateTime.UtcNow;

            try
            {
                if (baseValidationTime.Value.ToUniversalTime()/*.after*/> (notBefore.Value.ToUniversalTime()) && baseValidationTime.Value.ToUniversalTime()/*.before*/< (notAfter.Value.ToUniversalTime()))
                {
                    if (logger.IsDebugEnabled)
                        logger.Debug("Sertifika tarihi gecerli");
                    aCertStatusInfo.addDetail(this, DateCheckStatus.VALID_DATE, true);
                    return PathValidationResult.SUCCESS;
                }
                else
                {
                    //logger.Error("Kontrol Zamani " + baseValidationTime.Value.ToLocalTime() + ", Sertifika tarihi gecerli degil " + notBefore.Value.ToLocalTime() + " - " + notAfter.Value.ToLocalTime() /*+ Environment.NewLine*/ + cert.ToString());
                    //aCertStatusInfo.addDetail(this, DateCheckStatus.INVALID_DATE, false);
                    //return PathValidationResult.CERTIFICATE_EXPIRED;

                    logger.Error("Kontrol Zamani " + baseValidationTime.Value.ToLocalTime() + "Sertifika tarihi gecerli degil " + baseValidationTime.Value.ToLocalTime() + " [" + notBefore.Value.ToLocalTime() + " - " + notAfter.Value.ToLocalTime() + "]");
                    if (baseValidationTime > notBefore)
                        aCertStatusInfo.addDetail(new CheckResult(getCheckText(), Resource.message(Resource.SERTIFIKA_SURESI_DOLMUS), DateCheckStatus.INVALID_DATE, false));
                    else
                        aCertStatusInfo.addDetail(this, DateCheckStatus.INVALID_DATE, false);
                    return PathValidationResult.CERTIFICATE_EXPIRED;
                }
            }
            catch (Exception aEx)
            {
                logger.Error("Sertifika gecerlilik tarihi alanlari alinamadi: " + aEx.Message, aEx);
                aCertStatusInfo.addDetail(this, DateCheckStatus.CORRUPT_DATE_INFO, false);
            }
            return PathValidationResult.CERTIFICATE_EXPIRED;
        }


        public override String getCheckText()
        {
            return Resource.message(Resource.SERTIFIKA_TARIH_KONTROLU);
        }
    }
}
