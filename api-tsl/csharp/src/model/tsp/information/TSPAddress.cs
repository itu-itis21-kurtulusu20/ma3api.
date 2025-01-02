using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.information
{
    public class TSPAddress : BaseElement
    {
        private readonly TSLPostalAddresses postalAddresses;
        private readonly TSLElectronicAddress electronicAddress;

        public TSPAddress(XmlDocument document, TSLPostalAddresses iPostalAddresses, TSLElectronicAddress iElectronicAddress) : base(document)
        {
            postalAddresses = iPostalAddresses;
            electronicAddress = iElectronicAddress;

            addLineBreak();
            mElement.AppendChild(postalAddresses.Element);
            addLineBreak();
            mElement.AppendChild(electronicAddress.Element);
            addLineBreak();
        }

        public TSPAddress(XmlElement aElement)
            : base(aElement)
        {
            XmlNamespaceManager nsManager = TSLUtil.getInstance().getNamespaceManager(aElement.OwnerDocument);

            XmlNodeList nodeList = null;
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_POSTALADDRESSES, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_POSTALADDRESSES + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_POSTALADDRESSES + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            postalAddresses = new TSLPostalAddresses((XmlElement)nodeList[0]);
            ///
            nodeList = null;
            ///
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_ELECTRONICADDRESS, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_ELECTRONICADDRESS + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_ELECTRONICADDRESS + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            electronicAddress = new TSLElectronicAddress((XmlElement)nodeList[0]);
            ///
            //postalAddresses = new TSLPostalAddresses(XmlUtil.getNextElement(aElement.FirstChild));
            //electronicAddress = new TSLElectronicAddress(XmlUtil.getNextElement(postalAddresses.Element.NextSibling));
        }

        public TSLPostalAddresses PostalAddresses
        {
            get { return postalAddresses; }
        }

        public TSLElectronicAddress ElectronicAddress
        {
            get { return electronicAddress; }
        }

        public override string LocalName
        {
            get { return Constants.TAG_TSPADDRESS; }
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
