package tr.gov.tubitak.uekae.esya.cmpapi.base20.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIFailureInfo;
import tr.gov.tubitak.uekae.esya.asn.cmp.*;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMessages;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertRequest;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertTemplate;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.*;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.util.UtilCmp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 7, 2010
 * Time: 4:40:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class KeyRecoveryBuilder {
    private static final Logger logger = LoggerFactory.getLogger(KeyRecoveryBuilder.class);

    private ProtocolType protocolType;
    private List<IKeyRecoveryParam> certReqMsgs = new ArrayList<IKeyRecoveryParam>();
    private Set<Long> certReqMsgIDs = new HashSet<Long>();


    public KeyRecoveryBuilder(ProtocolType protocolType) {
        this.protocolType = protocolType;
        if (protocolType != ProtocolType.KEYRECOVERYPROTOCOL)
            throw new RuntimeException("KeyRecoveryBuilder cannot be created for protocol:" + protocolType.getProtocolName());
    }

    public void addCertReqMsg(IKeyRecoveryParam keyRecoveryParam) {
        CertTemplate template = new CertTemplate(null, //version
                null, //serialnumber
                null, //signingAlg
                null, //issuer
                null, //validity
                null, //subject
                null, //publickey
                null, //issuerUID
                null, //subjectUID
                null //extensions
        );
        long id = UtilCmp.createNextID(certReqMsgIDs);

        CertRequest istek = new CertRequest(id, template);
        CertReqMsg certReqMsg = new CertReqMsg(istek);
        keyRecoveryParam.addSpecificOperations(certReqMsg);
        keyRecoveryParam.setCertReq(certReqMsg);
        certReqMsgs.add(keyRecoveryParam);
        certReqMsgIDs.add(id);
    }

    public CertReqMsg[] getCertReqMsgs() {
        int i = 0;
        CertReqMsg[] certReqMsg = new CertReqMsg[certReqMsgs.size()];
        for (IKeyRecoveryParam keyRecoveryParam : certReqMsgs)
            certReqMsg[i++] = keyRecoveryParam.getCertReqMsg();
        return certReqMsg;
    }

    public PKIBody createCertReqBody(CertReqMsg[] certReqMsgs) {
        PKIMessageType.IRequestType reqType = protocolType.getReqType();
        if (!(reqType instanceof PKIMessageType.ICertificationType))
            throw new RuntimeException("CertReqBody cannot be created for protocol:" + protocolType.getProtocolName());
        CertReqMessages certReqMessages = new CertReqMessages(certReqMsgs);
        return new PKIBody(reqType.getChoice(), certReqMessages);
    }

    public List<IKeyRecoveryParam> extractResponse(PKIBody responseBody) throws CMPProtocolException {
        CertifiedKeyPair[] certifiedKeyPairs = verifyKeyRecRepMessage(responseBody);
        for (IKeyRecoveryParam certificationParam : certReqMsgs) {
            certificationParam.extractResponse(certifiedKeyPairs);
        }

        return certReqMsgs;
/*                    if (mRecResponse.keyPairHist != null) {
                CertifiedKeyPair[] liste = mRecResponse.keyPairHist.elements;
                sertifikalar = new Certificate[liste.length];
                for (int i = 0; i < liste.length; i++) {
                    if (liste[i].certOrEncCert == null) {
                        throw new ESYAException("Gelen cevapta " + i + ". sertifika boş");
                    } else if (liste[i].privateKey == null) {
                        throw new ESYAException("Gelen cevapta " + i + ". private key boş");
                    }
                    certificate = liste[i].certOrEncCert;
                    sertifikalar[i] = _getCertificate(certificate, null);
                }
            } else {
                throw new ESYAException("Gelen cevapta keyPairHist alanı yok");
            }*/


    }

    public CertifiedKeyPair[] verifyKeyRecRepMessage(PKIBody responseBody) throws CMPProtocolException {
/*        PKIMessageType.IResponseType resType = protocolType.getResType();
        if (!(resType instanceof PKIMessageType.KeyRecResType))
            throw new RuntimeException("CertResBody cannot be verified for protocol:" + protocolType.getProtocolName());

        PKIMessageType pkiMessageType = PKIMessageType.getPKIMessageType(responseBody.getChoiceID());
        if (pkiMessageType != resType)
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData, "Invalid PKI Body, must be " + resType + ", But it is:" + pkiMessageType);*/

        CertReqMsg[] certReqMsgs = getCertReqMsgs();
        KeyRecRepContent keyRecRepContent = (KeyRecRepContent) responseBody.getElement();
        if (keyRecRepContent == null
                || keyRecRepContent.keyPairHist == null)
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData, "No KeyRecRepContent_keyPairHist in KeyRecRepContent");


        PKIStatus status = keyRecRepContent.status.status;
        PKIFreeText info = keyRecRepContent.status.statusString;
        if ((status.equals(0)) || (status.equals(1))) {
            logger.debug("Valid KeyRecRepContent Status");
            return keyRecRepContent.keyPairHist.elements;
        } else if (status.equals(2)) {//gelen mesajdakilerden herhangibiri geçersiz olduğu anda hiçbirini kabul etme
            String msg = "Invalid Status for KeyRecRepContent, Info:" + AsnIO.getFormattedAsn(info);
            logger.error(msg);
            throw new CMPProtocolException(msg,
                    new ExceptionInfo(EPKIFailureInfo.incorrectData, "Invalid Status for KeyRecRepContent"));
        } else {
            logger.error("Invalid Status for CertResponse:" + AsnIO.getFormattedAsn(keyRecRepContent));
            throw new CMPProtocolException("Invalid Status for CertResponse:" + AsnIO.getFormattedAsn(keyRecRepContent),
                    new ExceptionInfo(EPKIFailureInfo.incorrectData, "Invalid Status for KeyRecRepContent"));
        }

    }

}
