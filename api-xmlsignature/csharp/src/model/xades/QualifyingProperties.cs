using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignature = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

    /// <summary>
	/// <p>The <code>QualifyingProperties</code> element acts as a container element
	/// for all the  qualifying information that should be added to an XML signature.
	/// 
	/// <p>The qualifying properties are split into properties that are
	/// cryptographically bound to (i.e. signed by) the XML signature
	/// (<code>SignedProperties</code>), and properties that are not
	/// cryptographically bound tothe XML signature
	/// (<code>UnsignedProperties</code>). The <code>SignedProperties</code> MUST be
	/// covered by a <code>ds:Reference</code> element of the XML signature.
	/// 
	/// <p>The mandatory <code>Target</code> attribute MUST refer to the Id attribute
	/// of the corresponding <code>ds:Signature</code>. Its value MUST be an URI
	/// with a bare-name XPointer fragment. When this element is enveloped by the
	/// XAdES signature, its not-fragment part MUST be empty. Otherwise, its
	/// not-fragment part MAY NOT be empty.
	/// 
	/// <p>The optional <code>Id</code> attribute can be used to make a reference to
	/// the <code>QualifyingProperties</code> container.
	/// 
	/// <p>It is strongly recommended not to include empty
	/// <code>xades:SignedProperties</code> or empty
	/// <code>xades:UnsignedProperties</code> elements within the signature.
	/// Applications verifying XAdES signatures MUST ignore empty
	/// <code>xades:SignedProperties</code> and empty
	/// <code>xades:UnsignedProperties</code> elements.
	/// 
	/// <p>The element has the following structure.
	/// <p><pre>
	/// &lt;xsd:element name="QualifyingProperties" type="QualifyingPropertiesType"/>
	/// 
	/// &lt;xsd:complexType name="QualifyingPropertiesType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="SignedProperties" type="SignedPropertiesType" minOccurs="0"/>
	///     &lt;xsd:element name="UnsignedProperties" type="UnsignedPropertiesType" minOccurs="0"/>
	///   &lt;/xsd:sequence>
	///   &lt;xsd:attribute name="Target" type="xsd:anyURI" use="required"/>
	///   &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Jun 22, 2009
	/// 
	/// </summary>
	public class QualifyingProperties : XAdESBaseElement
	{
		private readonly SignedProperties mSignedProperties;
		private UnsignedProperties mUnsignedProperties;

		private string mTarget;
		private readonly XMLSignature mSignature;

		public QualifyingProperties(XMLSignature aSignature, Context aContext) : base(aContext)
		{

			aSignature.QualifyingProperties = this;
            
            string xmlnsXAdESPrefix = aContext.Config.NsPrefixMap.getPrefix(Constants.NS_XADES_1_3_2);

            mElement.SetAttribute("xmlns:" +string.Intern(xmlnsXAdESPrefix),Constants.NS_XADES_1_3_2);

			Target = "#" + aSignature.Id;

			mSignedProperties = new SignedProperties(aContext);

			addLineBreak();
			mElement.AppendChild(mSignedProperties.Element);
			addLineBreak();

			mSignature = aSignature;
		}

		/// <summary>
		///  Construct QualifyingProperties from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
		public QualifyingProperties(Element aElement, Context aContext, XMLSignature aSignature) : base(aElement, aContext)
		{
			aSignature.QualifyingProperties = this;

			string xadesPrefix = aElement.Prefix;
            aContext.Config.NsPrefixMap.setPrefix(Constants.NS_XADES_1_3_2, xadesPrefix);
            
            XmlNameTable nsTable = aElement.OwnerDocument.NameTable;
            XmlNamespaceManager nsManager = new XmlNamespaceManager(nsTable);
            nsManager.AddNamespace("ds", Constants.NS_XMLDSIG);
            nsManager.AddNamespace("xades", Constants.NS_XADES_1_3_2);
            XmlElement signedPropsElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_SIGNEDPROPERTIES);
		    //Element signedPropsElement = (XmlElement)aElement.SelectSingleNode("//xades:" + Constants.TAGX_SIGNEDPROPERTIES, nsManager);
            if (signedPropsElement != null)
            {
                mContext.Document = mElement.OwnerDocument;
				mSignedProperties = new SignedProperties(signedPropsElement, mContext);
			}

            Element unsignedPropsElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_UNSIGNEDPROPERTIES);
			if (unsignedPropsElement != null)
			{
                mContext.Document = mElement.OwnerDocument;
				mUnsignedProperties = new UnsignedProperties(unsignedPropsElement, mContext, aSignature);
			}

            // signature:id - qualifying properties:target check
            // simdilik bunu istemediler
            //if (!aSignature.Id.Equals(this.Element.GetAttribute("Target").Substring(1)))
            //    throw new XMLSignatureException("Qualifying Properties Target does not match Signature Id.");

		}

		public virtual SignedProperties SignedProperties
		{
			get
			{
				return mSignedProperties;
			}
		}


		public virtual UnsignedProperties UnsignedProperties
		{
			get
			{
				return mUnsignedProperties;
			}
		}

		public virtual UnsignedProperties createOrGetUnsignedProperties()
		{
			if (mUnsignedProperties == null)
			{
			    //mContext.Document = mDocument;
				mUnsignedProperties = new UnsignedProperties(mContext, mSignature);

                //necessary for crossing XmlDocument contexts
                //Hata oluþtuðundan c# kýsmýnda bu þekilde yapýldý.
                //XmlNode importNode = mElement.OwnerDocument.ImportNode(mUnsignedProperties.Element, true);
			    //XmlNode appendChild = mElement.AppendChild(importNode);
                //mUnsignedProperties.Element = (XmlElement)importNode;
                mElement.AppendChild(mUnsignedProperties.Element);
			    addLineBreak();

			}
			return mUnsignedProperties;
		}

		public virtual string Target
		{
			get
			{
				return mTarget;
			}
			set
			{
                // eski 'Target' attribute'unu remove etmeyince bir tane daha ekliyor
                mElement.RemoveAttribute(Constants.ATTR_TARGET);
                mElement.SetAttribute(Constants.ATTR_TARGET,null, value);
				mTarget = value;
			}
		}


		public virtual UnsignedSignatureProperties UnsignedSignatureProperties
		{
			get
			{
				if (mUnsignedProperties != null)
				{
					return mUnsignedProperties.UnsignedSignatureProperties;
				}
				return null;
			}
		}

		public virtual UnsignedDataObjectProperties UnsignedDataObjectProperties
		{
			get
			{
				if (mUnsignedProperties != null)
				{
					return mUnsignedProperties.UnsignedDataObjectProperties;
				}
				return null;
			}
		}

		public virtual SignedSignatureProperties SignedSignatureProperties
		{
			get
			{
				if (mSignedProperties != null)
				{
					return mSignedProperties.SignedSignatureProperties;
				}
				return null;
    
			}
		}

		public virtual SignedDataObjectProperties SignedDataObjectProperties
		{
			get
			{
				if (mSignedProperties != null)
				{
					return mSignedProperties.SignedDataObjectProperties;
				}
				return null;
			}
		}

		// base element
		public override string LocalName
		{
			get
			{
                return Constants.TAGX_QUALIFYINGPROPERTIES;
			}
		}

	}

}