/**
 * EncryptedType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2001._04.xmlenc;

public abstract class EncryptedType  implements java.io.Serializable {
    private org.w3.www._2001._04.xmlenc.EncryptionMethodType encryptionMethod;

    private org.w3.www._2000._09.xmldsig.KeyInfoType keyInfo;

    private org.w3.www._2001._04.xmlenc.CipherDataType cipherData;

    private org.w3.www._2001._04.xmlenc.EncryptionPropertyType[] encryptionProperties;

    private org.apache.axis.types.Id id;  // attribute

    private org.apache.axis.types.URI type;  // attribute

    private java.lang.String mimeType;  // attribute

    private org.apache.axis.types.URI encoding;  // attribute

    public EncryptedType() {
    }

    public EncryptedType(
    		org.apache.axis.types.Id id,
            org.apache.axis.types.URI type,
            java.lang.String mimeType,
            org.apache.axis.types.URI encoding,
            org.w3.www._2001._04.xmlenc.EncryptionMethodType encryptionMethod,
            org.w3.www._2000._09.xmldsig.KeyInfoType keyInfo,
            org.w3.www._2001._04.xmlenc.CipherDataType cipherData,
            org.w3.www._2001._04.xmlenc.EncryptionPropertyType[] encryptionProperties) {
           this.encryptionMethod = encryptionMethod;
           this.keyInfo = keyInfo;
           this.cipherData = cipherData;
           this.encryptionProperties = encryptionProperties;
           this.id = id;
           this.type = type;
           this.mimeType = mimeType;
           this.encoding = encoding;
    }


    /**
     * Gets the encryptionMethod value for this EncryptedType.
     * 
     * @return encryptionMethod
     */
    public org.w3.www._2001._04.xmlenc.EncryptionMethodType getEncryptionMethod() {
        return encryptionMethod;
    }


    /**
     * Sets the encryptionMethod value for this EncryptedType.
     * 
     * @param encryptionMethod
     */
    public void setEncryptionMethod(org.w3.www._2001._04.xmlenc.EncryptionMethodType encryptionMethod) {
        this.encryptionMethod = encryptionMethod;
    }


    /**
     * Gets the keyInfo value for this EncryptedType.
     * 
     * @return keyInfo
     */
    public org.w3.www._2000._09.xmldsig.KeyInfoType getKeyInfo() {
        return keyInfo;
    }


    /**
     * Sets the keyInfo value for this EncryptedType.
     * 
     * @param keyInfo
     */
    public void setKeyInfo(org.w3.www._2000._09.xmldsig.KeyInfoType keyInfo) {
        this.keyInfo = keyInfo;
    }


    /**
     * Gets the cipherData value for this EncryptedType.
     * 
     * @return cipherData
     */
    public org.w3.www._2001._04.xmlenc.CipherDataType getCipherData() {
        return cipherData;
    }


    /**
     * Sets the cipherData value for this EncryptedType.
     * 
     * @param cipherData
     */
    public void setCipherData(org.w3.www._2001._04.xmlenc.CipherDataType cipherData) {
        this.cipherData = cipherData;
    }


    /**
     * Gets the encryptionProperties value for this EncryptedType.
     * 
     * @return encryptionProperties
     */
    public org.w3.www._2001._04.xmlenc.EncryptionPropertyType[] getEncryptionProperties() {
        return encryptionProperties;
    }


    /**
     * Sets the encryptionProperties value for this EncryptedType.
     * 
     * @param encryptionProperties
     */
    public void setEncryptionProperties(org.w3.www._2001._04.xmlenc.EncryptionPropertyType[] encryptionProperties) {
        this.encryptionProperties = encryptionProperties;
    }


    /**
     * Gets the id value for this EncryptedType.
     * 
     * @return id
     */
    public org.apache.axis.types.Id getId() {
        return id;
    }


    /**
     * Sets the id value for this EncryptedType.
     * 
     * @param id
     */
    public void setId(org.apache.axis.types.Id id) {
        this.id = id;
    }


    /**
     * Gets the type value for this EncryptedType.
     * 
     * @return type
     */
    public org.apache.axis.types.URI getType() {
        return type;
    }


    /**
     * Sets the type value for this EncryptedType.
     * 
     * @param type
     */
    public void setType(org.apache.axis.types.URI type) {
        this.type = type;
    }


    /**
     * Gets the mimeType value for this EncryptedType.
     * 
     * @return mimeType
     */
    public java.lang.String getMimeType() {
        return mimeType;
    }


    /**
     * Sets the mimeType value for this EncryptedType.
     * 
     * @param mimeType
     */
    public void setMimeType(java.lang.String mimeType) {
        this.mimeType = mimeType;
    }


    /**
     * Gets the encoding value for this EncryptedType.
     * 
     * @return encoding
     */
    public org.apache.axis.types.URI getEncoding() {
        return encoding;
    }


    /**
     * Sets the encoding value for this EncryptedType.
     * 
     * @param encoding
     */
    public void setEncoding(org.apache.axis.types.URI encoding) {
        this.encoding = encoding;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EncryptedType)) return false;
        EncryptedType other = (EncryptedType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.encryptionMethod==null && other.getEncryptionMethod()==null) || 
             (this.encryptionMethod!=null &&
              this.encryptionMethod.equals(other.getEncryptionMethod()))) &&
            ((this.keyInfo==null && other.getKeyInfo()==null) || 
             (this.keyInfo!=null &&
              this.keyInfo.equals(other.getKeyInfo()))) &&
            ((this.cipherData==null && other.getCipherData()==null) || 
             (this.cipherData!=null &&
              this.cipherData.equals(other.getCipherData()))) &&
            ((this.encryptionProperties==null && other.getEncryptionProperties()==null) || 
             (this.encryptionProperties!=null &&
              java.util.Arrays.equals(this.encryptionProperties, other.getEncryptionProperties()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.mimeType==null && other.getMimeType()==null) || 
             (this.mimeType!=null &&
              this.mimeType.equals(other.getMimeType()))) &&
            ((this.encoding==null && other.getEncoding()==null) || 
             (this.encoding!=null &&
              this.encoding.equals(other.getEncoding())));
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
        if (getEncryptionMethod() != null) {
            _hashCode += getEncryptionMethod().hashCode();
        }
        if (getKeyInfo() != null) {
            _hashCode += getKeyInfo().hashCode();
        }
        if (getCipherData() != null) {
            _hashCode += getCipherData().hashCode();
        }
        if (getEncryptionProperties() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEncryptionProperties());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEncryptionProperties(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getMimeType() != null) {
            _hashCode += getMimeType().hashCode();
        }
        if (getEncoding() != null) {
            _hashCode += getEncoding().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EncryptedType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "EncryptedType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("id");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Id"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "ID"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("type");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Type"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("mimeType");
        attrField.setXmlName(new javax.xml.namespace.QName("", "MimeType"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("encoding");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Encoding"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("encryptionMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "EncryptionMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "EncryptionMethodType"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("cipherData");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "CipherData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "CipherDataType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("encryptionProperties");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "EncryptionProperties"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "EncryptionPropertiesType"));
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
