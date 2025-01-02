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
    public class TSLElectronicAddress : BaseElement
    {
        private readonly IList<TSLURI> addresses = new List<TSLURI>();

        public TSLElectronicAddress(XmlDocument document, IList<TSLURI> iAddresses) : base(document)
        {
            addresses = iAddresses;
            addLineBreak();
            foreach (var address in addresses)
            {
                mElement.AppendChild(address.Element);
                addLineBreak();
            }
        }

        public TSLElectronicAddress(XmlDocument document) : base(document)
        {
            addLineBreak();
        }

        public TSLElectronicAddress(XmlElement aElement) : base(aElement)
        {
            if(mElement.HasChildNodes)
            {
                IList<XmlElement> childNodes = XmlUtil.selectChildElements(aElement);

                foreach (var xmlElement in childNodes)
                {
                    addresses.Add(new TSLURI(xmlElement));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_ELECTRONICADDRESS + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }
        
        public void addAddress(TSLURI iAddress)
        {
            addresses.Add(iAddress);
            mElement.AppendChild(iAddress.Element);
            addLineBreak();
        }

        public void removeAllAddresses()
        {
            mElement.RemoveAll();
            addresses.Clear();
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_ELECTRONICADDRESS; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TSLURI> Addresses
        {
            get { return addresses; }
        }

        public TSLURI AddressAt(int pos)
        {
            if(pos>=addresses.Count)
            {
                return null;
            }
            else
            {
                return addresses[pos];
            }
        }

        public string AddressStrAt(int pos)
        {
            if (pos >= addresses.Count)
            {
                return null;
            }
            else
            {
                return addresses[pos].TslURI;
            }
        }
    }
}
