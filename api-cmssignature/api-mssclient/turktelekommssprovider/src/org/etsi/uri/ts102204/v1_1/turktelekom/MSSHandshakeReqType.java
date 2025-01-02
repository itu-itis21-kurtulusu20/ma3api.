
package org.etsi.uri.ts102204.v1_1.turktelekom;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MSS_HandshakeReqType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="MSS_HandshakeReqType"&gt;
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
 *         &lt;element name="Certificates"&gt;
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
 *         &lt;element name="RootCAs"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="DN" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="SignatureAlgList"&gt;
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
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MSS_HandshakeReqType", propOrder = {
    "secureMethods",
    "certificates",
    "rootCAs",
    "signatureAlgList"
})
public class MSSHandshakeReqType
    extends MessageAbstractType
{

    @XmlElement(name = "SecureMethods", required = true)
    protected MSSHandshakeReqType.SecureMethods secureMethods;
    @XmlElement(name = "Certificates", required = true)
    protected MSSHandshakeReqType.Certificates certificates;
    @XmlElement(name = "RootCAs", required = true)
    protected MSSHandshakeReqType.RootCAs rootCAs;
    @XmlElement(name = "SignatureAlgList", required = true)
    protected MSSHandshakeReqType.SignatureAlgList signatureAlgList;

    /**
     * Gets the value of the secureMethods property.
     *
     * @return
     *     possible object is
     *     {@link MSSHandshakeReqType.SecureMethods }
     *
     */
    public MSSHandshakeReqType.SecureMethods getSecureMethods() {
        return secureMethods;
    }

    /**
     * Sets the value of the secureMethods property.
     *
     * @param value
     *     allowed object is
     *     {@link MSSHandshakeReqType.SecureMethods }
     *
     */
    public void setSecureMethods(MSSHandshakeReqType.SecureMethods value) {
        this.secureMethods = value;
    }

    /**
     * Gets the value of the certificates property.
     *
     * @return
     *     possible object is
     *     {@link MSSHandshakeReqType.Certificates }
     *
     */
    public MSSHandshakeReqType.Certificates getCertificates() {
        return certificates;
    }

    /**
     * Sets the value of the certificates property.
     *
     * @param value
     *     allowed object is
     *     {@link MSSHandshakeReqType.Certificates }
     *
     */
    public void setCertificates(MSSHandshakeReqType.Certificates value) {
        this.certificates = value;
    }

    /**
     * Gets the value of the rootCAs property.
     *
     * @return
     *     possible object is
     *     {@link MSSHandshakeReqType.RootCAs }
     *
     */
    public MSSHandshakeReqType.RootCAs getRootCAs() {
        return rootCAs;
    }

    /**
     * Sets the value of the rootCAs property.
     *
     * @param value
     *     allowed object is
     *     {@link MSSHandshakeReqType.RootCAs }
     *
     */
    public void setRootCAs(MSSHandshakeReqType.RootCAs value) {
        this.rootCAs = value;
    }

    /**
     * Gets the value of the signatureAlgList property.
     *
     * @return
     *     possible object is
     *     {@link MSSHandshakeReqType.SignatureAlgList }
     *
     */
    public MSSHandshakeReqType.SignatureAlgList getSignatureAlgList() {
        return signatureAlgList;
    }

    /**
     * Sets the value of the signatureAlgList property.
     *
     * @param value
     *     allowed object is
     *     {@link MSSHandshakeReqType.SignatureAlgList }
     *
     */
    public void setSignatureAlgList(MSSHandshakeReqType.SignatureAlgList value) {
        this.signatureAlgList = value;
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
    public static class Certificates {

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
     *         &lt;element name="DN" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
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
        "dn"
    })
    public static class RootCAs {

        @XmlElement(name = "DN")
        protected List<String> dn;

        /**
         * Gets the value of the dn property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the dn property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDN().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         *
         *
         */
        public List<String> getDN() {
            if (dn == null) {
                dn = new ArrayList<String>();
            }
            return this.dn;
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
    public static class SignatureAlgList {

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

}
