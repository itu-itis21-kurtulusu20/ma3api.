using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service.information
{
    public class ServiceName : BaseElement
    {
        private readonly IList<TSLName> serviceNames = new List<TSLName>();

        public ServiceName(XmlDocument document, IList<TSLName> iServiceNames) : base(document)
        {
            serviceNames = iServiceNames;
            addLineBreak();
            foreach (var tspName in serviceNames)
            {
                mElement.AppendChild(tspName.Element);
                addLineBreak();
            }
        }

        public ServiceName(XmlDocument document)
            : base(document)
        {
            addLineBreak();
        }

        public ServiceName(XmlElement aElement)
            : base(aElement)
        {
            if (mElement.HasChildNodes)
            {
                IList<XmlElement> childNodes = XmlUtil.selectChildElements(aElement);

                foreach (var xmlElement in childNodes)
                {
                    serviceNames.Add(new TSLName(xmlElement));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_SERVICENAME + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public void RemoveAllServiceNames()
        {
            mElement.RemoveAll();
            serviceNames.Clear();
            addLineBreak();
        }

        public void addServiceName(TSLName iServiceName)
        {
            serviceNames.Add(iServiceName);
            mElement.AppendChild(iServiceName.Element);
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_SERVICENAME; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TSLName> ServiceNames
        {
            get { return serviceNames; }
        }

        public TSLName ServiceNameAt(int pos)
        {
            if (pos >= serviceNames.Count)
            {
                return null;
            }
            else
            {
                return serviceNames[pos];
            }
        }
    }
}
