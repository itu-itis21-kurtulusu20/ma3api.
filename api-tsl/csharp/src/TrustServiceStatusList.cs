using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.model.tsp;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl
{
    public class TrustServiceStatusList : BaseElement
    {
        private bool mIsSigned = false;

        private TSLSchemeInformation schemeInformation;
        private TrustServiceProviderList tspList;
        private XmlElement signature;

        public TrustServiceStatusList(XmlDocument document, TSLSchemeInformation iSchemeInformation, TrustServiceProviderList iTSPList) : base(document)
        {
            generateAndSetId("ID");
            schemeInformation = iSchemeInformation;
            tspList = iTSPList;
            addLineBreak();
            mElement.AppendChild(schemeInformation.Element);
            addLineBreak();
            mElement.AppendChild(tspList.Element);
            addLineBreak();
            mElement.SetAttribute(Constants.ATTR_TSL, Constants.NS_TSL);
            mElement.SetAttribute(Constants.ATTR_XMLDSIG, Constants.NS_XMLDSIG);
            mElement.SetAttribute(Constants.ATTR_ECC, Constants.NS_ECC);
            mElement.SetAttribute(Constants.ATTR_TSLX, Constants.NS_TSLX);
            mElement.SetAttribute(Constants.ATTR_XADES, Constants.NS_XADES);
            mElement.SetAttribute(Constants.ATTR_ID, Id);
            mElement.SetAttribute(Constants.ATTR_TSLTAG, Constants.TSLTAG);
        }

        public TrustServiceStatusList(XmlDocument document, TSLSchemeInformation iSchemeInformation)
            : base(document)
        {
            generateAndSetId("ID");
            schemeInformation = iSchemeInformation;
            addLineBreak();
            mElement.AppendChild(schemeInformation.Element);
            addLineBreak();
            mElement.SetAttribute(Constants.ATTR_TSL, Constants.NS_TSL);
            mElement.SetAttribute(Constants.ATTR_XMLDSIG, Constants.NS_XMLDSIG);
            mElement.SetAttribute(Constants.ATTR_ECC, Constants.NS_ECC);
            mElement.SetAttribute(Constants.ATTR_TSLX, Constants.NS_TSLX);
            mElement.SetAttribute(Constants.ATTR_XADES, Constants.NS_XADES);
            mElement.SetAttribute(Constants.ATTR_ID, Id);
            mElement.SetAttribute(Constants.ATTR_TSLTAG, Constants.TSLTAG);
        }

        public TrustServiceStatusList(XmlDocument document)
            : base(document)
        {
            generateAndSetId("ID");
            addLineBreak();
            mElement.SetAttribute(Constants.ATTR_TSL, Constants.NS_TSL);
            mElement.SetAttribute(Constants.ATTR_XMLDSIG, Constants.NS_XMLDSIG);
            mElement.SetAttribute(Constants.ATTR_ECC, Constants.NS_ECC);
            mElement.SetAttribute(Constants.ATTR_TSLX, Constants.NS_TSLX);
            mElement.SetAttribute(Constants.ATTR_XADES, Constants.NS_XADES);
            mElement.SetAttribute(Constants.ATTR_ID, Id);
            mElement.SetAttribute(Constants.ATTR_TSLTAG, Constants.TSLTAG);
        }

        public TrustServiceStatusList(XmlElement aElement) : base(aElement)
        {
            XmlNamespaceManager nsManager = TSLUtil.getInstance().getNamespaceManager(aElement.OwnerDocument);

            XmlNodeList nodeList = null;
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_SCHEMEINFORMATION, nsManager);
            if(nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_SCHEMEINFORMATION + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if(nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_SCHEMEINFORMATION + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            schemeInformation = new TSLSchemeInformation((XmlElement) nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_TRUSTSERVICEPROVIDERLIST, nsManager);

            if (nodeList != null && nodeList.Count != 0)
            {
                if (nodeList.Count > 1)
                {
                    throw new TSLException(Constants.TAG_TRUSTSERVICEPROVIDERLIST + TSL_DIL.NODE_MORE_THAN_ONE);
                }
                tspList = new TrustServiceProviderList((XmlElement)nodeList[0]);
            }
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.DS_PREFIX + Constants.TAG_SIGNATURE, nsManager);

            if (nodeList != null && nodeList.Count != 0)
            {
                if (nodeList.Count > 1)
                {
                    throw new TSLException(Constants.TAG_SIGNATURE + TSL_DIL.NODE_MORE_THAN_ONE);
                }
                signature = (XmlElement)nodeList[0];
                mIsSigned = true;
            }
        }

        public void ClearTSL()
        {
            mElement.RemoveAll();

            schemeInformation = null;
            tspList = null;
            signature = null;

            mIsSigned = false;
            addLineBreak();
        }

        public string ID
        {
            get { return mElement.GetAttribute(Constants.ATTR_ID); }
        }

        public override string LocalName
        {
            get { return Constants.TAG_TRUSTSERVICESTATUSLIST; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public TSLSchemeInformation SchemeInformation
        {
            get { return schemeInformation; }
            set
            {
                if(schemeInformation != null)
                {
                    mElement.ReplaceChild(value.Element, schemeInformation.Element);
                    schemeInformation = value;
                }
                else
                {
                    schemeInformation = value;
                    mElement.AppendChild(schemeInformation.Element);
                    addLineBreak();
                }
            }
        }

        public TrustServiceProviderList TSPList
        {
            get { return tspList; }
            set
            {
                if(tspList != null)
                {
                    mElement.ReplaceChild(value.Element, tspList.Element);
                    tspList = value;
                }
                else
                {
                    tspList = value;
                    mElement.AppendChild(tspList.Element);
                    addLineBreak();
                }
            }
        }

        public XmlElement SignatureElement
        {
            get { return signature; }
            set 
            { 
                if(signature != null)
                {
                    mElement.ReplaceChild(value, signature);
                    signature = value;
                    mIsSigned = true;
                }
                else
                {
                    signature = value;
                    mElement.AppendChild(signature);
                    addLineBreak();
                    mIsSigned = true;
                }
            }
        }

        public bool isSigned
        {
            get { return mIsSigned; }
        }
    }
}
