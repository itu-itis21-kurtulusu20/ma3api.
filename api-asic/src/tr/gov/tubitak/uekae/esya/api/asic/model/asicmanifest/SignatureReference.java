package tr.gov.tubitak.uekae.esya.api.asic.model.asicmanifest;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asic.core.ASiCConstants;
import tr.gov.tubitak.uekae.esya.api.asic.model.XMLElement;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;

/**
 * @author ayetgin
 */
public class SignatureReference implements XMLElement
{
    private Element element;
    private String uri;   // required
    private String mimeType; // optional

    public SignatureReference(org.w3c.dom.Document document, String uri) {
        element = document.createElementNS(ASiCConstants.NS_ASiC, "asic:"+ASiCConstants.TAG_SIGREFERENCE);
        this.uri = uri;
        element.setAttribute(ASiCConstants.ATTR_URI, uri);
    }


    public SignatureReference(Element element) throws SignatureException
    {
        this.element = element;
        uri = element.getAttribute(ASiCConstants.ATTR_URI);
        if (uri==null)
            throw new SignatureException("Signature URI not found in ASiC Manifest");

        mimeType = element.getAttribute(ASiCConstants.ATTR_MIME);
    }

    public String getUri()
    {
        return uri;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public Element getElement()
    {
        return element;
    }
}
