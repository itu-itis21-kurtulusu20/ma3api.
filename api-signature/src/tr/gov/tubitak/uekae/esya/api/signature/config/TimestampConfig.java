package tr.gov.tubitak.uekae.esya.api.signature.config;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;

import static tr.gov.tubitak.uekae.esya.api.signature.config.ConfigConstants.*;

/**
 * @author ayetgin
 */
public class TimestampConfig extends BaseConfigElement
{
    private String mHost;
    private String mUserId;
    private String mPassword;
    private DigestAlg mDigestAlg = DigestAlg.SHA256;

    public TimestampConfig(Element aElement)
    {
        super(aElement);
        mHost = getChildText(NS_MA3, TAG_HOST);
        mUserId = getChildText(NS_MA3, TAG_USERID);
        mPassword = getChildText(NS_MA3, TAG_PASSWORD);

        String digestAlgStr = getChildText(NS_MA3, TAG_DIGESTALG);
        if (digestAlgStr != null) {
            mDigestAlg = DigestAlg.fromName(digestAlgStr);
        }
    }

    public TimestampConfig(String aHost, String aUserId, String aPassword, DigestAlg aDigestAlg)
    {
        mHost = aHost;
        mUserId = aUserId;
        mPassword = aPassword;
        mDigestAlg = aDigestAlg;
    }

    public String getHost()
    {
        return mHost;
    }

    public String getUserId()
    {
        return mUserId;
    }

    public String getPassword()
    {
        return mPassword;
    }

    public DigestAlg getDigestAlg()
    {
        return mDigestAlg;
    }

    public void setHost(String mHost)
    {
        this.mHost = mHost;
    }

    public void setUserId(String mUserId)
    {
        this.mUserId = mUserId;
    }

    public void setPassword(String mPassword)
    {
        this.mPassword = mPassword;
    }

    public void setDigestAlg(DigestAlg mDigestAlg)
    {
        this.mDigestAlg = mDigestAlg;
    }

    public TSSettings getSettings()
    {
        if (mUserId != null && mUserId.trim().length() > 0)
        {
            int userIdInt ;

            try {
                userIdInt = Integer.valueOf(mUserId);
            } catch (NumberFormatException e) {
                throw new ESYARuntimeException("Time Stamp User ID must be integer");
            }

            return new TSSettings(mHost, userIdInt, mPassword, mDigestAlg);
        }
        else
            return new TSSettings(mHost);
    }
}
