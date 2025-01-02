using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using BaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;

	/// <summary>
	/// @author ahmety
	/// date: Jun 22, 2009
	/// </summary>
	public abstract class XAdESBaseElement : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement
	{

		public XAdESBaseElement(Context aContext) : base(aContext)
		{
		}

	   /// <summary>
	   ///  Construct XADESBaseElement from existing </summary>
	   ///  <param name="aElement"> xml element </param>
	   ///  <param name="aContext"> according to context </param>
	   ///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
	   ///      resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public XAdESBaseElement(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public XAdESBaseElement(Element aElement, Context aContext) : base(aElement, aContext)
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

	}

}