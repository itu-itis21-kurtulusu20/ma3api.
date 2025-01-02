package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CheckResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.DateCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;

import java.util.Calendar;

/**
 * Checks the validity of date information in the Certificate 
 */
public class CertificateDateChecker extends CertificateSelfChecker {

    private static Logger logger = LoggerFactory.getLogger(CertificateDateChecker.class);

    /**
     * Sertifikanin geçerlilik süresini kontrol eder.
     * 6.1.3 Basic Certificate Processing (a)
     * (2) The certificate validity period includes the current time.
     */
    protected PathValidationResult _check(CertificateStatusInfo aCertStatusInfo)
    {
        if (logger.isDebugEnabled())
            logger.debug("Sertifika tarihi kontrol edilecek");

        ECertificate cert = aCertStatusInfo.getCertificate();

        Calendar notBefore = cert.getNotBefore();
        Calendar notAfter = cert.getNotAfter();
        Calendar baseValidationTime = null;

        Calendar now = mParentSystem.getTimeProvider() !=null ? mParentSystem.getTimeProvider().getCurrentTime() : Calendar.getInstance();

        if (mParentSystem!=null)
            baseValidationTime = mParentSystem.getBaseValidationTime();
        if (baseValidationTime == null)
            baseValidationTime = now;

        try {
            if (baseValidationTime.after(notBefore) && baseValidationTime.before(notAfter)) {
                if (logger.isDebugEnabled())
                    logger.debug("Sertifika tarihi geçerli");
                aCertStatusInfo.addDetail(this, DateCheckStatus.VALID_DATE, true);
                return PathValidationResult.SUCCESS;
            }
            else 
            {
                logger.error("Sertifika tarihi geçerli değil " + baseValidationTime.getTime() + " ["+notBefore.getTime() + " - " + notAfter.getTime()+"]");
                if(baseValidationTime.after(notBefore))
                	aCertStatusInfo.addDetail(new CheckResult(getCheckText(),CertI18n.message(CertI18n.SERTIFIKA_SURESI_DOLMUS) ,DateCheckStatus.INVALID_DATE, false) );
                else
                	aCertStatusInfo.addDetail(this, DateCheckStatus.INVALID_DATE, false);
                return PathValidationResult.CERTIFICATE_EXPIRED;
            }
        }
        catch (Exception aEx) {
            logger.error("Sertifika geÇerlilik tarihi alanları alınamadı: " + aEx.getMessage(), aEx);
            aCertStatusInfo.addDetail(this, DateCheckStatus.CORRUPT_DATE_INFO, false);
        }
        return PathValidationResult.CERTIFICATE_EXPIRED;
    }


    public String getCheckText()
    {
        return CertI18n.message(CertI18n.SERTIFIKA_TARIH_KONTROLU);
    }

}
