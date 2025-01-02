using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation
{
    public class TSLPostalAddresses : BaseElement
    {

        private readonly IList<TSLPostalAddress> postalAddresses = new List<TSLPostalAddress>();

        public TSLPostalAddresses(XmlDocument document, IList<TSLPostalAddress> iPostalAddresses) : base(document)
        {
            postalAddresses = iPostalAddresses;
            addLineBreak();
            foreach (var tslPostalAddress in iPostalAddresses)
            {
                mElement.AppendChild(tslPostalAddress.Element);
                addLineBreak();
            }
        }

        public TSLPostalAddresses(XmlDocument document)
            : base(document)
        {
            addLineBreak();
        }

        public TSLPostalAddresses(XmlElement aElement) : base(aElement)
        {
            IList<XmlElement> childNodes = XmlUtil.selectChildElements(aElement);
            if(childNodes.Count <1)
            {
                throw new TSLException(Constants.TAG_POSTALADDRESS + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
            foreach (var xmlElement in childNodes)
            {
                postalAddresses.Add(new TSLPostalAddress(xmlElement));
            }
        }

        public void removeAllPostalAddresses()
        {
            postalAddresses.Clear();
            mElement.RemoveAll();
            addLineBreak();
        }

        public void addPostalAddress(TSLPostalAddress iPostalAddress)
        {
            postalAddresses.Add(iPostalAddress);
            mElement.AppendChild(iPostalAddress.Element);
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_POSTALADDRESSES; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TSLPostalAddress> PostalAddresses
        {
            get { return postalAddresses; }
        }
    }
}
