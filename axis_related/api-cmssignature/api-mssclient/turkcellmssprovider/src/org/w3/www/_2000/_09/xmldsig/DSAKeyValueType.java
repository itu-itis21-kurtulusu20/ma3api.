/**
 * DSAKeyValueType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2000._09.xmldsig;

public class DSAKeyValueType  implements java.io.Serializable {
    private byte[] p;

    private byte[] q;

    private byte[] g;

    private byte[] y;

    private byte[] j;

    private byte[] seed;

    private byte[] pgenCounter;

    public DSAKeyValueType() {
    }

    public DSAKeyValueType(
           byte[] p,
           byte[] q,
           byte[] g,
           byte[] y,
           byte[] j,
           byte[] seed,
           byte[] pgenCounter) {
           this.p = p;
           this.q = q;
           this.g = g;
           this.y = y;
           this.j = j;
           this.seed = seed;
           this.pgenCounter = pgenCounter;
    }


    /**
     * Gets the p value for this DSAKeyValueType.
     * 
     * @return p
     */
    public byte[] getP() {
        return p;
    }


    /**
     * Sets the p value for this DSAKeyValueType.
     * 
     * @param p
     */
    public void setP(byte[] p) {
        this.p = p;
    }


    /**
     * Gets the q value for this DSAKeyValueType.
     * 
     * @return q
     */
    public byte[] getQ() {
        return q;
    }


    /**
     * Sets the q value for this DSAKeyValueType.
     * 
     * @param q
     */
    public void setQ(byte[] q) {
        this.q = q;
    }


    /**
     * Gets the g value for this DSAKeyValueType.
     * 
     * @return g
     */
    public byte[] getG() {
        return g;
    }


    /**
     * Sets the g value for this DSAKeyValueType.
     * 
     * @param g
     */
    public void setG(byte[] g) {
        this.g = g;
    }


    /**
     * Gets the y value for this DSAKeyValueType.
     * 
     * @return y
     */
    public byte[] getY() {
        return y;
    }


    /**
     * Sets the y value for this DSAKeyValueType.
     * 
     * @param y
     */
    public void setY(byte[] y) {
        this.y = y;
    }


    /**
     * Gets the j value for this DSAKeyValueType.
     * 
     * @return j
     */
    public byte[] getJ() {
        return j;
    }


    /**
     * Sets the j value for this DSAKeyValueType.
     * 
     * @param j
     */
    public void setJ(byte[] j) {
        this.j = j;
    }


    /**
     * Gets the seed value for this DSAKeyValueType.
     * 
     * @return seed
     */
    public byte[] getSeed() {
        return seed;
    }


    /**
     * Sets the seed value for this DSAKeyValueType.
     * 
     * @param seed
     */
    public void setSeed(byte[] seed) {
        this.seed = seed;
    }


    /**
     * Gets the pgenCounter value for this DSAKeyValueType.
     * 
     * @return pgenCounter
     */
    public byte[] getPgenCounter() {
        return pgenCounter;
    }


    /**
     * Sets the pgenCounter value for this DSAKeyValueType.
     * 
     * @param pgenCounter
     */
    public void setPgenCounter(byte[] pgenCounter) {
        this.pgenCounter = pgenCounter;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DSAKeyValueType)) return false;
        DSAKeyValueType other = (DSAKeyValueType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.p==null && other.getP()==null) || 
             (this.p!=null &&
              java.util.Arrays.equals(this.p, other.getP()))) &&
            ((this.q==null && other.getQ()==null) || 
             (this.q!=null &&
              java.util.Arrays.equals(this.q, other.getQ()))) &&
            ((this.g==null && other.getG()==null) || 
             (this.g!=null &&
              java.util.Arrays.equals(this.g, other.getG()))) &&
            ((this.y==null && other.getY()==null) || 
             (this.y!=null &&
              java.util.Arrays.equals(this.y, other.getY()))) &&
            ((this.j==null && other.getJ()==null) || 
             (this.j!=null &&
              java.util.Arrays.equals(this.j, other.getJ()))) &&
            ((this.seed==null && other.getSeed()==null) || 
             (this.seed!=null &&
              java.util.Arrays.equals(this.seed, other.getSeed()))) &&
            ((this.pgenCounter==null && other.getPgenCounter()==null) || 
             (this.pgenCounter!=null &&
              java.util.Arrays.equals(this.pgenCounter, other.getPgenCounter())));
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
        if (getP() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getP());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getP(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getQ() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getQ());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getQ(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getG() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getG());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getG(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getY() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getY());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getY(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getJ() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getJ());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getJ(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSeed() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSeed());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSeed(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPgenCounter() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPgenCounter());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPgenCounter(), i);
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
        new org.apache.axis.description.TypeDesc(DSAKeyValueType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "DSAKeyValueType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("p");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "P"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("q");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "Q"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("g");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "G"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("y");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "Y"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("j");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "J"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("seed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "Seed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pgenCounter");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "PgenCounter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
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
