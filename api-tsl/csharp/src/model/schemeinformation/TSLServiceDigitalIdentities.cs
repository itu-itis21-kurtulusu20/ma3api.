using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.tsl.model.core;
using tr.gov.tubitak.uekae.esya.api.tsl.util;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation
{
    public class TSLServiceDigitalIdentities : BaseElement
    {
        readonly IList<TSLServiceDigitalIdentity> serviceDigitalIdentityList = new List<TSLServiceDigitalIdentity>(); 

        public TSLServiceDigitalIdentities(XmlDocument document, IList<TSLServiceDigitalIdentity> iServiceDigitalIdentityList) : base(document)
        {
            serviceDigitalIdentityList = iServiceDigitalIdentityList;

            addLineBreak();
            foreach (var tslServiceDigitalIdentity in serviceDigitalIdentityList)
            {
                mElement.AppendChild(tslServiceDigitalIdentity.Element);
                addLineBreak();
            }
        }

        public TSLServiceDigitalIdentities(XmlDocument document) : base(document)
        {
            addLineBreak();
        }

        public TSLServiceDigitalIdentities(XmlElement aElement) : base(aElement)
        {
            if (mElement.HasChildNodes)
            {
                IList<XmlElement> childNodes = XmlUtil.selectChildElements(aElement);

                foreach (var xmlElement in childNodes)
                {
                    serviceDigitalIdentityList.Add(new TSLServiceDigitalIdentity(xmlElement));
                }
            }
            else
            {
                throw new TSLException(Constants.TAG_SERVICEDIGITALIDENTITIES + TSL_DIL.NODE_CANNOT_BE_FOUND);
            }
        }

        public void addServiceDigitalIdentity(TSLServiceDigitalIdentity iServiceDigitalIdentity)
        {
            serviceDigitalIdentityList.Add(iServiceDigitalIdentity);
            mElement.AppendChild(iServiceDigitalIdentity.Element);
            addLineBreak();
        }

        public void addServiceDigitalIdentityCert(ECertificate iCertificate)
        {
            TSLServiceDigitalIdentity serviceDigitalIdentity = new TSLServiceDigitalIdentity(mElement.OwnerDocument, iCertificate);
            serviceDigitalIdentityList.Add(serviceDigitalIdentity);
            mElement.AppendChild(serviceDigitalIdentity.Element);
            addLineBreak();
        }

        public override string LocalName
        {
            get { return Constants.TAG_SERVICEDIGITALIDENTITIES; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public IList<TSLServiceDigitalIdentity> ServiceDigitalIdentityList
        {
            get { return serviceDigitalIdentityList; }
        }

        public TSLServiceDigitalIdentity ServiceDigitalIdentityAt(int pos)
        {
            if(pos>=serviceDigitalIdentityList.Count)
            {
                return null;
            }
            else
            {
                return serviceDigitalIdentityList[pos];
            }
        }

        public ECertificate ServiceDigitalIdentityCertificateAt(int pos)
        {
            if (pos >= serviceDigitalIdentityList.Count)
            {
                return null;
            }
            else
            {
                return serviceDigitalIdentityList[pos].X509Certificate;
            }
        }
    }
}
