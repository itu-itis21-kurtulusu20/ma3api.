/**
 * StatusType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class StatusType  implements java.io.Serializable {
    private org.etsi.uri.TS102204.v1_1_2.StatusCodeType statusCode;

    private java.lang.String statusMessage;

    private org.etsi.uri.TS102204.v1_1_2.StatusDetailType statusDetail;

    public StatusType() {
    }

    public StatusType(
           org.etsi.uri.TS102204.v1_1_2.StatusCodeType statusCode,
           java.lang.String statusMessage,
           org.etsi.uri.TS102204.v1_1_2.StatusDetailType statusDetail) {
           this.statusCode = statusCode;
           this.statusMessage = statusMessage;
           this.statusDetail = statusDetail;
    }


    /**
     * Gets the statusCode value for this StatusType.
     * 
     * @return statusCode
     */
    public org.etsi.uri.TS102204.v1_1_2.StatusCodeType getStatusCode() {
        return statusCode;
    }


    /**
     * Sets the statusCode value for this StatusType.
     * 
     * @param statusCode
     */
    public void setStatusCode(org.etsi.uri.TS102204.v1_1_2.StatusCodeType statusCode) {
        this.statusCode = statusCode;
    }


    /**
     * Gets the statusMessage value for this StatusType.
     * 
     * @return statusMessage
     */
    public java.lang.String getStatusMessage() {
        return statusMessage;
    }


    /**
     * Sets the statusMessage value for this StatusType.
     * 
     * @param statusMessage
     */
    public void setStatusMessage(java.lang.String statusMessage) {
        this.statusMessage = statusMessage;
    }


    /**
     * Gets the statusDetail value for this StatusType.
     * 
     * @return statusDetail
     */
    public org.etsi.uri.TS102204.v1_1_2.StatusDetailType getStatusDetail() {
        return statusDetail;
    }


    /**
     * Sets the statusDetail value for this StatusType.
     * 
     * @param statusDetail
     */
    public void setStatusDetail(org.etsi.uri.TS102204.v1_1_2.StatusDetailType statusDetail) {
        this.statusDetail = statusDetail;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StatusType)) return false;
        StatusType other = (StatusType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.statusCode==null && other.getStatusCode()==null) || 
             (this.statusCode!=null &&
              this.statusCode.equals(other.getStatusCode()))) &&
            ((this.statusMessage==null && other.getStatusMessage()==null) || 
             (this.statusMessage!=null &&
              this.statusMessage.equals(other.getStatusMessage()))) &&
            ((this.statusDetail==null && other.getStatusDetail()==null) || 
             (this.statusDetail!=null &&
              this.statusDetail.equals(other.getStatusDetail())));
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
        if (getStatusCode() != null) {
            _hashCode += getStatusCode().hashCode();
        }
        if (getStatusMessage() != null) {
            _hashCode += getStatusMessage().hashCode();
        }
        if (getStatusDetail() != null) {
            _hashCode += getStatusDetail().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StatusType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "StatusType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statusCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "StatusCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "StatusCodeType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statusMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "StatusMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statusDetail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "StatusDetail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "StatusDetailType"));
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
