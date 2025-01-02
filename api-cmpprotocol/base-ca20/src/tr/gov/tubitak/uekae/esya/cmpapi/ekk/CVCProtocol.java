package tr.gov.tubitak.uekae.esya.cmpapi.ekk;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.BaseCertificationProtocol;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ICertificationAcceptanceStrategy;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ICertificationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ProtocolType;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.httplayer.IConnection;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.protection.IProtectionTrustProvider;
import tr.gov.tubitak.uekae.esya.cmpapi.esya.MsgBuilder;

import java.util.List;

/**
 <b>Author</b>    : zeldal.ozdemir <br>
 <b>Project</b>   : MA3   <br>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 <b>Date</b>: 5/16/11 - 2:21 PM <p>
 <b>Description</b>: <br>
 */

public class CVCProtocol extends BaseCertificationProtocol {
    /**
     Common parameters must be supllied to execute protocol

     @param connection
     @param cardName
     @param recipient
     @param certificationParams
     @param protectionTrustProvider
     @param acceptanceStrategy
     @throws tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException
     */
    public CVCProtocol(IConnection connection,
                       EName cardName,
                       EName recipient,
                       List<ICertificationParam> certificationParams,
                       IProtectionTrustProvider protectionTrustProvider,
                       ICertificationAcceptanceStrategy acceptanceStrategy) throws CMPProtocolException {
        super(connection,
                new MsgBuilder(ProtocolType.CVCPROTOCOL, cardName, recipient,protectionTrustProvider),
                certificationParams,
                acceptanceStrategy);
    }

    public CVCProtocol(IConnection connection,
                       EName cardName,
                       EName recipient,
                       List<ICertificationParam> certificationParams,
                       IProtectionTrustProvider protectionTrustProvider,
                       ICertificationAcceptanceStrategy acceptanceStrategy,boolean allFailOnSingleFail) throws CMPProtocolException {
        super(connection,
                new MsgBuilder(ProtocolType.CVCPROTOCOL, cardName, recipient,protectionTrustProvider),
                certificationParams,
                acceptanceStrategy,allFailOnSingleFail);
    }


}
