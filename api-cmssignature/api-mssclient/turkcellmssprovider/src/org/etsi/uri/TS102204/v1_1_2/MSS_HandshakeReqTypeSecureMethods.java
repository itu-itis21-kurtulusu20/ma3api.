/**
 * MSS_HandshakeReqTypeSecureMethods.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MSS_HandshakeReqTypeSecureMethods  implements java.io.Serializable {
    private boolean MSS_Signature;  // attribute

    private boolean MSS_Registration;  // attribute

    private boolean MSS_Notification;  // attribute

    private boolean MSS_ProfileQuery;  // attribute

    private boolean MSS_Receipt;  // attribute

    private boolean MSS_Status;  // attribute

    public MSS_HandshakeReqTypeSecureMethods() {
    }

    public MSS_HandshakeReqTypeSecureMethods(
           boolean MSS_Signature,
           boolean MSS_Registration,
           boolean MSS_Notification,
           boolean MSS_ProfileQuery,
           boolean MSS_Receipt,
           boolean MSS_Status) {
           this.MSS_Signature = MSS_Signature;
           this.MSS_Registration = MSS_Registration;
           this.MSS_Notification = MSS_Notification;
           this.MSS_ProfileQuery = MSS_ProfileQuery;
           this.MSS_Receipt = MSS_Receipt;
           this.MSS_Status = MSS_Status;
    }


    /**
     * Gets the MSS_Signature value for this MSS_HandshakeReqTypeSecureMethods.
     * 
     * @return MSS_Signature
     */
    public boolean isMSS_Signature() {
        return MSS_Signature;
    }


    /**
     * Sets the MSS_Signature value for this MSS_HandshakeReqTypeSecureMethods.
     * 
     * @param MSS_Signature
     */
    public void setMSS_Signature(boolean MSS_Signature) {
        this.MSS_Signature = MSS_Signature;
    }


    /**
     * Gets the MSS_Registration value for this MSS_HandshakeReqTypeSecureMethods.
     * 
     * @return MSS_Registration
     */
    public boolean isMSS_Registration() {
        return MSS_Registration;
    }


    /**
     * Sets the MSS_Registration value for this MSS_HandshakeReqTypeSecureMethods.
     * 
     * @param MSS_Registration
     */
    public void setMSS_Registration(boolean MSS_Registration) {
        this.MSS_Registration = MSS_Registration;
    }


    /**
     * Gets the MSS_Notification value for this MSS_HandshakeReqTypeSecureMethods.
     * 
     * @return MSS_Notification
     */
    public boolean isMSS_Notification() {
        return MSS_Notification;
    }


    /**
     * Sets the MSS_Notification value for this MSS_HandshakeReqTypeSecureMethods.
     * 
     * @param MSS_Notification
     */
    public void setMSS_Notification(boolean MSS_Notification) {
        this.MSS_Notification = MSS_Notification;
    }


    /**
     * Gets the MSS_ProfileQuery value for this MSS_HandshakeReqTypeSecureMethods.
     * 
     * @return MSS_ProfileQuery
     */
    public boolean isMSS_ProfileQuery() {
        return MSS_ProfileQuery;
    }


    /**
     * Sets the MSS_ProfileQuery value for this MSS_HandshakeReqTypeSecureMethods.
     * 
     * @param MSS_ProfileQuery
     */
    public void setMSS_ProfileQuery(boolean MSS_ProfileQuery) {
        this.MSS_ProfileQuery = MSS_ProfileQuery;
    }


    /**
     * Gets the MSS_Receipt value for this MSS_HandshakeReqTypeSecureMethods.
     * 
     * @return MSS_Receipt
     */
    public boolean isMSS_Receipt() {
        return MSS_Receipt;
    }


    /**
     * Sets the MSS_Receipt value for this MSS_HandshakeReqTypeSecureMethods.
     * 
     * @param MSS_Receipt
     */
    public void setMSS_Receipt(boolean MSS_Receipt) {
        this.MSS_Receipt = MSS_Receipt;
    }


    /**
     * Gets the MSS_Status value for this MSS_HandshakeReqTypeSecureMethods.
     * 
     * @return MSS_Status
     */
    public boolean isMSS_Status() {
        return MSS_Status;
    }


    /**
     * Sets the MSS_Status value for this MSS_HandshakeReqTypeSecureMethods.
     * 
     * @param MSS_Status
     */
    public void setMSS_Status(boolean MSS_Status) {
        this.MSS_Status = MSS_Status;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MSS_HandshakeReqTypeSecureMethods)) return false;
        MSS_HandshakeReqTypeSecureMethods other = (MSS_HandshakeReqTypeSecureMethods) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.MSS_Signature == other.isMSS_Signature() &&
            this.MSS_Registration == other.isMSS_Registration() &&
            this.MSS_Notification == other.isMSS_Notification() &&
            this.MSS_ProfileQuery == other.isMSS_ProfileQuery() &&
            this.MSS_Receipt == other.isMSS_Receipt() &&
            this.MSS_Status == other.isMSS_Status();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += (isMSS_Signature() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isMSS_Registration() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isMSS_Notification() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isMSS_ProfileQuery() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isMSS_Receipt() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isMSS_Status() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MSS_HandshakeReqTypeSecureMethods.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MSS_HandshakeReqType>SecureMethods"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("MSS_Signature");
        attrField.setXmlName(new javax.xml.namespace.QName("", "MSS_Signature"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("MSS_Registration");
        attrField.setXmlName(new javax.xml.namespace.QName("", "MSS_Registration"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("MSS_Notification");
        attrField.setXmlName(new javax.xml.namespace.QName("", "MSS_Notification"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("MSS_ProfileQuery");
        attrField.setXmlName(new javax.xml.namespace.QName("", "MSS_ProfileQuery"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("MSS_Receipt");
        attrField.setXmlName(new javax.xml.namespace.QName("", "MSS_Receipt"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("MSS_Status");
        attrField.setXmlName(new javax.xml.namespace.QName("", "MSS_Status"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
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
