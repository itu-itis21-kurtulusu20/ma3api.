package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;

/**
 * Checks if the signature Algorithms specified in the certificate's signature
 * field and in the signatureAlgorithm field are equal. 
 */
public class SignatureAlgConsistencyChecker extends CertificateSelfChecker {

    private static Logger logger = LoggerFactory.getLogger(SignatureAlgConsistencyChecker.class);


    public enum SignatureAlgConsistencyCheckStatus implements CheckStatus {
        
        SIGNATURE_ALG_MATCHES,
        SIGNATURE_ALG_MISMATCH;

        public String getText()
        {
            switch (this) {
                case SIGNATURE_ALG_MATCHES:
                    return CertI18n.message(CertI18n.SIGNATURE_ALG_AYNI);
                case SIGNATURE_ALG_MISMATCH:
                    return CertI18n.message(CertI18n.SIGNATURE_ALG_FARKLI);

                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }
    }

    /**
     * Sertifika icindeki tbscertificate.signature sertifika icindeki signatureAlgorithm de�erlerini kontrol eder
     * 4.1.2.3 Signature field MUST contain the same algorithm identifier as the
     * signatureAlgorithm field in the sequence Certificate
     */
    protected PathValidationResult _check(CertificateStatusInfo aCertStatusInfo)
    {
        if (logger.isDebugEnabled())
            logger.debug("Sertifika içindeki algoritmalar kontrol edilecek");

        ECertificate cert = aCertStatusInfo.getCertificate();
        EAlgorithmIdentifier tbsAlg = new EAlgorithmIdentifier(cert.getObject().tbsCertificate.signature);

        if (!tbsAlg.equals(cert.getSignatureAlgorithm())) {
            logger.error("Sertifika içindeki algoritmalar farkli");
            aCertStatusInfo.addDetail(this, SignatureAlgConsistencyCheckStatus.SIGNATURE_ALG_MISMATCH, false);
            return PathValidationResult.SIGNATURE_ALGORITHM_DIFFERENT;
        }
        else {
            if (logger.isDebugEnabled())
                logger.debug("Sertifika içindeki algoritmalar aynı");
            aCertStatusInfo.addDetail(this, SignatureAlgConsistencyCheckStatus.SIGNATURE_ALG_MATCHES, true);
            return PathValidationResult.SUCCESS;
        }
    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.SIGNATURE_ALG_AYNIMI_KONTROLU);
    }

}
