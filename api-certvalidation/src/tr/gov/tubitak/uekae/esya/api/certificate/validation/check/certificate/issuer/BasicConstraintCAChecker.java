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
 * Checks whether Basic Constraints extension information in the issuer
 * certificate has CA feature. 
 *
 * @author IH
 */
public class BasicConstraintCAChecker extends IssuerChecker {

    private static Logger logger = LoggerFactory.getLogger(BasicConstraintCAChecker.class);

    public enum BasicConstraintCACheckStatus implements CheckStatus {

        NO_BASIC_CONST_EXTENSION,
        INVALID_BASIC_CONST_EXTENSION,
        NO_BASIC_CONST_EXTENSION_CA,
        INVALID_BASIC_CONST_EXTENSION_CA,
        BASIC_CONST_EXTENSION_CA_OK;

        public String getText()
        {
            switch (this) {
                case NO_BASIC_CONST_EXTENSION:
                    return CertI18n.message(CertI18n.BASIC_CONST_EKLENTI_YOK);
                case INVALID_BASIC_CONST_EXTENSION:
                    return CertI18n.message(CertI18n.BASIC_CONST_EKLENTISI_BOZUK);
                case NO_BASIC_CONST_EXTENSION_CA:
                    return CertI18n.message(CertI18n.BASIC_CONST_EKLENTI_CA_DEGERI_YOK);
                case INVALID_BASIC_CONST_EXTENSION_CA:
                    return CertI18n.message(CertI18n.BASIC_CONST_EKLENTI_CA_DEGERI_YANLIS);
                case BASIC_CONST_EXTENSION_CA_OK:
                    return CertI18n.message(CertI18n.BASIC_CONST_EKLENTI_CA_DEGERI_DOGRU);

                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }
    }

    /**
     * Temel Kısıtlamalar Eklentisi ile ilgili kontrolleri yapar
     */
    protected PathValidationResult _check(IssuerCheckParameters aConstraintcheckParam,
                                          ECertificate aIssuerCertificate, ECertificate aCertificate,
                                          CertificateStatusInfo aCertStatusInfo)
    {
        EBasicConstraints bc = aIssuerCertificate.getExtensions().getBasicConstraints();
        if (bc == null) {
            logger.error("Sertifikada Basic Constraints uzantısı yok");
            aCertStatusInfo.addDetail(this, BasicConstraintCACheckStatus.NO_BASIC_CONST_EXTENSION, false);
            return PathValidationResult.BASICCONSTRAINTS_FAILURE;
        }
        if (bc.getObject().cA == null) {
            logger.error("BasicConstraints CA değeri yok");
            aCertStatusInfo.addDetail(this, BasicConstraintCACheckStatus.NO_BASIC_CONST_EXTENSION_CA, false);
            return PathValidationResult.BASICCONSTRAINTS_FAILURE;
        }
        if (bc.isCA()) {
            aCertStatusInfo.addDetail(this, BasicConstraintCACheckStatus.BASIC_CONST_EXTENSION_CA_OK, true);
            return PathValidationResult.SUCCESS;
        }
        else {
            logger.error("BasicConstraints CA geçersiz.");
            aCertStatusInfo.addDetail(this, BasicConstraintCACheckStatus.INVALID_BASIC_CONST_EXTENSION_CA, false);
            return PathValidationResult.BASICCONSTRAINTS_FAILURE;
        }
    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.BASIC_CONST_CA_KONTROLU);
    }
}


