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
    public class ServiceSupplyPoints : BaseElement
    {
        readonly IList<string> ssPoints = new List<string>();
        readonly IList<ServiceSupplyPoint> ssPointElements = new List<ServiceSupplyPoint>(); 

        public ServiceSupplyPoints(XmlDocument document, IList<string> iSSPoints) : base(document)
        {
            addLineBreak();
            ssPoints = iSSPoints;
            foreach (var ssPoint in ssPoints)
            {
                XmlElement serviceSupplyPoint = mDocument.CreateElement(Constants.TSL_PREFIX + Constants.TAG_SERVICESUPPLYPOINT, Constants.NS_TSL);
                ssPointElements.Add(new ServiceSupplyPoint(serviceSupplyPoint));
                serviceSupplyPoint.InnerText = ssPoint;
                mElement.AppendChild(serviceSupplyPoint);
                addLineBreak();
            }
        }

        public ServiceSupplyPoints(XmlDocument document, IList<ServiceSupplyPoint> iSSPointElements) : base(document)
        {
            addLineBreak();
            ssPointElements = iSSPointElements;
            foreach (var ssPointElement in ssPointElements)
            {
                ssPointElements.Add(ssPointElement);
                mElement.AppendChild(ssPointElement.Element);
                addLineBreak();
            }
        }

        public ServiceSupplyPoints(XmlDocument document)
            : base(document)
        {
            addLineBreak();
        }

        public ServiceSupplyPoints(XmlElement aElement) : base(aElement)
        {
            if (mElement.HasChildNodes)
            {
                IList<XmlElement> childNodes = XmlUtil.selectChildElements(aElement);

                foreach (var xmlElement in childNodes)
                {
                    ssPointElements.Add(new ServiceSupplyPoint(xmlElement));
                    ssPoints.Add(xmlElement.InnerText);
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_SERVICESUPPLYPOINTS + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public void RemoveAllServiceSupplyPoints()
        {
            mElement.RemoveAll();
            ssPoints.Clear();
            ssPointElements.Clear();
            addLineBreak();
        }

        public void addServiceSupplyPoint(string iSSPoint)
        {
            ssPoints.Add(iSSPoint);
            XmlElement serviceSupplyPoint = mDocument.CreateElement(Constants.TSL_PREFIX + Constants.TAG_SERVICESUPPLYPOINT, Constants.NS_TSL);
            serviceSupplyPoint.InnerText = iSSPoint;
            ssPointElements.Add(new ServiceSupplyPoint(serviceSupplyPoint));
            mElement.AppendChild(serviceSupplyPoint);
            addLineBreak();
        }

        public void addServiceSupplyPointElement(ServiceSupplyPoint ssPointElement)
        {
            ssPointElements.Add(ssPointElement);
            ssPoints.Add(ssPointElement.SSPoint);
            mElement.AppendChild(ssPointElement.Element);
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_SERVICESUPPLYPOINTS; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<string> ServiceSupplyPointsStr
        {
            get { return ssPoints; }
        }

        public IList<ServiceSupplyPoint>  ServiceSupplyPointElements
        {
            get { return ssPointElements; }
        }
    }
}
