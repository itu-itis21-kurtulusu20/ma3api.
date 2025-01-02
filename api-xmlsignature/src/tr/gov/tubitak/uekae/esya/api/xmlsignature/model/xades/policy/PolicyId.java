package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.ObjectIdentifier;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.Identifier;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

import java.util.List;

import org.w3c.dom.Element;

/**
 * The <code>SigPolicyId</code> element contains an identifier that uniquely
 * identifies a specific version of the signature policy.
 *
 * @author ahmety
 * date: Oct 15, 2009
 */
public class PolicyId extends ObjectIdentifier
{

    public PolicyId(Context aBaglam,
                    Identifier aIdentifier,
                    String aDescription,
                    List<String> aDocumentationReferences)
    {
        super(aBaglam, aIdentifier, aDescription, aDocumentationReferences);
    }

    /**
     * Construct ObjectIdentifier from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public PolicyId(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    @Override
    public String getLocalName()
    {
        return Constants.TAGX_SIGPOLICYID;
    }
}
