package tr.gov.tubitak.uekae.esya.cmpapi.base.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.crmf.ECertReqMsg;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.cmp.*;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMessages;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertRequest;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertTemplate;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.*;
import tr.gov.tubitak.uekae.esya.cmpapi.base.util.UtilCmp;

import java.util.*;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 11, 2010 - 2:32:35 PM <p>
 * <b>Description</b>: <br>
 *     Builds Certification Request with Certifcation Params,
 *     and verify{@literal &}extracts responses
 */
public class CertificationBuilder {
    private static final Logger logger = LoggerFactory.getLogger(CertificationBuilder.class);
    private List<ICertificationParam> certReqMsgs = new ArrayList<ICertificationParam>();
    private Set<Long> certReqMsgIDs = new HashSet<Long>();
    private ProtocolType protocolType;

    public CertificationBuilder(ProtocolType protocolType) throws CMPProtocolException {
        this.protocolType = protocolType;
        if (protocolType != ProtocolType.INITIALIZATIONPROTOCOL
                && protocolType != ProtocolType.CERTIFICATIONPROTOCOL
                && protocolType != ProtocolType.KEYUPDATEPROTOCOL
                && protocolType != ProtocolType.CVCPROTOCOL)
            throw new ESYARuntimeException("CertificationBuilder cannot be created for protocol:" + protocolType.getProtocolName());
    }

    /**
     * Creates CertReqMsg with given ICertificationParam
     * @param certificationParam
     */
    public void addCertReqMsg(ICertificationParam certificationParam) {
        CertTemplate template = new CertTemplate(null, //version
                null, //serialnumber
                null, //signingAlg
                null, //issuer
                null, //validity
                certificationParam.getSender().getObject(), //subject
                null, //publicKey
                null, //issuerUID
                null, //subjectUID
                null //extensions
        );
        long id = UtilCmp.createNextID(certReqMsgIDs);


        CertRequest istek = new CertRequest(id, template);
        CertReqMsg certReqMsg = new CertReqMsg(istek);
        certificationParam.addSpecificOperations(certReqMsg);
        certificationParam.setCertReq(certReqMsg);
        certReqMsgs.add(certificationParam);
        certReqMsgIDs.add(id);
    }

    /**
     * Creates PKIBody with CertReqMsges
     * @param certReqMsgs
     * @return
     */
    public PKIBody createCertReqBody(CertReqMsg[] certReqMsgs) {
        PKIMessageType.IRequestType reqType = protocolType.getReqType();
        if (!(reqType instanceof PKIMessageType.CertReqType))
            throw new ESYARuntimeException("CertReqBody cannot be created for protocol:" + protocolType.getProtocolName());

        CertReqMessages certReqMessages = new CertReqMessages(certReqMsgs);
        return new PKIBody(reqType.getChoice(), certReqMessages);
    }

    public PKIBody createCertResBody(List<CertResponse> certResponses) throws CMPProtocolException {
        _SeqOfCertResponse seqOfCertResponse = new _SeqOfCertResponse(
                certResponses.toArray(new CertResponse[certResponses.size()]) );
        return new PKIBody(protocolType.getResType().getChoice(), new CertRepMessage(seqOfCertResponse));
    }

    /**
     * Verifies whether incomnig message body contains CertReqMsg es or not
     * @param pkiBody
     * @throws CMPProtocolException
     */
    public void verifyCertReqMessage(PKIBody pkiBody) throws CMPProtocolException {
        PKIMessageType.IRequestType reqType = protocolType.getReqType();
        if (!(reqType instanceof PKIMessageType.CertReqType))
            throw new ESYARuntimeException("CertReqBody cannot be verified for protocol:" + protocolType.getProtocolName());

        PKIMessageType pkiMessageType = PKIMessageType.getPKIMessageType(pkiBody.getChoiceID());
        if (pkiMessageType != reqType)
            throw new CMPProtocolException(PKIFailureInfo.incorrectData,
                    "Invalid PKI Body, must be " + reqType + ", But it is:" + pkiMessageType);

    }


    public PKIBody createCertConfBody(List<CertStatus> certStatusList) {
        CertStatus[] elements = new CertStatus[certStatusList.size()];
        CertConfirmContent certConfirmContent = new CertConfirmContent(certStatusList.toArray(elements));
        return new PKIBody(PKIMessageType.CERTCONF.getChoice(), certConfirmContent);
    }

    public void verifyCertConfBody(PKIBody certResBody, PKIBody certConfBody) throws CMPProtocolException {
        PKIMessageType.IRequestType requestType = protocolType.getConfType();
        if (!(requestType instanceof PKIMessageType.CertConfType))
            throw new ESYARuntimeException("CertConfBody cannot be verified for protocol:" + protocolType.getProtocolName());

    }

    /**
     * return all CertReqMsges collected
     * @return
     */
    public CertReqMsg[] getCertReqMsgs() {
        int i = 0;
        CertReqMsg[] certReqMsg = new CertReqMsg[certReqMsgs.size()];
        for (ICertificationParam certificationParam : certReqMsgs)
            certReqMsg[i++] = certificationParam.getCertReqMsg();
        return certReqMsg;
    }

    /**
     * Verifies Cert Response Messages with Cert Request Messages
     * it matches reqID, Certification Statuses. <br>
     * <ul>
     *  <li> Number of request/response must same
     *  <li> Req IDs must match in ordered
     *  <li> Response statues must be fine.
     * </ul>
     *  <p>
     * if anything goes wrong it throw CMPProtocolException
     *
     * @param responseBody
     * @param allFailOnSingleFail
     * @return
     * @throws CMPProtocolException
     */
    public CertResponse[] verifyCertResMessage(PKIBody responseBody, boolean allFailOnSingleFail) throws CMPProtocolException {
        PKIMessageType.IResponseType resType = protocolType.getResType();
        if (!(resType instanceof PKIMessageType.CertResType))
            throw new ESYARuntimeException("CertResBody cannot be verified for protocol:" + protocolType.getProtocolName());

        PKIMessageType pkiMessageType = PKIMessageType.getPKIMessageType(responseBody.getChoiceID());
        if (pkiMessageType != resType)
            throw new CMPProtocolException(PKIFailureInfo.incorrectData,"Invalid PKI Body, must be " + resType + ", But it is:" + pkiMessageType);

        CertReqMsg[] certReqMsgs = getCertReqMsgs();
        CertRepMessage certRepMessage = (CertRepMessage) responseBody.getElement();
        if (certRepMessage.response == null || certRepMessage.response.elements == null)
            throw new CMPProtocolException(PKIFailureInfo.incorrectData,"No CertResponse in CertRepMessage");
        if (certReqMsgs.length != certRepMessage.response.elements.length)
            throw new CMPProtocolException(PKIFailureInfo.incorrectData,
                    "Number of Response does not match with Requests, Responses:"
                    + certRepMessage.response.elements.length
                    + "Requests:" + certReqMsgs.length);

        PKIStatus status = null;
        PKIFreeText info = null;

        for (int i = 0; i < certReqMsgs.length; i++) {
            CertReqMsg certReqMsg = certReqMsgs[i];
            CertResponse certResponse = certRepMessage.response.elements[i];
            if (certReqMsg.certReq.certReqId.value != certResponse.certReqId.value) {
                String msg = "Response CertReqId(" + certResponse.certReqId.value +
                        ") does not match with Request CertReqId(" + certReqMsg.certReq.certReqId.value + ")";
                logger.error(msg);
                throw new CMPProtocolException(PKIFailureInfo.incorrectData, msg);
            }
            status = certResponse.status.status;
            info = certResponse.status.statusString;
            if ((status.equals(PKIStatus.accepted)) || (status.equals(PKIStatus.grantedWithMods))) {//Eğer talep geçerli ise
                logger.debug("Valid Status And CertReqId for " + certResponse.certReqId);
                continue;
            } else if (status.equals(PKIStatus.rejection)) {
                if(allFailOnSingleFail)
                {
                    //gelen mesajdakilerden herhangibiri geçersiz olduğu anda hiçbirini kabul etme
                    String msg = "Invalid Status for CertResponse, Info:" + AsnIO.getFormattedAsn(info);
                    logger.error(msg);
                    throw new CMPProtocolException(msg,new ExceptionInfo(PKIFailureInfo.incorrectData,"Invalid Status for CertResponse:"+certResponse.certReqId));
                }
                else{
                    //RMZ Burda EKK için rejection durumunda da devam edicez.
                    logger.debug("Rejection Status And CertReqId for " + certResponse.certReqId);
                    continue;
                }
            }  else {
                logger.error("Invalid Status for CertResponse:" + AsnIO.getFormattedAsn(certResponse));
                throw new CMPProtocolException("Invalid Status for CertResponse:" + AsnIO.getFormattedAsn(certResponse),
                        new ExceptionInfo(PKIFailureInfo.incorrectData,"Invalid Status for CertResponse:"+certResponse.certReqId) );
            }
        }
        return certRepMessage.response.elements;

    }

    /**
     * Verifies and extract Reponses with Certification Parameters
     *
     * @param responseBody
     * @param allFailOnSingleFail
     * @return
     * @throws CMPProtocolException
     */
    public List<ICertificationParam> extractResponse(PKIBody responseBody, boolean allFailOnSingleFail) throws CMPProtocolException {

        try {
            int k=0;
            CertRepMessage certRepMessage = (CertRepMessage) responseBody.getElement();
            CertResponse[] receivedCertResponses = certRepMessage.response.elements;
            for (ICertificationParam  certificationParam: certReqMsgs) {
                CertResponse certResponse = receivedCertResponses[k];
                certificationParam.setCertRep(certResponse);
                k++;
            }
        }
        catch (Exception exc){
            logger.warn("Warning in CertificationBuilder", exc);
        }

        CertResponse[] certResponses = verifyCertResMessage(responseBody,allFailOnSingleFail);
        int i = 0;
        for (ICertificationParam  certificationParam: certReqMsgs) {
            CertResponse certResponse = certResponses[i++];
            try{
                certificationParam.extractResponse(certResponse);
            }
            catch (CMPProtocolException exc){
                if(allFailOnSingleFail)
                    throw exc;
            }
        }
        return certReqMsgs;
    }



}
