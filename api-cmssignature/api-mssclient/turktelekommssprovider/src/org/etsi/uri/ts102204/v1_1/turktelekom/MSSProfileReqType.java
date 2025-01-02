
package org.etsi.uri.ts102204.v1_1.turktelekom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MSS_ProfileReqType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MSS_ProfileReqType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://uri.etsi.org/TS102204/v1.1.2#}MessageAbstractType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MobileUser" type="{http://uri.etsi.org/TS102204/v1.1.2#}MobileUserType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MSS_ProfileReqType", propOrder = {
    "mobileUser"
})
public class MSSProfileReqType
    extends MessageAbstractType
{

    @XmlElement(name = "MobileUser", required = true)
    protected MobileUserType mobileUser;

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

}
