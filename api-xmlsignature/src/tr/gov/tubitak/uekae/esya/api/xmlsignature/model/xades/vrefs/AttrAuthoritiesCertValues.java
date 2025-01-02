package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;
import tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate;

/**
 * This property contains the certificate values of the Attribute Authorities
 * that have been used to validate the attribute certificate when present in the
 * signature. Should any of the certificates present within
 * <code>CertificateValues</code> property have been used for validate the
 * attribute certificate, they do not need to appear within the
 * <code>AttrAuthoritiesCertValues</code>.
 *
 * <p>If <code>AttributeCertificateRefs</code> and
 * <code>AttrAuthoritiesCertValues</code> are present,
 * <code>AttrAuthoritiesCertValues</code> and <code>CertificateValues</code>
 * properties MUST contain all the certificates referenced in
 * <code>AttributeCertificateRefs</code>.
 *
 * <p>This is an optional unsigned property that qualifies the signature.
 *
 * <p>There SHALL be at most one occurence of this property in the signature.
 *
 * <p>Below follows the Schema definition for this element.
 * <pre>
 * &lt;xsd:element name="AttrAuthoritiesCertValues" type="CertificateValuesType"/&gt;
 * </pre>
 *
 * @author ahmety
 * date: Jan 6, 2010
 */
public class AttrAuthoritiesCertValues extends CertificateValuesType implements UnsignedSignaturePropertyElement
{

    public AttrAuthoritiesCertValues(Context aContext, AttributeCertificate[] aCertificates)
            throws XMLSignatureException
    {
        super(aContext, aCertificates);
    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public AttrAuthoritiesCertValues(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    @Override
    public String getLocalName()
    {
        return Constants.TAGX_ATTRAUHORITIESCERTVALUES;  
    }
}
