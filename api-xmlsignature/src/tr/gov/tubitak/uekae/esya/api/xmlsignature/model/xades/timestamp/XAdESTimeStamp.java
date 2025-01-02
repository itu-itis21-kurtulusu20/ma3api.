package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LE;
import tr.gov.tubitak.uekae.esya.api.common.lcns.LV;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.TimestampType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;


/**
 * <p>This concrete derived type is provided for containing time-stamp tokens
 * computed on data objects of XAdES signatures.
 *
 * <p>This type provides two mechanisms for identifying data objects that are
 * covered by the time-stamp token present in the container, and for specifying
 * how to use them for computing the digest value that is sent to the TSA:
 * <ul>
 * <li>Explicit. This mechanism uses the Include element for referencing
 * specific data objects and for indicating their contribution to the input of
 * the digest computation.
 * <li>Implicit. For certain time-stamp container properties under certain
 * circumstances, applications do not require any additional indication for
 * knowing that certain data objects are covered by the time-stamp tokens and
 * how they contribute to the input of the digest computation. The present
 * document specifies, in the clauses defining such properties (clauses 7.2.9,
 * 7.2.10, 7.3, 7.5 and 7.7), how applications MUST act in these cases without
 * explicit indications.
 * </ul>
 *
 * <pre>
 * &lt;xsd:complexType name="XAdESTimeStampType"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:restriction base="GenericTimeStampType"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element ref="Include" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="ds:CanonicalizationMethod" minOccurs="0"/&gt;
 *         &lt;xsd:choice maxOccurs="unbounded"&gt;
 *           &lt;xsd:element name="EncapsulatedTimeStamp" type="EncapsulatedPKIDataType"/&gt;
 *           &lt;xsd:element name="XMLTimeStamp" type="AnyType"/&gt;
 *         &lt;/xsd:choice&gt;
 *       &lt;/xsd:sequence&gt;
 *       &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/&gt;
 *     &lt;/xsd:restriction&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexTypev
 * </pre>
 *
 * @author ahmety
 * date: Sep 28, 2009
 */
public abstract class XAdESTimeStamp extends GenericTimeStamp {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected XAdESTimeStamp(Context aContext)
    {
        super(aContext);
        checkBeforeUpgrade();
    }

    /**
     * Construct GenericTimeStamp from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    protected XAdESTimeStamp(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    private void checkBeforeUpgrade() {

        try
        {
            LV.getInstance().checkLD(LV.Urunler.XMLIMZAGELISMIS);
        }
        catch(LE e)
        {
            logger.error("Lisans kontrolu basarisiz.", e);
            throw new ESYARuntimeException("Lisans kontrolu basarisiz. "+ e.getMessage(), e);
        }


    }

    public abstract TimestampType getType();

    public abstract byte[] getContentForTimeStamp(XMLSignature aSignature) throws XMLSignatureException;
}
