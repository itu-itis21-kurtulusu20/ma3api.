/**
 * X509IssuerSerialType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2000._09.xmldsig;

public class X509IssuerSerialType  implements java.io.Serializable {
    private java.lang.String x509IssuerName;

    private java.math.BigInteger x509SerialNumber;

    public X509IssuerSerialType() {
    }

    public X509IssuerSerialType(
           java.lang.String x509IssuerName,
           java.math.BigInteger x509SerialNumber) {
           this.x509IssuerName = x509IssuerName;
           this.x509SerialNumber = x509SerialNumber;
    }


    /**
     * Gets the x509IssuerName value for this X509IssuerSerialType.
     * 
     * @return x509IssuerName
     */
    public java.lang.String getX509IssuerName() {
        return x509IssuerName;
    }


    /**
     * Sets the x509IssuerName value for this X509IssuerSerialType.
     * 
     * @param x509IssuerName
     */
    public void setX509IssuerName(java.lang.String x509IssuerName) {
        this.x509IssuerName = x509IssuerName;
    }


    /**
     * Gets the x509SerialNumber value for this X509IssuerSerialType.
     * 
     * @return x509SerialNumber
     */
    public java.math.BigInteger getX509SerialNumber() {
        return x509SerialNumber;
    }


    /**
     * Sets the x509SerialNumber value for this X509IssuerSerialType.
     * 
     * @param x509SerialNumber
     */
    public void setX509SerialNumber(java.math.BigInteger x509SerialNumber) {
        this.x509SerialNumber = x509SerialNumber;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof X509IssuerSerialType)) return false;
        X509IssuerSerialType other = (X509IssuerSerialType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.x509IssuerName==null && other.getX509IssuerName()==null) || 
             (this.x509IssuerName!=null &&
              this.x509IssuerName.equals(other.getX509IssuerName()))) &&
            ((this.x509SerialNumber==null && other.getX509SerialNumber()==null) || 
             (this.x509SerialNumber!=null &&
              this.x509SerialNumber.equals(other.getX509SerialNumber())));
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
        if (getX509IssuerName() != null) {
            _hashCode += getX509IssuerName().hashCode();
        }
        if (getX509SerialNumber() != null) {
            _hashCode += getX509SerialNumber().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(X509IssuerSerialType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "X509IssuerSerialType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("x509IssuerName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "X509IssuerName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("x509SerialNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "X509SerialNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
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
