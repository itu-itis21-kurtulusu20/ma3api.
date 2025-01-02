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
    public class TSLType : BaseElement
    {
        private string mTSLType;

        public TSLType(XmlDocument document, string iTSLType) : base(document)
        {
            mTSLType = iTSLType;
            mElement.InnerText = mTSLType;
        }

        public TSLType(XmlDocument document) : base(document)
        {
        }

        public TSLType(XmlElement aElement) : base(aElement)
        {
            mTSLType = XmlUtil.getText(aElement);
        }

        public override string LocalName
        {
            get { return Constants.TAG_TSLTYPE; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public string TslType
        {
            get { return mTSLType; }
            set 
            { 
                mTSLType = value;
                mElement.InnerText = mTSLType;
            }
        }
    }
}
