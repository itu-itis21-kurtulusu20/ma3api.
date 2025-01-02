using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation
{
    public class OtherTSLPointer : BaseElement
    {
        private readonly TSLServiceDigitalIdentities serviceDigitalIdentities;
        private readonly TSLLocation mTSLLocation;
        private readonly TSLAdditionalInformation additionalInformation;

        public OtherTSLPointer(XmlDocument document, TSLServiceDigitalIdentities iServiceDigitalIdentities, TSLLocation iTslLocation, TSLAdditionalInformation iAdditionalInformation) : base(document)
        {
            serviceDigitalIdentities = iServiceDigitalIdentities;
            mTSLLocation = iTslLocation;
            additionalInformation = iAdditionalInformation;

            addLineBreak();
            mElement.AppendChild(serviceDigitalIdentities.Element);
            addLineBreak();
            mElement.AppendChild(mTSLLocation.Element);
            addLineBreak();
            mElement.AppendChild(additionalInformation.Element);
            addLineBreak();
        }

        public OtherTSLPointer(XmlElement aElement) : base(aElement)
        {
            XmlNamespaceManager nsManager = TSLUtil.getInstance().getNamespaceManager(aElement.OwnerDocument);

            XmlNodeList nodeList = null;
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_SERVICEDIGITALIDENTITIES, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_SERVICEDIGITALIDENTITIES + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_SERVICEDIGITALIDENTITIES + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            serviceDigitalIdentities = new TSLServiceDigitalIdentities((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_TSLLOCATION, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_TSLLOCATION + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_TSLLOCATION + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            mTSLLocation = new TSLLocation((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_TSLADDITIONALINFORMATION, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_TSLADDITIONALINFORMATION + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_TSLADDITIONALINFORMATION + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            additionalInformation = new TSLAdditionalInformation((XmlElement)nodeList[0]);
            ///
            //serviceDigitalIdentities = new TSLServiceDigitalIdentities(XmlUtil.getNextElement(aElement.FirstChild));
            //mTSLLocation = new TSLLocation(XmlUtil.getNextElement(serviceDigitalIdentities.Element.NextSibling));
            //additionalInformation = new TSLAdditionalInformation(XmlUtil.getNextElement(mTSLLocation.Element.NextSibling));
        }

        public override string LocalName
        {
            get { return Constants.TAG_OTHERTSLPOINTER; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public TSLServiceDigitalIdentities ServiceDigitalIdentities
        {
            get { return serviceDigitalIdentities; }
        }

        public TSLLocation TslLocation
        {
            get { return mTSLLocation; }
        }

        public TSLAdditionalInformation AdditionalInformation
        {
            get { return additionalInformation; }
        }
    }
}
