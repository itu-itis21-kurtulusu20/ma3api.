using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self
{
    public class EncryptionCertificateKeyUsageChecker : CertificateSelfChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        protected override PathValidationResult _check(CertificateStatusInfo aCertStatusInfo)
        {
            logger.Debug("Sertifika key usage alanı şifreleme işlemi için uygun mu kontrolü yapılacak");

            ECertificate cert = aCertStatusInfo.getCertificate();


            if (cert.isCACertificate())
            {
                logger.Debug("CA certificate.");
                aCertStatusInfo.addDetail(this, CertificateKeyUsageChecker.KeyUsageCheckStatus.KEY_USAGE_CHECK_ENCRYPTION_OK, true);
                return PathValidationResult.SUCCESS;
            }
            if (cert.isOCSPSigningCertificate() == true)
            {
                logger.Debug("OCSP Signing certificate.");
                aCertStatusInfo.addDetail(this, CertificateKeyUsageChecker.KeyUsageCheckStatus.KEY_USAGE_CHECK_ENCRYPTION_OK, true);
                return PathValidationResult.SUCCESS;
            }
            if (cert.isTimeStampingCertificate() == true)
            {
                logger.Debug("TimeStamping certificate.");
                aCertStatusInfo.addDetail(this, CertificateKeyUsageChecker.KeyUsageCheckStatus.KEY_USAGE_CHECK_ENCRYPTION_OK, true);
                return PathValidationResult.SUCCESS;
            }

            EKeyUsage certKeyUsage = cert.getExtensions().getKeyUsage();

            if (certKeyUsage.isKeyEncipherment())
            {
                logger.Debug("Sertifika KeyEncipherment özelliği var");
                aCertStatusInfo.addDetail(this, CertificateKeyUsageChecker.KeyUsageCheckStatus.KEY_USAGE_CHECK_ENCRYPTION_OK, true);
                return PathValidationResult.SUCCESS;
            }
            else
            {
                logger.Debug("Sertifika KeyEncipherment özelliği yok, hata");
                aCertStatusInfo.addDetail(this, CertificateKeyUsageChecker.KeyUsageCheckStatus.INVALID_KEY_USAGE, false);
                return PathValidationResult.KEYUSAGE_CONTROL_FAILURE;
            }
        }

        public String getCheckText()
        {
            return Resource.message(Resource.ENCRYPTIONCERT_KEY_USAGE_KONTROLU);
        }
    }
}
