package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import com.objsys.asn1j.runtime.Asn1Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertOrEncCert;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertResponse;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFailureInfo;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIStatus;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ExceptionInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.Utilcrmf;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Nov 11, 2010
 * Time: 3:50:53 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class BaseCertificationParam implements ICertificationParam {
    protected static final Logger logger = LoggerFactory.getLogger(BaseCertificationParam.class);
    private EName sender;
    private Long sertifikaTalepNo;                   // mandatory
    private String cardNo;                           // optional
    private Long cardManufacturerNo;
    protected CertReqMsg certReqMsg;
    protected ECertificate certificate;
    protected CertOrEncCert certOrEncCert;

    protected CertResponse certResponse;
    public ECertResponse getCertResponse() {
        if(certResponse == null){
            return null;
        }
        else
            return new ECertResponse(certResponse);
    }

    public void setCertRep(CertResponse certResponse) {
        this.certResponse = certResponse;
    }

    PKIStatus pkiStatus;

    public BaseCertificationParam(EName sender, Long sertifikaTalepNo, String cardNo, Long cardManufacturerNo) {
        this.sender = sender;
        this.sertifikaTalepNo = sertifikaTalepNo;
        this.cardNo = cardNo;
        this.cardManufacturerNo = cardManufacturerNo;
    }

    public EName getSender() {
        return sender;
    }

    public void addSpecificOperations(CertReqMsg certReqMsg)  {
        try {
            Utilcrmf.istegeSertTalepNoEkle(certReqMsg.certReq, sertifikaTalepNo);
            logger.info("sertifikaTalepNo added to certReqMsg.");
            if (cardNo != null) {
                Utilcrmf.istegeKartSeriNoEkle(certReqMsg.certReq, cardNo);
                logger.info("cardNo added to certReqMsg.");
            }
            if (cardManufacturerNo != null) {
                Utilcrmf.istegeKartUreticiNoEkle(certReqMsg.certReq, cardManufacturerNo.intValue());
                logger.info("cardManufacturerNo added to certReqMsg.");
            }
        } catch (Asn1Exception e) {
            throw new RuntimeException("Error while creating CertReqMsg-controls" + e.getMessage(), e);
        }
    }

    public void setCertReq(CertReqMsg certReqMsg) {
        this.certReqMsg = certReqMsg;
    }

    public CertReqMsg getCertReqMsg() {
        return certReqMsg;
    }

    public ECertificate getCertificate() {
        return certificate;
    }

    public void setCertificate(ECertificate certificate) {
        this.certificate = certificate;
    }

    public boolean isSuccess(){
        return pkiStatus.equals(PKIStatus.accepted) ||
                pkiStatus.equals(PKIStatus.grantedWithMods);
    }

    public void extractResponse(CertResponse certResponse) throws CMPProtocolException {
        this.certResponse = certResponse;
        pkiStatus = certResponse.status.status;
        if (certResponse.certifiedKeyPair == null
                || certResponse.certifiedKeyPair.certOrEncCert == null)
            throw new CMPProtocolException("CertResponse:" + AsnIO.getFormattedAsn(certResponse),
                    new ExceptionInfo(PKIFailureInfo.badDataFormat,"CertOrEncCert is Empty for certReqID:" + certReqMsg.certReq.certReqId.value) );
        certOrEncCert = certResponse.certifiedKeyPair.certOrEncCert;

/*        if (encCert.getChoiceID() != CertOrEncCert._CERTIFICATE) {
            if (!(encCert.getElement() instanceof CMPCertificate)) {
                throw new CMPProtocolException("choice CertOrEncCert._CERTIFICATE olmasına rağmen sertifika CMPCertificate değil");
            }
            CMPCertificate cc = (CMPCertificate) encCert.getElement();
            try {
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                cc.encode(encBuf);
                byte[] bb = encBuf.getMsgCopy();
                certificate = new Certificate();
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(bb);

                certificate.decode(decBuf);

            } catch (Exception ex) {
                logger.error("Error while encode-decoding Certificate", ex);
                throw new CMPProtocolException("Error while encode-decoding Certificate",ex);
            }

        } else if (encCert.getChoiceID() == CertOrEncCert._ENCRYPTEDCERT) {

        }*/
    }

    public byte[] getCertificateEncoded() {
        return certificate.getEncoded();
    }

}
