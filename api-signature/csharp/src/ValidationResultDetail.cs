using System;
using System.Collections.Generic;

namespace tr.gov.tubitak.uekae.esya.api.signature
{
    /**
 * Check result for any property of signature. Multiple validation result
 * details are hold in SignatureValidationResult.
 *
 * @see SignatureValidationResult
 * @author ayetgin
 */
    public interface ValidationResultDetail
    {
        Type getValidatorClass();
        String getCheckMessage();
        String getCheckResult();
        ValidationResultType getResultType();

        List<T> getDetails<T>() where T : ValidationResultDetail;
        //List<ValidationResultDetail> getDetails();
    }
}
