package tr.gov.tubitak.uekae.esya.api.signature;

import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;

import java.util.List;
import java.util.Map;

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
    Map<Signature, SignatureValidationResult> getSignatureValidationResults();

    /**
     * @return all signature validation results that are not valid,
     * including counter signatures
     */
    List<SignatureValidationResult> getInvalidValidationResults();

}
