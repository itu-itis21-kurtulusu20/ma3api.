using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service.information
{
    public class TSPServiceDefinitionURI : BaseElement
    {
        private readonly IList<TSLURI> tspServiceDefinitionURIs = new List<TSLURI>();

        public TSPServiceDefinitionURI(XmlDocument document, IList<TSLURI> iTSPServiceDefinitionURIs) : base(document)
        {
            tspServiceDefinitionURIs = iTSPServiceDefinitionURIs;
            addLineBreak();
            foreach (var tspServiceDefinitionURI in tspServiceDefinitionURIs)
            {
                mElement.AppendChild(tspServiceDefinitionURI.Element);
                addLineBreak();
            }
        }

        public TSPServiceDefinitionURI(XmlDocument document)
            : base(document)
        {
            addLineBreak();
        }

        public TSPServiceDefinitionURI(XmlElement aElement)
            : base(aElement)
        {
            if (mElement.HasChildNodes)
            {
                XmlNodeList childList = aElement.ChildNodes;
                foreach (XmlNode child in childList)
                {
                    tspServiceDefinitionURIs.Add(new TSLURI((XmlElement)child));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_TSPSERVICEDEFINITONURI + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public void RemoveAllServiceDefinitionURI()
        {
            mElement.RemoveAll();
            tspServiceDefinitionURIs.Clear();
            addLineBreak();
        }

        public void addServiceDefinitionURI(TSLURI tspServiceDefinitonURI)
        {
            tspServiceDefinitionURIs.Add(tspServiceDefinitonURI);
            mElement.AppendChild(tspServiceDefinitonURI.Element);
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_TSPSERVICEDEFINITONURI; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TSLURI> TSPServiceDefinitionURIs
        {
            get { return tspServiceDefinitionURIs; }
        }

        public TSLURI TSPServiceDefinitionURIAt(int pos)
        {
            if (pos >= tspServiceDefinitionURIs.Count)
            {
                return null;
            }
            else
            {
                return tspServiceDefinitionURIs[pos];
            }
        }

        public string TSPServiceDefinitionURIStrAt(int pos)
        {
            if (pos >= tspServiceDefinitionURIs.Count)
            {
                return null;
            }
            else
            {
                return tspServiceDefinitionURIs[pos].TslURI;
            }
        }
    }
}
