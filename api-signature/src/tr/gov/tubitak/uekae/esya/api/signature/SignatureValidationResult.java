package tr.gov.tubitak.uekae.esya.api.signature;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;

import java.util.List;
import java.util.Map;

/**
 * Validation result for one signature. Multiple SignatureValidationResult can
 * be within ContainerValidationResult.
 *
 * @see ValidationResultDetail
 * @author ayetgin
 */
public interface SignatureValidationResult
{
    /**
     * @return validation result ıf signers certificate.
     */
    CertificateStatusInfo getCertificateStatusInfo();

    /**
     * @return type of result VALID, INVALID, INCOMPLETE
     */
    ValidationResultType getResultType();

    String getCheckMessage();

    String getCheckResult();

    /**
     * @return detailed check results
     */
    List<ValidationResultDetail> getDetails();

    /**
     * @return validation results of counter signatures
     */
    List<SignatureValidationResult> getCounterSignatureValidationResults();
}
