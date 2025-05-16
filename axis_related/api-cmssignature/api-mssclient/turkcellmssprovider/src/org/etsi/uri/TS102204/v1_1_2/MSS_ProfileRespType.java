/**
 * MSS_ProfileRespType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MSS_ProfileRespType  extends org.etsi.uri.TS102204.v1_1_2.MessageAbstractType  implements java.io.Serializable {
    private org.etsi.uri.TS102204.v1_1_2.MssURIType[] signatureProfile;

    private org.etsi.uri.TS102204.v1_1_2.StatusType status;

    public MSS_ProfileRespType() {
    }

    public MSS_ProfileRespType(
           java.math.BigInteger majorVersion,
           java.math.BigInteger minorVersion,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeAP_Info AP_Info,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeMSSP_Info MSSP_Info,
           org.etsi.uri.TS102204.v1_1_2.MssURIType[] signatureProfile,
           org.etsi.uri.TS102204.v1_1_2.StatusType status) {
        super(
            majorVersion,
            minorVersion,
            AP_Info,
            MSSP_Info);
        this.signatureProfile = signatureProfile;
        this.status = status;
    }


    /**
     * Gets the signatureProfile value for this MSS_ProfileRespType.
     * 
     * @return signatureProfile
     */
    public org.etsi.uri.TS102204.v1_1_2.MssURIType[] getSignatureProfile() {
        return signatureProfile;
    }


    /**
     * Sets the signatureProfile value for this MSS_ProfileRespType.
     * 
     * @param signatureProfile
     */
    public void setSignatureProfile(org.etsi.uri.TS102204.v1_1_2.MssURIType[] signatureProfile) {
        this.signatureProfile = signatureProfile;
    }

    public org.etsi.uri.TS102204.v1_1_2.MssURIType getSignatureProfile(int i) {
        return this.signatureProfile[i];
    }

    public void setSignatureProfile(int i, org.etsi.uri.TS102204.v1_1_2.MssURIType _value) {
        this.signatureProfile[i] = _value;
    }


    /**
     * Gets the status value for this MSS_ProfileRespType.
     * 
     * @return status
     */
    public org.etsi.uri.TS102204.v1_1_2.StatusType getStatus() {
        return status;
    }


    /**
     * Sets the status value for this MSS_ProfileRespType.
     * 
     * @param status
     */
    public void setStatus(org.etsi.uri.TS102204.v1_1_2.StatusType status) {
        this.status = status;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MSS_ProfileRespType)) return false;
        MSS_ProfileRespType other = (MSS_ProfileRespType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.signatureProfile==null && other.getSignatureProfile()==null) || 
             (this.signatureProfile!=null &&
              java.util.Arrays.equals(this.signatureProfile, other.getSignatureProfile()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus())));
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
        if (getSignatureProfile() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSignatureProfile());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSignatureProfile(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MSS_ProfileRespType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_ProfileRespType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signatureProfile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "SignatureProfile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "mssURIType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "StatusType"));
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
