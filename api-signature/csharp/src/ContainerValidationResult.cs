using System.Collections.Generic;

namespace tr.gov.tubitak.uekae.esya.api.signature
{
    /**
 * Holds signature validation result for all signatures in a SignatureContainer
 *
 * @see SignatureContainer#verifyAll()
 * @see SignatureValidationResult
 * @author ayetgin
 */
    public interface ContainerValidationResult
    {
        /**
      * @return Result type summing all validation results
      */
        ContainerValidationResultType getResultType();

        /**
         * @return Map of root signature results in pairs
         * of Signature:SignatureValidationResult
         */
        Dictionary<Signature, SignatureValidationResult> getSignatureValidationResults();
        /**
         * @return all signature validation results that are not valid,
         * including counter signatures
         */
        List<SignatureValidationResult> getInvalidValidationResults();
    }
}
