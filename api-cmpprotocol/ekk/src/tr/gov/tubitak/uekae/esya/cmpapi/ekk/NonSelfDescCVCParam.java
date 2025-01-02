package tr.gov.tubitak.uekae.esya.cmpapi.ekk;

import tr.gov.tubitak.uekae.esya.api.asn.cvc.ENonSelfDescCVCwithHeader;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseCipher;
import tr.gov.tubitak.uekae.esya.asn.cmp.CMPCertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFailureInfo;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;
import tr.gov.tubitak.uekae.esya.asn.cvc.NonSelfDescCVCwithHeader;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.Utilcrmf;

import java.security.PublicKey;

/**
 <b>Author</b>    : zeldal.ozdemir <br>
 <b>Project</b>   : MA3   <br>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 <b>Date</b>: 5/16/11 - 3:49 PM <p>
 <b>Description</b>: <br>
 */

public class NonSelfDescCVCParam extends BaseCertificationParam {
    private ENonSelfDescCVCwithHeader nonSelfDescCVC;
    private Long sertifikaTalepNo;

    public NonSelfDescCVCParam(EName cardName, long sablonNo, PublicKey proEncKey, BaseCipher protocolDecryptor) {
        super(cardName, sablonNo, proEncKey, protocolDecryptor);
    }
    public NonSelfDescCVCParam(EName cardName, long sablonNo, Long sertifikaTalepNo, PublicKey proEncKey, BaseCipher protocolDecryptor) {
        super(cardName, sablonNo, proEncKey, protocolDecryptor);
        this.sertifikaTalepNo = sertifikaTalepNo;
    }

    @Override
    public void addSpecificOperations(CertReqMsg certReqMsg) {
        super.addSpecificOperations(certReqMsg);
        if (sertifikaTalepNo != null) {
            try {
                Utilcrmf.istegeSertTalepNoEkle(certReqMsg.certReq, sertifikaTalepNo);
            } catch (Exception aEx) {
                throw new RuntimeException("Error while encoding SertifikaTalepNo:" + aEx.getMessage(), aEx);
            }
        }
    }

    @Override
    public void setCertificate(CMPCertificate cmpCertificate) throws CMPProtocolException {
        if (cmpCertificate.getChoiceID() != CMPCertificate._NONSELFDESCCVCWITHHEADER)
            throw new CMPProtocolException(PKIFailureInfo.incorrectData,
                    "choice must be _NONSELFDESCCVC for incoming CMPCertificate, its:" + cmpCertificate.getElemName());
        nonSelfDescCVC = new ENonSelfDescCVCwithHeader((NonSelfDescCVCwithHeader) cmpCertificate.getElement());
    }

    @Override
    protected long getSablonTipi() {
        return CVCReqType.NONSELFDESCCVCFORCARD.ordinal();
    }


    public ENonSelfDescCVCwithHeader getNonSelfDescCVC() {
        return nonSelfDescCVC;
    }

    public byte[] getCertificateEncoded() {
        return nonSelfDescCVC.getEncoded();
    }
}
