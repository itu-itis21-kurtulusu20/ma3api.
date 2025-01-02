package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EKeyUsage;
import tr.gov.tubitak.uekae.esya.api.certificate.i18n.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;

/**
 * Checks whether the 'issuer certificate' of the certificate has 
 * CertificateSigning key usage.
 */
public class CertificateKeyUsageChecker extends IssuerChecker {

    private final static Logger logger = LoggerFactory.getLogger(CertificateKeyUsageChecker.class);

    public enum KeyUsageCheckStatus implements CheckStatus {

        NO_KEY_USAGE,
        INVALID_KEY_USAGE,
        KEY_USAGE_ISNOT_CERT_SIGN,
        KEY_USAGE_CERT_SIGN_OK,
        KEY_USAGE_CHECK_ENCRYPTION_OK;


        public String getText()
        {
            switch (this) {
                case NO_KEY_USAGE:
                    return CertI18n.message(CertI18n.KEY_USAGE_YOK);
                case INVALID_KEY_USAGE:
                    return CertI18n.message(CertI18n.KEY_USAGE_BOZUK);
                case KEY_USAGE_ISNOT_CERT_SIGN:
                    return CertI18n.message(CertI18n.KEY_USAGE_SERTIFIKA_IMZALAYICI_DEGIL);
                case KEY_USAGE_CERT_SIGN_OK:
                    return CertI18n.message(CertI18n.KEY_USAGE_SERTIFIKA_IMZALAYICI);
                case KEY_USAGE_CHECK_ENCRYPTION_OK:
                    return CertI18n.message(CertI18n.KEY_USAGE_SERTIFIKA_SIFRELEME);
                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }
    }


    /**
     * Ust SM sertifikası sertifika imzalayıcı özelliğe sahip olmalı
     */
    protected PathValidationResult _check(IssuerCheckParameters aConstraintCheckParam,
                                          ECertificate aIssuercertificate, ECertificate aCertificate,
                                          CertificateStatusInfo aCertStatusInfo)
    {
        //sertifika KeyUsage alalım
        EKeyUsage ku = aIssuercertificate.getExtensions().getKeyUsage();

        //int	res = ExtensionGenerator::getKeyUsageExtension( aUstSertifika.getTBSCertificate().getExtensions(),ku);
        if (ku == null) {
            logger.error("Sertifikada Key Usage yok");
            aCertStatusInfo.addDetail(this, KeyUsageCheckStatus.NO_KEY_USAGE, false);
            return PathValidationResult.KEYUSAGE_CONTROL_FAILURE;
        }

        if (ku.isKeyCertSign()) {
            aCertStatusInfo.addDetail(this, KeyUsageCheckStatus.KEY_USAGE_CERT_SIGN_OK, true);
            return PathValidationResult.SUCCESS;
        }
        else {
            logger.error("Sertifikada Key Usage cert sign değil");
            aCertStatusInfo.addDetail(this, KeyUsageCheckStatus.KEY_USAGE_ISNOT_CERT_SIGN, false);
            return PathValidationResult.KEYUSAGE_CONTROL_FAILURE;
        }
    }


    public String getCheckText()
    {
        return CertI18n.message(CertI18n.ISSUERCERT_KEY_USAGE_KONTROLU);
    }

}
