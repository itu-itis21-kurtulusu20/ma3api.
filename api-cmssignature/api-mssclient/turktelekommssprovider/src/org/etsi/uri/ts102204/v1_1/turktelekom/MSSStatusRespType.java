
package org.etsi.uri.ts102204.v1_1.turktelekom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MSS_StatusRespType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MSS_StatusRespType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://uri.etsi.org/TS102204/v1.1.2#}MessageAbstractType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MobileUser" type="{http://uri.etsi.org/TS102204/v1.1.2#}MobileUserType"/&gt;
 *         &lt;element name="MSS_Signature" type="{http://uri.etsi.org/TS102204/v1.1.2#}SignatureType" minOccurs="0"/&gt;
 *         &lt;element name="Status" type="{http://uri.etsi.org/TS102204/v1.1.2#}StatusType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MSS_StatusRespType", propOrder = {
    "mobileUser",
    "mssSignature",
    "status"
})
public class MSSStatusRespType
    extends MessageAbstractType
{

    @XmlElement(name = "MobileUser", required = true)
    protected MobileUserType mobileUser;
    @XmlElement(name = "MSS_Signature")
    protected SignatureType mssSignature;
    @XmlElement(name = "Status", required = true)
    protected StatusType status;

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
     * Gets the value of the mssSignature property.
     *
     * @return
     *     possible object is
     *     {@link SignatureType }
     *
     */
    public SignatureType getMSSSignature() {
        return mssSignature;
    }

    /**
     * Sets the value of the mssSignature property.
     *
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *
     */
    public void setMSSSignature(SignatureType value) {
        this.mssSignature = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return
     *     possible object is
     *     {@link StatusType }
     *
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value
     *     allowed object is
     *     {@link StatusType }
     *
     */
    public void setStatus(StatusType value) {
        this.status = value;
    }

}
