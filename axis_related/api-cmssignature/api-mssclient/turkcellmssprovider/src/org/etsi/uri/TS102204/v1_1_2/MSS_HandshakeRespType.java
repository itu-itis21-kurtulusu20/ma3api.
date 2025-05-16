/**
 * MSS_HandshakeRespType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.etsi.uri.TS102204.v1_1_2;

public class MSS_HandshakeRespType  extends org.etsi.uri.TS102204.v1_1_2.MessageAbstractType  implements java.io.Serializable {
    private org.etsi.uri.TS102204.v1_1_2.MSS_HandshakeRespTypeSecureMethods secureMethods;

    private byte[][] matchingMSSPCertificates;

    private byte[][] matchingAPCertificates;

    private org.etsi.uri.TS102204.v1_1_2.MssURIType[] matchingSigAlgList;

    private org.apache.axis.types.NCName MSSP_TransID;  // attribute

    public MSS_HandshakeRespType() {
    }

    public MSS_HandshakeRespType(
           java.math.BigInteger majorVersion,
           java.math.BigInteger minorVersion,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeAP_Info AP_Info,
           org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeMSSP_Info MSSP_Info,
           org.apache.axis.types.NCName MSSP_TransID,
           org.etsi.uri.TS102204.v1_1_2.MSS_HandshakeRespTypeSecureMethods secureMethods,
           byte[][] matchingMSSPCertificates,
           byte[][] matchingAPCertificates,
           org.etsi.uri.TS102204.v1_1_2.MssURIType[] matchingSigAlgList) {
        super(
            majorVersion,
            minorVersion,
            AP_Info,
            MSSP_Info);
        this.MSSP_TransID = MSSP_TransID;
        this.secureMethods = secureMethods;
        this.matchingMSSPCertificates = matchingMSSPCertificates;
        this.matchingAPCertificates = matchingAPCertificates;
        this.matchingSigAlgList = matchingSigAlgList;
    }


    /**
     * Gets the secureMethods value for this MSS_HandshakeRespType.
     * 
     * @return secureMethods
     */
    public org.etsi.uri.TS102204.v1_1_2.MSS_HandshakeRespTypeSecureMethods getSecureMethods() {
        return secureMethods;
    }


    /**
     * Sets the secureMethods value for this MSS_HandshakeRespType.
     * 
     * @param secureMethods
     */
    public void setSecureMethods(org.etsi.uri.TS102204.v1_1_2.MSS_HandshakeRespTypeSecureMethods secureMethods) {
        this.secureMethods = secureMethods;
    }


    /**
     * Gets the matchingMSSPCertificates value for this MSS_HandshakeRespType.
     * 
     * @return matchingMSSPCertificates
     */
    public byte[][] getMatchingMSSPCertificates() {
        return matchingMSSPCertificates;
    }


    /**
     * Sets the matchingMSSPCertificates value for this MSS_HandshakeRespType.
     * 
     * @param matchingMSSPCertificates
     */
    public void setMatchingMSSPCertificates(byte[][] matchingMSSPCertificates) {
        this.matchingMSSPCertificates = matchingMSSPCertificates;
    }


    /**
     * Gets the matchingAPCertificates value for this MSS_HandshakeRespType.
     * 
     * @return matchingAPCertificates
     */
    public byte[][] getMatchingAPCertificates() {
        return matchingAPCertificates;
    }


    /**
     * Sets the matchingAPCertificates value for this MSS_HandshakeRespType.
     * 
     * @param matchingAPCertificates
     */
    public void setMatchingAPCertificates(byte[][] matchingAPCertificates) {
        this.matchingAPCertificates = matchingAPCertificates;
    }


    /**
     * Gets the matchingSigAlgList value for this MSS_HandshakeRespType.
     * 
     * @return matchingSigAlgList
     */
    public org.etsi.uri.TS102204.v1_1_2.MssURIType[] getMatchingSigAlgList() {
        return matchingSigAlgList;
    }


    /**
     * Sets the matchingSigAlgList value for this MSS_HandshakeRespType.
     * 
     * @param matchingSigAlgList
     */
    public void setMatchingSigAlgList(org.etsi.uri.TS102204.v1_1_2.MssURIType[] matchingSigAlgList) {
        this.matchingSigAlgList = matchingSigAlgList;
    }


    /**
     * Gets the MSSP_TransID value for this MSS_HandshakeRespType.
     * 
     * @return MSSP_TransID
     */
    public org.apache.axis.types.NCName getMSSP_TransID() {
        return MSSP_TransID;
    }


    /**
     * Sets the MSSP_TransID value for this MSS_HandshakeRespType.
     * 
     * @param MSSP_TransID
     */
    public void setMSSP_TransID(org.apache.axis.types.NCName MSSP_TransID) {
        this.MSSP_TransID = MSSP_TransID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MSS_HandshakeRespType)) return false;
        MSS_HandshakeRespType other = (MSS_HandshakeRespType) obj;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.secureMethods==null && other.getSecureMethods()==null) || 
             (this.secureMethods!=null &&
              this.secureMethods.equals(other.getSecureMethods()))) &&
            ((this.matchingMSSPCertificates==null && other.getMatchingMSSPCertificates()==null) || 
             (this.matchingMSSPCertificates!=null &&
              java.util.Arrays.equals(this.matchingMSSPCertificates, other.getMatchingMSSPCertificates()))) &&
            ((this.matchingAPCertificates==null && other.getMatchingAPCertificates()==null) || 
             (this.matchingAPCertificates!=null &&
              java.util.Arrays.equals(this.matchingAPCertificates, other.getMatchingAPCertificates()))) &&
            ((this.matchingSigAlgList==null && other.getMatchingSigAlgList()==null) || 
             (this.matchingSigAlgList!=null &&
              java.util.Arrays.equals(this.matchingSigAlgList, other.getMatchingSigAlgList()))) &&
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
        if (getSecureMethods() != null) {
            _hashCode += getSecureMethods().hashCode();
        }
        if (getMatchingMSSPCertificates() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMatchingMSSPCertificates());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMatchingMSSPCertificates(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMatchingAPCertificates() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMatchingAPCertificates());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMatchingAPCertificates(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMatchingSigAlgList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMatchingSigAlgList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMatchingSigAlgList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMSSP_TransID() != null) {
            _hashCode += getMSSP_TransID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MSS_HandshakeRespType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_HandshakeRespType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("MSSP_TransID");
        attrField.setXmlName(new javax.xml.namespace.QName("", "MSSP_TransID"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "NCName"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("secureMethods");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "SecureMethods"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MSS_HandshakeRespType>SecureMethods"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matchingMSSPCertificates");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MatchingMSSPCertificates"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Certificate"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matchingAPCertificates");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MatchingAPCertificates"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Certificate"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matchingSigAlgList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MatchingSigAlgList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "mssURIType"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Algorithm"));
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
