using System.Collections.Generic;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy
{

	using ObjectIdentifier = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.ObjectIdentifier;
	using Identifier = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.Identifier;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;


	using Element = XmlElement;

	/// <summary>
	/// The <code>SigPolicyId</code> element contains an identifier that uniquely
	/// identifies a specific version of the signature policy.
	/// 
	/// @author ahmety
	/// date: Oct 15, 2009
	/// </summary>
	public class PolicyId : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.ObjectIdentifier
	{

		public PolicyId(Context aBaglam, Identifier aIdentifier, string aDescription, IList<string> aDocumentationReferences) : base(aBaglam, aIdentifier, aDescription, aDocumentationReferences)
		{
		}

		/// <summary>
		/// Construct ObjectIdentifier from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public PolicyId(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public PolicyId(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_SIGPOLICYID;
			}
		}
	}

}