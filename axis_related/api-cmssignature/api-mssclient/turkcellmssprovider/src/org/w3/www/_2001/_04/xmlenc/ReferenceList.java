/**
 * ReferenceList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2001._04.xmlenc;

public class ReferenceList  implements java.io.Serializable {
    private org.w3.www._2001._04.xmlenc.ReferenceType dataReference;

    private org.w3.www._2001._04.xmlenc.ReferenceType keyReference;

    public ReferenceList() {
    }

    public ReferenceList(
           org.w3.www._2001._04.xmlenc.ReferenceType dataReference,
           org.w3.www._2001._04.xmlenc.ReferenceType keyReference) {
           this.dataReference = dataReference;
           this.keyReference = keyReference;
    }


    /**
     * Gets the dataReference value for this ReferenceList.
     * 
     * @return dataReference
     */
    public org.w3.www._2001._04.xmlenc.ReferenceType getDataReference() {
        return dataReference;
    }


    /**
     * Sets the dataReference value for this ReferenceList.
     * 
     * @param dataReference
     */
    public void setDataReference(org.w3.www._2001._04.xmlenc.ReferenceType dataReference) {
        this.dataReference = dataReference;
    }


    /**
     * Gets the keyReference value for this ReferenceList.
     * 
     * @return keyReference
     */
    public org.w3.www._2001._04.xmlenc.ReferenceType getKeyReference() {
        return keyReference;
    }


    /**
     * Sets the keyReference value for this ReferenceList.
     * 
     * @param keyReference
     */
    public void setKeyReference(org.w3.www._2001._04.xmlenc.ReferenceType keyReference) {
        this.keyReference = keyReference;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReferenceList)) return false;
        ReferenceList other = (ReferenceList) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dataReference==null && other.getDataReference()==null) || 
             (this.dataReference!=null &&
              this.dataReference.equals(other.getDataReference()))) &&
            ((this.keyReference==null && other.getKeyReference()==null) || 
             (this.keyReference!=null &&
              this.keyReference.equals(other.getKeyReference())));
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
        if (getDataReference() != null) {
            _hashCode += getDataReference().hashCode();
        }
        if (getKeyReference() != null) {
            _hashCode += getKeyReference().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReferenceList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", ">ReferenceList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "DataReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "ReferenceType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("keyReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "KeyReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "ReferenceType"));
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
