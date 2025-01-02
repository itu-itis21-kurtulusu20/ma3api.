
package org.etsi.uri.ts102204.v1_1.turktelekom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.w3._2000._09.xmldsig_.DigestMethodType;


/**
 * <p>Java class for DigestAlgAndValueType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DigestAlgAndValueType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DigestMethod" type="{http://www.w3.org/2000/09/xmldsig#}DigestMethodType" minOccurs="0"/&gt;
 *         &lt;element name="DigestValue" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DigestAlgAndValueType", propOrder = {
    "digestMethod",
    "digestValue"
})
public class DigestAlgAndValueType {

    @XmlElement(name = "DigestMethod")
    protected DigestMethodType digestMethod;
    @XmlElement(name = "DigestValue", required = true)
    protected byte[] digestValue;

    /**
     * Gets the value of the digestMethod property.
     *
     * @return
     *     possible object is
     *     {@link DigestMethodType }
     *
     */
    public DigestMethodType getDigestMethod() {
        return digestMethod;
    }

    /**
     * Sets the value of the digestMethod property.
     *
     * @param value
     *     allowed object is
     *     {@link DigestMethodType }
     *
     */
    public void setDigestMethod(DigestMethodType value) {
        this.digestMethod = value;
    }

    /**
     * Gets the value of the digestValue property.
     *
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getDigestValue() {
        return digestValue;
    }

    /**
     * Sets the value of the digestValue property.
     *
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDigestValue(byte[] value) {
        this.digestValue = ((byte[]) value);
    }

}
