using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation
{
    public class TSLLegalNotice : BaseElement
    {
        private readonly string legalNotice;
        private readonly string language;

        public TSLLegalNotice(XmlDocument document, string iLanguage, string iLegalNotice) : base(document)
        {
            language = iLanguage;
            legalNotice = iLegalNotice;

            if (mElement.Attributes.Count > 0)
            {
                throw new TSLException("should not have a default attribute");
            }
            mElement.SetAttribute(Constants.XML_PREFIX + Constants.TSL_LANG_ATTR, language);
            mElement.InnerText = legalNotice;          

        }

        public TSLLegalNotice(XmlElement aElement) : base(aElement)
        {
            legalNotice = XmlUtil.getText(aElement);
            if (aElement.HasAttribute(Constants.XML_PREFIX + Constants.TSL_LANG_ATTR))
            {
                language = aElement.GetAttribute(Constants.XML_PREFIX + Constants.TSL_LANG_ATTR);
            }
            else
            {
                throw new TSLException("Language Attribute could not be found!");
            }
        }

        public override string LocalName
        {
            get { return Constants.TAG_TSLLEGALNOTICE; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public string LegalNotice
        {
            get { return legalNotice; }
        }

        public string Language
        {
            get { return language; }
        }
    }
}
