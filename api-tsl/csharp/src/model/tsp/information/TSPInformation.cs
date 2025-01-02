using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.information
{
    public class TSPInformation : BaseElement
    {
        private TSPName tspName;
        private TSPTradeName tspTradeName;
        private TSPAddress tspAddress;
        private TSPInformationURI tspInformationUri;

        public TSPInformation(XmlDocument document, TSPName iTSPName, TSPTradeName iTSPTradeName, TSPAddress iTSPAddress, TSPInformationURI iTSPInformationURI) : base(document)
        {
            tspName = iTSPName;
            tspTradeName = iTSPTradeName;
            tspAddress = iTSPAddress;
            tspInformationUri = iTSPInformationURI;
            addLineBreak();
            mElement.AppendChild(tspName.Element);
            addLineBreak();
            mElement.AppendChild(tspTradeName.Element);
            addLineBreak();
            mElement.AppendChild(tspAddress.Element);
            addLineBreak();
            mElement.AppendChild(tspInformationUri.Element);
            addLineBreak();
        }

        public TSPInformation(XmlDocument document)
            : base(document)
        {
            addLineBreak();
        }

        public TSPInformation(XmlElement aElement) : base(aElement)
        {
            XmlNamespaceManager nsManager = TSLUtil.getInstance().getNamespaceManager(aElement.OwnerDocument);

            XmlNodeList nodeList = null;
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_TSPNAME, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_TSPNAME + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_TSPNAME + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            tspName = new TSPName((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_TSPTRADENAME, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_TSPTRADENAME + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_TSPTRADENAME + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            tspTradeName = new TSPTradeName((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_TSPADDRESS, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_TSPADDRESS + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_TSPADDRESS + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            tspAddress = new TSPAddress((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_TSPINFORMATIONURI, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_TSPINFORMATIONURI + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_TSPINFORMATIONURI + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            tspInformationUri = new TSPInformationURI((XmlElement)nodeList[0]);
            ///
            //tspName = new TSPName(XmlUtil.getNextElement(aElement.FirstChild));
            //tspTradeName = new TSPTradeName(XmlUtil.getNextElement(tspName.Element.NextSibling));
            //tspAddress = new TSPAddress(XmlUtil.getNextElement(tspTradeName.Element.NextSibling));
            //tspInformationUri = new TSPInformationURI(XmlUtil.getNextElement(tspAddress.Element.NextSibling));
        }

        public override string LocalName
        {
            get { return Constants.TAG_TSPINFORMATION; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public TSPName TspName
        {
            get { return tspName; }
            set
            {
                if (tspName != null)
                {
                    mElement.ReplaceChild(value.Element, tspName.Element);
                    tspName = value;
                }
                else
                {
                    tspName = value;
                    mElement.AppendChild(tspName.Element);
                    addLineBreak();
                }
            }
        }

        public TSPTradeName TspTradeName
        {
            get { return tspTradeName; }
            set
            {
                if(tspTradeName!=null)
                {
                    mElement.ReplaceChild(value.Element, tspTradeName.Element);
                    tspTradeName = value;
                }
                else
                {
                    tspTradeName = value;
                    mElement.AppendChild(tspTradeName.Element);
                    addLineBreak();
                }
            }
        }

        public TSPAddress TspAddress
        {
            get { return tspAddress; }
            set
            {
                if(tspAddress!=null)
                {
                    mElement.ReplaceChild(value.Element, tspAddress.Element);
                    tspAddress = value;
                }
                else
                {
                    tspAddress = value;
                    mElement.AppendChild(tspAddress.Element);
                    addLineBreak();
                }
            }
        }

        public TSPInformationURI TspInformationUri
        {
            get { return tspInformationUri; }
            set
            {
                if (tspInformationUri != null)
                {
                    mElement.ReplaceChild(value.Element, tspInformationUri.Element);
                    tspInformationUri = value;
                }
                else
                {
                    tspInformationUri = value;
                    mElement.AppendChild(tspInformationUri.Element);
                    addLineBreak();
                }
            }
        }
    }
}
