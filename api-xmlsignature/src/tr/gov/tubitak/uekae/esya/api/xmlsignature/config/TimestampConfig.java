package tr.gov.tubitak.uekae.esya.api.xmlsignature.config;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.signature.config.ConfigurationException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.C14nMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

/**
 * @author ayetgin
 * @deprecated
 * @see tr.gov.tubitak.uekae.esya.api.signature.config.TimestampConfig
 */
@Deprecated
public class TimestampConfig extends tr.gov.tubitak.uekae.esya.api.signature.config.TimestampConfig
{
    // bu degisken timestamp'in baska c14n'lere sahip olabilmesi icin duzenlenmistir
    private C14nMethod mC14nMethod = null;

    public TimestampConfig(Element aElement) throws ConfigurationException
    {
        super(aElement);

        // burasi de bu c14n'i config'den okumaya calisiyor, bulursa set ediyor
        String c14nMethodStr = getChildText(Constants.NS_MA3, ConfigConstants.TAG_C14N);
        if(c14nMethodStr != null)
            mC14nMethod = C14nMethod.resolve(c14nMethodStr);
    }

    public TimestampConfig(tr.gov.tubitak.uekae.esya.api.signature.config.TimestampConfig tc)
    {
        super(tc.getHost(), tc.getUserId(), tc.getPassword(), tc.getDigestAlg());
    }

    public TimestampConfig(String aHost, String aUserId, String aPassword, DigestAlg aDigestAlg) throws ConfigurationException
    {
        super(aHost, aUserId, aPassword, aDigestAlg);
    }

    public DigestMethod getDigestMethod()
    {
        return DigestMethod.resolveFromName(getDigestAlg());
    }

    public C14nMethod getC14nMethod()
    {
        return mC14nMethod;
    }

    public void setC14nMethod(C14nMethod aC14nMethod)
    {
        mC14nMethod = aC14nMethod;
    }


    /*
    private String mHost;
    private String mUserId;
    private String mPassword;
    private DigestAlg mDigestAlg = DigestAlg.SHA1;
    private DigestMethod mDigestMethod;

    public TimestampConfig(Element aElement) throws ConfigurationException
    {
        super(aElement);
        mHost = getChildText(Constants.NS_MA3, TAG_HOST);
        mUserId = getChildText(Constants.NS_MA3, TAG_USERID);
        mPassword = getChildText(Constants.NS_MA3, TAG_PASSWORD);

        String digestAlgStr = getChildText(Constants.NS_MA3, TAG_DIGESTALG);
        if (digestAlgStr != null) {
            mDigestAlg = DigestAlg.fromName(digestAlgStr);
        }
        try {
            mDigestMethod = DigestMethod.resolveFromName(mDigestAlg);
        }
        catch (Exception x) {
            throw new ConfigurationException("xml.WrongElement", "DigestAlg", I18n.translate("config"));
        }
    }

    public TimestampConfig(String aHost, String aUserId, String aPassword, DigestAlg aDigestAlg)
            throws ConfigurationException
    {
        mHost = aHost;
        mUserId = aUserId;
        mPassword = aPassword;
        mDigestAlg = aDigestAlg;
        try {
            mDigestMethod = DigestMethod.resolveFromName(mDigestAlg);
        }
        catch (Exception x) {
            throw new ConfigurationException("xml.WrongElement", "DigestAlg", I18n.translate("config"));
        }
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

    public DigestMethod getDigestMethod()
    {
        return mDigestMethod;
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

    public void setDigestMethod(DigestMethod mDigestMethod)
    {
        this.mDigestMethod = mDigestMethod;
    }

    public TSSettings getSettings()
    {
        if (mUserId != null && mUserId.trim().length() > 0)
            return new TSSettings(mHost, Integer.valueOf(mUserId), mPassword);
        else
            return new TSSettings(mHost);
    } */
}
