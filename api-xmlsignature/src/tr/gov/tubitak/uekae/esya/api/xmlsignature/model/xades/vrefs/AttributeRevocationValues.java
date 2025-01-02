package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;

/**
 * This property contains the set of revocation data that have been used to
 * validate the attribute certificate when present in the signature. Should any
 * of the revocation data present within <code>RevocationValues</code> property
 * have been used for validate the attribute certificate, they do not need to
 * appear within the <code>AttributeRevocationValues</code>.
 *
 * <p>If <code>AttributeRevocationRefs</code> and
 * <code>AttributeRevocationValues</code> are present,
 * <code>AttributeRevocationValues</code> and <code>RevocationValues</code> MUST
 * contain the values of all the objects referenced in
 * <code>AttributeRevocationRefs</code>.
 *
 * <p>This is an optional unsigned property that qualifies the signature.
 *
 * <p>There SHALL be at most one occurence of this property in the signature.
 *
 * <p>Below follows the Schema definition for this element.
 *
 * <pre>
 * &lt;xsd:element name="AttributeRevocationValues" type="RevocationValuesType"/&gt;
 * </pre>
 * @author ahmety
 * date: Jan 6, 2010
 */
public class AttributeRevocationValues extends RevocationValuesType implements UnsignedSignaturePropertyElement
{

    public AttributeRevocationValues(Context aContext)
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
    public AttributeRevocationValues(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    @Override
    public String getLocalName()
    {
        return Constants.TAGX_ATTRIBUTEREVOCATIONVALUES;  
    }
}
