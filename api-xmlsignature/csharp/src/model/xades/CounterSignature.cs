using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    using Element = XmlElement;

	/// <summary>
	/// <p>The <code>CounterSignature</code> is an unsigned property that qualifies
	/// the signature. A XAdES signature MAY have more than one
	/// <code>CounterSignature</code> properties. As indicated by its name, it
	/// contains one countersignature of the qualified signature.
	/// 
	/// <p>The following schema fragment specifies the expected content contained
	/// within this class.
	/// 
	/// <pre>
	/// &lt;xsd:element name="CounterSignature" type="CounterSignatureType" />
	/// 
	/// &lt;xsd:complexType name="CounterSignatureType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element ref="ds:Signature"/>
	///   &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Sep 3, 2009
	/// </summary>
	public class CounterSignature : XAdESBaseElement, UnsignedSignaturePropertyElement
	{
		private readonly XMLSignature mSignature;

		public CounterSignature(Context aBaglam, XMLSignature aSignature) : base(aBaglam)
		{
			mSignature = aSignature;
            
			addLineBreak();

            // xImportDoc is the XmlDocument to be imported.
            // xTargetNode is the XmlNode into which the import is to be done.

            //XmlNode xChildNode = mElement.OwnerDocument.ImportNode(aSignature.Element, true);
		    //mSignature.Element = (XmlElement) xChildNode;
		    //mSignature.Document = mDocument;
            mElement.AppendChild(aSignature.Element);
			addLineBreak();
		}

		/// <summary>
		///  Construct CounterSignature from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
		public CounterSignature(Element aElement, Context aContext) : base(aElement, aContext)
		{
			Element signatureElm = XmlCommonUtil.getNextElement(aElement.FirstChild);
			if (signatureElm == null)
			{
                throw new XMLSignatureException("xml.WrongContent", Constants.TAG_SIGNATURE, Constants.TAGX_COUNTERSIGNATURE);
			}
			mSignature = new XMLSignature(signatureElm, aContext);
		}

		public virtual XMLSignature Signature
		{
			get
			{
				return mSignature;
			}
		}

		public override string LocalName
		{
			get
			{
                return Constants.TAGX_COUNTERSIGNATURE;
			}
		}
	}

}