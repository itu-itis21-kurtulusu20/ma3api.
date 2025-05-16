/**
 * KeyReferenceType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class KeyReferenceType  implements java.io.Serializable, org.apache.axis.encoding.AnyContentType {
    private org.apache.axis.types.URI[] certificateURL;

    private java.lang.String[] certificateIssuerDN;

    private org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType[] hashOfUsersPublicKey;

    private org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType[] hashOfCAPublicKey;

    private org.apache.axis.message.MessageElement [] _any;

    public KeyReferenceType() {
    }

    public KeyReferenceType(
           org.apache.axis.types.URI[] certificateURL,
           java.lang.String[] certificateIssuerDN,
           org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType[] hashOfUsersPublicKey,
           org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType[] hashOfCAPublicKey,
           org.apache.axis.message.MessageElement [] _any) {
           this.certificateURL = certificateURL;
           this.certificateIssuerDN = certificateIssuerDN;
           this.hashOfUsersPublicKey = hashOfUsersPublicKey;
           this.hashOfCAPublicKey = hashOfCAPublicKey;
           this._any = _any;
    }


    /**
     * Gets the certificateURL value for this KeyReferenceType.
     * 
     * @return certificateURL
     */
    public org.apache.axis.types.URI[] getCertificateURL() {
        return certificateURL;
    }


    /**
     * Sets the certificateURL value for this KeyReferenceType.
     * 
     * @param certificateURL
     */
    public void setCertificateURL(org.apache.axis.types.URI[] certificateURL) {
        this.certificateURL = certificateURL;
    }

    public org.apache.axis.types.URI getCertificateURL(int i) {
        return this.certificateURL[i];
    }

    public void setCertificateURL(int i, org.apache.axis.types.URI _value) {
        this.certificateURL[i] = _value;
    }


    /**
     * Gets the certificateIssuerDN value for this KeyReferenceType.
     * 
     * @return certificateIssuerDN
     */
    public java.lang.String[] getCertificateIssuerDN() {
        return certificateIssuerDN;
    }


    /**
     * Sets the certificateIssuerDN value for this KeyReferenceType.
     * 
     * @param certificateIssuerDN
     */
    public void setCertificateIssuerDN(java.lang.String[] certificateIssuerDN) {
        this.certificateIssuerDN = certificateIssuerDN;
    }

    public java.lang.String getCertificateIssuerDN(int i) {
        return this.certificateIssuerDN[i];
    }

    public void setCertificateIssuerDN(int i, java.lang.String _value) {
        this.certificateIssuerDN[i] = _value;
    }


    /**
     * Gets the hashOfUsersPublicKey value for this KeyReferenceType.
     * 
     * @return hashOfUsersPublicKey
     */
    public org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType[] getHashOfUsersPublicKey() {
        return hashOfUsersPublicKey;
    }


    /**
     * Sets the hashOfUsersPublicKey value for this KeyReferenceType.
     * 
     * @param hashOfUsersPublicKey
     */
    public void setHashOfUsersPublicKey(org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType[] hashOfUsersPublicKey) {
        this.hashOfUsersPublicKey = hashOfUsersPublicKey;
    }

    public org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType getHashOfUsersPublicKey(int i) {
        return this.hashOfUsersPublicKey[i];
    }

    public void setHashOfUsersPublicKey(int i, org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType _value) {
        this.hashOfUsersPublicKey[i] = _value;
    }


    /**
     * Gets the hashOfCAPublicKey value for this KeyReferenceType.
     * 
     * @return hashOfCAPublicKey
     */
    public org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType[] getHashOfCAPublicKey() {
        return hashOfCAPublicKey;
    }


    /**
     * Sets the hashOfCAPublicKey value for this KeyReferenceType.
     * 
     * @param hashOfCAPublicKey
     */
    public void setHashOfCAPublicKey(org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType[] hashOfCAPublicKey) {
        this.hashOfCAPublicKey = hashOfCAPublicKey;
    }

    public org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType getHashOfCAPublicKey(int i) {
        return this.hashOfCAPublicKey[i];
    }

    public void setHashOfCAPublicKey(int i, org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType _value) {
        this.hashOfCAPublicKey[i] = _value;
    }


    /**
     * Gets the _any value for this KeyReferenceType.
     * 
     * @return _any
     */
    public org.apache.axis.message.MessageElement [] get_any() {
        return _any;
    }


    /**
     * Sets the _any value for this KeyReferenceType.
     * 
     * @param _any
     */
    public void set_any(org.apache.axis.message.MessageElement [] _any) {
        this._any = _any;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof KeyReferenceType)) return false;
        KeyReferenceType other = (KeyReferenceType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.certificateURL==null && other.getCertificateURL()==null) || 
             (this.certificateURL!=null &&
              java.util.Arrays.equals(this.certificateURL, other.getCertificateURL()))) &&
            ((this.certificateIssuerDN==null && other.getCertificateIssuerDN()==null) || 
             (this.certificateIssuerDN!=null &&
              java.util.Arrays.equals(this.certificateIssuerDN, other.getCertificateIssuerDN()))) &&
            ((this.hashOfUsersPublicKey==null && other.getHashOfUsersPublicKey()==null) || 
             (this.hashOfUsersPublicKey!=null &&
              java.util.Arrays.equals(this.hashOfUsersPublicKey, other.getHashOfUsersPublicKey()))) &&
            ((this.hashOfCAPublicKey==null && other.getHashOfCAPublicKey()==null) || 
             (this.hashOfCAPublicKey!=null &&
              java.util.Arrays.equals(this.hashOfCAPublicKey, other.getHashOfCAPublicKey()))) &&
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
        if (getCertificateURL() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCertificateURL());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCertificateURL(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCertificateIssuerDN() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCertificateIssuerDN());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCertificateIssuerDN(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getHashOfUsersPublicKey() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getHashOfUsersPublicKey());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getHashOfUsersPublicKey(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getHashOfCAPublicKey() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getHashOfCAPublicKey());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getHashOfCAPublicKey(), i);
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
        new org.apache.axis.description.TypeDesc(KeyReferenceType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "KeyReferenceType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certificateURL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "CertificateURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certificateIssuerDN");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "CertificateIssuerDN"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hashOfUsersPublicKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "HashOfUsersPublicKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "DigestAlgAndValueType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hashOfCAPublicKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "HashOfCAPublicKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "DigestAlgAndValueType"));
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
