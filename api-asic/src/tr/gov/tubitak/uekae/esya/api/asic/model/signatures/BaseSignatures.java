package tr.gov.tubitak.uekae.esya.api.asic.model.signatures;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asic.core.ASiCConstants;
import tr.gov.tubitak.uekae.esya.api.asic.model.ASiCDocument;
import tr.gov.tubitak.uekae.esya.api.asic.model.PackageContents;
import tr.gov.tubitak.uekae.esya.api.asic.util.ASiCUtil;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.signature.*;
import tr.gov.tubitak.uekae.esya.api.signature.impl.AbstractSignatureContainer;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.provider.SignatureImpl;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.provider.XMLProviderUtil;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ayetgin
 */
public abstract class BaseSignatures extends AbstractSignatureContainer implements ASiCDocument
{
    protected static Logger logger = LoggerFactory.getLogger(BaseSignatures.class);
    private PackageContents owner;
    private Context context;
    private String filename;

    private Element rootElement;
    private Document document;

    protected BaseSignatures(Context context, PackageContents contents)
    {
        this.context = context;
        owner = contents;
        rootElement = createRootElement();
        document.appendChild(rootElement);
    }

    public Signature createSignature(ECertificate certificate)
    {
        tr.gov.tubitak.uekae.esya.api.xmlsignature.Context xc = XMLProviderUtil.convert(context);
        ASiCUtil.fixResolversConfig(xc, owner);

        xc.setDocument(rootElement.getOwnerDocument());

        XMLSignature xmlSignature = new XMLSignature(xc, false);
        rootElement.appendChild(xmlSignature.getElement());

        xmlSignature.addKeyInfo(certificate);

        SignatureImpl signature = new SignatureImpl(this, xmlSignature, null);
        rootSignatures.add(signature);
        return signature;
    }

    public void addExternalSignature(Signature signature) throws SignatureException
    {
        if (!(signature instanceof SignatureImpl))
            throw new SignatureRuntimeException("Unknown Signature impl! "+signature.getClass());

        super.addExternalSignature(signature);
        rootElement.appendChild(((SignatureImpl)signature).getInternalSignature().getElement());
    }

    public void detachSignature(Signature signature) throws SignatureException
    {
        try {
            SignatureImpl xmlSignatureImpl = (SignatureImpl)signature;
            XMLSignature xmlSignature = (XMLSignature)xmlSignatureImpl.getUnderlyingObject();
            rootElement.removeChild(xmlSignature.getElement());
            rootSignatures.remove(signature);
        }
        catch (Exception x){
            throw new SignatureException("Cant extract signature from container "+ signature.getClass(), x);
        }
    }

    public SignatureFormat getSignatureFormat()
    {
        return SignatureFormat.XAdES;
    }

    public boolean isSignatureContainer(InputStream stream) throws SignatureException
    {
        return false;  // todo
    }

    public void setOwner(PackageContents aOwner)
    {
        owner = aOwner;
    }

    public void read(InputStream stream) throws SignatureException
    {
        try {
            Source s = new StreamSource(stream);
            DOMResult dom = new DOMResult();
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.transform(s, dom);
            document = (Document) dom.getNode();
            rootElement = document.getDocumentElement();

            Element[] sigElms = XmlUtil.selectNodes(rootElement.getFirstChild(), ASiCConstants.NS_XMLDSIG, ASiCConstants.TAG_SIGNATURE);

            for (Element se : sigElms){
                tr.gov.tubitak.uekae.esya.api.xmlsignature.Context xc = XMLProviderUtil.convert(context);
                ASiCUtil.fixResolversConfig(xc, owner);
                xc.setDocument(document);

                XMLSignature xmlSig = new XMLSignature(se, xc);

                SignatureImpl signature = new SignatureImpl(this, xmlSig, null);

                rootSignatures.add(signature);
            }
        }
        catch (Exception x) {
            throw new SignatureException("Error in reading XML Signature", x);//todo
        }

    }

    public void write(OutputStream stream) throws SignatureException
    {
        try {
            Source s = new DOMSource(rootElement);
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.transform(s, new StreamResult(stream));
        }
        catch (Exception x) {
            logger.error("Error in BaseSignatures", x);
            throw new SignatureException("Cannot output signature.", x);
        }
    }

    public Object getUnderlyingObject()
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
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Element rootElement = document.createElement(getRootElementPrefix() + ":" + getRootElementName());

            rootElement.setAttributeNS(Constants.NS_NAMESPACESPEC, "xmlns:"+getRootElementPrefix(), getRootElementNamespace());

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
