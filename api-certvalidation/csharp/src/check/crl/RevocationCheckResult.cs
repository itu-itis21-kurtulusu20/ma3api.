using System;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl
{
    /**
     * The result of the Certificate Revocation Check operation
     */
    [Serializable]
    public class RevocationCheckResult : CheckResult
    {
        private RevokeCheckStatus mCheckStatus;

        public RevocationCheckResult(String aKontrolText, String aResultText,
                                     CheckStatus aResult,
                                     RevokeCheckStatus aCheckResult)
            : base(aKontrolText, aResultText, aResult, false)
        {
            
            mCheckStatus = aCheckResult;
        }

        public RevokeCheckStatus getRevocationCheckResult()
        {
            return mCheckStatus;
        }

        public void setRevocationCheckResult(RevokeCheckStatus aIKST)
        {
            mCheckStatus = aIKST;
        }
    }
}
