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
    public class PointersToOtherTSL : BaseElement
    {
        readonly IList<OtherTSLPointer> otherTSLPointerList = new List<OtherTSLPointer>();

        public PointersToOtherTSL(XmlDocument document, IList<OtherTSLPointer> iOtherTSLPointerList) : base(document)
        {
            otherTSLPointerList = iOtherTSLPointerList;
            addLineBreak();
            foreach (var otherTslPointer in otherTSLPointerList)
            {
                mElement.AppendChild(otherTslPointer.Element);
                addLineBreak();
            }
        }

        public PointersToOtherTSL(XmlDocument document) : base(document)
        {
            addLineBreak();
        }

        public PointersToOtherTSL(XmlElement aElement) : base(aElement)
        {
            if (mElement.HasChildNodes)
            {
                IList<XmlElement> childNodes = XmlUtil.selectChildElements(aElement);

                foreach (var xmlElement in childNodes)
                {
                    otherTSLPointerList.Add(new OtherTSLPointer(xmlElement));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_OTHERTSLPOINTER + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public void addTSLPointer(OtherTSLPointer tslPointer)
        {
            otherTSLPointerList.Add(tslPointer);
            mElement.AppendChild(tslPointer.Element);
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_POINTERSTOOTHERTSL; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<OtherTSLPointer> OtherTSLPointerList
        {
            get { return otherTSLPointerList; }
        }

        public OtherTSLPointer OtherTSLPointerAt(int pos)
        {
            if(pos>=otherTSLPointerList.Count)
            {
                return null;
            }
            else
            {
                return otherTSLPointerList[pos];
            }
        }
    }
}
