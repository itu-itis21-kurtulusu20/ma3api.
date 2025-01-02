using System;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using OIDUtil = tr.gov.tubitak.uekae.esya.api.common.util.OIDUtil;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Element = XmlElement;

	/// <summary>
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
	/// @author ahmety
	/// date: Sep 17, 2009
	/// </summary>
	public class Identifier : XAdESBaseElement
	{
		public const string QUALIFIER_OIDAsURI = "OIDAsURI";
		public const string QUALIFIER_OIDAsURN = "OIDAsURN";


		private readonly string mValue;
		private readonly string mQualifier;

		public Identifier(Context aContext, int[] aOId)
            : this(aContext, OIDUtil.toURN(aOId), QUALIFIER_OIDAsURN)
		{
		}

        public Identifier(Context aContext, String uri)
            : this(aContext, uri, QUALIFIER_OIDAsURI)
        {
        }

		public Identifier(Context aBaglam, string aValue, string aQualifier) : base(aBaglam)
		{
			mValue = aValue;
			mQualifier = aQualifier;

			mElement.InnerText = aValue;
			if (mQualifier != null)
			{
				mElement.SetAttribute(Constants.ATTR_QUALIFIER, null,mQualifier);
			}
		}

		/// <summary>
		/// Construct Identifier from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>

		public Identifier(Element aElement, Context aContext) : base(aElement, aContext)
		{
			mValue = mElement.InnerText;
			mQualifier = mElement.GetAttribute(Constants.ATTR_QUALIFIER);
		}

		public virtual string Value
		{
			get
			{
				return mValue;
			}
		}

		public virtual string Qualifier
		{
			get
			{
				return mQualifier;
			}
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_IDENTIFIER;
			}
		}
	}

}