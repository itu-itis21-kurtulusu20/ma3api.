using System;
using System.Text;
using System.Xml;
using Org.BouncyCastle.Utilities.Encoders;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Element = XmlElement;
	using EOCSPResponse = tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
	using EResponseData = tr.gov.tubitak.uekae.esya.api.asn.ocsp.EResponseData;
	using OCSPSearchCriteria = tr.gov.tubitak.uekae.esya.api.signature.certval.OCSPSearchCriteria;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using DigestMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using DigestAlgAndValue = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.DigestAlgAndValue;
	using XAdESBaseElement = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
	using KriptoUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
	using XmlUtil = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

	
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// <code>OcspRef</code> element references one OCSP response. Each reference
	/// contains:
	/// <ul>
	/// <li>a set of data (<code>OCSPIdentifier</code> element) that includes an
	/// identifier of the responder and an indication of the time when the response
	/// was generated. The responder may be identified by its name, using the
	/// <code>Byname</code> element within <code>ResponderID</code>. It may also be
	/// identified by the digest of the server's public key computed as mandated in
	/// RFC 2560 [8] , using the <code>ByKey</code> element. In this case the
	/// content of the <code>ByKey</code> element will be the DER value of the
	/// <code>byKey</code> field defined in RFC 2560, base-64 encoded. The contents
	/// of <code>ByName</code> element MUST follow the rules established by XMLDSIG
	/// in its clause 4.4.4 for strings representing Distinguished Names. The
	/// generation time indication appears in the <code>ProducedAt</code> element
	/// and corresponds to the "ProducedAt" field of the referenced response. The
	/// optional <code>URI</code> attribute could serve to indicate where the OCSP
	/// response identified is archived;
	/// <li>the digest computed on the DER encoded <code>OCSPResponse</code> defined
	/// in RFC 2560, appearing within <code>DigestAlgAndValue</code> element.
	/// Applications claiming alignment with the present document SHOULD include
	/// the <code>DigestAlgAndValue</code> element within each <code>OCSPRef</code>
	/// element.
	/// </ul>
	/// 
	/// <p>Below follows the schema definition:
	/// <pre>
	/// &lt;xsd:complexType name="OCSPRefType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="OCSPIdentifier" type="OCSPIdentifierType"/>
	///     &lt;xsd:element name="DigestAlgAndValue" type="DigestAlgAndValueType" minOccurs="0"/>
	///   &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Nov 10, 2009
	/// </summary>
	public class OCSPReference : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement
	{
		private readonly OCSPIdentifier mOCSPIdentifier;
		private DigestAlgAndValue mDigestAlgAndValue;

		public OCSPReference(Context aContext, DigestMethod aDigestMethod, byte[] aDigestValue, OCSPIdentifier aOCSPId) : base(aContext)
		{
			addLineBreak();

			mOCSPIdentifier = aOCSPId;
			mDigestAlgAndValue.DigestValue = aDigestValue;

			if (mOCSPIdentifier != null)
			{
				mElement.AppendChild(mOCSPIdentifier.Element);
				addLineBreak();
			}
			addDigestInfo(aDigestMethod, aDigestValue);
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public OCSPReference(tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext, tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse aOcspResponse, tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod aDigestMethod, String aURI) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public OCSPReference(Context aContext, EOCSPResponse aOcspResponse, DigestMethod aDigestMethod, string aURI) : base(aContext)
		{
		    EResponseData rd = aOcspResponse.getBasicOCSPResponse().getTbsResponseData();
			ResponderID responderID = new ResponderID(aContext, rd);

			DateTime producedAt;

			try
			{
				DateTime time = aOcspResponse.getBasicOCSPResponse().getProducedAt().Value;
			    producedAt = time; //;XmlUtil.createDate(time);
			}
			catch (Exception exc)
			{
				throw new XMLSignatureException(exc,"core.ocsp.cantResolveProducedAt");
			}

			mOCSPIdentifier = new OCSPIdentifier(mContext, responderID, producedAt, aURI);

			mElement.AppendChild(mOCSPIdentifier.Element);
			addLineBreak();
			byte[] digest = KriptoUtil.digest(aOcspResponse.getEncoded(),aDigestMethod);
			addDigestInfo(aDigestMethod, digest);
		}

		private void addDigestInfo(DigestMethod aDigestMethod, byte[] aDigestValue)
		{
			mDigestAlgAndValue = new DigestAlgAndValue(mContext);
			mDigestAlgAndValue.DigestMethod = aDigestMethod;
			mElement.AppendChild(mDigestAlgAndValue.Element);
			mDigestAlgAndValue.DigestValue = aDigestValue;
			mElement.AppendChild(mDigestAlgAndValue.Element);
			addLineBreak();
		}

		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public OCSPReference(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public OCSPReference(Element aElement, Context aContext) : base(aElement, aContext)
		{
            Element ocspIdElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_OCSPIDENTIFIER);
			mOCSPIdentifier = new OCSPIdentifier(ocspIdElement, mContext);
            Element daavElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_DIGESTALGANDVALUE);
			if (daavElement != null)
			{
				mDigestAlgAndValue = new DigestAlgAndValue(daavElement, mContext);
			}
		}

		public virtual OCSPIdentifier OCSPIdentifier
		{
			get
			{
				return mOCSPIdentifier;
			}
		}

		public virtual DigestAlgAndValue DigestAlgAndValue
		{
			get
			{
				return mDigestAlgAndValue;
			}
		}

		public virtual OCSPSearchCriteria toSearchCriteria()
		{
			ResponderID rid = mOCSPIdentifier.ResponderID;
			OCSPSearchCriteria criteria = new OCSPSearchCriteria(rid.ByName, rid.ByKey, mDigestAlgAndValue.DigestMethod.Algorithm, mDigestAlgAndValue.DigestValue, mOCSPIdentifier.ProducedAt);
			return criteria;
		}

		public override string ToString()
		{
			StringBuilder builder = new StringBuilder();
			builder.Append(mOCSPIdentifier.ToString()).Append("\n");
			builder.Append(mDigestAlgAndValue.DigestMethod.Algorithm).Append(" : ").Append(Base64.Encode(mDigestAlgAndValue.DigestValue)).Append("\n");
			return builder.ToString();
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_OCSPREF;
			}
		}
	}

}