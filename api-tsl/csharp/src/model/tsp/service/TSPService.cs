using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service.information;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service
{
    public class TSPService : BaseElement
    {
        private ServiceInformation serviceInformation;

        public TSPService(XmlDocument document, ServiceInformation iServiceInformation) : base(document)
        {
            serviceInformation = iServiceInformation;
            addLineBreak();
            mElement.AppendChild(serviceInformation.Element);
            addLineBreak();
        }

        public TSPService(XmlDocument document)
            : base(document)
        {
            addLineBreak();
        }

        public TSPService(XmlElement aElement) : base(aElement)
        {
            XmlNamespaceManager nsManager = TSLUtil.getInstance().getNamespaceManager(aElement.OwnerDocument);

            XmlNodeList nodeList = null;
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_SERVICEINFORMATION, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_SERVICEINFORMATION + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_SERVICEINFORMATION + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            serviceInformation = new ServiceInformation((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            //serviceInformation = new ServiceInformation(XmlUtil.getNextElement(aElement.FirstChild));
        }

        public override string LocalName
        {
            get { return Constants.TAG_TSPSERVICE; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public ServiceInformation ServiceInformation
        {
            get { return serviceInformation; }
            set
            {
                if(serviceInformation!=null)
                {
                    mElement.ReplaceChild(value.Element, serviceInformation.Element);
                    serviceInformation = value;
                }
                else
                {
                    serviceInformation = value;
                    mElement.AppendChild(serviceInformation.Element);
                    addLineBreak();
                }
            }
        }
    }
}
