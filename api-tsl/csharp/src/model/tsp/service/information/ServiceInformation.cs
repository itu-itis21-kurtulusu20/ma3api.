using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service.information
{
    public class ServiceInformation : BaseElement
    {
        private XmlElement serviceTypeIdentifier;
        private ServiceName serviceName;
        private TSLServiceDigitalIdentity serviceDigitalIdentity;
        private XmlElement serviceStatus;
        private StatusStartingTime statusStartingTime;
        private SchemeServiceDefinitionURI schemeServiceDefinitionURI;
        private ServiceSupplyPoints serviceSupplyPoints;
        private TSPServiceDefinitionURI tspServiceDefinitionURI;

        public ServiceInformation(XmlDocument document) : base(document)
        {
            addLineBreak();
        }

        public ServiceInformation(XmlElement aElement) : base(aElement)
        {
            XmlNamespaceManager nsManager = TSLUtil.getInstance().getNamespaceManager(aElement.OwnerDocument);

            XmlNodeList nodeList = null;
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_SERVICETYPEIDENTIFIER, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_SERVICETYPEIDENTIFIER + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_SERVICETYPEIDENTIFIER + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            serviceTypeIdentifier = (XmlElement)nodeList[0];
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_SERVICENAME, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_SERVICENAME + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_SERVICENAME + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            serviceName = new ServiceName((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_SERVICEDIGITALIDENTITY, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_SERVICEDIGITALIDENTITY + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_SERVICEDIGITALIDENTITY + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            serviceDigitalIdentity = new TSLServiceDigitalIdentity((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_SERVICESTATUS, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_SERVICESTATUS + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_SERVICESTATUS + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            serviceStatus = (XmlElement)nodeList[0];
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_STATUSSTARTINGTIME, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_STATUSSTARTINGTIME + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_STATUSSTARTINGTIME + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            statusStartingTime = new StatusStartingTime((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_SCHEMESERVICEDEFINITIONURI, nsManager);

            if (nodeList != null && nodeList.Count != 0)
            {
                if (nodeList.Count > 1)
                {
                    throw new TSLException(Constants.TAG_SCHEMESERVICEDEFINITIONURI + TSL_DIL.NODE_MORE_THAN_ONE);
                }
                schemeServiceDefinitionURI = new SchemeServiceDefinitionURI((XmlElement)nodeList[0]);
            }
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_SERVICESUPPLYPOINTS, nsManager);

            if (nodeList != null && nodeList.Count != 0)
            {
                if (nodeList.Count > 1)
                {
                    throw new TSLException(Constants.TAG_SERVICESUPPLYPOINTS + TSL_DIL.NODE_MORE_THAN_ONE);
                }
                serviceSupplyPoints = new ServiceSupplyPoints((XmlElement)nodeList[0]);
            }
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_TSPSERVICEDEFINITONURI, nsManager);

            if(serviceTypeIdentifier.InnerText.Contains("NationalRootCA-QC"))
            {
                if (nodeList == null || nodeList.Count == 0)
                {
                    throw new TSLException(Constants.TAG_TSPSERVICEDEFINITONURI + TSL_DIL.NODE_CANNOT_BE_FOUND);
                }
                if (nodeList.Count > 1)
                {
                    throw new TSLException(Constants.TAG_TSPSERVICEDEFINITONURI + TSL_DIL.NODE_MORE_THAN_ONE);
                }

                tspServiceDefinitionURI = new TSPServiceDefinitionURI((XmlElement)nodeList[0]);
            }
            else
            {
                if (nodeList != null && nodeList.Count != 0)
                {
                    if (nodeList.Count > 1)
                    {
                        throw new TSLException(Constants.TAG_SERVICESUPPLYPOINTS + TSL_DIL.NODE_MORE_THAN_ONE);
                    }
                    tspServiceDefinitionURI = new TSPServiceDefinitionURI((XmlElement)nodeList[0]);
                }
            }
            //serviceTypeIdentifier = XmlUtil.getNextElement(aElement.FirstChild);
            //serviceName = new ServiceName(XmlUtil.getNextElement(serviceTypeIdentifier.NextSibling));
            //serviceDigitalIdentity = new TSLServiceDigitalIdentity(XmlUtil.getNextElement(serviceName.Element.NextSibling));
            //serviceStatus = XmlUtil.getNextElement(serviceDigitalIdentity.Element.NextSibling);
            //statusStartingTime = new StatusStartingTime(XmlUtil.getNextElement(serviceStatus.NextSibling));
            //schemeServiceDefinitionURI = new SchemeServiceDefinitionURI(XmlUtil.getNextElement(statusStartingTime.Element.NextSibling));
            //serviceSupplyPoints = new ServiceSupplyPoints(XmlUtil.getNextElement(schemeServiceDefinitionURI.Element.NextSibling));
            //tspServiceDefinitionURI = new TSPServiceDefinitionURI(XmlUtil.getNextElement(serviceSupplyPoints.Element.NextSibling));
        }

        public XmlElement ServiceTypeIdentifier
        {
            get
            {
                return serviceTypeIdentifier;
            }
            set
            {
                if(serviceTypeIdentifier!=null)
                {
                    mElement.ReplaceChild(value, serviceTypeIdentifier);
                    serviceTypeIdentifier = value;
                }
                else
                {
                    serviceTypeIdentifier = value;
                    mElement.AppendChild(serviceTypeIdentifier);
                    addLineBreak();
                }
            }
        }

        public ServiceName ServiceName
        {
            get { return serviceName; }
            set
            {
                if(serviceName!=null)
                {
                    mElement.ReplaceChild(value.Element, serviceName.Element);
                    serviceName = value;
                }
                else
                {
                    serviceName = value;
                    mElement.AppendChild(serviceName.Element);
                    addLineBreak();
                }
            }
        }

        public TSLServiceDigitalIdentity ServiceDigitalIdentity
        {
            get
            {
                return serviceDigitalIdentity;
            }
            set
            {
                if(serviceDigitalIdentity!=null)
                {
                    mElement.ReplaceChild(value.Element, serviceDigitalIdentity.Element);
                    serviceDigitalIdentity = value;
                }
                else
                {
                    serviceDigitalIdentity = value;
                    mElement.AppendChild(serviceDigitalIdentity.Element);
                    addLineBreak();
                }
            }
        }

        public XmlElement ServiceStatus
        {
            get
            {
                return serviceStatus;
            }
            set
            {
                if(serviceStatus!=null)
                {
                    mElement.ReplaceChild(value, serviceStatus);
                    serviceStatus = value;
                }
                else
                {
                    serviceStatus = value;
                    mElement.AppendChild(serviceStatus);
                    addLineBreak();
                }
            }
        }

        public StatusStartingTime StatusStartingTime
        {
            get
            {
                return statusStartingTime;
            }
            set 
            {
                if(statusStartingTime!=null)
                {
                    mElement.ReplaceChild(value.Element, statusStartingTime.Element);
                    statusStartingTime = value;
                }
                else
                {
                    statusStartingTime = value;
                    mElement.AppendChild(statusStartingTime.Element);
                    addLineBreak();    
                }
                
            }
        }

        public SchemeServiceDefinitionURI SchemeServiceDefinitionUri
        {
            get 
            { 
                return schemeServiceDefinitionURI;
            }
            set
            {
                if(schemeServiceDefinitionURI!=null)
                {
                    mElement.ReplaceChild(value.Element, schemeServiceDefinitionURI.Element);
                    schemeServiceDefinitionURI = value;
                }
                else
                {
                    schemeServiceDefinitionURI = value;
                    mElement.AppendChild(schemeServiceDefinitionURI.Element);
                    addLineBreak();   
                }
            }
        }

        public ServiceSupplyPoints ServiceSupplyPoints
        {
            get { return serviceSupplyPoints; }
            set
            {
                if(serviceSupplyPoints!=null)
                {
                    mElement.ReplaceChild(value.Element, serviceSupplyPoints.Element);
                    serviceSupplyPoints = value;
                }
                else
                {
                    serviceSupplyPoints = value;
                    mElement.AppendChild(serviceSupplyPoints.Element);
                    addLineBreak();
                }
            }
        }

        public TSPServiceDefinitionURI TspServiceDefinitionUri
        {
            get 
            { 
                return tspServiceDefinitionURI; 
            }
            set
            {
                if(tspServiceDefinitionURI!=null)
                {
                    mElement.ReplaceChild(value.Element, tspServiceDefinitionURI.Element);
                    tspServiceDefinitionURI = value;
                }
                else
                {
                    tspServiceDefinitionURI = value;
                    mElement.AppendChild(tspServiceDefinitionURI.Element);
                    addLineBreak();   
                }
            }
        }

        public override string LocalName
        {
            get { return Constants.TAG_SERVICEINFORMATION; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }
    }
}
