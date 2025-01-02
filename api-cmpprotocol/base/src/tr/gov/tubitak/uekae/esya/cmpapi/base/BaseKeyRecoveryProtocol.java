package tr.gov.tubitak.uekae.esya.cmpapi.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIBody;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIHeader;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAServiceConfigType;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.cmp.ErrorMsgContent;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIBody;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CmpConfigType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificationAcceptanceStrategy;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.IKeyRecoveryParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base.content.IMsgBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base.content.KeyRecoveryBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.IProtectionTrustProvider;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.IConnection;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.PKIMessagePacket;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.PKIMessageTCPException;

import java.util.List;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Dec 1, 2010 - 2:00:46 PM <p>
 * <b>Description</b>: <br>
  */

public abstract class BaseKeyRecoveryProtocol implements IProtocol{
    private static final Logger logger = LoggerFactory.getLogger(BaseKeyRecoveryProtocol.class);
    private IConnection connection;
    private IMsgBuilder msgBuilder;
    private List<? extends IKeyRecoveryParam> keyRecoveryParams;
    private IProtectionTrustProvider protectionTrustProvider;
    private ICertificationAcceptanceStrategy acceptanceStrategy;
    private KeyRecoveryBuilder bodyContent;
    private PKIMessagePacket requestPacket;
    private PKIMessagePacket confPacket;
    private PKIMessagePacket responsePacket;

    public BaseKeyRecoveryProtocol(IConnection connection, IMsgBuilder msgBuilder,
                                     List<? extends IKeyRecoveryParam> keyRecoveryParams,
                                     IProtectionTrustProvider protectionTrustProvider) throws CMPProtocolException {
        this.msgBuilder = msgBuilder;
        this.keyRecoveryParams = keyRecoveryParams;
        this.protectionTrustProvider = protectionTrustProvider;
        this.connection = connection;
        this.bodyContent = msgBuilder.createKeyRecoveryBuilder();
    }

    public void runProtocol() {
        logger.info("Protocol is starting...");

        createRequest();

        connection.connect();

        connection.sendMessage(requestPacket);

        receiveResponse();

        connection.finish();
        
        logger.info("Protocol is finished Successfully.");
    }

    private void createRequest() {
        EPKIHeader requestHeader = msgBuilder.createPkiHeader();
        for (IKeyRecoveryParam keyRecoveryParam : keyRecoveryParams)
            bodyContent.addCertReqMsg(keyRecoveryParam);
        PKIBody pkiBody = bodyContent.createCertReqBody(bodyContent.getCertReqMsgs());
        requestPacket = msgBuilder.createCmpPacket(requestHeader, new EPKIBody(pkiBody), protectionTrustProvider);
        logger.info("Request is created");
    }


    private void receiveResponse() {
        try {
            responsePacket = connection.readPKIMessagePacket();
        } catch (PKIMessageTCPException e) {
            connection.sendErrorMessage(e);
            throw new ESYARuntimeException("ProtocolException In TCP Layer:" + e.getMessage(), e);
        }

        EPKIMessage pkiResponse = responsePacket.getPkiMessage();
        try {
            msgBuilder.verifyResponse(requestPacket.getPkiMessage(), pkiResponse, protectionTrustProvider);

            if (msgBuilder.isErrorMsg(pkiResponse.getBody())) {
                msgBuilder.reportErrorMessage((ErrorMsgContent) pkiResponse.getBody().getObject().getElement());
                PKIMessagePacket conf = msgBuilder.createPkiConfMessage(requestPacket.getPkiMessage().getHeader(), protectionTrustProvider);
                connection.sendMessage(conf);
                connection.finish();
                throw new ESYARuntimeException("Received Error Message, Protocol Will End:");
            }
            keyRecoveryParams = bodyContent.extractResponse(pkiResponse.getBody().getObject());
        } catch (CMPProtocolException e) {
            PKIMessagePacket errorMessage = msgBuilder.createErrorMessage(requestPacket.getPkiMessage().getHeader(), e, protectionTrustProvider);
            connection.sendMessage(errorMessage);
            connection.finish();
            throw new ESYARuntimeException("CMPProtocolException in Response: " + e.getMessage(), e);
        }
        logger.info("Response is received");

    }

    public void setServiceConfigType(CmpConfigType cmpConfigType){
        msgBuilder.setServiceConfigType(cmpConfigType);
    }

}
