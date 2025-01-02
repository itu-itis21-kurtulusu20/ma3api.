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
    public class TSLServiceDigitalIdentity : BaseElement
    {
        private readonly string certificateBase64Str;
        private readonly ECertificate certificate;
        
        private readonly XmlElement digitalIdElement;
        private readonly XmlElement x509CertificateElement;

        public TSLServiceDigitalIdentity(XmlDocument document, ECertificate iCertificate) : base(document)
        {
            certificate = iCertificate;
            certificateBase64Str = Convert.ToBase64String(certificate.getBytes());

            x509CertificateElement = createElement(Constants.NS_TSL,Constants.TAG_X509CERTIFICATE);
            x509CertificateElement.InnerText = certificateBase64Str;

            digitalIdElement = createElement(Constants.NS_TSL,Constants.TAG_DIGITALID);
            digitalIdElement.AppendChild(document.CreateTextNode("\n"));
            digitalIdElement.AppendChild(x509CertificateElement);
            digitalIdElement.AppendChild(document.CreateTextNode("\n"));
           
            addLineBreak();
            mElement.AppendChild(digitalIdElement);
            addLineBreak();
        }

        public TSLServiceDigitalIdentity(XmlElement aElement) : base(aElement)
        {
            digitalIdElement = XmlUtil.getNextElement(aElement.FirstChild);
            x509CertificateElement = XmlUtil.getNextElement(digitalIdElement.FirstChild);

            certificateBase64Str = XmlUtil.getText(x509CertificateElement);
            certificate = new ECertificate(certificateBase64Str);
        }

        public override string LocalName
        {
            get { return Constants.TAG_SERVICEDIGITALIDENTITY; }
        }

        public override string Namespace
        {
            get
            {
                return Constants.NS_TSL;
            }
        }

        public ECertificate X509Certificate
        {
            get { return certificate; }
        }

        public string Base64EncodedCertificate
        {
            get { return certificateBase64Str; }
        }
    }
}
