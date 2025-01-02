using System;
using System.Text;
using System.Xml;
using Org.BouncyCastle.Math;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509
{

	using Element =  XmlElement;
	using LDAPDNUtil = tr.gov.tubitak.uekae.esya.api.common.util.LDAPDNUtil;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;



	/// <summary>
	/// <p><code>X509Data</code> child element, which contains an X.509 issuer
	/// distinguished name/serial number pair. The distinguished name SHOULD be
	/// represented as a string that complies with section 3 of RFC4514 [LDAP-DN],
	/// to be generated according to the [Distinguished Name Encoding Rules]
	/// described in [XMLdSig].
	/// 
	/// <p>Below follows the schema definition.
	/// 
	/// <pre>
	/// &lt;complexType name="X509IssuerSerialType">
	///   &lt;complexContent>
	///     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
	///       &lt;sequence>
	///         &lt;element name="X509IssuerName" type="{http://www.w3.org/2001/XMLSchema}string"/>
	///         &lt;element name="X509SerialNumber" type="{http://www.w3.org/2001/XMLSchema}integer"/>
	///       &lt;/sequence>
	///     &lt;/restriction>
	///   &lt;/complexContent>
	/// &lt;/complexType>
	/// </pre>
	/// </summary>
	/// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data </seealso>
	/// <seealso cref= tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo
	/// @author ahmety
	/// date: Jun 16, 2009 </seealso>
	public class X509IssuerSerial : X509DataElement
	{

	    private readonly string mIssuerName;
		private readonly BigInteger mSerialNumber;

		// internal
		private readonly Element mIssuerElement, mSerialElement;




		public X509IssuerSerial(Context aContext, ECertificate aCertificate) : this(aContext, aCertificate.getIssuer().stringValue(), new BigInteger(aCertificate.getSerialNumber().GetData()))
		{

		}


		public X509IssuerSerial(Context aContext, string aIssuerName, BigInteger aSerialNumber) : base(aContext)
		{
			addLineBreak();
			mIssuerName = aIssuerName;
			mSerialNumber = aSerialNumber;

            mIssuerElement = insertTextElement(Constants.NS_XMLDSIG, Constants.TAG_X509ISSUERNAME, LDAPDNUtil.normalize(aIssuerName));
            mSerialElement = insertBase64EncodedElement(Constants.NS_XMLDSIG, Constants.TAG_X509SERIALNUMBER, mSerialNumber.ToByteArray());

		}

		/// <summary>
		///  Construct X509IssuerSerial from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public X509IssuerSerial(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public X509IssuerSerial(Element aElement, Context aContext) : base(aElement, aContext)
		{

			mIssuerElement = XmlCommonUtil.getNextElement(aElement.FirstChild);
			mSerialElement = XmlCommonUtil.getNextElement(mIssuerElement);

			mIssuerName = XmlCommonUtil.getText(mIssuerElement);
		    String text = XmlCommonUtil.getText(mSerialElement);
            mSerialNumber = new BigInteger(1, Encoding.UTF8.GetBytes(text));
        }

		public virtual string IssuerName
		{
			get
			{
				return mIssuerName;
			}
		}

		public virtual BigInteger SerialNumber
		{
			get
			{
				return mSerialNumber;
			}
		}

		// base element
		public override string LocalName
		{
			get
			{
				return Constants.TAG_X509ISSUERSERIAL;
			}
		}

	}

}