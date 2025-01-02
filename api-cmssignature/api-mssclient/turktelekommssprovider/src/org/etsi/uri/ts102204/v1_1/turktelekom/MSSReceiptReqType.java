
package org.etsi.uri.ts102204.v1_1.turktelekom;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.w3._2000._09.xmldsig_.SignatureType;


/**
 * <p>Java class for MSS_ReceiptReqType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MSS_ReceiptReqType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://uri.etsi.org/TS102204/v1.1.2#}MessageAbstractType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MobileUser" type="{http://uri.etsi.org/TS102204/v1.1.2#}MobileUserType"/&gt;
 *         &lt;element name="Status" type="{http://uri.etsi.org/TS102204/v1.1.2#}StatusType" minOccurs="0"/&gt;
 *         &lt;element name="Message" type="{http://uri.etsi.org/TS102204/v1.1.2#}DataType" minOccurs="0"/&gt;
 *         &lt;element name="SignedReceipt" type="{http://www.w3.org/2000/09/xmldsig#}SignatureType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="MSSP_TransID" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MSS_ReceiptReqType", propOrder = {
    "mobileUser",
    "status",
    "message",
    "signedReceipt"
})
public class MSSReceiptReqType
    extends MessageAbstractType
{

    @XmlElement(name = "MobileUser", required = true)
    protected MobileUserType mobileUser;
    @XmlElement(name = "Status")
    protected StatusType status;
    @XmlElement(name = "Message")
    protected DataType message;
    @XmlElement(name = "SignedReceipt")
    protected SignatureType signedReceipt;
    @XmlAttribute(name = "MSSP_TransID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String msspTransID;

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

    /**
     * Gets the value of the message property.
     *
     * @return
     *     possible object is
     *     {@link DataType }
     *
     */
    public DataType getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     *
     * @param value
     *     allowed object is
     *     {@link DataType }
     *
     */
    public void setMessage(DataType value) {
        this.message = value;
    }

    /**
     * Gets the value of the signedReceipt property.
     *
     * @return
     *     possible object is
     *     {@link SignatureType }
     *
     */
    public SignatureType getSignedReceipt() {
        return signedReceipt;
    }

    /**
     * Sets the value of the signedReceipt property.
     *
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *
     */
    public void setSignedReceipt(SignatureType value) {
        this.signedReceipt = value;
    }

    /**
     * Gets the value of the msspTransID property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMSSPTransID() {
        return msspTransID;
    }

    /**
     * Sets the value of the msspTransID property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMSSPTransID(String value) {
        this.msspTransID = value;
    }

}
