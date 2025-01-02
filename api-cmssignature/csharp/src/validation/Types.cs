using System;
namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation
{
    //todo Annotation!
    //@ApiClass
    public class Types
    {
        //todo Annotation!
        //@ApiClass
        [Serializable]
        public class CheckerResult_Status
        {
            //enum _enum
            //{
            //    SUCCESS,
            //    UNSUCCESS,
            //    NOTFOUND,
            //    INCOMPLETE
            //}
            public static readonly CheckerResult_Status SUCCESS = new CheckerResult_Status("(+)");
            public static readonly CheckerResult_Status UNSUCCESS = new CheckerResult_Status("(-)");
            public static readonly CheckerResult_Status NOTFOUND = new CheckerResult_Status("(!)");
            public static readonly CheckerResult_Status INCOMPLETE = new CheckerResult_Status("(.)");

            private readonly String mIdentifier;

            internal CheckerResult_Status(String aIdentifier)
            {
                mIdentifier = aIdentifier;
            }

            public String getIdentifier()
            {
                return mIdentifier;
            }

        };

        //todo Annotation!
        //@ApiClass
        public enum Signature_Type
        {
            PARALLEL,
            COUNTER
        };

        //todo Annotation!
        //@ApiClass
        public enum Signature_Status
        {
            /**
		 * Signature is validated
		 */
            VALID,
            /**
		 * Signature can not valid
		 */
            INVALID,
            /**
		 * Can not reach certificate revocation info
		 */
            INCOMPLETE
        };

        //todo Annotation!
        //@ApiClass
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

        //todo Annotation!
        //@ApiClass
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
}
