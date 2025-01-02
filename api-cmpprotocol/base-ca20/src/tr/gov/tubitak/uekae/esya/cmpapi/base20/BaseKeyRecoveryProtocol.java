package tr.gov.tubitak.uekae.esya.cmpapi.base20;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIBody;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIFailureInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIHeader;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIBody;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPConnectionException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.IKeyRecoveryParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.content.IMsgBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.content.KeyRecoveryBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.httplayer.IConnection;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.util.LocaleUtility;

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
    private IMsgBuilder msgBuilder;
    private List<? extends IKeyRecoveryParam> keyRecoveryParams;
    private KeyRecoveryBuilder bodyContent;
    private EPKIMessage request;
    private EPKIMessage response;
    private final ProtocolConnection protocolConnection;
	private String locale;

/*    private EPKIMessage certConfirmRequest;
    private EPKIMessage pkiConfResponse;*/

    public BaseKeyRecoveryProtocol(IConnection connection, IMsgBuilder msgBuilder,
                                     List<? extends IKeyRecoveryParam> keyRecoveryParams) throws CMPProtocolException {
        this.msgBuilder = msgBuilder;
        this.keyRecoveryParams = keyRecoveryParams;
        this.bodyContent = msgBuilder.createKeyRecoveryBuilder();
        protocolConnection = new ProtocolConnection(connection, msgBuilder);

    }

    // todo ramço bakıp ilklendirme ile seviştirip manyaq hale getirecek
    public void runProtocol() throws CMPConnectionException, CMPProtocolException, ESYAException {
        logger.info("Protocol is starting...");

	    if(locale!=null){
		    //Burda sunucunun bu istek için kullacağı dili belirliyoruz.
		    request = LocaleUtility.createLocaleSetRequest(locale, msgBuilder);
		    response = protocolConnection.sendPKIMessageAndReceiveResponse(request);
	    }

        EPKIHeader requestHeader = msgBuilder.createPkiHeader();
        for (IKeyRecoveryParam keyRecoveryParam : keyRecoveryParams)
            bodyContent.addCertReqMsg(keyRecoveryParam);

        PKIBody pkiBody = bodyContent.createCertReqBody(bodyContent.getCertReqMsgs());
        request = msgBuilder.createCmpMessage(requestHeader, new EPKIBody(pkiBody));
        response = protocolConnection.sendPKIMessageAndReceiveResponse(request);
        logger.info("Response is received");

        try {
            keyRecoveryParams = bodyContent.extractResponse(response.getBody().getObject());
        } catch (CMPProtocolException e) {
            protocolConnection.sendErrorMsg(EPKIFailureInfo.badDataFormat, "Unexpected Response");
            throw e;
        }
        protocolConnection.finish();
        logger.info("Protocol is finished successfully.");
    }

	public void setLocaleTag(String localeTag){
		this.locale = localeTag;
	}



}
