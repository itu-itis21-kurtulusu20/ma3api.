using System;
using System.Collections.Generic;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy
{
    using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
    using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
    using Element = XmlElement;


    /// <summary>
    /// <p>User notice that should be displayed when the signature is verified.
    /// 
    /// <p>The <code>SPUserNotice</code> element is intended for being displayed
    /// whenever the signature is validated. The <code>ExplicitText</code> element
    /// contains the text of the notice to be displayed. Other notices could come
    /// from the organization issuing the signature policy. The
    /// <code>NoticeRef</code> element names an organization and identifies by
    /// numbers (<code>NoticeNumbers</code> element) a group of textual statements
    /// prepared by that organization, so that the application could get the
    /// explicit notices from a notices file.
    /// 
    /// <p>Below follows the schema definition:
    /// <pre>
    /// &lt;xsd:element name="SPUserNotice" type="SPUserNoticeType"/>
    /// 
    /// &lt;xsd:complexType name="SPUserNoticeType">
    ///   &lt;xsd:sequence>
    ///     &lt;xsd:element name="NoticeRef" type="NoticeReferenceType" minOccurs="0"/>
    ///     &lt;xsd:element name="ExplicitText" type="xsd:string" minOccurs="0"/>
    ///   &lt;/xsd:sequence>
    /// &lt;/xsd:complexType>
    /// </pre>
    /// 
    /// @author ahmety
    /// date: Oct 15, 2009
    /// </summary>
    public class SPUserNotice : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement
	{
        private readonly List<NoticeReference> mNoticeReferences = new List<NoticeReference>(0);
        private readonly List<string> mExplicitTexts = new List<string>(0);

		public SPUserNotice(Context aContext, IList<NoticeReference> aNoticeReferences, IList<string> aExplicitTexts) : base(aContext)
		{
			addLineBreak();

			if (aNoticeReferences != null)
			{
				mNoticeReferences.AddRange(aNoticeReferences);
				foreach (NoticeReference noticeReference in aNoticeReferences)
				{
					mElement.AppendChild(noticeReference.Element);
					addLineBreak();
				}
			}

			if (aExplicitTexts != null)
			{
				mExplicitTexts.AddRange(aExplicitTexts);
				foreach (string explicitText in aExplicitTexts)
				{
                    insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_EXPLICITTEXT, explicitText);
				}
			}
		}

		/// <summary>
		/// Construct XADESBaseElement from existing </summary>
		/// <param name="aElement"> xml element </param>
		/// <param name="aContext"> according to context </param>
		/// <exception cref="tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException">
		///          when structure is invalid or can not be
		///          resolved appropriately </exception>
		public SPUserNotice(Element aElement, Context aContext) : base(aElement, aContext)
		{

            Element[] noticeRefElmArr = selectChildren(Constants.NS_XADES_1_3_2, Constants.TAGX_NOTICEREF);
			foreach (Element noticeRefElm in noticeRefElmArr)
			{
				mNoticeReferences.Add(new NoticeReference(noticeRefElm, mContext));
			}

            Element[] explicitTextElmArr = selectChildren(Constants.NS_XADES_1_3_2, Constants.TAGX_EXPLICITTEXT);
			foreach (Element explicitTextElm in explicitTextElmArr)
			{
				mExplicitTexts.Add(XmlCommonUtil.getText(explicitTextElm));
			}
		}

        public List<NoticeReference> getNoticeReferences()
        {
            return mNoticeReferences;
        }

        public List<String> getExplicitTexts()
        {
            return mExplicitTexts;
        }

		public override string LocalName
		{
			get
			{
                return Constants.TAGX_SPUSERNOTICE;
			}
		}
	}

}