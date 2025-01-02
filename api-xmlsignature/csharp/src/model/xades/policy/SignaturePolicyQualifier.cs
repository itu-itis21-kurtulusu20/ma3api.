using System;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy
{

	using Any = tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Any;

	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XMLSignatureException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
	using Element = XmlElement;


	/// <summary>
	/// @author ahmety
	/// date: Oct 15, 2009
	/// </summary>
	public class SignaturePolicyQualifier : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Any
	{
		private readonly string mURI;
		private readonly SPUserNotice mUserNotice;

		/// <summary>
		/// Construct new BaseElement according to context </summary>
		/// <param name="aContext"> where some signature spesific properties reside. </param>
		/// <param name="aURI"> where the policy stays </param>
		/// <param name="aUserNotice"> User notice that should be displayed when the
		///      signature is verified. </param>
		public SignaturePolicyQualifier(Context aContext, string aURI, SPUserNotice aUserNotice) : base(aContext)
		{

			mURI = aURI;
			mUserNotice = aUserNotice;

			addLineBreak();
			if (mURI != null)
			{
                insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_SPURI, mURI);
			}
			if (mUserNotice != null)
			{
				mElement.AppendChild(mUserNotice.Element);
				addLineBreak();
			}
		}

		/// <summary>
		/// Construct Any from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
//JAVA TO C# CONVERTER WARNING: Method 'throws' clauses are not available in .NET:
//ORIGINAL LINE: public SignaturePolicyQualifier(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public SignaturePolicyQualifier(Element aElement, Context aContext) : base(aElement, aContext)
		{

            mURI = getChildText(Constants.NS_XADES_1_3_2, Constants.TAGX_SPURI);

            Element noticeElm = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_SPUSERNOTICE);
			if (noticeElm != null)
			{
				mUserNotice = new SPUserNotice(noticeElm, mContext);
			}
		}

		public virtual string URI
		{
			get
			{
				return mURI;
			}
		}

		public virtual SPUserNotice UserNotice
		{
			get
			{
				return mUserNotice;
			}
		}

	    public String getUserNoticeFirstExplicitText()
	    {
	        return mUserNotice.getExplicitTexts()[0];
	    }

        public override string Namespace
		{
			get
			{
                return Constants.NS_XADES_1_3_2;
			}
		}

		public override string LocalName
		{
			get
			{
                return Constants.TAGX_SIGPOLICYQUALIFIER;
			}
		}
	}

}