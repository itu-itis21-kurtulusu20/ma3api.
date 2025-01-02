package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.issuer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EKeyUsage;
import tr.gov.tubitak.uekae.esya.api.certificate.i18n.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.IssuerCheckParameters;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;

/**
 * Checks whether crl issuer certificate has CRLSigning key usage. 
 */
public class CRLKeyUsageChecker extends CRLIssuerChecker {

    private static Logger logger = LoggerFactory.getLogger(CRLKeyUsageChecker.class);


    public enum KeyUsageCheckStatus implements CheckStatus {
        
        KEY_USAGE_NOT_EXISTS,
        KEY_USAGE_INVALID,
        KEY_USAGE_NOT_CRL_SIGN,
        KEY_USAGE_CRL_SIGN;

        public String getText()
        {
            switch (this) {
                case KEY_USAGE_NOT_EXISTS:
                    return CertI18n.message(CertI18n.KEY_USAGE_YOK);
                case KEY_USAGE_INVALID:
                    return CertI18n.message(CertI18n.KEY_USAGE_BOZUK);
                case KEY_USAGE_NOT_CRL_SIGN:
                    return CertI18n.message(CertI18n.KEY_USAGE_SIL_IMZALAYICI_DEGIL);
                case KEY_USAGE_CRL_SIGN:
                    return CertI18n.message(CertI18n.KEY_USAGE_SIL_IMZALAYICI);

                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }
    }

    /**
     * SİL imzalayan Sertifikasının S�L imzalama özelliği olup olmadığını kontrol eder.
     */
    protected PathValidationResult _check(IssuerCheckParameters aConstraint,
                                          ECRL aSil, ECertificate aIssuerCertificate,
                                          CRLStatusInfo aCRLStatusInfo)
    {
        EKeyUsage ku = aIssuerCertificate.getExtensions().getKeyUsage();

        try {
            if (ku == null) {
                logger.error("Sertifikada Key Usage yok");
                aCRLStatusInfo.addDetail(this, KeyUsageCheckStatus.KEY_USAGE_NOT_EXISTS, false);
                return PathValidationResult.CRL_KEYUSAGE_CONTROL_FAILURE;
            }
        }
        catch (Exception x) {
            logger.error("Sertifika Key Usage decode edilirken hata olustu", x);
            aCRLStatusInfo.addDetail(this, KeyUsageCheckStatus.KEY_USAGE_INVALID, false);
            return PathValidationResult.CRL_KEYUSAGE_CONTROL_FAILURE;
        }
        if (ku.isCRLSign()) {
            aCRLStatusInfo.addDetail(this, KeyUsageCheckStatus.KEY_USAGE_CRL_SIGN, true);
            return PathValidationResult.SUCCESS;
        }
        else {
            logger.error("CRL issuer'da Key usage crlSign set edilmemiş.");
            aCRLStatusInfo.addDetail(this, KeyUsageCheckStatus.KEY_USAGE_NOT_CRL_SIGN, false);
            return PathValidationResult.CRL_KEYUSAGE_CONTROL_FAILURE;
        }

    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.SIL_KEY_USAGE_KONTROLU);
    }
}
