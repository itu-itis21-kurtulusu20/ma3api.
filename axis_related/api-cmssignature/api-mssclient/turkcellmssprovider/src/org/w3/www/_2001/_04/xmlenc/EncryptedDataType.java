/**
 * EncryptedDataType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2001._04.xmlenc;

public class EncryptedDataType  extends org.w3.www._2001._04.xmlenc.EncryptedType  implements java.io.Serializable {
    public EncryptedDataType() {
    }

    public EncryptedDataType(
           org.apache.axis.types.Id id,
           org.apache.axis.types.URI type,
           java.lang.String mimeType,
           org.apache.axis.types.URI encoding,
           org.w3.www._2001._04.xmlenc.EncryptionMethodType encryptionMethod,
           org.w3.www._2000._09.xmldsig.KeyInfoType keyInfo,
           org.w3.www._2001._04.xmlenc.CipherDataType cipherData,
           org.w3.www._2001._04.xmlenc.EncryptionPropertyType[] encryptionProperties) {
        super(
            id,
            type,
            mimeType,
            encoding,
            encryptionMethod,
            keyInfo,
            cipherData,
            encryptionProperties);
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EncryptedDataType)) return false;
        EncryptedDataType other = (EncryptedDataType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj);
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EncryptedDataType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "EncryptedDataType"));
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
