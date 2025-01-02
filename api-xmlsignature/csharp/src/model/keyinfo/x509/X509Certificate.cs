using System;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509
{

    using Logger = log4net.ILog;
    using Element = XmlElement;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	//using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n.core.utils.I18n;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
    using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;


    /// <summary>
    /// <code>X509Data</code> child element, which contains a base64-encoded
    /// [X509v3] certificate
    /// </summary>
    /// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data </seealso>
    /// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo
    /// @author ahmety
    /// date: Jun 16, 2009 </seealso>
    public class X509Certificate : X509DataElement
	{
		private static readonly Logger logger = log4net.LogManager.GetLogger(typeof(X509Certificate));

		private byte[] mCertificateBytes;
		private ECertificate mCertificate;


//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public X509Certificate(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aBaglam, byte[] aCertificateBytes) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public X509Certificate(Context aBaglam, byte[] aCertificateBytes) : base(aBaglam)
		{
			mCertificateBytes = aCertificateBytes;
            XmlCommonUtil.setBase64EncodedText(mElement, aCertificateBytes);
			try
			{
				mCertificate = new ECertificate(mCertificateBytes);
			}
			catch (Exception x)
			{
				logger.Error("Error resolving x509 certificate", x);
				throw new XMLSignatureException(x, "errors.cantDecode", "byte[]", I18n.translate("certificate"));
			}
		}

		public X509Certificate(Context aBaglam, ECertificate aCertificate) : base(aBaglam)
		{
			mCertificate = aCertificate;
		    mCertificateBytes = aCertificate.getEncoded();
            XmlCommonUtil.setBase64EncodedText(mElement, mCertificateBytes);
		}

		/// <summary>
		///  Construct X509Certificate from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
		public X509Certificate(Element aElement, Context aContext) : base(aElement, aContext)
		{
			mCertificateBytes = XmlCommonUtil.getBase64DecodedText(mElement);
			try
			{
				mCertificate = new ECertificate(mCertificateBytes);
			}
			catch (Exception x)
			{
				logger.Error("Error resolving x509 certificate", x);
				throw new XMLSignatureException(x, "errors.cantDecode", aElement.Name, I18n.translate("certificate"));
			}
		}


        public void setCertificateBytes(ECertificate aCertificate)
        {
            mCertificate = aCertificate;
            byte[] certBytes = aCertificate.getEncoded();
            XmlCommonUtil.setBase64EncodedText(mElement, certBytes);
            mCertificateBytes = certBytes;
        }
		public virtual byte[] CertificateBytes
		{
			get
			{
				return mCertificateBytes;
			}
		}

		public virtual ECertificate Certificate
		{
			get
			{
				return mCertificate;
			}
		}


		public override string ToString()
		{
			return mCertificate.ToString();
		}

		// base element
		public override string LocalName
		{
			get
			{
				return Constants.TAG_X509CERTIFICATE;
			}
		}
	}

}