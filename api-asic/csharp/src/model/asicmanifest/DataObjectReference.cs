/**
 * @author yavuz.kahveci
 */
using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asic.core;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.core;

namespace tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest
{
    using Element = System.Xml.XmlElement;
    using Document = System.Xml.XmlDocument;

    public class DataObjectReference : XMLElement
    {
        private readonly Element element;

        private readonly DigestMethod digestMethod;
        private readonly byte[] digestValue;

        private readonly string uri;       // required
        private readonly string mimeType;  // optional
        private readonly Boolean rootFile; // optional

        private readonly List<Extension> extensions = new List<Extension>();

        public DataObjectReference(Document document, Signable signable, DigestAlg digestAlg)
        {
            element = document.CreateElement("asic:" + ASiCConstants.TAG_DATAOBJECTREFERENCE, ASiCConstants.NS_ASiC);
            XmlUtil.addLineBreak(element);

            digestMethod = DigestMethod.resolveFromName(digestAlg);
            digestValue = signable.getDigest(digestAlg);
            uri = signable.getURI();

            element.SetAttribute(ASiCConstants.ATTR_URI, uri);

            Element digestMethodElm = document.CreateElement("ds:" + ASiCConstants.TAG_DIGESTMETHOD, ASiCConstants.NS_XMLDSIG);
            Element digestValueElm = document.CreateElement("ds:" + ASiCConstants.TAG_DIGESTVALUE, ASiCConstants.NS_XMLDSIG);

            digestMethodElm.SetAttribute(/*null,*/ Constants.ATTR_ALGORITHM, digestMethod.Url);
            XmlUtil.setBase64EncodedText(digestValueElm, digestValue);

            element.AppendChild(digestMethodElm);
            XmlUtil.addLineBreak(element);
            element.AppendChild(digestValueElm);
            XmlUtil.addLineBreak(element);

            // todo mimetype, rootfile
        }

        public DataObjectReference(Element element)
        {
            Element digestMethodElm = XmlUtil.selectFirstElement(element.FirstChild, ASiCConstants.NS_XMLDSIG, ASiCConstants.TAG_DIGESTMETHOD);
            Element digestValueElm = XmlUtil.selectFirstElement(element.FirstChild, ASiCConstants.NS_XMLDSIG, ASiCConstants.TAG_DIGESTVALUE);

            digestMethod = DigestMethod.resolve(digestMethodElm.GetAttribute(ASiCConstants.ATTR_ALGO));
            digestValue = Base64.Decode(digestValueElm.InnerText);

            uri = element.GetAttribute(ASiCConstants.ATTR_URI);
            if (uri==null)
            {
                throw new SignatureException("Signature URI not found in ASiC Manifest");
            }

            mimeType = element.GetAttribute(ASiCConstants.ATTR_MIME);
            rootFile = element.GetAttribute(ASiCConstants.ATTR_ROOTFILE).Equals("true",StringComparison.OrdinalIgnoreCase);//.equalsIgnoreCase("true");

            Element extensionsElm = XmlUtil.selectFirstElement(element.FirstChild, ASiCConstants.NS_ASiC, ASiCConstants.TAG_DATAREFERENCE_EXTENSIONS);

            if (extensionsElm!=null){
                Element[] extensionElms = XmlUtil.selectNodes(element.FirstChild, ASiCConstants.NS_ASiC, ASiCConstants.TAG_EXTENSION);
                foreach (Element extElm in extensionElms){
                    ExtensionImpl impl = new ExtensionImpl(extElm);
                    extensions.Add(impl);
                }
            }

        }

        public DigestMethod getDigestMethod()
        {
            return digestMethod;
        }

        public byte[] getDigestValue()
        {
            return digestValue;
        }

        public String getUri()
        {
            return uri;
        }

        public String getMimeType()
        {
            return mimeType;
        }

        public Boolean isRootFile()
        {
            return rootFile;
        }

        public List<Extension> getExtensions()
        {
            return extensions;
        }

        public Element getElement()
        {
            return element;
        }
    }
}
