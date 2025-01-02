package tr.gov.tubitak.uekae.esya.cmpapi.ekk;

import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1ValueParseException;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYACardSerialNumber;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAOID;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYASDOHash;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAYeniSertifikaDurumu;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseCipher;
import tr.gov.tubitak.uekae.esya.asn.cmp.CMPCertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFailureInfo;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYAYeniSertifikaDurumu;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.Utilcrmf;

import java.security.PublicKey;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: 5/16/11 - 3:51 PM <p>
 * <b>Description</b>: <br>
 */

public class X509CVCParam extends BaseCertificationParam {
    private ECertificate certificate;
    private EESYASDOHash sdoHash;
    private EESYACardSerialNumber cardSerialNumber;
    private EESYAYeniSertifikaDurumu yeniSertifikaDurumu;
    private Long sertifikaTalepNo;

    public X509CVCParam(EESYACardSerialNumber cardSerialNumber, EESYASDOHash sdoHash, EName cardName, long sablonNo, PublicKey proEncKey, BaseCipher protocolDecryptor) {
        super(cardName, sablonNo, proEncKey, protocolDecryptor);
        this.cardSerialNumber = cardSerialNumber;
        this.sdoHash = sdoHash;
    }

    public void setAskidaUret(boolean askidaUret){
        try {
            EESYAYeniSertifikaDurumu sertifikaDurumu = new EESYAYeniSertifikaDurumu(askidaUret);
            this.yeniSertifikaDurumu = sertifikaDurumu;
        } catch (Asn1ValueParseException e) {
            e.printStackTrace();
        }
    }


    public X509CVCParam(EESYACardSerialNumber cardSerialNumber, EESYASDOHash sdoHash, EName cardName, long sablonNo, PublicKey proEncKey, BaseCipher protocolDecryptor,boolean askidaUret) {
        super(cardName, sablonNo, proEncKey, protocolDecryptor);
        this.cardSerialNumber = cardSerialNumber;
        this.sdoHash = sdoHash;
        if(askidaUret)
        {
            setAskidaUret(askidaUret);
        }
    }

    public X509CVCParam(EESYACardSerialNumber cardSerialNumber, EESYASDOHash sdoHash, EName cardName, long sablonNo, long sertifikaTalepNo, PublicKey proEncKey, BaseCipher protocolDecryptor) {
        super(cardName, sablonNo, proEncKey, protocolDecryptor);
        this.cardSerialNumber = cardSerialNumber;
        this.sdoHash = sdoHash;
        this.sertifikaTalepNo = sertifikaTalepNo;
    }

    public X509CVCParam(EESYACardSerialNumber cardSerialNumber, EESYASDOHash sdoHash, EName cardName, long sablonNo, long sertifikaTalepNo, PublicKey proEncKey, BaseCipher protocolDecryptor,boolean askidaUret) {
        super(cardName, sablonNo, proEncKey, protocolDecryptor);
        this.cardSerialNumber = cardSerialNumber;
        this.sdoHash = sdoHash;
        this.sertifikaTalepNo = sertifikaTalepNo;
        if(askidaUret){
            setAskidaUret(askidaUret);
        }
    }

    @Override
    public void setCertificate(CMPCertificate cmpCertificate) throws CMPProtocolException {
        if (cmpCertificate.getChoiceID() != CMPCertificate._X509V3PKCERT)
            throw new CMPProtocolException(PKIFailureInfo.incorrectData,
                    "choice must be _X509V3PKCERT for incoming CMPCertificate, its:" + cmpCertificate.getElemName());
        certificate = new ECertificate((Certificate) cmpCertificate.getElement());
    }

    @Override
    public void addSpecificOperations(CertReqMsg certReqMsg) {

        super.addSpecificOperations(certReqMsg);

        if (sdoHash != null) {
            try {
                Utilcrmf.istegeASN1TypeEkle(certReqMsg.certReq, new Asn1OctetString(sdoHash.getEncoded()), EESYAOID.oid_ESYA_SDO);
            } catch (Exception aEx) {
                throw new RuntimeException("Error while encoding EsyaSdoHash:" + aEx.getMessage(), aEx);
            }
        }

        if (cardSerialNumber != null) {
            try {
                Utilcrmf.istegeASN1TypeEkle(certReqMsg.certReq, new Asn1OctetString(cardSerialNumber.getEncoded()), EESYAOID.oid_ESYA_CardSerialNumber);
            } catch (Exception aEx) {
                throw new RuntimeException("Error while encoding EsyaSdoHash:" + aEx.getMessage(), aEx);
            }
        }
        if (sertifikaTalepNo != null) {
            try {
                Utilcrmf.istegeSertTalepNoEkle(certReqMsg.certReq, sertifikaTalepNo);
            } catch (Exception aEx) {
                throw new RuntimeException("Error while encoding SertifikaTalepNo:" + aEx.getMessage(), aEx);
            }
        }

        if (yeniSertifikaDurumu != null) {
            try {
                Utilcrmf.istegeASN1TypeEkle(certReqMsg.certReq, new Asn1OctetString(yeniSertifikaDurumu.getEncoded()), EESYAOID.oid_cmpYeniSertifikaDurumu);
            } catch (Exception aEx) {
                throw new RuntimeException("Error while encoding cmpYeniSertifikaDurumu:" + aEx.getMessage(), aEx);
            }
        }

    }

    @Override
    protected long getSablonTipi() {
        return CVCReqType.X509FORCARD.ordinal();
    }

    public ECertificate getCertificate() {
        return certificate;
    }

    public byte[] getCertificateEncoded() {
        return certificate.getEncoded();
    }

}
