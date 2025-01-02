package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;

/**
 * @author ahmety
 * date: Jun 22, 2009
 */
public abstract class XAdESBaseElement extends BaseElement
{

    public XAdESBaseElement(Context aContext)
    {
        super(aContext);
    }

   /**
    *  Construct XADESBaseElement from existing
    *  @param aElement xml element
    *  @param aContext according to context
    *  @throws XMLSignatureException when structure is invalid or can not be
    *      resolved appropriately
    */
    public XAdESBaseElement(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    // base element
    public String getNamespace()
    {
        return Constants.NS_XADES_1_3_2;
    }

}
