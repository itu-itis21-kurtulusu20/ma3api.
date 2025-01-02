package tr.gov.tubitak.uekae.esya.api.signature.certval;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;

import java.math.BigInteger;
import java.util.Date;


/**
 * @author ayetgin
 */

public class CRLSearchCriteria
{
    private String mIssuer;
    private Date mIssueTime;
    private BigInteger mNumber;
    private DigestAlg mDigestAlg;
    private byte[] mDigestValue;

    public CRLSearchCriteria()
    {
    }

    public CRLSearchCriteria(String aIssuer, Date aIssueTime, BigInteger aNumber, DigestAlg aDigestAlg, byte[] aDigestValue)
    {
        mIssuer = aIssuer;
        mIssueTime = aIssueTime;
        mNumber = aNumber;
        mDigestAlg = aDigestAlg;
        mDigestValue = aDigestValue;
    }

    public String getIssuer()
    {
        return mIssuer;
    }

    public void setIssuer(String aIssuer)
    {
        mIssuer = aIssuer;
    }

    public Date getIssueTime()
    {
        return mIssueTime;
    }

    public void setIssueTime(Date aIssueTime)
    {
        mIssueTime = aIssueTime;
    }

    public BigInteger getNumber()
    {
        return mNumber;
    }

    public void setNumber(BigInteger aNumber)
    {
        mNumber = aNumber;
    }

    public DigestAlg getDigestAlg()
    {
        return mDigestAlg;
    }

    public void setDigestAlg(DigestAlg aDigestAlg)
    {
        mDigestAlg = aDigestAlg;
    }

    public byte[] getDigestValue()
    {
        return mDigestValue;
    }

    public void setDigestValue(byte[] aDigestValue)
    {
        mDigestValue = aDigestValue;
    }
}
