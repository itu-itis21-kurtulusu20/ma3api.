using System.Collections.Generic;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asic.model;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;

namespace tr.gov.tubitak.uekae.esya.api.asic.core
{
    /**
     * @author yavuz.kahveci
     */
    public class PackageValidationResultImpl : PackageValidationResult
    {
        private PackageValidationResultType resultType;
        private readonly IDictionary<SignatureContainerEntryImpl, ContainerValidationResult> results = new Dictionary<SignatureContainerEntryImpl, ContainerValidationResult>();

        public PackageValidationResultImpl()
        {
        }

        public PackageValidationResultImpl(PackageValidationResultType aResultType, IDictionary<SignatureContainerEntryImpl, ContainerValidationResult> aResults)
        {
            resultType = aResultType;
            results = aResults;
        }

        public void setResultType(PackageValidationResultType resultType)
        {
            this.resultType = resultType;
        }

        public PackageValidationResultType getResultType()
        {
            return resultType;
        }

        public IDictionary<SignatureContainerEntry, ContainerValidationResult> getAllResults()
        {
            // todo burda cast yapmak icab etti
            return new Dictionary<SignatureContainerEntry, ContainerValidationResult>((IDictionary<SignatureContainerEntry, ContainerValidationResult>) results);
        }

        public void addResult(SignatureContainerEntryImpl container, ContainerValidationResult validationResult){
            results.Add(container, validationResult);
        }

        public override string ToString()
        {
            StringBuilder buffer = new StringBuilder();
            buffer.Append("Package Validation Result : ").Append(resultType).Append("\n");
            //int index=0;
            foreach (SignatureContainerEntryImpl container in results.Keys){
                ContainerValidationResult cvr = results[container];
                buffer.Append(mark(cvr)).Append("Container ").Append(container.getASiCDocumentName()).Append("\n> ");
                buffer.Append(cvr.ToString());
            }
            return buffer.ToString();
        }

        private string mark(ContainerValidationResult cvr){
            switch (cvr.getResultType()){
                case ContainerValidationResultType.ALL_VALID: return "(+) ";
                case ContainerValidationResultType.CONTAINS_INCOMPLETE: return "(?) ";
                default: return "(-) ";
            }
        }

    }
}
