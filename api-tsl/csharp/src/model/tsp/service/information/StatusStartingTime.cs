using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service.information
{
    public class StatusStartingTime : BaseElement
    {
        private readonly DateTime startingDateTime;
        private readonly string greagorianDateTime;

        public StatusStartingTime(XmlDocument document, DateTime iIssueDateTime) : base(document)
        {
            startingDateTime = iIssueDateTime.AddMilliseconds(-iIssueDateTime.Millisecond);
            greagorianDateTime = XmlUtil.datetime2xmlgregstr(startingDateTime);
            mElement.InnerText = greagorianDateTime;
        }

        public StatusStartingTime(XmlDocument document, string iGregorianIssueDateTime)
            : base(document)
        {
            greagorianDateTime = iGregorianIssueDateTime;
            mElement.InnerText = greagorianDateTime;
            startingDateTime = XmlUtil.getDate(mElement);
        }

        public StatusStartingTime(XmlElement aElement)
            : base(aElement)
        {
            greagorianDateTime = XmlUtil.getText(aElement);
            startingDateTime = XmlUtil.getDate(aElement);
        }


        public override string LocalName
        {
            get { return Constants.TAG_STATUSSTARTINGTIME; }
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
            get { return startingDateTime; }
        }
    }
}
