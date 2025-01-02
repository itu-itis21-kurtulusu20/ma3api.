using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;


	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// <p>When presenting signed data to a human user it may be important that there
	/// is no ambiguity as to the presentation of the signed data object to the
	/// relying party. In order for the appropriate representation (text, sound or
	/// video) to be selected by the relying party a content hint MAY be indicated
	/// by the signer. If a relying party system does not use the format
	/// specified to present the data object to the relying party, the electronic
	/// signature may not be valid. Such behaviour may have been established by the
	/// signature policy, for instance.
	/// 
	/// <p>The <code>DataObjectFormat</code> element provides information that
	/// describes the format of the signed data object. This element SHOULD be
	/// present when the signed data is to be presented to human users on
	/// validation if the presentation format is not implicit within the data that
	/// has been signed. This is a signed property that qualifies one specific
	/// signed data object. In consequence, an XML electronic signature aligned with
	/// the present document MAY contain more than one <code>DataObjectFormat</code>
	/// elements, each one qualifying one signed data object.
	/// 
	/// <p>Below follows the schema definition for this element.
	/// <pre>
	/// &lt;xsd:element name="DataObjectFormat" type="DataObjectFormatType"/>
	/// 
	/// &lt;xsd:complexType name="DataObjectFormatType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="Description" type="xsd:string" minOccurs="0"/>
	///     &lt;xsd:element name="ObjectIdentifier" type="ObjectIdentifierType"
	///                  minOccurs="0"/>
	///     &lt;xsd:element name="MimeType" type="xsd:string" minOccurs="0"/>
	///     &lt;xsd:element name="Encoding" type="xsd:anyURI" minOccurs="0"/>
	///   &lt;/xsd:sequence>
	///   &lt;xsd:attribute name="ObjectReference" type="xsd:anyURI" use="required"/>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// <p>The mandatory <code>ObjectReference</code> attribute MUST reference the
	/// <code>ds:Reference</code> element of the <code>ds:Signature</code>
	/// corresponding with the data object qualified by this property.
	/// 
	/// <p> This element can convey:
	/// <ul>
	/// <li>textual information related to the signed data object in element
	/// <code>Description</code>;
	/// <li>an identifier indicating the type of the signed data object in element
	/// <code>ObjectIdentifier</code>;
	/// <li>an indication of the MIME type of the signed data object in element
	/// <code>MimeType</code>;
	/// <li>an indication of the encoding format of the signed data object in
	/// element <code>Encoding</code>.
	/// </ul>
	/// <p>At least one element of <code>Description</code>,
	/// <code>ObjectIdentifier</code> and <code>MimeType</code> MUST be present
	/// within the property.
	/// 
	/// @author ahmety
	/// date: Sep 17, 2009
	/// </summary>
	public class DataObjectFormat : XAdESBaseElement
	{
		// mandatory attribute
		private readonly string mObjectReference;

		// one of Description, ObjectIdentifier and MimeType is mandatory
		private readonly string mDescription;
		private readonly ObjectIdentifier mObjectIdentifier;
		private readonly string mMIMEType;
		private readonly string mEncoding;

		// todo check if reference exists!
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public DataObjectFormat(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext, String aObjectReference, String aDescription, ObjectIdentifier aObjectIdentifier, String aMIMEType, String aEncoding) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public DataObjectFormat(Context aContext, string aObjectReference, string aDescription, ObjectIdentifier aObjectIdentifier, string aMIMEType, string aEncoding) : base(aContext)
		{
			addLineBreak();

			mObjectReference = aObjectReference;
			mDescription = aDescription;
			mObjectIdentifier = aObjectIdentifier;
			mMIMEType = aMIMEType;
			mEncoding = aEncoding;

			if (mObjectReference == null)
			{
				throw new XMLSignatureException("errors.null", "ObjectReference");
			}
			if ((mDescription == null) && (mObjectIdentifier == null) && (mMIMEType == null))
			{
				throw new XMLSignatureException("core.model.missingDataObjectFormatContent");

			}


            mElement.SetAttribute(Constants.ATTR_OBJECTREFERENCE,null, mObjectReference);

			if (mDescription != null)
			{
                insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_DESCRIPTION, mDescription);
			}
			if (mObjectIdentifier != null)
			{
				mElement.AppendChild(mObjectIdentifier.Element);
				addLineBreak();
			}
			if (mMIMEType != null)
			{
                insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_MIMETYPE, mMIMEType);
			}
			if (mEncoding != null)
			{
                insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_ENCODING, mEncoding);
			}
		}


		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public DataObjectFormat(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public DataObjectFormat(Element aElement, Context aContext) : base(aElement, aContext)
		{

			// manadatory reference attribute
            mObjectReference = mElement.GetAttribute(Constants.ATTR_OBJECTREFERENCE);
			if (mObjectReference == null || mObjectReference.Trim().Length < 1)
			{
				throw new XMLSignatureException("errors.null", "ObjectReference");
			}

            Element objIdElm = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_OBJECTIDENTIFIER);

            mDescription = getChildText(Constants.NS_XADES_1_3_2, Constants.TAGX_DESCRIPTION);
            mMIMEType = getChildText(Constants.NS_XADES_1_3_2, Constants.TAGX_MIMETYPE);
            mEncoding = getChildText(Constants.NS_XADES_1_3_2, Constants.TAGX_ENCODING);

			if (mDescription == null && objIdElm == null && mMIMEType == null)
			{
				throw new XMLSignatureException("core.model.missingDataObjectFormatContent");
			}

			if (objIdElm != null)
			{
				mObjectIdentifier = new ObjectIdentifier(objIdElm, mContext);
			}
		}

		public virtual string ObjectReference
		{
			get
			{
				return mObjectReference;
			}
		}

		public virtual string Description
		{
			get
			{
				return mDescription;
			}
		}

		public virtual ObjectIdentifier ObjectIdentifier
		{
			get
			{
				return mObjectIdentifier;
			}
		}

		public virtual string MIMEType
		{
			get
			{
				return mMIMEType;
			}
		}

		public virtual string Encoding
		{
			get
			{
				return mEncoding;
			}
		}

		public override string LocalName
		{
			get
			{
                return Constants.TAGX_DATAOBJECTFORMAT;
			}
		}

	}

}