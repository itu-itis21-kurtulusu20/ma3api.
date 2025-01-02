using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Element = XmlElement;
	using UnsignedSignaturePropertyElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;

	/// <summary>
	/// This clause defines the <code>AttributeRevocationRefs</code> element able to
	/// carry the references to the full set of revocation data that have been used
	/// in the validation of the attribute certificate(s) present in the signature.
	/// This is an unsigned property that qualifies the signature.
	/// 
	/// <p>This property MAY be used only when a user attribute certificate is
	/// present in the signature within the signature.
	/// 
	/// <p>There SHALL be at most one occurence of this property in the signature.
	/// 
	/// <p>Below follows the schema definition for this element.
	/// <pre>
	/// &lt;xsd:element name="AttributeRevocationRefs" type="CompleteRevocationRefsType"/>
	/// </pre>
	/// 
	/// <p>NOTE: Copies of the revocation values referenced in this property may be
	/// held using the <code>AttributeRevocationValues</code> property.
	/// 
	/// @author ahmety
	/// date: Nov 9, 2009
	/// </summary>
	public class AttributeRevocationRefs : CompleteRevocationRefs
	{

		public AttributeRevocationRefs(Context aContext) : base(aContext)
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
//ORIGINAL LINE: public AttributeRevocationRefs(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public AttributeRevocationRefs(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_ATTRIBUTEREVOCATIONREFS;
			}
		}
	}

}