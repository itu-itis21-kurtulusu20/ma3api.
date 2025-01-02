/**
 * DigestAlgAndValueType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class DigestAlgAndValueType  implements java.io.Serializable {
    private org.w3.www._2000._09.xmldsig.DigestMethodType digestMethod;

    private byte[] digestValue;

    public DigestAlgAndValueType() {
    }

    public DigestAlgAndValueType(
           org.w3.www._2000._09.xmldsig.DigestMethodType digestMethod,
           byte[] digestValue) {
           this.digestMethod = digestMethod;
           this.digestValue = digestValue;
    }


    /**
     * Gets the digestMethod value for this DigestAlgAndValueType.
     * 
     * @return digestMethod
     */
    public org.w3.www._2000._09.xmldsig.DigestMethodType getDigestMethod() {
        return digestMethod;
    }


    /**
     * Sets the digestMethod value for this DigestAlgAndValueType.
     * 
     * @param digestMethod
     */
    public void setDigestMethod(org.w3.www._2000._09.xmldsig.DigestMethodType digestMethod) {
        this.digestMethod = digestMethod;
    }


    /**
     * Gets the digestValue value for this DigestAlgAndValueType.
     * 
     * @return digestValue
     */
    public byte[] getDigestValue() {
        return digestValue;
    }


    /**
     * Sets the digestValue value for this DigestAlgAndValueType.
     * 
     * @param digestValue
     */
    public void setDigestValue(byte[] digestValue) {
        this.digestValue = digestValue;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DigestAlgAndValueType)) return false;
        DigestAlgAndValueType other = (DigestAlgAndValueType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.digestMethod==null && other.getDigestMethod()==null) || 
             (this.digestMethod!=null &&
              this.digestMethod.equals(other.getDigestMethod()))) &&
            ((this.digestValue==null && other.getDigestValue()==null) || 
             (this.digestValue!=null &&
              java.util.Arrays.equals(this.digestValue, other.getDigestValue())));
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
        if (getDigestMethod() != null) {
            _hashCode += getDigestMethod().hashCode();
        }
        if (getDigestValue() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDigestValue());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDigestValue(), i);
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
        new org.apache.axis.description.TypeDesc(DigestAlgAndValueType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "DigestAlgAndValueType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("digestMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "DigestMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "DigestMethodType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("digestValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "DigestValue"));
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
