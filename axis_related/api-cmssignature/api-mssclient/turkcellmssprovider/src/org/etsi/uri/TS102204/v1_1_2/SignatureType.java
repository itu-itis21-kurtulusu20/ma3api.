/**
 * SignatureType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class SignatureType  implements java.io.Serializable, org.apache.axis.encoding.AnyContentType {
    private org.w3.www._2000._09.xmldsig.SignatureType XMLSignature;

    private byte[] base64Signature;

    private org.apache.axis.message.MessageElement [] _any;

    public SignatureType() {
    }

    public SignatureType(
           org.w3.www._2000._09.xmldsig.SignatureType XMLSignature,
           byte[] base64Signature,
           org.apache.axis.message.MessageElement [] _any) {
           this.XMLSignature = XMLSignature;
           this.base64Signature = base64Signature;
           this._any = _any;
    }


    /**
     * Gets the XMLSignature value for this SignatureType.
     * 
     * @return XMLSignature
     */
    public org.w3.www._2000._09.xmldsig.SignatureType getXMLSignature() {
        return XMLSignature;
    }


    /**
     * Sets the XMLSignature value for this SignatureType.
     * 
     * @param XMLSignature
     */
    public void setXMLSignature(org.w3.www._2000._09.xmldsig.SignatureType XMLSignature) {
        this.XMLSignature = XMLSignature;
    }


    /**
     * Gets the base64Signature value for this SignatureType.
     * 
     * @return base64Signature
     */
    public byte[] getBase64Signature() {
        return base64Signature;
    }


    /**
     * Sets the base64Signature value for this SignatureType.
     * 
     * @param base64Signature
     */
    public void setBase64Signature(byte[] base64Signature) {
        this.base64Signature = base64Signature;
    }


    /**
     * Gets the _any value for this SignatureType.
     * 
     * @return _any
     */
    public org.apache.axis.message.MessageElement [] get_any() {
        return _any;
    }


    /**
     * Sets the _any value for this SignatureType.
     * 
     * @param _any
     */
    public void set_any(org.apache.axis.message.MessageElement [] _any) {
        this._any = _any;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SignatureType)) return false;
        SignatureType other = (SignatureType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.XMLSignature==null && other.getXMLSignature()==null) || 
             (this.XMLSignature!=null &&
              this.XMLSignature.equals(other.getXMLSignature()))) &&
            ((this.base64Signature==null && other.getBase64Signature()==null) || 
             (this.base64Signature!=null &&
              java.util.Arrays.equals(this.base64Signature, other.getBase64Signature()))) &&
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
        if (getXMLSignature() != null) {
            _hashCode += getXMLSignature().hashCode();
        }
        if (getBase64Signature() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBase64Signature());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBase64Signature(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
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
        new org.apache.axis.description.TypeDesc(SignatureType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "SignatureType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("XMLSignature");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "XMLSignature"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignatureType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("base64Signature");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Base64Signature"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
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
