using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation
{
    public class TSLPolicyOrLegalNotice : BaseElement
    {
        private readonly IList<TSLLegalNotice> legalNotices = new List<TSLLegalNotice>();

        public TSLPolicyOrLegalNotice(XmlDocument document, IList<TSLLegalNotice> iLegalNotices) : base(document)
        {
            legalNotices = iLegalNotices;

            addLineBreak();
            foreach (var tslLegalNotice in legalNotices)
            {
                mElement.AppendChild(tslLegalNotice.Element);
                addLineBreak();
            }
        }

        public TSLPolicyOrLegalNotice(XmlDocument document) : base(document)
        {
            addLineBreak();
        }

        public TSLPolicyOrLegalNotice(XmlElement aElement) : base(aElement)
        {
            if(mElement.HasChildNodes)
            {
                IList<XmlElement> childNodes = XmlUtil.selectChildElements(aElement);

                foreach (var xmlElement in childNodes)
                {
                    legalNotices.Add(new TSLLegalNotice(xmlElement));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_TSLLEGALNOTICE + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public void RemoveAllNotices()
        {
            legalNotices.Clear();
            mElement.RemoveAll();
            addLineBreak();
        }

        public void addLegalNotice(TSLLegalNotice iLegalNotice)
        {
            legalNotices.Add(iLegalNotice);
            mElement.AppendChild(iLegalNotice.Element);
            addLineBreak();
        }

        public void addLegalNoticeString(string iLanguage, string iLegalNoticeStr)
        {
            TSLLegalNotice legalNotice = new TSLLegalNotice(mElement.OwnerDocument,iLanguage,iLegalNoticeStr);
            legalNotices.Add(legalNotice);
            mElement.AppendChild(legalNotice.Element);
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_POLICYORLEGALNOTICE; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TSLLegalNotice> LegalNoticeList
        {
            get { return legalNotices; }
        }

        public TSLLegalNotice LegalNoticeAt(int pos)
        {
            if (pos >= legalNotices.Count)
            {
                return null;
            }
            else
            {
                return legalNotices[pos];
            }
        }
    }
}
