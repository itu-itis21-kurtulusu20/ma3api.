using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.tsp
{
    public class TrustServiceProviderList : BaseElement
    {
        private readonly IList<TrustServiceProvider> tspList = new List<TrustServiceProvider>();

        public TrustServiceProviderList(XmlDocument document, IList<TrustServiceProvider> iTSPList) : base(document)
        {
            tspList = iTSPList;
            addLineBreak();
            foreach (var tsp in tspList)
            {
                mElement.AppendChild(tsp.Element);
                addLineBreak();
            }
        }

        public TrustServiceProviderList(XmlDocument document)
            : base(document)
        {
            addLineBreak();
        }

        public TrustServiceProviderList(XmlElement aElement) : base(aElement)
        {
            if (mElement.HasChildNodes)
            {
                XmlNodeList childList = aElement.ChildNodes;
                foreach (XmlNode child in childList)
                {
                    tspList.Add(new TrustServiceProvider((XmlElement)child));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_TRUSTSERVICEPROVIDERLIST + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public void addTrustServiceProvider(TrustServiceProvider iTSP)
        {
            tspList.Add(iTSP);
            mElement.AppendChild(iTSP.Element);
            addLineBreak();
        }

        public void removeAllTSPs()
        {
            tspList.Clear();
            mElement.RemoveAll();
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_TRUSTSERVICEPROVIDERLIST; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TrustServiceProvider> TSPList
        {
            get { return tspList; }
        }

        public TrustServiceProvider TSPAt(int pos)
        {
            if (pos >= tspList.Count)
            {
                return null;
            }
            else
            {
                return tspList[pos];
            }
        }
    }
}
