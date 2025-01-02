
package org.etsi.uri.ts102204.v1_1.turktelekom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MobileUserType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MobileUserType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IdentityIssuer" type="{http://uri.etsi.org/TS102204/v1.1.2#}MeshMemberType" minOccurs="0"/&gt;
 *         &lt;element name="UserIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="HomeMSSP" type="{http://uri.etsi.org/TS102204/v1.1.2#}MeshMemberType" minOccurs="0"/&gt;
 *         &lt;element name="MSISDN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MobileUserType", propOrder = {
    "identityIssuer",
    "userIdentifier",
    "homeMSSP",
    "msisdn"
})
public class MobileUserType {

    @XmlElement(name = "IdentityIssuer")
    protected MeshMemberType identityIssuer;
    @XmlElement(name = "UserIdentifier")
    protected String userIdentifier;
    @XmlElement(name = "HomeMSSP")
    protected MeshMemberType homeMSSP;
    @XmlElement(name = "MSISDN")
    protected String msisdn;

    /**
     * Gets the value of the identityIssuer property.
     *
     * @return
     *     possible object is
     *     {@link MeshMemberType }
     *
     */
    public MeshMemberType getIdentityIssuer() {
        return identityIssuer;
    }

    /**
     * Sets the value of the identityIssuer property.
     *
     * @param value
     *     allowed object is
     *     {@link MeshMemberType }
     *
     */
    public void setIdentityIssuer(MeshMemberType value) {
        this.identityIssuer = value;
    }

    /**
     * Gets the value of the userIdentifier property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUserIdentifier() {
        return userIdentifier;
    }

    /**
     * Sets the value of the userIdentifier property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUserIdentifier(String value) {
        this.userIdentifier = value;
    }

    /**
     * Gets the value of the homeMSSP property.
     *
     * @return
     *     possible object is
     *     {@link MeshMemberType }
     *
     */
    public MeshMemberType getHomeMSSP() {
        return homeMSSP;
    }

    /**
     * Sets the value of the homeMSSP property.
     *
     * @param value
     *     allowed object is
     *     {@link MeshMemberType }
     *
     */
    public void setHomeMSSP(MeshMemberType value) {
        this.homeMSSP = value;
    }

    /**
     * Gets the value of the msisdn property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMSISDN() {
        return msisdn;
    }

    /**
     * Sets the value of the msisdn property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMSISDN(String value) {
        this.msisdn = value;
    }

}
