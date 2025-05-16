/**
 * MobileUserType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MobileUserType  implements java.io.Serializable {
    private org.etsi.uri.TS102204.v1_1_2.MeshMemberType identityIssuer;

    private java.lang.String userIdentifier;

    private org.etsi.uri.TS102204.v1_1_2.MeshMemberType homeMSSP;

    private java.lang.String MSISDN;

    public MobileUserType() {
    }

    public MobileUserType(
           org.etsi.uri.TS102204.v1_1_2.MeshMemberType identityIssuer,
           java.lang.String userIdentifier,
           org.etsi.uri.TS102204.v1_1_2.MeshMemberType homeMSSP,
           java.lang.String MSISDN) {
           this.identityIssuer = identityIssuer;
           this.userIdentifier = userIdentifier;
           this.homeMSSP = homeMSSP;
           this.MSISDN = MSISDN;
    }


    /**
     * Gets the identityIssuer value for this MobileUserType.
     * 
     * @return identityIssuer
     */
    public org.etsi.uri.TS102204.v1_1_2.MeshMemberType getIdentityIssuer() {
        return identityIssuer;
    }


    /**
     * Sets the identityIssuer value for this MobileUserType.
     * 
     * @param identityIssuer
     */
    public void setIdentityIssuer(org.etsi.uri.TS102204.v1_1_2.MeshMemberType identityIssuer) {
        this.identityIssuer = identityIssuer;
    }


    /**
     * Gets the userIdentifier value for this MobileUserType.
     * 
     * @return userIdentifier
     */
    public java.lang.String getUserIdentifier() {
        return userIdentifier;
    }


    /**
     * Sets the userIdentifier value for this MobileUserType.
     * 
     * @param userIdentifier
     */
    public void setUserIdentifier(java.lang.String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }


    /**
     * Gets the homeMSSP value for this MobileUserType.
     * 
     * @return homeMSSP
     */
    public org.etsi.uri.TS102204.v1_1_2.MeshMemberType getHomeMSSP() {
        return homeMSSP;
    }


    /**
     * Sets the homeMSSP value for this MobileUserType.
     * 
     * @param homeMSSP
     */
    public void setHomeMSSP(org.etsi.uri.TS102204.v1_1_2.MeshMemberType homeMSSP) {
        this.homeMSSP = homeMSSP;
    }


    /**
     * Gets the MSISDN value for this MobileUserType.
     * 
     * @return MSISDN
     */
    public java.lang.String getMSISDN() {
        return MSISDN;
    }


    /**
     * Sets the MSISDN value for this MobileUserType.
     * 
     * @param MSISDN
     */
    public void setMSISDN(java.lang.String MSISDN) {
        this.MSISDN = MSISDN;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MobileUserType)) return false;
        MobileUserType other = (MobileUserType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.identityIssuer==null && other.getIdentityIssuer()==null) || 
             (this.identityIssuer!=null &&
              this.identityIssuer.equals(other.getIdentityIssuer()))) &&
            ((this.userIdentifier==null && other.getUserIdentifier()==null) || 
             (this.userIdentifier!=null &&
              this.userIdentifier.equals(other.getUserIdentifier()))) &&
            ((this.homeMSSP==null && other.getHomeMSSP()==null) || 
             (this.homeMSSP!=null &&
              this.homeMSSP.equals(other.getHomeMSSP()))) &&
            ((this.MSISDN==null && other.getMSISDN()==null) || 
             (this.MSISDN!=null &&
              this.MSISDN.equals(other.getMSISDN())));
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
        if (getIdentityIssuer() != null) {
            _hashCode += getIdentityIssuer().hashCode();
        }
        if (getUserIdentifier() != null) {
            _hashCode += getUserIdentifier().hashCode();
        }
        if (getHomeMSSP() != null) {
            _hashCode += getHomeMSSP().hashCode();
        }
        if (getMSISDN() != null) {
            _hashCode += getMSISDN().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MobileUserType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MobileUserType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identityIssuer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "IdentityIssuer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MeshMemberType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userIdentifier");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "UserIdentifier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("homeMSSP");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "HomeMSSP"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MeshMemberType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MSISDN");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSISDN"));
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
