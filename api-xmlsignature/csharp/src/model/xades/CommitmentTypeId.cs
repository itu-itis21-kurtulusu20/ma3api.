using System.Collections.Generic;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;


	using Element = XmlElement;

	/// <summary>
	/// @author ahmety
	/// date: Sep 29, 2009
	/// </summary>
	public class CommitmentTypeId : ObjectIdentifier
	{

		public CommitmentTypeId(Context aBaglam, Identifier aIdentifier, string aDescription, IList<string> aDocumentationReferences) : base(aBaglam, aIdentifier, aDescription, aDocumentationReferences)
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
//ORIGINAL LINE: public CommitmentTypeId(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public CommitmentTypeId(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_COMMITMENTTYPEID;
			}
		}
	}

}