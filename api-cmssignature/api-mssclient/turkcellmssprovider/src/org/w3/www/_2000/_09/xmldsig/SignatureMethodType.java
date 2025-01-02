/**
 * SignatureMethodType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2000._09.xmldsig;

public class SignatureMethodType  implements java.io.Serializable, org.apache.axis.encoding.AnyContentType, org.apache.axis.encoding.MixedContentType {
    private java.math.BigInteger HMACOutputLength;

    private org.apache.axis.message.MessageElement [] _any;

    private org.apache.axis.types.URI algorithm;  // attribute

    public SignatureMethodType() {
    }

    public SignatureMethodType(
           java.math.BigInteger HMACOutputLength,
           org.apache.axis.message.MessageElement [] _any,
           org.apache.axis.types.URI algorithm) {
           this.HMACOutputLength = HMACOutputLength;
           this._any = _any;
           this.algorithm = algorithm;
    }


    /**
     * Gets the HMACOutputLength value for this SignatureMethodType.
     * 
     * @return HMACOutputLength
     */
    public java.math.BigInteger getHMACOutputLength() {
        return HMACOutputLength;
    }


    /**
     * Sets the HMACOutputLength value for this SignatureMethodType.
     * 
     * @param HMACOutputLength
     */
    public void setHMACOutputLength(java.math.BigInteger HMACOutputLength) {
        this.HMACOutputLength = HMACOutputLength;
    }


    /**
     * Gets the _any value for this SignatureMethodType.
     * 
     * @return _any
     */
    public org.apache.axis.message.MessageElement [] get_any() {
        return _any;
    }


    /**
     * Sets the _any value for this SignatureMethodType.
     * 
     * @param _any
     */
    public void set_any(org.apache.axis.message.MessageElement [] _any) {
        this._any = _any;
    }


    /**
     * Gets the algorithm value for this SignatureMethodType.
     * 
     * @return algorithm
     */
    public org.apache.axis.types.URI getAlgorithm() {
        return algorithm;
    }


    /**
     * Sets the algorithm value for this SignatureMethodType.
     * 
     * @param algorithm
     */
    public void setAlgorithm(org.apache.axis.types.URI algorithm) {
        this.algorithm = algorithm;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SignatureMethodType)) return false;
        SignatureMethodType other = (SignatureMethodType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.HMACOutputLength==null && other.getHMACOutputLength()==null) || 
             (this.HMACOutputLength!=null &&
              this.HMACOutputLength.equals(other.getHMACOutputLength()))) &&
            ((this._any==null && other.get_any()==null) || 
             (this._any!=null &&
              java.util.Arrays.equals(this._any, other.get_any()))) &&
            ((this.algorithm==null && other.getAlgorithm()==null) || 
             (this.algorithm!=null &&
              this.algorithm.equals(other.getAlgorithm())));
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
        if (getHMACOutputLength() != null) {
            _hashCode += getHMACOutputLength().hashCode();
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
        if (getAlgorithm() != null) {
            _hashCode += getAlgorithm().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SignatureMethodType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignatureMethodType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("algorithm");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Algorithm"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("HMACOutputLength");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "HMACOutputLength"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
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
