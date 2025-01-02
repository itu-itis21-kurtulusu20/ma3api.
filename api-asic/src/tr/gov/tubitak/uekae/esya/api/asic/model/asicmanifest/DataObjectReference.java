package tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asic.core.ASiCConstants;
import tr.gov.tubitak.uekae.esya.api.asic.model.XMLElement;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.signature.Signable;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;

import java.util.ArrayList;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.asic.core.ASiCConstants.*;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.ATTR_ALGORITHM;

/**
 * @author ayetgin
 */
public class DataObjectReference implements XMLElement
{
    private Element element;

    private DigestMethod digestMethod;
    private byte[] digestValue;

    private String uri;       // required
    private String mimeType;  // optional
    private Boolean rootFile; // optional

    private List<Extension> extensions = new ArrayList<Extension>();

    public DataObjectReference(Document document, Signable signable, DigestAlg digestAlg)
            throws SignatureException
    {
        element = document.createElementNS(ASiCConstants.NS_ASiC, "asic:"+ASiCConstants.TAG_DATAOBJECTREFERENCE);
        XmlUtil.addLineBreak(element);

        digestMethod = DigestMethod.resolveFromName(digestAlg);
        digestValue = signable.getDigest(digestAlg);
        uri = signable.getURI();

        element.setAttribute(ASiCConstants.ATTR_URI, uri);

        Element digestMethodElm = document.createElementNS(ASiCConstants.NS_XMLDSIG, "ds:"+ASiCConstants.TAG_DIGESTMETHOD);
        Element digestValueElm = document.createElementNS(ASiCConstants.NS_XMLDSIG, "ds:"+ASiCConstants.TAG_DIGESTVALUE);

        digestMethodElm.setAttributeNS(null, ATTR_ALGORITHM, digestMethod.getUrl());
        XmlUtil.setBase64EncodedText(digestValueElm, digestValue);

        element.appendChild(digestMethodElm);
        XmlUtil.addLineBreak(element);
        element.appendChild(digestValueElm);
        XmlUtil.addLineBreak(element);

        // todo mimetype, rootfile
    }

    public DataObjectReference(Element element) throws SignatureException
    {
        Element digestMethodElm = XmlUtil.selectFirstElement(element.getFirstChild(), NS_XMLDSIG, TAG_DIGESTMETHOD);
        Element digestValueElm = XmlUtil.selectFirstElement(element.getFirstChild(), NS_XMLDSIG, TAG_DIGESTVALUE);

        digestMethod = DigestMethod.resolve(digestMethodElm.getAttribute(ATTR_ALGO));
        digestValue = Base64.decode(digestValueElm.getTextContent());

        uri = element.getAttribute(ASiCConstants.ATTR_URI);
        if (uri==null)
            throw new SignatureException("Signature URI not found in ASiC Manifest");

        mimeType = element.getAttribute(ASiCConstants.ATTR_MIME);
        rootFile = element.getAttribute(ASiCConstants.ATTR_ROOTFILE).equalsIgnoreCase("true");

        Element extensionsElm = XmlUtil.selectFirstElement(element.getFirstChild(), NS_ASiC, TAG_DATAREFERENCE_EXTENSIONS);

        if (extensionsElm!=null){
            Element[] extensionElms = XmlUtil.selectNodes(element.getFirstChild(), NS_ASiC, TAG_EXTENSION);
            for (Element extElm : extensionElms){
                ExtensionImpl impl = new ExtensionImpl(extElm);
                extensions.add(impl);
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
