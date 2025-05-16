/**
 * MSS_MessageSignature.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MSS_MessageSignature  implements java.io.Serializable {
    private org.w3.www._2000._09.xmldsig.SignatureType signature;

    private org.apache.axis.types.URI role;  // attribute

    private boolean mustUnderstand;  // attribute

    public MSS_MessageSignature() {
    }

    public MSS_MessageSignature(
           org.w3.www._2000._09.xmldsig.SignatureType signature,
           org.apache.axis.types.URI role,
           boolean mustUnderstand) {
           this.signature = signature;
           this.role = role;
           this.mustUnderstand = mustUnderstand;
    }


    /**
     * Gets the signature value for this MSS_MessageSignature.
     * 
     * @return signature
     */
    public org.w3.www._2000._09.xmldsig.SignatureType getSignature() {
        return signature;
    }


    /**
     * Sets the signature value for this MSS_MessageSignature.
     * 
     * @param signature
     */
    public void setSignature(org.w3.www._2000._09.xmldsig.SignatureType signature) {
        this.signature = signature;
    }


    /**
     * Gets the role value for this MSS_MessageSignature.
     * 
     * @return role
     */
    public org.apache.axis.types.URI getRole() {
        return role;
    }


    /**
     * Sets the role value for this MSS_MessageSignature.
     * 
     * @param role
     */
    public void setRole(org.apache.axis.types.URI role) {
        this.role = role;
    }


    /**
     * Gets the mustUnderstand value for this MSS_MessageSignature.
     * 
     * @return mustUnderstand
     */
    public boolean isMustUnderstand() {
        return mustUnderstand;
    }


    /**
     * Sets the mustUnderstand value for this MSS_MessageSignature.
     * 
     * @param mustUnderstand
     */
    public void setMustUnderstand(boolean mustUnderstand) {
        this.mustUnderstand = mustUnderstand;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MSS_MessageSignature)) return false;
        MSS_MessageSignature other = (MSS_MessageSignature) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.signature==null && other.getSignature()==null) || 
             (this.signature!=null &&
              this.signature.equals(other.getSignature()))) &&
            ((this.role==null && other.getRole()==null) || 
             (this.role!=null &&
              this.role.equals(other.getRole()))) &&
            this.mustUnderstand == other.isMustUnderstand();
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
        if (getSignature() != null) {
            _hashCode += getSignature().hashCode();
        }
        if (getRole() != null) {
            _hashCode += getRole().hashCode();
        }
        _hashCode += (isMustUnderstand() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MSS_MessageSignature.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MSS_MessageSignature"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("role");
        attrField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "role"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("mustUnderstand");
        attrField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "mustUnderstand"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signature");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "Signature"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignatureType"));
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
