package tr.gov.tubitak.uekae.esya.cmpapi.base20;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIBody;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIFailureInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.ErrorMsgContent;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPConnectionException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.PKIMessageType;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.content.IMsgBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.httplayer.IConnection;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2013 <br>
 * <b>Date</b>: 5/16/13 - 1:43 PM <p>
 * <b>Description</b>: <br>
 */
public class ProtocolConnection {
    private static final Logger logger = LoggerFactory.getLogger(ProtocolConnection.class);


    private IConnection connection;
    private IMsgBuilder msgBuilder;

    public ProtocolConnection(IConnection connection, IMsgBuilder msgBuilder) {
        this.connection = connection;
        this.msgBuilder = msgBuilder;
    }

    /**
     * Receive Response, check and extract (indeed more work than method name implies, refactor need here)
     * - receive response
     * - IMsgBuilder verifies response
     * - if it's error message, send confirm, finish protocol and raise exception
     * @return
     */
    public EPKIMessage sendPKIMessageAndReceiveResponse(EPKIMessage request) throws CMPConnectionException, CMPProtocolException, ESYAException {
        try {
            EPKIMessage response = connection.sendPKIMessageAndReceiveResponse(request);
            msgBuilder.verifyResponse(request, response);
            checkErrorContent(response.getBody());
            return response;
        } catch (CMPConnectionException e) {
            logger.error("Cannot Send Request:" + e.getMessage(), e);
            throw e;
        } catch (CMPProtocolException e) {
            sendErrorMsg(EPKIFailureInfo.badDataFormat, "Cannot Read Response:" + e.getMessage());
            throw e;
        }
    }

    public void sendErrorMsg(EPKIFailureInfo failureInfo, String details) throws CMPConnectionException, CMPProtocolException, ESYAException {
        sendErrorMsg(new CMPProtocolException(failureInfo, details));

    }

    public void sendErrorMsg(CMPProtocolException e) throws CMPConnectionException, CMPProtocolException, ESYAException {
        EPKIMessage epkiMessage = connection.sendPKIMessageAndReceiveResponse(
                msgBuilder.createErrorMessage(msgBuilder.createPkiHeader(),  e)
        );
        PKIMessageType responseType = PKIMessageType.getPKIMessageType(epkiMessage.getChoiceID());
        if(responseType != PKIMessageType.PKICONF)
            logger.warn("Received "+responseType+" Expected:"+PKIMessageType.PKICONF);
        connection.finish();

    }

    /**
     * check error content inside incoming message
     * send PKIConf, end connection and raise exception if incoming Body type is ErrorMsg.
     * @param body
     */
    public void checkErrorContent(EPKIBody body) {
        if (msgBuilder.isErrorMsg(body)) {
            msgBuilder.reportErrorMessage((ErrorMsgContent) body.getObject().getElement());
            connection.finish();
            throw new RuntimeException("Received Error Message, Protocol Ended");
        }
    }

    public void finish() {
        connection.finish();
    }
}
