/**
 * MessageAbstractTypeMSSP_Info.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MessageAbstractTypeMSSP_Info  implements java.io.Serializable {
    private org.etsi.uri.TS102204.v1_1_2.MeshMemberType MSSP_ID;

    private java.util.Calendar instant;  // attribute

    public MessageAbstractTypeMSSP_Info() {
    }

    public MessageAbstractTypeMSSP_Info(
           org.etsi.uri.TS102204.v1_1_2.MeshMemberType MSSP_ID,
           java.util.Calendar instant) {
           this.MSSP_ID = MSSP_ID;
           this.instant = instant;
    }


    /**
     * Gets the MSSP_ID value for this MessageAbstractTypeMSSP_Info.
     * 
     * @return MSSP_ID
     */
    public org.etsi.uri.TS102204.v1_1_2.MeshMemberType getMSSP_ID() {
        return MSSP_ID;
    }


    /**
     * Sets the MSSP_ID value for this MessageAbstractTypeMSSP_Info.
     * 
     * @param MSSP_ID
     */
    public void setMSSP_ID(org.etsi.uri.TS102204.v1_1_2.MeshMemberType MSSP_ID) {
        this.MSSP_ID = MSSP_ID;
    }


    /**
     * Gets the instant value for this MessageAbstractTypeMSSP_Info.
     * 
     * @return instant
     */
    public java.util.Calendar getInstant() {
        return instant;
    }


    /**
     * Sets the instant value for this MessageAbstractTypeMSSP_Info.
     * 
     * @param instant
     */
    public void setInstant(java.util.Calendar instant) {
        this.instant = instant;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MessageAbstractTypeMSSP_Info)) return false;
        MessageAbstractTypeMSSP_Info other = (MessageAbstractTypeMSSP_Info) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.MSSP_ID==null && other.getMSSP_ID()==null) || 
             (this.MSSP_ID!=null &&
              this.MSSP_ID.equals(other.getMSSP_ID()))) &&
            ((this.instant==null && other.getInstant()==null) || 
             (this.instant!=null &&
              this.instant.equals(other.getInstant())));
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
        if (getMSSP_ID() != null) {
            _hashCode += getMSSP_ID().hashCode();
        }
        if (getInstant() != null) {
            _hashCode += getInstant().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MessageAbstractTypeMSSP_Info.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MessageAbstractType>MSSP_Info"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("instant");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Instant"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MSSP_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSSP_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MeshMemberType"));
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
