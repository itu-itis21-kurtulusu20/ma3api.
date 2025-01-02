package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.*;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;

/**
 * User: zeldal.ozdemir
 * Date: 2/1/11
 * Time: 4:48 PM
 */
public class EKeyRecRepContent extends BaseASNWrapper<KeyRecRepContent> {

    public EKeyRecRepContent(byte[] aBytes) throws ESYAException {
        super(aBytes, new KeyRecRepContent());
    }

    public EKeyRecRepContent(KeyRecRepContent aObject) {
        super(aObject);
    }

    public EKeyRecRepContent(EPKIStatusInfo statusInfo) {
        super(new KeyRecRepContent());
        mObject.status = statusInfo.getObject();
    }

    public void setKeyPairHistory(ECertifiedKeyPair[] keyPairHistory){
        mObject.keyPairHist = new KeyRecRepContent_keyPairHist(BaseASNWrapper.unwrapArray(keyPairHistory));
    }

    public String getRecoveredCertificateSerials() {
        KeyRecRepContent_keyPairHist content = mObject.keyPairHist;
        if (content == null)
            return "??";

        String sertifikalar = "";
        for (CertifiedKeyPair certifiedKeyPair : content.elements) {
            if (certifiedKeyPair.certOrEncCert.getElement() instanceof CMPCertificate
                    && ((CMPCertificate) certifiedKeyPair.certOrEncCert.getElement()).getElement() instanceof Certificate)
                sertifikalar += " " + ((Certificate) ((CMPCertificate) certifiedKeyPair.certOrEncCert.getElement()).getElement()).tbsCertificate.serialNumber.value.toString(16);
            else sertifikalar += " ??";
        }

        return sertifikalar;
    }

    public EPKIStatus getKeyRecoveryStatus(){
        return new EPKIStatus(mObject.status.status);
    }

}
