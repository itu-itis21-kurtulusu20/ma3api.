using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation
{
    public class TSLSchemeInformationURI : BaseElement
    {
        private readonly IList<TSLURI> informationURIs = new List<TSLURI>(); 

        public TSLSchemeInformationURI(XmlDocument document, IList<TSLURI> iInformationURIs) : base(document)
        {
            addLineBreak();
            informationURIs = iInformationURIs;

            for (int i = 0; i < informationURIs.Count; i++)
            {
                mElement.AppendChild(informationURIs[i].Element);
                addLineBreak();
            }
        }

        public TSLSchemeInformationURI(XmlDocument document)
            : base(document)
        {
            addLineBreak();
        }

        public TSLSchemeInformationURI(XmlElement aElement)
            : base(aElement)
        {
            if(mElement.HasChildNodes)
            {
                XmlNodeList childList = aElement.ChildNodes;
                foreach (XmlNode child in childList)
                {
                    informationURIs.Add(new TSLURI((XmlElement)child));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_SCHEMEINFORMATIONURI + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public  void RemoveAllURIs()
        {
            informationURIs.Clear();
            mElement.RemoveAll();
            addLineBreak();
        }

        public void addInformationURI(TSLURI tslURI)
        {
            informationURIs.Add(tslURI);
            mElement.AppendChild(tslURI.Element);
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_SCHEMEINFORMATIONURI; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TSLURI> InformationURIs
        {
            get { return informationURIs; }
        }

        public TSLURI InformationURIAt(int pos)
        {
            if (pos >= informationURIs.Count)
            {
                return null;
            }
            else
            {
                return informationURIs[pos];
            }
        }

        public string InformationURIStrAt(int pos)
        {
            if (pos >= informationURIs.Count)
            {
                return null;
            }
            else
            {
                return informationURIs[pos].TslURI;
            }
        }
    }
}
