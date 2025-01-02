using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Element = XmlElement;
	using ECRL = tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
	using CRLSearchCriteria = tr.gov.tubitak.uekae.esya.api.signature.certval.CRLSearchCriteria;
	using LDAPDNUtil = tr.gov.tubitak.uekae.esya.api.common.util.LDAPDNUtil;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using DigestMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using DigestAlgAndValue = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.DigestAlgAndValue;
	using XAdESBaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
	using KriptoUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
	using XmlUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;


	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// CrlRef element references one CRL. Each reference contains:
	/// <ul>
	/// <li>the digest of the entire DER encoded CRL (<code>DigestAlgAndValue</code>
	/// element);
	/// <li>a set of data (<code>CRLIdentifier</code> element) including the issuer
	/// (<code>Issuer</code> element), the time when the CRL was issued
	/// (<code>IssueTime</code> element) and optionally the number of the CRL
	/// (<code>Number</code> element).
	/// <code>CRLIdentifier</code> element contents MUST follow the rules
	/// established by XMLDSIG [3] in its clause 4.4.4 for strings representing
	/// Distinguished Names. In addition, this element can be dropped if the CRL
	/// could be inferred from other information. Its URI attribute could serve to
	/// indicate where the identified CRL is archived.
	/// </ul>
	/// 
	/// <p>NOTE: The <code>number</code> element is an optional hint helping
	/// applications to get the CRL whose digest matches the value present in the
	/// reference.
	/// 
	/// <p>Below follows the schema dsefinition:
	/// <pre>
	/// &lt;xsd:complexType name="CRLRefType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="DigestAlgAndValue" type="DigestAlgAndValueType"/>
	///     &lt;xsd:element name="CRLIdentifier" type="CRLIdentifierType" minOccurs="0"/>
	///   &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Nov 10, 2009
	/// </summary>
	public class CRLReference : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement
	{
		private readonly DigestAlgAndValue mDigestAlgAndValue;
		private readonly CRLIdentifier mCRLIdentifier;

		public CRLReference(Context aContext, DigestMethod aDigestMethod, byte[] aDigestValue, CRLIdentifier aCRLIdentifier) : this(aContext, aDigestMethod)
		{

			if (aDigestValue == null)
			{
				throw new XMLSignatureException("errors.null", "CrlReference.digestValue");
			}

			mDigestAlgAndValue.DigestValue = aDigestValue;

			if (aCRLIdentifier != null)
			{
				mElement.AppendChild(aCRLIdentifier.Element);
				addLineBreak();
			}
            mCRLIdentifier = aCRLIdentifier;
		}

	public CRLReference(Context aContext, ECRL aCRL, DigestMethod aDigestMethod, string aURI) : this(aContext, aDigestMethod)
		{

			if (aCRL == null)
			{
				throw new XMLSignatureException("errors.null", I18n.translate("CRL"));
			}

			mDigestAlgAndValue.DigestValue = KriptoUtil.digest(aCRL.getEncoded(), mDigestAlgAndValue.DigestMethod);

            mCRLIdentifier = new CRLIdentifier(mContext, LDAPDNUtil.normalize(aCRL.getIssuer().stringValue()), aCRL.getThisUpdate().Value, aCRL.getCRLNumber(), aURI);
            //mCRLIdentifier = new CRLIdentifier(mContext, LDAPDNUtil.normalize(aCRL.getIssuer().stringValue()), aCRL.getThisUpdate().Value, aCRL.getCRLNumber(), aURI);
            mElement.AppendChild(/*mCRLIdentifier.Element*/mCRLIdentifier.Element);
			addLineBreak();
		}

		private CRLReference(Context aContext, DigestMethod aDigestMethod) : base(aContext)
		{
			addLineBreak();

			DigestMethod digestMethod = (aDigestMethod == null) ? mContext.Config.AlgorithmsConfig.DigestMethod : aDigestMethod;
			mDigestAlgAndValue = new DigestAlgAndValue(mContext);
			mDigestAlgAndValue.DigestMethod = digestMethod;

			mElement.AppendChild(mDigestAlgAndValue.Element);
			addLineBreak();
		}

		/// <summary>
		/// Construct CRLReference from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
		public CRLReference(Element aElement, Context aContext) : base(aElement, aContext)
		{

            Element davElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_DIGESTALGANDVALUE);
			mDigestAlgAndValue = new DigestAlgAndValue(davElement, mContext);

            Element idElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_CRLIDENTIFIER);
			if (idElement != null)
			{
				mCRLIdentifier = new CRLIdentifier(idElement, mContext);
			}
		}

		public virtual DigestMethod DigestMethod
		{
			get
			{
				return mDigestAlgAndValue.DigestMethod;
			}
		}

		public virtual byte[] DigestValue
		{
			get
			{
				return mDigestAlgAndValue.DigestValue;
			}
		}

		public virtual CRLIdentifier CRLIdentifier
		{
			get
			{
				return mCRLIdentifier;
			}
		}

		public virtual CRLSearchCriteria toSearchCriteria()
		{
			return new CRLSearchCriteria(LDAPDNUtil.normalize(mCRLIdentifier.Issuer), mCRLIdentifier.IssueTime, mCRLIdentifier.Number, mDigestAlgAndValue.DigestMethod.Algorithm, mDigestAlgAndValue.DigestValue);
		}

		public override string ToString()
		{
			StringBuilder builder = new StringBuilder();
			builder.Append(mCRLIdentifier.ToString());
			builder.Append(mDigestAlgAndValue.ToString());
			return builder.ToString();
		}

		public override string LocalName
		{
			get
			{
                return Constants.TAGX_CRLREF;
			}
		}

	}

}