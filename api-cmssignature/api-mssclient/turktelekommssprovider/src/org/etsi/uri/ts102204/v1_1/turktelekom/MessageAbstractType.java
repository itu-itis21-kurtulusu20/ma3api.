
package org.etsi.uri.ts102204.v1_1.turktelekom;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for MessageAbstractType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MessageAbstractType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AP_Info"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="AP_ID" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *                 &lt;attribute name="AP_TransID" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" /&gt;
 *                 &lt;attribute name="AP_PWD" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;attribute name="Instant" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" /&gt;
 *                 &lt;attribute name="AP_URL" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="MSSP_Info"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="MSSP_ID" type="{http://uri.etsi.org/TS102204/v1.1.2#}MeshMemberType"/&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="Instant" type="{http://www.w3.org/2001/XMLSchema}dateTime" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="MajorVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *       &lt;attribute name="MinorVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageAbstractType", propOrder = {
    "apInfo",
    "msspInfo"
})
@XmlSeeAlso({
    MSSStatusRespType.class,
    MSSRegistrationReqType.class,
    MSSStatusReqType.class,
    MSSProfileRespType.class,
    MSSReceiptReqType.class,
    MSSSignatureRespType.class,
    MSSHandshakeRespType.class,
    MSSHandshakeReqType.class,
    MSSProfileReqType.class,
    MSSSignatureReqType.class,
    MSSReceiptRespType.class,
    MSSRegistrationRespType.class
})
public abstract class MessageAbstractType {

    @XmlElement(name = "AP_Info", required = true)
    protected MessageAbstractType.APInfo apInfo;
    @XmlElement(name = "MSSP_Info", required = true)
    protected MessageAbstractType.MSSPInfo msspInfo;
    @XmlAttribute(name = "MajorVersion", required = true)
    protected BigInteger majorVersion;
    @XmlAttribute(name = "MinorVersion", required = true)
    protected BigInteger minorVersion;

    /**
     * Gets the value of the apInfo property.
     *
     * @return
     *     possible object is
     *     {@link MessageAbstractType.APInfo }
     *
     */
    public MessageAbstractType.APInfo getAPInfo() {
        return apInfo;
    }

    /**
     * Sets the value of the apInfo property.
     *
     * @param value
     *     allowed object is
     *     {@link MessageAbstractType.APInfo }
     *
     */
    public void setAPInfo(MessageAbstractType.APInfo value) {
        this.apInfo = value;
    }

    /**
     * Gets the value of the msspInfo property.
     *
     * @return
     *     possible object is
     *     {@link MessageAbstractType.MSSPInfo }
     *
     */
    public MessageAbstractType.MSSPInfo getMSSPInfo() {
        return msspInfo;
    }

    /**
     * Sets the value of the msspInfo property.
     *
     * @param value
     *     allowed object is
     *     {@link MessageAbstractType.MSSPInfo }
     *
     */
    public void setMSSPInfo(MessageAbstractType.MSSPInfo value) {
        this.msspInfo = value;
    }

    /**
     * Gets the value of the majorVersion property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getMajorVersion() {
        return majorVersion;
    }

    /**
     * Sets the value of the majorVersion property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setMajorVersion(BigInteger value) {
        this.majorVersion = value;
    }

    /**
     * Gets the value of the minorVersion property.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getMinorVersion() {
        return minorVersion;
    }

    /**
     * Sets the value of the minorVersion property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setMinorVersion(BigInteger value) {
        this.minorVersion = value;
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
     *       &lt;/sequence&gt;
     *       &lt;attribute name="AP_ID" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
     *       &lt;attribute name="AP_TransID" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" /&gt;
     *       &lt;attribute name="AP_PWD" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *       &lt;attribute name="Instant" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" /&gt;
     *       &lt;attribute name="AP_URL" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class APInfo {

        @XmlAttribute(name = "AP_ID", required = true)
        @XmlSchemaType(name = "anyURI")
        protected String apid;
        @XmlAttribute(name = "AP_TransID", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "NCName")
        protected String apTransID;
        @XmlAttribute(name = "AP_PWD", required = true)
        protected String appwd;
        @XmlAttribute(name = "Instant", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar instant;
        @XmlAttribute(name = "AP_URL")
        @XmlSchemaType(name = "anyURI")
        protected String apurl;

        /**
         * Gets the value of the apid property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getAPID() {
            return apid;
        }

        /**
         * Sets the value of the apid property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setAPID(String value) {
            this.apid = value;
        }

        /**
         * Gets the value of the apTransID property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getAPTransID() {
            return apTransID;
        }

        /**
         * Sets the value of the apTransID property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setAPTransID(String value) {
            this.apTransID = value;
        }

        /**
         * Gets the value of the appwd property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getAPPWD() {
            return appwd;
        }

        /**
         * Sets the value of the appwd property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setAPPWD(String value) {
            this.appwd = value;
        }

        /**
         * Gets the value of the instant property.
         *
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public XMLGregorianCalendar getInstant() {
            return instant;
        }

        /**
         * Sets the value of the instant property.
         *
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public void setInstant(XMLGregorianCalendar value) {
            this.instant = value;
        }

        /**
         * Gets the value of the apurl property.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getAPURL() {
            return apurl;
        }

        /**
         * Sets the value of the apurl property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setAPURL(String value) {
            this.apurl = value;
        }

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
     *         &lt;element name="MSSP_ID" type="{http://uri.etsi.org/TS102204/v1.1.2#}MeshMemberType"/&gt;
     *       &lt;/sequence&gt;
     *       &lt;attribute name="Instant" type="{http://www.w3.org/2001/XMLSchema}dateTime" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "msspid"
    })
    public static class MSSPInfo {

        @XmlElement(name = "MSSP_ID", required = true)
        protected MeshMemberType msspid;
        @XmlAttribute(name = "Instant")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar instant;

        /**
         * Gets the value of the msspid property.
         *
         * @return
         *     possible object is
         *     {@link MeshMemberType }
         *
         */
        public MeshMemberType getMSSPID() {
            return msspid;
        }

        /**
         * Sets the value of the msspid property.
         *
         * @param value
         *     allowed object is
         *     {@link MeshMemberType }
         *
         */
        public void setMSSPID(MeshMemberType value) {
            this.msspid = value;
        }

        /**
         * Gets the value of the instant property.
         *
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public XMLGregorianCalendar getInstant() {
            return instant;
        }

        /**
         * Sets the value of the instant property.
         *
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public void setInstant(XMLGregorianCalendar value) {
            this.instant = value;
        }

    }

}
