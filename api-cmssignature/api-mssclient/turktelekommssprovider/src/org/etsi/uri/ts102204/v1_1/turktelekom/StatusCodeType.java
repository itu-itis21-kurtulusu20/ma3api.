
package org.etsi.uri.ts102204.v1_1.turktelekom;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StatusCodeType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="StatusCodeType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="StatusCode" type="{http://uri.etsi.org/TS102204/v1.1.2#}StatusCodeType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="Value" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StatusCodeType", propOrder = {
    "statusCode"
})
public class StatusCodeType {

    @XmlElement(name = "StatusCode")
    protected StatusCodeType statusCode;
    @XmlAttribute(name = "Value", required = true)
    protected BigInteger value;

    /**
     * Gets the value of the statusCode property.
     *
     * @return
     *     possible object is
     *     {@link StatusCodeType }
     *
     */
    public StatusCodeType getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     *
     * @param value
     *     allowed object is
     *     {@link StatusCodeType }
     *
     */
    public void setStatusCode(StatusCodeType value) {
        this.statusCode = value;
    }

    /**
     * Gets the value of the value property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setValue(BigInteger value) {
        this.value = value;
    }

}
