package tr.gov.tubitak.uekae.esya.cmpapi.mobil;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1Integer;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertOrEncCert;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertResponse;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFailureInfo;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIStatus;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.util.OIDESYA;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ExceptionInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificateType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.Utilcrmf;

import java.security.PublicKey;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 14, 2010
 * Time: 10:41:53 AM
 * To change this template use File | Settings | File Templates.
 */

public class CertificationParam implements ICertificationParam {
    private EName sender;
    private PublicKey publicKey;
    private long siparisDetayno;
    private ICertificateType certificateType;
    protected CertReqMsg certReqMsg;
    protected ECertificate certificate;
    protected CertOrEncCert certOrEncCert;
    PKIStatus pkiStatus;

    public CertificationParam(EName sender, PublicKey publicKey, long siparisDetayno, ICertificateType certificateType) {
        this.sender = sender;
        this.publicKey = publicKey;
        this.siparisDetayno = siparisDetayno;
        this.certificateType = certificateType;
    }

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

    public EName getSender() {
        return sender;
    }

    public void addSpecificOperations(CertReqMsg certReqMsg) {

        SubjectPublicKeyInfo publicKeyInfo = new SubjectPublicKeyInfo();
        try {
            publicKeyInfo.decode(new Asn1DerDecodeBuffer(publicKey.getEncoded()));
        } catch (Exception aEx) {
            throw new RuntimeException("Error whilde encoding SubjectPublicKeyInfo:" + aEx.getMessage(), aEx);
        }
        certReqMsg.certReq.certTemplate.publicKey = publicKeyInfo;
        try {
            Utilcrmf.istegeASN1TypeEkle(certReqMsg.certReq, new Asn1Integer(siparisDetayno), OIDESYA.oid_siparisDetayNo);
        } catch (Asn1Exception e) {
            throw new RuntimeException("Error while adding siparisDetayno to  CertRequest - controls" + e.getMessage(), e);
        }
        certificateType.addSpecificOperations(certReqMsg);
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
                    new ExceptionInfo(PKIFailureInfo.badDataFormat, "CertOrEncCert is Empty for certReqID:" + certReqMsg.certReq.certReqId.value));
        certOrEncCert = certResponse.certifiedKeyPair.certOrEncCert;
        certificate = certificateType.extractCertificate(certOrEncCert);
    }

    public byte[] getCertificateEncoded() {
        return certificate.getEncoded();
    }
}
