using System;
using System.Collections.Generic;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    [Serializable]
    public class CheckerResult : IValidationResult, ValidationResultDetail
    {      
        private int mCheckerLevel = 0;
        private String mCheckerName;
        //	private List<ValidationMessage> mMessages = new ArrayList<ValidationMessage>();
        private Types.CheckerResult_Status mResultStatus = Types.CheckerResult_Status.UNSUCCESS;
        private Object mResultObject;
        private readonly List<IValidationResult> mResults = new List<IValidationResult>();
        private List<CheckerResult> mCheckerResults = null;
        private Type mCheckerClass;

        /**
            * Returns checker name
            * @return 
            */
        public String getCheckerName()
        {
            return mCheckerName;
        }
        /**
         * Returns checker message
         * @return 
         */
        public String getCheckMessage()
        {
            return mCheckerName;
        }

        public void setCheckerName(String aCheckerName, Type aCheckerClass)
        {
            mCheckerName = aCheckerName;
            mCheckerClass = aCheckerClass;
        }
        /**
         * Returns checker class
         * @return 
         */
        public Type getCheckerClass()
        {
            return mCheckerClass;
        }
        public Type getValidatorClass()
        {
            return mCheckerClass;
        }
        /*public List<ValidationMessage> getMessages()
        {
            return mMessages;
        }
	
        public void addMessage(ValidationMessage aMessage)
        {
            mMessages.add(aMessage);
        }*/
        /**
         * Returns checker result status
         * @return 
         */
        public Types.CheckerResult_Status getResultStatus()
        {
            return mResultStatus;
        }

        public void setResultStatus(Types.CheckerResult_Status aResultStatus)
        {
            mResultStatus = aResultStatus;
        }
        /**
         * Returns checker result object
         * @return 
         */
        public Object getResultObject()
        {
            return mResultObject;
        }
        /**
         * Set result object of checker
         * @param aCheckerResult
         */
        public void setResultObject(Object aResultObject)
        {
            mResultObject = aResultObject;
        }

        public void setMsCheckerLevel(int mCheckerLevel)
        {
            this.mCheckerLevel = mCheckerLevel;
        }
        /**
         * Returns messages  of result 
         * @return 
         */
        public List<IValidationResult> getMessages()
        {
            return mResults;
        }
        /**
         * Add messages  of result 
         * @param aCheckerResult
         */
        public void addMessage(IValidationResult aResult)
        {
            mResults.Add(aResult);
        }
        /**
         * Clear all messages 
         */
        public void removeMessages()
        {
            mResults.Clear();
        }
        /**
        * Add checker result
        * @param aCheckerResult
        */
        public void addCheckerResult(CheckerResult aCheckerResult)
        {
            if (mCheckerResults == null)
                mCheckerResults = new List<CheckerResult>();

            mCheckerResults.Add(aCheckerResult);
        }
        /**
            * Returns results of checker
            * @return 
            */
        public List<CheckerResult> getCheckerResults()
        {
            return mCheckerResults;
        }
        /**
         * Returns results of checker as string
         * @return 
         */
        public String getCheckResult()
        {
            StringBuilder buffer = new StringBuilder();
            foreach (IValidationResult vr in mResults)
            {
                buffer.Append(vr.ToString());
            }

            return buffer.ToString();
        }

        public List<T> getDetails<T>() where T : ValidationResultDetail
        {
            List<CheckerResult> details=getDetails();
            List<ValidationResultDetail> detail = new List<ValidationResultDetail>();
            for(int i=0;i<details.Count;i++)
            {
                detail.Add(details[i]);  
            }
            return detail as List<T>;
        }
        /**
         * Returns results of checker
         * @return 
         */
        public List<CheckerResult> getDetails()
        {
            return mCheckerResults == null ? new List<CheckerResult>() : mCheckerResults;
        }
        /**
         * Returns result type of checker(VALID,INVALID,INCOMPLETE)
         * @return 
         */
        public ValidationResultType getResultType()
        {
            switch (mResultStatus.getIdentifier())
            {
                case "(+)": return ValidationResultType.VALID;
                case "(-)": return ValidationResultType.INVALID;
                default: return ValidationResultType.INCOMPLETE;
            }
        }
        /**
         * Convert checker result of signatures to string
         */
        public override String ToString()
        {
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < mCheckerLevel; i++)
                result.Append("\t");

            result.Append(mResultStatus.getIdentifier()).Append(" ").Append(mCheckerName).Append("\n");


            foreach (IValidationResult vr in mResults)
            {

                for (int i = 0; i < mCheckerLevel; i++)
                    result.Append("\t");

                result.Append("\t");
                result.Append(vr.ToString());
            }


            if (mCheckerResults != null)
            {

                for (int i = 0; i < mCheckerLevel; i++)
                    result.Append("\t");

                result.Append("\t");
                result.Append(Msg.getMsg(Msg.SUB_CHECKER_RESULTS));
                result.Append("\n");
               
                foreach (CheckerResult cr in mCheckerResults)
                {
                    cr.setMsCheckerLevel(mCheckerLevel + 1);
                    result.Append(cr.ToString());
                }
               
            }
            return result.ToString();

        }
        /**
         * Prints checker result
         */
        public void printDetails()
        {
            Console.WriteLine(ToString());
        }

    }
}
