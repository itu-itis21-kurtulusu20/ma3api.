using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model
{

	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
    using Element = XmlElement;
    using Node = XmlNode;
    using NodeList = XmlNodeList;


	/// <summary>
	/// The <code>AnyType</code> Schema data type has a content model that allows a
	/// sequence of arbitrary XML elements that (mixed with text) is of unrestricted
	/// length. It also allows for text content only. Additionally, an element of
	/// this data type can bear an unrestricted number of arbitrary attributes. It
	/// is used throughout the remaining parts of this clause wherever the content
	/// of an XML element has been left open.
	/// 
	/// <pre>
	/// &lt;xsd:complexType name="AnyType" mixed="true">
	///   &lt;xsd:sequence minOccurs="0" maxOccurs="unbounded">
	///     &lt;xsd:any namespace="##any" processContents="lax"/>
	///   &lt;/xsd:sequence>
	///   &lt;xsd:anyAttribute namespace="##any"/>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Sep 18, 2009
	/// </summary>
	public abstract class Any : BaseElement
	{

		/// <summary>
		/// Construct new BaseElement according to context </summary>
		/// <param name="aContext"> where some signature spesific properties reside. </param>
		protected internal Any(Context aContext) : base(aContext)
		{
		}

		/// <summary>
		/// Construct Any from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>

		protected internal Any(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		public virtual NodeList Content
		{
			get
			{
				return mElement.ChildNodes;
			}
		}

		/// <summary>
		/// Add String to contents
		/// </summary>
		/// <param name="aContent"> that will be added to content/s. </param>
		public virtual void addContent(string aContent)
		{
			mElement.AppendChild(Document.CreateTextNode(aContent));
			//addLineBreak();
		}

		/// <summary>
		/// Add bytes as base64 to contents
		/// </summary>
		/// <param name="aBytes"> that will be added to content/s. </param>
		public virtual void addContentBase64(byte[] aBytes)
		{
            XmlCommonUtil.setBase64EncodedText(mElement, aBytes);
			addLineBreak();
		}

		/// <summary>
		/// Add element to contents
		/// </summary>
		/// <param name="aElement"> that will be added to content/s. </param>
		public virtual void addContent(BaseElement aElement)
		{
			mElement.AppendChild(aElement.Element);
			addLineBreak();
		}

		/// <summary>
		/// Add node to contents
		/// </summary>
		/// <param name="aNode"> that will be added to content/s. </param>
		public virtual void addContent(Node aNode)
		{
			mElement.AppendChild(Document.ImportNode(aNode, true));
			addLineBreak();
		}

	}

}