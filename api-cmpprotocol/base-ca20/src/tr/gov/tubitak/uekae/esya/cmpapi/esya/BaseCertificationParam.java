package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import com.objsys.asn1j.runtime.Asn1Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertResponse;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertStatus;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIFailureInfo;
import tr.gov.tubitak.uekae.esya.api.asn.crmf.ECertReqMsg;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertOrEncCert;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertResponse;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIStatus;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ExceptionInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ICertificationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.util.Utilcrmf;

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
    protected ECertReqMsg certReqMsg;
    protected ECertificate certificate;
    protected CertOrEncCert certOrEncCert;
    private ECertStatus acceptanceStatus;
    private boolean waiting = true;
    private boolean handled = false;


    protected ECertResponse certResponse;

    public ECertResponse getCertResponse() {
        return certResponse;
    }

    public void setCertRep(ECertResponse certResponse) {
        this.certResponse = certResponse;
    }

    public BaseCertificationParam(EName sender, Long sertifikaTalepNo, String cardNo, Long cardManufacturerNo) {
        this.sender = sender;
        this.sertifikaTalepNo = sertifikaTalepNo;
        this.cardNo = cardNo;
        this.cardManufacturerNo = cardManufacturerNo;
    }

    public EName getSender() {
        return sender;
    }

    public void addSpecificOperations(ECertReqMsg certReqMsg)  {
        try {

            Utilcrmf.istegeSertTalepNoEkle(certReqMsg.getCertRequest().getObject(), sertifikaTalepNo);
            logger.info("sertifikaTalepNo added to certReqMsg.");
            if (cardNo != null) {
                Utilcrmf.istegeKartSeriNoEkle(certReqMsg.getCertRequest().getObject(), cardNo);
                logger.info("cardNo added to certReqMsg.");
            }
            if (cardManufacturerNo != null) {
                Utilcrmf.istegeKartUreticiNoEkle(certReqMsg.getCertRequest().getObject(), cardManufacturerNo.intValue());
                logger.info("cardManufacturerNo added to certReqMsg.");
            }
        } catch (Asn1Exception e) {
            throw new RuntimeException("Error while creating CertReqMsg-controls" + e.getMessage(), e);
        }
    }

    public void setCertReq(ECertReqMsg certReqMsg) {
        this.certReqMsg = certReqMsg;
    }

    public ECertReqMsg getCertReqMsg() {
        return certReqMsg;
    }

    public long getCertReqId(){
        return certReqMsg.getCertRequest().getCertReqId();
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }

    public ECertificate getCertificate() {
        return certificate;
    }

    public void setCertificate(ECertificate certificate) {
        this.certificate = certificate;
    }

    public void extractResponse(ECertResponse certResponse) throws CMPProtocolException {
        waiting=false;
        if(certResponse.getObject().status.status.value == PKIStatus.waiting){
            waiting = true;
            return;
        }
        this.certResponse = certResponse;
        if (certResponse.getObject().certifiedKeyPair == null
                || certResponse.getObject().certifiedKeyPair.certOrEncCert == null)
            throw new CMPProtocolException("CertResponse:" + AsnIO.getFormattedAsn(certResponse.getObject()),
                    new ExceptionInfo(EPKIFailureInfo.badDataFormat,"CertOrEncCert is Empty for certReqID:" + certReqMsg.getCertRequest().getObject().certReqId.value) );
        certOrEncCert = certResponse.getObject().certifiedKeyPair.certOrEncCert;
        handled = true;

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

    public boolean isWaiting() {
        return waiting;
    }

    public boolean canBeAccept() {
        return handled && acceptanceStatus == null;
    }

    public void setAcceptanceStatus(ECertStatus acceptanceStatus) {
        this.acceptanceStatus = acceptanceStatus;
    }

    public ECertStatus getAcceptanceStatus() {
        return acceptanceStatus;
    }
}
