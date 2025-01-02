package tr.gov.tubitak.uekae.esya.cmpapi.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.ECertStatus;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIBody;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIHeader;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAServiceConfigType;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.cmp.CertStatus;
import tr.gov.tubitak.uekae.esya.asn.cmp.ErrorMsgContent;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIBody;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.*;
import tr.gov.tubitak.uekae.esya.cmpapi.base.content.CertificationBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base.content.IMsgBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.IProtectionTrustProvider;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.IConnection;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.MessageType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.PKIMessagePacket;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.PKIMessageTCPException;

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

public abstract class BaseCertificationProtocol implements IProtocol{
    private static final Logger logger = LoggerFactory.getLogger(BaseCertificationProtocol.class);
    /**
     * Connnection layer for Cmp protocol, this implementations will be used to send/recevice messages for any layer.
     */
    private IConnection connection;
    private IMsgBuilder msgBuilder;
    private List<ICertificationParam> certificationParams;
    private IProtectionTrustProvider protectionTrustProvider;
    private ICertificationAcceptanceStrategy acceptanceStrategy;
    private CertificationBuilder bodyContent;
    private PKIMessagePacket requestPacket;
    private PKIMessagePacket confPacket;
    private PKIMessagePacket responsePacket;
    private List<ICertificationParam> certificationResult;
    private boolean allFailOnSingleFail = true;

    /**
     * Common parameters must be supllied to execute protocol
     * @param connection
     * @param msgBuilder
     * @param certificationParams
     * @param protectionTrustProvider
     * @param acceptanceStrategy
     * @throws CMPProtocolException
     */
    public BaseCertificationProtocol(IConnection connection, IMsgBuilder msgBuilder,
                                     List<ICertificationParam> certificationParams,
                                     IProtectionTrustProvider protectionTrustProvider,
                                     ICertificationAcceptanceStrategy acceptanceStrategy) throws CMPProtocolException {
        this.msgBuilder = msgBuilder;
        this.certificationParams = certificationParams;
        this.protectionTrustProvider = protectionTrustProvider;
        this.acceptanceStrategy = acceptanceStrategy;
        this.connection = connection;
        this.bodyContent = msgBuilder.createCertificationBuilder();
    }

    public BaseCertificationProtocol(IConnection connection, IMsgBuilder msgBuilder,
                                     List<ICertificationParam> certificationParams,
                                     IProtectionTrustProvider protectionTrustProvider,
                                     ICertificationAcceptanceStrategy acceptanceStrategy,boolean allFailOnSingleFail) throws CMPProtocolException {
        this.msgBuilder = msgBuilder;
        this.certificationParams = certificationParams;
        this.protectionTrustProvider = protectionTrustProvider;
        this.acceptanceStrategy = acceptanceStrategy;
        this.connection = connection;
        this.bodyContent = msgBuilder.createCertificationBuilder();
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
    public void runProtocol() {
        logger.info("Protocol is starting...");

        /* prepeare and send request */
        sendRequest();

        List<ECertStatus> certStatusList = receiveResponse();

        try {
            /* Since user got Certificates if anything happens after Receiving response
                we must  inform user to rollback certificates. Because CA will revoke these certificates
                 if any error occurs while CertConfirm-PKIConf, user must know that */
            sendCertConfirm(certStatusList);
            receivePkiConf();
        } catch (RuntimeException e) {
            /* inform user that something goes wrong after receiving certificates and throw exception.*/
            acceptanceStrategy.rollbackCertificates(certificationResult);
            throw e;
        }
        connection.finish();
        logger.info("Protocol is finished successfully.");
    }

    /**
     *  send request with opening connection,
     *  i intentionally put open connection after createRequest, Because no need to open connection
     *  if user supplies wrong Certification parameters
     */
    private void sendRequest() {
        requestPacket = createRequest();

        connection.connect();

        connection.sendMessage(requestPacket);
    }

    /**
     * Here we create PKIRequest to send CA,
     * - IMsgBuilder implementations creates header, empty Body
     * - CertificationBuilder(bodyContent) creates CertReqMsg for each ICertificationParam
     * - IMsgBuilder create CmpPacket with adding Protection using given IProtectionTrustProvider implementation
     */
    private PKIMessagePacket createRequest() {
        EPKIHeader requestHeader = msgBuilder.createPkiHeader();
        for (ICertificationParam certificationParam : certificationParams)
            bodyContent.addCertReqMsg(certificationParam);
        PKIBody pkiBody = bodyContent.createCertReqBody(bodyContent.getCertReqMsgs());
        logger.info("Request is created");
        return msgBuilder.createCmpPacket(requestHeader, new EPKIBody(pkiBody ), protectionTrustProvider);
    }

    /**
     * Receive Response, check and extract (indeed more work than method name implies, refactor need here)
     * - receive response
     * - IMsgBuilder verifies response
     * - if it's error message, send confirm, finish protocol and raise exception
     * - CertificationBuilder(bodyContent) extract response
     * - Run ICertificationAcceptanceStrategy to check whether user accept certificates or not.
     * since ICertificationAcceptanceStrategy is parameter.
     * - if user didnt accept certificates (invalid certificates, fail to write into smardcard etc...)
     * we send back error message and finish protocol.
     * @return
     */
    private List<ECertStatus> receiveResponse() {
        try {
            responsePacket = connection.readPKIMessagePacket();
        } catch (PKIMessageTCPException e) {
            connection.sendErrorMessage(e);
            throw new ESYARuntimeException("ProtocolException In Connection Layer:" + e.getMessage(), e);
        }
        /* we may get Error Message from CMP Connection Layer */
        if(responsePacket.getMessageType() == MessageType.errorMsgRep ) {
            connection.close();
            throw new ESYARuntimeException("MessageType.errorMsgRep In Connection Layer");
        }


        EPKIMessage pkiResponse = responsePacket.getPkiMessage();
        try {
            /* Verifying Header, Protection etc with request packet */
            msgBuilder.verifyResponse(requestPacket.getPkiMessage(), pkiResponse, protectionTrustProvider);

            /* if we get Error Message inside PKIBody we must end protocol and raise exception for user*/
            checkErrorContent(pkiResponse.getBody(),requestPacket.getPkiMessage().getHeader());

            /* extract response */
            this.certificationResult = bodyContent.extractResponse(pkiResponse.getBody().getObject(),allFailOnSingleFail);
        } catch (CMPProtocolException e) {
            PKIMessagePacket errorMessage = msgBuilder.createErrorMessage(requestPacket.getPkiMessage().getHeader(), e, protectionTrustProvider);
            connection.sendMessage(errorMessage);
            connection.finish();
            throw new ESYARuntimeException("CMPProtocolException in Response: " + e.getMessage(), e);
        }
        logger.info("Response is received");
        try {
            return acceptanceStrategy.acceptCertificates(certificationResult);
        } catch (RuntimeException e) {
            connection.sendMessage(
                    msgBuilder.createErrorMessage(
                            requestPacket.getPkiMessage().getHeader(),
                            new CMPProtocolException(e, null),
                            protectionTrustProvider));
            connection.finish();
            throw e;
        }
    }

    /**
     * Create Certification Comfirm and send.
     * - certStatusList is coming from user.
     * - CertificationBuilder(bodyContent) creates Confirmation Body
     * - IMsgBuilder Creates header, protection, PKIMessage and CmpPacket
     * @param certStatusList
     */
    private void sendCertConfirm(List<ECertStatus> certStatusList) {
        List<CertStatus> certStatuses = new ArrayList<CertStatus>();
        for (ECertStatus eCertStatus : certStatusList)
            certStatuses.add(eCertStatus.getObject());

        PKIBody pkiBody = bodyContent.createCertConfBody(certStatuses);
        EPKIHeader confirmHeader = msgBuilder.createPkiHeader(responsePacket.getPkiMessage().getHeader());
        this.confPacket = msgBuilder.createCmpPacket(confirmHeader, new EPKIBody(pkiBody), protectionTrustProvider);
        connection.sendMessage(confPacket);
        logger.info("CertConfirmation has sent");
    }

    /**
     * Receive PKIConf so we can end Protocol successfully.(we may not get PKIConf if anything fails on protocol)
     */

    private void receivePkiConf() {
        PKIMessagePacket pkiConfPacket = null;
        try {
            pkiConfPacket = connection.readPKIMessagePacket();
        } catch (PKIMessageTCPException e) {
            connection.sendErrorMessage(e);
            throw new CMPConnectionException("ProtocolException In TCP Layer:" + e.getMessage(), e);
        }
        EPKIMessage pkiConfMessage = pkiConfPacket.getPkiMessage();
        EPKIHeader pkiConfHeader = msgBuilder.createPkiHeader(confPacket.getPkiMessage().getHeader());
        try {
            msgBuilder.verifyResponse(confPacket.getPkiMessage(), pkiConfMessage, protectionTrustProvider);

            checkErrorContent(pkiConfMessage.getBody(), pkiConfHeader);

            msgBuilder.verifyPkiConfBody(pkiConfPacket.getPkiMessage().getBody());
        } catch (CMPProtocolException e) {
            PKIMessagePacket errorMessage = msgBuilder.createErrorMessage(pkiConfHeader, e, protectionTrustProvider);
            connection.sendMessage(errorMessage);
            connection.finish();
            throw new ESYARuntimeException("CMPProtocolException in Response: " + e.getMessage(), e);
        }
        logger.info("PKIConf is received");
    }

    /**
     * check error content inside incoming message
     * send PKIConf, end connection and raise exception if incoming Body type is ErrorMsg.
     * @param body
     * @param pkiConfHeader
     */
    private void checkErrorContent(EPKIBody body , EPKIHeader pkiConfHeader) {
        if (msgBuilder.isErrorMsg(body)) {
            msgBuilder.reportErrorMessage((ErrorMsgContent) body.getObject().getElement());
            PKIMessagePacket conf = msgBuilder.createPkiConfMessage(pkiConfHeader, protectionTrustProvider);
            connection.sendMessage(conf);
            connection.finish();
            throw new ESYARuntimeException("Received Error Message, Protocol Ended");
        }
    }

    public void setServiceConfigType(CmpConfigType cmpConfigType){
        msgBuilder.setServiceConfigType(cmpConfigType);
    }
}
