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
    public class TSLDistributionPoints : BaseElement
    {
        readonly IList<TSLURI> distributionPoints = new List<TSLURI>(); 

        public TSLDistributionPoints(XmlDocument document,IList<TSLURI> iDistributionPoints) : base(document)
        {
            distributionPoints = iDistributionPoints;
            addLineBreak();
            foreach (var distributionPoint in distributionPoints)
            {
                mElement.AppendChild(distributionPoint.Element);
                addLineBreak();

            }
        }

        public TSLDistributionPoints(XmlDocument document)
            : base(document)
        {
            addLineBreak();
        }

        public TSLDistributionPoints(XmlElement aElement) : base(aElement)
        {
            if (mElement.HasChildNodes)
            {
                IList<XmlElement> childNodes = XmlUtil.selectChildElements(aElement);

                foreach (var xmlElement in childNodes)
                {
                    distributionPoints.Add(new TSLURI(xmlElement));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_DISTRIBUTIONPOINTS + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public void RemoveAllDistributionPoints()
        {
            distributionPoints.Clear();
            mElement.RemoveAll();
            addLineBreak();
        }

        public void addDistributionPoint(TSLURI uri)
        {
            distributionPoints.Add(uri);
            mElement.AppendChild(uri.Element);
            addLineBreak();
        }

        public void addDistributionPoint(string uriStr)
        {
            TSLURI uri = new TSLURI(mElement.OwnerDocument, uriStr);
            distributionPoints.Add(uri);
            mElement.AppendChild(uri.Element);
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_DISTRIBUTIONPOINTS; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TSLURI> DistributionPoints
        {
            get { return distributionPoints; }
        }

        public TSLURI DistributionPointAt(int pos)
        {
            if(pos>=distributionPoints.Count)
            {
                return null;
            }
            else
            {
                return distributionPoints[pos];
            }
        }

        public string DistributionPointStrAt(int pos)
        {
            if (pos >= distributionPoints.Count)
            {
                return null;
            }
            else
            {
                return distributionPoints[pos].TslURI;
            }
        }
    }
}
