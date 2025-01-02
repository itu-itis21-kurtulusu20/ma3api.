using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model
{

	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// <dl>
	/// <dt>Identifier</dt>
	/// <dd><code>Type=<a id="Object" href="http://www.w3.org/2000/09/xmldsig#Object"
	/// name="Object" >"http://www.w3.org/2000/09/xmldsig#Object"</a></code>
	/// (this can be used within a <code>Reference</code> element to identify the
	/// referent's type)</dd>
	/// </dl>
	/// 
	/// <p><code>Object</code> is an optional element that may occur one
	/// or more times. When present, this element may contain any data.
	/// The <code>Object</code> element may include optional MIME type,
	/// ID, and encoding attributes.</p>
	/// 
	/// <p>The <code>Object</code>'s <code>Encoding</code> attributed may
	/// be used to provide a URI that identifies the method by which the
	/// object is encoded (e.g., a binary file).</p>
	/// 
	/// <p>The <code>MimeType</code> attribute is an optional attribute
	/// which describes the data within the <code>Object</code>
	/// (independent of its encoding). This is a string with values
	/// defined by [MIME]. For
	/// example, if the <code>Object</code> contains base64 encoded
	/// <a href="http://www.w3.org/Graphics/PNG/">PNG</a>,
	/// the <code>Encoding</code> may be specified as
	/// 'http://www.w3.org/2000/09/xmldsig#base64' and the
	/// <code>MimeType</code> as 'image/png'. This attribute is purely
	/// advisory; no validation of the <code>MimeType</code> information
	/// is required by this specification. Applications which require
	/// normative type and encoding information for signature validation
	/// should specify <code>Transforms</code> with well defined resulting types
	/// and/or encodings.</p>
	/// 
	/// <p>The <code>Object</code>'s <code>Id</code> is commonly
	/// referenced from a <code>Reference</code> in
	/// <code>SignedInfo</code>, or <code>Manifest</code>. This element
	/// is typically used for [enveloping signatures] where the
	/// object being signed is to be included in the signature element.
	/// The digest is calculated over the entire <code>Object</code> element
	/// including start and end tags.</p>
	/// 
	/// <p>Note, if the application wishes to exclude the
	/// <code>&lt;Object&gt;</code> tags from the digest calculation the
	/// <code>Reference</code> must identify the actual data object (easy
	/// for XML documents) or a transform must be used to remove the
	/// <code>Object</code> tags (likely where the data object is
	/// non-XML). Exclusion of the object tags may be desired for cases
	/// where one wants the signature to remain valid if the data object
	/// is moved from inside a signature to outside the signature (or
	/// vice versa), or where the content of the <code>Object</code> is
	/// an encoding of an original binary document and it is desired to
	/// extract and decode so as to sign the original bitwise
	/// representation.</p>
	/// 
	/// <p>The following schema fragment specifies the expected content contained
	/// within this class.
	/// 
	/// <pre>
	/// &lt;complexType name="ObjectType">
	///   &lt;complexContent>
	///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	///       &lt;sequence maxOccurs="unbounded" minOccurs="0">
	///         &lt;any processContents='lax'/>
	///       &lt;/sequence>
	///       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
	///       &lt;attribute name="MimeType" type="{http://www.w3.org/2001/XMLSchema}string" />
	///       &lt;attribute name="Encoding" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
	///     &lt;/restriction>
	///   &lt;/complexContent>
	/// &lt;/complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Jun 11, 2009
	/// </summary>
	public class XMLObject : Any
	{

		private string mMIMEType;
		private string mEncoding;


		public XMLObject(Context aBaglam) : base(aBaglam)
		{
		}

		/// <summary>
		///  Construct ds:Object from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public XMLObject(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public XMLObject(Element aElement, Context aContext) : base(aElement, aContext)
		{
		}

		public virtual string MIMEType
		{
			get
			{
				return mMIMEType;
			}
			set
			{
				mElement.SetAttribute(Constants.ATTR_MIMETYPE,null, value);
				mMIMEType = value;
			}
		}


		public virtual string Encoding
		{
			get
			{
				return mEncoding;
			}
			set
			{
				mElement.SetAttribute(Constants.ATTR_ENCODING,null,value);
				mEncoding = value;
			}
		}


		/// <summary>
		/// Add bytes as base64 to contents </summary>
		/// <param name="aBytes"> that will be added to content/s. </param>
		public override void addContentBase64(byte[] aBytes)
		{
			base.addContentBase64(aBytes);
			Encoding = "http://www.w3.org/2000/09/xmldsig#base64";
		}

		// base element
		public override string LocalName
		{
			get
			{
				return Constants.TAG_OBJECT;
			}
		}
	}

}