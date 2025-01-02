package tr.gov.tubitak.uekae.esya.cmpapi.base20;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIBody;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIHeader;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIBody;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPConnectionException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.IRevocationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.content.IMsgBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.content.RevocationBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.httplayer.IConnection;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.util.LocaleUtility;

import java.util.List;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Nov 11, 2010 - 3:00:16 PM <p>
 * <b>Description</b>: <br>
 * Revocation protocol. it is used to revoke certificates. inspite of Certification protocol it only runs
 * with simple send-receive. there is no protocol transaction.
 * List of IRevocationParam are using to determine which certificates should be revoked.
 */

public abstract class BaseRevocationProtocol implements IProtocol{
    private static final Logger logger = LoggerFactory.getLogger(BaseRevocationProtocol.class);
    private IMsgBuilder msgBuilder;
    private List<IRevocationParam> revocationParams;
    private RevocationBuilder bodyContent;
    private final ProtocolConnection protocolConnection;
    private EPKIMessage request;
    private EPKIMessage response;
	private String locale;

	public BaseRevocationProtocol(IConnection connection, IMsgBuilder msgBuilder,
                                     List<IRevocationParam> revocationParams ) throws CMPProtocolException {
        this.msgBuilder = msgBuilder;
        this.revocationParams = revocationParams;
        this.bodyContent = msgBuilder.createRecovationBuilder();
        protocolConnection = new ProtocolConnection(connection, msgBuilder);
    }

    public void runProtocol() throws CMPConnectionException, CMPProtocolException, ESYAException {
        logger.info("Protocol is starting...");
	    if(locale!=null){
		    //Burda sunucunun bu istek için kullacağı dili belirliyoruz.
		    request = LocaleUtility.createLocaleSetRequest(locale, msgBuilder);
		    response = protocolConnection.sendPKIMessageAndReceiveResponse(request);
	    }

        createRequest();

        response = protocolConnection.sendPKIMessageAndReceiveResponse(request);

        try {
            revocationParams = bodyContent.extractResponse(response.getBody().getObject());
        } catch (CMPProtocolException e) {
            protocolConnection.sendErrorMsg(e);
            throw e;
        }
        logger.info("Response is received");

        protocolConnection.finish();
        
        logger.info("Protocol is finished successfully.");
    }

    private void createRequest() throws ESYAException {
        EPKIHeader requestHeader = msgBuilder.createPkiHeader();
        for (IRevocationParam keyRecoveryParam : revocationParams)
            bodyContent.addRevDetails(keyRecoveryParam);
        PKIBody pkiBody = bodyContent.createRevReqBody(bodyContent.getRevDetailses());
        request = msgBuilder.createCmpMessage(requestHeader, new EPKIBody(pkiBody));
        logger.info("Request is created");
    }

	public void setLocaleTag(String localeTag){
		this.locale = localeTag;
	}

}
