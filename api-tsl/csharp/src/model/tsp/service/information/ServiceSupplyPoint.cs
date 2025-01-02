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
    public class ServiceSupplyPoint : BaseElement
    {
        private string mServiceSupplyPoint;

        public ServiceSupplyPoint(XmlDocument document, string iSSPoint)
            : base(document)
        {
            mServiceSupplyPoint = iSSPoint;
            mElement.InnerText = mServiceSupplyPoint;
        }

        public ServiceSupplyPoint(XmlDocument document)
            : base(document)
        {
        }

        public ServiceSupplyPoint(XmlElement aElement)
            : base(aElement)
        {
            mServiceSupplyPoint = XmlUtil.getText(aElement);
        }

        public override string LocalName
        {
            get { return Constants.TAG_SERVICESUPPLYPOINT; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public string SSPoint
        {
            get { return mServiceSupplyPoint; }
            set
            {
                mServiceSupplyPoint = value;
                mElement.InnerText = mServiceSupplyPoint;
            }
        }
    }
}
