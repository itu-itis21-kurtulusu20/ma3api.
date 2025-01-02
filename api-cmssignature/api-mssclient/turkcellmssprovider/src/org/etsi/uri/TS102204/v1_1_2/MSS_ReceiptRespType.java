/**
 * MSS_ReceiptRespType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MSS_ReceiptRespType  extends org.etsi.uri.TS102204.v1_1_2.MessageAbstractType  implements java.io.Serializable {
    private org.etsi.uri.TS102204.v1_1_2.StatusType status;

    public MSS_ReceiptRespType() {
    }

    public MSS_ReceiptRespType(
           java.math.BigInteger majorVersion,
           java.math.BigInteger minorVersion,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeAP_Info AP_Info,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeMSSP_Info MSSP_Info,
           org.etsi.uri.TS102204.v1_1_2.StatusType status) {
        super(
            majorVersion,
            minorVersion,
            AP_Info,
            MSSP_Info);
        this.status = status;
    }


    /**
     * Gets the status value for this MSS_ReceiptRespType.
     * 
     * @return status
     */
    public org.etsi.uri.TS102204.v1_1_2.StatusType getStatus() {
        return status;
    }


    /**
     * Sets the status value for this MSS_ReceiptRespType.
     * 
     * @param status
     */
    public void setStatus(org.etsi.uri.TS102204.v1_1_2.StatusType status) {
        this.status = status;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MSS_ReceiptRespType)) return false;
        MSS_ReceiptRespType other = (MSS_ReceiptRespType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus())));
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
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MSS_ReceiptRespType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_ReceiptRespType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "StatusType"));
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
