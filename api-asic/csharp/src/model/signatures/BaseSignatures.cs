using System;
using System.IO;
using System.Text;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.asic.core;
using tr.gov.tubitak.uekae.esya.api.asic.util;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature.impl;
using tr.gov.tubitak.uekae.esya.api.signature.sigpackage;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.provider;
using Context = tr.gov.tubitak.uekae.esya.api.signature.Context;
using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

namespace tr.gov.tubitak.uekae.esya.api.asic.model.signatures
{
    using Element = XmlElement;
    using Document = XmlDocument;

    /**
     * @author yavuz.kahveci
     */
    public abstract class BaseSignatures : AbstractSignatureContainer , ASiCDocument
    {
        private PackageContents owner;
        private readonly Context context;
        private string filename;

        private Element rootElement;
        private Document document;

        protected BaseSignatures(Context context, PackageContents contents)
        {
            this.context = context;
            owner = contents;
            rootElement = createRootElement();
            document.AppendChild(rootElement);
        }

        public override Signature createSignature(ECertificate certificate)
        {
            tr.gov.tubitak.uekae.esya.api.xmlsignature.Context xc = XMLProviderUtil.convert(context);
            ASiCUtil.fixResolversConfig(xc, owner);

            xc.Document = rootElement.OwnerDocument;

            XMLSignature xmlSignature = new XMLSignature(xc, false);
            rootElement.AppendChild(xmlSignature.Element);

            xmlSignature.addKeyInfo(certificate);

            SignatureImpl signature = new SignatureImpl(this, xmlSignature, null);
            rootSignatures.Add(signature);
            return signature;
        }

        public override void addExternalSignature(Signature signature)
        {
            if (signature != typeof(SignatureImpl))
                throw new SignatureRuntimeException("Unknown Signature impl! "+signature.GetType());

            base.addExternalSignature(signature);
            rootElement.AppendChild(((SignatureImpl)signature).getInternalSignature().Element);
        }

        public override void detachSignature(Signature signature)
        {
            try {
                SignatureImpl xmlSignatureImpl = (SignatureImpl)signature;
                XMLSignature xmlSignature = (XMLSignature)xmlSignatureImpl.getUnderlyingObject();
                rootElement.RemoveChild(xmlSignature.Element);
                rootSignatures.Remove(signature);
            }
            catch (Exception x){
                throw new SignatureException("Cant extract signature from container " + signature.GetType(), x);
            }
        }

        public override SignatureFormat getSignatureFormat()
        {
            return SignatureFormat.XAdES;
        }

        public override bool isSignatureContainer(Stream stream)
        {
            return false;  // todo
        }

        public void setOwner(PackageContents aOwner)
        {
            owner = aOwner;
        }

        public override void read(Stream stream)
        {
            try {
                //Document domDocument = new Document();
                XmlReader reader = XmlReader.Create(stream);
                document.PreserveWhitespace = true;
                document.Load(reader);
                //document = domDocument;
                rootElement = document.DocumentElement;

                Element[] sigElms = XmlUtil.selectNodes(rootElement.FirstChild, ASiCConstants.NS_XMLDSIG, ASiCConstants.TAG_SIGNATURE);

                foreach(Element se in sigElms){
                    tr.gov.tubitak.uekae.esya.api.xmlsignature.Context xc = XMLProviderUtil.convert(context);
                    ASiCUtil.fixResolversConfig(xc, owner);
                    xc.Document = document;
                    XMLSignature xmlSig = new XMLSignature(se, xc);
                    SignatureImpl signature = new SignatureImpl(this, xmlSig, null);

                    rootSignatures.Add(signature);
                }
            }
            catch (Exception x) {
                throw new SignatureException("Error in reading XML Signature", x);//todo
            }

        }

        public override void write(Stream stream)
        {
            try {
                BinaryWriter bw = new BinaryWriter(stream);
                Document ownerDocument = rootElement.OwnerDocument;
                if(!ownerDocument.InnerXml.Contains(XmlCommonUtil.XML_PREAMBLE_STR))
                {
                    byte[] utf8Definition = XmlCommonUtil.XML_PREAMBLE;
                    stream.Write(utf8Definition, 0, utf8Definition.Length);
                    stream.Flush();
                }
                string outerXml = rootElement.OuterXml;
                byte[] bytes = Encoding.UTF8.GetBytes(outerXml);
                stream.Write(bytes,0,bytes.Length);
                stream.Flush();
            }
            catch (Exception x) {
                Console.WriteLine(x.StackTrace);
                throw new SignatureException("Cannot output signature.", x);
            }
        }

        public override Object getUnderlyingObject()
        {
            return this;
        }

        public String getASiCDocumentName()
        {
            return filename;
        }

        public void setASiCDocumentName(String name)
        {
            filename = name;
        }

        protected Element createRootElement()
        {
            try {
                document = new XmlDocument();

                Element rootElement = document.CreateElement(getRootElementPrefix() + ":" + getRootElementName(), getRootElementNamespace());

                rootElement.SetAttribute(/*Constants.NS_NAMESPACESPEC,*/ "xmlns:"+getRootElementPrefix(), getRootElementNamespace());

                return rootElement;
            }
            catch (Exception x){
                throw new SignatureRuntimeException(x);
            }
        }
        
        protected abstract String getRootElementName();
        protected abstract String getRootElementPrefix();
        protected abstract String getRootElementNamespace();
    }
}