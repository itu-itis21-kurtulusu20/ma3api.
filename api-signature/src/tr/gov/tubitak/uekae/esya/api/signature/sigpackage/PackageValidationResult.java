package tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

import tr.gov.tubitak.uekae.esya.api.signature.ContainerValidationResult;

import java.util.Map;

/**
 * @author ayetgin
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
    Map<SignatureContainerEntry, ContainerValidationResult> getAllResults();

}
