package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.CMPCertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertOrEncCert;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFailureInfo;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ExceptionInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.IPKCS10Param;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 7, 2010
 * Time: 9:56:21 AM
 * To change this template use File | Settings | File Templates.
 */

public class PKCS10Param implements IPKCS10Param {
    private byte[] pkcs10Request;

    public PKCS10Param(byte[] pkcs10Request) {
        this.pkcs10Request = pkcs10Request;
    }

    public byte[] getPkcs10Request() {
        return pkcs10Request;
    }

    public ECertificate extractCertificate(CertOrEncCert certOrEncCert) throws CMPProtocolException {
        if (certOrEncCert.getChoiceID() != CertOrEncCert._CERTIFICATE)
            throw new CMPProtocolException(PKIFailureInfo.incorrectData,
                    "choice must be CERTIFICATE for incoming CertOrEncCert, its:" + certOrEncCert.getElemName());

        if (!(certOrEncCert.getElement() instanceof CMPCertificate))
            throw new CMPProtocolException(PKIFailureInfo.incorrectData,
                    "CertOrEncCert must have CMPCertificate, but its:" + certOrEncCert.getElement().getClass());

        CMPCertificate cc = (CMPCertificate) certOrEncCert.getElement();
        try {
            return new ECertificate((Certificate) cc.getElement());
        } catch (Exception ex) {
            throw new CMPProtocolException( ex,
                    new ExceptionInfo(PKIFailureInfo.badDataFormat,"Error while encode-decoding Certificate"));
        }
    }

}
