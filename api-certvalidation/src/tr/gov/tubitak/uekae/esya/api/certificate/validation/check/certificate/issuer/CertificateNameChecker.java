package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.certificate.i18n.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.NameCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;

/**
 * Checks whether issuer field of the certificate and subject field of the
 * issuer certificate are the same. 
 */
public class CertificateNameChecker extends IssuerChecker {

    private static Logger logger = LoggerFactory.getLogger(CertificateNameChecker.class);

    /**
     * Sertifikanın issuer alanı ile SM Sertifikasının subject alanları eşleşiyor mu kontrol eder.
     */
    protected PathValidationResult _check(IssuerCheckParameters aConstraintCheckParam,
                                          ECertificate aIssuerCertificate, ECertificate aCertificate,
                                          CertificateStatusInfo aCertStatusInfo)
    {

        EName issuer = aCertificate.getIssuer();
        if (issuer.stringValue().length() == 0) {
            logger.error("Sertifikada issuer alanı yok");
            aCertStatusInfo.addDetail(this, NameCheckStatus.CERTIFICATE_NO_ISSUER, false);
            return PathValidationResult.NAME_CONTROL_FAILURE;
        }
        EName subject = aIssuerCertificate.getSubject();
        if (subject.stringValue().length() == 0) {
            logger.error("Sertifikada subject alanı yok");
            aCertStatusInfo.addDetail(this, NameCheckStatus.ISSUERCERTIFICATE_NO_SUBJECT, false);
            return PathValidationResult.NAME_CONTROL_FAILURE;
        }
        if (!issuer.equals(subject)) {
            logger.error("SM sertifikası subject-sertifika issuer isimleri uyuşmuyor: '"+issuer+"' - '"+subject+"'");
            aCertStatusInfo.addDetail(this, NameCheckStatus.ISSUER_SUBJECT_MISMATCH, false);
            return PathValidationResult.NAME_CONTROL_FAILURE;
        }
        else {
            aCertStatusInfo.addDetail(this, NameCheckStatus.ISSUER_SUBJECT_MATCH_OK, true);
            return PathValidationResult.SUCCESS;
        }

    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.SERTIFIKA_NAME_KONTROLU);
    }

}
