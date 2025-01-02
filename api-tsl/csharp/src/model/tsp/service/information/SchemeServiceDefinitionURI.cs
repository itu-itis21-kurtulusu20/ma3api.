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
    public class SchemeServiceDefinitionURI : BaseElement
    {
        private readonly IList<TSLURI> schemeServiceDefinitionURIs = new List<TSLURI>();

        public SchemeServiceDefinitionURI(XmlDocument document, IList<TSLURI> iSchemeServiceDefinitionURIs) : base(document)
        {
            schemeServiceDefinitionURIs = iSchemeServiceDefinitionURIs;
            addLineBreak();
            foreach (var serviceDefinitionURI in schemeServiceDefinitionURIs)
            {
                mElement.AppendChild(serviceDefinitionURI.Element);
                addLineBreak();
            }
        }

        public SchemeServiceDefinitionURI(XmlDocument document)
            : base(document)
        {
            addLineBreak();
        }

        public SchemeServiceDefinitionURI(XmlElement aElement) : base(aElement)
        {
            if (mElement.HasChildNodes)
            {
                XmlNodeList childList = aElement.ChildNodes;
                foreach (XmlNode child in childList)
                {
                    schemeServiceDefinitionURIs.Add(new TSLURI((XmlElement)child));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_SCHEMESERVICEDEFINITIONURI + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public void RemoveAllServiceDefinitionURI()
        {
            mElement.RemoveAll();
            schemeServiceDefinitionURIs.Clear();
            addLineBreak();
        }

        public void addSchemeServiceDefinitionURI(TSLURI schemeServiceDefinitonURI)
        {
            schemeServiceDefinitionURIs.Add(schemeServiceDefinitonURI);
            mElement.AppendChild(schemeServiceDefinitonURI.Element);
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_SCHEMESERVICEDEFINITIONURI; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TSLURI> SchemeServiceDefinitionURIs
        {
            get { return schemeServiceDefinitionURIs; }
        }

        public TSLURI SchemeServiceDefinitionURIAt(int pos)
        {
            if (pos >= schemeServiceDefinitionURIs.Count)
            {
                return null;
            }
            else
            {
                return schemeServiceDefinitionURIs[pos];
            }
        }

        public string SchemeServiceDefinitionURIStrAt(int pos)
        {
            if (pos >= schemeServiceDefinitionURIs.Count)
            {
                return null;
            }
            else
            {
                return schemeServiceDefinitionURIs[pos].TslURI;
            }
        }
    }
}
