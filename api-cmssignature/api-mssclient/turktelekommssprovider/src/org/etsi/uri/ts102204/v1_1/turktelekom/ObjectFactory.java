
package org.etsi.uri.ts102204.v1_1.turktelekom;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.etsi.uri.ts102204.v1_1 package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _MSSRegistrationReq_QNAME = new QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_RegistrationReq");
    private final static QName _MSSProfileResp_QNAME = new QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_ProfileResp");
    private final static QName _MSSStatusResp_QNAME = new QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_StatusResp");
    private final static QName _MSSProfileReq_QNAME = new QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_ProfileReq");
    private final static QName _MSSReceiptResp_QNAME = new QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_ReceiptResp");
    private final static QName _MSSRegistrationResp_QNAME = new QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_RegistrationResp");
    private final static QName _MSSSignatureResp_QNAME = new QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_SignatureResp");
    private final static QName _MSSHandshakeReq_QNAME = new QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_HandshakeReq");
    private final static QName _MSSStatusReq_QNAME = new QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_StatusReq");
    private final static QName _MSSSignatureReq_QNAME = new QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_SignatureReq");
    private final static QName _MSSHandshakeResp_QNAME = new QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_HandshakeResp");
    private final static QName _MSSReceiptReq_QNAME = new QName("http://uri.etsi.org/TS102204/v1.1.2#", "MSS_ReceiptReq");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.etsi.uri.ts102204.v1_1
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MSSRegistrationRespType }
     *
     */
    public MSSRegistrationRespType createMSSRegistrationRespType() {
        return new MSSRegistrationRespType();
    }

    /**
     * Create an instance of {@link MSSRegistrationReqType }
     *
     */
    public MSSRegistrationReqType createMSSRegistrationReqType() {
        return new MSSRegistrationReqType();
    }

    /**
     * Create an instance of {@link MSSReceiptReqType }
     *
     */
    public MSSReceiptReqType createMSSReceiptReqType() {
        return new MSSReceiptReqType();
    }

    /**
     * Create an instance of {@link MSSSignatureReqType.AdditionalServices }
     *
     */
    public MSSSignatureReqType.AdditionalServices createMSSSignatureReqTypeAdditionalServices() {
        return new MSSSignatureReqType.AdditionalServices();
    }

    /**
     * Create an instance of {@link AdditionalServiceType }
     *
     */
    public AdditionalServiceType createAdditionalServiceType() {
        return new AdditionalServiceType();
    }

    /**
     * Create an instance of {@link MSSHandshakeReqType.Certificates }
     *
     */
    public MSSHandshakeReqType.Certificates createMSSHandshakeReqTypeCertificates() {
        return new MSSHandshakeReqType.Certificates();
    }

    /**
     * Create an instance of {@link MSSHandshakeReqType.SecureMethods }
     *
     */
    public MSSHandshakeReqType.SecureMethods createMSSHandshakeReqTypeSecureMethods() {
        return new MSSHandshakeReqType.SecureMethods();
    }

    /**
     * Create an instance of {@link KeyReferenceType }
     *
     */
    public KeyReferenceType createKeyReferenceType() {
        return new KeyReferenceType();
    }

    /**
     * Create an instance of {@link DataType }
     *
     */
    public DataType createDataType() {
        return new DataType();
    }

    /**
     * Create an instance of {@link MSSStatusReqType }
     *
     */
    public MSSStatusReqType createMSSStatusReqType() {
        return new MSSStatusReqType();
    }

    /**
     * Create an instance of {@link MSSHandshakeReqType.RootCAs }
     *
     */
    public MSSHandshakeReqType.RootCAs createMSSHandshakeReqTypeRootCAs() {
        return new MSSHandshakeReqType.RootCAs();
    }

    /**
     * Create an instance of {@link MSSStatusRespType }
     *
     */
    public MSSStatusRespType createMSSStatusRespType() {
        return new MSSStatusRespType();
    }

    /**
     * Create an instance of {@link MSSProfileRespType }
     *
     */
    public MSSProfileRespType createMSSProfileRespType() {
        return new MSSProfileRespType();
    }

    /**
     * Create an instance of {@link MSSHandshakeRespType.MatchingMSSPCertificates }
     *
     */
    public MSSHandshakeRespType.MatchingMSSPCertificates createMSSHandshakeRespTypeMatchingMSSPCertificates() {
        return new MSSHandshakeRespType.MatchingMSSPCertificates();
    }

    /**
     * Create an instance of {@link MSSProfileReqType }
     *
     */
    public MSSProfileReqType createMSSProfileReqType() {
        return new MSSProfileReqType();
    }

    /**
     * Create an instance of {@link StatusType }
     *
     */
    public StatusType createStatusType() {
        return new StatusType();
    }

    /**
     * Create an instance of {@link MSSMessageSignature }
     *
     */
    public MSSMessageSignature createMSSMessageSignature() {
        return new MSSMessageSignature();
    }

    /**
     * Create an instance of {@link MSSSignatureReqType }
     *
     */
    public MSSSignatureReqType createMSSSignatureReqType() {
        return new MSSSignatureReqType();
    }

    /**
     * Create an instance of {@link MSSReceiptRespType }
     *
     */
    public MSSReceiptRespType createMSSReceiptRespType() {
        return new MSSReceiptRespType();
    }

    /**
     * Create an instance of {@link MSSHandshakeReqType }
     *
     */
    public MSSHandshakeReqType createMSSHandshakeReqType() {
        return new MSSHandshakeReqType();
    }

    /**
     * Create an instance of {@link MSSHandshakeRespType.MatchingAPCertificates }
     *
     */
    public MSSHandshakeRespType.MatchingAPCertificates createMSSHandshakeRespTypeMatchingAPCertificates() {
        return new MSSHandshakeRespType.MatchingAPCertificates();
    }

    /**
     * Create an instance of {@link MeshMemberType }
     *
     */
    public MeshMemberType createMeshMemberType() {
        return new MeshMemberType();
    }

    /**
     * Create an instance of {@link MobileUserType }
     *
     */
    public MobileUserType createMobileUserType() {
        return new MobileUserType();
    }

    /**
     * Create an instance of {@link SignatureType }
     *
     */
    public SignatureType createSignatureType() {
        return new SignatureType();
    }

    /**
     * Create an instance of {@link MSSHandshakeRespType.MatchingSigAlgList }
     *
     */
    public MSSHandshakeRespType.MatchingSigAlgList createMSSHandshakeRespTypeMatchingSigAlgList() {
        return new MSSHandshakeRespType.MatchingSigAlgList();
    }

    /**
     * Create an instance of {@link MSSHandshakeRespType }
     *
     */
    public MSSHandshakeRespType createMSSHandshakeRespType() {
        return new MSSHandshakeRespType();
    }

    /**
     * Create an instance of {@link MessageAbstractType.MSSPInfo }
     *
     */
    public MessageAbstractType.MSSPInfo createMessageAbstractTypeMSSPInfo() {
        return new MessageAbstractType.MSSPInfo();
    }

    /**
     * Create an instance of {@link StatusCodeType }
     *
     */
    public StatusCodeType createStatusCodeType() {
        return new StatusCodeType();
    }

    /**
     * Create an instance of {@link MessageAbstractType.APInfo }
     *
     */
    public MessageAbstractType.APInfo createMessageAbstractTypeAPInfo() {
        return new MessageAbstractType.APInfo();
    }

    /**
     * Create an instance of {@link StatusDetailType }
     *
     */
    public StatusDetailType createStatusDetailType() {
        return new StatusDetailType();
    }

    /**
     * Create an instance of {@link DigestAlgAndValueType }
     *
     */
    public DigestAlgAndValueType createDigestAlgAndValueType() {
        return new DigestAlgAndValueType();
    }

    /**
     * Create an instance of {@link MSSHandshakeRespType.SecureMethods }
     *
     */
    public MSSHandshakeRespType.SecureMethods createMSSHandshakeRespTypeSecureMethods() {
        return new MSSHandshakeRespType.SecureMethods();
    }

    /**
     * Create an instance of {@link MSSSignatureRespType }
     *
     */
    public MSSSignatureRespType createMSSSignatureRespType() {
        return new MSSSignatureRespType();
    }

    /**
     * Create an instance of {@link MSSHandshakeReqType.SignatureAlgList }
     *
     */
    public MSSHandshakeReqType.SignatureAlgList createMSSHandshakeReqTypeSignatureAlgList() {
        return new MSSHandshakeReqType.SignatureAlgList();
    }

    /**
     * Create an instance of {@link MssURIType }
     *
     */
    public MssURIType createMssURIType() {
        return new MssURIType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MSSRegistrationReqType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/TS102204/v1.1.2#", name = "MSS_RegistrationReq")
    public JAXBElement<MSSRegistrationReqType> createMSSRegistrationReq(MSSRegistrationReqType value) {
        return new JAXBElement<MSSRegistrationReqType>(_MSSRegistrationReq_QNAME, MSSRegistrationReqType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MSSProfileRespType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/TS102204/v1.1.2#", name = "MSS_ProfileResp")
    public JAXBElement<MSSProfileRespType> createMSSProfileResp(MSSProfileRespType value) {
        return new JAXBElement<MSSProfileRespType>(_MSSProfileResp_QNAME, MSSProfileRespType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MSSStatusRespType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/TS102204/v1.1.2#", name = "MSS_StatusResp")
    public JAXBElement<MSSStatusRespType> createMSSStatusResp(MSSStatusRespType value) {
        return new JAXBElement<MSSStatusRespType>(_MSSStatusResp_QNAME, MSSStatusRespType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MSSProfileReqType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/TS102204/v1.1.2#", name = "MSS_ProfileReq")
    public JAXBElement<MSSProfileReqType> createMSSProfileReq(MSSProfileReqType value) {
        return new JAXBElement<MSSProfileReqType>(_MSSProfileReq_QNAME, MSSProfileReqType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MSSReceiptRespType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/TS102204/v1.1.2#", name = "MSS_ReceiptResp")
    public JAXBElement<MSSReceiptRespType> createMSSReceiptResp(MSSReceiptRespType value) {
        return new JAXBElement<MSSReceiptRespType>(_MSSReceiptResp_QNAME, MSSReceiptRespType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MSSRegistrationRespType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/TS102204/v1.1.2#", name = "MSS_RegistrationResp")
    public JAXBElement<MSSRegistrationRespType> createMSSRegistrationResp(MSSRegistrationRespType value) {
        return new JAXBElement<MSSRegistrationRespType>(_MSSRegistrationResp_QNAME, MSSRegistrationRespType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MSSSignatureRespType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/TS102204/v1.1.2#", name = "MSS_SignatureResp")
    public JAXBElement<MSSSignatureRespType> createMSSSignatureResp(MSSSignatureRespType value) {
        return new JAXBElement<MSSSignatureRespType>(_MSSSignatureResp_QNAME, MSSSignatureRespType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MSSHandshakeReqType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/TS102204/v1.1.2#", name = "MSS_HandshakeReq")
    public JAXBElement<MSSHandshakeReqType> createMSSHandshakeReq(MSSHandshakeReqType value) {
        return new JAXBElement<MSSHandshakeReqType>(_MSSHandshakeReq_QNAME, MSSHandshakeReqType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MSSStatusReqType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/TS102204/v1.1.2#", name = "MSS_StatusReq")
    public JAXBElement<MSSStatusReqType> createMSSStatusReq(MSSStatusReqType value) {
        return new JAXBElement<MSSStatusReqType>(_MSSStatusReq_QNAME, MSSStatusReqType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MSSSignatureReqType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/TS102204/v1.1.2#", name = "MSS_SignatureReq")
    public JAXBElement<MSSSignatureReqType> createMSSSignatureReq(MSSSignatureReqType value) {
        return new JAXBElement<MSSSignatureReqType>(_MSSSignatureReq_QNAME, MSSSignatureReqType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MSSHandshakeRespType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/TS102204/v1.1.2#", name = "MSS_HandshakeResp")
    public JAXBElement<MSSHandshakeRespType> createMSSHandshakeResp(MSSHandshakeRespType value) {
        return new JAXBElement<MSSHandshakeRespType>(_MSSHandshakeResp_QNAME, MSSHandshakeRespType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MSSReceiptReqType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/TS102204/v1.1.2#", name = "MSS_ReceiptReq")
    public JAXBElement<MSSReceiptReqType> createMSSReceiptReq(MSSReceiptReqType value) {
        return new JAXBElement<MSSReceiptReqType>(_MSSReceiptReq_QNAME, MSSReceiptReqType.class, null, value);
    }

}
