package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

/**
 * <dl><dt>Identifier</dt>
 *
 * <dd><code>Type="<a name="PGPData" href="http://www.w3.org/2000/09/xmldsig#PGPData">http://www.w3.org/2000/09/xmldsig#PGPData</a></code>"
 *
 * (this can be used within a <code>RetrievalMethod</code> or
 * <code>Reference</code> element to identify the referent's type)</dd>
 * </dl>
 *
 * <p>The <code>PGPData</code> element within <code>KeyInfo</code> is used to
 * convey information related to PGP public key pairs and signatures on such
 * keys. The <code>PGPKeyID</code>'s value is a base64Binary sequence
 * containing a standard PGP public key identifier as defined in
 * [<a href="http://www.ietf.org/rfc/rfc2440.txt">PGP</a>, section 11.2]. The
 * <code>PGPKeyPacket</code> contains a base64-encoded Key Material Packet as 
 * defined in [<a href="http://www.ietf.org/rfc/rfc2440.txt">PGP</a>, section
 * 5.5]. These children element types can be complemented/extended by siblings
 * from an * external namespace within <code>PGPData</code>, or
 * <code>PGPData</code> can be replaced all together with an alternative PGP XML
 * structure as a child of <code>KeyInfo</code>. <code>PGPData</code> must
 * contain one <code>PGPKeyID</code> and/or one <code>PGPKeyPacket</code> and 0
 * or more elements from an external namespace.</p>
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PGPDataType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="PGPKeyID" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *           &lt;element name="PGPKeyPacket" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *           &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;/sequence&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="PGPKeyPacket" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *           &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;/sequence&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 * @author ahmety
 * date: Jun 16, 2009
 *
 * todo implement this class
 */
public class PGPData extends BaseElement implements KeyInfoElement
{

    /**
     *  Construct PGPData from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public PGPData(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    // base element
    public String getLocalName()
    {
        return Constants.TAG_PGPDATA;
    }
}
