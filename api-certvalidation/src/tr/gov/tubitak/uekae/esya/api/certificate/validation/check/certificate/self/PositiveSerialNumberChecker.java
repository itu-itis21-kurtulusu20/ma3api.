package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;

import java.math.BigInteger;

/**
 * Checks if the serial number of the certificate is positive. 
 */
public class PositiveSerialNumberChecker extends CertificateSelfChecker {

    private static final Logger logger = LoggerFactory.getLogger(PositiveSerialNumberChecker.class);

    public enum SerialNumberPositiveCheckStatus implements CheckStatus {

        SERIAL_NO_POSITIVE,
        SERIAL_NO_NEGATIVE;

        public String getText()
        {
            switch (this) {
                case SERIAL_NO_POSITIVE:
                    return CertI18n.message(CertI18n.SERI_NO_POZITIF);
                case SERIAL_NO_NEGATIVE:
                    return CertI18n.message(CertI18n.SERI_NO_NEGATIF);

                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }
    }

    /**
     * Sertifikanın seri numarasını kontrol eder.
     * 4.1.2.2 Serial number MUST be a positive integer
     */
    protected PathValidationResult _check(CertificateStatusInfo aCertStatusInfo)
    {
        logger.debug("Seri numara kontrolü yapılacak.");

        ECertificate certificate = aCertStatusInfo.getCertificate();
        BigInteger serialNumber = certificate.getSerialNumber();

        if (serialNumber.compareTo(BigInteger.ZERO) >= 0) {
            if (logger.isDebugEnabled())
                logger.debug("Sertifika seri numarası pozitif tam sayı: " + serialNumber);
            aCertStatusInfo.addDetail(this, SerialNumberPositiveCheckStatus.SERIAL_NO_POSITIVE, true);
            return PathValidationResult.SUCCESS;
        }
        logger.error("Sertifika seri numarası negatif tam sayı: " + serialNumber);
        aCertStatusInfo.addDetail(this, SerialNumberPositiveCheckStatus.SERIAL_NO_NEGATIVE, false);

        return PathValidationResult.SERIALNUMBER_NOT_POSITIVE;
    }


    public String getCheckText()
    {
        return CertI18n.message(CertI18n.SERI_NO_KONTROLU);
    }

}
