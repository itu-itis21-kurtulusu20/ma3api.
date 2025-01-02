using System;
using System.Collections.Generic;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy
{

	using XMLSignatureRuntimeException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureRuntimeException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using Context = tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;


    using Element = XmlElement;

	/// <summary>
	/// The <code>NoticeRef</code> element names an organization and identifies by
	/// numbers (<code>NoticeNumbers</code> element) a group of textual statements
	/// prepared by that organization, so that the application could get the
	/// explicit notices from a notices file.
	/// 
	/// <p>Below follows the schema definition:
	/// <pre>
	/// &lt;xsd:complexType name="NoticeReferenceType">
	///   &lt;xsd:sequence>
	///     &lt;xsd:element name="Organization" type="xsd:string"/>
	///     &lt;xsd:element name="NoticeNumbers" type="IntegerListType"/>
	///   &lt;/xsd:sequence>
	/// &lt;/xsd:complexType>
	/// </pre>
	/// 
	/// @author ahmety
	/// date: Oct 15, 2009
	/// </summary>
	public class NoticeReference : tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement
	{
		private readonly string mOrganization;
		private readonly List<int?> mNoticeNumbers = new List<int?>(0);

		public NoticeReference(Context aContext, string aOrganization, int?[] aNoticeNumbers) : base(aContext)
		{

			if (aOrganization == null)
			{
                throw new XMLSignatureRuntimeException("errors.nullElement", Constants.TAGX_ORGANIZATION);
			}

			if (aNoticeNumbers == null || aNoticeNumbers.Length < 1)
			{
                throw new XMLSignatureRuntimeException("errors.nullElement", Constants.TAGX_NOTICENUMBERS);
			}

			mOrganization = aOrganization;
            mNoticeNumbers.AddRange(new List<int?>(aNoticeNumbers));

			addLineBreak();
            insertTextElement(Constants.NS_XADES_1_3_2, Constants.TAGX_ORGANIZATION, mOrganization);

            Element numbersElm = insertElement(Constants.NS_XADES_1_3_2, Constants.TAGX_NOTICENUMBERS);
			addLineBreak(numbersElm);

			foreach (int? noticeNumber in mNoticeNumbers)
			{
                Element numberElm = createElement(Constants.NS_XADES_1_3_2, Constants.TAGX_INT);
				numberElm.InnerText = noticeNumber + "";
				numbersElm.AppendChild(numberElm);
				addLineBreak(numbersElm);
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
//ORIGINAL LINE: public NoticeReference(XmlElement aElement, tr.gov.tubitak.uekae.esya.api.xmlsignature.Context aContext) throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
		public NoticeReference(Element aElement, Context aContext) : base(aElement, aContext)
		{

            mOrganization = getChildText(Constants.NS_XADES_1_3_2, Constants.TAGX_ORGANIZATION);

            Element numbersElm = selectChildElement(Constants.NS_XADES_1_3_2, Constants.TAGX_NOTICENUMBERS);
            Element[] numberElmArr = XmlCommonUtil.selectNodes(numbersElm.FirstChild, Constants.NS_XADES_1_3_2, Constants.TAGX_INT);
			foreach (Element numberElm in numberElmArr)
			{
				string noticeNumberStr = XmlCommonUtil.getText(numberElm);
				int? noticeNumber = Convert.ToInt32(noticeNumberStr);
				mNoticeNumbers.Add(noticeNumber);
			}
		}


		public virtual string Organization
		{
			get
			{
				return mOrganization;
			}
		}

		public virtual IList<int?> NoticeNumbers
		{
			get
			{
				return mNoticeNumbers;
			}
		}

		public override string LocalName
		{
			get
			{
                return Constants.TAGX_NOTICEREF;
			}
		}
	}

}