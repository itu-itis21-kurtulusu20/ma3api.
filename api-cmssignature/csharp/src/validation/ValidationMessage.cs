using System;
using System.Text;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation
{
    [Serializable]
    public class ValidationMessage : IValidationResult
    {
        private readonly String mMessage;
        private readonly Exception mThrowable;

        public ValidationMessage(String aMessage)
        {
            mMessage = aMessage;
        }

        public ValidationMessage(String aMessage, Exception aThrowable)
        {
            mMessage = aMessage;
            mThrowable = aThrowable;
        }

        public String getMessage()
        {
            return mMessage;
        }

        public Exception getThrowable()
        {
            return mThrowable;
        }


        public override String ToString()
        {
            StringBuilder result = new StringBuilder();
            result.Append(mMessage).Append("\n");
            if (mThrowable != null)
            {
                //StringWriter writer = new StringWriter();
                //PrintWriter pwriter = new PrintWriter(writer);
                result.Append(mThrowable.ToString());
            }
            return result.ToString();
        }

        public void printDetails()
        {
            Console.WriteLine(ToString());
        }


    }
}
