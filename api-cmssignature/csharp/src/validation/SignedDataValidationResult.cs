using System;
using System.Collections.Generic;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation
{

    /**
     * Keeps results of the signatures a signed document.
     * @author orcun.ertugrul
     *
     */
    [Serializable]
    public class SignedDataValidationResult : IValidationResult
    {
        private String mDescription;
        private SignedData_Status mStatus = SignedData_Status.NOT_ALL_VALID;
        
        private readonly List<SignatureValidationResult> sigValidationResults = new List<SignatureValidationResult>();

        //private NotSerializableClass dummy;

        /**
    	 * 
    	 * @return description of the result of signature
    	 */
        public String getDescription()
        {
            return mDescription;
        }

        /**
    	 * sets the description
    	 * @param aDescription
    	 */
        public void setDescription(String aDescription)
        {
            mDescription = aDescription;
        }

        /**
    	 * 
    	 * @return the signed data status
    	 */
        public SignedData_Status getSDStatus()
        {
            return mStatus;
        }

        /**
    	 * sets the status
    	 * @param aStatus 
    	 */
        public void setSDStatus(SignedData_Status aStatus)
        {
            mStatus = aStatus;
        }

        /**
    	 * 
    	 * @return the each result of signature
    	 */
        public List<SignatureValidationResult> getSDValidationResults()
        {
            return sigValidationResults;
        }

        /**
    	 * adds signature validation result
    	 * @param aSVR
    	 */
        public void addSignatureValidationResult(SignatureValidationResult aSVR)
        {
            sigValidationResults.Add(aSVR);
        }

        /**
         * Convert all validation result of signatures to string
         */
        public override String ToString()
        {
            StringBuilder result = new StringBuilder();
            int i = 1;
            foreach (SignatureValidationResult svr in sigValidationResults)
            {
                result.Append(i++).Append(Msg.getMsg(Msg.SIGNATURE_CHECKED_RESULTS).Replace("@",System.Environment.NewLine));
                result.Append(Msg.getMsg(Msg.SIGNER_CERTIFICATE)).Append(svr.getSignerCertificate().getSubject().stringValue()).Append("\n");
                result.Append(svr.ToString());
            }
            return result.ToString();
        }
        /**
         * Prints all validation result of signatures
         */
        public void printDetails()
        {
            Console.WriteLine(ToString());
        }

    }
}
