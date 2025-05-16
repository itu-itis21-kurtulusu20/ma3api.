/**
 * MSS_StatusReqType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MSS_StatusReqType  extends org.etsi.uri.TS102204.v1_1_2.MessageAbstractType  implements java.io.Serializable {
    private org.apache.axis.types.NCName MSSP_TransID;  // attribute

    public MSS_StatusReqType() {
    }

    public MSS_StatusReqType(
           java.math.BigInteger majorVersion,
           java.math.BigInteger minorVersion,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeAP_Info AP_Info,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeMSSP_Info MSSP_Info,
           org.apache.axis.types.NCName MSSP_TransID) {
        super(
            majorVersion,
            minorVersion,
            AP_Info,
            MSSP_Info);
        this.MSSP_TransID = MSSP_TransID;
    }


    /**
     * Gets the MSSP_TransID value for this MSS_StatusReqType.
     * 
     * @return MSSP_TransID
     */
    public org.apache.axis.types.NCName getMSSP_TransID() {
        return MSSP_TransID;
    }


    /**
     * Sets the MSSP_TransID value for this MSS_StatusReqType.
     * 
     * @param MSSP_TransID
     */
    public void setMSSP_TransID(org.apache.axis.types.NCName MSSP_TransID) {
        this.MSSP_TransID = MSSP_TransID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MSS_StatusReqType)) return false;
        MSS_StatusReqType other = (MSS_StatusReqType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.MSSP_TransID==null && other.getMSSP_TransID()==null) || 
             (this.MSSP_TransID!=null &&
              this.MSSP_TransID.equals(other.getMSSP_TransID())));
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
        if (getMSSP_TransID() != null) {
            _hashCode += getMSSP_TransID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MSS_StatusReqType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_StatusReqType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("MSSP_TransID");
        attrField.setXmlName(new javax.xml.namespace.QName("", "MSSP_TransID"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
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
