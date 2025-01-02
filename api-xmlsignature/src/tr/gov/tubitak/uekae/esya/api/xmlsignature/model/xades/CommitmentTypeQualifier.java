package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Any;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import org.w3c.dom.Element;

/**
 * @author ahmety
 * date: Sep 23, 2009
 */
public class CommitmentTypeQualifier extends Any
{

    /**
     * Construct new BaseElement according to context
     * @param aContext where some signature spesific properties reside.
     */
    public CommitmentTypeQualifier(Context aContext)
    {
        super(aContext);
    }

    /**
     * Construct new CommitmentTypeQualifier according to context
     * @param aContext where some signature spesific properties reside.
     * @param aText that defines commitment type. 
     */
    public CommitmentTypeQualifier(Context aContext, String aText)
    {
        super(aContext);
        mElement.appendChild(getDocument().createTextNode(aText));
    }


    /**
     * Construct Any from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public CommitmentTypeQualifier(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    @Override
    public String getNamespace()
    {
        return NS_XADES_1_3_2;
    }

    public String getLocalName()
    {
        return TAGX_COMMITMENTTYPEQUALIFIER;  
    }
}
