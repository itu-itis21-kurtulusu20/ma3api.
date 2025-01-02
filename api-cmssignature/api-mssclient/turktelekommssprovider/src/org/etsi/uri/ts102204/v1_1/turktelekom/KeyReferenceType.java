
package org.etsi.uri.ts102204.v1_1.turktelekom;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.w3c.dom.Element;


/**
 * <p>Java class for KeyReferenceType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="KeyReferenceType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CertificateURL" type="{http://www.w3.org/2001/XMLSchema}anyURI" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="CertificateIssuerDN" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="HashOfUsersPublicKey" type="{http://uri.etsi.org/TS102204/v1.1.2#}DigestAlgAndValueType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="HashOfCAPublicKey" type="{http://uri.etsi.org/TS102204/v1.1.2#}DigestAlgAndValueType" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@XmlType(name = "KeyReferenceType", propOrder = {
    "certificateURL",
    "certificateIssuerDN",
    "hashOfUsersPublicKey",
    "hashOfCAPublicKey",
    "any"
})
public class KeyReferenceType {

    @XmlElement(name = "CertificateURL")
    @XmlSchemaType(name = "anyURI")
    protected List<String> certificateURL;
    @XmlElement(name = "CertificateIssuerDN")
    protected List<String> certificateIssuerDN;
    @XmlElement(name = "HashOfUsersPublicKey")
    protected List<DigestAlgAndValueType> hashOfUsersPublicKey;
    @XmlElement(name = "HashOfCAPublicKey")
    protected List<DigestAlgAndValueType> hashOfCAPublicKey;
    @XmlAnyElement(lax = true)
    protected Object any;

    /**
     * Gets the value of the certificateURL property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the certificateURL property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCertificateURL().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     *
     *
     */
    public List<String> getCertificateURL() {
        if (certificateURL == null) {
            certificateURL = new ArrayList<String>();
        }
        return this.certificateURL;
    }

    /**
     * Gets the value of the certificateIssuerDN property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the certificateIssuerDN property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCertificateIssuerDN().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     *
     *
     */
    public List<String> getCertificateIssuerDN() {
        if (certificateIssuerDN == null) {
            certificateIssuerDN = new ArrayList<String>();
        }
        return this.certificateIssuerDN;
    }

    /**
     * Gets the value of the hashOfUsersPublicKey property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hashOfUsersPublicKey property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHashOfUsersPublicKey().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DigestAlgAndValueType }
     *
     *
     */
    public List<DigestAlgAndValueType> getHashOfUsersPublicKey() {
        if (hashOfUsersPublicKey == null) {
            hashOfUsersPublicKey = new ArrayList<DigestAlgAndValueType>();
        }
        return this.hashOfUsersPublicKey;
    }

    /**
     * Gets the value of the hashOfCAPublicKey property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hashOfCAPublicKey property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHashOfCAPublicKey().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DigestAlgAndValueType }
     *
     *
     */
    public List<DigestAlgAndValueType> getHashOfCAPublicKey() {
        if (hashOfCAPublicKey == null) {
            hashOfCAPublicKey = new ArrayList<DigestAlgAndValueType>();
        }
        return this.hashOfCAPublicKey;
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
