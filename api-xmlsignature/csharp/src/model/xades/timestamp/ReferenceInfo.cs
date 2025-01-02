using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp
{

	using DigestMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	using Element = XmlElement;

	/// <summary>
	/// <pre>
	/// &lt;xsd:element name="ReferenceInfo" type="ReferenceInfoType"/>
	/// 
	/// &lt;xsd:complexType name="ReferenceInfoType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element ref="ds:DigestMethod"/>
	///     &lt;xsd:element ref="ds:DigestValue"/>
	///   &lt;/xsd:sequence>
	///   &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/>
	///   &lt;xsd:attribute name="URI" type="xsd:anyURI" use="optional"/>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Sep 28, 2009
	/// </summary>
	public class ReferenceInfo : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement
	{
		private DigestMethod mDigestMethod;
		private byte[] mDigestValue;
		private string mURI;

		public ReferenceInfo(Context aContext, DigestMethod aDigestMethod, byte[] aDigestValue, string aURI) : base(aContext)
		{
			mDigestMethod = aDigestMethod;
			mDigestValue = aDigestValue;
			mURI = aURI;

			setupChildren();
		}

		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public ReferenceInfo(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public ReferenceInfo(Element aElement, Context aContext) : base(aElement, aContext)
		{

			Element digestMethodElm = XmlCommonUtil.getNextElement(aElement.FirstChild);

            string digestMethodAlg = getAttribute(digestMethodElm, Constants.ATTR_ALGORITHM);
			mDigestMethod = DigestMethod.resolve(digestMethodAlg);

			Element digestValueElm = XmlCommonUtil.getNextElement(digestMethodElm.NextSibling);
			mDigestValue = XmlCommonUtil.getBase64DecodedText(digestValueElm);

			mURI = getAttribute(mElement,Constants.ATTR_URI);
		}

		private void setupChildren()
		{
            XmlCommonUtil.removeChildren(mElement);
			addLineBreak();

            Element digestMethodElm = insertElement(Constants.NS_XMLDSIG, Constants.TAG_DIGESTMETHOD);
            digestMethodElm.SetAttribute(Constants.ATTR_ALGORITHM, mDigestMethod.Url);

            Element digestValueElm = insertElement(Constants.NS_XMLDSIG, Constants.TAG_DIGESTVALUE);
            XmlCommonUtil.setBase64EncodedText(digestValueElm, mDigestValue);

			if (mURI != null)
			{
                if (mElement.HasAttribute(Constants.ATTR_URI))
                {
                    mElement.RemoveAttribute(Constants.ATTR_URI);
                }
                mElement.SetAttribute(Constants.ATTR_URI,null, mURI);
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

		public virtual DigestMethod DigestMethod
		{
			get
			{
				return mDigestMethod;
			}
			set
			{
				mDigestMethod = value;
				setupChildren();
			}
		}


		public virtual byte[] DigestValue
		{
			get
			{
				return mDigestValue;
			}
			set
			{
				mDigestValue = value;
				setupChildren();
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
				mURI = value;
				setupChildren();
			}
		}


		public override string LocalName
		{
			get
			{
				return Constants.TAGX_REFERENCEINFO;
			}
		}
	}

}