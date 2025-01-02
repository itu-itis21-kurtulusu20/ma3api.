using System;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;

namespace tr.gov.tubitak.uekae.esya.api.signature.config
{
    public class TimestampConfig : BaseConfigElement
    {
        private String mHost;
        private String mUserId;
        private String mPassword;
        private DigestAlg mDigestAlg = DigestAlg.SHA256;

        public TimestampConfig(XmlElement aElement)
            : base(aElement)
        {
            mHost = getChildText(ConfigConstants.NS_MA3, ConfigConstants.TAG_HOST);
            mUserId = getChildText(ConfigConstants.NS_MA3, ConfigConstants.TAG_USERID);
            mPassword = getChildText(ConfigConstants.NS_MA3, ConfigConstants.TAG_PASSWORD);

            String digestAlgStr = getChildText(ConfigConstants.NS_MA3, ConfigConstants.TAG_DIGESTALG);
            
            // smartcard.dll'in crypto.dll'e bagliligini kaldirmak icin SHA-1 SHA1 yapilmis
            // o sebeple buraya bir kontrol yazmak icap etti
            // ayrintili bilgi icin tarihi osmani mecmuasinin DigestAlg sinifina bakilabilir
 
            if (digestAlgStr.CompareTo("SHA-1") == 0)
            {
                digestAlgStr = "SHA1";
            }
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

        public TSSettings Settings
        {
            get
            {
                if (mUserId != null && mUserId.Trim().Length > 0)
                {
                    int userIdInt = 0;
                    if (int.TryParse(mUserId, out userIdInt) == false)
                    {
                        throw new ESYAException("Time Stamp User ID must be integer");
                    }
                    return new TSSettings(mHost, userIdInt, mPassword, mDigestAlg);
                }
                else
                    return new TSSettings(mHost);
            }
        }
    }
}
