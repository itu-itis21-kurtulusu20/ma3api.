package tr.gov.tubitak.uekae.esya.api.signature.impl;

import tr.gov.tubitak.uekae.esya.api.signature.*;

import java.util.*;

/**
 * ContainerValidationResult implementation for signature API providers
 *
 * @author ayetgin
 */
public class ContainerValidationResultImpl implements ContainerValidationResult
{

    private Map<Signature, SignatureValidationResult> results = new LinkedHashMap<Signature, SignatureValidationResult>();
    private ContainerValidationResultType resultType;

    public ContainerValidationResultImpl()
    {
    }

    public ContainerValidationResultImpl(ContainerValidationResultType aResultType, Map<Signature, SignatureValidationResult> aResults)
    {
        results = aResults;
        resultType = aResultType;
    }

    public ContainerValidationResultType getResultType()
    {
        return resultType;
    }

    public List<SignatureValidationResult> getInvalidValidationResults()
    {
        List<SignatureValidationResult> invalids = new ArrayList<SignatureValidationResult>();
        for (Signature signature : results.keySet()) {
            traceResults(results.get(signature), invalids);
        }
        return invalids;
    }

    private void traceResults(SignatureValidationResult svr, List<SignatureValidationResult> invalids)
    {
        // imza geçerli mi?
        if (svr.getResultType() != ValidationResultType.VALID) {
            invalids.add(svr);
        }
        // counter signatures
        if (svr.getCounterSignatureValidationResults() != null) {
            for (SignatureValidationResult counter : svr.getCounterSignatureValidationResults())
                traceResults(counter, invalids);
        }
    }

    public Map<Signature, SignatureValidationResult> getSignatureValidationResults()
    {
        return results;
    }

    public void addResult(Signature signature, SignatureValidationResult aValidationResult)
    {
        results.put(signature, aValidationResult);
    }

    public void setResultType(ContainerValidationResultType aResultType)
    {
        resultType = aResultType;
    }

    @Override
    public String toString()
    {
        ResultFormatter formatter = new ResultFormatter();
        return formatter.prettyPrint(this);
    }
}
