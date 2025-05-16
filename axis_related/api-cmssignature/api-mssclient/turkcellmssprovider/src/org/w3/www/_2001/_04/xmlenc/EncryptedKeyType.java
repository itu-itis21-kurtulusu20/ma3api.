/**
 * EncryptedKeyType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2001._04.xmlenc;

public class EncryptedKeyType  extends org.w3.www._2001._04.xmlenc.EncryptedType  implements java.io.Serializable {
    private org.w3.www._2001._04.xmlenc.ReferenceList referenceList;

    private java.lang.String carriedKeyName;

    private java.lang.String recipient;  // attribute

    public EncryptedKeyType() {
    }

    public EncryptedKeyType(
           org.apache.axis.types.Id id,
           org.apache.axis.types.URI type,
           java.lang.String mimeType,
           org.apache.axis.types.URI encoding,
           org.w3.www._2001._04.xmlenc.EncryptionMethodType encryptionMethod,
           org.w3.www._2000._09.xmldsig.KeyInfoType keyInfo,
           org.w3.www._2001._04.xmlenc.CipherDataType cipherData,
           org.w3.www._2001._04.xmlenc.EncryptionPropertyType[] encryptionProperties,
           java.lang.String recipient,
           org.w3.www._2001._04.xmlenc.ReferenceList referenceList,
           java.lang.String carriedKeyName) {
        super(
            id,
            type,
            mimeType,
            encoding,
            encryptionMethod,
            keyInfo,
            cipherData,
            encryptionProperties);
        this.recipient = recipient;
        this.referenceList = referenceList;
        this.carriedKeyName = carriedKeyName;
    }


    /**
     * Gets the referenceList value for this EncryptedKeyType.
     * 
     * @return referenceList
     */
    public org.w3.www._2001._04.xmlenc.ReferenceList getReferenceList() {
        return referenceList;
    }


    /**
     * Sets the referenceList value for this EncryptedKeyType.
     * 
     * @param referenceList
     */
    public void setReferenceList(org.w3.www._2001._04.xmlenc.ReferenceList referenceList) {
        this.referenceList = referenceList;
    }


    /**
     * Gets the carriedKeyName value for this EncryptedKeyType.
     * 
     * @return carriedKeyName
     */
    public java.lang.String getCarriedKeyName() {
        return carriedKeyName;
    }


    /**
     * Sets the carriedKeyName value for this EncryptedKeyType.
     * 
     * @param carriedKeyName
     */
    public void setCarriedKeyName(java.lang.String carriedKeyName) {
        this.carriedKeyName = carriedKeyName;
    }


    /**
     * Gets the recipient value for this EncryptedKeyType.
     * 
     * @return recipient
     */
    public java.lang.String getRecipient() {
        return recipient;
    }


    /**
     * Sets the recipient value for this EncryptedKeyType.
     * 
     * @param recipient
     */
    public void setRecipient(java.lang.String recipient) {
        this.recipient = recipient;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EncryptedKeyType)) return false;
        EncryptedKeyType other = (EncryptedKeyType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.referenceList==null && other.getReferenceList()==null) || 
             (this.referenceList!=null &&
              this.referenceList.equals(other.getReferenceList()))) &&
            ((this.carriedKeyName==null && other.getCarriedKeyName()==null) || 
             (this.carriedKeyName!=null &&
              this.carriedKeyName.equals(other.getCarriedKeyName()))) &&
            ((this.recipient==null && other.getRecipient()==null) || 
             (this.recipient!=null &&
              this.recipient.equals(other.getRecipient())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getReferenceList() != null) {
            _hashCode += getReferenceList().hashCode();
        }
        if (getCarriedKeyName() != null) {
            _hashCode += getCarriedKeyName().hashCode();
        }
        if (getRecipient() != null) {
            _hashCode += getRecipient().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EncryptedKeyType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "EncryptedKeyType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("recipient");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Recipient"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referenceList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "ReferenceList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", ">ReferenceList"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("carriedKeyName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "CarriedKeyName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
