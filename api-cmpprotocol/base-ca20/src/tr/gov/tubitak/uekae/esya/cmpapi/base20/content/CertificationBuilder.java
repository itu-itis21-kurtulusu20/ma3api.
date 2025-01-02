package tr.gov.tubitak.uekae.esya.cmpapi.base20.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.*;
import tr.gov.tubitak.uekae.esya.api.asn.crmf.ECertReqMsg;
import tr.gov.tubitak.uekae.esya.api.asn.crmf.ECertRequest;
import tr.gov.tubitak.uekae.esya.api.asn.crmf.ECertTemplate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertConfirmContent;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertStatus;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIBody;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIStatus;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertReqMsg;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.*;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.util.UtilCmp;

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
//    private List<ICertificationParam> certReqMsgs = new ArrayList<ICertificationParam>();
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
    public void createCertCertMsg(ICertificationParam certificationParam) throws ESYAException {
        ECertTemplate template = new ECertTemplate();
        template.setSubject(certificationParam.getSender());

        long id = UtilCmp.createNextID(certReqMsgIDs);


        ECertRequest istek = new ECertRequest(id, template);
        ECertReqMsg certReqMsg = new ECertReqMsg(istek);
        certificationParam.addSpecificOperations(certReqMsg);
        certificationParam.setCertReq(certReqMsg);
        certReqMsgIDs.add(id);
    }

    /**
     * Creates PKIBody with CertReqMsges
     *
     * @param certificationParams
     * @return
     */
    public EPKIBody createCertReqBody(List<ICertificationParam> certificationParams) {
        PKIMessageType.IRequestType reqType = protocolType.getReqType();
        if (!(reqType instanceof PKIMessageType.CertReqType))
            throw new ESYARuntimeException("CertReqBody cannot be created for protocol:" + protocolType.getProtocolName());

        EPKIBody epkiBody = new EPKIBody();
        CertReqMsg[] certReqMessages = new CertReqMsg[certificationParams.size()];
        for (int i = 0; i < certReqMessages.length; i++) {
            certReqMessages[i] = certificationParams.get(i).getCertReqMsg().getObject();
        }
        epkiBody.setCertReqMessages(reqType.getChoice(), certReqMessages);
        return epkiBody;
    }

/*    public PKIBody createCertResBody(List<CertResponse> certResponses) throws CMPProtocolException {
        _SeqOfCertResponse seqOfCertResponse = new _SeqOfCertResponse(
                certResponses.toArray(new CertResponse[certResponses.size()]) );
        return new PKIBody(protocolType.getResType().getChoice(), new CertRepMessage(seqOfCertResponse));
    }*/

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
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData,
                    "Invalid PKI Body, must be " + reqType + ", But it is:" + pkiMessageType);

    }


    public PKIBody createCertConfBody(List<CertStatus> certStatusList) {
        CertStatus[] elements = new CertStatus[certStatusList.size()];
        CertConfirmContent certConfirmContent = new CertConfirmContent(certStatusList.toArray(elements));
        return new PKIBody(PKIMessageType.CERTCONF.getChoice(), certConfirmContent);
    }

/*    public void verifyCertConfBody(PKIBody certResBody, PKIBody certConfBody) throws CMPProtocolException {
        PKIMessageType.IRequestType requestType = protocolType.getConfType();
        if (!(requestType instanceof PKIMessageType.CertConfType))
            throw new RuntimeException("CertConfBody cannot be verified for protocol:" + protocolType.getProtocolName());

    }*/

/*    *//**
     * return all CertReqMsges collected
     * @return
     *//*
    public CertReqMsg[] getCertReqMsgs() {
        int i = 0;
        CertReqMsg[] certReqMsg = new CertReqMsg[certReqMsgs.size()];
        for (ICertificationParam certificationParam : certReqMsgs)
            certReqMsg[i++] = certificationParam.getCertReqMsg().getObject();
        return certReqMsg;
    }*/

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
     *
     * @param responseBody
     * @throws CMPProtocolException
     */
    public void verifyCertResMessage(ECertResponse[] responseBody, Map<Long, ICertificationParam> paramMap,boolean allFailOnSingleFail) throws CMPProtocolException {
/*        PKIMessageType.IResponseType resType = protocolType.getResType();
        if (!(resType instanceof PKIMessageType.CertResType))
            throw new RuntimeException("CertResBody cannot be verified for protocol:" + protocolType.getProtocolName());*/

/*
        PKIMessageType pkiMessageType = PKIMessageType.getPKIMessageType(responseBody.getChoiceID());
        if (pkiMessageType instanceof PKIMessageType.CertResType )
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData,"Invalid PKI Body, must be instance of " + PKIMessageType.CertResType.class + ", But it is:" + pkiMessageType);
*/

//        CertReqMsg[] certReqMsgs = getCertReqMsgs();
//        CertRepMessage certRepMessage = (CertRepMessage) responseBody.getElement();

        if (responseBody == null )
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData,"No CertResponse in CertRepMessage");
        if (responseBody.length != paramMap.size())
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData,
                    "Number of Response does not match with Requests, Responses:"
                    + paramMap.size()
                    + "Requests:" + responseBody.length);

        String info = null;

        PKIStatus status = null;
        for (int i = 0; i < responseBody.length; i++) {
            ECertResponse certResponse = responseBody[i];
            info = certResponse.getPkiStatusInfo().stringValue(" ");
            ICertificationParam certificationParam = paramMap.get(certResponse.getCertReqID());
            status = certResponse.getPkiStatusInfo().getStatus().getObject();
            if(certificationParam == null)
                throw new CMPProtocolException(EPKIFailureInfo.badCertId, "Unexpected CertID:"+certResponse.getCertReqID());
            if (certResponse.isAccepted() || certResponse.getPkiStatusInfo().getStatus().equals(EPKIStatus.WAITING) ) {
/*                if (certResponse.certReq.certReqId.value != certResponse.certReqId.value) {
                    String msg = "Response CertReqId(" + certResponse.certReqId.value +
                            ") does not match with Request CertReqId(" + certResponse.certReq.certReqId.value + ")";
                    logger.error(msg);
                    throw new CMPProtocolException(EPKIFailureInfo.incorrectData, msg);
                }*/
                logger.debug("Valid Status And CertReqId for " + certResponse.getCertReqID());
                continue;
/*            } else if () {//gelen mesajdakilerden herhangibiri geçersiz olduğu anda hiçbirini kabul etme
                String msg = "Invalid Status for CertResponse, Info:" + AsnIO.getFormattedAsn(info);
                logger.error(msg);
                throw new CMPProtocolException(msg,
                        new ExceptionInfo(EPKIFailureInfo.incorrectData,"Invalid Status for CertResponse:"+certResponse.certReqId) );*/
            } else if (status.equals(PKIStatus.rejection)) {
                if(allFailOnSingleFail)
                {
                    //gelen mesajdakilerden herhangibiri geçersiz olduğu anda hiçbirini kabul etme
                    String msg = "Invalid Status for CertResponse, Info:" + info;
                    logger.error(msg);
                    throw new CMPProtocolException(msg,new ExceptionInfo(EPKIFailureInfo.incorrectData,"Invalid Status for CertResponse:"+certResponse.getCertReqID()));
                }
                else{
                    //RMZ Burda EKK için rejection durumunda da devam edicez.
                    logger.debug("Rejection Status And CertReqId for " + certResponse.getCertReqID());
                    continue;
                }
            }
            {
                logger.error("Invalid Status for CertResponse:" + info);
                throw new CMPProtocolException("Invalid Status for CertResponse:" + info,
                        new ExceptionInfo(EPKIFailureInfo.incorrectData,"Invalid Status for CertResponse:"+certResponse.getCertReqID()) );
            }
        }
    }

    /**
     * Verifies and extract Reponses with Certification Parameters
     *
     * @param responseBody
     * @param certificationParams
     * @throws CMPProtocolException
     */
    public void extractResponse(EPKIBody responseBody, List<ICertificationParam> certificationParams,boolean allFailOnSingleFail) throws CMPProtocolException, ESYAException {
        PKIMessageType pkiMessageType = PKIMessageType.getPKIMessageType(responseBody.getChoiceID());

        if(pkiMessageType == PKIMessageType.POLLREP){
            verifyPollingRep(responseBody.getPollReps(), certificationParams);


        }else if(pkiMessageType instanceof PKIMessageType.CertResType){

            try {
                int k=0;
                ECertResponse[] receivedCertResponses = responseBody.getCertRepMessage();
                for (ICertificationParam  certificationParam: certificationParams) {
                    ECertResponse receivedCertResponse = receivedCertResponses[k];
                    certificationParam.setCertRep(receivedCertResponse);
                    k++;
                }
            }
            catch (Exception exc){
                logger.warn("Warning in CertificationBuilder", exc);
            }

            Map<Long, ICertificationParam> paramMap = getMappedCertificationParams(certificationParams);
            ECertResponse[] certRepMessage = responseBody.getCertRepMessage();
            verifyCertResMessage(certRepMessage, paramMap,allFailOnSingleFail);
            for (int i = 0; i < certRepMessage.length; i++) {
                ECertResponse eCertResponse = certRepMessage[i];
                paramMap.get(eCertResponse.getCertReqID()).extractResponse(eCertResponse);
            }
        }
    }

    private void verifyPollingRep(EPollRep[] pollReps, List<ICertificationParam> certificationParams) throws CMPProtocolException {
        if(pollReps == null || pollReps.length != certificationParams.size())
            throw new CMPProtocolException(EPKIFailureInfo.incorrectData,
                    "Number of Response does not match with Requests, Responses:"
                            + (pollReps==null ? 0 : pollReps.length)
                            + "Requests:" + certificationParams.size());
        Map<Long, ICertificationParam> paramMap = getMappedCertificationParams(certificationParams);
        long longestWait = 0;
        for (int i = 0; i < pollReps.length; i++) {
            EPollRep pollRep = pollReps[i];
            ICertificationParam iCertificationParam = paramMap.get(pollRep.getCertReqId());
            if(iCertificationParam == null)
                throw new CMPProtocolException(EPKIFailureInfo.badCertId, "Unexpected CertID:"+pollRep.getCertReqId());
            if(longestWait < pollRep.getCheckAfter())
                longestWait = pollRep.getCheckAfter();
        }

        try {
            logger.info("Waiting "+longestWait+" milisec after Polling ");
            Thread.sleep(longestWait);
        } catch (InterruptedException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public Map<Long,ICertificationParam> getMappedCertificationParams(List<ICertificationParam> certificationParams){
        HashMap<Long, ICertificationParam> paramHashMap = new HashMap<Long, ICertificationParam>();
        for (ICertificationParam certificationParam : certificationParams) {
            paramHashMap.put(certificationParam.getCertReqId(),certificationParam);
        }
        return paramHashMap;
    }


    public void createPollingReq(ICertificationParam certificationParam) {
        //To change body of created methods use File | Settings | File Templates.
    }

    public EPKIBody createPollingReqBody(List<ICertificationParam> toPolling) throws ESYAException {
        EPKIBody epkiBody = new EPKIBody();
        EPollReq[] reqContents = new EPollReq[toPolling.size()];
        for (int i = 0; i < reqContents.length; i++) {
            reqContents[i] = new EPollReq(toPolling.get(i).getCertReqId());
        }
        epkiBody.setPollReq(reqContents);
        return epkiBody;
    }
}
