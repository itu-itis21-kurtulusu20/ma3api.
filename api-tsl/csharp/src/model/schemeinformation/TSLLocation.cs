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
    public class TSLLocation : BaseElement
    {
        private readonly string mTSLLocation;

        public TSLLocation(XmlDocument document, string iTSLLocation) : base(document)
        {
            mTSLLocation = iTSLLocation;
            mElement.InnerText = mTSLLocation;
        }

        public TSLLocation(XmlElement aElement) : base(aElement)
        {
            mTSLLocation = XmlUtil.getText(aElement);
        }

        public override string LocalName
        {
            get { return Constants.TAG_TSLLOCATION; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public string tslLocation
        {
            get { return mTSLLocation; }
        }
    }
}
