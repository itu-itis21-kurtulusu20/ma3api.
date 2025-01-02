using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp
{

	using Any = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Any;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Element = XmlElement;

	/// <summary>
	/// @author ahmety
	/// date: Sep 29, 2009
	/// </summary>
	public class XMLTimeStamp : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Any
	{

		/// <summary>
		/// Construct new BaseElement according to context </summary>
		/// <param name="aContext"> where some signature spesific properties reside. </param>
		public XMLTimeStamp(Context aContext) : base(aContext)
		{
		}

		/// <summary>
		/// Construct Any from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public XMLTimeStamp(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public XMLTimeStamp(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_XMLTIMESTAMP;
			}
		}
	}

}