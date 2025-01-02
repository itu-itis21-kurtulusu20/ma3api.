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
    public class TSLURI : BaseElement
    {
        private readonly string tslURI;
        private readonly string language;

        public TSLURI(XmlElement aElement) : base(aElement)
        {
            tslURI = XmlUtil.getText(aElement);
            if (aElement.HasAttribute(Constants.XML_PREFIX + Constants.TSL_LANG_ATTR))
            {
                language = aElement.GetAttribute(Constants.XML_PREFIX + Constants.TSL_LANG_ATTR);
            }
            //because tsluri tag may be without language attribute!!!
            //else
            //{
            //    throw new TSLException("Language Attribute could not be found!");
            //}
        }

        public TSLURI(XmlDocument document, string iLang, string iTSLURI)
            : base(document)
        {
            tslURI = iTSLURI;
            language = iLang;

            if(mElement.Attributes.Count>0)
            {
                throw new TSLException("should not have a default attribute");
            }
            mElement.SetAttribute(Constants.XML_PREFIX + Constants.TSL_LANG_ATTR, language);
            mElement.InnerText = tslURI;
        }

        public TSLURI(XmlDocument document, string iTSLURI)
            : base(document)
        {
            tslURI = iTSLURI;
            language = null;

            mElement.InnerText = tslURI;
        }

        public override string LocalName
        {
            get { return Constants.TSL_URI; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public string TslURI
        {
            get { return tslURI; }
        }

        public string Language
        {
            get { return language; }
        }
    }
}
