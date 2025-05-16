/**
 * X509DataType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2000._09.xmldsig;

public class X509DataType  implements java.io.Serializable, org.apache.axis.encoding.AnyContentType {
    private org.w3.www._2000._09.xmldsig.X509IssuerSerialType x509IssuerSerial;

    private byte[] x509SKI;

    private java.lang.String x509SubjectName;

    private byte[] x509Certificate;

    private byte[] x509CRL;

    private org.apache.axis.message.MessageElement [] _any;

    public X509DataType() {
    }

    public X509DataType(
           org.w3.www._2000._09.xmldsig.X509IssuerSerialType x509IssuerSerial,
           byte[] x509SKI,
           java.lang.String x509SubjectName,
           byte[] x509Certificate,
           byte[] x509CRL,
           org.apache.axis.message.MessageElement [] _any) {
           this.x509IssuerSerial = x509IssuerSerial;
           this.x509SKI = x509SKI;
           this.x509SubjectName = x509SubjectName;
           this.x509Certificate = x509Certificate;
           this.x509CRL = x509CRL;
           this._any = _any;
    }


    /**
     * Gets the x509IssuerSerial value for this X509DataType.
     * 
     * @return x509IssuerSerial
     */
    public org.w3.www._2000._09.xmldsig.X509IssuerSerialType getX509IssuerSerial() {
        return x509IssuerSerial;
    }


    /**
     * Sets the x509IssuerSerial value for this X509DataType.
     * 
     * @param x509IssuerSerial
     */
    public void setX509IssuerSerial(org.w3.www._2000._09.xmldsig.X509IssuerSerialType x509IssuerSerial) {
        this.x509IssuerSerial = x509IssuerSerial;
    }


    /**
     * Gets the x509SKI value for this X509DataType.
     * 
     * @return x509SKI
     */
    public byte[] getX509SKI() {
        return x509SKI;
    }


    /**
     * Sets the x509SKI value for this X509DataType.
     * 
     * @param x509SKI
     */
    public void setX509SKI(byte[] x509SKI) {
        this.x509SKI = x509SKI;
    }


    /**
     * Gets the x509SubjectName value for this X509DataType.
     * 
     * @return x509SubjectName
     */
    public java.lang.String getX509SubjectName() {
        return x509SubjectName;
    }


    /**
     * Sets the x509SubjectName value for this X509DataType.
     * 
     * @param x509SubjectName
     */
    public void setX509SubjectName(java.lang.String x509SubjectName) {
        this.x509SubjectName = x509SubjectName;
    }


    /**
     * Gets the x509Certificate value for this X509DataType.
     * 
     * @return x509Certificate
     */
    public byte[] getX509Certificate() {
        return x509Certificate;
    }


    /**
     * Sets the x509Certificate value for this X509DataType.
     * 
     * @param x509Certificate
     */
    public void setX509Certificate(byte[] x509Certificate) {
        this.x509Certificate = x509Certificate;
    }


    /**
     * Gets the x509CRL value for this X509DataType.
     * 
     * @return x509CRL
     */
    public byte[] getX509CRL() {
        return x509CRL;
    }


    /**
     * Sets the x509CRL value for this X509DataType.
     * 
     * @param x509CRL
     */
    public void setX509CRL(byte[] x509CRL) {
        this.x509CRL = x509CRL;
    }


    /**
     * Gets the _any value for this X509DataType.
     * 
     * @return _any
     */
    public org.apache.axis.message.MessageElement [] get_any() {
        return _any;
    }


    /**
     * Sets the _any value for this X509DataType.
     * 
     * @param _any
     */
    public void set_any(org.apache.axis.message.MessageElement [] _any) {
        this._any = _any;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof X509DataType)) return false;
        X509DataType other = (X509DataType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.x509IssuerSerial==null && other.getX509IssuerSerial()==null) || 
             (this.x509IssuerSerial!=null &&
              this.x509IssuerSerial.equals(other.getX509IssuerSerial()))) &&
            ((this.x509SKI==null && other.getX509SKI()==null) || 
             (this.x509SKI!=null &&
              java.util.Arrays.equals(this.x509SKI, other.getX509SKI()))) &&
            ((this.x509SubjectName==null && other.getX509SubjectName()==null) || 
             (this.x509SubjectName!=null &&
              this.x509SubjectName.equals(other.getX509SubjectName()))) &&
            ((this.x509Certificate==null && other.getX509Certificate()==null) || 
             (this.x509Certificate!=null &&
              java.util.Arrays.equals(this.x509Certificate, other.getX509Certificate()))) &&
            ((this.x509CRL==null && other.getX509CRL()==null) || 
             (this.x509CRL!=null &&
              java.util.Arrays.equals(this.x509CRL, other.getX509CRL()))) &&
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
        if (getX509IssuerSerial() != null) {
            _hashCode += getX509IssuerSerial().hashCode();
        }
        if (getX509SKI() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getX509SKI());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getX509SKI(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getX509SubjectName() != null) {
            _hashCode += getX509SubjectName().hashCode();
        }
        if (getX509Certificate() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getX509Certificate());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getX509Certificate(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getX509CRL() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getX509CRL());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getX509CRL(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
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
        new org.apache.axis.description.TypeDesc(X509DataType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "X509DataType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("x509IssuerSerial");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "X509IssuerSerial"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "X509IssuerSerialType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("x509SKI");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "X509SKI"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("x509SubjectName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "X509SubjectName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("x509Certificate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "X509Certificate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("x509CRL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "X509CRL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
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
