package tr.gov.tubitak.uekae.esya.api.asn.passport;

import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.passport.PassportCVCerticate;

/**
 * Created by ahmet.asa on 19.09.2017.
 */
public class EPassportCVCertificate extends BaseASNWrapper<PassportCVCerticate> {

    public EPassportCVCertificate(){
        super(new PassportCVCerticate());
    }

    public EPassportCVCertificate(byte[] encoded) throws ESYAException {
        super(encoded, new PassportCVCerticate());
    }

    public EPassportCVCertificate(PassportCVCerticate aObject){
        super(aObject);
    }

    public EPassportCVCertificate(byte[] signature, EPassportCVCertificateBody certificateBody){
        super(new PassportCVCerticate());

        setSignature(signature);
        setCertificateBody(certificateBody);

    }

    public EPassportCVCertificateBody getPassportCertificateBody() throws ESYAException {
        return new EPassportCVCertificateBody(getObject().certificateBody);
    }

    private void setCertificateBody(EPassportCVCertificateBody _certificateBody) {
        getObject().certificateBody = _certificateBody.getObject();
    }

    private void setSignature(byte[] _signature) {
        getObject().signature = new Asn1OctetString(_signature);
    }

}
