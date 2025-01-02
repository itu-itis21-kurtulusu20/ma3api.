using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation
{
    public class TSLListIssueDateTime : BaseElement
    {
        private readonly DateTime issueDateTime;
        private readonly string greagorianDateTime;

        public TSLListIssueDateTime(XmlDocument document, DateTime iIssueDateTime) : base(document)
        {
            issueDateTime = iIssueDateTime;
            greagorianDateTime = XmlUtil.datetime2xmlgregstr(issueDateTime);
            mElement.InnerText = greagorianDateTime;
        }

        public TSLListIssueDateTime(XmlDocument document, string iGregorianIssueDateTime)
            : base(document)
        {
            greagorianDateTime = iGregorianIssueDateTime;
            mElement.InnerText = greagorianDateTime;
            issueDateTime = XmlUtil.getDate(mElement);
        }

        public TSLListIssueDateTime(XmlDocument document)
            : base(document)
        {
        }

        public TSLListIssueDateTime(XmlElement aElement) : base(aElement)
        {
            greagorianDateTime = XmlUtil.getText(aElement);
            issueDateTime = XmlUtil.getDate(aElement);
        }

        public override string LocalName
        {
            get { return Constants.TAG_LISTISSUEDATETIME; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public string GregorianDateTime
        {
            get { return greagorianDateTime; }
        }

        public DateTime DateTime
        {
            get { return issueDateTime; }
        }
    }
}
