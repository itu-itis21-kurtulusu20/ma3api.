/**
 * SignedInfoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2000._09.xmldsig;

public class SignedInfoType  implements java.io.Serializable {
    private org.w3.www._2000._09.xmldsig.CanonicalizationMethodType canonicalizationMethod;

    private org.w3.www._2000._09.xmldsig.SignatureMethodType signatureMethod;

    private org.w3.www._2000._09.xmldsig.ReferenceType[] reference;

    private org.apache.axis.types.Id id;  // attribute

    public SignedInfoType() {
    }

    public SignedInfoType(
           org.w3.www._2000._09.xmldsig.CanonicalizationMethodType canonicalizationMethod,
           org.w3.www._2000._09.xmldsig.SignatureMethodType signatureMethod,
           org.w3.www._2000._09.xmldsig.ReferenceType[] reference,
           org.apache.axis.types.Id id) {
           this.canonicalizationMethod = canonicalizationMethod;
           this.signatureMethod = signatureMethod;
           this.reference = reference;
           this.id = id;
    }


    /**
     * Gets the canonicalizationMethod value for this SignedInfoType.
     * 
     * @return canonicalizationMethod
     */
    public org.w3.www._2000._09.xmldsig.CanonicalizationMethodType getCanonicalizationMethod() {
        return canonicalizationMethod;
    }


    /**
     * Sets the canonicalizationMethod value for this SignedInfoType.
     * 
     * @param canonicalizationMethod
     */
    public void setCanonicalizationMethod(org.w3.www._2000._09.xmldsig.CanonicalizationMethodType canonicalizationMethod) {
        this.canonicalizationMethod = canonicalizationMethod;
    }


    /**
     * Gets the signatureMethod value for this SignedInfoType.
     * 
     * @return signatureMethod
     */
    public org.w3.www._2000._09.xmldsig.SignatureMethodType getSignatureMethod() {
        return signatureMethod;
    }


    /**
     * Sets the signatureMethod value for this SignedInfoType.
     * 
     * @param signatureMethod
     */
    public void setSignatureMethod(org.w3.www._2000._09.xmldsig.SignatureMethodType signatureMethod) {
        this.signatureMethod = signatureMethod;
    }


    /**
     * Gets the reference value for this SignedInfoType.
     * 
     * @return reference
     */
    public org.w3.www._2000._09.xmldsig.ReferenceType[] getReference() {
        return reference;
    }


    /**
     * Sets the reference value for this SignedInfoType.
     * 
     * @param reference
     */
    public void setReference(org.w3.www._2000._09.xmldsig.ReferenceType[] reference) {
        this.reference = reference;
    }

    public org.w3.www._2000._09.xmldsig.ReferenceType getReference(int i) {
        return this.reference[i];
    }

    public void setReference(int i, org.w3.www._2000._09.xmldsig.ReferenceType _value) {
        this.reference[i] = _value;
    }


    /**
     * Gets the id value for this SignedInfoType.
     * 
     * @return id
     */
    public org.apache.axis.types.Id getId() {
        return id;
    }


    /**
     * Sets the id value for this SignedInfoType.
     * 
     * @param id
     */
    public void setId(org.apache.axis.types.Id id) {
        this.id = id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SignedInfoType)) return false;
        SignedInfoType other = (SignedInfoType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.canonicalizationMethod==null && other.getCanonicalizationMethod()==null) || 
             (this.canonicalizationMethod!=null &&
              this.canonicalizationMethod.equals(other.getCanonicalizationMethod()))) &&
            ((this.signatureMethod==null && other.getSignatureMethod()==null) || 
             (this.signatureMethod!=null &&
              this.signatureMethod.equals(other.getSignatureMethod()))) &&
            ((this.reference==null && other.getReference()==null) || 
             (this.reference!=null &&
              java.util.Arrays.equals(this.reference, other.getReference()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId())));
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
        if (getCanonicalizationMethod() != null) {
            _hashCode += getCanonicalizationMethod().hashCode();
        }
        if (getSignatureMethod() != null) {
            _hashCode += getSignatureMethod().hashCode();
        }
        if (getReference() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getReference());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReference(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SignedInfoType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignedInfoType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("id");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Id"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "ID"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("canonicalizationMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "CanonicalizationMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "CanonicalizationMethodType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signatureMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignatureMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignatureMethodType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "Reference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "Reference"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
