/**
 * MSS_SignatureRespType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MSS_SignatureRespType  extends org.etsi.uri.TS102204.v1_1_2.MessageAbstractType  implements java.io.Serializable {
    private org.etsi.uri.TS102204.v1_1_2.MobileUserType mobileUser;

    private org.etsi.uri.TS102204.v1_1_2.SignatureType MSS_Signature;

    private org.etsi.uri.TS102204.v1_1_2.MssURIType signatureProfile;

    private org.etsi.uri.TS102204.v1_1_2.StatusType status;

    private org.apache.axis.types.NCName MSSP_TransID;  // attribute

    public MSS_SignatureRespType() {
    }

    public MSS_SignatureRespType(
           java.math.BigInteger majorVersion,
           java.math.BigInteger minorVersion,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeAP_Info AP_Info,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeMSSP_Info MSSP_Info,
           org.apache.axis.types.NCName MSSP_TransID,
           org.etsi.uri.TS102204.v1_1_2.MobileUserType mobileUser,
           org.etsi.uri.TS102204.v1_1_2.SignatureType MSS_Signature,
           org.etsi.uri.TS102204.v1_1_2.MssURIType signatureProfile,
           org.etsi.uri.TS102204.v1_1_2.StatusType status) {
        super(
            majorVersion,
            minorVersion,
            AP_Info,
            MSSP_Info);
        this.MSSP_TransID = MSSP_TransID;
        this.mobileUser = mobileUser;
        this.MSS_Signature = MSS_Signature;
        this.signatureProfile = signatureProfile;
        this.status = status;
    }


    /**
     * Gets the mobileUser value for this MSS_SignatureRespType.
     * 
     * @return mobileUser
     */
    public org.etsi.uri.TS102204.v1_1_2.MobileUserType getMobileUser() {
        return mobileUser;
    }


    /**
     * Sets the mobileUser value for this MSS_SignatureRespType.
     * 
     * @param mobileUser
     */
    public void setMobileUser(org.etsi.uri.TS102204.v1_1_2.MobileUserType mobileUser) {
        this.mobileUser = mobileUser;
    }


    /**
     * Gets the MSS_Signature value for this MSS_SignatureRespType.
     * 
     * @return MSS_Signature
     */
    public org.etsi.uri.TS102204.v1_1_2.SignatureType getMSS_Signature() {
        return MSS_Signature;
    }


    /**
     * Sets the MSS_Signature value for this MSS_SignatureRespType.
     * 
     * @param MSS_Signature
     */
    public void setMSS_Signature(org.etsi.uri.TS102204.v1_1_2.SignatureType MSS_Signature) {
        this.MSS_Signature = MSS_Signature;
    }


    /**
     * Gets the signatureProfile value for this MSS_SignatureRespType.
     * 
     * @return signatureProfile
     */
    public org.etsi.uri.TS102204.v1_1_2.MssURIType getSignatureProfile() {
        return signatureProfile;
    }


    /**
     * Sets the signatureProfile value for this MSS_SignatureRespType.
     * 
     * @param signatureProfile
     */
    public void setSignatureProfile(org.etsi.uri.TS102204.v1_1_2.MssURIType signatureProfile) {
        this.signatureProfile = signatureProfile;
    }


    /**
     * Gets the status value for this MSS_SignatureRespType.
     * 
     * @return status
     */
    public org.etsi.uri.TS102204.v1_1_2.StatusType getStatus() {
        return status;
    }


    /**
     * Sets the status value for this MSS_SignatureRespType.
     * 
     * @param status
     */
    public void setStatus(org.etsi.uri.TS102204.v1_1_2.StatusType status) {
        this.status = status;
    }


    /**
     * Gets the MSSP_TransID value for this MSS_SignatureRespType.
     * 
     * @return MSSP_TransID
     */
    public org.apache.axis.types.NCName getMSSP_TransID() {
        return MSSP_TransID;
    }


    /**
     * Sets the MSSP_TransID value for this MSS_SignatureRespType.
     * 
     * @param MSSP_TransID
     */
    public void setMSSP_TransID(org.apache.axis.types.NCName MSSP_TransID) {
        this.MSSP_TransID = MSSP_TransID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MSS_SignatureRespType)) return false;
        MSS_SignatureRespType other = (MSS_SignatureRespType) obj;
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
            ((this.MSS_Signature==null && other.getMSS_Signature()==null) || 
             (this.MSS_Signature!=null &&
              this.MSS_Signature.equals(other.getMSS_Signature()))) &&
            ((this.signatureProfile==null && other.getSignatureProfile()==null) || 
             (this.signatureProfile!=null &&
              this.signatureProfile.equals(other.getSignatureProfile()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.MSSP_TransID==null && other.getMSSP_TransID()==null) || 
             (this.MSSP_TransID!=null &&
              this.MSSP_TransID.equals(other.getMSSP_TransID())));
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
        if (getMSS_Signature() != null) {
            _hashCode += getMSS_Signature().hashCode();
        }
        if (getSignatureProfile() != null) {
            _hashCode += getSignatureProfile().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getMSSP_TransID() != null) {
            _hashCode += getMSSP_TransID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MSS_SignatureRespType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_SignatureRespType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("MSSP_TransID");
        attrField.setXmlName(new javax.xml.namespace.QName("", "MSSP_TransID"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mobileUser");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MobileUser"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MobileUserType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MSS_Signature");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_Signature"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "SignatureType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signatureProfile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "SignatureProfile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "mssURIType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
