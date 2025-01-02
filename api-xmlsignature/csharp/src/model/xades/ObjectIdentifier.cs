using System.Collections.Generic;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Element = XmlElement;


	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// <p>The <code>ObjectIdentifier</code> data type can be used to identify a
	/// particular data object. It allows the specification of a unique and
	/// permanent identifier of an object. In addition, it may also contain,
	/// a textual description of the nature of the data object, and a number of
	/// references to documents where additional information about the nature of the
	/// data object can be found.
	/// 
	/// <p><pre>
	/// &lt;xsd:complexType name="ObjectIdentifierType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="Identifier" type="IdentifierType"/>
	///     &lt;xsd:element name="Description" type="xsd:string" minOccurs="0"/>
	///     &lt;xsd:element name="DocumentationReferences"
	///                  type="DocumentationReferencesType" minOccurs="0"/>
	///   &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// <p>The Identifier element contains a permanent identifier. Once the
	/// identifier is assigned, it can never be re-assigned again. It supports both
	/// the mechanism that is used to identify objects in ASN.1 and the mechanism
	/// that is usually used to identify objects in an XML environment:
	/// <ul>
	/// <li>in a XML environment objects are typically identified by means of a
	/// Uniform Resource Identifier, URI. In this case, the content of
	/// <code>Identifier</code> consists of the identifying URI, and the optional
	/// </code>Qualifier</code> attribute does not appear;
	/// <li>in ASN.1 an Object IDentifier (OID) is used to identify an object.
	/// To support an OID, the content of Identifier consists of an OID, either
	/// encoded as Uniform Resource Name (URN) or as Uniform Resource Identifier
	/// (URI). The optional <code>Qualifier</code> attribute can be used to provide
	/// a hint about the applied encoding (values OIDAsURN or OIDAsURI).
	/// </ul>
	/// <p>Should an OID and an URI exist identifying the same object, the present
	/// document encourages the use of the URI as explained in the first bullet
	/// above.
	/// 
	/// <p><pre>
	/// &lt;xsd:complexType name="IdentifierType">
	///   &lt;xsd:simpleContent>
	///     &lt;xsd:extension base="xsd:anyURI">
	///       &lt;xsd:attribute name="Qualifier" type="QualifierType" use="optional"/>
	///     &lt;/xsd:extension>
	///   &lt;/xsd:simpleContent>
	/// &lt;/xsd:complexType>
	/// 
	/// &lt;xsd:simpleType name="QualifierType">
	///   &lt;xsd:restriction base="xsd:string">
	///     &lt;xsd:enumeration value="OIDAsURI"/>
	///     &lt;xsd:enumeration value="OIDAsURN"/>
	///   &lt;/xsd:restriction>
	/// &lt;/xsd:simpleType>
	/// </pre>
	/// 
	/// <p>The optional <code>Description</code> element contains an informal text
	/// describing the object identifier.
	/// 
	/// <p>The optional <code>DocumentationReferences</code> element consists of an
	/// arbitrary number of references pointing to further explanatory documentation
	/// of the object identifier.
	/// <p><pre>
	/// &lt;xsd:complexType name="DocumentationReferencesType">
	///   &lt;xsd:sequence maxOccurs="unbounded">
	///   &lt;xsd:element name="DocumentationReference" type="xsd:anyURI"/>
	/// &lt;/xsd:sequence>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Sep 17, 2009
	/// </summary>
	public class ObjectIdentifier : XAdESBaseElement
	{
		private readonly Identifier mIdentifier;
		private readonly string mDescription;
		private readonly IList<string> mDocumentationReferences = new List<string>();

		public ObjectIdentifier(Context aBaglam, Identifier aIdentifier, string aDescription, IList<string> aDocumentationReferences) : base(aBaglam)
		{
			mIdentifier = aIdentifier;
			mDescription = aDescription;
			mDocumentationReferences = aDocumentationReferences;

			addLineBreak();
			mElement.AppendChild(aIdentifier.Element);
			addLineBreak();

			if (aDescription != null)
			{
                insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_DESCRIPTION, mDescription);
			}
			if (aDocumentationReferences != null && aDocumentationReferences.Count > 0)
			{
                Element docsElm = insertElement(Constants.NS_XADES_1_3_2, Constants.TAGX_DOCUMENTATIONREFERENCES);
				addLineBreak(docsElm);
				for (int i = 0; i < aDocumentationReferences.Count; i++)
				{
                    Element doc = createElement(Constants.NS_XADES_1_3_2, Constants.TAGX_DOCUMENTATIONREFERENCE);
					doc.InnerText = aDocumentationReferences[i];
					docsElm.AppendChild(doc);
					addLineBreak(docsElm);
				}
			}
		}

		/// <summary>
		/// Construct ObjectIdentifier from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public ObjectIdentifier(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public ObjectIdentifier(Element aElement, Context aContext) : base(aElement, aContext)
		{

            Element identifierElm = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_IDENTIFIER);
			if (identifierElm != null)
			{
				mIdentifier = new Identifier(identifierElm, mContext);
			}

            mDescription = getChildText(Constants.NS_XADES_1_3_2, Constants.TAGX_DESCRIPTION);

            Element docsElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_DOCUMENTATIONREFERENCES);
            Element[] refs = XmlCommonUtil.selectNodes(docsElement, Constants.NS_XADES_1_3_2, Constants.TAGX_DOCUMENTATIONREFERENCE);
			for (int i = 0; i < refs.Length; i++)
			{
				mDocumentationReferences.Add(XmlCommonUtil.getText(refs[i]));
			}
		}

		public virtual Identifier Identifier
		{
			get
			{
				return mIdentifier;
			}
		}

		public virtual string Description
		{
			get
			{
				return mDescription;
			}
		}

		public virtual IList<string> DocumentationReferences
		{
			get
			{
				return mDocumentationReferences;
			}
		}

		public override string LocalName
		{
			get
			{
                return Constants.TAGX_OBJECTIDENTIFIER;
			}
		}
	}

}