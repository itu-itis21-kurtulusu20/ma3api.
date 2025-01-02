using System;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Element = XmlElement;
	using EOCSPResponse = tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using PKIEncodingType = tr.gov.tubitak.uekae.esya.api.xmlsignature.PKIEncodingType;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using EncapsulatedPKIData = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.EncapsulatedPKIData;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

	/// <summary>
	/// The <code>EncapsulatedOCSPValue</code> element contains the base-64 encoding
	/// of a DER-encoded <code>OCSPResponse</code> defined in RFC 2560.
	/// 
	/// @author ahmety
	/// date: Jan 8, 2010
	/// </summary>
	public class EncapsulatedOCSPValue : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.EncapsulatedPKIData
	{

		public EncapsulatedOCSPValue(Context aContext, EOCSPResponse aOCSPResponse) : base(aContext)
		{
			Encoding = PKIEncodingType.DER;
		    Value = aOCSPResponse.getEncoded();
		}

		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public EncapsulatedOCSPValue(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public EncapsulatedOCSPValue(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		/// <summary>
		/// Useless method, dont need to set PKIEncodingType on EncapsulatedOCSPValue
		/// becuase its has always "DER" encoding type.
		/// </summary>
		/// <param name="aEncoding"> </param>
		public override PKIEncodingType Encoding
		{
			set
			{
				if (value != PKIEncodingType.DER)
				{
					throw new Exception(I18n.translate("core.model.invalidEncapsulatedEncoding", I18n.translate("OCSP")));
				}
				base.Encoding = PKIEncodingType.DER;
			}
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_ENCAPSULATEDOCSPVALUE;
			}
		}
	}

}