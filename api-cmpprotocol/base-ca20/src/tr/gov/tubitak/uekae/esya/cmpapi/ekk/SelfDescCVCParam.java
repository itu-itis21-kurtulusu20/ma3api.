package tr.gov.tubitak.uekae.esya.cmpapi.ekk;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIFailureInfo;
import tr.gov.tubitak.uekae.esya.api.asn.crmf.ECertReqMsg;
import tr.gov.tubitak.uekae.esya.api.asn.cvc.ESelfDescCVC;
import tr.gov.tubitak.uekae.esya.api.asn.passport.EPassportCVCertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseCipher;
import tr.gov.tubitak.uekae.esya.asn.cmp.CMPCertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFailureInfo;
import tr.gov.tubitak.uekae.esya.asn.cvc.SelfDescCVC;
import tr.gov.tubitak.uekae.esya.asn.passport.PassportCVCerticate;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.util.Utilcrmf;

import java.security.PublicKey;

/**
 <b>Author</b>    : zeldal.ozdemir <br>
 <b>Project</b>   : MA3   <br>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 <b>Date</b>: 5/16/11 - 3:43 PM <p>
 <b>Description</b>: <br>
 */

public class SelfDescCVCParam extends BaseCertificationParam {
    private EPassportCVCertificate passportCVCertificate;
    private Long sertifikaTalepNo;


    public SelfDescCVCParam(EName cardName, long sablonNo, PublicKey proEncKey, BaseCipher protocolDecryptor) {
        super(cardName, sablonNo, proEncKey, protocolDecryptor);
    }

    public SelfDescCVCParam(EName cardName, long sablonNo, Long sertifikaTalepNo, PublicKey proEncKey, BaseCipher protocolDecryptor) {
        super(cardName, sablonNo, proEncKey, protocolDecryptor);
        this.sertifikaTalepNo = sertifikaTalepNo;
    }

    @Override
    public void addSpecificOperations(ECertReqMsg certReqMsg) {
        super.addSpecificOperations(certReqMsg);
        if (sertifikaTalepNo != null) {
            try {
                Utilcrmf.istegeSertTalepNoEkle(certReqMsg.getCertRequest().getObject(), sertifikaTalepNo);
            } catch (Exception aEx) {
                throw new RuntimeException("Error while encoding SertifikaTalepNo:" + aEx.getMessage(), aEx);
            }
        }
    }

    @Override
    public void setCertificate(CMPCertificate cmpCertificate) throws CMPProtocolException {
        if (cmpCertificate.getChoiceID() != CMPCertificate._SELFDESCCVC)
            throw new CMPProtocolException(new EPKIFailureInfo(PKIFailureInfo.incorrectData),
                    "choice must be _SELFDESCCVC for incoming CMPCertificate, its:" + cmpCertificate.getElemName());
        passportCVCertificate = new EPassportCVCertificate((PassportCVCerticate) cmpCertificate.getElement());
    }

    @Override
    protected long getSablonTipi() {
        return CVCReqType.SELFDESCCVCFORCARD.ordinal();
    }


    public EPassportCVCertificate getSelfDescCVC() {
        return passportCVCertificate;
    }

    public byte[] getCertificateEncoded() {
        return passportCVCertificate.getEncoded();
    }
}
