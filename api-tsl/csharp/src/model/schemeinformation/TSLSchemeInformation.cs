using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using Org.BouncyCastle.Math;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation
{
    public class TSLSchemeInformation : BaseElement
    {

        private BigInteger versionId, sequenceNumber, historicalInformationPeriod;
        private string tslType, statusDeterminationApproach;

        private XmlElement versionIdElement, sequenceNumberElement; //1-2

        private TSLType tslTypeElement; //3
        
        private TSLSchemeOperatorName schemeOperatorName;   //4
        private TSLSchemeOperatorAddress schemeOperatorAddress; //5
        private TSLSchemeName schemeName;   //6
        private TSLSchemeInformationURI schemeInformationURI;   //7
        
        private XmlElement statusDeterminationApproachElement;  //8

        private TSLSchemeTypeCommunityRules schemeTypeCommunityRules;   //9

        private TSLSchemeTerritory schemeTerritory;  //10

        private TSLPolicyOrLegalNotice policyOrLegalNotice; //11

        private XmlElement historicalInformationPeriodElement;  //12

        private PointersToOtherTSL pointersToOtherTSL;  //13

        private TSLListIssueDateTime issueDateTime; //14

        private TSLNextUpdate nextUpdate;   //15

        private TSLDistributionPoints distributionPoints;   //16

        public TSLSchemeInformation(XmlDocument document) : base(document)
        {
            addLineBreak();
        }

        public TSLSchemeInformation(XmlElement aElement)
            : base(aElement)
        {
            XmlNamespaceManager nsManager = TSLUtil.getInstance().getNamespaceManager(aElement.OwnerDocument);

            XmlNodeList nodeList = null;
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_TSLVERSIONIDENTIFIER, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_TSLVERSIONIDENTIFIER + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_TSLVERSIONIDENTIFIER + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            versionIdElement = (XmlElement)nodeList[0];
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_TSLSEQUENCENUMBER, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_TSLSEQUENCENUMBER + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_TSLSEQUENCENUMBER + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            sequenceNumberElement = (XmlElement)nodeList[0];
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_TSLTYPE, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_TSLTYPE + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_TSLTYPE + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            tslTypeElement = new TSLType((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_SCHEMEOPERATORNAME, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_SCHEMEOPERATORNAME + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_SCHEMEOPERATORNAME + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            schemeOperatorName = new TSLSchemeOperatorName((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_SCHEMEOPERATORADDRESS, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_SCHEMEOPERATORADDRESS + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_SCHEMEOPERATORADDRESS + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            schemeOperatorAddress = new TSLSchemeOperatorAddress((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_SCHEMENAME, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_SCHEMENAME + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_SCHEMENAME + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            schemeName = new TSLSchemeName((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            /// 
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_SCHEMEINFORMATIONURI, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_SCHEMEINFORMATIONURI + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_SCHEMEINFORMATIONURI + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            schemeInformationURI = new TSLSchemeInformationURI((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_TSLSTATUSDETERMINATIONAPPROACH, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_TSLSTATUSDETERMINATIONAPPROACH + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_TSLSTATUSDETERMINATIONAPPROACH + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            statusDeterminationApproachElement = (XmlElement)nodeList[0];
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_SCHEMETYPECOMMUNITYRULES, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_SCHEMETYPECOMMUNITYRULES + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_SCHEMETYPECOMMUNITYRULES + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            schemeTypeCommunityRules = new TSLSchemeTypeCommunityRules((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_SCHEMETERRITORY, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_SCHEMETERRITORY + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_SCHEMETERRITORY + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            schemeTerritory = new TSLSchemeTerritory((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_POLICYORLEGALNOTICE, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_POLICYORLEGALNOTICE + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_POLICYORLEGALNOTICE + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            policyOrLegalNotice = new TSLPolicyOrLegalNotice((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_HISTORICALINFORMATIONPERIOD, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_HISTORICALINFORMATIONPERIOD + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_HISTORICALINFORMATIONPERIOD + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            historicalInformationPeriodElement = (XmlElement) nodeList[0];
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_POINTERSTOOTHERTSL, nsManager);

            if (nodeList != null && nodeList.Count != 0)
            {
                if (nodeList.Count > 1)
                {
                    throw new TSLException(Constants.TAG_POINTERSTOOTHERTSL + TSL_DIL.NODE_MORE_THAN_ONE);
                }
                pointersToOtherTSL = new PointersToOtherTSL((XmlElement) nodeList[0]);
            }
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_LISTISSUEDATETIME, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_LISTISSUEDATETIME + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_LISTISSUEDATETIME + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            issueDateTime = new TSLListIssueDateTime((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_NEXTUPDATE, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_NEXTUPDATE + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_NEXTUPDATE + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            nextUpdate = new TSLNextUpdate((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_DISTRIBUTIONPOINTS, nsManager);

            if (nodeList != null && nodeList.Count != 0)
            {
                if (nodeList.Count > 1)
                {
                    throw new TSLException(Constants.TAG_DISTRIBUTIONPOINTS + TSL_DIL.NODE_MORE_THAN_ONE);
                }
                distributionPoints = new TSLDistributionPoints((XmlElement)nodeList[0]);
            }
            ///
            //versionIdElement = XmlUtil.getNextElement(aElement.FirstChild);
            //sequenceNumberElement = XmlUtil.getNextElement(versionIdElement.NextSibling);
            //tslTypeElement = new TSLType(XmlUtil.getNextElement(sequenceNumberElement.NextSibling));
            //schemeOperatorName = new TSLSchemeOperatorName(XmlUtil.getNextElement(tslTypeElement.Element.NextSibling));
            //schemeOperatorAddress = new TSLSchemeOperatorAddress(XmlUtil.getNextElement(schemeOperatorName.Element.NextSibling));
            //schemeName = new TSLSchemeName(XmlUtil.getNextElement(schemeOperatorAddress.Element.NextSibling));
            //schemeInformationURI = new TSLSchemeInformationURI(XmlUtil.getNextElement(schemeName.Element.NextSibling));
            //statusDeterminationApproachElement = XmlUtil.getNextElement(schemeInformationURI.Element.NextSibling);
            //schemeTypeCommunityRules = new TSLSchemeTypeCommunityRules(XmlUtil.getNextElement(statusDeterminationApproachElement.NextSibling));
            //schemeTerritory = new TSLSchemeTerritory(XmlUtil.getNextElement(schemeTypeCommunityRules.Element.NextSibling));
            //policyOrLegalNotice = new TSLPolicyOrLegalNotice(XmlUtil.getNextElement(schemeTerritory.Element.NextSibling));
            //historicalInformationPeriodElement = XmlUtil.getNextElement(policyOrLegalNotice.Element.NextSibling);
            //pointersToOtherTSL = new PointersToOtherTSL(XmlUtil.getNextElement(historicalInformationPeriodElement.NextSibling));
            //issueDateTime = new TSLListIssueDateTime(XmlUtil.getNextElement(pointersToOtherTSL.Element.NextSibling));
            //nextUpdate = new TSLNextUpdate(XmlUtil.getNextElement(issueDateTime.Element.NextSibling));
            //distributionPoints = new TSLDistributionPoints(XmlUtil.getNextElement(nextUpdate.Element.NextSibling));

            versionId = new BigInteger(1, Encoding.UTF8.GetBytes(XmlUtil.getText(versionIdElement)));
            sequenceNumber = new BigInteger(1, Encoding.UTF8.GetBytes(XmlUtil.getText(sequenceNumberElement)));
            tslType = XmlUtil.getText(tslTypeElement.Element);
            statusDeterminationApproach = XmlUtil.getText(statusDeterminationApproachElement);
            historicalInformationPeriod = new BigInteger(1, Encoding.UTF8.GetBytes(XmlUtil.getText(historicalInformationPeriodElement)));
        }

        public XmlElement VersionID //1
        {
            get { return versionIdElement; }
            set 
            { 
                versionIdElement = value;
                versionId = new BigInteger(1, Encoding.UTF8.GetBytes(versionIdElement.InnerText));
                mElement.AppendChild(versionIdElement);
                addLineBreak();
            }
        }

        public XmlElement SequenceNumber //2
        {
            get { return sequenceNumberElement; }
            set
            {
                sequenceNumberElement = value;
                sequenceNumber = new BigInteger(1, Encoding.UTF8.GetBytes(sequenceNumberElement.InnerText));
                mElement.AppendChild(sequenceNumberElement);
                addLineBreak();
            }
        }

        public TSLType TSLType  //3
        {
            get { return tslTypeElement; }
            set 
            { 
                tslTypeElement = value;
                mElement.AppendChild(tslTypeElement.Element);
                tslType = tslTypeElement.TslType;
                addLineBreak();
            }
        }

        public TSLSchemeOperatorName SchemeOperatorName //4
        {
            get { return schemeOperatorName; }
            set 
            { 
                schemeOperatorName = value;
                mElement.AppendChild(schemeOperatorName.Element);
                addLineBreak();
            }
        }

        public TSLSchemeOperatorAddress SchemeOperatorAddress   //5
        {
            get { return schemeOperatorAddress; }
            set 
            { 
                schemeOperatorAddress = value;
                mElement.AppendChild(schemeOperatorAddress.Element);
                addLineBreak();
            }
        }

        public TSLSchemeName SchemeName //6
        {
            get { return schemeName; }
            set
            {
                schemeName = value;
                mElement.AppendChild(schemeName.Element);
                addLineBreak();
            }
        }

        public TSLSchemeInformationURI SchemeInformationUri //7
        {
            get { return schemeInformationURI; }
            set
            {
                schemeInformationURI = value;
                mElement.AppendChild(schemeInformationURI.Element);
                addLineBreak();
            }
        }

        public XmlElement StatusDeterminationApproach   //8
        {
            get { return statusDeterminationApproachElement; }
            set
            {
                statusDeterminationApproachElement = value;
                mElement.AppendChild(statusDeterminationApproachElement);
                statusDeterminationApproach = statusDeterminationApproachElement.InnerText;
                addLineBreak();
            }
        }

        public TSLSchemeTypeCommunityRules SchemeTypeCommunityRules //9
        {
            get { return schemeTypeCommunityRules; }
            set
            {
                schemeTypeCommunityRules = value;
                mElement.AppendChild(schemeTypeCommunityRules.Element);
                addLineBreak();
            }
        }

        public TSLSchemeTerritory SchemeTerritory   //10
        {
            get { return schemeTerritory; }
            set
            {
                schemeTerritory = value;
                mElement.AppendChild(schemeTerritory.Element);
                addLineBreak();
            }
        }

        public TSLPolicyOrLegalNotice PolicyOrLegalNotice   //11
        {
            get { return policyOrLegalNotice; }
            set
            {
                policyOrLegalNotice = value;
                mElement.AppendChild(policyOrLegalNotice.Element);
                addLineBreak();
            }
        }

        public XmlElement HistoricalInformationPeriod   //12
        {
            get { return historicalInformationPeriodElement; }
            set
            {
                historicalInformationPeriodElement = value;
                historicalInformationPeriod = new BigInteger(1, Encoding.UTF8.GetBytes(historicalInformationPeriodElement.InnerText));
                mElement.AppendChild(historicalInformationPeriodElement);
                addLineBreak();
            }
        }

        public PointersToOtherTSL PointersToOtherTSL    //13
        {
            get { return pointersToOtherTSL; }
            set
            {
                pointersToOtherTSL = value;
                mElement.AppendChild(pointersToOtherTSL.Element);
                addLineBreak();
            }
        }

        public TSLListIssueDateTime ListIssueDateTime   //14
        {
            get { return issueDateTime; }
            set
            {
                issueDateTime = value;
                mElement.AppendChild(issueDateTime.Element);
                addLineBreak();
            }
        }

        public TSLNextUpdate NextUpdate //15
        {
            get { return nextUpdate; }
            set
            {
                nextUpdate = value;
                mElement.AppendChild(nextUpdate.Element);
                addLineBreak();
            }
        }

        public TSLDistributionPoints DistributionPoints //16
        {
            get { return distributionPoints; }
            set
            {
                distributionPoints = value;
                mElement.AppendChild(distributionPoints.Element);
                addLineBreak();
            }
        }

        public override string LocalName
        {
            get { return Constants.TAG_SCHEMEINFORMATION; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public BigInteger VersionIDBigInt
        {
            get { return versionId; }
        }

        public BigInteger SequenceNumberBigInt
        {
            get { return sequenceNumber; }
        }

        public BigInteger HistoricalInformationPeriodBigInt
        {
            get { return historicalInformationPeriod; }
        }

        public string TSLTypeStr
        {
            get { return tslType; }
        }

        public string StatusDeterminationApproachStr
        {
            get { return statusDeterminationApproach; }
        }
    }
}
