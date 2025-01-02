using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using UnsignedSignaturePropertyElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;
	using AttributeCertificate = tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate;

	/// <summary>
	/// This property contains the certificate values of the Attribute Authorities
	/// that have been used to validate the attribute certificate when present in the
	/// signature. Should any of the certificates present within
	/// <code>CertificateValues</code> property have been used for validate the
	/// attribute certificate, they do not need to appear within the
	/// <code>AttrAuthoritiesCertValues</code>.
	/// 
	/// <p>If <code>AttributeCertificateRefs</code> and
	/// <code>AttrAuthoritiesCertValues</code> are present,
	/// <code>AttrAuthoritiesCertValues</code> and <code>CertificateValues</code>
	/// properties MUST contain all the certificates referenced in
	/// <code>AttributeCertificateRefs</code>.
	/// 
	/// <p>This is an optional unsigned property that qualifies the signature.
	/// 
	/// <p>There SHALL be at most one occurence of this property in the signature.
	/// 
	/// <p>Below follows the Schema definition for this element.
	/// <pre>
	/// &lt;xsd:element name="AttrAuthoritiesCertValues" type="CertificateValuesType"/>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Jan 6, 2010
	/// </summary>
	public class AttrAuthoritiesCertValues : CertificateValuesType, tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement
	{

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public AttrAuthoritiesCertValues(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext, tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate[] aCertificates) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public AttrAuthoritiesCertValues(Context aContext, AttributeCertificate[] aCertificates) : base(aContext, aCertificates)
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
//ORIGINAL LINE: public AttrAuthoritiesCertValues(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public AttrAuthoritiesCertValues(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_ATTRAUHORITIESCERTVALUES;
			}
		}
	}

}