using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using UnsignedSignaturePropertyElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;

	/// <summary>
	/// This property contains the set of revocation data that have been used to
	/// validate the attribute certificate when present in the signature. Should any
	/// of the revocation data present within <code>RevocationValues</code> property
	/// have been used for validate the attribute certificate, they do not need to
	/// appear within the <code>AttributeRevocationValues</code>.
	/// 
	/// <p>If <code>AttributeRevocationRefs</code> and
	/// <code>AttributeRevocationValues</code> are present,
	/// <code>AttributeRevocationValues</code> and <code>RevocationValues</code> MUST
	/// contain the values of all the objects referenced in
	/// <code>AttributeRevocationRefs</code>.
	/// 
	/// <p>This is an optional unsigned property that qualifies the signature.
	/// 
	/// <p>There SHALL be at most one occurence of this property in the signature.
	/// 
	/// <p>Below follows the Schema definition for this element.
	/// 
	/// <pre>
	/// &lt;xsd:element name="AttributeRevocationValues" type="RevocationValuesType"/>
	/// </pre>
	/// @author ahmety
	/// date: Jan 6, 2010
	/// </summary>
	public class AttributeRevocationValues : RevocationValuesType, tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement
	{

		public AttributeRevocationValues(Context aContext) : base(aContext)
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
//ORIGINAL LINE: public AttributeRevocationValues(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public AttributeRevocationValues(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_ATTRIBUTEREVOCATIONVALUES;
			}
		}
	}

}