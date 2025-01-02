package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import org.w3c.dom.Element;

/**
 * <p>The <code>CounterSignature</code> is an unsigned property that qualifies
 * the signature. A XAdES signature MAY have more than one
 * <code>CounterSignature</code> properties. As indicated by its name, it
 * contains one countersignature of the qualified signature.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;xsd:element name="CounterSignature" type="CounterSignatureType" /&gt;
 *
 * &lt;xsd:complexType name="CounterSignatureType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element ref="ds:Signature"/&gt;
 *   &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Sep 3, 2009
 */
public class CounterSignature extends XAdESBaseElement implements UnsignedSignaturePropertyElement
{
    private XMLSignature mSignature;

    public CounterSignature(Context aBaglam, XMLSignature aSignature)
    {
        super(aBaglam);  
        mSignature = aSignature;

        addLineBreak();
        mElement.appendChild(aSignature.getElement());
        addLineBreak();
    }

    /**
     *  Construct CounterSignature from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public CounterSignature(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        Element signatureElm = XmlUtil.getNextElement(aElement.getFirstChild());
        if (signatureElm==null){
               throw new XMLSignatureException("xml.WrongContent", TAG_SIGNATURE, TAGX_COUNTERSIGNATURE);
        }
        mSignature = new XMLSignature(signatureElm, aContext);
    }

    public XMLSignature getSignature()
    {
        return mSignature;
    }

    public String getLocalName()
    {
        return TAGX_COUNTERSIGNATURE;
    }
}
