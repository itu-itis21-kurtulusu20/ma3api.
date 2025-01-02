package tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asic.core.ASiCConstants;
import tr.gov.tubitak.uekae.esya.api.asic.core.PackageContentResolver;
import tr.gov.tubitak.uekae.esya.api.asic.model.BaseASiCXMLDocument;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.signature.Signable;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.StringUtil;

import static tr.gov.tubitak.uekae.esya.api.asic.core.ASiCConstants.*;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_NAMESPACESPEC;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author ayetgin
 */
public class ASiCManifest extends BaseASiCXMLDocument
{
    private static Logger logger = LoggerFactory.getLogger(ASiCManifest.class);

    private SignatureReference signatureReference; // 1
    private List<DataObjectReference> dataObjectReferences = new ArrayList<DataObjectReference>(); // 1+
    private List<Extension> asicManifestExtensions = new ArrayList<Extension>(0); // 0+

    public ASiCManifest(){}

    public ASiCManifest(String signatureid, String signatureURI)
    {
        document = createDocument();
        documentName = "META-INF/ASiCManifest"+signatureid+".xml";

        Element rootElm = document.createElementNS(ASiCConstants.NS_ASiC, "asic:"+ASiCConstants.TAG_ASICMANIFEST);
        rootElm.setAttributeNS(NS_NAMESPACESPEC, "xmlns:asic", NS_ASiC);
        document.appendChild(rootElm);

        signatureReference = new SignatureReference(document, signatureURI);
        XmlUtil.addLineBreak(rootElm);
        rootElm.appendChild(signatureReference.getElement());
        XmlUtil.addLineBreak(rootElm);
    }

    protected void init(Element element) throws SignatureException
    {
        Element sigRefElm = XmlUtil.selectFirstElement(element.getFirstChild(), NS_ASiC, TAG_SIGREFERENCE);
        if (sigRefElm == null) {
            throw new SignatureException("No signature references found in ASiCManifest!"); // todo i18n
        }
        signatureReference = new SignatureReference(sigRefElm);

        Element[] objectRefElms = XmlUtil.selectNodes(element.getFirstChild(), NS_ASiC, TAG_DATAOBJECTREFERENCE);
        if (objectRefElms == null || objectRefElms.length == 0) {
            throw new SignatureException("No data object references found in ASiCManifest!"); // todo i18n
        }
        for (Element dorElm : objectRefElms) {
            dataObjectReferences.add(new DataObjectReference(dorElm));
        }

        Element extensions = XmlUtil.selectFirstElement(element.getFirstChild(), NS_ASiC, TAG_ASICMANIFEST_EXTENSIONS);

        if (extensions != null) {
            Element[] extensionElms = XmlUtil.selectNodes(element.getFirstChild(), NS_ASiC, TAG_EXTENSION);
            for (Element extElm : extensionElms) {
                ExtensionImpl impl = new ExtensionImpl(extElm);
                asicManifestExtensions.add(impl);
            }
        }
    }

    public void addContent(Signable data, DigestAlg digestAlg)
            throws SignatureException
    {
        DataObjectReference dof = new DataObjectReference(document, data, digestAlg);
        getElement().appendChild(dof.getElement());
        XmlUtil.addLineBreak(getElement());
        dataObjectReferences.add(dof);
    }


    public boolean validateDataObjectRefs(PackageContentResolver resolver)
    {
        for (DataObjectReference ref : dataObjectReferences) {
            try {
                Document doc = resolver.resolve(ref.getUri(), null);
                if (logger.isDebugEnabled()) {
                    logger.debug("Validating reference uri :" + ref.getUri());
                    logger.debug("Reference.mime :" + ref.getMimeType());
                    logger.debug("Reference.digestMethod :" + ref.getDigestMethod().getAlgorithm());
                    logger.debug("Reference.data (trimmed) :" + StringUtil.substring(doc.getBytes(), 256));
                }
                byte[] bytes;

                bytes = KriptoUtil.digest(doc.getBytes(), ref.getDigestMethod());

                if (logger.isDebugEnabled()) {
                    logger.debug("Original digest: " + Base64.encode(ref.getDigestValue()));
                    logger.debug("Calculated digest: " + Base64.encode(bytes));
                }
                boolean digestOk = Arrays.equals(bytes, ref.getDigestValue());
                if (!digestOk) {
                    logger.info("Reference( uri: '" + ref.getUri() + "' could not be validated.");
                    return false;
                }
                else {
                    logger.info("Reference( uri: '" + ref.getUri() + "' validated.");
                }

            }
            catch (Exception x) {
                logger.info("Referans( ' uri: '" + ref.getUri() + "' could not be processed.", x);
                return false;
            }
        }

        return true;
    }

    public SignatureReference getSignatureReference()
    {
        return signatureReference;
    }

    public List<DataObjectReference> getDataObjectReferences()
    {
        return dataObjectReferences;
    }

    public List<Extension> getAsicManifestExtensions()
    {
        return asicManifestExtensions;
    }
}
