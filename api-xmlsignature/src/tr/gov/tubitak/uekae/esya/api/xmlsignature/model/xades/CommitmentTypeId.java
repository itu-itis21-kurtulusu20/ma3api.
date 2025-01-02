package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

import java.util.List;

import org.w3c.dom.Element;

/**
 * @author ahmety
 * date: Sep 29, 2009
 */
public class CommitmentTypeId extends ObjectIdentifier
{

    public CommitmentTypeId(Context aBaglam,
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
    public CommitmentTypeId(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    @Override
    public String getLocalName()
    {
        return Constants.TAGX_COMMITMENTTYPEID;
    }
}
