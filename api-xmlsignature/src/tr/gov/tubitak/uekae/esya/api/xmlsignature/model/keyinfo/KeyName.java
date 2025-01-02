package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

/**
 * <p>The KeyName element contains a string value (in which white space is
 * significant) which may be used by the signer to communicate a key identifier
 * to the recipient. Typically, KeyName contains an identifier related to the
 * key pair used to sign the message, but it may contain other protocol-related
 * information that indirectly identifies a key pair. (Common uses of KeyName
 * include simple string names for keys, a key index, a distinguished name (DN),
 * an email address, etc.)
 *
 * @author ahmety
 * date: Jun 10, 2009
 */
public class KeyName extends BaseElement implements KeyInfoElement
{
    private String mName;


    /**
     *  Construct KeyName from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public KeyName(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String aName)
    {
        mName = aName;
    }

    // baseelement
    public String getLocalName()
    {
        return Constants.TAG_KEYNAME;
    }
}
