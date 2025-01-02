/**
 * MSS_ProfileReqType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MSS_ProfileReqType  extends org.etsi.uri.TS102204.v1_1_2.MessageAbstractType  implements java.io.Serializable {
    private org.etsi.uri.TS102204.v1_1_2.MobileUserType mobileUser;

    public MSS_ProfileReqType() {
    }

    public MSS_ProfileReqType(
           java.math.BigInteger majorVersion,
           java.math.BigInteger minorVersion,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeAP_Info AP_Info,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeMSSP_Info MSSP_Info,
           org.etsi.uri.TS102204.v1_1_2.MobileUserType mobileUser) {
        super(
            majorVersion,
            minorVersion,
            AP_Info,
            MSSP_Info);
        this.mobileUser = mobileUser;
    }


    /**
     * Gets the mobileUser value for this MSS_ProfileReqType.
     * 
     * @return mobileUser
     */
    public org.etsi.uri.TS102204.v1_1_2.MobileUserType getMobileUser() {
        return mobileUser;
    }


    /**
     * Sets the mobileUser value for this MSS_ProfileReqType.
     * 
     * @param mobileUser
     */
    public void setMobileUser(org.etsi.uri.TS102204.v1_1_2.MobileUserType mobileUser) {
        this.mobileUser = mobileUser;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MSS_ProfileReqType)) return false;
        MSS_ProfileReqType other = (MSS_ProfileReqType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.mobileUser==null && other.getMobileUser()==null) || 
             (this.mobileUser!=null &&
              this.mobileUser.equals(other.getMobileUser())));
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
        if (getMobileUser() != null) {
            _hashCode += getMobileUser().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MSS_ProfileReqType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_ProfileReqType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mobileUser");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MobileUser"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MobileUserType"));
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
