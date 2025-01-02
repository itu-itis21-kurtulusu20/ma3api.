using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{

	using EResponseData = tr.gov.tubitak.uekae.esya.api.asn.ocsp.EResponseData;
	using LDAPDNUtil = tr.gov.tubitak.uekae.esya.api.common.util.LDAPDNUtil;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;


    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Element = XmlElement;

	/// <summary>
	/// The OCSP responder may be identified by its name, using the
	/// <code>Byname</code> element within <code>ResponderID</code>. It may also be
	/// identified by the digest of the server's public key computed as mandated in
	/// RFC 2560, using the <code>ByKey</code> element. In this case the content of
	/// the <code>ByKey</code> element will be the DER value of the
	/// <code>byKey</code> field defined in RFC 2560, base-64 encoded. The contents
	/// of <code>ByName</code> element MUST follow the rules established by XMLDSIG
	/// in its clause 4.4.4 for strings representing Distinguished Names.
	/// 
	/// <p>Below follows the schema definition:
	/// <pre>
	/// &lt;xsd:complexType name="ResponderIDType">
	///   &lt;xsd:choice>
	///     &lt;xsd:element name="ByName" type="xsd:string"/>
	///     &lt;xsd:element name="ByKey" type="xsd:base-64Binary"/>
	///   &lt;/xsd:choice>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Nov 11, 2009
	/// </summary>
	public class ResponderID : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement
	{
		private string mByName;
		private byte[] mByKey;

		public ResponderID(Context aContext, string aName) : base(aContext)
		{
			constructByName(LDAPDNUtil.normalize(aName));
		}

		public ResponderID(Context aContext, byte[] aKey) : base(aContext)
		{
			constructByKey(aKey);
		}

		public ResponderID(Context aContext, EResponseData aResponseData) : base(aContext)
		{
			if (aResponseData.getResponderIDType() == tr.gov.tubitak.uekae.esya.asn.ocsp.ResponderID._BYNAME)
			{
				string name = aResponseData.getResponderIdByName().stringValue();
				constructByName(name);
			}
			else
			{
				byte[] key = aResponseData.getResponderIdByKey();
				constructByKey(key);
			}
		}

		private void constructByName(string aName)
		{
			mByName = aName;
			addLineBreak();
            insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_BYNAME, mByName);
			addLineBreak();
		}

		private void constructByKey(byte[] aKey)
		{
			mByKey = aKey;
			addLineBreak();
            insertBase64EncodedElement(Constants.NS_XADES_1_3_2, Constants.TAGX_BYKEY, mByKey);
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
//ORIGINAL LINE: public ResponderID(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public ResponderID(Element aElement, Context aContext) : base(aElement, aContext)
		{

            Element nameElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_BYNAME);
            Element keyElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_BYKEY);

			if (nameElement == null && keyElement == null)
			{
				throw new XMLSignatureException("core.model.invalidResponderId");
			}

			if (nameElement != null)
			{
				mByName = XmlCommonUtil.getText(nameElement);
			}

			if (keyElement != null)
			{
				mByKey = XmlCommonUtil.getBase64DecodedText(keyElement);
			}
		}

		public virtual string ByName
		{
			get
			{
				return mByName;
			}
		}

		public virtual byte[] ByKey
		{
			get
			{
				return mByKey;
			}
		}

		public override string LocalName
		{
			get
			{
                return Constants.TAGX_RESPONDERID;
			}
		}
	}

}