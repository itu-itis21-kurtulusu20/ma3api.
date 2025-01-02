using System;
using System.Collections.Generic;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using Element = XmlElement;
	using EOCSPResponse = tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
	using ECRL = tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;



    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// 
	/// <summary>
	/// <p>Below follows the Schema definition for this element.
	/// <pre>
	/// &lt;xsd:complexType name="RevocationValuesType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="CRLValues" type="CRLValuesType" minOccurs="0"/>
	///     &lt;xsd:element name="OCSPValues" type="OCSPValuesType" minOccurs="0"/>
	///     &lt;xsd:element name="OtherValues" type="OtherCertStatusValuesType" minOccurs="0"/>
	///   &lt;/xsd:sequence>
	///   &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Jan 6, 2010
	/// </summary>
	public abstract class RevocationValuesType : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement
	{
		private readonly IList<EncapsulatedCRLValue> mCRLValues = new List<EncapsulatedCRLValue>(0);
		private readonly IList<EncapsulatedOCSPValue> mOCSPValues = new List<EncapsulatedOCSPValue>(0);


		protected internal RevocationValuesType(Context aContext) : base(aContext)
		{
		}

		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: protected RevocationValuesType(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		protected internal RevocationValuesType(Element aElement, Context aContext) : base(aElement, aContext)
		{

            Element crlsElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_CRLVALUES);
            Element ocspsElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_OCSPVALUES);

			Element[] crlElements = null;
			Element[] ocspElements = null;

			if (crlsElement != null)
			{
                crlElements = XmlCommonUtil.selectNodes(crlsElement.FirstChild, Constants.NS_XADES_1_3_2, Constants.TAGX_ENCAPSULATEDCRLVALUE);
			}

			if (ocspsElement != null)
			{
                ocspElements = XmlCommonUtil.selectNodes(ocspsElement.FirstChild, Constants.NS_XADES_1_3_2, Constants.TAGX_ENCAPSULATEDOCSPVALUE);
			}

			//Element[] crlElements = selectChildren(NS_XADES_1_3_2, TAGX_ENCAPSULATEDCRLVALUE);
			//Element[] ocspElements = selectChildren(NS_XADES_1_3_2, TAGX_ENCAPSULATEDOCSPVALUE);

			if (crlElements != null)
			{
			foreach (Element child in crlElements)
			{
				EncapsulatedCRLValue crl = new EncapsulatedCRLValue(child, mContext);
				mCRLValues.Add(crl);
			}
			}

			if (ocspElements != null)
			{
			foreach (Element child in ocspElements)
			{
				EncapsulatedOCSPValue ocsp = new EncapsulatedOCSPValue(child, mContext);
				mOCSPValues.Add(ocsp);
			}
			}

		}

		private void setUpChildren()
		{
            XmlCommonUtil.removeChildren(mElement);
			addLineBreak();
			if (mCRLValues.Count > 0)
			{
                Element crlsElement = insertElement(Constants.NS_XADES_1_3_2, Constants.TAGX_CRLVALUES);
				addLineBreak(crlsElement);
				foreach (EncapsulatedCRLValue crlValue in mCRLValues)
				{
					crlsElement.AppendChild(crlValue.Element);
					addLineBreak(crlsElement);
				}
				addLineBreak();
			}
			if (mOCSPValues.Count > 0)
			{
                Element ocspsElement = insertElement(Constants.NS_XADES_1_3_2, Constants.TAGX_OCSPVALUES);
				addLineBreak(ocspsElement);
				foreach (EncapsulatedOCSPValue ocspValue in mOCSPValues)
				{
					ocspsElement.AppendChild(ocspValue.Element);
					addLineBreak(ocspsElement);
				}
				addLineBreak();
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

		public virtual int CRLValueCount
		{
			get
			{
				return mCRLValues.Count;
			}
		}

		public virtual EncapsulatedCRLValue getCRLValue(int aIndex)
		{
			return mCRLValues[aIndex];
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL getCRL(int aIndex) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public virtual ECRL getCRL(int aIndex)
		{
			try
			{
				return new ECRL(mCRLValues[aIndex].Value);
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "errors.cantDecode", "RevocationValues[crl;index:" + aIndex + "]", I18n.translate("CRL"));
			}
		}

		// todo cache
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public java.util.List<tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL> getAllCRLs() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public virtual IList<ECRL> AllCRLs
		{
			get
			{
				IList<ECRL> crls = new List<ECRL>(CRLValueCount);
				for (int i = 0; i < CRLValueCount; i++)
				{
					crls.Add(getCRL(i));
				}
				return crls;
			}
		}


		public virtual void addCRLValue(EncapsulatedCRLValue aCRLValue)
		{
			mCRLValues.Add(aCRLValue);
			setUpChildren();
		}

		public virtual void addCRL(ECRL aCRL)
		{
			mCRLValues.Add(new EncapsulatedCRLValue(mContext, aCRL));
			setUpChildren();
		}

		public virtual int OCSPValueCount
		{
			get
			{
				return mOCSPValues.Count;
			}
		}

		public virtual EncapsulatedOCSPValue getOCSPValue(int aIndex)
		{
			return mOCSPValues[aIndex];
		}

//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse getOCSPResponse(int aIndex) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public virtual EOCSPResponse getOCSPResponse(int aIndex)
		{
			try
			{
				return new EOCSPResponse(mOCSPValues[aIndex].Value);
			}
			catch (Exception x)
			{
				throw new XMLSignatureException(x, "errors.cantDecode", "RevocationValues[ocsp;index:" + aIndex + "]", I18n.translate("OCSP"));
			}
		}

		// todo cache
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public java.util.List<tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse> getAllOCSPResponses() throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public virtual IList<EOCSPResponse> AllOCSPResponses
		{
			get
			{
				IList<EOCSPResponse> ocsps = new List<EOCSPResponse>(OCSPValueCount);
				for (int i = 0; i < OCSPValueCount; i++)
				{
					ocsps.Add(getOCSPResponse(i));
				}
				return ocsps;
			}
		}

		public virtual void addOCSPValue(EncapsulatedOCSPValue aOCSPValue)
		{
			mOCSPValues.Add(aOCSPValue);
			setUpChildren();
		}

		public virtual void addOCSPResponse(EOCSPResponse aOCSPResponse)
		{
			mOCSPValues.Add(new EncapsulatedOCSPValue(mContext, aOCSPResponse));
			setUpChildren();
		}

	}

}