
package org.etsi.uri.ts102204.v1_1.turktelekom;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for MSS_SignatureReqType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MSS_SignatureReqType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://uri.etsi.org/TS102204/v1.1.2#}MessageAbstractType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MobileUser" type="{http://uri.etsi.org/TS102204/v1.1.2#}MobileUserType"/&gt;
 *         &lt;element name="DataToBeSigned" type="{http://uri.etsi.org/TS102204/v1.1.2#}DataType"/&gt;
 *         &lt;element name="DataToBeDisplayed" type="{http://uri.etsi.org/TS102204/v1.1.2#}DataType" minOccurs="0"/&gt;
 *         &lt;element name="SignatureProfile" type="{http://uri.etsi.org/TS102204/v1.1.2#}mssURIType" minOccurs="0"/&gt;
 *         &lt;element name="AdditionalServices" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Service" type="{http://uri.etsi.org/TS102204/v1.1.2#}AdditionalServiceType" maxOccurs="unbounded"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="MSS_Format" type="{http://uri.etsi.org/TS102204/v1.1.2#}mssURIType" minOccurs="0"/&gt;
 *         &lt;element name="KeyReference" type="{http://uri.etsi.org/TS102204/v1.1.2#}KeyReferenceType" minOccurs="0"/&gt;
 *         &lt;element name="SignatureProfileComparison" type="{http://uri.etsi.org/TS102204/v1.1.2#}SignatureProfileComparisonType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="ValidityDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" /&gt;
 *       &lt;attribute name="TimeOut" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" /&gt;
 *       &lt;attribute name="MessagingMode" use="required" type="{http://uri.etsi.org/TS102204/v1.1.2#}MessagingModeType" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MSS_SignatureReqType", propOrder = {
    "mobileUser",
    "dataToBeSigned",
    "dataToBeDisplayed",
    "signatureProfile",
    "additionalServices",
    "mssFormat",
    "keyReference",
    "signatureProfileComparison"
})
public class MSSSignatureReqType
    extends MessageAbstractType
{

    @XmlElement(name = "MobileUser", required = true)
    protected MobileUserType mobileUser;
    @XmlElement(name = "DataToBeSigned", required = true)
    protected DataType dataToBeSigned;
    @XmlElement(name = "DataToBeDisplayed")
    protected DataType dataToBeDisplayed;
    @XmlElement(name = "SignatureProfile")
    protected MssURIType signatureProfile;
    @XmlElement(name = "AdditionalServices")
    protected MSSSignatureReqType.AdditionalServices additionalServices;
    @XmlElement(name = "MSS_Format")
    protected MssURIType mssFormat;
    @XmlElement(name = "KeyReference")
    protected KeyReferenceType keyReference;
    @XmlElement(name = "SignatureProfileComparison")
    protected SignatureProfileComparisonType signatureProfileComparison;
    @XmlAttribute(name = "ValidityDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar validityDate;
    @XmlAttribute(name = "TimeOut")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger timeOut;
    @XmlAttribute(name = "MessagingMode", required = true)
    protected MessagingModeType messagingMode;

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
     * Gets the value of the dataToBeSigned property.
     *
     * @return
     *     possible object is
     *     {@link DataType }
     *
     */
    public DataType getDataToBeSigned() {
        return dataToBeSigned;
    }

    /**
     * Sets the value of the dataToBeSigned property.
     *
     * @param value
     *     allowed object is
     *     {@link DataType }
     *
     */
    public void setDataToBeSigned(DataType value) {
        this.dataToBeSigned = value;
    }

    /**
     * Gets the value of the dataToBeDisplayed property.
     *
     * @return
     *     possible object is
     *     {@link DataType }
     *
     */
    public DataType getDataToBeDisplayed() {
        return dataToBeDisplayed;
    }

    /**
     * Sets the value of the dataToBeDisplayed property.
     *
     * @param value
     *     allowed object is
     *     {@link DataType }
     *
     */
    public void setDataToBeDisplayed(DataType value) {
        this.dataToBeDisplayed = value;
    }

    /**
     * Gets the value of the signatureProfile property.
     *
     * @return
     *     possible object is
     *     {@link MssURIType }
     *
     */
    public MssURIType getSignatureProfile() {
        return signatureProfile;
    }

    /**
     * Sets the value of the signatureProfile property.
     *
     * @param value
     *     allowed object is
     *     {@link MssURIType }
     *
     */
    public void setSignatureProfile(MssURIType value) {
        this.signatureProfile = value;
    }

    /**
     * Gets the value of the additionalServices property.
     *
     * @return
     *     possible object is
     *     {@link MSSSignatureReqType.AdditionalServices }
     *
     */
    public MSSSignatureReqType.AdditionalServices getAdditionalServices() {
        return additionalServices;
    }

    /**
     * Sets the value of the additionalServices property.
     *
     * @param value
     *     allowed object is
     *     {@link MSSSignatureReqType.AdditionalServices }
     *
     */
    public void setAdditionalServices(MSSSignatureReqType.AdditionalServices value) {
        this.additionalServices = value;
    }

    /**
     * Gets the value of the mssFormat property.
     *
     * @return
     *     possible object is
     *     {@link MssURIType }
     *
     */
    public MssURIType getMSSFormat() {
        return mssFormat;
    }

    /**
     * Sets the value of the mssFormat property.
     *
     * @param value
     *     allowed object is
     *     {@link MssURIType }
     *
     */
    public void setMSSFormat(MssURIType value) {
        this.mssFormat = value;
    }

    /**
     * Gets the value of the keyReference property.
     *
     * @return
     *     possible object is
     *     {@link KeyReferenceType }
     *
     */
    public KeyReferenceType getKeyReference() {
        return keyReference;
    }

    /**
     * Sets the value of the keyReference property.
     *
     * @param value
     *     allowed object is
     *     {@link KeyReferenceType }
     *
     */
    public void setKeyReference(KeyReferenceType value) {
        this.keyReference = value;
    }

    /**
     * Gets the value of the signatureProfileComparison property.
     *
     * @return
     *     possible object is
     *     {@link SignatureProfileComparisonType }
     *
     */
    public SignatureProfileComparisonType getSignatureProfileComparison() {
        return signatureProfileComparison;
    }

    /**
     * Sets the value of the signatureProfileComparison property.
     *
     * @param value
     *     allowed object is
     *     {@link SignatureProfileComparisonType }
     *
     */
    public void setSignatureProfileComparison(SignatureProfileComparisonType value) {
        this.signatureProfileComparison = value;
    }

    /**
     * Gets the value of the validityDate property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getValidityDate() {
        return validityDate;
    }

    /**
     * Sets the value of the validityDate property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setValidityDate(XMLGregorianCalendar value) {
        this.validityDate = value;
    }

    /**
     * Gets the value of the timeOut property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getTimeOut() {
        return timeOut;
    }

    /**
     * Sets the value of the timeOut property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setTimeOut(BigInteger value) {
        this.timeOut = value;
    }

    /**
     * Gets the value of the messagingMode property.
     *
     * @return
     *     possible object is
     *     {@link MessagingModeType }
     *
     */
    public MessagingModeType getMessagingMode() {
        return messagingMode;
    }

    /**
     * Sets the value of the messagingMode property.
     *
     * @param value
     *     allowed object is
     *     {@link MessagingModeType }
     *
     */
    public void setMessagingMode(MessagingModeType value) {
        this.messagingMode = value;
    }


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
     *         &lt;element name="Service" type="{http://uri.etsi.org/TS102204/v1.1.2#}AdditionalServiceType" maxOccurs="unbounded"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "service"
    })
    public static class AdditionalServices {

        @XmlElement(name = "Service", required = true)
        protected List<AdditionalServiceType> service;

        /**
         * Gets the value of the service property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the service property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getService().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AdditionalServiceType }
         *
         *
         */
        public List<AdditionalServiceType> getService() {
            if (service == null) {
                service = new ArrayList<AdditionalServiceType>();
            }
            return this.service;
        }

    }

}
