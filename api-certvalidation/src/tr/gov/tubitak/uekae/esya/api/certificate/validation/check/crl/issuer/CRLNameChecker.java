package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.issuer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.NameCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.IssuerCheckParameters;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;

/**
 * Checks whether the Issuer field of the crl and the Subject field of the
 * issuer certificate are the same. 
 */
public class CRLNameChecker extends CRLIssuerChecker {

    private static Logger logger = LoggerFactory.getLogger(CRLNameChecker.class);

    /**
     * SİL'deki issuer ile İmzalayan Sertifikasının subject alanları eşleşiyor mu kontrol eder
     */
    protected PathValidationResult _check(IssuerCheckParameters aIssuerCheckParameters,
                                          ECRL aCRL, ECertificate aIssuerCertificate,
                                          CRLStatusInfo aCRLStatusInfo)
    {
        EName issuer = aCRL.getIssuer();
        EName subject = aIssuerCertificate.getSubject();

        if (!issuer.equals(subject)) {
            logger.error("SM sertifikası subject-sertifika issuer isimleri uyuşmuyor");
            aCRLStatusInfo.addDetail(this, NameCheckStatus.ISSUER_SUBJECT_MISMATCH, false);
            return PathValidationResult.CRL_NAME_CONTROL_FAILURE;
        }
        else {
            aCRLStatusInfo.addDetail(this, NameCheckStatus.ISSUER_SUBJECT_MATCH_OK, true);
            return PathValidationResult.SUCCESS;
        }
    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.SIL_NAME_KONTROLU);
    }
}
