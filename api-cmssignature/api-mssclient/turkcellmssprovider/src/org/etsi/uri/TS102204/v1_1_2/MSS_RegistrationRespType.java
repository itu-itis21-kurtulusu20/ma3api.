/**
 * MSS_RegistrationRespType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MSS_RegistrationRespType  extends org.etsi.uri.TS102204.v1_1_2.MessageAbstractType  implements java.io.Serializable {
    private org.etsi.uri.TS102204.v1_1_2.StatusType status;

    private org.w3.www._2001._04.xmlenc.EncryptedType encryptedData;

    private org.apache.axis.types.URI certificateURI;

    private byte[] x509Certificate;

    private byte[] publicKey;

    public MSS_RegistrationRespType() {
    }

    public MSS_RegistrationRespType(
           java.math.BigInteger majorVersion,
           java.math.BigInteger minorVersion,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeAP_Info AP_Info,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeMSSP_Info MSSP_Info,
           org.etsi.uri.TS102204.v1_1_2.StatusType status,
           org.w3.www._2001._04.xmlenc.EncryptedType encryptedData,
           org.apache.axis.types.URI certificateURI,
           byte[] x509Certificate,
           byte[] publicKey) {
        super(
            majorVersion,
            minorVersion,
            AP_Info,
            MSSP_Info);
        this.status = status;
        this.encryptedData = encryptedData;
        this.certificateURI = certificateURI;
        this.x509Certificate = x509Certificate;
        this.publicKey = publicKey;
    }


    /**
     * Gets the status value for this MSS_RegistrationRespType.
     * 
     * @return status
     */
    public org.etsi.uri.TS102204.v1_1_2.StatusType getStatus() {
        return status;
    }


    /**
     * Sets the status value for this MSS_RegistrationRespType.
     * 
     * @param status
     */
    public void setStatus(org.etsi.uri.TS102204.v1_1_2.StatusType status) {
        this.status = status;
    }


    /**
     * Gets the encryptedData value for this MSS_RegistrationRespType.
     * 
     * @return encryptedData
     */
    public org.w3.www._2001._04.xmlenc.EncryptedType getEncryptedData() {
        return encryptedData;
    }


    /**
     * Sets the encryptedData value for this MSS_RegistrationRespType.
     * 
     * @param encryptedData
     */
    public void setEncryptedData(org.w3.www._2001._04.xmlenc.EncryptedType encryptedData) {
        this.encryptedData = encryptedData;
    }


    /**
     * Gets the certificateURI value for this MSS_RegistrationRespType.
     * 
     * @return certificateURI
     */
    public org.apache.axis.types.URI getCertificateURI() {
        return certificateURI;
    }


    /**
     * Sets the certificateURI value for this MSS_RegistrationRespType.
     * 
     * @param certificateURI
     */
    public void setCertificateURI(org.apache.axis.types.URI certificateURI) {
        this.certificateURI = certificateURI;
    }


    /**
     * Gets the x509Certificate value for this MSS_RegistrationRespType.
     * 
     * @return x509Certificate
     */
    public byte[] getX509Certificate() {
        return x509Certificate;
    }


    /**
     * Sets the x509Certificate value for this MSS_RegistrationRespType.
     * 
     * @param x509Certificate
     */
    public void setX509Certificate(byte[] x509Certificate) {
        this.x509Certificate = x509Certificate;
    }


    /**
     * Gets the publicKey value for this MSS_RegistrationRespType.
     * 
     * @return publicKey
     */
    public byte[] getPublicKey() {
        return publicKey;
    }


    /**
     * Sets the publicKey value for this MSS_RegistrationRespType.
     * 
     * @param publicKey
     */
    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MSS_RegistrationRespType)) return false;
        MSS_RegistrationRespType other = (MSS_RegistrationRespType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.encryptedData==null && other.getEncryptedData()==null) || 
             (this.encryptedData!=null &&
              this.encryptedData.equals(other.getEncryptedData()))) &&
            ((this.certificateURI==null && other.getCertificateURI()==null) || 
             (this.certificateURI!=null &&
              this.certificateURI.equals(other.getCertificateURI()))) &&
            ((this.x509Certificate==null && other.getX509Certificate()==null) || 
             (this.x509Certificate!=null &&
              java.util.Arrays.equals(this.x509Certificate, other.getX509Certificate()))) &&
            ((this.publicKey==null && other.getPublicKey()==null) || 
             (this.publicKey!=null &&
              java.util.Arrays.equals(this.publicKey, other.getPublicKey())));
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
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getEncryptedData() != null) {
            _hashCode += getEncryptedData().hashCode();
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
        if (getPublicKey() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPublicKey());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPublicKey(), i);
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
        new org.apache.axis.description.TypeDesc(MSS_RegistrationRespType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_RegistrationRespType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "StatusType"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("publicKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "PublicKey"));
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
