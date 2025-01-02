using System;
using System.Collections.Generic;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.core;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Logger = log4net.ILog;
	using Element = XmlElement;
	using ECertificate = tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using XAdESBaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
	using AttributeCertificate = tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate;


	/// <summary>
	/// When dealing with long term electronic signatures, all the data used in the
	/// validation (including the certificate path) MUST be conveniently archived.
	/// In principle, the CertificateValues element contains the full set of
	/// certificates that have been used to validate the electronic signature,
	/// including the signer's certificate. However, it is not necessary to include
	/// one of those certificates into this property, if the certificate is already
	/// present in the ds:KeyInfo element of the signature.
	/// 
	/// <p>If <code>CompleteCertificateRefs</code> and <code>CertificateValues</code>
	/// are present, all the certificates referenced in CompleteCertificateRefs MUST
	/// be present either in the ds:KeyInfo element of the signature or in the
	/// <code>CertificateValues</code> property element.
	/// 
	/// <p>The <code>CertificateValues</code> is an optional unsigned property and
	/// qualifies the XML signature.
	/// 
	/// <p>There SHALL be at most one occurence of this property in the signature.
	/// 
	/// <p>Below follows the schema description:
	/// <pre>
	/// &lt;xsd:complexType name="CertificateValuesType">
	///   &lt;xsd:choice minOccurs="0" maxOccurs="unbounded">
	///     &lt;xsd:element name="EncapsulatedX509Certificate" type="EncapsulatedPKIDataType"/>
	///     &lt;xsd:element name="OtherCertificate" type="AnyType"/>
	///   &lt;/xsd:choice>
	///   &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// <p>The <code>EncapsulatedX509Certificate</code> element is able to contain
	/// the base-64 encoding of a DER-encoded X.509 certificate. The
	/// <code>OtherCertificate</code> element is a placeholder for potential future
	/// new formats of certificates.
	/// 
	/// <p>Should XML time-stamp tokens based in XMLDSIG be standardized and spread,
	/// this type could also serve to contain the certification chain for any TSUs
	/// providing such time-stamp tokens, if these certificates are not already
	/// present in the time-stamp tokens themselves as part of the TSUs' signatures.
	/// In this case, an element of this type could be added as an unsigned property
	/// to the XML time-stamp token using the incorporation mechanisms defined in the
	/// present document.
	/// 
	/// <p><font color="red">Note: OtherCertificate is not supported for now...
	/// </font>
	/// 
	/// @author ahmety
	/// date: Jan 6, 2010
	/// </summary>
	public abstract class CertificateValuesType : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement
	{
		// base-64 encoding of a DER-encoded X.509 certificate
		private readonly IList<EncapsulatedX509Certificate> mEncapsulatedX509Certificates = new List<EncapsulatedX509Certificate>();

		public CertificateValuesType(Context aContext) : base(aContext)
		{
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public CertificateValuesType(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext, tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate[] aCertificates) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public CertificateValuesType(Context aContext, ECertificate[] aCertificates) : base(aContext)
		{
			addLineBreak();
			foreach (ECertificate sertifika in aCertificates)
			{
				EncapsulatedX509Certificate ec = new EncapsulatedX509Certificate(mContext, sertifika);
				mEncapsulatedX509Certificates.Add(ec);
				mElement.AppendChild(ec.Element);
				addLineBreak();
			}
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public CertificateValuesType(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext, tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate[] aCertificates) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public CertificateValuesType(Context aContext, AttributeCertificate[] aCertificates) : base(aContext)
		{
			foreach (AttributeCertificate certificate in aCertificates)
			{
				EncapsulatedX509Certificate ec = new EncapsulatedX509Certificate(mContext, certificate);
				mEncapsulatedX509Certificates.Add(ec);
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
//ORIGINAL LINE: public CertificateValuesType(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public CertificateValuesType(Element aElement, Context aContext) : base(aElement, aContext)
		{
            Element[] children = selectChildren(Constants.NS_XADES_1_3_2, Constants.TAGX_ENCAPSULATEDX509CERTIFICATE);

			if (children != null)
			{
			foreach (Element child in children)
			{
				EncapsulatedX509Certificate ec = new EncapsulatedX509Certificate(child, mContext);
				mEncapsulatedX509Certificates.Add(ec);
			}
			}
		}

		public virtual int CertificateCount
		{
			get
			{
				return mEncapsulatedX509Certificates.Count;
			}
		}

		public virtual EncapsulatedX509Certificate getEncapsulatedCertificate(int aIndex)
		{
			return mEncapsulatedX509Certificates[aIndex];
		}


//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public void addCertificate(tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate aCertificate) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public virtual void addCertificate(ECertificate aCertificate)
		{
			EncapsulatedX509Certificate ec = new EncapsulatedX509Certificate(mContext, aCertificate);
			mEncapsulatedX509Certificates.Add(ec);
			mElement.AppendChild(ec.Element);
			addLineBreak();
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate getCertificate(int aIndex) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public virtual ECertificate getCertificate(int aIndex)
		{
			try
			{
				return new ECertificate(mEncapsulatedX509Certificates[aIndex].Value);
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "errors.cantDecode", "CertificateValues[index:" + aIndex + "]", I18n.translate("certificate"));
			}
		}

		// todo cache
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public java.util.List<tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate> getAllCertificates() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public virtual IList<ECertificate> AllCertificates
		{
			get
			{
				IList<ECertificate> certs = new List<ECertificate>(CertificateCount);
				for (int i = 0; i < CertificateCount; i++)
				{
					certs.Add(getCertificate(i));
				}
				return certs;
			}
		}


	}

}