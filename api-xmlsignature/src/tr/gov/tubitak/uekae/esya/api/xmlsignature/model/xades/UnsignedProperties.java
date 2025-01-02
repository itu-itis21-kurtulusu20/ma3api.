package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import org.w3c.dom.Element;

/**
 * <p>The <code>UnsignedProperties</code> element MAY contain properties that
 * qualify XML signature itself or the signer. They are included as content of
 * the <code>UnsignedSignatureProperties</code> element.
 *
 * <p>The <code>UnsignedProperties</code> element MAY also contain properties
 * that qualify some of the signed data objects. These properties appear as
 * content of the <code>UnsignedDataObjectProperties</code> element.
 *
 * <p>The optional <code>Id</code> attribute can be used to make a reference to the
 * <code>UnsignedProperties</code> element.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 * 
 * <p><pre>
 * &lt;complexType name="UnsignedPropertiesType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="UnsignedSignatureProperties" type="{http://uri.etsi.org/01903/v1.3.2#}UnsignedSignaturePropertiesType" minOccurs="0"/&gt;
 *         &lt;element name="UnsignedDataObjectProperties" type="{http://uri.etsi.org/01903/v1.3.2#}UnsignedDataObjectPropertiesType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Jun 22, 2009
 */
public class UnsignedProperties extends XAdESBaseElement
{

    private UnsignedSignatureProperties mUnsignedSignatureProperties;
    private UnsignedDataObjectProperties mUnsignedDataObjectProperties;

    
    public UnsignedProperties(Context aContext, XMLSignature aSignature)
    {
        super(aContext);

        mUnsignedSignatureProperties= new UnsignedSignatureProperties(aContext, aSignature);

        addLineBreak();
        mElement.appendChild(mUnsignedSignatureProperties.getElement());
        addLineBreak();
    }

    /**
     * Construct UnsignedProperties from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public UnsignedProperties(Element aElement, Context aContext, XMLSignature aSignature)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        Element usp = selectChildElement(NS_XADES_1_3_2, TAGX_UNSIGNEDSIGNATUREPROPERTIES);
        if (usp!=null){
            mUnsignedSignatureProperties = new UnsignedSignatureProperties(usp, mContext, aSignature);
        }
    }

    public UnsignedSignatureProperties getUnsignedSignatureProperties()
    {
        return mUnsignedSignatureProperties;
    }

    public UnsignedDataObjectProperties getUnsignedDataObjectProperties(){
        return mUnsignedDataObjectProperties;
    }

    public UnsignedDataObjectProperties createOrGetUnsignedDataObjectProperties()
    {
        if (mUnsignedDataObjectProperties==null){
            mUnsignedDataObjectProperties = new UnsignedDataObjectProperties(mContext);
            mElement.appendChild(mUnsignedDataObjectProperties.getElement());
            addLineBreak();
        }
        return mUnsignedDataObjectProperties;
    }


    public String getLocalName()
    {
        return TAGX_UNSIGNEDPROPERTIES;
    }
}
