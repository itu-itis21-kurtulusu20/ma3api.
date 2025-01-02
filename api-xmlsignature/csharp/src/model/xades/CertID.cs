using System.Text;
using System.Xml;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

	using Element = XmlElement;
	using CertificateSearchCriteria = tr.gov.tubitak.uekae.esya.api.signature.certval.CertificateSearchCriteria;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using DigestMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	//using XMLUtils = tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n.core.utils.XMLUtils;
	using LDAPDNUtil = tr.gov.tubitak.uekae.esya.api.common.util.LDAPDNUtil;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
    using KriptoUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;

	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;




	/// <summary>
	/// The <code>SigningCertificate</code> element contains the aforementioned
	/// sequence of certificate identifiers and digests computed on the
	/// certificates (Cert elements).
	/// 
	/// <p>The element <code>IssuerSerial</code> contains the identifier of one
	/// of the certificates referenced in the sequence. Should the
	/// <code>ds:X509IssuerSerial</code> element appear in the signature to denote
	/// the same certificate, its value MUST be consistent with the corresponding
	/// <code>IssuerSerial</code> element.
	/// 
	/// <p>The element <code>CertDigest</code> contains the digest of one of the certificates
	/// referenced in the sequence. It contains two elements: 
	/// <code>ds:DigestMethod</code> indicates the digest algorithm and
	/// <code>ds:DigestValue</code> contains the base-64 encoded value of the digest
	/// computed on the DER-encoded certificate.
	/// 
	/// <p>The optional <code>URI</code> attribute indicates where the referenced
	/// certificate can be found.
	/// 
	/// <p>Below follows the Schema definition.
	/// <pre>
	/// &lt;xsd:element name="Cert" type="CertIDType"/>
	/// 
	/// &lt;xsd:complexType name="CertIDType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="CertDigest" type="DigestAlgAndValueType"/>
	///     &lt;xsd:element name="IssuerSerial" type="ds:X509IssuerSerialType"/>
	///   &lt;/xsd:sequence>
	///   &lt;xsd:attribute name="URI" type="xsd:anyURI" use="optional"/>
	/// &lt;/xsd:complexType>
	/// 
	/// &lt;xsd:complexType name="DigestAlgAndValueType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element ref="ds:DigestMethod"/>
	///     &lt;xsd:element ref="ds:DigestValue"/>
	///   &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Jun 22, 2009
	/// </summary>
	public class CertID : XAdESBaseElement
	{
		//digestAlgAndValue
		private readonly CertDigest mCertificateDigest;

		// issuerSerial
		private string mX509IssuerName;
		protected internal BigInteger mX509SerialNumber;

		private string mURI;

		private readonly Element mIssuerSerialElement, mIssuerNameElement, mSerialNumberElement;

		public CertID(Context aContext) : base(aContext)
		{

			addLineBreak();

			mCertificateDigest = new CertDigest(aContext);
			mElement.AppendChild(mCertificateDigest.Element);
			addLineBreak();


            mIssuerSerialElement = insertElement(Constants.NS_XADES_1_3_2, Constants.TAGX_ISSUERSERIAL);
            mIssuerNameElement = createElement(Constants.NS_XMLDSIG, Constants.TAG_X509ISSUERNAME);
            mSerialNumberElement = createElement(Constants.NS_XMLDSIG, Constants.TAG_X509SERIALNUMBER);

			addLineBreak(mIssuerSerialElement);
			mIssuerSerialElement.AppendChild(mIssuerNameElement);
			addLineBreak(mIssuerSerialElement);
			mIssuerSerialElement.AppendChild(mSerialNumberElement);
			addLineBreak(mIssuerSerialElement);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public CertID(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aBaglam, tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate aCertificate, tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod aDigestAlg) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public CertID(Context aBaglam, ECertificate aCertificate, DigestMethod aDigestAlg) : this(aBaglam)
		{
			DigestMethod digestMethod = (aDigestAlg == null) ? mContext.Config.AlgorithmsConfig.DigestMethod : aDigestAlg;
			byte[] ozet = KriptoUtil.digest(aCertificate.getEncoded(), digestMethod);

			DigestMethod = digestMethod;
			DigestValue = ozet;

			X509SerialNumber = aCertificate.getSerialNumber();
			X509IssuerName = aCertificate.getIssuer().stringValue();

		}

        public CertID(Context aBaglam, byte[] certHash, DigestMethod aDigestAlg, ESignerIdentifier eSignerIdentifier) : this(aBaglam)
        {
            DigestMethod digestMethod = (aDigestAlg == null) ? mContext.Config.AlgorithmsConfig.DigestMethod : aDigestAlg;

            DigestMethod = digestMethod;
            DigestValue = certHash;

            X509SerialNumber = eSignerIdentifier.getIssuerAndSerialNumber().getSerialNumber();
            X509IssuerName = eSignerIdentifier.getIssuerAndSerialNumber().getIssuer().stringValue();
        }

		/// <summary>
		/// Mevcut xml yapisindaki elemanı cozmek icin constructor </summary>
		/// <param name="aElement"> xml element to unmarshall </param>
		/// <param name="aContext"> which owner signature belongs to </param>
		/// <exception cref="XMLSignatureException"> if wrong element is given! </exception>
		//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
		//ORIGINAL LINE: public CertID(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public CertID(Element aElement, Context aContext) : base(aElement, aContext)
		{

			// digest alg & value
			Element certDigestElement = XmlCommonUtil.getNextElement(mElement.FirstChild);
			mCertificateDigest = new CertDigest(certDigestElement, aContext);

			// issuer name & serial
			mIssuerSerialElement = XmlCommonUtil.getNextElement(certDigestElement.NextSibling);
            mIssuerNameElement = XmlCommonUtil.getNextElement(mIssuerSerialElement.FirstChild);
            mSerialNumberElement = XmlCommonUtil.getNextElement(mIssuerNameElement.NextSibling);

			//mX509IssuerName = RFC2253Parser.normalize(XmlUtil.getText(mIssuerNameElement));
			mX509IssuerName = LDAPDNUtil.normalize(XmlCommonUtil.getText(mIssuerNameElement));
			mX509SerialNumber = new BigInteger(XmlCommonUtil.getText(mSerialNumberElement));

			mURI = mElement.GetAttribute(Constants.ATTR_URI);
		}

		public virtual DigestMethod DigestMethod
		{
			get
			{
				return mCertificateDigest.DigestMethod;
			}
			set
			{
				mCertificateDigest.DigestMethod = value;
			}
		}


		public virtual byte[] DigestValue
		{
			get
			{
				return mCertificateDigest.DigestValue;
			}
			set
			{
				mCertificateDigest.DigestValue = value;
			}
		}


		public virtual string X509IssuerName
		{
			get
			{
				return mX509IssuerName;
			}
			set
			{
				mIssuerNameElement.InnerText = value;
				mX509IssuerName = value;
			}
		}


		public virtual BigInteger X509SerialNumber
		{
			get
			{
				return mX509SerialNumber;
			}
			set
			{
				mSerialNumberElement.InnerText = value.ToString();
				mX509SerialNumber = value;
			}
		}


		public virtual string URI
		{
			get
			{
				return mURI;
			}
			set
			{
				mElement.SetAttribute(Constants.ATTR_URI,null, value);
				mURI = value;
			}
		}


		public virtual CertificateSearchCriteria toSearchCriteria()
		{
			CertificateSearchCriteria criteria = new CertificateSearchCriteria();
			if (mCertificateDigest != null)
			{
                criteria.setDigestAlg(mCertificateDigest.DigestMethod.Algorithm);
			    criteria.setDigestValue(mCertificateDigest.DigestValue);
			}
			criteria.setIssuer(mX509IssuerName);
			criteria.setSerial(mX509SerialNumber);
			return criteria;
		}

		public override string ToString()
		{
			StringBuilder builder = new StringBuilder();
            builder.Append("CertId issuer: ").Append(mX509IssuerName).Append("; serial: ").Append(mX509SerialNumber.ToString(16));
			return builder.ToString();
		}

		//
		public override string LocalName
		{
			get
			{
				return Constants.TAGX_CERTID;
			}
		}
	}

}