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
    public class TSLAdditionalInformation : BaseElement
    {
        readonly IList<TSLOtherInformation> otherInformationList = new List<TSLOtherInformation>();

        public TSLAdditionalInformation(XmlDocument document, IList<TSLOtherInformation> iOtherInformationList) : base(document)
        {
            otherInformationList = iOtherInformationList;
            addLineBreak();
            foreach (var tslOtherInformation in otherInformationList)
            {
                mElement.AppendChild(tslOtherInformation.Element);
                addLineBreak();
            }
        }

        public TSLAdditionalInformation(XmlDocument document) : base(document)
        {
            addLineBreak();
        }

        public void addOtherInformation(TSLOtherInformation information)
        {
            otherInformationList.Add(information);
            mElement.AppendChild(information.Element);
            addLineBreak();
        }

        public TSLAdditionalInformation(XmlElement aElement) : base(aElement)
        {
            if (mElement.HasChildNodes)
            {
                IList<XmlElement> childNodes = XmlUtil.selectChildElements(aElement);

                foreach (var xmlElement in childNodes)
                {
                    otherInformationList.Add(new TSLOtherInformation(xmlElement));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_OTHERINFORMATION + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public override string LocalName
        {
            get { return Constants.TAG_TSLADDITIONALINFORMATION; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TSLOtherInformation> OtherInformationList
        {
            get { return otherInformationList; }
        }

        public TSLOtherInformation OtherInformationAt(int pos)
        {
            if(pos >= otherInformationList.Count)
            {
                return null;
            }
            else
            {
                return otherInformationList[pos];
            }
        }
    }
}
