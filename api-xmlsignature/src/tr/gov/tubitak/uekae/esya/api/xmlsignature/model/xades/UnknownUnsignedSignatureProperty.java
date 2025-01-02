package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;

/**
 * @author ayetgin
 */
public class UnknownUnsignedSignatureProperty extends BaseElement implements UnsignedSignaturePropertyElement
{

    public UnknownUnsignedSignatureProperty(Element aElement, Context aContext) throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    @Override
    public String getLocalName()
    {
        return mElement.getLocalName();
    }

    @Override
    public String getNamespace()
    {
        return mElement.getNamespaceURI();
    }

    @Override
    protected void checkNamespace() throws XMLSignatureException
    {
        // no op
    }
}
