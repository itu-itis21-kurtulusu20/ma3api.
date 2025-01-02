using System;
using System.Collections.Generic;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;
using tr.gov.tubitak.uekae.esya.api.signature;



//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation
{
    /**
 * Keeps result of signature
 * @author orcun.ertugrul
 *
 */
    [Serializable]
    public class SignatureValidationResult : IValidationResult, api.signature.SignatureValidationResult
    {
        private String mName;
        private String mDescription;
        private Types.Signature_Status mSignatureStatus;
        private ECertificate mCertificate;
        private CertificateStatusInfo mCertStatusInfo;

        private List<CheckerResult> mCheckDetails = null;

        private List<SignatureValidationResult> mCounterSigValidationRslts = null;


        private ValidationState mValidationState;
        private DateTime? mSigningTime;


        /**
        * gets name
        * @return
        */
        public String getName()
        {
            return mName;
        }
        /**
	    * sets name
	    * @param aName
	    */
        public void setName(String aName)
        {
            mName = aName;
        }
        /**
	    * sets description of result
	    * @return
	    */
        public String getDescription()
        {
            return mDescription;
        }
        /**
	    * gets description of result
	    * @param aDesc
	    */
        public void setDescription(String aDesc)
        {
            mDescription = aDesc;
        }
        /**
	    * gets signature status
	    * @return
	    */
        public Types.Signature_Status getSignatureStatus()
        {
            return mSignatureStatus;
        }
        public ValidationResultType getResultType()
        {
            switch (mSignatureStatus)
            {
                case Types.Signature_Status.INCOMPLETE: return ValidationResultType.INCOMPLETE;
                case Types.Signature_Status.INVALID: return ValidationResultType.INVALID;
                case Types.Signature_Status.VALID: return ValidationResultType.VALID;
            }
            throw new SignatureRuntimeException("Unknown signature status : " + mSignatureStatus);
        }


        /**
	    * sets signature status
	    * @param aStatus
	    */
        public void setSignatureStatus(Types.Signature_Status aStatus)
        {
            mSignatureStatus = aStatus;
        }
        /**
	    * gets signer certificate
	    * @return
	    */
        public ECertificate getSignerCertificate()
        {
            return mCertificate;
        }
        /**
	    * sets signer certificate
	    * @param aCert
	    */
        public void setSignerCertificate(ECertificate aCert)
        {
            mCertificate = aCert;
        }

        /**
	    * gets certificate status
	    * @return
	    */
        public CertificateStatusInfo getCertStatusInfo()
        {
            return mCertStatusInfo;
        }
        public CertificateStatusInfo getCertificateStatusInfo()
        {
            return mCertStatusInfo;
        }
        public DateTime? getSigningTime()
        {
            return mSigningTime;
        }

        /**
         * sets certificate status
         * @param aCertStatusInfo
         */
        public void setCertStatusInfo(CertificateStatusInfo aCertStatusInfo)
        {
            mCertStatusInfo = aCertStatusInfo;
        }
        /**
        * gets all checker's results
        * @return
        */
        public List<CheckerResult> getCheckerResults()
        {
            return mCheckDetails;
        }


        /***
     * sets validation state
     * @param validationState
     */
        public void setValidationState(ValidationState validationState)
        {
            mValidationState = validationState;
        }

        /**
         * gets validation state
         * @return
         */
        public ValidationState getValidationState()
        {
            return mValidationState;
        }


        /**
        * adds checker result
        * @param aResult
        */
        public void addCheckResult(CheckerResult aResult)
        {
            if (mCheckDetails == null)
            {
                mCheckDetails = new List<CheckerResult>();
            }
            if (aResult.getResultObject() != null)
            {
                if (aResult.getResultObject() is CertificateCheckerResultObject)
                {
                    CertificateCheckerResultObject certChecherResulObj = (CertificateCheckerResultObject)aResult.getResultObject();
                    mCertStatusInfo = certChecherResulObj.getCertStatusInfo();
                    mSigningTime = certChecherResulObj.getSigningTime();
                }

            }
            mCheckDetails.Add(aResult);
        }

        /**
    	 * gets counter signature validation results
    	 * @return
    	 */
        public List<SignatureValidationResult> getCounterSigValidationResults()
        {
            return mCounterSigValidationRslts;
        }
        public List<tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult> getCounterSignatureValidationResults()
        {           
            if (mCounterSigValidationRslts == null)
                return new List<tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult>();
            List<tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult> results = new List<tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult>();
            foreach (SignatureValidationResult result in mCounterSigValidationRslts)
            {
                results.Add(result);
            }
            return results;
        }
        /**
    	 * adds counter signature validation result
    	 * @param aResult
    	 */
        public void addCounterSigValidationResult(SignatureValidationResult aResult)
        {
            if (mCounterSigValidationRslts == null)
            {
                mCounterSigValidationRslts = new List<SignatureValidationResult>();
            }
            mCounterSigValidationRslts.Add(aResult);
        }

        /**
	    * returns checkers' results of signature and counter signatures
	    */
        public override String ToString()
        {
            StringBuilder result = new StringBuilder();
            result.Append(getValidationDetails());

            if (mCounterSigValidationRslts != null)
            {
                result.Append(Msg.getMsg(Msg.COUNTER_SIGNATURE_VERIFICATION_RESULTS).Replace("@", System.Environment.NewLine));
                int i = 1;
                foreach (SignatureValidationResult svr in mCounterSigValidationRslts)
                {
                    result.Append(i++).Append(Msg.getMsg(Msg.COUNTER_SIGNATURE_CHECKED).Replace("@", System.Environment.NewLine));
                    result.Append(Msg.getMsg(Msg.SIGNER_CERTIFICATE).Replace("@", System.Environment.NewLine))
                            .Append(svr.getSignerCertificate().getSubject().stringValue())
                            .Append("\n");
                    result.Append(svr.ToString());
                }
            }
            return result.ToString();
        }
        public String getCheckMessage()
        {
            return mName != null ? mName : "Imza kontrolu"; //todo i18n
        }

        public String getCheckResult()
        {
            return mDescription != null ? mDescription : mSignatureStatus.ToString();
        }

        public List<ValidationResultDetail> getDetails()
        {
            List<ValidationResultDetail> result = new List<ValidationResultDetail>();
            for (int i = 0; i < mCheckDetails.Count; i++)
                result.Add(mCheckDetails[i]);
            return result;
        }

        /**
     * returns checkers' results of only this signature
     * @return
     */
        public String getValidationDetails()
        {
            StringBuilder result = new StringBuilder();
            if (mValidationState == ValidationState.PREMATURE)
                result.Append(Msg.getMsg(Msg.PRE_VERIFICATION_DONE).Replace("@", System.Environment.NewLine));

            if (mCheckDetails != null)
            {
                result.Append(Msg.getMsg(Msg.SIGNATURE_CHECKER_RESULTS).Replace("@", System.Environment.NewLine));
                foreach (CheckerResult cr in mCheckDetails)
                {
                    result.Append(cr.ToString());
                }
            }
            return result.ToString();
        }


        public void printDetails()
        {
            Console.WriteLine(ToString());
        }

    }
}
