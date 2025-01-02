using System.Collections.Generic;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Element = XmlElement;

	/// <summary>
	/// Full set of references to the revocation data that have been used in the
	/// validation of the signer and CAs certificates, provide means to retrieve the
	/// actual revocation data archived elsewhere in case of dispute and, in this
	/// way, to illustrate that the verifier has taken due diligence of the
	/// available revocation information.
	/// 
	/// <p>Currently two major types of revocation data are managed in most of the
	/// systems, namely CRLs and responses of on-line certificate status servers,
	/// obtained through protocols designed for these purposes, like OCSP protocol.
	/// 
	/// <p>This clause defines the <code>CompleteRevocationRefs</code> element that
	/// will carry the full set of revocation information used for the validation
	/// of the electronic signature.
	/// 
	/// <p>This is an optional unsigned property that qualifies the signature.
	/// 
	/// <p>There SHALL be at most one occurence of this property in the signature.
	/// This occurrence SHALL NOT be empty.
	/// 
	/// <p>Below follows the Schema definition for this element.
	/// <pre>
	/// &lt;xsd:element name="CompleteRevocationRefs" type="CompleteRevocationRefsType"/>
	/// 
	/// &lt;xsd:complexType name="CompleteRevocationRefsType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="CRLRefs" type="CRLRefsType" minOccurs="0"/>
	///     &lt;xsd:element name="OCSPRefs" type="OCSPRefsType" minOccurs="0"/>
	///     &lt;xsd:element name="OtherRefs" type="OtherCertStatusRefsType" minOccurs="0"/>
	///   &lt;/xsd:sequence>
	///   &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/>
	/// &lt;/xsd:complexType>
	/// 
	/// &lt;xsd:complexType name="CRLRefsType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="CRLRef" type="CRLRefType" maxOccurs="unbounded"/>
	///   &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// 
	/// &lt;xsd:complexType name="OCSPRefsType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="OCSPRef" type="OCSPRefType" maxOccurs="unbounded"/>
	///   &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// 
	/// &lt;xsd:complexType name="OtherCertStatusRefsType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="OtherRef" type="AnyType" maxOccurs="unbounded"/>
	///   &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// <p>The CompleteRevocationRefs element can contain:
	/// <ul>
	/// <li>sequences of references to CRLs (CRLRefs element);
	/// <li>sequences of references to OCSPResponse data as defined in RFC 2560 [8] (OCSPRefs element);
	/// <li>other references to alternative forms of revocation data (OtherRefs element).
	/// </ul>
	/// 
	/// @author ahmety
	/// date: Nov 9, 2009
	/// </summary>
	public class CompleteRevocationRefs : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement
	{
		private readonly IList<CRLReference> mCRLReferences = new List<CRLReference>(0);
		private readonly IList<OCSPReference> mOCSPReferences = new List<OCSPReference>(0);
		private readonly IList<OtherCertStatusReference> mOtherReferences = new List<OtherCertStatusReference>(0);


		public CompleteRevocationRefs(Context aContext) : base(aContext)
		{
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
//ORIGINAL LINE: public CompleteRevocationRefs(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public CompleteRevocationRefs(Element aElement, Context aContext) : base(aElement, aContext)
		{

            Element crlElements = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_CRLREFS);
			if (crlElements != null)
			{
				IList<Element> crls = XmlCommonUtil.selectChildElements(crlElements);
				foreach (Element crlElement in crls)
				{
					CRLReference crlRef = new CRLReference(crlElement, mContext);
					mCRLReferences.Add(crlRef);
				}
			}
            Element ocspElements = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_OCSPREFS);
			if (ocspElements != null)
			{
				IList<Element> ocsps = XmlCommonUtil.selectChildElements(ocspElements);
				foreach (Element ocspElement in ocsps)
				{
					OCSPReference ocspRef = new OCSPReference(ocspElement, mContext);
					mOCSPReferences.Add(ocspRef);
				}
			}
            Element otherRefElements = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_OTHERREFS);
			if (otherRefElements != null)
			{
				IList<Element> otherRefs = XmlCommonUtil.selectChildElements(otherRefElements);
				foreach (Element otherRefElement in otherRefs)
				{
					OtherCertStatusReference otherRef = new OtherCertStatusReference(otherRefElement, mContext);
					mOtherReferences.Add(otherRef);
				}
			}

		}

		private void setupChildren()
		{
            XmlCommonUtil.removeChildren(mElement);
			addLineBreak();

			if (mCRLReferences.Count > 0)
			{
                Element crls = insertElement(Constants.NS_XADES_1_3_2, Constants.TAGX_CRLREFS);
				addLineBreak(crls);
				foreach (CRLReference crlRef in mCRLReferences)
				{
					crls.AppendChild(crlRef.Element);
					addLineBreak(crls);
				}
			}

			if (mOCSPReferences.Count > 0)
			{
                Element ocsps = insertElement(Constants.NS_XADES_1_3_2, Constants.TAGX_OCSPREFS);
				addLineBreak(ocsps);
				foreach (OCSPReference ocspRef in mOCSPReferences)
				{
					ocsps.AppendChild(ocspRef.Element);
					addLineBreak(ocsps);
				}
			}

			if (mOtherReferences.Count > 0)
			{
                Element others = insertElement(Constants.NS_XADES_1_3_2, Constants.TAGX_OTHERREFS);
				addLineBreak(others);
				foreach (OtherCertStatusReference otherRef in mOtherReferences)
				{
					others.AppendChild(otherRef.Element);
					addLineBreak(others);
				}
			}
			if (mId != null)
			{
                if (mElement.HasAttribute(Constants.ATTR_ID))
                {
                    mElement.RemoveAttribute(Constants.ATTR_ID);
                }
                mElement.SetAttribute(Constants.ATTR_ID,null, mId);
			}

		}

		public virtual int CRLReferenceCount
		{
			get
			{
				return mCRLReferences.Count;
			}
		}

		public virtual int OCSPReferenceCount
		{
			get
			{
				return mOCSPReferences.Count;
			}
		}

		public virtual int OtherCertStatusReferenceCount
		{
			get
			{
				return mOtherReferences.Count;
			}
		}

		public virtual CRLReference getCRLReference(int aIndex)
		{
			return mCRLReferences[aIndex];
		}

		public virtual OCSPReference getOCSPReference(int aIndex)
		{
			return mOCSPReferences[aIndex];
		}

		public virtual OtherCertStatusReference getOtherCertStatusReference(int aIndex)
		{
			return mOtherReferences[aIndex];
		}

		public virtual void addCRLReference(CRLReference aCRLReference)
		{
			mCRLReferences.Add(aCRLReference);
			setupChildren();
		}

		public virtual void addOCSPReference(OCSPReference aOCSPReference)
		{
			mOCSPReferences.Add(aOCSPReference);
			setupChildren();
		}

		public virtual void addOtherCertStatusReference(OtherCertStatusReference aOtherReference)
		{
			mOtherReferences.Add(aOtherReference);
			setupChildren();
		}


		public override string LocalName
		{
			get
			{
                return Constants.TAGX_COMPLETEREVOCATIONREFS;
			}
		}
	}

}