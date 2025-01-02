package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtensions;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;

/**
 * Checks if the version information in the certificate is valid 
 */
public class VersionChecker extends CertificateSelfChecker {

    private static Logger logger = LoggerFactory.getLogger(VersionChecker.class);

    public class EXP_Version {

        public static final long v1 = 0;
        public static final long v2 = 1;
        public static final long v3 = 2;
    }

    public enum VersionCheckStatus implements CheckStatus {
        EXTENSION_EXISTS_VERSION_VALID,
        EXTENSION_EXISTS_VERSION_INVALID,
        UID_EXISTS_VERSION_VALID,
        UID_EXISTS_VERSION_INVALID,
        BASIC_FIELDS_EXISTS_VERSION_VALID,
        BASIC_FIELDS_EXISTS_VERSION_INVALID;

        public String getText()
        {
            switch (this) {
                case EXTENSION_EXISTS_VERSION_VALID:
                    return CertI18n.message(CertI18n.EXTENSION_VAR_DOGRU);
                case EXTENSION_EXISTS_VERSION_INVALID:
                    return CertI18n.message(CertI18n.EXTENSION_VAR_YANLIS);
                case UID_EXISTS_VERSION_VALID:
                    return CertI18n.message(CertI18n.UID_VAR_DOGRU);
                case UID_EXISTS_VERSION_INVALID:
                    return CertI18n.message(CertI18n.UID_VAR_YANLIS);
                case BASIC_FIELDS_EXISTS_VERSION_VALID:
                    return CertI18n.message(CertI18n.BASIT_ALANLAR_VAR_DOGRU);
                case BASIC_FIELDS_EXISTS_VERSION_INVALID:
                    return CertI18n.message(CertI18n.BASIT_ALANLAR_VAR_YANLIS);

                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }

    }


    /**
     * Sertifikanın versiyon bilgisini kontrol eder.
     * 4.1.2.1 Version
     */
    protected PathValidationResult _check(CertificateStatusInfo aCertStatusInfo)
    {
        ECertificate cert = aCertStatusInfo.getCertificate();
        if (logger.isDebugEnabled()) logger.debug("Versiyon kontrolü yapılacak.");
        long version = cert.getVersion();
        // When extensions are used, as expected in this profile, version MUST be 3 (value is 2).
        EExtensions extensions = cert.getExtensions();
        if (extensions.getExtensionCount() > 0) {
            if (version == EXP_Version.v3) {
                if (logger.isDebugEnabled())
                    logger.debug("Sertifikada extension var, version bilgisi doğru: " + version);
                aCertStatusInfo.addDetail(this, VersionCheckStatus.EXTENSION_EXISTS_VERSION_VALID, true);
                return PathValidationResult.SUCCESS;
            }
            logger.error("Sertifikada extension var, version bilgisi yanlış: " + version);
            aCertStatusInfo.addDetail(this, VersionCheckStatus.EXTENSION_EXISTS_VERSION_INVALID, false);
            return PathValidationResult.VERSION_CONTROL_FAILURE;
        }
        // If no extensions are present, but a UniqueIdentifier is present, the version SHOULD be 2 (value is 1); however version MAY be 3.
        if ((cert.getObject().tbsCertificate.issuerUniqueID.value.length > 0) || (cert.getObject().tbsCertificate.subjectUniqueID.value.length > 0)) {
            if ((version == EXP_Version.v2) || (version == EXP_Version.v3)) {
                if (logger.isDebugEnabled())
                    logger.debug("Sertifikada UniqueIdentifier var, version bilgisi doğru: " + version);
                aCertStatusInfo.addDetail(this, VersionCheckStatus.UID_EXISTS_VERSION_VALID, true);
                return PathValidationResult.SUCCESS;
            }
            logger.error("Sertifikada UniqueIdentifier var, version bilgisi yanlış: " + version);
            aCertStatusInfo.addDetail(this, VersionCheckStatus.UID_EXISTS_VERSION_INVALID, false);
            return PathValidationResult.VERSION_CONTROL_FAILURE;
        }
        // If only basic fields are present, the version SHOULD be 1 (the value is omitted from the certificate as the default value); however the version MAY be 2 or 3.
        if ((version == EXP_Version.v1) || (version == EXP_Version.v2) || (version == EXP_Version.v3)) {
            if (logger.isDebugEnabled())
                logger.debug("Sertifikada sadece basit alanlar var, version bilgisi doğru: " + version);
            aCertStatusInfo.addDetail(this, VersionCheckStatus.BASIC_FIELDS_EXISTS_VERSION_VALID, true);
            return PathValidationResult.SUCCESS;
        }
        logger.error("Sertifikada basit alanlar var, version bilgisi yanlış: " + version);
        aCertStatusInfo.addDetail(this, VersionCheckStatus.BASIC_FIELDS_EXISTS_VERSION_INVALID, false);
        return PathValidationResult.VERSION_CONTROL_FAILURE;
    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.VERSION);
    }

}
