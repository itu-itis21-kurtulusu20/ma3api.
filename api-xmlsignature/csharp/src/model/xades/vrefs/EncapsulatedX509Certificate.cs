using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using PKIEncodingType = tr.gov.tubitak.uekae.esya.api.xmlsignature.PKIEncodingType;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using EncapsulatedPKIData = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.EncapsulatedPKIData;
	using AsnUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.AsnUtil;
	using AttributeCertificate = tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;


	/// <summary>
	/// @author ahmety
	/// date: Jan 8, 2010
	/// </summary>
	public class EncapsulatedX509Certificate : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.EncapsulatedPKIData
	{

		public EncapsulatedX509Certificate(Context aContext, ECertificate aCertificate) : base(aContext)
		{
			Encoding = PKIEncodingType.DER;
			Value = aCertificate.getEncoded();
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public EncapsulatedX509Certificate(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext, tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate aCertificate) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public EncapsulatedX509Certificate(Context aContext, AttributeCertificate aCertificate) : base(aContext)
		{
			Encoding = PKIEncodingType.DER;
			Value = AsnUtil.encode(aCertificate, mEncoding);
		}

		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public EncapsulatedX509Certificate(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public EncapsulatedX509Certificate(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_ENCAPSULATEDX509CERTIFICATE;
			}
		}
	}

}