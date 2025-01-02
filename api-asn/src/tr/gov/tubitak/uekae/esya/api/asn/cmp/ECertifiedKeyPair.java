package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import com.objsys.asn1j.runtime.Asn1Type;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.crmf.EEncryptedValue;
import tr.gov.tubitak.uekae.esya.api.asn.cvc.ENonSelfDescCVCwithHeader;
import tr.gov.tubitak.uekae.esya.api.asn.cvc.ESelfDescCVC;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.CMPCertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertOrEncCert;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertifiedKeyPair;
import tr.gov.tubitak.uekae.esya.asn.cvc.NonSelfDescCVCwithHeader;
import tr.gov.tubitak.uekae.esya.asn.cvc.SelfDescCVC;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;

/**
 * User: zeldal.ozdemir
 * Date: 2/2/11
 * Time: 11:37 AM
 */
public class ECertifiedKeyPair extends BaseASNWrapper<CertifiedKeyPair>{

    public ECertifiedKeyPair(CertifiedKeyPair aObject) {
        super(aObject);
    }

    public ECertifiedKeyPair() {
        super(new CertifiedKeyPair());
    }

    public void setCertificate(ECertificate certificate){
        mObject.certOrEncCert = new CertOrEncCert();
        mObject.certOrEncCert.set_certificate(
                new CMPCertificate(CMPCertificate._X509V3PKCERT, certificate.getObject())
        );
    }

    public void setCertificate(ENonSelfDescCVCwithHeader nonSelfDescCVC){
        mObject.certOrEncCert = new CertOrEncCert();
        mObject.certOrEncCert.set_certificate(
                new CMPCertificate(CMPCertificate._NONSELFDESCCVCWITHHEADER, nonSelfDescCVC.getObject())
        );
    }

    public void setPassportCVCertificate(Asn1Type ePassportCVCertificate){
        mObject.certOrEncCert = new CertOrEncCert();
        mObject.certOrEncCert.set_certificate(
                new CMPCertificate(CMPCertificate._SELFDESCCVC, ePassportCVCertificate)
        );
    }

    public void setEncryptedCert(EEncryptedValue encryptedValue){
        mObject.certOrEncCert = new CertOrEncCert();
        mObject.certOrEncCert.set_encryptedCert(encryptedValue.getObject());
    }

    public ECertificate getCertificate(){
        CMPCertificate cmpCertificate = (CMPCertificate) mObject.certOrEncCert.getElement();
        if(cmpCertificate.getChoiceID() != CMPCertificate._X509V3PKCERT)
            return null;
        Certificate certificate = (Certificate) cmpCertificate.getElement();
        return new ECertificate(certificate);
    }

    public ENonSelfDescCVCwithHeader getNonSelfDescCVC(){
        CMPCertificate cmpCertificate = (CMPCertificate) mObject.certOrEncCert.getElement();
        if(cmpCertificate.getChoiceID() != CMPCertificate._NONSELFDESCCVCWITHHEADER)
            return null;
        NonSelfDescCVCwithHeader certificate = (NonSelfDescCVCwithHeader) cmpCertificate.getElement();
        return new ENonSelfDescCVCwithHeader(certificate);
    }

    public ESelfDescCVC getSelfDescCVC(){
        CMPCertificate cmpCertificate = (CMPCertificate) mObject.certOrEncCert.getElement();
        if(cmpCertificate.getChoiceID() != CMPCertificate._SELFDESCCVC)
            return null;
        SelfDescCVC certificate = (SelfDescCVC) cmpCertificate.getElement();
        return new ESelfDescCVC(certificate);
    }


    public Asn1Type getPassportCVCertificate(){
        CMPCertificate cmpCertificate = (CMPCertificate) mObject.certOrEncCert.getElement();
        if(cmpCertificate.getChoiceID() != CMPCertificate._SELFDESCCVC)
            return null;
        return cmpCertificate.getElement();
    }

    public void setPrivateKey(EEncryptedValue privateKey){
        mObject.privateKey = privateKey.getObject();
    }

    public EEncryptedValue getPrivateKey(){
        if(mObject.privateKey == null)
            return null;
        return new EEncryptedValue(mObject.privateKey);
    }


}
