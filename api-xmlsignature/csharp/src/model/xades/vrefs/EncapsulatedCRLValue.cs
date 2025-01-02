using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Element = XmlElement;
	using ECRL = tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using PKIEncodingType = tr.gov.tubitak.uekae.esya.api.xmlsignature.PKIEncodingType;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using XMLSignatureRuntimeException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureRuntimeException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using EncapsulatedPKIData = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.EncapsulatedPKIData;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

	/// 
	/// <summary>
	/// <code>EncapsulatedCRLValue</code> contains the base-64 encoding of a
	/// DER-encoded X.509 CRL.
	/// 
	/// @author ahmety
	/// date: Jan 8, 2010
	/// </summary>
	public class EncapsulatedCRLValue : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.EncapsulatedPKIData
	{

		public EncapsulatedCRLValue(Context aContext, ECRL aCrl) : base(aContext)
		{
			Encoding = PKIEncodingType.DER;
			Value = aCrl.getEncoded();
		}

		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public EncapsulatedCRLValue(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public EncapsulatedCRLValue(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		/// <summary>
		/// Useless method, dont need to set PKIEncodingType on EncapsulatedCRLValue
		/// becuase its has always "DER" encoding type.
		/// </summary>
		/// <param name="aEncoding"> </param>
		public override PKIEncodingType Encoding
		{
			set
			{
				if (value != PKIEncodingType.DER)
				{
					throw new XMLSignatureRuntimeException("core.model.invalidEncapsulatedEncoding", I18n.translate("CRL"));
				}
				base.Encoding = PKIEncodingType.DER;
			}
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_ENCAPSULATEDCRLVALUE;
			}
		}
	}

}