/**
 * MSS_ReceiptReqType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MSS_ReceiptReqType  extends org.etsi.uri.TS102204.v1_1_2.MessageAbstractType  implements java.io.Serializable {
    private org.etsi.uri.TS102204.v1_1_2.MobileUserType mobileUser;

    private org.etsi.uri.TS102204.v1_1_2.StatusType status;

    private org.etsi.uri.TS102204.v1_1_2.DataType message;

    private org.w3.www._2000._09.xmldsig.SignatureType signedReceipt;

    private org.apache.axis.types.NCName MSSP_TransID;  // attribute

    public MSS_ReceiptReqType() {
    }

    public MSS_ReceiptReqType(
           java.math.BigInteger majorVersion,
           java.math.BigInteger minorVersion,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeAP_Info AP_Info,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeMSSP_Info MSSP_Info,
           org.apache.axis.types.NCName MSSP_TransID,
           org.etsi.uri.TS102204.v1_1_2.MobileUserType mobileUser,
           org.etsi.uri.TS102204.v1_1_2.StatusType status,
           org.etsi.uri.TS102204.v1_1_2.DataType message,
           org.w3.www._2000._09.xmldsig.SignatureType signedReceipt) {
        super(
            majorVersion,
            minorVersion,
            AP_Info,
            MSSP_Info);
        this.MSSP_TransID = MSSP_TransID;
        this.mobileUser = mobileUser;
        this.status = status;
        this.message = message;
        this.signedReceipt = signedReceipt;
    }


    /**
     * Gets the mobileUser value for this MSS_ReceiptReqType.
     * 
     * @return mobileUser
     */
    public org.etsi.uri.TS102204.v1_1_2.MobileUserType getMobileUser() {
        return mobileUser;
    }


    /**
     * Sets the mobileUser value for this MSS_ReceiptReqType.
     * 
     * @param mobileUser
     */
    public void setMobileUser(org.etsi.uri.TS102204.v1_1_2.MobileUserType mobileUser) {
        this.mobileUser = mobileUser;
    }


    /**
     * Gets the status value for this MSS_ReceiptReqType.
     * 
     * @return status
     */
    public org.etsi.uri.TS102204.v1_1_2.StatusType getStatus() {
        return status;
    }


    /**
     * Sets the status value for this MSS_ReceiptReqType.
     * 
     * @param status
     */
    public void setStatus(org.etsi.uri.TS102204.v1_1_2.StatusType status) {
        this.status = status;
    }


    /**
     * Gets the message value for this MSS_ReceiptReqType.
     * 
     * @return message
     */
    public org.etsi.uri.TS102204.v1_1_2.DataType getMessage() {
        return message;
    }


    /**
     * Sets the message value for this MSS_ReceiptReqType.
     * 
     * @param message
     */
    public void setMessage(org.etsi.uri.TS102204.v1_1_2.DataType message) {
        this.message = message;
    }


    /**
     * Gets the signedReceipt value for this MSS_ReceiptReqType.
     * 
     * @return signedReceipt
     */
    public org.w3.www._2000._09.xmldsig.SignatureType getSignedReceipt() {
        return signedReceipt;
    }


    /**
     * Sets the signedReceipt value for this MSS_ReceiptReqType.
     * 
     * @param signedReceipt
     */
    public void setSignedReceipt(org.w3.www._2000._09.xmldsig.SignatureType signedReceipt) {
        this.signedReceipt = signedReceipt;
    }


    /**
     * Gets the MSSP_TransID value for this MSS_ReceiptReqType.
     * 
     * @return MSSP_TransID
     */
    public org.apache.axis.types.NCName getMSSP_TransID() {
        return MSSP_TransID;
    }


    /**
     * Sets the MSSP_TransID value for this MSS_ReceiptReqType.
     * 
     * @param MSSP_TransID
     */
    public void setMSSP_TransID(org.apache.axis.types.NCName MSSP_TransID) {
        this.MSSP_TransID = MSSP_TransID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MSS_ReceiptReqType)) return false;
        MSS_ReceiptReqType other = (MSS_ReceiptReqType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.mobileUser==null && other.getMobileUser()==null) || 
             (this.mobileUser!=null &&
              this.mobileUser.equals(other.getMobileUser()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.message==null && other.getMessage()==null) || 
             (this.message!=null &&
              this.message.equals(other.getMessage()))) &&
            ((this.signedReceipt==null && other.getSignedReceipt()==null) || 
             (this.signedReceipt!=null &&
              this.signedReceipt.equals(other.getSignedReceipt()))) &&
            ((this.MSSP_TransID==null && other.getMSSP_TransID()==null) || 
             (this.MSSP_TransID!=null &&
              this.MSSP_TransID.equals(other.getMSSP_TransID())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getMobileUser() != null) {
            _hashCode += getMobileUser().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getMessage() != null) {
            _hashCode += getMessage().hashCode();
        }
        if (getSignedReceipt() != null) {
            _hashCode += getSignedReceipt().hashCode();
        }
        if (getMSSP_TransID() != null) {
            _hashCode += getMSSP_TransID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MSS_ReceiptReqType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_ReceiptReqType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("MSSP_TransID");
        attrField.setXmlName(new javax.xml.namespace.QName("", "MSSP_TransID"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mobileUser");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MobileUser"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MobileUserType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "StatusType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("message");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "DataType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signedReceipt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "SignedReceipt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignatureType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
