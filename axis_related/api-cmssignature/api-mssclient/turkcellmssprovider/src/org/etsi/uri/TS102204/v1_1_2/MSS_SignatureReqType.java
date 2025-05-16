/**
 * MSS_SignatureReqType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MSS_SignatureReqType  extends org.etsi.uri.TS102204.v1_1_2.MessageAbstractType  implements java.io.Serializable {
    private org.etsi.uri.TS102204.v1_1_2.MobileUserType mobileUser;

    private org.etsi.uri.TS102204.v1_1_2.DataType dataToBeSigned;

    private org.etsi.uri.TS102204.v1_1_2.DataType dataToBeDisplayed;

    private org.etsi.uri.TS102204.v1_1_2.MssURIType signatureProfile;

    private org.etsi.uri.TS102204.v1_1_2.AdditionalServiceType[] additionalServices;

    private org.etsi.uri.TS102204.v1_1_2.MssURIType MSS_Format;

    private org.etsi.uri.TS102204.v1_1_2.KeyReferenceType keyReference;

    private org.etsi.uri.TS102204.v1_1_2.SignatureProfileComparisonType signatureProfileComparison;

    private java.util.Calendar validityDate;  // attribute

    private org.apache.axis.types.PositiveInteger timeOut;  // attribute

    private org.etsi.uri.TS102204.v1_1_2.MessagingModeType messagingMode;  // attribute

    public MSS_SignatureReqType() {
    }

    public MSS_SignatureReqType(
           java.math.BigInteger majorVersion,
           java.math.BigInteger minorVersion,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeAP_Info AP_Info,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeMSSP_Info MSSP_Info,
           java.util.Calendar validityDate,
           org.apache.axis.types.PositiveInteger timeOut,
           org.etsi.uri.TS102204.v1_1_2.MessagingModeType messagingMode,
           org.etsi.uri.TS102204.v1_1_2.MobileUserType mobileUser,
           org.etsi.uri.TS102204.v1_1_2.DataType dataToBeSigned,
           org.etsi.uri.TS102204.v1_1_2.DataType dataToBeDisplayed,
           org.etsi.uri.TS102204.v1_1_2.MssURIType signatureProfile,
           org.etsi.uri.TS102204.v1_1_2.AdditionalServiceType[] additionalServices,
           org.etsi.uri.TS102204.v1_1_2.MssURIType MSS_Format,
           org.etsi.uri.TS102204.v1_1_2.KeyReferenceType keyReference,
           org.etsi.uri.TS102204.v1_1_2.SignatureProfileComparisonType signatureProfileComparison) {
        super(
            majorVersion,
            minorVersion,
            AP_Info,
            MSSP_Info);
        this.validityDate = validityDate;
        this.timeOut = timeOut;
        this.messagingMode = messagingMode;
        this.mobileUser = mobileUser;
        this.dataToBeSigned = dataToBeSigned;
        this.dataToBeDisplayed = dataToBeDisplayed;
        this.signatureProfile = signatureProfile;
        this.additionalServices = additionalServices;
        this.MSS_Format = MSS_Format;
        this.keyReference = keyReference;
        this.signatureProfileComparison = signatureProfileComparison;
    }


    /**
     * Gets the mobileUser value for this MSS_SignatureReqType.
     * 
     * @return mobileUser
     */
    public org.etsi.uri.TS102204.v1_1_2.MobileUserType getMobileUser() {
        return mobileUser;
    }


    /**
     * Sets the mobileUser value for this MSS_SignatureReqType.
     * 
     * @param mobileUser
     */
    public void setMobileUser(org.etsi.uri.TS102204.v1_1_2.MobileUserType mobileUser) {
        this.mobileUser = mobileUser;
    }


    /**
     * Gets the dataToBeSigned value for this MSS_SignatureReqType.
     * 
     * @return dataToBeSigned
     */
    public org.etsi.uri.TS102204.v1_1_2.DataType getDataToBeSigned() {
        return dataToBeSigned;
    }


    /**
     * Sets the dataToBeSigned value for this MSS_SignatureReqType.
     * 
     * @param dataToBeSigned
     */
    public void setDataToBeSigned(org.etsi.uri.TS102204.v1_1_2.DataType dataToBeSigned) {
        this.dataToBeSigned = dataToBeSigned;
    }


    /**
     * Gets the dataToBeDisplayed value for this MSS_SignatureReqType.
     * 
     * @return dataToBeDisplayed
     */
    public org.etsi.uri.TS102204.v1_1_2.DataType getDataToBeDisplayed() {
        return dataToBeDisplayed;
    }


    /**
     * Sets the dataToBeDisplayed value for this MSS_SignatureReqType.
     * 
     * @param dataToBeDisplayed
     */
    public void setDataToBeDisplayed(org.etsi.uri.TS102204.v1_1_2.DataType dataToBeDisplayed) {
        this.dataToBeDisplayed = dataToBeDisplayed;
    }


    /**
     * Gets the signatureProfile value for this MSS_SignatureReqType.
     * 
     * @return signatureProfile
     */
    public org.etsi.uri.TS102204.v1_1_2.MssURIType getSignatureProfile() {
        return signatureProfile;
    }


    /**
     * Sets the signatureProfile value for this MSS_SignatureReqType.
     * 
     * @param signatureProfile
     */
    public void setSignatureProfile(org.etsi.uri.TS102204.v1_1_2.MssURIType signatureProfile) {
        this.signatureProfile = signatureProfile;
    }


    /**
     * Gets the additionalServices value for this MSS_SignatureReqType.
     * 
     * @return additionalServices
     */
    public org.etsi.uri.TS102204.v1_1_2.AdditionalServiceType[] getAdditionalServices() {
        return additionalServices;
    }


    /**
     * Sets the additionalServices value for this MSS_SignatureReqType.
     * 
     * @param additionalServices
     */
    public void setAdditionalServices(org.etsi.uri.TS102204.v1_1_2.AdditionalServiceType[] additionalServices) {
        this.additionalServices = additionalServices;
    }


    /**
     * Gets the MSS_Format value for this MSS_SignatureReqType.
     * 
     * @return MSS_Format
     */
    public org.etsi.uri.TS102204.v1_1_2.MssURIType getMSS_Format() {
        return MSS_Format;
    }


    /**
     * Sets the MSS_Format value for this MSS_SignatureReqType.
     * 
     * @param MSS_Format
     */
    public void setMSS_Format(org.etsi.uri.TS102204.v1_1_2.MssURIType MSS_Format) {
        this.MSS_Format = MSS_Format;
    }


    /**
     * Gets the keyReference value for this MSS_SignatureReqType.
     * 
     * @return keyReference
     */
    public org.etsi.uri.TS102204.v1_1_2.KeyReferenceType getKeyReference() {
        return keyReference;
    }


    /**
     * Sets the keyReference value for this MSS_SignatureReqType.
     * 
     * @param keyReference
     */
    public void setKeyReference(org.etsi.uri.TS102204.v1_1_2.KeyReferenceType keyReference) {
        this.keyReference = keyReference;
    }


    /**
     * Gets the signatureProfileComparison value for this MSS_SignatureReqType.
     * 
     * @return signatureProfileComparison
     */
    public org.etsi.uri.TS102204.v1_1_2.SignatureProfileComparisonType getSignatureProfileComparison() {
        return signatureProfileComparison;
    }


    /**
     * Sets the signatureProfileComparison value for this MSS_SignatureReqType.
     * 
     * @param signatureProfileComparison
     */
    public void setSignatureProfileComparison(org.etsi.uri.TS102204.v1_1_2.SignatureProfileComparisonType signatureProfileComparison) {
        this.signatureProfileComparison = signatureProfileComparison;
    }


    /**
     * Gets the validityDate value for this MSS_SignatureReqType.
     * 
     * @return validityDate
     */
    public java.util.Calendar getValidityDate() {
        return validityDate;
    }


    /**
     * Sets the validityDate value for this MSS_SignatureReqType.
     * 
     * @param validityDate
     */
    public void setValidityDate(java.util.Calendar validityDate) {
        this.validityDate = validityDate;
    }


    /**
     * Gets the timeOut value for this MSS_SignatureReqType.
     * 
     * @return timeOut
     */
    public org.apache.axis.types.PositiveInteger getTimeOut() {
        return timeOut;
    }


    /**
     * Sets the timeOut value for this MSS_SignatureReqType.
     * 
     * @param timeOut
     */
    public void setTimeOut(org.apache.axis.types.PositiveInteger timeOut) {
        this.timeOut = timeOut;
    }


    /**
     * Gets the messagingMode value for this MSS_SignatureReqType.
     * 
     * @return messagingMode
     */
    public org.etsi.uri.TS102204.v1_1_2.MessagingModeType getMessagingMode() {
        return messagingMode;
    }


    /**
     * Sets the messagingMode value for this MSS_SignatureReqType.
     * 
     * @param messagingMode
     */
    public void setMessagingMode(org.etsi.uri.TS102204.v1_1_2.MessagingModeType messagingMode) {
        this.messagingMode = messagingMode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MSS_SignatureReqType)) return false;
        MSS_SignatureReqType other = (MSS_SignatureReqType) obj;
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
            ((this.dataToBeSigned==null && other.getDataToBeSigned()==null) || 
             (this.dataToBeSigned!=null &&
              this.dataToBeSigned.equals(other.getDataToBeSigned()))) &&
            ((this.dataToBeDisplayed==null && other.getDataToBeDisplayed()==null) || 
             (this.dataToBeDisplayed!=null &&
              this.dataToBeDisplayed.equals(other.getDataToBeDisplayed()))) &&
            ((this.signatureProfile==null && other.getSignatureProfile()==null) || 
             (this.signatureProfile!=null &&
              this.signatureProfile.equals(other.getSignatureProfile()))) &&
            ((this.additionalServices==null && other.getAdditionalServices()==null) || 
             (this.additionalServices!=null &&
              java.util.Arrays.equals(this.additionalServices, other.getAdditionalServices()))) &&
            ((this.MSS_Format==null && other.getMSS_Format()==null) || 
             (this.MSS_Format!=null &&
              this.MSS_Format.equals(other.getMSS_Format()))) &&
            ((this.keyReference==null && other.getKeyReference()==null) || 
             (this.keyReference!=null &&
              this.keyReference.equals(other.getKeyReference()))) &&
            ((this.signatureProfileComparison==null && other.getSignatureProfileComparison()==null) || 
             (this.signatureProfileComparison!=null &&
              this.signatureProfileComparison.equals(other.getSignatureProfileComparison()))) &&
            ((this.validityDate==null && other.getValidityDate()==null) || 
             (this.validityDate!=null &&
              this.validityDate.equals(other.getValidityDate()))) &&
            ((this.timeOut==null && other.getTimeOut()==null) || 
             (this.timeOut!=null &&
              this.timeOut.equals(other.getTimeOut()))) &&
            ((this.messagingMode==null && other.getMessagingMode()==null) || 
             (this.messagingMode!=null &&
              this.messagingMode.equals(other.getMessagingMode())));
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
        if (getDataToBeSigned() != null) {
            _hashCode += getDataToBeSigned().hashCode();
        }
        if (getDataToBeDisplayed() != null) {
            _hashCode += getDataToBeDisplayed().hashCode();
        }
        if (getSignatureProfile() != null) {
            _hashCode += getSignatureProfile().hashCode();
        }
        if (getAdditionalServices() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAdditionalServices());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAdditionalServices(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMSS_Format() != null) {
            _hashCode += getMSS_Format().hashCode();
        }
        if (getKeyReference() != null) {
            _hashCode += getKeyReference().hashCode();
        }
        if (getSignatureProfileComparison() != null) {
            _hashCode += getSignatureProfileComparison().hashCode();
        }
        if (getValidityDate() != null) {
            _hashCode += getValidityDate().hashCode();
        }
        if (getTimeOut() != null) {
            _hashCode += getTimeOut().hashCode();
        }
        if (getMessagingMode() != null) {
            _hashCode += getMessagingMode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MSS_SignatureReqType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_SignatureReqType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("validityDate");
        attrField.setXmlName(new javax.xml.namespace.QName("", "ValidityDate"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("timeOut");
        attrField.setXmlName(new javax.xml.namespace.QName("", "TimeOut"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "positiveInteger"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("messagingMode");
        attrField.setXmlName(new javax.xml.namespace.QName("", "MessagingMode"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MessagingModeType"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mobileUser");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MobileUser"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MobileUserType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataToBeSigned");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "DataToBeSigned"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "DataType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataToBeDisplayed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "DataToBeDisplayed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "DataType"));
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
        elemField.setFieldName("additionalServices");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "AdditionalServices"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "AdditionalServiceType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Service"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MSS_Format");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_Format"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "mssURIType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("keyReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "KeyReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "KeyReferenceType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signatureProfileComparison");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "SignatureProfileComparison"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "SignatureProfileComparisonType"));
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
