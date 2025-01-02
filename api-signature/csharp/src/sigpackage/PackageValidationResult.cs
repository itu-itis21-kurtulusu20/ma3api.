using System.Collections.Generic;

namespace tr.gov.tubitak.uekae.esya.api.signature.sigpackage
{
    /**
     * @author yavuz.kahveci
     */
    public interface PackageValidationResult
    {
        /**
         * @return Result type summing all validation results
         */
        PackageValidationResultType getResultType();

        /**
         * @return Map of all results in pairs of Signature:SignatureValidationResult
         */
        IDictionary<SignatureContainerEntry, ContainerValidationResult> getAllResults();

    }
}