/**
 * Faultcode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2003._05.soap_envelope;

public class Faultcode  implements java.io.Serializable {
    private org.w3.www._2003._05.soap_envelope.FaultcodeEnum value;

    private org.w3.www._2003._05.soap_envelope.Subcode subcode;

    public Faultcode() {
    }

    public Faultcode(
           org.w3.www._2003._05.soap_envelope.FaultcodeEnum value,
           org.w3.www._2003._05.soap_envelope.Subcode subcode) {
           this.value = value;
           this.subcode = subcode;
    }


    /**
     * Gets the value value for this Faultcode.
     * 
     * @return value
     */
    public org.w3.www._2003._05.soap_envelope.FaultcodeEnum getValue() {
        return value;
    }


    /**
     * Sets the value value for this Faultcode.
     * 
     * @param value
     */
    public void setValue(org.w3.www._2003._05.soap_envelope.FaultcodeEnum value) {
        this.value = value;
    }


    /**
     * Gets the subcode value for this Faultcode.
     * 
     * @return subcode
     */
    public org.w3.www._2003._05.soap_envelope.Subcode getSubcode() {
        return subcode;
    }


    /**
     * Sets the subcode value for this Faultcode.
     * 
     * @param subcode
     */
    public void setSubcode(org.w3.www._2003._05.soap_envelope.Subcode subcode) {
        this.subcode = subcode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Faultcode)) return false;
        Faultcode other = (Faultcode) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              this.value.equals(other.getValue()))) &&
            ((this.subcode==null && other.getSubcode()==null) || 
             (this.subcode!=null &&
              this.subcode.equals(other.getSubcode())));
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
        if (getValue() != null) {
            _hashCode += getValue().hashCode();
        }
        if (getSubcode() != null) {
            _hashCode += getSubcode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Faultcode.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "faultcode"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "Value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "faultcodeEnum"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subcode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "Subcode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "subcode"));
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
