using System.Collections.Generic;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.core;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{

    using Element = XmlElement;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
    using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    //using tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

    /// <summary>
    /// The SigningCertificate property is designed to prevent the simple
    /// substitution of the certificate. This property contains references to
    /// certificates and digest values computed on them.
    /// 
    /// <p>The certificate used to verify the signature SHALL be identified in
    /// the sequence; the signature policy MAY mandate other certificates be
    /// present, that MAY include all the certificates up to the point of trust.
    /// 
    /// <p>This is a signed property that qualifies the signature.
    /// 
    /// <p>At most one SigningCertificate element MAY be present in the signature.
    /// 
    /// <p>Below follows the Schema definition.
    /// 
    /// <pre>
    /// &lt;xsd:element name="SigningCertificate" type="CertIDListType"/>
    /// &lt;xsd:complexType name="CertIDListType">
    ///      &lt;xsd:sequence>
    ///        &lt;xsd:element name="Cert" type="CertIDType" maxOccurs="unbounded"/>
    ///      &lt;/xsd:sequence>
    ///  &lt;/xsd:complexType>
    ///  &lt;xsd:complexType name="CertIDType">
    ///      &lt;xsd:sequence>
    ///        &lt;xsd:element name="CertDigest" type="DigestAlgAndValueType"/>
    ///        &lt;xsd:element name="IssuerSerial" type="ds:X509IssuerSerialType"/>
    ///      &lt;/xsd:sequence>
    ///      &lt;xsd:attribute name="URI" type="xsd:anyURI" use="optional"/>
    ///  &lt;/xsd:complexType>
    ///  &lt;xsd:complexType name="DigestAlgAndValueType">
    ///      &lt;xsd:sequence>
    ///        &lt;xsd:element ref="ds:DigestMethod"/>
    ///        &lt;xsd:element ref="ds:DigestValue"/>
    ///      &lt;/xsd:sequence>
    ///  &lt;/xsd:complexType>
    /// </pre>
    /// 
    /// @author ahmety
    /// date: Jun 22, 2009
    /// </summary>
    public class SigningCertificate : XAdESBaseElement
	{
		private readonly List<CertID> mCerts = new List<CertID>(1);

		public SigningCertificate(Context aBaglam) : base(aBaglam)
		{
			addLineBreak();
		}

		/// <summary>
		///  Construct SigningCertificate from existing </summary>
		///  <param name="aElement"> xml element </param>
		///  <param name="aContext"> according to context </param>
		///  <exception cref="XMLSignatureException"> when structure is invalid or can not be
		///      resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SigningCertificate(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public SigningCertificate(Element aElement, Context aContext) : base(aElement, aContext)
		{
            Element[] elements = XmlCommonUtil.selectNodes(aElement.FirstChild, Constants.NS_XADES_1_3_2, Constants.TAGX_CERTID);
			foreach (Element certid in elements)
			{
				CertID certID = new CertID(certid, aContext);
				mCerts.Add(certID);

			}
		}

		public virtual int CertIDCount
		{
			get
			{
				return mCerts.Count;
			}
		}

		public virtual CertID getCertID(int aIndex)
		{
			return mCerts[aIndex];
		}

		public virtual void addCertID(CertID aCertID)
		{
			Element.AppendChild(aCertID.Element);
			addLineBreak();
			mCerts.Add(aCertID);
		}

		public virtual IList<CertID> CertIDListCopy
		{
			get
			{
                //TODO Bu kopyalamanýn çalýþýp çalýþmadýðýný kontrol etmek gerekiyor.
                List<CertID> copy = new List<CertID>();
                copy.AddRange(mCerts);
			    return copy;
			}
		}

		// base element
		public override string LocalName
		{
			get
			{
				return Constants.TAGX_SIGNINGCERTIFICATE;
			}
		}
	}


}