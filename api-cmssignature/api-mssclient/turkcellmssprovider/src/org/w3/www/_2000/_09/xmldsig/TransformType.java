/**
 * TransformType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2000._09.xmldsig;

public class TransformType  implements java.io.Serializable, org.apache.axis.encoding.AnyContentType, org.apache.axis.encoding.MixedContentType {
    private org.apache.axis.message.MessageElement [] _any;

    private java.lang.String XPath;

    private org.apache.axis.types.URI algorithm;  // attribute

    public TransformType() {
    }

    public TransformType(
           org.apache.axis.message.MessageElement [] _any,
           java.lang.String XPath,
           org.apache.axis.types.URI algorithm) {
           this._any = _any;
           this.XPath = XPath;
           this.algorithm = algorithm;
    }


    /**
     * Gets the _any value for this TransformType.
     * 
     * @return _any
     */
    public org.apache.axis.message.MessageElement [] get_any() {
        return _any;
    }


    /**
     * Sets the _any value for this TransformType.
     * 
     * @param _any
     */
    public void set_any(org.apache.axis.message.MessageElement [] _any) {
        this._any = _any;
    }


    /**
     * Gets the XPath value for this TransformType.
     * 
     * @return XPath
     */
    public java.lang.String getXPath() {
        return XPath;
    }


    /**
     * Sets the XPath value for this TransformType.
     * 
     * @param XPath
     */
    public void setXPath(java.lang.String XPath) {
        this.XPath = XPath;
    }


    /**
     * Gets the algorithm value for this TransformType.
     * 
     * @return algorithm
     */
    public org.apache.axis.types.URI getAlgorithm() {
        return algorithm;
    }


    /**
     * Sets the algorithm value for this TransformType.
     * 
     * @param algorithm
     */
    public void setAlgorithm(org.apache.axis.types.URI algorithm) {
        this.algorithm = algorithm;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TransformType)) return false;
        TransformType other = (TransformType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this._any==null && other.get_any()==null) || 
             (this._any!=null &&
              java.util.Arrays.equals(this._any, other.get_any()))) &&
            ((this.XPath==null && other.getXPath()==null) || 
             (this.XPath!=null &&
              this.XPath.equals(other.getXPath()))) &&
            ((this.algorithm==null && other.getAlgorithm()==null) || 
             (this.algorithm!=null &&
              this.algorithm.equals(other.getAlgorithm())));
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
        if (get_any() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(get_any());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(get_any(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getXPath() != null) {
            _hashCode += getXPath().hashCode();
        }
        if (getAlgorithm() != null) {
            _hashCode += getAlgorithm().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TransformType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "TransformType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("algorithm");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Algorithm"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("XPath");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "XPath"));
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
