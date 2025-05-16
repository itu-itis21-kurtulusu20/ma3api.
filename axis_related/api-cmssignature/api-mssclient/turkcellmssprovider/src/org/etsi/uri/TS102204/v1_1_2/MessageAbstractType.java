/**
 * MessageAbstractType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public abstract class MessageAbstractType  implements java.io.Serializable {
    private org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeAP_Info AP_Info;

    private org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeMSSP_Info MSSP_Info;

    private java.math.BigInteger majorVersion;  // attribute

    private java.math.BigInteger minorVersion;  // attribute

    public MessageAbstractType() {
    }

    public MessageAbstractType(
    		java.math.BigInteger majorVersion,
            java.math.BigInteger minorVersion,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeAP_Info AP_Info,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeMSSP_Info MSSP_Info) {
           this.AP_Info = AP_Info;
           this.MSSP_Info = MSSP_Info;
           this.majorVersion = majorVersion;
           this.minorVersion = minorVersion;
    }


    /**
     * Gets the AP_Info value for this MessageAbstractType.
     * 
     * @return AP_Info
     */
    public org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeAP_Info getAP_Info() {
        return AP_Info;
    }


    /**
     * Sets the AP_Info value for this MessageAbstractType.
     * 
     * @param AP_Info
     */
    public void setAP_Info(org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeAP_Info AP_Info) {
        this.AP_Info = AP_Info;
    }


    /**
     * Gets the MSSP_Info value for this MessageAbstractType.
     * 
     * @return MSSP_Info
     */
    public org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeMSSP_Info getMSSP_Info() {
        return MSSP_Info;
    }


    /**
     * Sets the MSSP_Info value for this MessageAbstractType.
     * 
     * @param MSSP_Info
     */
    public void setMSSP_Info(org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeMSSP_Info MSSP_Info) {
        this.MSSP_Info = MSSP_Info;
    }


    /**
     * Gets the majorVersion value for this MessageAbstractType.
     * 
     * @return majorVersion
     */
    public java.math.BigInteger getMajorVersion() {
        return majorVersion;
    }


    /**
     * Sets the majorVersion value for this MessageAbstractType.
     * 
     * @param majorVersion
     */
    public void setMajorVersion(java.math.BigInteger majorVersion) {
        this.majorVersion = majorVersion;
    }


    /**
     * Gets the minorVersion value for this MessageAbstractType.
     * 
     * @return minorVersion
     */
    public java.math.BigInteger getMinorVersion() {
        return minorVersion;
    }


    /**
     * Sets the minorVersion value for this MessageAbstractType.
     * 
     * @param minorVersion
     */
    public void setMinorVersion(java.math.BigInteger minorVersion) {
        this.minorVersion = minorVersion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MessageAbstractType)) return false;
        MessageAbstractType other = (MessageAbstractType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.AP_Info==null && other.getAP_Info()==null) || 
             (this.AP_Info!=null &&
              this.AP_Info.equals(other.getAP_Info()))) &&
            ((this.MSSP_Info==null && other.getMSSP_Info()==null) || 
             (this.MSSP_Info!=null &&
              this.MSSP_Info.equals(other.getMSSP_Info()))) &&
            ((this.majorVersion==null && other.getMajorVersion()==null) || 
             (this.majorVersion!=null &&
              this.majorVersion.equals(other.getMajorVersion()))) &&
            ((this.minorVersion==null && other.getMinorVersion()==null) || 
             (this.minorVersion!=null &&
              this.minorVersion.equals(other.getMinorVersion())));
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
        if (getAP_Info() != null) {
            _hashCode += getAP_Info().hashCode();
        }
        if (getMSSP_Info() != null) {
            _hashCode += getMSSP_Info().hashCode();
        }
        if (getMajorVersion() != null) {
            _hashCode += getMajorVersion().hashCode();
        }
        if (getMinorVersion() != null) {
            _hashCode += getMinorVersion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MessageAbstractType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MessageAbstractType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("majorVersion");
        attrField.setXmlName(new javax.xml.namespace.QName("", "MajorVersion"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("minorVersion");
        attrField.setXmlName(new javax.xml.namespace.QName("", "MinorVersion"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AP_Info");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "AP_Info"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MessageAbstractType>AP_Info"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MSSP_Info");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSSP_Info"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MessageAbstractType>MSSP_Info"));
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
