
package org.etsi.uri.ts102204.v1_1.turktelekom;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.w3._2001._04.xmlenc_.EncryptedType;
import org.w3c.dom.Element;


/**
 * <p>Java class for MSS_RegistrationReqType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MSS_RegistrationReqType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://uri.etsi.org/TS102204/v1.1.2#}MessageAbstractType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MobileUser" type="{http://uri.etsi.org/TS102204/v1.1.2#}MobileUserType"/&gt;
 *         &lt;element name="EncryptedData" type="{http://www.w3.org/2001/04/xmlenc#}EncryptedType" minOccurs="0"/&gt;
 *         &lt;element name="EncryptResponseBy" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
 *         &lt;element name="CertificateURI" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/&gt;
 *         &lt;element name="X509Certificate" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MSS_RegistrationReqType", propOrder = {
    "mobileUser",
    "encryptedData",
    "encryptResponseBy",
    "certificateURI",
    "x509Certificate",
    "any"
})
public class MSSRegistrationReqType
    extends MessageAbstractType
{

    @XmlElement(name = "MobileUser", required = true)
    protected MobileUserType mobileUser;
    @XmlElement(name = "EncryptedData")
    protected EncryptedType encryptedData;
    @XmlElement(name = "EncryptResponseBy")
    @XmlSchemaType(name = "anyURI")
    protected String encryptResponseBy;
    @XmlElement(name = "CertificateURI")
    @XmlSchemaType(name = "anyURI")
    protected String certificateURI;
    @XmlElement(name = "X509Certificate")
    protected byte[] x509Certificate;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the mobileUser property.
     *
     * @return
     *     possible object is
     *     {@link MobileUserType }
     *
     */
    public MobileUserType getMobileUser() {
        return mobileUser;
    }

    /**
     * Sets the value of the mobileUser property.
     *
     * @param value
     *     allowed object is
     *     {@link MobileUserType }
     *
     */
    public void setMobileUser(MobileUserType value) {
        this.mobileUser = value;
    }

    /**
     * Gets the value of the encryptedData property.
     *
     * @return
     *     possible object is
     *     {@link EncryptedType }
     *
     */
    public EncryptedType getEncryptedData() {
        return encryptedData;
    }

    /**
     * Sets the value of the encryptedData property.
     *
     * @param value
     *     allowed object is
     *     {@link EncryptedType }
     *
     */
    public void setEncryptedData(EncryptedType value) {
        this.encryptedData = value;
    }

    /**
     * Gets the value of the encryptResponseBy property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEncryptResponseBy() {
        return encryptResponseBy;
    }

    /**
     * Sets the value of the encryptResponseBy property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEncryptResponseBy(String value) {
        this.encryptResponseBy = value;
    }

    /**
     * Gets the value of the certificateURI property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCertificateURI() {
        return certificateURI;
    }

    /**
     * Sets the value of the certificateURI property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCertificateURI(String value) {
        this.certificateURI = value;
    }

    /**
     * Gets the value of the x509Certificate property.
     *
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getX509Certificate() {
        return x509Certificate;
    }

    /**
     * Sets the value of the x509Certificate property.
     *
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setX509Certificate(byte[] value) {
        this.x509Certificate = ((byte[]) value);
    }

    /**
     * Gets the value of the any property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * {@link Object }
     *
     *
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

}
