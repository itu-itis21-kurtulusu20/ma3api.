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
    public class TSLName : BaseElement
    {
        private readonly string tslName;
        private readonly string language;

        public TSLName(XmlElement aElement) : base(aElement)
        {
            tslName = XmlUtil.getText(aElement);
            if (aElement.HasAttribute(Constants.XML_PREFIX + Constants.TSL_LANG_ATTR))
            {
                language = aElement.GetAttribute(Constants.XML_PREFIX + Constants.TSL_LANG_ATTR);
            }
            else
            {
                throw new TSLException("Language Attribute could not be found!");
            }
        }

        public TSLName(XmlDocument document, string iLang, string iTSLName) : base(document)
        {
            tslName = iTSLName;
            language = iLang;

            if(mElement.Attributes.Count>0)
            {
                throw new TSLException("should not have a default attribute");
            }
            mElement.SetAttribute(Constants.XML_PREFIX + Constants.TSL_LANG_ATTR, language);
            mElement.InnerText = tslName;
        }

        public override string LocalName
        {
            get { return Constants.TSL_NAME; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public string TslName
        {
            get { return tslName; }
        }

        public string Language
        {
            get { return language; }
        }
    }
}
