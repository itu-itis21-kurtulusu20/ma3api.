
package org.etsi.uri.ts102204.v1_1.turktelekom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.w3._2000._09.xmldsig_.SignatureType;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Signature" type="{http://www.w3.org/2000/09/xmldsig#}SignatureType" form="qualified"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute ref="{http://www.w3.org/2003/05/soap-envelope}role use="required""/&gt;
 *       &lt;attribute ref="{http://www.w3.org/2003/05/soap-envelope}mustUnderstand use="required""/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "signature"
})
@XmlRootElement(name = "MSS_MessageSignature")
public class MSSMessageSignature {

    @XmlElement(name = "Signature", namespace = "http://uri.etsi.org/TS102204/v1.1.2#", required = true)
    protected SignatureType signature;
    @XmlAttribute(namespace = "http://www.w3.org/2003/05/soap-envelope", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String role;
    @XmlAttribute(namespace = "http://www.w3.org/2003/05/soap-envelope", required = true)
    protected boolean mustUnderstand;

    /**
     * Gets the value of the signature property.
     *
     * @return
     *     possible object is
     *     {@link SignatureType }
     *
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     *
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
    }

    /**
     * Gets the value of the role property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRole(String value) {
        this.role = value;
    }

    /**
     * Gets the value of the mustUnderstand property.
     *
     */
    public boolean isMustUnderstand() {
        return mustUnderstand;
    }

    /**
     * Sets the value of the mustUnderstand property.
     *
     */
    public void setMustUnderstand(boolean value) {
        this.mustUnderstand = value;
    }

}
