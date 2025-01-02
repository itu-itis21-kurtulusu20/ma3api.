package tr.gov.tubitak.uekae.esya.cmpapi.ekk;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1Integer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertResponse;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAOID;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseCipher;
import tr.gov.tubitak.uekae.esya.asn.cmp.*;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;
import tr.gov.tubitak.uekae.esya.asn.crmf.EncryptedValue;
import tr.gov.tubitak.uekae.esya.asn.crmf.OptionalValidity;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.util.UtilTime;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ExceptionInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.Utilcrmf;

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
    private CertReqMsg certReqMsg;
    private CertOrEncCert certOrEncCert;
    private long sablonNo;
    private BaseCipher cipher;
    private PrivateKey privateKey;
    private Calendar optionalValidityStart;
    private EncryptedValue encryptedPrivateKey;
    PKIStatus pkiStatus;
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

    public void addSpecificOperations(CertReqMsg certReqMsg) {
        if (optionalValidityStart != null) {
            if (certReqMsg.certReq.certTemplate.validity == null)
                certReqMsg.certReq.certTemplate.validity = new OptionalValidity();
            certReqMsg.certReq.certTemplate.validity.notBefore = UtilTime.calendarToTimeFor3280(optionalValidityStart);
        }
        try {
            Utilcrmf.istegeASN1TypeEkle(certReqMsg.certReq, new Asn1Integer(sablonNo), EESYAOID.oid_sablonNo);
        } catch (Exception aEx) {
            throw new RuntimeException("Error whilde encoding SablonNo:" + aEx.getMessage(), aEx);
        }

        try {
            Utilcrmf.istegeASN1TypeEkle(certReqMsg.certReq, new Asn1Integer(getSablonTipi()), EESYAOID.oid_cvcSablonTipi);
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
            Utilcrmf.istegeProtocolEncKeyEkle(certReqMsg.certReq, proEncKeyInfo);
        } catch (Asn1Exception e) {
            throw new RuntimeException("Error while creating CertReqMsg-ProEncrKey" + e.getMessage(), e);
        }

    }

    public void setCertReq(CertReqMsg certReqMsg) {
        this.certReqMsg = certReqMsg;
    }

    public CertReqMsg getCertReqMsg() {
        return certReqMsg;
    }

    public boolean isSuccess(){
        if(pkiStatus==null){
            return false;
        }
        return pkiStatus.equals(PKIStatus.accepted) ||
                pkiStatus.equals(PKIStatus.grantedWithMods);
    }


    public void extractResponse(CertResponse certResponse) throws CMPProtocolException {
        this.certResponse = certResponse;
        if(certResponse != null){
            logger.debug("Cert response bos degil");
            pkiStatus = certResponse.status.status;
        }

        if(certResponse.certifiedKeyPair==null){
            logger.error("certifiedKeyPair is Empty");
            throw new CMPProtocolException("CertResponse:" + AsnIO.getFormattedAsn(certResponse),
                    new ExceptionInfo(PKIFailureInfo.badDataFormat, "certifiedKeyPair is Empty for certReqID:" + certReqMsg.certReq.certReqId.value));
        }

        if (certResponse.certifiedKeyPair.certOrEncCert == null) {
            logger.error("CertOrEncCert is Empty");
            throw new CMPProtocolException("CertResponse:" + AsnIO.getFormattedAsn(certResponse),
                    new ExceptionInfo(PKIFailureInfo.badDataFormat, "CertOrEncCert is Empty for certReqID:" + certReqMsg.certReq.certReqId.value));
        }
        certOrEncCert = certResponse.certifiedKeyPair.certOrEncCert;

        if (certOrEncCert.getChoiceID() != CertOrEncCert._CERTIFICATE) {
            logger.error("choice must be CERTIFICATE for incoming CertOrEncCert");
            throw new CMPProtocolException(PKIFailureInfo.incorrectData,
                    "choice must be CERTIFICATE for incoming CertOrEncCert, its:" + certOrEncCert.getElemName());
        }

        if (!(certOrEncCert.getElement() instanceof CMPCertificate)) {
            logger.error("CertOrEncCert must have CMPCertificate");
            throw new CMPProtocolException(PKIFailureInfo.incorrectData,
                    "CertOrEncCert must have CMPCertificate, but its:" + certOrEncCert.getElement().getClass());
        }
        logger.debug("Sertifika set edilecek.");
        setCertificate((CMPCertificate) certOrEncCert.getElement());
        logger.debug("Sertifika set edildi.");

        logger.debug("Şifreli Private key response'dan okunacak.");
        encryptedPrivateKey = certResponse.certifiedKeyPair.privateKey;
        logger.debug("Şifreli Private key response'dan okundu.");
        if (cipher == null) {
            logger.debug("Herhangi bir cipher gönderilmediğinden şifre çözmeye çalışmıyoruz.");
            return;//throw new RuntimeException("No Cipher presented to decrypt incoming PrivateKey");
        } else {
            logger.debug("Bize cipher gönderilmiş şifre çözme işlemi yapıcaz.");
            try {
                logger.debug("Encrypted value içindeki şifreli veri okunacak.");
                byte[] protocolEncKeyBytes = Utilcrmf.encryptedValuedakiSifreliyiAl(cipher, this.encryptedPrivateKey);
                logger.debug("Encrypted value içindeki şifreli veri okundu.");
                logger.debug("Şifreli private key yapısı çözülecek.");
                privateKey = Utilcrmf.decryptPrivateKey(protocolEncKeyBytes,encryptedPrivateKey);
                logger.debug("Şifreli private key yapısı çözüldü.");
            } catch (Exception ex) {
                throw new CMPProtocolException("Error while decrypting PrivateKey:" + ex.getMessage(), ex,
                        new ExceptionInfo(PKIFailureInfo.badDataFormat, "Error while encode-decoding Certificate"));
            }
            logger.debug("Bize gönderilen cipher ile şifre çözme işlemi tamamlandı.");

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

    public EncryptedValue getEncryptedPrivateKey() {
        return encryptedPrivateKey;
    }
}
