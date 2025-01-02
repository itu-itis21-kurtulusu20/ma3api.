using System;
using System.Text;
using System.Xml;
using Com.Objsys.Asn1.Runtime;
using LDAPDNUtil = tr.gov.tubitak.uekae.esya.api.common.util.LDAPDNUtil;
using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{
    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Element = XmlElement;


	/// <summary>
	/// <code>CrlRef</code> element references one CRL. Each reference contains a
	/// set of data (<code>CRLIdentifier</code> element) including the issuer
	/// (<code>Issuer</code> element), the time when the CRL was issued (
	/// <code>IssueTime</code> element) and optionally the number of the CRL
	/// (<code>Number</code> element).
	/// 
	/// <p><code>CRLIdentifier</code> element contents MUST follow the rules
	/// established by XMLDSIG [3] in its clause 4.4.4 for strings representing
	/// Distinguished Names.
	/// 
	/// <p>Below follows the schema definition: 
	/// <pre>
	/// &lt;xsd:complexType name="CRLIdentifierType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="Issuer" type="xsd:string"/>
	///     &lt;xsd:element name="IssueTime" type="xsd:dateTime" />
	///     &lt;xsd:element name="Number" type="xsd:integer" minOccurs="0"/>
	///   &lt;/xsd:sequence>
	///   &lt;xsd:attribute name="URI" type="xsd:anyURI" use="optional"/>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Nov 10, 2009
	/// </summary>
	public class CRLIdentifier : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement
	{
		private readonly string mIssuer;
		private readonly DateTime mIssueTime;
		private readonly BigInteger mNumber;

		private readonly string mURI;

		public CRLIdentifier(Context aContext, string aIssuer, DateTime aIssueTime, BigInteger aNumber, string aURI) : base(aContext)
		{

			mIssuer = LDAPDNUtil.normalize(aIssuer);
			mIssueTime = aIssueTime;
			mNumber = aNumber;
			mURI = aURI;

            insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_ISSUER, mIssuer);

            insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_ISSUETIME, XmlCommonUtil.datetime2xmlgregstr(aIssueTime));
			if (mNumber != null)
			{
                insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_NUMBER, mNumber.ToString());
			}
			if (mURI != null)
			{
                mElement.SetAttribute(Constants.ATTR_URI,null, mURI);
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
//ORIGINAL LINE: public CRLIdentifier(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public CRLIdentifier(Element aElement, Context aContext) : base(aElement, aContext)
		{

            Element issuerElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_ISSUER);
			mIssuer = XmlCommonUtil.getText(issuerElement);

            Element issueTimeElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_ISSUETIME);
            
            mIssueTime = XmlCommonUtil.getDate(issueTimeElement);

            Element numberElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_NUMBER);
			if (numberElement != null)
			{
				mNumber = new BigInteger(XmlCommonUtil.getText(numberElement));
			}

            mURI = mElement.GetAttribute(Constants.ATTR_URI);
		}

		public virtual string Issuer
		{
			get
			{
				return mIssuer;
			}
		}

		public virtual DateTime IssueTime
		{
			get
			{
				return mIssueTime;
			}
		}

		public virtual BigInteger Number
		{
			get
			{
				return mNumber;
			}
		}

		public virtual string URI
		{
			get
			{
				return mURI;
			}
		}

		public override string ToString()
		{
			StringBuilder builder = new StringBuilder();
			if (mURI != null)
			{
				builder.Append("uri: ").Append(mURI).Append("\n");
			}

			builder.Append("issuer: ").Append(mIssuer).Append("\nissue time: ").Append(mIssueTime).Append("\nno: ").Append(mNumber).Append("\n");

			return builder.ToString();
		}

		public override string LocalName
		{
			get
			{
                return Constants.TAGX_CRLIDENTIFIER;
			}
		}
	}

}