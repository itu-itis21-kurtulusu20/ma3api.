/**
 * MssURIType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MssURIType  implements java.io.Serializable, org.apache.axis.encoding.AnyContentType {
    private org.apache.axis.types.URI mssURI;

    private org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType digestAlgAndValue;

    private org.apache.axis.message.MessageElement [] _any;

    public MssURIType() {
    }

    public MssURIType(
           org.apache.axis.types.URI mssURI,
           org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType digestAlgAndValue,
           org.apache.axis.message.MessageElement [] _any) {
           this.mssURI = mssURI;
           this.digestAlgAndValue = digestAlgAndValue;
           this._any = _any;
    }


    /**
     * Gets the mssURI value for this MssURIType.
     * 
     * @return mssURI
     */
    public org.apache.axis.types.URI getMssURI() {
        return mssURI;
    }


    /**
     * Sets the mssURI value for this MssURIType.
     * 
     * @param mssURI
     */
    public void setMssURI(org.apache.axis.types.URI mssURI) {
        this.mssURI = mssURI;
    }


    /**
     * Gets the digestAlgAndValue value for this MssURIType.
     * 
     * @return digestAlgAndValue
     */
    public org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType getDigestAlgAndValue() {
        return digestAlgAndValue;
    }


    /**
     * Sets the digestAlgAndValue value for this MssURIType.
     * 
     * @param digestAlgAndValue
     */
    public void setDigestAlgAndValue(org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType digestAlgAndValue) {
        this.digestAlgAndValue = digestAlgAndValue;
    }


    /**
     * Gets the _any value for this MssURIType.
     * 
     * @return _any
     */
    public org.apache.axis.message.MessageElement [] get_any() {
        return _any;
    }


    /**
     * Sets the _any value for this MssURIType.
     * 
     * @param _any
     */
    public void set_any(org.apache.axis.message.MessageElement [] _any) {
        this._any = _any;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MssURIType)) return false;
        MssURIType other = (MssURIType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.mssURI==null && other.getMssURI()==null) || 
             (this.mssURI!=null &&
              this.mssURI.equals(other.getMssURI()))) &&
            ((this.digestAlgAndValue==null && other.getDigestAlgAndValue()==null) || 
             (this.digestAlgAndValue!=null &&
              this.digestAlgAndValue.equals(other.getDigestAlgAndValue()))) &&
            ((this._any==null && other.get_any()==null) || 
             (this._any!=null &&
              java.util.Arrays.equals(this._any, other.get_any())));
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
        if (getMssURI() != null) {
            _hashCode += getMssURI().hashCode();
        }
        if (getDigestAlgAndValue() != null) {
            _hashCode += getDigestAlgAndValue().hashCode();
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MssURIType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "mssURIType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mssURI");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "mssURI"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("digestAlgAndValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "DigestAlgAndValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "DigestAlgAndValueType"));
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
