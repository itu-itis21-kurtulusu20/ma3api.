/**
 * Fault.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.w3.www._2003._05.soap_envelope;


/**
 * Fault reporting structure
 */
public class Fault  implements java.io.Serializable {
    private org.w3.www._2003._05.soap_envelope.Faultcode code;

    private org.w3.www._2003._05.soap_envelope.Reasontext[] reason;

    private org.apache.axis.types.URI node;

    private org.apache.axis.types.URI role;

    private org.w3.www._2003._05.soap_envelope.Detail detail;

    public Fault() {
    }

    public Fault(
           org.w3.www._2003._05.soap_envelope.Faultcode code,
           org.w3.www._2003._05.soap_envelope.Reasontext[] reason,
           org.apache.axis.types.URI node,
           org.apache.axis.types.URI role,
           org.w3.www._2003._05.soap_envelope.Detail detail) {
           this.code = code;
           this.reason = reason;
           this.node = node;
           this.role = role;
           this.detail = detail;
    }


    /**
     * Gets the code value for this Fault.
     * 
     * @return code
     */
    public org.w3.www._2003._05.soap_envelope.Faultcode getCode() {
        return code;
    }


    /**
     * Sets the code value for this Fault.
     * 
     * @param code
     */
    public void setCode(org.w3.www._2003._05.soap_envelope.Faultcode code) {
        this.code = code;
    }


    /**
     * Gets the reason value for this Fault.
     * 
     * @return reason
     */
    public org.w3.www._2003._05.soap_envelope.Reasontext[] getReason() {
        return reason;
    }


    /**
     * Sets the reason value for this Fault.
     * 
     * @param reason
     */
    public void setReason(org.w3.www._2003._05.soap_envelope.Reasontext[] reason) {
        this.reason = reason;
    }


    /**
     * Gets the node value for this Fault.
     * 
     * @return node
     */
    public org.apache.axis.types.URI getNode() {
        return node;
    }


    /**
     * Sets the node value for this Fault.
     * 
     * @param node
     */
    public void setNode(org.apache.axis.types.URI node) {
        this.node = node;
    }


    /**
     * Gets the role value for this Fault.
     * 
     * @return role
     */
    public org.apache.axis.types.URI getRole() {
        return role;
    }


    /**
     * Sets the role value for this Fault.
     * 
     * @param role
     */
    public void setRole(org.apache.axis.types.URI role) {
        this.role = role;
    }


    /**
     * Gets the detail value for this Fault.
     * 
     * @return detail
     */
    public org.w3.www._2003._05.soap_envelope.Detail getDetail() {
        return detail;
    }


    /**
     * Sets the detail value for this Fault.
     * 
     * @param detail
     */
    public void setDetail(org.w3.www._2003._05.soap_envelope.Detail detail) {
        this.detail = detail;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Fault)) return false;
        Fault other = (Fault) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.code==null && other.getCode()==null) || 
             (this.code!=null &&
              this.code.equals(other.getCode()))) &&
            ((this.reason==null && other.getReason()==null) || 
             (this.reason!=null &&
              java.util.Arrays.equals(this.reason, other.getReason()))) &&
            ((this.node==null && other.getNode()==null) || 
             (this.node!=null &&
              this.node.equals(other.getNode()))) &&
            ((this.role==null && other.getRole()==null) || 
             (this.role!=null &&
              this.role.equals(other.getRole()))) &&
            ((this.detail==null && other.getDetail()==null) || 
             (this.detail!=null &&
              this.detail.equals(other.getDetail())));
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
        if (getCode() != null) {
            _hashCode += getCode().hashCode();
        }
        if (getReason() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getReason());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReason(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNode() != null) {
            _hashCode += getNode().hashCode();
        }
        if (getRole() != null) {
            _hashCode += getRole().hashCode();
        }
        if (getDetail() != null) {
            _hashCode += getDetail().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Fault.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "Fault"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("code");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "Code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "faultcode"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reason");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "Reason"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "reasontext"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "Text"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("node");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "Node"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("role");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "Role"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("detail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "Detail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "detail"));
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
