/**
 * MSS_RegistrationReqType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MSS_RegistrationReqType  extends org.etsi.uri.TS102204.v1_1_2.MessageAbstractType  implements java.io.Serializable, org.apache.axis.encoding.AnyContentType {
    private org.etsi.uri.TS102204.v1_1_2.MobileUserType mobileUser;

    private org.w3.www._2001._04.xmlenc.EncryptedType encryptedData;

    private org.apache.axis.types.URI encryptResponseBy;

    private org.apache.axis.types.URI certificateURI;

    private byte[] x509Certificate;

    private org.apache.axis.message.MessageElement [] _any;

    public MSS_RegistrationReqType() {
    }

    public MSS_RegistrationReqType(
           java.math.BigInteger majorVersion,
           java.math.BigInteger minorVersion,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeAP_Info AP_Info,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeMSSP_Info MSSP_Info,
           org.etsi.uri.TS102204.v1_1_2.MobileUserType mobileUser,
           org.w3.www._2001._04.xmlenc.EncryptedType encryptedData,
           org.apache.axis.types.URI encryptResponseBy,
           org.apache.axis.types.URI certificateURI,
           byte[] x509Certificate,
           org.apache.axis.message.MessageElement [] _any) {
        super(
            majorVersion,
            minorVersion,
            AP_Info,
            MSSP_Info);
        this.mobileUser = mobileUser;
        this.encryptedData = encryptedData;
        this.encryptResponseBy = encryptResponseBy;
        this.certificateURI = certificateURI;
        this.x509Certificate = x509Certificate;
        this._any = _any;
    }


    /**
     * Gets the mobileUser value for this MSS_RegistrationReqType.
     * 
     * @return mobileUser
     */
    public org.etsi.uri.TS102204.v1_1_2.MobileUserType getMobileUser() {
        return mobileUser;
    }


    /**
     * Sets the mobileUser value for this MSS_RegistrationReqType.
     * 
     * @param mobileUser
     */
    public void setMobileUser(org.etsi.uri.TS102204.v1_1_2.MobileUserType mobileUser) {
        this.mobileUser = mobileUser;
    }


    /**
     * Gets the encryptedData value for this MSS_RegistrationReqType.
     * 
     * @return encryptedData
     */
    public org.w3.www._2001._04.xmlenc.EncryptedType getEncryptedData() {
        return encryptedData;
    }


    /**
     * Sets the encryptedData value for this MSS_RegistrationReqType.
     * 
     * @param encryptedData
     */
    public void setEncryptedData(org.w3.www._2001._04.xmlenc.EncryptedType encryptedData) {
        this.encryptedData = encryptedData;
    }


    /**
     * Gets the encryptResponseBy value for this MSS_RegistrationReqType.
     * 
     * @return encryptResponseBy
     */
    public org.apache.axis.types.URI getEncryptResponseBy() {
        return encryptResponseBy;
    }


    /**
     * Sets the encryptResponseBy value for this MSS_RegistrationReqType.
     * 
     * @param encryptResponseBy
     */
    public void setEncryptResponseBy(org.apache.axis.types.URI encryptResponseBy) {
        this.encryptResponseBy = encryptResponseBy;
    }


    /**
     * Gets the certificateURI value for this MSS_RegistrationReqType.
     * 
     * @return certificateURI
     */
    public org.apache.axis.types.URI getCertificateURI() {
        return certificateURI;
    }


    /**
     * Sets the certificateURI value for this MSS_RegistrationReqType.
     * 
     * @param certificateURI
     */
    public void setCertificateURI(org.apache.axis.types.URI certificateURI) {
        this.certificateURI = certificateURI;
    }


    /**
     * Gets the x509Certificate value for this MSS_RegistrationReqType.
     * 
     * @return x509Certificate
     */
    public byte[] getX509Certificate() {
        return x509Certificate;
    }


    /**
     * Sets the x509Certificate value for this MSS_RegistrationReqType.
     * 
     * @param x509Certificate
     */
    public void setX509Certificate(byte[] x509Certificate) {
        this.x509Certificate = x509Certificate;
    }


    /**
     * Gets the _any value for this MSS_RegistrationReqType.
     * 
     * @return _any
     */
    public org.apache.axis.message.MessageElement [] get_any() {
        return _any;
    }


    /**
     * Sets the _any value for this MSS_RegistrationReqType.
     * 
     * @param _any
     */
    public void set_any(org.apache.axis.message.MessageElement [] _any) {
        this._any = _any;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MSS_RegistrationReqType)) return false;
        MSS_RegistrationReqType other = (MSS_RegistrationReqType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.mobileUser==null && other.getMobileUser()==null) || 
             (this.mobileUser!=null &&
              this.mobileUser.equals(other.getMobileUser()))) &&
            ((this.encryptedData==null && other.getEncryptedData()==null) || 
             (this.encryptedData!=null &&
              this.encryptedData.equals(other.getEncryptedData()))) &&
            ((this.encryptResponseBy==null && other.getEncryptResponseBy()==null) || 
             (this.encryptResponseBy!=null &&
              this.encryptResponseBy.equals(other.getEncryptResponseBy()))) &&
            ((this.certificateURI==null && other.getCertificateURI()==null) || 
             (this.certificateURI!=null &&
              this.certificateURI.equals(other.getCertificateURI()))) &&
            ((this.x509Certificate==null && other.getX509Certificate()==null) || 
             (this.x509Certificate!=null &&
              java.util.Arrays.equals(this.x509Certificate, other.getX509Certificate()))) &&
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
        int _hashCode = super.hashCode();
        if (getMobileUser() != null) {
            _hashCode += getMobileUser().hashCode();
        }
        if (getEncryptedData() != null) {
            _hashCode += getEncryptedData().hashCode();
        }
        if (getEncryptResponseBy() != null) {
            _hashCode += getEncryptResponseBy().hashCode();
        }
        if (getCertificateURI() != null) {
            _hashCode += getCertificateURI().hashCode();
        }
        if (getX509Certificate() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getX509Certificate());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getX509Certificate(), i);
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
        new org.apache.axis.description.TypeDesc(MSS_RegistrationReqType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_RegistrationReqType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mobileUser");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MobileUser"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MobileUserType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("encryptedData");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "EncryptedData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "EncryptedType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("encryptResponseBy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "EncryptResponseBy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certificateURI");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "CertificateURI"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("x509Certificate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "X509Certificate"));
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
