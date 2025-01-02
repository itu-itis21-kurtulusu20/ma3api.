/**
 * CipherDataType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2001._04.xmlenc;

public class CipherDataType  implements java.io.Serializable {
    private byte[] cipherValue;

    private org.w3.www._2001._04.xmlenc.CipherReferenceType cipherReference;

    public CipherDataType() {
    }

    public CipherDataType(
           byte[] cipherValue,
           org.w3.www._2001._04.xmlenc.CipherReferenceType cipherReference) {
           this.cipherValue = cipherValue;
           this.cipherReference = cipherReference;
    }


    /**
     * Gets the cipherValue value for this CipherDataType.
     * 
     * @return cipherValue
     */
    public byte[] getCipherValue() {
        return cipherValue;
    }


    /**
     * Sets the cipherValue value for this CipherDataType.
     * 
     * @param cipherValue
     */
    public void setCipherValue(byte[] cipherValue) {
        this.cipherValue = cipherValue;
    }


    /**
     * Gets the cipherReference value for this CipherDataType.
     * 
     * @return cipherReference
     */
    public org.w3.www._2001._04.xmlenc.CipherReferenceType getCipherReference() {
        return cipherReference;
    }


    /**
     * Sets the cipherReference value for this CipherDataType.
     * 
     * @param cipherReference
     */
    public void setCipherReference(org.w3.www._2001._04.xmlenc.CipherReferenceType cipherReference) {
        this.cipherReference = cipherReference;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CipherDataType)) return false;
        CipherDataType other = (CipherDataType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cipherValue==null && other.getCipherValue()==null) || 
             (this.cipherValue!=null &&
              java.util.Arrays.equals(this.cipherValue, other.getCipherValue()))) &&
            ((this.cipherReference==null && other.getCipherReference()==null) || 
             (this.cipherReference!=null &&
              this.cipherReference.equals(other.getCipherReference())));
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
        if (getCipherValue() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCipherValue());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCipherValue(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCipherReference() != null) {
            _hashCode += getCipherReference().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CipherDataType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "CipherDataType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cipherValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "CipherValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cipherReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "CipherReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "CipherReferenceType"));
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
