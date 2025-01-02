package tr.gov.tubitak.uekae.esya.cmpapi.base.content;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.CMPCertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertOrEncCert;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIFailureInfo;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ExceptionInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificateType;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 11, 2010 - 2:59:52 PM <p>
 * <b>Description</b>: <br>
 *     it is used to take Sign Certificates
 */

public class CertificateSign implements ICertificateType {
    private POPSigner signer;

    public CertificateSign(POPSigner signer) {
        this.signer = signer;
    }

    public void addSpecificOperations(CertReqMsg certReqMsg) {
        certReqMsg.pop = signer.popOlustur(certReqMsg.certReq);
    }

    public ECertificate extractCertificate(CertOrEncCert certOrEncCert) throws CMPProtocolException {
        if (certOrEncCert.getChoiceID() != CertOrEncCert._CERTIFICATE)
            throw new CMPProtocolException(PKIFailureInfo.incorrectData,
                    "choice must be CERTIFICATE for incoming CertOrEncCert, its:" + certOrEncCert.getElemName());

        if (!(certOrEncCert.getElement() instanceof CMPCertificate))
            throw new CMPProtocolException(PKIFailureInfo.incorrectData,
                    "CertOrEncCert must have CMPCertificate, but its:" + certOrEncCert.getElement().getClass());

        try {
            CMPCertificate cc = (CMPCertificate) certOrEncCert.getElement();
            return new ECertificate((Certificate) cc.getElement());
        } catch (Exception ex) {
            throw new CMPProtocolException(ex,
                    new ExceptionInfo(PKIFailureInfo.badDataFormat, "Error while encode-decoding Certificate"));
        }
    }
}
