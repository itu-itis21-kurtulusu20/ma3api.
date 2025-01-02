using System;
using System.Text;
using System.Xml;
using Org.BouncyCastle.Utilities.Encoders;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs
{
    using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
    using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	
	using Element = XmlElement;

	/// 
	/// <summary>
	/// <p>Below follows the schema definiton:
	/// <pre>
	/// &lt;xsd:complexType name="OCSPIdentifierType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="ResponderID" type="ResponderIDType"/>
	///     &lt;xsd:element name="ProducedAt" type="xsd:dateTime"/>
	///   &lt;/xsd:sequence>
	///   &lt;xsd:attribute name="URI" type="xsd:anyURI" use="optional"/>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Nov 11, 2009
	/// </summary>
	public class OCSPIdentifier : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement
	{
		private readonly ResponderID mResponderID;
		private readonly DateTime mProducedAt;

		private readonly string mURI;

        public OCSPIdentifier(Context aContext, ResponderID aResponderID, DateTime aProducedAt, string aURI)
            : base(aContext)
		{
			addLineBreak();

			mURI = aURI;
			mResponderID = aResponderID;
            mProducedAt = aProducedAt;

			mElement.AppendChild(mResponderID.Element);
			addLineBreak();

            insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_PRODUCEDAT, XmlCommonUtil.datetime2xmlgregstr(aProducedAt));

			if (mURI != null)
			{
                mElement.SetAttribute( Constants.ATTR_URI,null, mURI);
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
//ORIGINAL LINE: public OCSPIdentifier(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public OCSPIdentifier(Element aElement, Context aContext) : base(aElement, aContext)
		{

            Element responderElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_RESPONDERID);
			mResponderID = new ResponderID(responderElement, mContext);

            Element timeElement = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_PRODUCEDAT);
			mProducedAt = XmlCommonUtil.getDate(timeElement);

            mURI = mElement.GetAttribute(Constants.ATTR_URI);
		}

		public virtual ResponderID ResponderID
		{
			get
			{
				return mResponderID;
			}
		}

		public virtual DateTime ProducedAt
		{
			get
			{
				return mProducedAt;
			}
		}

		public virtual string URI
		{
			get
			{
				return mURI;
			}
		}

		public override string ToString()
		{
			StringBuilder builder = new StringBuilder();
			if (mURI != null)
			{
                builder.Append("uri : ").Append(mURI).Append("\n");
			}
			if (mResponderID.ByKey != null)
			{
                builder.Append("key : ").Append(Base64.Encode(mResponderID.ByKey));
			}
			if (mResponderID.ByName != null)
			{
                builder.Append("name : ").Append(mResponderID.ByName);
			}
            builder.Append("\nproduced at : ").Append(mProducedAt);

			return builder.ToString();
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_OCSPIDENTIFIER;
			}
		}
	}

}