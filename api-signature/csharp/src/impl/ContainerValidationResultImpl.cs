using System;
using System.Collections.Generic;

namespace tr.gov.tubitak.uekae.esya.api.signature.impl
{
    /**
     * ContainerValidationResult implementation for signature API providers
     * @author ayetgin
     */
    public class ContainerValidationResultImpl : ContainerValidationResult
    {
        private readonly Dictionary<Signature, SignatureValidationResult> results = new Dictionary<Signature, SignatureValidationResult>();
        private ContainerValidationResultType resultType;

        public ContainerValidationResultImpl()
        {
        }

        public ContainerValidationResultImpl(ContainerValidationResultType aResultType, Dictionary<Signature, SignatureValidationResult> aResults)
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
            List<SignatureValidationResult> invalids = new List<SignatureValidationResult>();
            foreach (Signature signature in results.Keys) {
                traceResults(results[signature], invalids);
            }
            return invalids;
        }

        private void traceResults(SignatureValidationResult svr, List<SignatureValidationResult> invalids)
        {
            // imza geçerli mi?
            if (svr.getResultType() != ValidationResultType.VALID) {
                invalids.Add(svr);
            }
            // counter signatures
            if (svr.getCounterSignatureValidationResults() != null) {
                foreach (SignatureValidationResult counter in svr.getCounterSignatureValidationResults())
                    traceResults(counter, invalids);
            }
        }

        public Dictionary<Signature, SignatureValidationResult> getSignatureValidationResults()
        {
            return results;
        }


        public void addResult(Signature signature, SignatureValidationResult aValidationResult)
        {
            results.Add(signature, aValidationResult);
        }

        public void setResultType(ContainerValidationResultType aResultType)
        {
            resultType = aResultType;
        }
        // @Override
        public override String ToString()
        {
            ResultFormatter formatter = new ResultFormatter();
            return formatter.prettyPrint(this);
        }
    }
}
