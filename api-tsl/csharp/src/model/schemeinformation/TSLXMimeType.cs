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
    public class TSLXMimeType : BaseElement
    {
        private readonly string mimeType;

        public TSLXMimeType(XmlDocument document, string iMimeType) : base(document)
        {
            mimeType = iMimeType;
            mElement.InnerText = mimeType;
        }

        public TSLXMimeType(XmlElement aElement) : base(aElement)
        {
            mimeType = XmlUtil.getText(aElement);
        }

        public override string LocalName
        {
            get { return Constants.TAG_MIMETYPE; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSLX;
            }
        }

        public string MimeType
        {
            get { return mimeType; }
        }
    }
}
