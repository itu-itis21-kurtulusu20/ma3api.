/**
 * MSS_ProfileQueryBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.turkcelltech.mobilesignature.validation.soap;

public class MSS_ProfileQueryBindingStub extends org.apache.axis.client.Stub implements com.turkcelltech.mobilesignature.validation.soap.MSS_ProfileQueryPortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[1];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("MSS_ProfileQuery");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "MSS_ProfileReq"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_ProfileReqType"), org.etsi.uri.TS102204.v1_1_2.MSS_ProfileReqType.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_ProfileRespType"));
        oper.setReturnClass(org.etsi.uri.TS102204.v1_1_2.MSS_ProfileRespType.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "MSS_ProfileResp"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

    }

    public MSS_ProfileQueryBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public MSS_ProfileQueryBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public MSS_ProfileQueryBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MessageAbstractType>AP_Info");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeAP_Info.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MessageAbstractType>MSSP_Info");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MessageAbstractTypeMSSP_Info.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MSS_HandshakeReqType>Certificates");
            cachedSerQNames.add(qName);
            cls = byte[][].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary");
            qName2 = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Certificate");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MSS_HandshakeReqType>RootCAs");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "DN");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MSS_HandshakeReqType>SecureMethods");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MSS_HandshakeReqTypeSecureMethods.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MSS_HandshakeReqType>SignatureAlgList");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MssURIType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "mssURIType");
            qName2 = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Algorithm");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MSS_HandshakeRespType>MatchingAPCertificates");
            cachedSerQNames.add(qName);
            cls = byte[][].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary");
            qName2 = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Certificate");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MSS_HandshakeRespType>MatchingMSSPCertificates");
            cachedSerQNames.add(qName);
            cls = byte[][].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary");
            qName2 = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Certificate");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MSS_HandshakeRespType>MatchingSigAlgList");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MssURIType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "mssURIType");
            qName2 = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Algorithm");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MSS_HandshakeRespType>SecureMethods");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MSS_HandshakeRespTypeSecureMethods.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MSS_MessageSignature");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MSS_MessageSignature.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", ">MSS_SignatureReqType>AdditionalServices");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.AdditionalServiceType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "AdditionalServiceType");
            qName2 = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "Service");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "AdditionalServiceType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.AdditionalServiceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "DataType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.DataType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "DigestAlgAndValueType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.DigestAlgAndValueType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "KeyReferenceType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.KeyReferenceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MeshMemberType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MeshMemberType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MessageAbstractType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MessageAbstractType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MessagingModeType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MessagingModeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MobileUserType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MobileUserType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_HandshakeReqType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MSS_HandshakeReqType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_HandshakeRespType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MSS_HandshakeRespType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_ProfileReqType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MSS_ProfileReqType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_ProfileRespType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MSS_ProfileRespType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_ReceiptReqType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MSS_ReceiptReqType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_ReceiptRespType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MSS_ReceiptRespType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_RegistrationReqType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MSS_RegistrationReqType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_RegistrationRespType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MSS_RegistrationRespType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_SignatureReqType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MSS_SignatureReqType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_SignatureRespType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MSS_SignatureRespType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_StatusReqType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MSS_StatusReqType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_StatusRespType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MSS_StatusRespType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "mssURIType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.MssURIType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "SignatureProfileComparisonType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.SignatureProfileComparisonType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "SignatureType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.SignatureType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "StatusCodeType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.StatusCodeType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "StatusDetailType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.StatusDetailType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://uri.etsi.org/TS102204/v1.1.2#", "StatusType");
            cachedSerQNames.add(qName);
            cls = org.etsi.uri.TS102204.v1_1_2.StatusType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "CanonicalizationMethodType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.CanonicalizationMethodType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "CryptoBinary");
            cachedSerQNames.add(qName);
            cls = byte[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "DigestMethodType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.DigestMethodType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "DigestValueType");
            cachedSerQNames.add(qName);
            cls = byte[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "DSAKeyValueType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.DSAKeyValueType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "HMACOutputLengthType");
            cachedSerQNames.add(qName);
            cls = java.math.BigInteger.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "KeyInfoType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.KeyInfoType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "KeyValueType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.KeyValueType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "ManifestType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.ReferenceType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "Reference");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "ObjectType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.ObjectType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "PGPDataType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.PGPDataType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "ReferenceType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.ReferenceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "RetrievalMethodType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.RetrievalMethodType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "RSAKeyValueType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.RSAKeyValueType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignatureMethodType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.SignatureMethodType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignaturePropertiesType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.SignaturePropertyType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignatureProperty");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignaturePropertyType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.SignaturePropertyType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignatureType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.SignatureType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignatureValueType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.SignatureValueType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SignedInfoType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.SignedInfoType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "SPKIDataType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.SPKIDataType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "TransformsType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.TransformType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "Transform");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "TransformType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.TransformType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "X509DataType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.X509DataType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "X509IssuerSerialType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.X509IssuerSerialType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", ">ReferenceList");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2001._04.xmlenc.ReferenceList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "AgreementMethodType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2001._04.xmlenc.AgreementMethodType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "CipherDataType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2001._04.xmlenc.CipherDataType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "CipherReferenceType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2001._04.xmlenc.CipherReferenceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "EncryptedDataType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2001._04.xmlenc.EncryptedDataType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "EncryptedKeyType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2001._04.xmlenc.EncryptedKeyType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "EncryptedType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2001._04.xmlenc.EncryptedType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "EncryptionMethodType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2001._04.xmlenc.EncryptionMethodType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "EncryptionPropertiesType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2001._04.xmlenc.EncryptionPropertyType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "EncryptionProperty");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "EncryptionPropertyType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2001._04.xmlenc.EncryptionPropertyType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "KeySizeType");
            cachedSerQNames.add(qName);
            cls = java.math.BigInteger.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "ReferenceType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2001._04.xmlenc.ReferenceType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/04/xmlenc#", "TransformsType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2000._09.xmldsig.TransformType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2000/09/xmldsig#", "Transform");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "Body");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2003._05.soap_envelope.Body.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "detail");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2003._05.soap_envelope.Detail.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "Envelope");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2003._05.soap_envelope.Envelope.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "Fault");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2003._05.soap_envelope.Fault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "faultcode");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2003._05.soap_envelope.Faultcode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "faultcodeEnum");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2003._05.soap_envelope.FaultcodeEnum.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "faultreason");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2003._05.soap_envelope.Reasontext[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "reasontext");
            qName2 = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "Text");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "Header");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2003._05.soap_envelope.Header.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "NotUnderstoodType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2003._05.soap_envelope.NotUnderstoodType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "reasontext");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2003._05.soap_envelope.Reasontext.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "subcode");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2003._05.soap_envelope.Subcode.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "SupportedEnvType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2003._05.soap_envelope.SupportedEnvType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "UpgradeType");
            cachedSerQNames.add(qName);
            cls = org.w3.www._2003._05.soap_envelope.SupportedEnvType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "SupportedEnvType");
            qName2 = new javax.xml.namespace.QName("http://www.w3.org/2003/05/soap-envelope", "SupportedEnvelope");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (Exception e) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", e);
        }
    }

    public org.etsi.uri.TS102204.v1_1_2.MSS_ProfileRespType MSS_ProfileQuery(org.etsi.uri.TS102204.v1_1_2.MSS_ProfileReqType MSS_ProfileReq) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("tns:MSS_ProfileQuery");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP12_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "MSS_ProfileQuery"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {MSS_ProfileReq});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.etsi.uri.TS102204.v1_1_2.MSS_ProfileRespType) _resp;
            } catch (Exception e) {
                return (org.etsi.uri.TS102204.v1_1_2.MSS_ProfileRespType) org.apache.axis.utils.JavaUtils.convert(_resp, org.etsi.uri.TS102204.v1_1_2.MSS_ProfileRespType.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
