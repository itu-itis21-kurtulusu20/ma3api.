using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using PKIEncodingType = tr.gov.tubitak.uekae.esya.api.xmlsignature.PKIEncodingType;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
    using Element = XmlElement;

	/// <summary>
	/// <p>The <code>EncapsulatedPKIDataType</code> is used to incorporate non XML
	/// pieces of PKI data into an XML structure. Examples of such PKI data that are
	/// widely used at the time being include X.509 certificates and revocation
	/// lists, OCSP responses, attribute certificates and time-stamp tokens.
	/// 
	/// <p>
	/// <pre>
	/// <xsd:element name="EncapsulatedPKIData" type="EncapsulatedPKIDataType"/>
	/// 
	/// <xsd:complexType name="EncapsulatedPKIDataType">
	///   <xsd:simpleContent>
	///     <xsd:extension base="xsd:base-64Binary">
	///       <xsd:attribute name="Id" type="xsd:ID" use="optional"/>
	///       <xsd:attribute name="Encoding" type="xsd:anyURI" use="optional"/>
	///     </xsd:extension>
	///   </xsd:simpleContent>
	/// </xsd:complexType>
	/// </pre>
	/// 
	/// <p>The content of this data type is the piece of PKI data, base-64 encoded.
	/// 
	/// <p>For encoding types see <code><seealso cref="PKIEncodingType"/></code>
	/// 
	/// <p>If the Encoding attribute is not present, then it is assumed that the PKI
	/// data is ASN.1 data encoded in DER.
	/// 
	/// <p>NOTE: The present document restricts the encoding options to only one for
	/// certain types of the aforementioned PKI data in those sections that specify
	/// XAdES properties related to these data.
	/// 
	/// <p>The optional ID attribute can be used to make a reference to an element
	/// of this data type.
	/// 
	/// @author ahmety
	/// date: Sep 29, 2009
	/// </summary>
	public abstract class EncapsulatedPKIData : XAdESBaseElement
	{
		protected internal PKIEncodingType mEncoding;
		protected internal byte[] mValue;

		public EncapsulatedPKIData(Context aContext) : base(aContext)
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
//ORIGINAL LINE: public EncapsulatedPKIData(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public EncapsulatedPKIData(Element aElement, Context aContext) : base(aElement, aContext)
		{
			mValue = XmlCommonUtil.getBase64DecodedText(mElement);
			mEncoding = PKIEncodingType.resolve(mElement.GetAttribute(Constants.ATTR_ENCODING));
		}

		public virtual PKIEncodingType Encoding
		{
			get
			{
				return mEncoding;
			}
			set
			{
				mEncoding = value;
                mElement.SetAttribute(Constants.ATTR_ENCODING, null, value.URI);
			}
		}


		public virtual byte[] Value
		{
			get
			{
				return mValue;
			}
			set
			{
				mValue = value;
                XmlCommonUtil.setBase64EncodedText(mElement, value);
			}
		}

	}

}