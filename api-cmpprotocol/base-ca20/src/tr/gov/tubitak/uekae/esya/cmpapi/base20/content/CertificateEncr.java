package tr.gov.tubitak.uekae.esya.cmpapi.base20.content;

import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIFailureInfo;
import tr.gov.tubitak.uekae.esya.api.asn.crmf.ECertReqMsg;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseCipher;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertOrEncCert;
import tr.gov.tubitak.uekae.esya.asn.crmf.EncryptedValue;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ExceptionInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ICertificateType;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.util.Utilcrmf;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 11, 2010 - 3:00:16 PM <p>
 * <b>Description</b>: <br>
 *     it is used to take Encrytion certificates
  */

public class CertificateEncr implements ICertificateType {
    private BaseCipher certificateDecryptor;

    public CertificateEncr(BaseCipher certificateDecryptor) {
        this.certificateDecryptor = certificateDecryptor;
    }

    public ECertificate extractCertificate(CertOrEncCert certOrEncCert) throws CMPProtocolException {
        if (certOrEncCert.getChoiceID() != CertOrEncCert._ENCRYPTEDCERT)
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData,
                    "choice must be _ENCRYPTEDCERT for incoming CertOrEncCert, its:" + certOrEncCert.getElemName());

        if (!(certOrEncCert.getElement() instanceof EncryptedValue))
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData,
                    "CertOrEncCert must have EncryptedValue, but its:" + certOrEncCert.getElement().getClass());

        EncryptedValue value = (EncryptedValue) certOrEncCert.getElement();
        byte[] bb;
        try {
            bb = Utilcrmf.encryptedValuedakiSifreliyiAl(certificateDecryptor, value);
        } catch (Exception ex) {
            throw new CMPProtocolException("Error while decrypting Certificate in CertOrEncCert:" + ex.getMessage(), ex,
                    new ExceptionInfo(EPKIFailureInfo.badDataFormat,"_ENCRYPTEDCERT Decrytion Failure"));
        }
        try {
            return new ECertificate(bb);
        } catch (Exception ex) {
            throw new CMPProtocolException("Error while decoding decrypted value to Certificate:" + ex.getMessage(), ex,
                    new ExceptionInfo(EPKIFailureInfo.incorrectData,"CERT Encoding Failure"));
        }
    }

    public void addSpecificOperations(ECertReqMsg certReqMsg)  {
        certReqMsg.getObject().pop = new POPEncryptor().popOlustur(certReqMsg.getCertRequest().getObject());
    }
}
