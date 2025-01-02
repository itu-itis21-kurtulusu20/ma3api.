using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using IdGenerator = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.IdGenerator;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// <p><code>The SignedProperties</code> element contains a number of properties
	/// that are collectively signed by the XMLDSIG signature.
	/// 
	/// <p>The <code>SignedProperties</code> element MAY contain properties that
	/// qualify the XMLDSIG signature itself or the signer. If present, they are
	/// included as content of the <code>SignedSignatureProperties</code> element.
	/// 
	/// <p>NOTE: If the <code>ds:KeyInfo</code> element is build according as
	/// specified in clause 4.4.1, it could happen that no signed signature property
	/// is required, and no <code>SignedSignatureProperties</code> element would be
	/// needed in the XAdES signature.
	/// 
	/// <p>The <code>SignedProperties</code> element MAY also contain properties that
	/// qualify some of the signed data objects. These properties appear as content
	/// of the <code>SignedDataObjectProperties</code> element.
	/// 
	/// <p>The optional <code>Id</code> attribute can be used to make a reference to
	/// the <code>SignedProperties</code> element.
	/// 
	/// <p>Below follows the schema definition for SignedProperties element.
	/// 
	/// <p><pre>
	/// &lt;xsd:element name="SignedProperties" type="SignedPropertiesType" />
	/// 
	/// &lt;xsd:complexType name="SignedPropertiesType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="SignedSignatureProperties"
	///          type="SignedSignaturePropertiesType" minOccurs="0"/>
	///     &lt;xsd:element name="SignedDataObjectProperties"
	///          type="SignedDataObjectPropertiesType" minOccurs="0"/>
	///   &lt;/xsd:sequence>
	///   &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Jun 22, 2009
	/// </summary>
	public class SignedProperties : XAdESBaseElement
	{
		private SignedSignatureProperties mSignedSignatureProperties;
		private SignedDataObjectProperties mSignedDataObjectProperties;

		public SignedProperties(Context aBaglam) : base(aBaglam)
		{

			generateAndSetId(IdGenerator.TYPE_SIGNED_PROPERTIES);

			mSignedSignatureProperties = new SignedSignatureProperties(mContext);

			setupChildren();
		}

		/// <summary>
		///  Construct SignedProperties from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
		public SignedProperties(Element aElement, Context aContext) : base(aElement, aContext)
		{
            Element spe = selectChildElementXML(Constants.NS_XADES_PREFIX, Constants.TAGX_SIGNEDSIGNATUREPROPERTIES);
			if (spe != null)
			{
				mSignedSignatureProperties = new SignedSignatureProperties(spe, aContext);
			}

            Element sdop = selectChildElementXML(Constants.NS_XADES_PREFIX, Constants.TAGX_SIGNEDATAOBJECTPROPERIES);
			if (sdop != null)
			{
				mSignedDataObjectProperties = new SignedDataObjectProperties(sdop, aContext);
			}

			if (mId != null)
			{
                if (mElement.HasAttribute(Constants.ATTR_ID))
                {
                    mElement.RemoveAttribute(Constants.ATTR_ID);
                }
                mElement.SetAttribute(Constants.ATTR_ID,null, mId);
			}
		}

		public virtual SignedSignatureProperties SignedSignatureProperties
		{
			get
			{
				return mSignedSignatureProperties;
			}
			set
			{
				mSignedSignatureProperties = value;
				setupChildren();
			}
		}


		public virtual SignedDataObjectProperties SignedDataObjectProperties
		{
			get
			{
				return mSignedDataObjectProperties;
			}
			set
			{
				mSignedDataObjectProperties = value;
				setupChildren();
			}
		}

		public virtual SignedDataObjectProperties createOrGetSignedDataObjectProperties()
		{
			if (mSignedDataObjectProperties == null)
			{
				mSignedDataObjectProperties = new SignedDataObjectProperties(mContext);
				setupChildren();
			}
			return mSignedDataObjectProperties;
		}


		private void setupChildren()
		{
            XmlCommonUtil.removeChildren(mElement);
			addLineBreak();

			if (mSignedSignatureProperties != null)
			{
				mElement.AppendChild(mSignedSignatureProperties.Element);
				addLineBreak();
			}

			if (mSignedDataObjectProperties != null)
			{
				mElement.AppendChild(mSignedDataObjectProperties.Element);
				addLineBreak();
			}

			if (mId != null)
			{
                if (mElement.HasAttribute(Constants.ATTR_ID))
                {
                    mElement.RemoveAttribute(Constants.ATTR_ID);
                }
                mElement.SetAttribute(Constants.ATTR_ID,null, mId);
			}
		}

		// base element
		public override string LocalName
		{
			get
			{
                return Constants.TAGX_SIGNEDPROPERTIES;
			}
		}
	}

}