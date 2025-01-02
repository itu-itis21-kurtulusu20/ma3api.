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
    public class TSLSchemeTypeCommunityRules : BaseElement
    {
        private readonly IList<TSLURI> uriList = new List<TSLURI>();

        public TSLSchemeTypeCommunityRules(XmlDocument document, IList<TSLURI> iURIList) : base(document)
        {
            uriList = iURIList;

            addLineBreak();
            foreach (var tsluri in uriList)
            {
                mElement.AppendChild(tsluri.Element);
                addLineBreak();
            }
        }

        public TSLSchemeTypeCommunityRules(XmlDocument document) : base(document)
        {
            addLineBreak();
        }

        public TSLSchemeTypeCommunityRules(XmlElement aElement) : base(aElement)
        {
            if (aElement.HasChildNodes)
            {
                IList<XmlElement> childNodes = XmlUtil.selectChildElements(aElement);
                foreach (var xmlElement in childNodes)
                {
                    uriList.Add(new TSLURI(xmlElement));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_SCHEMETYPECOMMUNITYRULES + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public void RemoveAllURIs()
        {
            uriList.Clear();
            mElement.RemoveAll();
            addLineBreak();
        }

        public void addRuleURI(TSLURI iTSLURI)
        {
            uriList.Add(iTSLURI);
            mElement.AppendChild(iTSLURI.Element);
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_SCHEMETYPECOMMUNITYRULES; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TSLURI> RuleURIs
        {
            get { return uriList; }
        }

        public TSLURI RuleURIAt(int pos)
        {
            if (pos >= uriList.Count)
            {
                return null;
            }
            else
            {
                return uriList[pos];
            }
        }

        public string RuleURIAtStr(int pos)
        {
            if (pos >= uriList.Count)
            {
                return null;
            }
            else
            {
                return uriList[pos].TslURI;
            }
        }
    }
}
