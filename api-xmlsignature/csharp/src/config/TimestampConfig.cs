using System;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.config
{

	using Element = XmlElement;
	using DigestAlg = tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
	using TSSettings = tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
	using DigestMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
	//using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n.core.utils.I18n;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// @author ayetgin
	/// @deprecated
	/// @see tr.gov.tubitak.uekae.esya.api.signature.config.TimestampConfig
	/// </summary>
	
    [Obsolete]
	public class TimestampConfig : tr.gov.tubitak.uekae.esya.api.signature.config.TimestampConfig
	{
        // bu degisken timestamp'in baska c14n'lere sahip olabilmesi icin duzenlenmistir
        private C14nMethod mC14nMethod = null;

        public TimestampConfig(Element aElement) : base(aElement)
        {
            // burasi de bu c14n'i config'den okumaya calisiyor, bulursa set ediyor
            String c14nMethodStr = getChildText(Constants.NS_MA3, ConfigConstants.TAG_C14N);
            if (c14nMethodStr != null)
                mC14nMethod = C14nMethod.resolve(c14nMethodStr);
        }

        public TimestampConfig(tr.gov.tubitak.uekae.esya.api.signature.config.TimestampConfig tc)
            : base(tc.getHost(), tc.getUserId(), tc.getPassword(), tc.getDigestAlg())
        {
            
        }

        public TimestampConfig(String aHost, String aUserId, String aPassword, DigestAlg aDigestAlg)
            : base(aHost, aUserId, aPassword, aDigestAlg)
        {
            
        }

	    public virtual DigestMethod DigestMethod
	    {
	        get { return DigestMethod.resolveFromName(getDigestAlg()); }
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
		private string mHost;
		private string mUserId;
		private string mPassword;
		private DigestAlg mDigestAlg = DigestAlg.SHA1;
		private DigestMethod mDigestMethod;

		public TimestampConfig(Element aElement) : base(aElement)
		{
			mHost = getChildText(Constants.NS_MA3, ConfigConstants.TAG_HOST);
			mUserId = getChildText(Constants.NS_MA3, ConfigConstants.TAG_USERID);
			mPassword = getChildText(Constants.NS_MA3, ConfigConstants.TAG_PASSWORD);

			string digestAlgStr = getChildText(Constants.NS_MA3, ConfigConstants.TAG_DIGESTALG);
            if (digestAlgStr.CompareTo("SHA-1") == 0)
            {
                digestAlgStr = "SHA1";
            }
			if (digestAlgStr != null)
			{
				mDigestAlg = DigestAlg.fromName(digestAlgStr);
			}
			try
			{
				mDigestMethod = DigestMethod.resolveFromName(mDigestAlg);
			}
			catch (Exception exc)
			{
				throw new ConfigurationException(exc,"xml.WrongElement", "DigestAlg", I18n.translate("config"));
			}
		}

		public TimestampConfig(string aHost, string aUserId, string aPassword, DigestAlg aDigestAlg)
		{
			mHost = aHost;
			mUserId = aUserId;
			mPassword = aPassword;
			mDigestAlg = aDigestAlg;
			try
			{
				mDigestMethod = DigestMethod.resolveFromName(mDigestAlg);
			}
			catch (Exception exc)
			{
				throw new ConfigurationException(exc,"xml.WrongElement", "DigestAlg", I18n.translate("config"));
			}
		}

		public virtual string Host
		{
			get
			{
				return mHost;
			}
			set
			{
				this.mHost = value;
			}
		}

		public virtual string UserId
		{
			get
			{
				return mUserId;
			}
			set
			{
				this.mUserId = value;
			}
		}

		public virtual string Password
		{
			get
			{
				return mPassword;
			}
			set
			{
				this.mPassword = value;
			}
		}

		public virtual DigestAlg DigestAlg
		{
			get
			{
				return mDigestAlg;
			}
			set
			{
				this.mDigestAlg = value;
			}
		}

		public virtual DigestMethod DigestMethod
		{
			get
			{
				return mDigestMethod;
			}
			set
			{
				this.mDigestMethod = value;
			}
		}






		public virtual TSSettings Settings
		{
			get
			{
				if (mUserId != null && mUserId.Trim().Length > 0)
				{
					return new TSSettings(mHost, Convert.ToInt32(mUserId), mPassword);
				}
				else
				{
					return new TSSettings(mHost);
				}
			}
		}
         */
	}

}