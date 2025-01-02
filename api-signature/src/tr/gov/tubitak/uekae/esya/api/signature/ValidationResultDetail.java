package tr.gov.tubitak.uekae.esya.api.signature;

import java.util.List;

/**
 * Check result for any property of signature. Multiple validation result
 * details are hold in SignatureValidationResult.
 *
 * @see SignatureValidationResult
 * @author ayetgin
 */
public interface ValidationResultDetail
{
    Class getValidatorClass();
    String getCheckMessage();
    String getCheckResult();
    ValidationResultType getResultType();

    List<? extends ValidationResultDetail> getDetails();
}
