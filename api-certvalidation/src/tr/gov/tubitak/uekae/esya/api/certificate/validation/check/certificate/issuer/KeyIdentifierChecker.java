package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAuthorityKeyIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectKeyIdentifier;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.asn.util.UtilEsitlikler;

/**
 * Checks if the SubjectKeyIdentifier extension of the CA certificate and
 * AuthorityKeyIdentifier extension of the certificate matches 
 */
public class KeyIdentifierChecker extends IssuerChecker {

    private static Logger logger = LoggerFactory.getLogger(KeyIdentifierChecker.class);

    public enum KeyIdentifierCheckStatus implements CheckStatus {

        NO_CERTIFICATE_AUTHORITY_KEY_IDENTIFIER,
        NO_ISSUER_CERTIFICATE_SUBJECT_KEY_IDENTIFIER,
        AUTHORITY_SUBJECT_KEY_IDENTIFIER_MISMATCH,
        AUTHORITY_SUBJECT_KEY_IDENTIFIER_MATCH_OK;

        public String getText()
        {
            switch (this) {
                case NO_CERTIFICATE_AUTHORITY_KEY_IDENTIFIER:
                    return CertI18n.message(CertI18n.SERTIFIKA_AUTHORITY_KEY_IDENTIFIER_YOK);
                case NO_ISSUER_CERTIFICATE_SUBJECT_KEY_IDENTIFIER:
                    return CertI18n.message(CertI18n.SMSERTIFIKA_SUBJECT_KEY_IDENTIFIER_YOK);
                case AUTHORITY_SUBJECT_KEY_IDENTIFIER_MISMATCH:
                    return CertI18n.message(CertI18n.AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMSUZ);
                case AUTHORITY_SUBJECT_KEY_IDENTIFIER_MATCH_OK:
                    return CertI18n.message(CertI18n.AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMLU);

                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }
    }

    /**
     * Sertifika Yetkili Anahtar Tanımlayıcısı ile SM sertifikası Anahtar tanımlayıcısı uyuşuyormu kontrol eder.
     */
    protected PathValidationResult _check(IssuerCheckParameters aIssuerCheckParameters,
                                          ECertificate aIssuerCertificate, ECertificate aCertificate,
                                          CertificateStatusInfo aCertStatusInfo)
    {
        //sertifika AuthorityKeyIdentifier alalım
        EAuthorityKeyIdentifier aki = aCertificate.getExtensions().getAuthorityKeyIdentifier();
        if (aki == null) {
            logger.error("Sertifikada AKI yok");
            aCertStatusInfo.addDetail(this, KeyIdentifierCheckStatus.NO_CERTIFICATE_AUTHORITY_KEY_IDENTIFIER, false);
            return PathValidationResult.KEYID_CONTROL_FAILURE;
        }

        //sm SubjectKeyIdentifier alalım
        ESubjectKeyIdentifier ski = aIssuerCertificate.getExtensions().getSubjectKeyIdentifier();
        if (ski == null) {
            logger.error("Sertifikada SKI yok");
            aCertStatusInfo.addDetail(this, KeyIdentifierCheckStatus.NO_ISSUER_CERTIFICATE_SUBJECT_KEY_IDENTIFIER, false);
            return PathValidationResult.KEYID_CONTROL_FAILURE;
        }

        if (!UtilEsitlikler.esitMi(aki.getKeyIdentifier(), ski.getValue())) {
            logger.error("Authority Key Identifier ve Subject Key Identifier değerleri aynı değil");
            aCertStatusInfo.addDetail(this, KeyIdentifierCheckStatus.AUTHORITY_SUBJECT_KEY_IDENTIFIER_MISMATCH, false);
            return PathValidationResult.KEYID_CONTROL_FAILURE;
        }
        else {
            if (logger.isDebugEnabled())
                logger.debug("Authority Key Identifier ve Subject Key Identifier değerleri aynı");
            aCertStatusInfo.addDetail(this, KeyIdentifierCheckStatus.AUTHORITY_SUBJECT_KEY_IDENTIFIER_MATCH_OK, true);
            return PathValidationResult.SUCCESS;
        }
    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.KEY_IDENTIFIER_KONTROLU);
    }

}
