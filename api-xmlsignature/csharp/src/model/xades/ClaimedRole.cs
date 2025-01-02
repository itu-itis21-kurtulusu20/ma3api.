using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Any = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Any;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Element = XmlElement;

	/// <summary>
	/// <p>The signer MAY state his own role without any certificate to corroborate 
	/// this claim, in which case the claimed role can be added to the signature
	/// as a signed qualifying property.
	/// 
	/// @author ahmety
	/// date: Sep 23, 2009
	/// </summary>
	public class ClaimedRole : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Any
	{

		/// <summary>
		/// Construct new BaseElement according to context </summary>
		/// <param name="aContext"> where some signature spesific properties reside. </param>
		public ClaimedRole(Context aContext) : base(aContext)
		{
		}

		/// <summary>
		/// Construct new ClaimedRole according to context </summary>
		/// <param name="aContext"> where some signature spesific properties reside. </param>
		/// <param name="aRoleName"> role as text </param>
		public ClaimedRole(Context aContext, string aRoleName) : base(aContext)
		{
			mElement.AppendChild(Document.CreateTextNode(aRoleName));
		}

		/// <summary>
		/// Construct Any from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>

		public ClaimedRole(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		// base element

		public override string Namespace
		{
			get
			{
				return Constants.NS_XADES_1_3_2;
			}
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_CLAIMEDROLE;
			}
		}
	}

}