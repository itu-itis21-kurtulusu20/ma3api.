/**
 * KeyValueType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2000._09.xmldsig;

public class KeyValueType  implements java.io.Serializable, org.apache.axis.encoding.AnyContentType, org.apache.axis.encoding.MixedContentType {
    private org.w3.www._2000._09.xmldsig.DSAKeyValueType DSAKeyValue;

    private org.w3.www._2000._09.xmldsig.RSAKeyValueType RSAKeyValue;

    private org.apache.axis.message.MessageElement [] _any;

    public KeyValueType() {
    }

    public KeyValueType(
           org.w3.www._2000._09.xmldsig.DSAKeyValueType DSAKeyValue,
           org.w3.www._2000._09.xmldsig.RSAKeyValueType RSAKeyValue,
           org.apache.axis.message.MessageElement [] _any) {
           this.DSAKeyValue = DSAKeyValue;
           this.RSAKeyValue = RSAKeyValue;
           this._any = _any;
    }


    /**
     * Gets the DSAKeyValue value for this KeyValueType.
     * 
     * @return DSAKeyValue
     */
    public org.w3.www._2000._09.xmldsig.DSAKeyValueType getDSAKeyValue() {
        return DSAKeyValue;
    }


    /**
     * Sets the DSAKeyValue value for this KeyValueType.
     * 
     * @param DSAKeyValue
     */
    public void setDSAKeyValue(org.w3.www._2000._09.xmldsig.DSAKeyValueType DSAKeyValue) {
        this.DSAKeyValue = DSAKeyValue;
    }


    /**
     * Gets the RSAKeyValue value for this KeyValueType.
     * 
     * @return RSAKeyValue
     */
    public org.w3.www._2000._09.xmldsig.RSAKeyValueType getRSAKeyValue() {
        return RSAKeyValue;
    }


    /**
     * Sets the RSAKeyValue value for this KeyValueType.
     * 
     * @param RSAKeyValue
     */
    public void setRSAKeyValue(org.w3.www._2000._09.xmldsig.RSAKeyValueType RSAKeyValue) {
        this.RSAKeyValue = RSAKeyValue;
    }


    /**
     * Gets the _any value for this KeyValueType.
     * 
     * @return _any
     */
    public org.apache.axis.message.MessageElement [] get_any() {
        return _any;
    }


    /**
     * Sets the _any value for this KeyValueType.
     * 
     * @param _any
     */
    public void set_any(org.apache.axis.message.MessageElement [] _any) {
        this._any = _any;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof KeyValueType)) return false;
        KeyValueType other = (KeyValueType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.DSAKeyValue==null && other.getDSAKeyValue()==null) || 
             (this.DSAKeyValue!=null &&
              this.DSAKeyValue.equals(other.getDSAKeyValue()))) &&
            ((this.RSAKeyValue==null && other.getRSAKeyValue()==null) || 
             (this.RSAKeyValue!=null &&
              this.RSAKeyValue.equals(other.getRSAKeyValue()))) &&
            ((this._any==null && other.get_any()==null) || 
             (this._any!=null &&
              java.util.Arrays.equals(this._any, other.get_any())));
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
        if (getDSAKeyValue() != null) {
            _hashCode += getDSAKeyValue().hashCode();
        }
        if (getRSAKeyValue() != null) {
            _hashCode += getRSAKeyValue().hashCode();
        }
        if (get_any() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(get_any());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(get_any(), i);
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
        new org.apache.axis.description.TypeDesc(KeyValueType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "KeyValueType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("DSAKeyValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "DSAKeyValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "DSAKeyValueType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("RSAKeyValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "RSAKeyValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "RSAKeyValueType"));
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
