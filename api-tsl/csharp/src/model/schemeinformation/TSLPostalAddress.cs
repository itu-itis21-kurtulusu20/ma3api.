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
    public class TSLPostalAddress : BaseElement
    {
        private readonly string language;

        private readonly string streetAddress, locality, postalCode, countryName;
        
        private readonly XmlElement streetAddressElement, localityElement, postalCodeElement, countryNameElement;
        
        public TSLPostalAddress(XmlDocument document, string iStreetAddress, string iLocality, string iPostalCode, string iCountryName, string iLanguage) : base(document)
        {
            language = iLanguage;

            streetAddress = iStreetAddress;
            locality = iLocality;
            postalCode = iPostalCode;
            countryName = iCountryName;

            addLineBreak();
            streetAddressElement = insertTextElement(Constants.NS_TSL, Constants.TAG_STREETADDRESS,streetAddress);
            localityElement = insertTextElement(Constants.NS_TSL, Constants.TAG_LOCALITY, locality);
            postalCodeElement = insertTextElement(Constants.NS_TSL, Constants.TAG_POSTALCODE, postalCode);
            countryNameElement = insertTextElement(Constants.NS_TSL, Constants.TAG_COUNTRYNAME, countryName);

            if (mElement.Attributes.Count > 0)
            {
                throw new TSLException("should not have a default attribute");
            }
            mElement.SetAttribute(Constants.XML_PREFIX + Constants.TSL_LANG_ATTR, language);
        }

        public TSLPostalAddress(XmlElement aElement) : base(aElement)
        {
            XmlNamespaceManager nsManager = TSLUtil.getInstance().getNamespaceManager(aElement.OwnerDocument);

            XmlNodeList nodeList = null;
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_STREETADDRESS, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_STREETADDRESS + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_STREETADDRESS + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            streetAddressElement = (XmlElement)nodeList[0];
            ///
            nodeList = null;
            /// 
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_LOCALITY, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_LOCALITY + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_LOCALITY + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            localityElement = (XmlElement)nodeList[0];
            ///
            nodeList = null;
            /// 
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_POSTALCODE, nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_POSTALCODE + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_POSTALCODE + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            postalCodeElement = (XmlElement)nodeList[0];
            ///
            nodeList = null;
            /// 
            nodeList = aElement.SelectNodes(Constants.TSL_PREFIX + Constants.TAG_COUNTRYNAME,nsManager);
            if (nodeList == null || nodeList.Count == 0)
            {
                throw new TSLException(Constants.TAG_COUNTRYNAME + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            if (nodeList.Count > 1)
            {
                throw new TSLException(Constants.TAG_COUNTRYNAME + TSL_DIL.NODE_MORE_THAN_ONE);
            }

            countryNameElement = (XmlElement)nodeList[0];
            ///
            
            //streetAddressElement = XmlUtil.getNextElement(aElement.FirstChild);
            //localityElement = XmlUtil.getNextElement(streetAddressElement.NextSibling);
            //postalCodeElement = XmlUtil.getNextElement(localityElement.NextSibling);
            //countryNameElement = XmlUtil.getNextElement(postalCodeElement.NextSibling);

            streetAddress = XmlUtil.getText(streetAddressElement);
            locality = XmlUtil.getText(localityElement);
            postalCode = XmlUtil.getText(postalCodeElement);
            countryName = XmlUtil.getText(countryNameElement);

            if (aElement.HasAttribute(Constants.XML_PREFIX + Constants.TSL_LANG_ATTR))
            {
                language = aElement.GetAttribute(Constants.XML_PREFIX + Constants.TSL_LANG_ATTR);
            }
            else
            {
                throw new TSLException("Language Attribute could not be found!");
            }
        }

        public override string LocalName
        {
            get { return Constants.TAG_POSTALADDRESS; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public string Language
        {
            get { return language; }
        }

        public string StreetAddress
        {
            get { return streetAddress; }
        }

        public string Locality
        {
            get { return locality; }
        }

        public string PostalCode
        {
            get { return postalCode; }
        }

        public string Country
        {
            get { return countryName; }
        }
    }
}
