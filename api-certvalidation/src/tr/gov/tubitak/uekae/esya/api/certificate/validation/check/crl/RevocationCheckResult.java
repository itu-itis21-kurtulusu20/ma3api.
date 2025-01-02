package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;

import java.io.Serializable;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.CheckResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;

/**
 * The result of the Certificate Revocation Check operation
 */
public class RevocationCheckResult extends CheckResult implements Serializable
{

    private RevokeCheckStatus mCheckStatus;

    public RevocationCheckResult(String aKontrolText, String aResultText,
                                 CheckStatus aResult,
                                 RevokeCheckStatus aCheckResult)
    {
        super(aKontrolText, aResultText, aResult, false);
        mCheckStatus = aCheckResult;
    }

    public RevokeCheckStatus getRevocationCheckResult()
    {
        return mCheckStatus;
    }

    public void setRevocationCheckResult(RevokeCheckStatus aIKST){
        mCheckStatus = aIKST;
    }

}
