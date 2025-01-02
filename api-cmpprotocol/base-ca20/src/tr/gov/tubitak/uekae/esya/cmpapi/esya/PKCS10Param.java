package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertResponse;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIFailureInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.CMPCertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertOrEncCert;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ExceptionInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.IPKCS10Param;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 7, 2010
 * Time: 9:56:21 AM
 * To change this template use File | Settings | File Templates.
 */

public class PKCS10Param implements IPKCS10Param {
    private byte[] pkcs10Request;
    ECertResponse eCertResponse;

    public ECertResponse getCertResponse() {
        return eCertResponse;
    }

    public void setCertResponse(ECertResponse eCertResponse) {
        this.eCertResponse = eCertResponse;
    }

    public PKCS10Param(byte[] pkcs10Request) {
        this.pkcs10Request = pkcs10Request;
    }

    public byte[] getPkcs10Request() {
        return pkcs10Request;
    }

    public ECertificate extractCertificate(CertOrEncCert certOrEncCert) throws CMPProtocolException {
        if (certOrEncCert.getChoiceID() != CertOrEncCert._CERTIFICATE)
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData,
                    "choice must be CERTIFICATE for incoming CertOrEncCert, its:" + certOrEncCert.getElemName());

        if (!(certOrEncCert.getElement() instanceof CMPCertificate))
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData,
                    "CertOrEncCert must have CMPCertificate, but its:" + certOrEncCert.getElement().getClass());

        CMPCertificate cc = (CMPCertificate) certOrEncCert.getElement();
        try {
            return new ECertificate((Certificate) cc.getElement());
        } catch (Exception ex) {
            throw new CMPProtocolException( ex,
                    new ExceptionInfo(EPKIFailureInfo.badDataFormat,"Error while encode-decoding Certificate"));
        }
    }

}
