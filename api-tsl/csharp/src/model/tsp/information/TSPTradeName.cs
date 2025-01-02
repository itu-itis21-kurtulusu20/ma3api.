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
    public class TSPTradeName : BaseElement
    {
        private readonly IList<TSLName> tspTradeNames = new List<TSLName>();

        public TSPTradeName(XmlDocument document, IList<TSLName> iTSPTradeNames)
            : base(document)
        {
            tspTradeNames = iTSPTradeNames;
            addLineBreak();
            foreach (var tspTradeName in tspTradeNames)
            {
                mElement.AppendChild(tspTradeName.Element);
                addLineBreak();
            }
        }

        public TSPTradeName(XmlDocument document)
            : base(document)
        {
            addLineBreak();
        }


        public TSPTradeName(XmlElement aElement) : base(aElement)
        {
            if (mElement.HasChildNodes)
            {
                IList<XmlElement> childNodes = XmlUtil.selectChildElements(aElement);

                foreach (var xmlElement in childNodes)
                {
                    tspTradeNames.Add(new TSLName(xmlElement));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_TSPTRADENAME + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public void RemoveAllTradeNames()
        {
            mElement.RemoveAll();
            tspTradeNames.Clear();
            addLineBreak();
        }

        public void addTSPTradeName(TSLName iTSLTradeName)
        {
            tspTradeNames.Add(iTSLTradeName);
            mElement.AppendChild(iTSLTradeName.Element);
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_TSPTRADENAME; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TSLName> TSPTradeNames
        {
            get { return tspTradeNames; }
        }

        public TSLName TSPTradeNameAt(int pos)
        {
            if (pos >= tspTradeNames.Count)
            {
                return null;
            }
            else
            {
                return tspTradeNames[pos];
            }
        }
    }
}
