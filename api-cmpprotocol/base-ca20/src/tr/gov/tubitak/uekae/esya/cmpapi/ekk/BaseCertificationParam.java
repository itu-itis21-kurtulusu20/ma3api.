package tr.gov.tubitak.uekae.esya.cmpapi.ekk;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1Integer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.*;
import tr.gov.tubitak.uekae.esya.api.asn.crmf.ECertReqMsg;
import tr.gov.tubitak.uekae.esya.api.asn.crmf.EEncryptedValue;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAOID;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseCipher;
import tr.gov.tubitak.uekae.esya.asn.cmp.CMPCertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertOrEncCert;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFailureInfo;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIStatus;
import tr.gov.tubitak.uekae.esya.asn.crmf.OptionalValidity;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.util.UtilTime;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ExceptionInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ICertificationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.util.Utilcrmf;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;

/**
 <b>Author</b>    : zeldal.ozdemir <br>
 <b>Project</b>   : MA3   <br>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 <b>Date</b>: 5/16/11 - 3:22 PM <p>
 <b>Description</b>: <br>
 */
public abstract class BaseCertificationParam implements ICertificationParam {
    private PublicKey proEncKey;
    EName cardName;
    private ECertReqMsg certReqMsg;
    private CertOrEncCert certOrEncCert;
    private long sablonNo;
    private BaseCipher cipher;
    private PrivateKey privateKey;
    private Calendar optionalValidityStart;
    private EEncryptedValue encryptedPrivateKey;
    EPKIStatus pkiStatus;
    protected ECertResponse certResponse;


    private ECertStatus acceptanceStatus;

    public ECertResponse getCertResponse() {
        return certResponse;
    }

    public void setCertRep(ECertResponse certResponse) {
        this.certResponse = certResponse;
    }


    private static final Logger logger = LoggerFactory.getLogger(BaseCertificationParam.class);

    public BaseCertificationParam(EName cardName,
                                  long sablonNo,
                                  PublicKey proEncKey,
                                  BaseCipher protocolDecryptor
    ) {
        this.cardName = cardName;
        this.sablonNo = sablonNo;
        this.cipher = protocolDecryptor;
        this.proEncKey = proEncKey;
    }

    public EName getSender() {
        return cardName;
    }

    public void addSpecificOperations(ECertReqMsg certReqMsg) {
        if (optionalValidityStart != null) {
            if (certReqMsg.getObject().certReq.certTemplate.validity == null)
                certReqMsg.getObject().certReq.certTemplate.validity = new OptionalValidity();
            certReqMsg.getObject().certReq.certTemplate.validity.notBefore = UtilTime.calendarToTimeFor3280(optionalValidityStart);
        }
        try {
            Utilcrmf.istegeASN1TypeEkle(certReqMsg.getObject().certReq, new Asn1Integer(sablonNo), EESYAOID.oid_sablonNo);
        } catch (Exception aEx) {
            throw new RuntimeException("Error whilde encoding SablonNo:" + aEx.getMessage(), aEx);
        }

        try {
            Utilcrmf.istegeASN1TypeEkle(certReqMsg.getObject().certReq, new Asn1Integer(getSablonTipi()), EESYAOID.oid_cvcSablonTipi);
        } catch (Exception aEx) {
            throw new RuntimeException("Error whilde encoding SablonTipi:" + aEx.getMessage(), aEx);
        }

        SubjectPublicKeyInfo proEncKeyInfo = new SubjectPublicKeyInfo();
        try {
            proEncKeyInfo.decode(new Asn1DerDecodeBuffer(proEncKey.getEncoded()));
        } catch (Exception aEx) {
            throw new RuntimeException("Error whilde encoding SubjectPublicKeyInfo:" + aEx.getMessage(), aEx);
        }

        try {
            Utilcrmf.istegeProtocolEncKeyEkle(certReqMsg.getObject().certReq, proEncKeyInfo);
        } catch (Asn1Exception e) {
            throw new RuntimeException("Error while creating CertReqMsg-ProEncrKey" + e.getMessage(), e);
        }

    }

    public void setCertReq(ECertReqMsg certReqMsg) {
        this.certReqMsg = certReqMsg;
    }

    public ECertReqMsg getCertReqMsg() {
        return certReqMsg;
    }

    public boolean isSuccess(){
        if(pkiStatus==null){
            return false;
        }
        return pkiStatus.equals(PKIStatus.accepted) ||
                pkiStatus.equals(PKIStatus.grantedWithMods);
    }


    public void extractResponse(ECertResponse certResponse) throws CMPProtocolException {
        this.certResponse = certResponse;
        if(certResponse != null){
            pkiStatus = certResponse.getPkiStatusInfo().getStatus();
        }

        ECertifiedKeyPair certifiedKeyPair = certResponse.getCertifiedKeyPair();
        if ( certifiedKeyPair == null || certifiedKeyPair.getObject().certOrEncCert == null )
            throw new CMPProtocolException("CertResponse:" + AsnIO.getFormattedAsn(certResponse.getObject()),
                    new ExceptionInfo(EPKIFailureInfo.badDataFormat, "CertOrEncCert is Empty for certReqID:" + certReqMsg.getObject().certReq.certReqId.value));
        certOrEncCert = certifiedKeyPair.getObject().certOrEncCert;

        if (certOrEncCert.getChoiceID() != CertOrEncCert._CERTIFICATE)
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData,
                    "choice must be CERTIFICATE for incoming CertOrEncCert, its:" + certOrEncCert.getElemName());

        if (!(certOrEncCert.getElement() instanceof CMPCertificate))
            throw new CMPProtocolException(new EPKIFailureInfo(PKIFailureInfo.incorrectData),
                    "CertOrEncCert must have CMPCertificate, but its:" + certOrEncCert.getElement().getClass());
        setCertificate((CMPCertificate) certOrEncCert.getElement());

        encryptedPrivateKey = certResponse.getCertifiedKeyPair().getPrivateKey();
        if (cipher == null) {
            return;//throw new RuntimeException("No Cipher presented to decrypt incoming PrivateKey");
        } else {
            try {
               byte[] protocolEncKeyBytes = Utilcrmf.encryptedValuedakiSifreliyiAl(cipher, this.encryptedPrivateKey.getObject());
               privateKey = Utilcrmf.decryptPrivateKey(protocolEncKeyBytes,encryptedPrivateKey.getObject());
            } catch (Exception ex) {
                throw new CMPProtocolException("Error while decrypting PrivateKey:" + ex.getMessage(), ex,
                        new ExceptionInfo(new EPKIFailureInfo(PKIFailureInfo.badDataFormat), "Error while encode-decoding Certificate"));
            }

        }

    }

    public ECertificate getCertificate() {
        return null;
    }

    public void setCertificate(ECertificate certificate) {

    }

    public abstract void setCertificate(CMPCertificate cmpCertificate) throws CMPProtocolException;

    protected abstract long getSablonTipi();

    public void setOptionalValidityStart(Calendar optionalValidityStart) {
        this.optionalValidityStart = optionalValidityStart;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public EEncryptedValue getEncryptedPrivateKey() {
        return encryptedPrivateKey;
    }

    //todo bu metodları tamamlayalım lutfen
    public boolean isWaiting() {
        return false;
    }

    public boolean canBeAccept() {
        return true;
    }

    public void setAcceptanceStatus(ECertStatus acceptanceStatus) {
        this.acceptanceStatus = acceptanceStatus;
    }

    public ECertStatus getAcceptanceStatus() {
        return acceptanceStatus;
    }

    public long getCertReqId() {
        return certReqMsg.getCertRequest().getCertReqId();
    }

    public void setHandled(boolean handled) {

    }
}
