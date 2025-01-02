using System.Collections.Generic;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using CertID = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.CertID;

	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;


    using Element = XmlElement;

	/// <summary>
	/// <code>CompleteCertificateRefs</code> element is the XML element able to carry
	/// the references to the CA certificates.
	/// 
	/// <p>This is an optional unsigned property that qualifies the signature.
	/// 
	/// <p>There SHALL be at most one occurence of this property in the signature.
	/// 
	/// <pre>
	/// &lt;xsd:element name="CompleteCertificateRefs" type="CompleteCertificateRefsType"/>
	/// 
	/// &lt;xsd:complexType name="CompleteCertificateRefsType">
	///     &lt;xsd:sequence>
	///       &lt;xsd:element name="CertRefs" type="CertIDListType" />
	///     &lt;/xsd:sequence>
	///     &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// <p>The <code>CertRefs</code> element contains a sequence of <code>Cert</code> 
	/// elements already defined in clause 7.2.2, incorporating the digest of each
	/// certificate and the issuer and serial number identifier.
	/// 
	/// <p>Should XML time-stamp tokens based in XMLDSIG be standardized and spread,
	/// this type could also serve to contain references to the certification chain
	/// for any TSUs providing such time-stamp tokens. In this case, an element of
	/// this type could be added as an unsigned property to the XML time-stamp token
	/// using the incorporation mechanisms defined in the present document.
	/// 
	/// @author ahmety
	/// date: Nov 9, 2009
	/// </summary>
	public class CompleteCertificateRefs : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement
	{

		private readonly IList<CertID> mCertificateReferences = new List<CertID>(0);

		private readonly Element mRefsElement;

		public CompleteCertificateRefs(Context aContext) : base(aContext)
		{
			addLineBreak();
            mRefsElement = insertElement(Constants.NS_XADES_1_3_2, Constants.TAGX_CERTREFS);
			addLineBreak(mRefsElement);
		}

		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public CompleteCertificateRefs(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public CompleteCertificateRefs(Element aElement, Context aContext) : base(aElement, aContext)
		{

            mRefsElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_CERTREFS);

            Element[] elements = XmlCommonUtil.selectNodes(mRefsElement.FirstChild, Constants.NS_XADES_1_3_2, Constants.TAGX_CERTID);
			foreach (Element certid in elements)
			{
				CertID certID = new CertID(certid, aContext);
				mCertificateReferences.Add(certID);
			}
		}

		public virtual int CertificateReferenceCount
		{
			get
			{
				return mCertificateReferences.Count;
			}
		}

		public virtual CertID getCertificateReference(int aIndex)
		{
			return mCertificateReferences[aIndex];
		}

		public virtual void addCertificateReference(CertID aCertificateReference)
		{
			mCertificateReferences.Add(aCertificateReference);
			mRefsElement.AppendChild(aCertificateReference.Element);
			addLineBreak(mRefsElement);
		}


		public override string LocalName
		{
			get
			{
                return Constants.TAGX_COMPLETECERTREFS;
			}
		}
	}

}