package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Any;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>RevocationValues</code> property element is used to hold the values 
 * of the revocation information which are to be shipped with the electronic 
 * signature. If <code>CompleteRevocationRefs</code> and 
 * <code>RevocationValues</code> are present, all the revocation data referenced 
 * in <code>RevocationRefs</code> MUST be present either in the 
 * <code>ds:KeyInfo</code> element of the signature or in the 
 * <code>RevocationValues</code> property element.
 * 
 * <p>This is an optional unsigned property that qualifies the signature.
 * 
 * <p>There SHALL be at most one occurence of this property in the signature.
 * 
 * <p>Below follows the Schema definition for this element.
 * <pre>
 * &lt;xsd:element name="RevocationValues" type="RevocationValuesType"/&gt;
 *
 * &lt;xsd:complexType name="RevocationValuesType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="CRLValues" type="CRLValuesType" minOccurs="0"/&gt;
 *     &lt;xsd:element name="OCSPValues" type="OCSPValuesType" minOccurs="0"/&gt;
 *     &lt;xsd:element name="OtherValues" type="OtherCertStatusValuesType" minOccurs="0"/&gt;
 *   &lt;/xsd:sequence&gt;
 *   &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 * 
 * <p>Revocation information can include Certificate Revocation Lists 
 * (CRLValues) or responses from an online certificate status server 
 * (OCSPValues). Additionally a placeholder for other revocation information 
 * (OtherValues) is provided for future use.
 *
 * <p><font color="red">Note: Curretly other values is not supported..</font></p>
 * 
 * @author ahmety
 * date: Dec 17, 2009
 */
public class RevocationValues extends RevocationValuesType
        implements UnsignedSignaturePropertyElement
{

    private static final Logger logger = LoggerFactory.getLogger(RevocationValues.class);

    public RevocationValues(Context aContext)
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
    public RevocationValues(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        if (logger.isDebugEnabled()){
            logger.debug("REVOCATION VALUES");
            for (int i=0; i<getCRLValueCount(); i++){
                logger.debug(""+getCRL(i));
            }
            for (int i=0; i<getOCSPValueCount(); i++){
                logger.debug(""+getOCSPResponse(i));
            }
        }
    }


    @Override
    public String getLocalName()
    {
        return TAGX_REVOCATIONVALUES;
    }
}
