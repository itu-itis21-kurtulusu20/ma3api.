
package org.etsi.uri.ts102204.v1_1.turktelekom;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for MSS_HandshakeRespType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MSS_HandshakeRespType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://uri.etsi.org/TS102204/v1.1.2#}MessageAbstractType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SecureMethods"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="MSS_Signature" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *                 &lt;attribute name="MSS_Registration" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *                 &lt;attribute name="MSS_Notification" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *                 &lt;attribute name="MSS_ProfileQuery" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *                 &lt;attribute name="MSS_Receipt" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *                 &lt;attribute name="MSS_Status" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="MatchingMSSPCertificates"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Certificate" type="{http://www.w3.org/2001/XMLSchema}base64Binary" maxOccurs="unbounded" minOccurs="0"/v
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="MatchingAPCertificates"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Certificate" type="{http://www.w3.org/2001/XMLSchema}base64Binary" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="MatchingSigAlgList"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Algorithm" type="{http://uri.etsi.org/TS102204/v1.1.2#}mssURIType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
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
@XmlType(name = "MSS_HandshakeRespType", propOrder = {
    "secureMethods",
    "matchingMSSPCertificates",
    "matchingAPCertificates",
    "matchingSigAlgList"
})
public class MSSHandshakeRespType
    extends MessageAbstractType
{

    @XmlElement(name = "SecureMethods", required = true)
    protected MSSHandshakeRespType.SecureMethods secureMethods;
    @XmlElement(name = "MatchingMSSPCertificates", required = true)
    protected MSSHandshakeRespType.MatchingMSSPCertificates matchingMSSPCertificates;
    @XmlElement(name = "MatchingAPCertificates", required = true)
    protected MSSHandshakeRespType.MatchingAPCertificates matchingAPCertificates;
    @XmlElement(name = "MatchingSigAlgList", required = true)
    protected MSSHandshakeRespType.MatchingSigAlgList matchingSigAlgList;
    @XmlAttribute(name = "MSSP_TransID", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String msspTransID;

    /**
     * Gets the value of the secureMethods property.
     *
     * @return
     *     possible object is
     *     {@link MSSHandshakeRespType.SecureMethods }
     *
     */
    public MSSHandshakeRespType.SecureMethods getSecureMethods() {
        return secureMethods;
    }

    /**
     * Sets the value of the secureMethods property.
     *
     * @param value
     *     allowed object is
     *     {@link MSSHandshakeRespType.SecureMethods }
     *
     */
    public void setSecureMethods(MSSHandshakeRespType.SecureMethods value) {
        this.secureMethods = value;
    }

    /**
     * Gets the value of the matchingMSSPCertificates property.
     *
     * @return
     *     possible object is
     *     {@link MSSHandshakeRespType.MatchingMSSPCertificates }
     *
     */
    public MSSHandshakeRespType.MatchingMSSPCertificates getMatchingMSSPCertificates() {
        return matchingMSSPCertificates;
    }

    /**
     * Sets the value of the matchingMSSPCertificates property.
     *
     * @param value
     *     allowed object is
     *     {@link MSSHandshakeRespType.MatchingMSSPCertificates }
     *
     */
    public void setMatchingMSSPCertificates(MSSHandshakeRespType.MatchingMSSPCertificates value) {
        this.matchingMSSPCertificates = value;
    }

    /**
     * Gets the value of the matchingAPCertificates property.
     *
     * @return
     *     possible object is
     *     {@link MSSHandshakeRespType.MatchingAPCertificates }
     *
     */
    public MSSHandshakeRespType.MatchingAPCertificates getMatchingAPCertificates() {
        return matchingAPCertificates;
    }

    /**
     * Sets the value of the matchingAPCertificates property.
     *
     * @param value
     *     allowed object is
     *     {@link MSSHandshakeRespType.MatchingAPCertificates }
     *
     */
    public void setMatchingAPCertificates(MSSHandshakeRespType.MatchingAPCertificates value) {
        this.matchingAPCertificates = value;
    }

    /**
     * Gets the value of the matchingSigAlgList property.
     *
     * @return
     *     possible object is
     *     {@link MSSHandshakeRespType.MatchingSigAlgList }
     *
     */
    public MSSHandshakeRespType.MatchingSigAlgList getMatchingSigAlgList() {
        return matchingSigAlgList;
    }

    /**
     * Sets the value of the matchingSigAlgList property.
     *
     * @param value
     *     allowed object is
     *     {@link MSSHandshakeRespType.MatchingSigAlgList }
     *
     */
    public void setMatchingSigAlgList(MSSHandshakeRespType.MatchingSigAlgList value) {
        this.matchingSigAlgList = value;
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
     *         &lt;element name="Certificate" type="{http://www.w3.org/2001/XMLSchema}base64Binary" maxOccurs="unbounded" minOccurs="0"/&gt;
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
        "certificate"
    })
    public static class MatchingAPCertificates {

        @XmlElement(name = "Certificate")
        protected List<byte[]> certificate;

        /**
         * Gets the value of the certificate property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the certificate property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCertificate().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * byte[]
         *
         */
        public List<byte[]> getCertificate() {
            if (certificate == null) {
                certificate = new ArrayList<byte[]>();
            }
            return this.certificate;
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
     *         &lt;element name="Certificate" type="{http://www.w3.org/2001/XMLSchema}base64Binary" maxOccurs="unbounded" minOccurs="0"/&gt;
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
        "certificate"
    })
    public static class MatchingMSSPCertificates {

        @XmlElement(name = "Certificate")
        protected List<byte[]> certificate;

        /**
         * Gets the value of the certificate property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the certificate property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCertificate().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * byte[]
         *
         */
        public List<byte[]> getCertificate() {
            if (certificate == null) {
                certificate = new ArrayList<byte[]>();
            }
            return this.certificate;
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
     *         &lt;element name="Algorithm" type="{http://uri.etsi.org/TS102204/v1.1.2#}mssURIType" maxOccurs="unbounded" minOccurs="0"/&gt;
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
        "algorithm"
    })
    public static class MatchingSigAlgList {

        @XmlElement(name = "Algorithm")
        protected List<MssURIType> algorithm;

        /**
         * Gets the value of the algorithm property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the algorithm property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAlgorithm().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link MssURIType }
         *
         *
         */
        public List<MssURIType> getAlgorithm() {
            if (algorithm == null) {
                algorithm = new ArrayList<MssURIType>();
            }
            return this.algorithm;
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
     *       &lt;/sequence&gt;
     *       &lt;attribute name="MSS_Signature" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
     *       &lt;attribute name="MSS_Registration" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
     *       &lt;attribute name="MSS_Notification" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
     *       &lt;attribute name="MSS_ProfileQuery" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
     *       &lt;attribute name="MSS_Receipt" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
     *       &lt;attribute name="MSS_Status" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class SecureMethods {

        @XmlAttribute(name = "MSS_Signature", required = true)
        protected boolean mssSignature;
        @XmlAttribute(name = "MSS_Registration", required = true)
        protected boolean mssRegistration;
        @XmlAttribute(name = "MSS_Notification", required = true)
        protected boolean mssNotification;
        @XmlAttribute(name = "MSS_ProfileQuery", required = true)
        protected boolean mssProfileQuery;
        @XmlAttribute(name = "MSS_Receipt", required = true)
        protected boolean mssReceipt;
        @XmlAttribute(name = "MSS_Status", required = true)
        protected boolean mssStatus;

        /**
         * Gets the value of the mssSignature property.
         *
         */
        public boolean isMSSSignature() {
            return mssSignature;
        }

        /**
         * Sets the value of the mssSignature property.
         *
         */
        public void setMSSSignature(boolean value) {
            this.mssSignature = value;
        }

        /**
         * Gets the value of the mssRegistration property.
         *
         */
        public boolean isMSSRegistration() {
            return mssRegistration;
        }

        /**
         * Sets the value of the mssRegistration property.
         *
         */
        public void setMSSRegistration(boolean value) {
            this.mssRegistration = value;
        }

        /**
         * Gets the value of the mssNotification property.
         *
         */
        public boolean isMSSNotification() {
            return mssNotification;
        }

        /**
         * Sets the value of the mssNotification property.
         *
         */
        public void setMSSNotification(boolean value) {
            this.mssNotification = value;
        }

        /**
         * Gets the value of the mssProfileQuery property.
         *
         */
        public boolean isMSSProfileQuery() {
            return mssProfileQuery;
        }

        /**
         * Sets the value of the mssProfileQuery property.
         *
         */
        public void setMSSProfileQuery(boolean value) {
            this.mssProfileQuery = value;
        }

        /**
         * Gets the value of the mssReceipt property.
         *
         */
        public boolean isMSSReceipt() {
            return mssReceipt;
        }

        /**
         * Sets the value of the mssReceipt property.
         *
         */
        public void setMSSReceipt(boolean value) {
            this.mssReceipt = value;
        }

        /**
         * Gets the value of the mssStatus property.
         *
         */
        public boolean isMSSStatus() {
            return mssStatus;
        }

        /**
         * Sets the value of the mssStatus property.
         *
         */
        public void setMSSStatus(boolean value) {
            this.mssStatus = value;
        }

    }

}
