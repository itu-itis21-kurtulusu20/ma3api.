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
    public class TSLSchemeTerritory : BaseElement
    {
        private string language;

        public TSLSchemeTerritory(XmlDocument document, string iLanguage) : base(document)
        {
            language = iLanguage;
            mElement.InnerText = language;
        }

        public TSLSchemeTerritory(XmlDocument document)
            : base(document)
        {
        }

        public TSLSchemeTerritory(XmlElement aElement) : base(aElement)
        {
            language = XmlUtil.getText(aElement);
        }

        public override string LocalName
        {
            get { return Constants.TAG_SCHEMETERRITORY; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public string TerritoryLanguage
        {
            get { return language; }
            set
            {
                language = value;
                mElement.InnerText = language;
            }
        }
    }
}
