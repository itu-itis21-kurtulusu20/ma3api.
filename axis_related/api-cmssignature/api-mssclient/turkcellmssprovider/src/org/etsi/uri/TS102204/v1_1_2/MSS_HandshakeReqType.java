/**
 * MSS_HandshakeReqType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MSS_HandshakeReqType  extends org.etsi.uri.TS102204.v1_1_2.MessageAbstractType  implements java.io.Serializable {
    private org.etsi.uri.TS102204.v1_1_2.MSS_HandshakeReqTypeSecureMethods secureMethods;

    private byte[][] certificates;

    private java.lang.String[] rootCAs;

    private org.etsi.uri.TS102204.v1_1_2.MssURIType[] signatureAlgList;

    public MSS_HandshakeReqType() {
    }

    public MSS_HandshakeReqType(
           java.math.BigInteger majorVersion,
           java.math.BigInteger minorVersion,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeAP_Info AP_Info,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeMSSP_Info MSSP_Info,
           org.etsi.uri.TS102204.v1_1_2.MSS_HandshakeReqTypeSecureMethods secureMethods,
           byte[][] certificates,
           java.lang.String[] rootCAs,
           org.etsi.uri.TS102204.v1_1_2.MssURIType[] signatureAlgList) {
        super(
            majorVersion,
            minorVersion,
            AP_Info,
            MSSP_Info);
        this.secureMethods = secureMethods;
        this.certificates = certificates;
        this.rootCAs = rootCAs;
        this.signatureAlgList = signatureAlgList;
    }


    /**
     * Gets the secureMethods value for this MSS_HandshakeReqType.
     * 
     * @return secureMethods
     */
    public org.etsi.uri.TS102204.v1_1_2.MSS_HandshakeReqTypeSecureMethods getSecureMethods() {
        return secureMethods;
    }


    /**
     * Sets the secureMethods value for this MSS_HandshakeReqType.
     * 
     * @param secureMethods
     */
    public void setSecureMethods(org.etsi.uri.TS102204.v1_1_2.MSS_HandshakeReqTypeSecureMethods secureMethods) {
        this.secureMethods = secureMethods;
    }


    /**
     * Gets the certificates value for this MSS_HandshakeReqType.
     * 
     * @return certificates
     */
    public byte[][] getCertificates() {
        return certificates;
    }


    /**
     * Sets the certificates value for this MSS_HandshakeReqType.
     * 
     * @param certificates
     */
    public void setCertificates(byte[][] certificates) {
        this.certificates = certificates;
    }


    /**
     * Gets the rootCAs value for this MSS_HandshakeReqType.
     * 
     * @return rootCAs
     */
    public java.lang.String[] getRootCAs() {
        return rootCAs;
    }


    /**
     * Sets the rootCAs value for this MSS_HandshakeReqType.
     * 
     * @param rootCAs
     */
    public void setRootCAs(java.lang.String[] rootCAs) {
        this.rootCAs = rootCAs;
    }


    /**
     * Gets the signatureAlgList value for this MSS_HandshakeReqType.
     * 
     * @return signatureAlgList
     */
    public org.etsi.uri.TS102204.v1_1_2.MssURIType[] getSignatureAlgList() {
        return signatureAlgList;
    }


    /**
     * Sets the signatureAlgList value for this MSS_HandshakeReqType.
     * 
     * @param signatureAlgList
     */
    public void setSignatureAlgList(org.etsi.uri.TS102204.v1_1_2.MssURIType[] signatureAlgList) {
        this.signatureAlgList = signatureAlgList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MSS_HandshakeReqType)) return false;
        MSS_HandshakeReqType other = (MSS_HandshakeReqType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.secureMethods==null && other.getSecureMethods()==null) || 
             (this.secureMethods!=null &&
              this.secureMethods.equals(other.getSecureMethods()))) &&
            ((this.certificates==null && other.getCertificates()==null) || 
             (this.certificates!=null &&
              java.util.Arrays.equals(this.certificates, other.getCertificates()))) &&
            ((this.rootCAs==null && other.getRootCAs()==null) || 
             (this.rootCAs!=null &&
              java.util.Arrays.equals(this.rootCAs, other.getRootCAs()))) &&
            ((this.signatureAlgList==null && other.getSignatureAlgList()==null) || 
             (this.signatureAlgList!=null &&
              java.util.Arrays.equals(this.signatureAlgList, other.getSignatureAlgList())));
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
        if (getSecureMethods() != null) {
            _hashCode += getSecureMethods().hashCode();
        }
        if (getCertificates() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCertificates());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCertificates(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRootCAs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRootCAs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRootCAs(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSignatureAlgList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSignatureAlgList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSignatureAlgList(), i);
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
        new org.apache.axis.description.TypeDesc(MSS_HandshakeReqType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_HandshakeReqType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("secureMethods");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "SecureMethods"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MSS_HandshakeReqType>SecureMethods"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certificates");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Certificates"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Certificate"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rootCAs");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "RootCAs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "DN"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signatureAlgList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "SignatureAlgList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "mssURIType"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Algorithm"));
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
