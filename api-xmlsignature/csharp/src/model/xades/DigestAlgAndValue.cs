using System.Text;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades
{
	using DigestMethod = tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
    //using XMLUtils = tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n.core.utils.XMLUtils;
    using Element = XmlElement;


	/// <summary>
	/// Element for keeping digest of a structure.
	/// 
	/// <p>Overriding classes must override <code><seealso cref="#getLocalName"/> method.</code>
	/// 
	/// @author ahmety
	/// date: Nov 10, 2009
	/// </summary>
	public class DigestAlgAndValue : XAdESBaseElement
	{
		private DigestMethod mDigestMethod;
		private byte[] mDigestValue;


		private readonly Element mDigestMethodElement, mDigestValueElement;



		public DigestAlgAndValue(Context aContext) : base(aContext)
		{

			addLineBreak();

            mDigestMethodElement = insertElement(Constants.NS_XMLDSIG, Constants.TAG_DIGESTMETHOD);
            mDigestValueElement = insertElement(Constants.NS_XMLDSIG, Constants.TAG_DIGESTVALUE);
		}

		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public DigestAlgAndValue(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public DigestAlgAndValue(Element aElement, Context aContext) : base(aElement, aContext)
		{

			mDigestMethodElement = XmlCommonUtil.getNextElement(mElement.FirstChild);
			mDigestValueElement = XmlCommonUtil.getNextElement(mDigestMethodElement.NextSibling);

			string digestAlg = mDigestMethodElement.GetAttribute(Constants.ATTR_ALGORITHM);
			mDigestMethod = DigestMethod.resolve(digestAlg);
			mDigestValue = XmlCommonUtil.getBase64DecodedText(mDigestValueElement);
		}

		public virtual DigestMethod DigestMethod
		{
			get
			{
				return mDigestMethod;
			}
			set
			{
				mDigestMethodElement.SetAttribute(Constants.ATTR_ALGORITHM,null, value.Url);
				mDigestMethod = value;
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
			    string base64String = System.Convert.ToBase64String(value);
			    mDigestValueElement.InnerText = base64String;
				mDigestValue = value;
			}
		}


		public override string ToString()
		{
			StringBuilder builder = new StringBuilder();
            string base64String = System.Convert.ToBase64String(mDigestValue);
			builder.Append(mDigestMethod.Algorithm).Append(" : ").Append(base64String);

			return builder.ToString();
		}

		public override string LocalName
		{
			get
			{
				return Constants.TAGX_DIGESTALGANDVALUE;
			}
		}
	}

}