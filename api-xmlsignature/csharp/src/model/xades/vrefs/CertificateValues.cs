using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Logger = log4net.ILog;
	using Element = XmlElement;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using UnsignedSignaturePropertyElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;


	/// <summary>
	/// @author ahmety
	/// date: Dec 17, 2009
	/// 
	/// todo
	/// </summary>
	public class CertificateValues : CertificateValuesType, tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement
	{

		private static Logger logger = log4net.LogManager.GetLogger(typeof(CertificateValues));

		public CertificateValues(Context aContext) : base(aContext)
		{
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public CertificateValues(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext, tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate[] aCertificates) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public CertificateValues(Context aContext, ECertificate[] aCertificates) : base(aContext, aCertificates)
		{
		}

		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public CertificateValues(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public CertificateValues(Element aElement, Context aContext) : base(aElement, aContext)
		{
			if (logger.IsDebugEnabled)
			{
				logger.Debug("CERTIFICATE VALUES");
				for (int i = 0; i < CertificateCount; i++)
				{
					logger.Debug(getCertificate(i));
				}
			}

		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_CERTIFICATEVALUES;
			}
		}
	}

}