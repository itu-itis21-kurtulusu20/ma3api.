/**
 * ReferenceType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2000._09.xmldsig;

public class ReferenceType  implements java.io.Serializable {
    private org.w3.www._2000._09.xmldsig.TransformType[] transforms;

    private org.w3.www._2000._09.xmldsig.DigestMethodType digestMethod;

    private byte[] digestValue;

    private org.apache.axis.types.Id id;  // attribute

    private org.apache.axis.types.URI URI;  // attribute

    private org.apache.axis.types.URI type;  // attribute

    public ReferenceType() {
    }

    public ReferenceType(
           org.w3.www._2000._09.xmldsig.TransformType[] transforms,
           org.w3.www._2000._09.xmldsig.DigestMethodType digestMethod,
           byte[] digestValue,
           org.apache.axis.types.Id id,
           org.apache.axis.types.URI URI,
           org.apache.axis.types.URI type) {
           this.transforms = transforms;
           this.digestMethod = digestMethod;
           this.digestValue = digestValue;
           this.id = id;
           this.URI = URI;
           this.type = type;
    }


    /**
     * Gets the transforms value for this ReferenceType.
     * 
     * @return transforms
     */
    public org.w3.www._2000._09.xmldsig.TransformType[] getTransforms() {
        return transforms;
    }


    /**
     * Sets the transforms value for this ReferenceType.
     * 
     * @param transforms
     */
    public void setTransforms(org.w3.www._2000._09.xmldsig.TransformType[] transforms) {
        this.transforms = transforms;
    }


    /**
     * Gets the digestMethod value for this ReferenceType.
     * 
     * @return digestMethod
     */
    public org.w3.www._2000._09.xmldsig.DigestMethodType getDigestMethod() {
        return digestMethod;
    }


    /**
     * Sets the digestMethod value for this ReferenceType.
     * 
     * @param digestMethod
     */
    public void setDigestMethod(org.w3.www._2000._09.xmldsig.DigestMethodType digestMethod) {
        this.digestMethod = digestMethod;
    }


    /**
     * Gets the digestValue value for this ReferenceType.
     * 
     * @return digestValue
     */
    public byte[] getDigestValue() {
        return digestValue;
    }


    /**
     * Sets the digestValue value for this ReferenceType.
     * 
     * @param digestValue
     */
    public void setDigestValue(byte[] digestValue) {
        this.digestValue = digestValue;
    }


    /**
     * Gets the id value for this ReferenceType.
     * 
     * @return id
     */
    public org.apache.axis.types.Id getId() {
        return id;
    }


    /**
     * Sets the id value for this ReferenceType.
     * 
     * @param id
     */
    public void setId(org.apache.axis.types.Id id) {
        this.id = id;
    }


    /**
     * Gets the URI value for this ReferenceType.
     * 
     * @return URI
     */
    public org.apache.axis.types.URI getURI() {
        return URI;
    }


    /**
     * Sets the URI value for this ReferenceType.
     * 
     * @param URI
     */
    public void setURI(org.apache.axis.types.URI URI) {
        this.URI = URI;
    }


    /**
     * Gets the type value for this ReferenceType.
     * 
     * @return type
     */
    public org.apache.axis.types.URI getType() {
        return type;
    }


    /**
     * Sets the type value for this ReferenceType.
     * 
     * @param type
     */
    public void setType(org.apache.axis.types.URI type) {
        this.type = type;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReferenceType)) return false;
        ReferenceType other = (ReferenceType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.transforms==null && other.getTransforms()==null) || 
             (this.transforms!=null &&
              java.util.Arrays.equals(this.transforms, other.getTransforms()))) &&
            ((this.digestMethod==null && other.getDigestMethod()==null) || 
             (this.digestMethod!=null &&
              this.digestMethod.equals(other.getDigestMethod()))) &&
            ((this.digestValue==null && other.getDigestValue()==null) || 
             (this.digestValue!=null &&
              java.util.Arrays.equals(this.digestValue, other.getDigestValue()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.URI==null && other.getURI()==null) || 
             (this.URI!=null &&
              this.URI.equals(other.getURI()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType())));
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
        if (getTransforms() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTransforms());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTransforms(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDigestMethod() != null) {
            _hashCode += getDigestMethod().hashCode();
        }
        if (getDigestValue() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDigestValue());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDigestValue(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getURI() != null) {
            _hashCode += getURI().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReferenceType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "ReferenceType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("id");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Id"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "ID"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("URI");
        attrField.setXmlName(new javax.xml.namespace.QName("", "URI"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("type");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Type"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transforms");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "Transforms"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "TransformsType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("digestMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "DigestMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "DigestMethodType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("digestValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "DigestValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "DigestValueType"));
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
