package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;

/**
 * <dl>
 *   <dt>Identifier</dt>
 *   <dd><code>Type="<a name="SPKIData" href="http://www.w3.org/2000/09/xmldsig#SPKIData">http://www.w3.org/2000/09/xmldsig#SPKIData</a>
 *   </code>"
 * (this can be used within a <code>RetrievalMethod</code> or
 * <code>Reference</code> element to identify the referent's type)</dd>
 * </dl>
 *
 * <p>The <code>SPKIData</code> element within <code>KeyInfo</code> is used to
 * convey information related to SPKI public key pairs, certificates and other
 * SPKI data. <code>SPKISexp</code> is the base64 encoding of a SPKI canonical
 * S-expression. <code>SPKIData</code> must have at least one
 * <code>SPKISexp</code>; <code>SPKISexp</code> can be complemented/extended by
 * siblings from an external namespace within <code>SPKIData</code>, or
 * <code>SPKIData</code> can be entirely replaced with an alternative SPKI XML
 * structure as a child of <code>KeyInfo</code>.</p>
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;complexType name="SPKIDataType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence maxOccurs="unbounded"&gt;
 *         &lt;element name="SPKISexp" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *         &lt;any processContents='lax' namespace='##other' minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
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
public class SPKIData extends BaseElement implements KeyInfoElement
{

    /**
     *  Construct SPKIData from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public SPKIData(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        throw new ESYARuntimeException("Not yet implemented! "+getLocalName());
    }

    // base element
    public String getLocalName()
    {
        return Constants.TAG_SPKIDATA;
    }
}
