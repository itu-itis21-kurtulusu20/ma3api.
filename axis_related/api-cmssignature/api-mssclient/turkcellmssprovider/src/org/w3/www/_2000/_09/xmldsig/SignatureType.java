/**
 * SignatureType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2000._09.xmldsig;

public class SignatureType  implements java.io.Serializable {
    private org.w3.www._2000._09.xmldsig.SignedInfoType signedInfo;

    private org.w3.www._2000._09.xmldsig.SignatureValueType signatureValue;

    private org.w3.www._2000._09.xmldsig.KeyInfoType keyInfo;

    private org.w3.www._2000._09.xmldsig.ObjectType[] object;

    private org.apache.axis.types.Id id;  // attribute

    public SignatureType() {
    }

    public SignatureType(
           org.w3.www._2000._09.xmldsig.SignedInfoType signedInfo,
           org.w3.www._2000._09.xmldsig.SignatureValueType signatureValue,
           org.w3.www._2000._09.xmldsig.KeyInfoType keyInfo,
           org.w3.www._2000._09.xmldsig.ObjectType[] object,
           org.apache.axis.types.Id id) {
           this.signedInfo = signedInfo;
           this.signatureValue = signatureValue;
           this.keyInfo = keyInfo;
           this.object = object;
           this.id = id;
    }


    /**
     * Gets the signedInfo value for this SignatureType.
     * 
     * @return signedInfo
     */
    public org.w3.www._2000._09.xmldsig.SignedInfoType getSignedInfo() {
        return signedInfo;
    }


    /**
     * Sets the signedInfo value for this SignatureType.
     * 
     * @param signedInfo
     */
    public void setSignedInfo(org.w3.www._2000._09.xmldsig.SignedInfoType signedInfo) {
        this.signedInfo = signedInfo;
    }


    /**
     * Gets the signatureValue value for this SignatureType.
     * 
     * @return signatureValue
     */
    public org.w3.www._2000._09.xmldsig.SignatureValueType getSignatureValue() {
        return signatureValue;
    }


    /**
     * Sets the signatureValue value for this SignatureType.
     * 
     * @param signatureValue
     */
    public void setSignatureValue(org.w3.www._2000._09.xmldsig.SignatureValueType signatureValue) {
        this.signatureValue = signatureValue;
    }


    /**
     * Gets the keyInfo value for this SignatureType.
     * 
     * @return keyInfo
     */
    public org.w3.www._2000._09.xmldsig.KeyInfoType getKeyInfo() {
        return keyInfo;
    }


    /**
     * Sets the keyInfo value for this SignatureType.
     * 
     * @param keyInfo
     */
    public void setKeyInfo(org.w3.www._2000._09.xmldsig.KeyInfoType keyInfo) {
        this.keyInfo = keyInfo;
    }


    /**
     * Gets the object value for this SignatureType.
     * 
     * @return object
     */
    public org.w3.www._2000._09.xmldsig.ObjectType[] getObject() {
        return object;
    }


    /**
     * Sets the object value for this SignatureType.
     * 
     * @param object
     */
    public void setObject(org.w3.www._2000._09.xmldsig.ObjectType[] object) {
        this.object = object;
    }

    public org.w3.www._2000._09.xmldsig.ObjectType getObject(int i) {
        return this.object[i];
    }

    public void setObject(int i, org.w3.www._2000._09.xmldsig.ObjectType _value) {
        this.object[i] = _value;
    }


    /**
     * Gets the id value for this SignatureType.
     * 
     * @return id
     */
    public org.apache.axis.types.Id getId() {
        return id;
    }


    /**
     * Sets the id value for this SignatureType.
     * 
     * @param id
     */
    public void setId(org.apache.axis.types.Id id) {
        this.id = id;
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
            ((this.signedInfo==null && other.getSignedInfo()==null) || 
             (this.signedInfo!=null &&
              this.signedInfo.equals(other.getSignedInfo()))) &&
            ((this.signatureValue==null && other.getSignatureValue()==null) || 
             (this.signatureValue!=null &&
              this.signatureValue.equals(other.getSignatureValue()))) &&
            ((this.keyInfo==null && other.getKeyInfo()==null) || 
             (this.keyInfo!=null &&
              this.keyInfo.equals(other.getKeyInfo()))) &&
            ((this.object==null && other.getObject()==null) || 
             (this.object!=null &&
              java.util.Arrays.equals(this.object, other.getObject()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId())));
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
        if (getSignedInfo() != null) {
            _hashCode += getSignedInfo().hashCode();
        }
        if (getSignatureValue() != null) {
            _hashCode += getSignatureValue().hashCode();
        }
        if (getKeyInfo() != null) {
            _hashCode += getKeyInfo().hashCode();
        }
        if (getObject() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getObject());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getObject(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SignatureType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignatureType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("id");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Id"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "ID"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signedInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignedInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignedInfoType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signatureValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignatureValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignatureValueType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("keyInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "KeyInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "KeyInfoType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("object");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "Object"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "Object"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
