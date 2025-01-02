
package org.etsi.uri.ts102204.v1_1.turktelekom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * <p>Java class for SignatureType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SignatureType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="XMLSignature" type="{http://www.w3.org/2000/09/xmldsig#}SignatureType" minOccurs="0"/&gt;
 *         &lt;element name="Base64Signature" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *         &lt;any processContents='lax' namespace='##other'/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SignatureType", propOrder = {
    "xmlSignature",
    "base64Signature",
    "any"
})
public class SignatureType {

    @XmlElement(name = "XMLSignature")
    protected org.w3._2000._09.xmldsig_.SignatureType xmlSignature;
    @XmlElement(name = "Base64Signature")
    protected byte[] base64Signature;
    @XmlAnyElement(lax = true)
    protected Object any;

    /**
     * Gets the value of the xmlSignature property.
     *
     * @return
     *     possible object is
     *     {@link org.w3._2000._09.xmldsig_.SignatureType }
     *
     */
    public org.w3._2000._09.xmldsig_.SignatureType getXMLSignature() {
        return xmlSignature;
    }

    /**
     * Sets the value of the xmlSignature property.
     *
     * @param value
     *     allowed object is
     *     {@link org.w3._2000._09.xmldsig_.SignatureType }
     *
     */
    public void setXMLSignature(org.w3._2000._09.xmldsig_.SignatureType value) {
        this.xmlSignature = value;
    }

    /**
     * Gets the value of the base64Signature property.
     *
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBase64Signature() {
        return base64Signature;
    }

    /**
     * Sets the value of the base64Signature property.
     *
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBase64Signature(byte[] value) {
        this.base64Signature = ((byte[]) value);
    }

    /**
     * Gets the value of the any property.
     *
     * @return
     *     possible object is
     *     {@link Element }
     *     {@link Object }
     *
     */
    public Object getAny() {
        return any;
    }

    /**
     * Sets the value of the any property.
     *
     * @param value
     *     allowed object is
     *     {@link Element }
     *     {@link Object }
     *
     */
    public void setAny(Object value) {
        this.any = value;
    }

}
