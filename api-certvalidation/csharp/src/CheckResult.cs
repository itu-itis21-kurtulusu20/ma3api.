using System;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    /**
     * Stores the details of the result of control operations performed by the
     * Checker objects 
     */
    [Serializable]
    public class CheckResult
    {
        /*[NonSerialized]
        private Checker mChecker;*/

        private CheckStatus mResult;
        private String mCheckText;
        private String mResultText;
        private bool mSuccessful;

        public CheckResult()
        {
        }

        public CheckResult(String aCheckText, String aResultText, CheckStatus aResult, bool aSuccessful)
        {
            mResult = aResult;
            mCheckText = aCheckText;
            mResultText = aResultText;
            mSuccessful = aSuccessful;
        }

        /*public Checker getChecker()
        {
            return mChecker;
        }*/

        /*public void setChecker(Checker aChecker)
        {
            mChecker = aChecker;
        }*/

        public bool isSuccessful()
        {
            return mSuccessful;
        }

        public void setSuccessful(bool aBasarili)
        {
            mSuccessful = aBasarili;
        }

        public String getCheckText()
        {
            return mCheckText;
        }

        public void setCheckText(String aKontrolText)
        {
            mCheckText = aKontrolText;
        }

        public CheckStatus getResult()
        {
            return mResult;
        }

        public void setResult(CheckStatus aSonuc)
        {
            mResult = aSonuc;
        }

        public String getResultText()
        {
            return mResultText;
        }

        public void setResultText(String aSonucText)
        {
            mResultText = aSonucText;
        }
        
        public override String ToString()
        {
            StringBuilder sb = new StringBuilder();

            sb.Append(mCheckText + Environment.NewLine);
            sb.Append("\t" + mResultText);

            return sb.ToString();
        }

    }
}
