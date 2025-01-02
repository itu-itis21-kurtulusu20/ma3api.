using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Any = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Any;

	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Element = XmlElement;

	/// <summary>
	/// @author ahmety
	/// date: Sep 23, 2009
	/// </summary>
	public class CommitmentTypeQualifier : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Any
	{

		/// <summary>
		/// Construct new BaseElement according to context </summary>
		/// <param name="aContext"> where some signature spesific properties reside. </param>
		public CommitmentTypeQualifier(Context aContext) : base(aContext)
		{
		}

		/// <summary>
		/// Construct new CommitmentTypeQualifier according to context </summary>
		/// <param name="aContext"> where some signature spesific properties reside. </param>
		/// <param name="aText"> that defines commitment type.  </param>
		public CommitmentTypeQualifier(Context aContext, string aText) : base(aContext)
		{
			mElement.AppendChild(Document.CreateTextNode(aText));
		}


		/// <summary>
		/// Construct Any from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>

		public CommitmentTypeQualifier(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

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
                return Constants.TAGX_COMMITMENTTYPEQUALIFIER;
			}
		}
	}

}