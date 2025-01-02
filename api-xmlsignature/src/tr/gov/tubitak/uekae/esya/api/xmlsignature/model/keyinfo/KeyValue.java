package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

import java.security.PublicKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;

import gnu.crypto.key.ecdsa.ECDSAPublicKey;

/**
 * <p>The KeyValue element contains a single public key that may be useful in
 * validating the signature. Structured formats for defining DSA (REQUIRED) and
 * RSA (RECOMMENDED) public keys are defined in Signature Algorithms (section
 * 6.4). The KeyValue element may include externally defined public keys values
 * represented as PCDATA or element types from an external namespace.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;complexType name="KeyValueType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}DSAKeyValue"/&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}RSAKeyValue"/&gt;
 *         &lt;any processContents='lax' namespace='##other'/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.DSAKeyValue
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.RSAKeyValue
 *
 * @author ahmety
 * date: Jun 10, 2009
 */
public class KeyValue extends BaseElement implements KeyInfoElement
{
    private KeyValueElement mValue;

    public KeyValue(Context aBaglam, PublicKey aPublicKey)
            throws XMLSignatureException
    {
        super(aBaglam);

        addLineBreak();
        if (aPublicKey instanceof DSAPublicKey){
            DSAKeyValue dsa = new DSAKeyValue(mContext,(DSAPublicKey)aPublicKey);
            this.mElement.appendChild(dsa.getElement());
            mValue = dsa;
        }
        else if (aPublicKey instanceof RSAPublicKey){
            RSAKeyValue rsa = new RSAKeyValue(mContext,(RSAPublicKey)aPublicKey);
            this.mElement.appendChild(rsa.getElement());
            mValue = rsa;
        } else if (aPublicKey instanceof ECDSAPublicKey){
            ECKeyValue ec = new ECKeyValue(mContext,(ECDSAPublicKey)aPublicKey);
            this.mElement.appendChild(ec.getElement());
            mValue = ec;
        } else {
            throw new XMLSignatureException("core.invalid.publickey", "KeyValue", aPublicKey);
        }
        addLineBreak();
    }

    /**
     *  Construct KeyValue from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public KeyValue(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        Element dsa = selectChildElement(NS_XMLDSIG, TAG_DSAKEYVALUE);
        if (dsa!=null){
            //mValueElement = dsa;
            mValue = new DSAKeyValue(dsa, aContext);
        }
        else {
            Element rsa = selectChildElement(NS_XMLDSIG, TAG_RSAKEYVALUE);
            if (rsa!=null){
                //mValueElement = rsa;
                mValue = new RSAKeyValue(rsa, mContext);
            }
            else {
                Element ecdsa = selectChildElement(NS_XMLDSIG_11, TAG_ECKEYVALUE);
                if (ecdsa!=null){
                    //mValueElement = rsa;
                    mValue = new ECKeyValue(ecdsa, mContext);
                } else {
                    throw new XMLSignatureException("unknown.keyValue");
                }
            }
        }
    }

    public KeyValueElement getValue()
    {
        return mValue;
    }

    // baseelement methods
    public String getLocalName()
    {
        return TAG_KEYVALUE;
    }
}
