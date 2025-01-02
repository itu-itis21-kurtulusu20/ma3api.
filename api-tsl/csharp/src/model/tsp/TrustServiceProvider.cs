using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.information;
using tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.tsp
{
    public class TrustServiceProvider : BaseElement
    {
        private TSPInformation tspInformation;
        private TSPServices tspServices;

        public TrustServiceProvider(XmlDocument document, TSPInformation iTSPInformation, TSPServices iTSPServices) : base(document)
        {
            tspInformation = iTSPInformation;
            tspServices = iTSPServices;
            addLineBreak();
            mElement.AppendChild(tspInformation.Element);
            addLineBreak();
            mElement.AppendChild(tspServices.Element);
            addLineBreak();
        }

        public TrustServiceProvider(XmlElement aElement)
            : base(aElement)
        {
            XmlNamespaceManager nsManager = TSLUtil.getInstance().getNamespaceManager(aElement.OwnerDocument);

            XmlNodeList nodeList = null;
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_TSPINFORMATION, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_TSPINFORMATION + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_TSPINFORMATION + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            tspInformation = new TSPInformation((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_TSPSERVICES, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_TSPSERVICES + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_TSPSERVICES + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            tspServices = new TSPServices((XmlElement)nodeList[0]);
            ///
            //tspInformation = new TSPInformation(XmlUtil.getNextElement(aElement.FirstChild));
            //tspServices = new TSPServices(XmlUtil.getNextElement(tspInformation.Element.NextSibling));
        }

        public TrustServiceProvider(XmlDocument document)
            : base(document)
        {
            addLineBreak();
        }

        public void RemoveAllChildNodes()
        {
            mElement.RemoveAll();
            tspInformation = null;
            tspServices = null;
            addLineBreak();
        }

        public void addTSPInformation(TSPInformation iTSPInformation)
        {
            tspInformation = iTSPInformation;
            mElement.AppendChild(tspInformation.Element);
            addLineBreak();
        }

        public void addTSPServices(TSPServices iTSPServices)
        {
            tspServices = iTSPServices;
            mElement.AppendChild(tspServices.Element);
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_TRUSTSERVICEPROVIDER; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public TSPInformation TSPInformation
        {
            get { return tspInformation; }
        }

        public TSPServices TSPServices
        {
            get { return tspServices; }
        }
    }
}
