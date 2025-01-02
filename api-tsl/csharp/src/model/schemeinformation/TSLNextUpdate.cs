using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.tsl.util;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation
{
    public class TSLNextUpdate : BaseElement
    {
        private readonly bool mIsClosed = true;
        private readonly XmlElement dateTimeElement;
        private readonly DateTime nextUpdate;
        private readonly string gregorianNextUpdate;

        public TSLNextUpdate(XmlDocument document, DateTime iNextUpdate) : base(document)
        {
            mIsClosed = false;
            nextUpdate = iNextUpdate;
            gregorianNextUpdate = XmlUtil.datetime2xmlgregstr(nextUpdate);
            addLineBreak();
            dateTimeElement = insertTextElement(Constants.NS_TSL, Constants.TAG_DATETIME, gregorianNextUpdate);
        }

        public TSLNextUpdate(XmlDocument document, string iGregorianDateTime)
            : base(document)
        {
            mIsClosed = true;
            gregorianNextUpdate = iGregorianDateTime;
            addLineBreak();
            dateTimeElement = insertTextElement(Constants.NS_TSL, Constants.TAG_DATETIME, gregorianNextUpdate);
            nextUpdate = XmlUtil.getDate(dateTimeElement);
        }

        public TSLNextUpdate(XmlDocument document)
            : base(document)
        {
        }

        public TSLNextUpdate(XmlElement aElement) : base(aElement)
        {
            dateTimeElement = XmlUtil.getNextElement(aElement.FirstChild);
            if(dateTimeElement != null)
            {
                mIsClosed = false;
                gregorianNextUpdate = XmlUtil.getText(dateTimeElement);
                nextUpdate = XmlUtil.getDate(dateTimeElement);
            }
        }

        public override string LocalName
        {
            get { return Constants.TAG_NEXTUPDATE; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public string GregorianNextUpdate
        {
            get { return gregorianNextUpdate; }
        }

        public DateTime NextUpdate
        {
            get { return nextUpdate; }
        }

        public bool isClosed
        {
            get { return mIsClosed; }
        }
    }
}
