package tr.gov.tubitak.uekae.esya.api.cmssignature.validation;



public class Types
{

	public enum CheckerResult_Status 
	{
		SUCCESS ("(+)"),
		UNSUCCESS ("(-)"),
		NOTFOUND ("(!)"),
		INCOMPLETE("(.)");

        private String mIdentifier;

        CheckerResult_Status(String aIdentifier)
        {
            mIdentifier = aIdentifier;
        }

        public String getIdentifier(){
            return mIdentifier;
        }
	};


	public enum Signature_Type
	{
		PARALLEL,
		COUNTER
	};


	public enum Signature_Status 
	{
		/**
		 * Signature is validated
		 */
		VALID , 
		/**
		 * Signature can not valid
		 */
		INVALID,
		/**
		 * Can not reach certificate revocation info
		 */
		INCOMPLETE
	};
	
	
	

	public enum Param_Type
	{
		GIVEN_SIGNER_CERTIFICATES,
		SIGNING_CERTIFICATE,
		SIGNED_DATA,
		IMZALANAN_VERI,
		PARENT_SIGNER_INFO,
		SIGNED_ATTR_INC_SET,
		SIGNED_ATTR_EXC_SET,
		SIGNED_ATTR_VALID_SET,
		POLICY_FILE_VALUE,
		CONTENT
	};


	public enum TS_Type
	{
		EST,
		ESC,
		ESA,
		ESAv3,		
		ES_REFS,
		CONTENT,
	}
	
}
