package tr.gov.tubitak.uekae.esya.cmpapi.base20;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.*;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertStatus;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIBody;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.*;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.content.CertificationBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.content.IMsgBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.httplayer.IConnection;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.util.LocaleUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 11, 2010 - 3:00:16 PM <p>
 * <b>Description</b>: <br>
 * Base Protocol Implementations for Certification Based Protocol Types.
 */

public abstract class BaseCertificationProtocol implements IProtocol {
    private static final Logger logger = LoggerFactory.getLogger(BaseCertificationProtocol.class);
    /**
     * Connnection layer for Cmp protocol, this implementations will be used to send/recevice messages for any layer.
     */
    private IConnection connection;
    protected IMsgBuilder msgBuilder;
    private List<ICertificationParam> certificationParams;
    private ICertificationAcceptanceStrategy acceptanceStrategy;
    private CertificationBuilder bodyContent;
    private EPKIMessage request;
    private EPKIMessage response;

    private boolean allFailOnSingleFail = true;

    public void setLocaleTag(String localeTag) {
        this.localeTag = localeTag;
    }

    private String localeTag=null;
    //    private EPKIMessage certConfirmRequest;
//    private EPKIMessage pkiConfResponse;
//    private List<ICertificationParam> certificationResult;
    private final ProtocolConnection protocolConnection;
//    private List<ECertStatus> certStatusList;

    /**
     * Common parameters must be supllied to execute protocol
     *
     * @param connection
     * @param msgBuilder
     * @param certificationParams
     * @param acceptanceStrategy
     * @throws CMPProtocolException
     */
    public BaseCertificationProtocol(IConnection connection, IMsgBuilder msgBuilder,
                                     List<ICertificationParam> certificationParams,
                                     ICertificationAcceptanceStrategy acceptanceStrategy) throws CMPProtocolException {
        this.msgBuilder = msgBuilder;
        this.certificationParams = certificationParams;
        this.acceptanceStrategy = acceptanceStrategy;
        this.connection = connection;
        this.bodyContent = msgBuilder.createCertificationBuilder();
        protocolConnection = new ProtocolConnection(connection, msgBuilder);
    }

    public BaseCertificationProtocol(IConnection connection, IMsgBuilder msgBuilder,
                                     List<ICertificationParam> certificationParams,
                                     ICertificationAcceptanceStrategy acceptanceStrategy,boolean allFailOnSingleFail) throws CMPProtocolException {
        this.msgBuilder = msgBuilder;
        this.certificationParams = certificationParams;
        this.acceptanceStrategy = acceptanceStrategy;
        this.connection = connection;
        this.bodyContent = msgBuilder.createCertificationBuilder();
        protocolConnection = new ProtocolConnection(connection, msgBuilder);
        this.allFailOnSingleFail = allFailOnSingleFail;
    }

    /**
     * Protocol Execution order:
     * - Create Request with given parameters.
     * - Open Connection, Send Message
     * - Receive response
     * - Send Certification Confirm
     * - Receive PKI Confirm
     * - Close connection
     */
    public void runProtocol() throws CMPConnectionException, CMPProtocolException, ESYAException {
        logger.info("Protocol is starting...");
        if(localeTag!=null){
            //Burda sunucunun bu istek için kullacağı dili belirliyoruz.
            request = LocaleUtility.createLocaleSetRequest(localeTag, msgBuilder);
            response = protocolConnection.sendPKIMessageAndReceiveResponse(request);
        }

        request = createRequest(certificationParams);
        response = protocolConnection.sendPKIMessageAndReceiveResponse(request);

        try {
            /* extract response */
            bodyContent.extractResponse(response.getBody(), certificationParams,allFailOnSingleFail);
            List<ICertificationParam> handledCertificationResults = certificationParams;

            while (waitingForAny()) {
                logger.info("Not All Certifications Accepted, so polling");
                if (!acceptanceStrategy.handleAllCertificatesTogether()) {
                    acceptReceivedCertificates(handledCertificationResults);
                }

                handledCertificationResults = getCertificationsToPoll();
                EPKIMessage pollingReq = createPollingReq(handledCertificationResults);
                EPKIMessage resp = protocolConnection.sendPKIMessageAndReceiveResponse(pollingReq);
                bodyContent.extractResponse(resp.getBody(), handledCertificationResults,allFailOnSingleFail);
            }
            acceptReceivedCertificates(certificationParams);

        } catch (CMPProtocolException e) {
            logger.error(e.getMessage(),e);
            protocolConnection.sendErrorMsg(EPKIFailureInfo.badDataFormat, "Unexpected Response");
            acceptanceStrategy.rollbackCertificates(certificationParams);
	        for (ICertificationParam certificationParam : certificationParams) {
		        ECertResponse certResponse = certificationParam.getCertResponse();
		        if (certResponse == null){
			        certResponse = new ECertResponse(certificationParam.getCertReqId(), new EPKIStatusInfo(EPKIStatus.REJECTION, e.getMessage(), e.getExceptionInfo().getPkiFailInfo()));
			        certificationParam.setCertRep(certResponse);
		        }
	        }
            throw new RuntimeException("CMPProtocolException in Response: " + e.getMessage(), e);
        }
        logger.info("Response is received");
        connection.finish();
        logger.info("Protocol is finished successfully.");
    }


    private EPKIMessage createPollingReq(List<ICertificationParam> waiting) throws ESYAException {
        EPKIHeader requestHeader = msgBuilder.createPkiHeader();

        EPKIBody pkiBody = bodyContent.createPollingReqBody(waiting);
        logger.info("Request is created");
        return msgBuilder.createCmpMessage(requestHeader, pkiBody);
    }

    private List<ICertificationParam> getCertificationsToPoll() {
        List<ICertificationParam> waiting = new ArrayList<ICertificationParam>();
        for (ICertificationParam certificationParam : certificationParams) {
            if (certificationParam.isWaiting()) {
                waiting.add(certificationParam);
            }
        }
        return waiting;
    }

    private void acceptReceivedCertificates(List<ICertificationParam> certificationResults) throws CMPConnectionException, CMPProtocolException, ESYAException {
        List<ICertificationParam> handled = new ArrayList<ICertificationParam>();
        for (ICertificationParam certificationParam : certificationResults) {
            if (certificationParam.canBeAccept())
                handled.add(certificationParam);
        }

        if (handled.isEmpty())
            return;
        try {
            List<ECertStatus> eCertStatuses = acceptanceStrategy.acceptCertificates(handled);
            for (int i = 0; i < eCertStatuses.size(); i++) {
                handled.get(i).setHandled(true);
                handled.get(i).setAcceptanceStatus(eCertStatuses.get(i));
            }
            EPKIMessage certConfirm = createCertConfirm(handled);
            sendCertConfirmAndReceivePkiConf(certConfirm);
        } catch (RuntimeException e) {
            protocolConnection.sendErrorMsg(EPKIFailureInfo.systemFailure, "Failed to Accept Certificates:" + e.getMessage());
            throw e;
        }

    }

    private boolean waitingForAny() {
        for (ICertificationParam certificationParam : certificationParams) {
            if (certificationParam.isWaiting())
                return true;
        }
        return false;
    }

    /**
     * Here we create PKIRequest to send CA,
     * - IMsgBuilder implementations creates header, empty Body
     * - CertificationBuilder(bodyContent) creates CertReqMsg for each ICertificationParam
     * - IMsgBuilder create CmpPacket with adding Protection using given IProtectionTrustProvider implementation
     */
    private EPKIMessage createRequest(List<ICertificationParam> certificationParams) throws ESYAException {
        EPKIHeader requestHeader = msgBuilder.createPkiHeader();
        for (ICertificationParam certificationParam : certificationParams)
            bodyContent.createCertCertMsg(certificationParam);

        EPKIBody pkiBody = bodyContent.createCertReqBody(certificationParams);
        logger.info("Request is created");
        return msgBuilder.createCmpMessage(requestHeader, pkiBody);
    }


    /**
     * Create Certification Comfirm and send.
     * - certStatusList is coming from user.
     * - CertificationBuilder(bodyContent) creates Confirmation Body
     * - IMsgBuilder Creates header, protection, PKIMessage and CmpPacket
     */
    private EPKIMessage createCertConfirm(List<ICertificationParam> handled) throws ESYAException {
        List<CertStatus> certStatuses = new ArrayList<CertStatus>();
        for (ICertificationParam certificationParam : handled)
            certStatuses.add(certificationParam.getAcceptanceStatus().getObject());

        PKIBody pkiBody = bodyContent.createCertConfBody(certStatuses);
        EPKIHeader confirmHeader = msgBuilder.createPkiHeader(response.getHeader());
        return msgBuilder.createCmpMessage(confirmHeader, new EPKIBody(pkiBody));
    }

    /**
     * Receive PKIConf so we can end Protocol successfully.(we may not get PKIConf if anything fails on protocol)
     *
     * @param certConfirmRequest
     */

    private void sendCertConfirmAndReceivePkiConf(EPKIMessage certConfirmRequest) throws CMPConnectionException, CMPProtocolException, ESYAException {

        EPKIMessage pkiConfResponse = connection.sendPKIMessageAndReceiveResponse(certConfirmRequest);
        try {
            msgBuilder.verifyPkiConfBody(pkiConfResponse.getBody());
        } catch (CMPProtocolException e) {
            protocolConnection.sendErrorMsg(e);
            throw e;
        }
        logger.info("PKIConf is received");
    }

    public void setServiceConfigType(CmpConfigType cmpConfigType){
        msgBuilder.setServiceConfigType(cmpConfigType);
    }
}
