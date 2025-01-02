using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;

namespace tr.gov.tubitak.uekae.esya.api.signature
{
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
}
