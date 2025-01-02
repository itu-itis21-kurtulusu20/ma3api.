package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Any;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import org.w3c.dom.Element;

/**
 * <p>The signer MAY state his own role without any certificate to corroborate 
 * this claim, in which case the claimed role can be added to the signature
 * as a signed qualifying property.
 *
 * @author ahmety
 * date: Sep 23, 2009
 */
public class ClaimedRole extends Any
{

    /**
     * Construct new BaseElement according to context
     * @param aContext where some signature spesific properties reside.
     */
    public ClaimedRole(Context aContext)
    {
        super(aContext);
    }

    /**
     * Construct new ClaimedRole according to context
     * @param aContext where some signature spesific properties reside.
     * @param aRoleName role as text
     */
    public ClaimedRole(Context aContext, String aRoleName)
    {
        super(aContext);
        mElement.appendChild(getDocument().createTextNode(aRoleName));
    }

    /**
     * Construct Any from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public ClaimedRole(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    // base element

    @Override
    public String getNamespace()
    {
        return Constants.NS_XADES_1_3_2;
    }

    public String getLocalName()
    {
        return Constants.TAGX_CLAIMEDROLE;
    }
}
