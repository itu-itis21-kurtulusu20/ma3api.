/**
 * AgreementMethodType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2001._04.xmlenc;

public class AgreementMethodType  implements java.io.Serializable, org.apache.axis.encoding.AnyContentType, org.apache.axis.encoding.MixedContentType {
    private byte[] KANonce;

    private org.apache.axis.message.MessageElement [] _any;

    private org.w3.www._2000._09.xmldsig.KeyInfoType originatorKeyInfo;

    private org.w3.www._2000._09.xmldsig.KeyInfoType recipientKeyInfo;

    private org.apache.axis.types.URI algorithm;  // attribute

    public AgreementMethodType() {
    }

    public AgreementMethodType(
           byte[] KANonce,
           org.apache.axis.message.MessageElement [] _any,
           org.w3.www._2000._09.xmldsig.KeyInfoType originatorKeyInfo,
           org.w3.www._2000._09.xmldsig.KeyInfoType recipientKeyInfo,
           org.apache.axis.types.URI algorithm) {
           this.KANonce = KANonce;
           this._any = _any;
           this.originatorKeyInfo = originatorKeyInfo;
           this.recipientKeyInfo = recipientKeyInfo;
           this.algorithm = algorithm;
    }


    /**
     * Gets the KANonce value for this AgreementMethodType.
     * 
     * @return KANonce
     */
    public byte[] getKANonce() {
        return KANonce;
    }


    /**
     * Sets the KANonce value for this AgreementMethodType.
     * 
     * @param KANonce
     */
    public void setKANonce(byte[] KANonce) {
        this.KANonce = KANonce;
    }


    /**
     * Gets the _any value for this AgreementMethodType.
     * 
     * @return _any
     */
    public org.apache.axis.message.MessageElement [] get_any() {
        return _any;
    }


    /**
     * Sets the _any value for this AgreementMethodType.
     * 
     * @param _any
     */
    public void set_any(org.apache.axis.message.MessageElement [] _any) {
        this._any = _any;
    }


    /**
     * Gets the originatorKeyInfo value for this AgreementMethodType.
     * 
     * @return originatorKeyInfo
     */
    public org.w3.www._2000._09.xmldsig.KeyInfoType getOriginatorKeyInfo() {
        return originatorKeyInfo;
    }


    /**
     * Sets the originatorKeyInfo value for this AgreementMethodType.
     * 
     * @param originatorKeyInfo
     */
    public void setOriginatorKeyInfo(org.w3.www._2000._09.xmldsig.KeyInfoType originatorKeyInfo) {
        this.originatorKeyInfo = originatorKeyInfo;
    }


    /**
     * Gets the recipientKeyInfo value for this AgreementMethodType.
     * 
     * @return recipientKeyInfo
     */
    public org.w3.www._2000._09.xmldsig.KeyInfoType getRecipientKeyInfo() {
        return recipientKeyInfo;
    }


    /**
     * Sets the recipientKeyInfo value for this AgreementMethodType.
     * 
     * @param recipientKeyInfo
     */
    public void setRecipientKeyInfo(org.w3.www._2000._09.xmldsig.KeyInfoType recipientKeyInfo) {
        this.recipientKeyInfo = recipientKeyInfo;
    }


    /**
     * Gets the algorithm value for this AgreementMethodType.
     * 
     * @return algorithm
     */
    public org.apache.axis.types.URI getAlgorithm() {
        return algorithm;
    }


    /**
     * Sets the algorithm value for this AgreementMethodType.
     * 
     * @param algorithm
     */
    public void setAlgorithm(org.apache.axis.types.URI algorithm) {
        this.algorithm = algorithm;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AgreementMethodType)) return false;
        AgreementMethodType other = (AgreementMethodType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.KANonce==null && other.getKANonce()==null) || 
             (this.KANonce!=null &&
              java.util.Arrays.equals(this.KANonce, other.getKANonce()))) &&
            ((this._any==null && other.get_any()==null) || 
             (this._any!=null &&
              java.util.Arrays.equals(this._any, other.get_any()))) &&
            ((this.originatorKeyInfo==null && other.getOriginatorKeyInfo()==null) || 
             (this.originatorKeyInfo!=null &&
              this.originatorKeyInfo.equals(other.getOriginatorKeyInfo()))) &&
            ((this.recipientKeyInfo==null && other.getRecipientKeyInfo()==null) || 
             (this.recipientKeyInfo!=null &&
              this.recipientKeyInfo.equals(other.getRecipientKeyInfo()))) &&
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
        if (getKANonce() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getKANonce());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getKANonce(), i);
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
        if (getOriginatorKeyInfo() != null) {
            _hashCode += getOriginatorKeyInfo().hashCode();
        }
        if (getRecipientKeyInfo() != null) {
            _hashCode += getRecipientKeyInfo().hashCode();
        }
        if (getAlgorithm() != null) {
            _hashCode += getAlgorithm().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AgreementMethodType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "AgreementMethodType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("algorithm");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Algorithm"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("KANonce");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "KA-Nonce"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("originatorKeyInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "OriginatorKeyInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "KeyInfoType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recipientKeyInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "RecipientKeyInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "KeyInfoType"));
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
