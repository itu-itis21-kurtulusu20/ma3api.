package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EBasicConstraints;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;

/**
 * Checks if the following condition is satisfied:
 *
 * <p>4.2.1.10 Basic Constraints pathLenConstraint field
 * it gives the maximum number of CA certificates that may
 * follow this certificate in a certification path. A value of zero
 * indicates that only an end-entity certificate may follow in the path.
 * Where it appears, the pathLenConstraint field MUST be greater than or
 * equal to zero. Where pathLenConstraint does not appear, there is no
 * limit to the allowed length of the certification path.
 *
 * @author IH
 */
public class PathLenConstraintChecker extends IssuerChecker {

    private static final Logger logger = LoggerFactory.getLogger(PathLenConstraintChecker.class);

    public enum PathLenConstraintCheckStatus implements CheckStatus {

        BASIC_CONST_EXTENSION_NOT_EXIST,
        BASIC_CONST_EXTENSION_LEN_CONS_VALUE_NOT_EXISTS,
        BASIC_CONST_EXTENSION_LEN_CONS_VALUE_NEGATIVE,
        BASIC_CONST_EXTENSION_LEN_CONS_VALUE_EXCEEDED,
        BASIC_CONST_EXTENSION_LEN_CONS_VALUE_VALID;

        public String getText()
        {
            switch (this) {
                case BASIC_CONST_EXTENSION_NOT_EXIST:
                    return CertI18n.message(CertI18n.BASIC_CONST_EKLENTI_YOK);
                case BASIC_CONST_EXTENSION_LEN_CONS_VALUE_NOT_EXISTS:
                    return CertI18n.message(CertI18n.BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_YOK);
                case BASIC_CONST_EXTENSION_LEN_CONS_VALUE_NEGATIVE:
                    return CertI18n.message(CertI18n.BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_NEGATIF);
                case BASIC_CONST_EXTENSION_LEN_CONS_VALUE_EXCEEDED:
                    return CertI18n.message(CertI18n.BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_ASILDI);
                case BASIC_CONST_EXTENSION_LEN_CONS_VALUE_VALID:
                    return CertI18n.message(CertI18n.BASIC_CONST_EKLENTI_LEN_CONS_DEGERI_GECERLI);

                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }
    }

    /**
     * SM Sertifikasının Yol Uzunluğu Kısıtlamaları ile ilgili kontrolleri yapar
     */
    protected PathValidationResult _check(IssuerCheckParameters aIssuerCheckParameters,
                                          ECertificate aIssuerCertificate, ECertificate aCertificate,
                                          CertificateStatusInfo aCertStatusInfo)
    {
        EBasicConstraints bc = aIssuerCertificate.getExtensions().getBasicConstraints();
        if (bc == null) {
            logger.error("Sertifikada Basic Constraints uzantısı yok");
            aCertStatusInfo.addDetail(this, PathLenConstraintCheckStatus.BASIC_CONST_EXTENSION_NOT_EXIST, false);
            return PathValidationResult.PATHLENCONSTRAINTS_FAILURE;
        }

        if (bc.getPathLenConstraint() == null) {
            logger.debug("BasicConstraints path length constraint değeri yok");
            aCertStatusInfo.addDetail(this, PathLenConstraintCheckStatus.BASIC_CONST_EXTENSION_LEN_CONS_VALUE_NOT_EXISTS, true);
            return PathValidationResult.SUCCESS;
        }

        long pathLength = bc.getPathLenConstraint();
        if (pathLength < 0) {
            logger.debug("BasicConstraints path length constraint değeri negatif");
            aCertStatusInfo.addDetail(this, PathLenConstraintCheckStatus.BASIC_CONST_EXTENSION_LEN_CONS_VALUE_NEGATIVE, true);
            return PathValidationResult.SUCCESS;
        }
        long kacinciSertifika = aIssuerCheckParameters.getCertificateOrder();
        if (kacinciSertifika > pathLength) {
            logger.error("BasicConstraints path length constraint değeri aşıldı:" + kacinciSertifika + ">" + pathLength);
            aCertStatusInfo.addDetail(this, PathLenConstraintCheckStatus.BASIC_CONST_EXTENSION_LEN_CONS_VALUE_EXCEEDED, false);
            return PathValidationResult.PATHLENCONSTRAINTS_FAILURE;
        }
        else {
            logger.debug("BasicConstraints path length constraint değeri geçerli:" + kacinciSertifika + "<=" + pathLength);
            aCertStatusInfo.addDetail(this, PathLenConstraintCheckStatus.BASIC_CONST_EXTENSION_LEN_CONS_VALUE_VALID, true);
            return PathValidationResult.SUCCESS;
        }

    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.PATH_LEN_CONSTRAINT_KONTROLU);
    }

}
