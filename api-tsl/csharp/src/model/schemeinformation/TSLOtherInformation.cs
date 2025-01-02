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
    public class TSLOtherInformation : BaseElement
    {
        private readonly XmlElement informationElement;

        public TSLOtherInformation(XmlDocument document, XmlElement iInformationElement) : base(document)
        {
            informationElement = iInformationElement;
            addLineBreak();
            mElement.AppendChild(informationElement);
            addLineBreak();
        }

        public TSLOtherInformation(XmlDocument document)
            : base(document)
        {
        }

        public TSLOtherInformation(XmlElement aElement) : base(aElement)
        {
            informationElement = XmlUtil.getNextElement(aElement.FirstChild);
        }

        public override string LocalName
        {
            get { return Constants.TAG_OTHERINFORMATION; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public XmlElement InformationElement
        {
            get { return informationElement; }
        }
    }
}
