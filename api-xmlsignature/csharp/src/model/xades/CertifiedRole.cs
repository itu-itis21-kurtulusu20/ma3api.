using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using PKIEncodingType = tr.gov.tubitak.uekae.esya.api.xmlsignature.PKIEncodingType;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using AsnUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.AsnUtil;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
    using AttributeCertificate = tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate;

	/// <summary>
	/// The <code>CertifiedRole</code> element contains the base-64 encoding of
	/// DER-encoded attribute certificates for the signer.
	/// 
	/// @author ahmety
	/// date: Sep 14, 2009
	/// </summary>
	public class CertifiedRole : EncapsulatedPKIData
	{

		private readonly AttributeCertificate mCertificate;


//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public CertifiedRole(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aBaglam, tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate aCertificate) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public CertifiedRole(Context aBaglam, AttributeCertificate aCertificate) : this(aBaglam, aCertificate, null)
		{
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public CertifiedRole(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aBaglam, tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate aCertificate, tr.gov.tubitak.uekae.esya.api.xmlsignature.PKIEncodingType aEncoding) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public CertifiedRole(Context aBaglam, AttributeCertificate aCertificate, PKIEncodingType aEncoding) : base(aBaglam)
		{

			mEncoding = (aEncoding == null) ? Constants.DEFAULT_PKI_ENCODING : aEncoding;

			mCertificate = aCertificate;

			Value = AsnUtil.encode(mCertificate, mEncoding);

			mElement.SetAttribute(Constants.ATTR_ENCODING,null, mEncoding.URI);

		}

		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public CertifiedRole(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public CertifiedRole(Element aElement, Context aContext) : base(aElement, aContext)
		{

			byte[] roleBytes = XmlCommonUtil.getBase64DecodedText(mElement);
			string encodingURI = mElement.GetAttribute(Constants.ATTR_ENCODING);

			PKIEncodingType encoding = PKIEncodingType.resolve(encodingURI);

			mCertificate = new AttributeCertificate();

			AsnUtil.decode(mCertificate, roleBytes, encoding);
		}

		public override PKIEncodingType Encoding
		{
			get
			{
				return mEncoding;
			}
		}

		public virtual AttributeCertificate Certificate
		{
			get
			{
				return mCertificate;
			}
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_CERTIFIEDROLE;
			}
		}
	}

}