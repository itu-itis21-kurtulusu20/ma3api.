package tr.gov.tubitak.uekae.esya.cmpapi.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIBody;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIHeader;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.esya.EESYAServiceConfigType;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.cmp.ErrorMsgContent;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIBody;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CmpConfigType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.IPKCS10Param;
import tr.gov.tubitak.uekae.esya.cmpapi.base.content.IMsgBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base.content.PKCS10Builder;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.IProtectionTrustProvider;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.IConnection;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.PKIMessagePacket;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.PKIMessageTCPException;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Dec 1, 2010 - 2:00:46 PM <p>
 * <b>Description</b>: <br>
  */

public abstract class BasePKCS10Protocol implements IProtocol{
    private static final Logger logger = LoggerFactory.getLogger(BasePKCS10Protocol.class);
    private IConnection connection;
    private IMsgBuilder msgBuilder;
    private IPKCS10Param pkcs10Param;
    private IProtectionTrustProvider protectionTrustProvider;
    private PKCS10Builder pkcs10Builder;
    private PKIMessagePacket requestPacket;
    private PKIMessagePacket responsePacket;
    private ECertificate resultCertificate;

    public BasePKCS10Protocol(IConnection connection, IMsgBuilder msgBuilder,
                              IPKCS10Param pkcs10Param,
                              IProtectionTrustProvider protectionTrustProvider) throws CMPProtocolException {
        this.msgBuilder = msgBuilder;
        this.pkcs10Param = pkcs10Param;
        this.protectionTrustProvider = protectionTrustProvider;
        this.connection = connection;
        this.pkcs10Builder = msgBuilder.createPKCS10Builder();
    }

    public void runProtocol() {
        logger.info("Protocol is starting...");

        createRequest();

        connection.connect();

        connection.sendMessage(requestPacket);

        receiveResponse();

        connection.finish();

        logger.info("Protocol is finished successfully.");
    }

    private void createRequest() {
        EPKIHeader requestHeader = msgBuilder.createPkiHeader();

        PKIBody pkiBody = pkcs10Builder.createPKCS10Body(pkcs10Param);
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
            resultCertificate = pkcs10Builder.extractResponse(pkiResponse.getBody().getObject());
        } catch (CMPProtocolException e) {
            PKIMessagePacket errorMessage = msgBuilder.createErrorMessage(requestPacket.getPkiMessage().getHeader(), e, protectionTrustProvider);
            connection.sendMessage(errorMessage);
            connection.finish();
            throw new ESYARuntimeException("CMPProtocolException in Response: " + e.getMessage(), e);
        }
    }


    public ECertificate getResultCertificate() {
        return resultCertificate;
    }

    public void setServiceConfigType(CmpConfigType cmpConfigType){
        msgBuilder.setServiceConfigType(cmpConfigType);
    }
}
