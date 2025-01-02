package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertResponse;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificateType;

import java.security.PublicKey;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Nov 11, 2010
 * Time: 2:58:57 PM
 * To change this template use File | Settings | File Templates.
 */

public class KeyGenerationOnClient extends BaseCertificationParam {
    private PublicKey publicKey;
    private ICertificateType certificateType;

    public KeyGenerationOnClient(EName sender,
                                 PublicKey publicKey,
                                 Long sertifikaTalepNo,
                                 String cardNo,
                                 Long cardManufacturerNo,
                                 ICertificateType certificateType) {
        super(sender, sertifikaTalepNo, cardNo, cardManufacturerNo);
        this.publicKey = publicKey;
        this.certificateType = certificateType;
    }

    @Override
    public void addSpecificOperations(CertReqMsg certReqMsg)  {
        super.addSpecificOperations(certReqMsg);
        SubjectPublicKeyInfo publicKeyInfo = new SubjectPublicKeyInfo();
        try {
            publicKeyInfo.decode(new Asn1DerDecodeBuffer(publicKey.getEncoded()));
        } catch (Exception aEx) {
            throw new RuntimeException("Error whilde encoding SubjectPublicKeyInfo:" + aEx.getMessage(), aEx);
        }
        certReqMsg.certReq.certTemplate.publicKey = publicKeyInfo;
        certificateType.addSpecificOperations(certReqMsg);
    }

    @Override
    public void extractResponse(CertResponse certResponse) throws CMPProtocolException {
        super.extractResponse(certResponse);
        certificate = certificateType.extractCertificate(certOrEncCert);
    }
}
