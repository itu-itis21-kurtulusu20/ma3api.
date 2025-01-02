using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.information
{
    public class TSPName : BaseElement
    {
        private readonly IList<TSLName> tspNames = new List<TSLName>();

        public TSPName(XmlDocument document, IList<TSLName> iTSPNames) : base(document)
        {
            tspNames = iTSPNames;
            addLineBreak();
            foreach(var tspName in tspNames)
            {
                mElement.AppendChild(tspName.Element);
                addLineBreak();
            }
        }

        public TSPName(XmlDocument document)
            : base(document)
        {
            addLineBreak();
        }

        public TSPName(XmlElement aElement) : base(aElement)
        {
            if (mElement.HasChildNodes)
            {
                IList<XmlElement> childNodes = XmlUtil.selectChildElements(aElement);

                foreach (var xmlElement in childNodes)
                {
                    tspNames.Add(new TSLName(xmlElement));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_TSPNAME + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public void RemoveAllTSPNames()
        {
            mElement.RemoveAll();
            tspNames.Clear();
            addLineBreak();
        }

        public void addTSPName(TSLName iTSLName)
        {
            tspNames.Add(iTSLName);
            mElement.AppendChild(iTSLName.Element);
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_TSPNAME; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TSLName> TSPNames
        {
            get { return tspNames; }
        }

        public TSLName TSPNameAt(int pos)
        {
            if(pos>=tspNames.Count)
            {
                return null;
            }
            else
            {
                return tspNames[pos];
            }
        }
    }
}
