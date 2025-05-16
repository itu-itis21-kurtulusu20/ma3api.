/**
 * MessageAbstractTypeAP_Info.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MessageAbstractTypeAP_Info  implements java.io.Serializable {
    private org.apache.axis.types.URI AP_ID;  // attribute

    private org.apache.axis.types.NCName AP_TransID;  // attribute

    private java.lang.String AP_PWD;  // attribute

    private java.util.Calendar instant;  // attribute

    private org.apache.axis.types.URI AP_URL;  // attribute

    public MessageAbstractTypeAP_Info() {
    }

    public MessageAbstractTypeAP_Info(
           org.apache.axis.types.URI AP_ID,
           org.apache.axis.types.NCName AP_TransID,
           java.lang.String AP_PWD,
           java.util.Calendar instant,
           org.apache.axis.types.URI AP_URL) {
           this.AP_ID = AP_ID;
           this.AP_TransID = AP_TransID;
           this.AP_PWD = AP_PWD;
           this.instant = instant;
           this.AP_URL = AP_URL;
    }


    /**
     * Gets the AP_ID value for this MessageAbstractTypeAP_Info.
     * 
     * @return AP_ID
     */
    public org.apache.axis.types.URI getAP_ID() {
        return AP_ID;
    }


    /**
     * Sets the AP_ID value for this MessageAbstractTypeAP_Info.
     * 
     * @param AP_ID
     */
    public void setAP_ID(org.apache.axis.types.URI AP_ID) {
        this.AP_ID = AP_ID;
    }


    /**
     * Gets the AP_TransID value for this MessageAbstractTypeAP_Info.
     * 
     * @return AP_TransID
     */
    public org.apache.axis.types.NCName getAP_TransID() {
        return AP_TransID;
    }


    /**
     * Sets the AP_TransID value for this MessageAbstractTypeAP_Info.
     * 
     * @param AP_TransID
     */
    public void setAP_TransID(org.apache.axis.types.NCName AP_TransID) {
        this.AP_TransID = AP_TransID;
    }


    /**
     * Gets the AP_PWD value for this MessageAbstractTypeAP_Info.
     * 
     * @return AP_PWD
     */
    public java.lang.String getAP_PWD() {
        return AP_PWD;
    }


    /**
     * Sets the AP_PWD value for this MessageAbstractTypeAP_Info.
     * 
     * @param AP_PWD
     */
    public void setAP_PWD(java.lang.String AP_PWD) {
        this.AP_PWD = AP_PWD;
    }


    /**
     * Gets the instant value for this MessageAbstractTypeAP_Info.
     * 
     * @return instant
     */
    public java.util.Calendar getInstant() {
        return instant;
    }


    /**
     * Sets the instant value for this MessageAbstractTypeAP_Info.
     * 
     * @param instant
     */
    public void setInstant(java.util.Calendar instant) {
        this.instant = instant;
    }


    /**
     * Gets the AP_URL value for this MessageAbstractTypeAP_Info.
     * 
     * @return AP_URL
     */
    public org.apache.axis.types.URI getAP_URL() {
        return AP_URL;
    }


    /**
     * Sets the AP_URL value for this MessageAbstractTypeAP_Info.
     * 
     * @param AP_URL
     */
    public void setAP_URL(org.apache.axis.types.URI AP_URL) {
        this.AP_URL = AP_URL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MessageAbstractTypeAP_Info)) return false;
        MessageAbstractTypeAP_Info other = (MessageAbstractTypeAP_Info) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.AP_ID==null && other.getAP_ID()==null) || 
             (this.AP_ID!=null &&
              this.AP_ID.equals(other.getAP_ID()))) &&
            ((this.AP_TransID==null && other.getAP_TransID()==null) || 
             (this.AP_TransID!=null &&
              this.AP_TransID.equals(other.getAP_TransID()))) &&
            ((this.AP_PWD==null && other.getAP_PWD()==null) || 
             (this.AP_PWD!=null &&
              this.AP_PWD.equals(other.getAP_PWD()))) &&
            ((this.instant==null && other.getInstant()==null) || 
             (this.instant!=null &&
              this.instant.equals(other.getInstant()))) &&
            ((this.AP_URL==null && other.getAP_URL()==null) || 
             (this.AP_URL!=null &&
              this.AP_URL.equals(other.getAP_URL())));
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
        if (getAP_ID() != null) {
            _hashCode += getAP_ID().hashCode();
        }
        if (getAP_TransID() != null) {
            _hashCode += getAP_TransID().hashCode();
        }
        if (getAP_PWD() != null) {
            _hashCode += getAP_PWD().hashCode();
        }
        if (getInstant() != null) {
            _hashCode += getInstant().hashCode();
        }
        if (getAP_URL() != null) {
            _hashCode += getAP_URL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MessageAbstractTypeAP_Info.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MessageAbstractType>AP_Info"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("AP_ID");
        attrField.setXmlName(new javax.xml.namespace.QName("", "AP_ID"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("AP_TransID");
        attrField.setXmlName(new javax.xml.namespace.QName("", "AP_TransID"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("AP_PWD");
        attrField.setXmlName(new javax.xml.namespace.QName("", "AP_PWD"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("instant");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Instant"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("AP_URL");
        attrField.setXmlName(new javax.xml.namespace.QName("", "AP_URL"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
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
