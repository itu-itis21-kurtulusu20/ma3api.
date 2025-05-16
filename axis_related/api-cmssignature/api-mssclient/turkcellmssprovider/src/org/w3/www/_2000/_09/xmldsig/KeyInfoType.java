/**
 * KeyInfoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2000._09.xmldsig;

public class KeyInfoType  implements java.io.Serializable, org.apache.axis.encoding.AnyContentType, org.apache.axis.encoding.MixedContentType {
    private java.lang.String keyName;

    private org.w3.www._2000._09.xmldsig.KeyValueType keyValue;

    private org.w3.www._2000._09.xmldsig.RetrievalMethodType retrievalMethod;

    private org.w3.www._2000._09.xmldsig.X509DataType x509Data;

    private org.w3.www._2000._09.xmldsig.PGPDataType PGPData;

    private org.w3.www._2000._09.xmldsig.SPKIDataType SPKIData;

    private java.lang.String mgmtData;

    private org.apache.axis.message.MessageElement [] _any;

    private org.apache.axis.types.Id id;  // attribute

    public KeyInfoType() {
    }

    public KeyInfoType(
           java.lang.String keyName,
           org.w3.www._2000._09.xmldsig.KeyValueType keyValue,
           org.w3.www._2000._09.xmldsig.RetrievalMethodType retrievalMethod,
           org.w3.www._2000._09.xmldsig.X509DataType x509Data,
           org.w3.www._2000._09.xmldsig.PGPDataType PGPData,
           org.w3.www._2000._09.xmldsig.SPKIDataType SPKIData,
           java.lang.String mgmtData,
           org.apache.axis.message.MessageElement [] _any,
           org.apache.axis.types.Id id) {
           this.keyName = keyName;
           this.keyValue = keyValue;
           this.retrievalMethod = retrievalMethod;
           this.x509Data = x509Data;
           this.PGPData = PGPData;
           this.SPKIData = SPKIData;
           this.mgmtData = mgmtData;
           this._any = _any;
           this.id = id;
    }


    /**
     * Gets the keyName value for this KeyInfoType.
     * 
     * @return keyName
     */
    public java.lang.String getKeyName() {
        return keyName;
    }


    /**
     * Sets the keyName value for this KeyInfoType.
     * 
     * @param keyName
     */
    public void setKeyName(java.lang.String keyName) {
        this.keyName = keyName;
    }


    /**
     * Gets the keyValue value for this KeyInfoType.
     * 
     * @return keyValue
     */
    public org.w3.www._2000._09.xmldsig.KeyValueType getKeyValue() {
        return keyValue;
    }


    /**
     * Sets the keyValue value for this KeyInfoType.
     * 
     * @param keyValue
     */
    public void setKeyValue(org.w3.www._2000._09.xmldsig.KeyValueType keyValue) {
        this.keyValue = keyValue;
    }


    /**
     * Gets the retrievalMethod value for this KeyInfoType.
     * 
     * @return retrievalMethod
     */
    public org.w3.www._2000._09.xmldsig.RetrievalMethodType getRetrievalMethod() {
        return retrievalMethod;
    }


    /**
     * Sets the retrievalMethod value for this KeyInfoType.
     * 
     * @param retrievalMethod
     */
    public void setRetrievalMethod(org.w3.www._2000._09.xmldsig.RetrievalMethodType retrievalMethod) {
        this.retrievalMethod = retrievalMethod;
    }


    /**
     * Gets the x509Data value for this KeyInfoType.
     * 
     * @return x509Data
     */
    public org.w3.www._2000._09.xmldsig.X509DataType getX509Data() {
        return x509Data;
    }


    /**
     * Sets the x509Data value for this KeyInfoType.
     * 
     * @param x509Data
     */
    public void setX509Data(org.w3.www._2000._09.xmldsig.X509DataType x509Data) {
        this.x509Data = x509Data;
    }


    /**
     * Gets the PGPData value for this KeyInfoType.
     * 
     * @return PGPData
     */
    public org.w3.www._2000._09.xmldsig.PGPDataType getPGPData() {
        return PGPData;
    }


    /**
     * Sets the PGPData value for this KeyInfoType.
     * 
     * @param PGPData
     */
    public void setPGPData(org.w3.www._2000._09.xmldsig.PGPDataType PGPData) {
        this.PGPData = PGPData;
    }


    /**
     * Gets the SPKIData value for this KeyInfoType.
     * 
     * @return SPKIData
     */
    public org.w3.www._2000._09.xmldsig.SPKIDataType getSPKIData() {
        return SPKIData;
    }


    /**
     * Sets the SPKIData value for this KeyInfoType.
     * 
     * @param SPKIData
     */
    public void setSPKIData(org.w3.www._2000._09.xmldsig.SPKIDataType SPKIData) {
        this.SPKIData = SPKIData;
    }


    /**
     * Gets the mgmtData value for this KeyInfoType.
     * 
     * @return mgmtData
     */
    public java.lang.String getMgmtData() {
        return mgmtData;
    }


    /**
     * Sets the mgmtData value for this KeyInfoType.
     * 
     * @param mgmtData
     */
    public void setMgmtData(java.lang.String mgmtData) {
        this.mgmtData = mgmtData;
    }


    /**
     * Gets the _any value for this KeyInfoType.
     * 
     * @return _any
     */
    public org.apache.axis.message.MessageElement [] get_any() {
        return _any;
    }


    /**
     * Sets the _any value for this KeyInfoType.
     * 
     * @param _any
     */
    public void set_any(org.apache.axis.message.MessageElement [] _any) {
        this._any = _any;
    }


    /**
     * Gets the id value for this KeyInfoType.
     * 
     * @return id
     */
    public org.apache.axis.types.Id getId() {
        return id;
    }


    /**
     * Sets the id value for this KeyInfoType.
     * 
     * @param id
     */
    public void setId(org.apache.axis.types.Id id) {
        this.id = id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof KeyInfoType)) return false;
        KeyInfoType other = (KeyInfoType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.keyName==null && other.getKeyName()==null) || 
             (this.keyName!=null &&
              this.keyName.equals(other.getKeyName()))) &&
            ((this.keyValue==null && other.getKeyValue()==null) || 
             (this.keyValue!=null &&
              this.keyValue.equals(other.getKeyValue()))) &&
            ((this.retrievalMethod==null && other.getRetrievalMethod()==null) || 
             (this.retrievalMethod!=null &&
              this.retrievalMethod.equals(other.getRetrievalMethod()))) &&
            ((this.x509Data==null && other.getX509Data()==null) || 
             (this.x509Data!=null &&
              this.x509Data.equals(other.getX509Data()))) &&
            ((this.PGPData==null && other.getPGPData()==null) || 
             (this.PGPData!=null &&
              this.PGPData.equals(other.getPGPData()))) &&
            ((this.SPKIData==null && other.getSPKIData()==null) || 
             (this.SPKIData!=null &&
              this.SPKIData.equals(other.getSPKIData()))) &&
            ((this.mgmtData==null && other.getMgmtData()==null) || 
             (this.mgmtData!=null &&
              this.mgmtData.equals(other.getMgmtData()))) &&
            ((this._any==null && other.get_any()==null) || 
             (this._any!=null &&
              java.util.Arrays.equals(this._any, other.get_any()))) &&
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
        if (getKeyName() != null) {
            _hashCode += getKeyName().hashCode();
        }
        if (getKeyValue() != null) {
            _hashCode += getKeyValue().hashCode();
        }
        if (getRetrievalMethod() != null) {
            _hashCode += getRetrievalMethod().hashCode();
        }
        if (getX509Data() != null) {
            _hashCode += getX509Data().hashCode();
        }
        if (getPGPData() != null) {
            _hashCode += getPGPData().hashCode();
        }
        if (getSPKIData() != null) {
            _hashCode += getSPKIData().hashCode();
        }
        if (getMgmtData() != null) {
            _hashCode += getMgmtData().hashCode();
        }
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
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(KeyInfoType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "KeyInfoType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("id");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Id"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "ID"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("keyName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "KeyName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("keyValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "KeyValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "KeyValueType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retrievalMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "RetrievalMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "RetrievalMethodType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("x509Data");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "X509Data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "X509DataType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PGPData");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "PGPData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "PGPDataType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SPKIData");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SPKIData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SPKIDataType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mgmtData");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "MgmtData"));
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
