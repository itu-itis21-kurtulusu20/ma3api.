/**
 * RSAKeyValueType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2000._09.xmldsig;

public class RSAKeyValueType  implements java.io.Serializable {
    private byte[] modulus;

    private byte[] exponent;

    public RSAKeyValueType() {
    }

    public RSAKeyValueType(
           byte[] modulus,
           byte[] exponent) {
           this.modulus = modulus;
           this.exponent = exponent;
    }


    /**
     * Gets the modulus value for this RSAKeyValueType.
     * 
     * @return modulus
     */
    public byte[] getModulus() {
        return modulus;
    }


    /**
     * Sets the modulus value for this RSAKeyValueType.
     * 
     * @param modulus
     */
    public void setModulus(byte[] modulus) {
        this.modulus = modulus;
    }


    /**
     * Gets the exponent value for this RSAKeyValueType.
     * 
     * @return exponent
     */
    public byte[] getExponent() {
        return exponent;
    }


    /**
     * Sets the exponent value for this RSAKeyValueType.
     * 
     * @param exponent
     */
    public void setExponent(byte[] exponent) {
        this.exponent = exponent;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RSAKeyValueType)) return false;
        RSAKeyValueType other = (RSAKeyValueType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.modulus==null && other.getModulus()==null) || 
             (this.modulus!=null &&
              java.util.Arrays.equals(this.modulus, other.getModulus()))) &&
            ((this.exponent==null && other.getExponent()==null) || 
             (this.exponent!=null &&
              java.util.Arrays.equals(this.exponent, other.getExponent())));
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
        if (getModulus() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getModulus());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getModulus(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getExponent() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getExponent());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getExponent(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RSAKeyValueType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "RSAKeyValueType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modulus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "Modulus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exponent");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "Exponent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
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
