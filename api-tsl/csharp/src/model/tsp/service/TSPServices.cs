using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service
{
    public class TSPServices : BaseElement
    {
        private readonly IList<TSPService> tspServiceList = new List<TSPService>();

        public TSPServices(XmlDocument document, IList<TSPService> iTSPServiceList) : base(document)
        {
            tspServiceList = iTSPServiceList;
            addLineBreak();
            foreach(var tspService in tspServiceList)
            {
                mElement.AppendChild(tspService.Element);
                addLineBreak();
            }
        }

        public TSPServices(XmlDocument document)
            : base(document)
        {
            addLineBreak();
        }


        public TSPServices(XmlElement aElement) : base(aElement)
        {
            if (mElement.HasChildNodes)
            {
                XmlNodeList childList = aElement.ChildNodes;
                foreach (XmlNode child in childList)
                {
                    tspServiceList.Add(new TSPService((XmlElement)child));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_TSPSERVICES + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public void addTSPService(TSPService iTSPService)
        {
            tspServiceList.Add(iTSPService);
            mElement.AppendChild(iTSPService.Element);
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_TSPSERVICES; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TSPService> TSPSerivceList
        {
            get { return tspServiceList; }
        }

        public TSPService TSPServiceAt(int pos)
        {
            if(pos>=tspServiceList.Count)
            {
                return null;
            }
            else
            {
                return tspServiceList[pos];
            }
        }
    }
}
