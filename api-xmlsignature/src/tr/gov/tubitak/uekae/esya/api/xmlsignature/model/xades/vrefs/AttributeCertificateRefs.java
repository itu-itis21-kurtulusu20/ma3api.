package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;

/**
 * This clause defines the <code>AttributeRevocationRefs</code> element able to
 * carry the references to the full set of revocation data that have been used
 * in the validation of the attribute certificate(s) present in the signature.
 * This is an unsigned property that qualifies the signature.
 *
 * <p>This property MAY be used only when a user attribute certificate is
 * present in the signature within the signature.
 *
 * <p>There SHALL be at most one occurence of this property in the signature.
 *
 * <p>Below follows the schema definition for this element.
 * <pre>
 * &lt;xsd:element name="AttributeRevocationRefs" type="CompleteRevocationRefsType"/&gt;
 * </pre>
 *
 * <p>NOTE: Copies of the revocation values referenced in this property may be 
 * held using the <code>AttributeRevocationValues</code> property.
 *
 * @author ahmety
 * date: Nov 9, 2009
 */
public class AttributeCertificateRefs extends CompleteCertificateRefs 
{

    public AttributeCertificateRefs(Context aContext)
    {
        super(aContext);
    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public AttributeCertificateRefs(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    @Override
    public String getLocalName()
    {
        return Constants.TAGX_ATTRIBUTECERTIFICATEREFS;    
    }
}
