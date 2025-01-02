package tr.gov.tubitak.uekae.esya.cmpapi.base20.content;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import gnu.crypto.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertResponse;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIFailureInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.asn.cmp.*;
import tr.gov.tubitak.uekae.esya.asn.pkcs10.CertificationRequest;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.*;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 7, 2010
 * Time: 9:22:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class PKCS10Builder {
    private static final Logger logger = LoggerFactory.getLogger(PKCS10Builder.class);

    private ProtocolType protocolType;
    private IPKCS10Param pkcs10Param;

    public PKCS10Builder(ProtocolType protocolType) {
        this.protocolType = protocolType;
        if (protocolType != ProtocolType.PKCS10PROTOCOL)
            throw new RuntimeException("PKCS10Builder cannot be created for protocol:" + protocolType.getProtocolName());
    }

    public PKIBody createPKCS10Body(IPKCS10Param pkcs10Param) {
        this.pkcs10Param = pkcs10Param;
        byte[] pkcs10Request = pkcs10Param.getPkcs10Request();
        if (pkcs10Request == null)
            throw new RuntimeException("PKCS10Request is null");
        byte[] decoded;
        //istegi Base64 yapisindan decode et
        try {
            decoded = Base64.decode(pkcs10Request, 0, pkcs10Request.length);
        } catch (IllegalArgumentException e) {
            logger.warn("Error while decoding PKCS10REQUEST:" + e.getMessage(), e);
            decoded = pkcs10Request;
        }
        //asn yapisina cevir
        CertificationRequest request = new CertificationRequest();
        Asn1DerDecodeBuffer buf = new Asn1DerDecodeBuffer(decoded);
        try {
            request.decode(buf);
        } catch (Exception e) {
            throw new RuntimeException("Error while creating PKCS10Request:" + e.getMessage(), e);
        }
        PKIBody body = new PKIBody();
        body.set_p10cr(request);
        return body;
    }

    public ECertificate extractResponse(PKIBody responseBody) throws CMPProtocolException {

        PKIMessageType pkiMessageType = PKIMessageType.getPKIMessageType(responseBody.getChoiceID());
        if (pkiMessageType != PKIMessageType.CP)
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData, "Invalid PKI Body, must be " + PKIMessageType.CP + ", But it is:" + pkiMessageType);

        CertRepMessage certRepMessage = (CertRepMessage) responseBody.getElement();
        if (certRepMessage.response == null || certRepMessage.response.elements == null)
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData, "No CertResponse in CertRepMessage");
        if (certRepMessage.response.elements.length != 1)
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData,
                    "Number of Response does not match with Requests, Responses:"
                            + certRepMessage.response.elements.length
                            + "Requests:" + 1);
        CertResponse certResponse = certRepMessage.response.elements[0];
        if(certResponse != null)
            pkcs10Param.setCertResponse(new ECertResponse(certResponse));

        PKIStatus status = certResponse.status.status;
        PKIFreeText info = certResponse.status.statusString;

        if ((status.equals(0)) || (status.equals(1))) {
            logger.debug("Valid Status And CertReqId for " + certResponse.certReqId);
        } else if (status.equals(2)) {//gelen mesajdakilerden herhangibiri geçersiz olduğu anda hiçbirini kabul etme
            logger.error("Invalid Status for CertResponse:" + AsnIO.getFormattedAsn(certResponse));
            throw new CMPProtocolException("Invalid Status for CertResponse:" + AsnIO.getFormattedAsn(certResponse),
                    new ExceptionInfo(EPKIFailureInfo.incorrectData, "Invalid Status for CertResponse:" + certResponse.certReqId));
        } else {
            logger.error("Invalid Status for CertResponse:" + AsnIO.getFormattedAsn(certResponse));
            throw new CMPProtocolException("Invalid Status for CertResponse:" + AsnIO.getFormattedAsn(certResponse),
                    new ExceptionInfo(EPKIFailureInfo.incorrectData, "Invalid Status for CertResponse:" + certResponse.certReqId));
        }

        return pkcs10Param.extractCertificate(certResponse.certifiedKeyPair.certOrEncCert);
    }
}
