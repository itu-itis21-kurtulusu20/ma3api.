using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation
{
    public class TSLSchemeName : BaseElement
    {
        private readonly IList<TSLName> schemeNames = new List<TSLName>(); 

        public TSLSchemeName(XmlDocument document, IList<TSLName> iSchemeNames) : base(document)
        {
            addLineBreak();
            schemeNames = iSchemeNames;

            foreach (var schemeName in schemeNames)
            {
                mElement.AppendChild(schemeName.Element);
                addLineBreak();
            }
        }

        public TSLSchemeName(XmlDocument document)
            : base(document)
        {
            addLineBreak();
        }

        public TSLSchemeName(XmlElement aElement)
            : base(aElement)
        {
            if(mElement.HasChildNodes)
            {
                IList<XmlElement> childNodes = XmlUtil.selectChildElements(aElement);

                foreach (var xmlElement in childNodes)
                {
                    schemeNames.Add(new TSLName(xmlElement));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_SCHEMENAME + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public void addName(TSLName tslName)
        {
            schemeNames.Add(tslName);
            mElement.AppendChild(tslName.Element);
            addLineBreak();
        }

        public void RemoveAllNames()
        {
            schemeNames.Clear();
            mElement.RemoveAll();
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_SCHEMENAME; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TSLName> SchemeNames
        {
            get { return schemeNames; }
        }
    }
}
