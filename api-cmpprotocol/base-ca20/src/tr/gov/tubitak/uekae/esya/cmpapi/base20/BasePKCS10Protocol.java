package tr.gov.tubitak.uekae.esya.cmpapi.base20;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIBody;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIHeader;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIMessage;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIBody;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPConnectionException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CmpConfigType;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.IPKCS10Param;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.content.IMsgBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.content.PKCS10Builder;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.httplayer.IConnection;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.util.LocaleUtility;

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
    private PKCS10Builder pkcs10Builder;
    private EPKIMessage request;
    private EPKIMessage response;
    private ECertificate resultCertificate;
    private final ProtocolConnection protocolConnection;
	private String locale;


	public BasePKCS10Protocol(IConnection connection, IMsgBuilder msgBuilder,
                              IPKCS10Param pkcs10Param) throws CMPProtocolException {
        this.msgBuilder = msgBuilder;
        this.pkcs10Param = pkcs10Param;
        this.connection = connection;
        this.pkcs10Builder = msgBuilder.createPKCS10Builder();
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
            resultCertificate = pkcs10Builder.extractResponse(response.getBody().getObject());
        } catch (CMPProtocolException e) {
            protocolConnection.sendErrorMsg(e);
            throw e;
        }
        connection.finish();

        logger.info("Protocol is finished successfully.");
    }

    private void createRequest() throws ESYAException {
        EPKIHeader requestHeader = msgBuilder.createPkiHeader();

        PKIBody pkiBody = pkcs10Builder.createPKCS10Body(pkcs10Param);
        request = msgBuilder.createCmpMessage(requestHeader, new EPKIBody(pkiBody));
        logger.info("Request is created");
    }

    public ECertificate getResultCertificate() {
        return resultCertificate;
    }

	public void setLocaleTag(String localeTag){
		this.locale = localeTag;
	}

    public void setServiceConfigType(CmpConfigType cmpConfigType){
        msgBuilder.setServiceConfigType(cmpConfigType);
    }
}
