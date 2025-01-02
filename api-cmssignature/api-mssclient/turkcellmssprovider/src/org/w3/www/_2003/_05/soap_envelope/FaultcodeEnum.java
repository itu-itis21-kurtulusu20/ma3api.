/**
 * FaultcodeEnum.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2003._05.soap_envelope;

public class FaultcodeEnum implements java.io.Serializable {
    private javax.xml.namespace.QName _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected FaultcodeEnum(javax.xml.namespace.QName value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final javax.xml.namespace.QName _value1 = javax.xml.namespace.QName.valueOf("{http://www.w3.org/2003/05/soap-envelope}DataEncodingUnknown");
    public static final javax.xml.namespace.QName _value2 = javax.xml.namespace.QName.valueOf("{http://www.w3.org/2003/05/soap-envelope}MustUnderstand");
    public static final javax.xml.namespace.QName _value3 = javax.xml.namespace.QName.valueOf("{http://www.w3.org/2003/05/soap-envelope}Receiver");
    public static final javax.xml.namespace.QName _value4 = javax.xml.namespace.QName.valueOf("{http://www.w3.org/2003/05/soap-envelope}Sender");
    public static final javax.xml.namespace.QName _value5 = javax.xml.namespace.QName.valueOf("{http://www.w3.org/2003/05/soap-envelope}VersionMismatch");
    public static final FaultcodeEnum value1 = new FaultcodeEnum(_value1);
    public static final FaultcodeEnum value2 = new FaultcodeEnum(_value2);
    public static final FaultcodeEnum value3 = new FaultcodeEnum(_value3);
    public static final FaultcodeEnum value4 = new FaultcodeEnum(_value4);
    public static final FaultcodeEnum value5 = new FaultcodeEnum(_value5);
    public javax.xml.namespace.QName getValue() { return _value_;}
    public static FaultcodeEnum fromValue(javax.xml.namespace.QName value)
          throws java.lang.IllegalArgumentException {
        FaultcodeEnum enumeration = (FaultcodeEnum)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static FaultcodeEnum fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        try {
            return fromValue(javax.xml.namespace.QName.valueOf(value));
        } catch (Exception e) {
            throw new java.lang.IllegalArgumentException();
        }
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_.toString();}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FaultcodeEnum.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "faultcodeEnum"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
