package tr.gov.tubitak.uekae.esya.api.signature.certval;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;

import java.util.Date;

/**
 * @author ayetgin
 */

public class OCSPSearchCriteria
{
    private String mOCSPResponderIDByName;
    private byte[] mOCSPResponderIDByKey;
    private DigestAlg mDigestAlg;
    private byte[] mDigestValue;
    private Date mProducedAt;

    public OCSPSearchCriteria() {
    }

    public OCSPSearchCriteria(String aOCSPResponderIDByName,
                              byte[] aOCSPResponderIDByKey, 
                              DigestAlg aDigestAlg, byte[] aDigestValue,
                              Date aProducedAt)
    {
        mOCSPResponderIDByName = aOCSPResponderIDByName;
        mOCSPResponderIDByKey = aOCSPResponderIDByKey;
        mDigestAlg = aDigestAlg;
        mDigestValue = aDigestValue;
        mProducedAt = aProducedAt;
    }

    public String getOCSPResponderIDByName()
    {
        return mOCSPResponderIDByName;
    }

    public void setOCSPResponderIDByName(String aOCSPResponderIDByName)
    {
        mOCSPResponderIDByName = aOCSPResponderIDByName;
    }

    public byte[] getOCSPResponderIDByKey()
    {
        return mOCSPResponderIDByKey;
    }

    public void setOCSPResponderIDByKey(byte[] aOCSPResponderIDByKey)
    {
        mOCSPResponderIDByKey = aOCSPResponderIDByKey;
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

    public Date getProducedAt()
    {
        return mProducedAt;
    }

    public void setProducedAt(Date aProducedAt)
    {
        mProducedAt = aProducedAt;
    }
}
