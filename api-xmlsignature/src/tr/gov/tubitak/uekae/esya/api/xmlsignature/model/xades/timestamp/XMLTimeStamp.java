package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Any;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import org.w3c.dom.Element;

/**
 * @author ahmety
 * date: Sep 29, 2009
 */
public class XMLTimeStamp extends Any
{

    /**
     * Construct new BaseElement according to context
     * @param aContext where some signature spesific properties reside.
     */
    public XMLTimeStamp(Context aContext)
    {
        super(aContext);
    }

    /**
     * Construct Any from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public XMLTimeStamp(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    public String getLocalName()
    {
        return Constants.TAGX_XMLTIMESTAMP;
    }
}
