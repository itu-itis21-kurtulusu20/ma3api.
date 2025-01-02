using System;
using System.Xml;
using System.Collections.Generic;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asic.core;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.document;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest
{
    using Element = XmlElement;
    using XmlUtil = common.util.XmlUtil;

    /**
     * @author yavuz.kahveci
     */
    public class ASiCManifest : BaseASiCXMLDocument
    {
        private static readonly ILog Logger = LogManager.GetLogger(typeof(ASiCManifest));

        private SignatureReference _signatureReference; // 1
        private readonly List<DataObjectReference> _dataObjectReferences = new List<DataObjectReference>(); // 1+
        private readonly List<Extension> _asicManifestExtensions = new List<Extension>(0); // 0+

        public ASiCManifest(){}

        public ASiCManifest(string signatureid, string signatureURI)
        {
            document = createDocument();
            documentName = "META-INF/ASiCManifest"+signatureid+".xml";

            Element rootElm = document.CreateElement("asic:" + ASiCConstants.TAG_ASICMANIFEST, ASiCConstants.NS_ASiC);
            rootElm.SetAttribute(/*Constants.NS_NAMESPACESPEC,*/ "xmlns:asic", ASiCConstants.NS_ASiC);
            document.AppendChild(rootElm);

            _signatureReference = new SignatureReference(document, signatureURI);
            XmlUtil.addLineBreak(rootElm);
            rootElm.AppendChild(_signatureReference.getElement());
            XmlUtil.addLineBreak(rootElm);
        }

        protected override void init(Element element)
        {
            Element sigRefElm = XmlUtil.selectFirstElement(element.FirstChild, ASiCConstants.NS_ASiC, ASiCConstants.TAG_SIGREFERENCE);
            if (sigRefElm == null) {
                throw new SignatureException("No signature references found in ASiCManifest!"); // todo i18n
            }
            _signatureReference = new SignatureReference(sigRefElm);

            Element[] objectRefElms = XmlUtil.selectNodes(element.FirstChild, ASiCConstants.NS_ASiC, ASiCConstants.TAG_DATAOBJECTREFERENCE);
            if (objectRefElms == null || objectRefElms.Length == 0) {
                throw new SignatureException("No data object references found in ASiCManifest!"); // todo i18n
            }
            foreach (Element dorElm in objectRefElms) {
                _dataObjectReferences.Add(new DataObjectReference(dorElm));
            }

            Element extensions = XmlUtil.selectFirstElement(element.FirstChild, ASiCConstants.NS_ASiC, ASiCConstants.TAG_ASICMANIFEST_EXTENSIONS);

            if (extensions != null) {
                Element[] extensionElms = XmlUtil.selectNodes(element.FirstChild, ASiCConstants.NS_ASiC, ASiCConstants.TAG_EXTENSION);
                foreach (Element extElm in extensionElms) {
                    ExtensionImpl impl = new ExtensionImpl(extElm);
                    _asicManifestExtensions.Add(impl);
                }
            }
        }

        public void addContent(Signable data, DigestAlg digestAlg)
        {
            DataObjectReference dof = new DataObjectReference(document, data, digestAlg);
            getElement().AppendChild(dof.getElement());
            XmlUtil.addLineBreak(getElement());
            _dataObjectReferences.Add(dof);
        }


        public bool validateDataObjectRefs(PackageContentResolver resolver)
        {
            foreach (DataObjectReference dor in _dataObjectReferences) {
                try {
                    Document doc = resolver.resolve(dor.getUri(), null);
                    if (Logger.IsDebugEnabled) {
                        Logger.Debug("Validating reference uri :" + dor.getUri());
                        Logger.Debug("Reference.mime :" + dor.getMimeType());
                        Logger.Debug("Reference.digestMethod :" + dor.getDigestMethod().Algorithm);
                        Logger.Debug("Reference.data (trimmed) :" + StringUtil.substring(doc.Bytes, 256));
                    }

                    var bytes = KriptoUtil.digest(doc.Bytes, dor.getDigestMethod());

                    if (Logger.IsDebugEnabled) {
                        Logger.Debug("Original digest: " + Base64.Encode(dor.getDigestValue()));
                        Logger.Debug("Calculated digest: " + Base64.Encode(bytes));
                    }
                    bool digestOk = ArrayUtil.Equals(bytes, dor.getDigestValue());
                    if (!digestOk) {
                        Logger.Info("Reference( uri: '" + dor.getUri() + "' could not be validated.");
                        return false;
                    }
                    Logger.Info("Reference( uri: '" + dor.getUri() + "' validated.");
                }
                catch (Exception x) {
                    Logger.Info("Referans( ' uri: '" + dor.getUri() + "' could not be processed.", x);
                    return false;
                }
            }

            return true;
        }

        public SignatureReference getSignatureReference()
        {
            return _signatureReference;
        }

        public List<DataObjectReference> getDataObjectReferences()
        {
            return _dataObjectReferences;
        }

        public List<Extension> getAsicManifestExtensions()
        {
            return _asicManifestExtensions;
        }
    }
}
