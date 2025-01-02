package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EKeyUsage;
import tr.gov.tubitak.uekae.esya.api.certificate.i18n.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.CertificateKeyUsageChecker;

/**
 * Created by orcun.ertugrul on 27-Jul-17.
 */
public class EncryptionCertificateKeyUsageChecker extends CertificateSelfChecker

{
    private static Logger logger = LoggerFactory.getLogger(EncryptionCertificateKeyUsageChecker.class);

    protected PathValidationResult _check(CertificateStatusInfo aCertStatusInfo) {

        logger.debug("Sertifika key usage alanı şifreleme işlemi için uygun mu kontrolü yapılacak");

        ECertificate cert = aCertStatusInfo.getCertificate();

        if(cert.isCACertificate())
        {
            logger.debug("CA certificate.");
            aCertStatusInfo.addDetail(this, CertificateKeyUsageChecker.KeyUsageCheckStatus.KEY_USAGE_CHECK_ENCRYPTION_OK, true);
            return PathValidationResult.SUCCESS;
        }
        if(cert.isOCSPSigningCertificate())
        {
            logger.debug("OCSP Signing certificate.");
            aCertStatusInfo.addDetail(this, CertificateKeyUsageChecker.KeyUsageCheckStatus.KEY_USAGE_CHECK_ENCRYPTION_OK, true);
            return PathValidationResult.SUCCESS;
        }
        if(cert.isTimeStampingCertificate())
        {
            logger.debug("TimeStamping certificate.");
            aCertStatusInfo.addDetail(this, CertificateKeyUsageChecker.KeyUsageCheckStatus.KEY_USAGE_CHECK_ENCRYPTION_OK, true);
            return PathValidationResult.SUCCESS;
        }




        EKeyUsage certKeyUsage = cert.getExtensions().getKeyUsage();

        if(certKeyUsage.isKeyEncipherment())
        {
            logger.debug("Sertifika KeyEncipherment özelliği var");
            aCertStatusInfo.addDetail(this, CertificateKeyUsageChecker.KeyUsageCheckStatus.KEY_USAGE_CHECK_ENCRYPTION_OK, true);
            return PathValidationResult.SUCCESS;
        }
        else
        {
            logger.debug("Sertifika KeyEncipherment özelliği yok, hata");
            aCertStatusInfo.addDetail(this, CertificateKeyUsageChecker.KeyUsageCheckStatus.INVALID_KEY_USAGE, false);
            return PathValidationResult.KEYUSAGE_CONTROL_FAILURE;
        }
    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.ENCRYPTIONCERT_KEY_USAGE_KONTROLU);
    }
}
