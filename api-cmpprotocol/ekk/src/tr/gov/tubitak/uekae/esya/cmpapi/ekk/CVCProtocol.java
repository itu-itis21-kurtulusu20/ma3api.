package tr.gov.tubitak.uekae.esya.cmpapi.ekk;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.cmpapi.base.BaseCertificationProtocol;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificationAcceptanceStrategy;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ProtocolType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.IProtectionTrustProvider;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.IConnection;

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
     @throws tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException
     */
    public CVCProtocol(IConnection connection,
                       EName cardName,
                       EName recipient,
                       List<ICertificationParam> certificationParams,
                       IProtectionTrustProvider protectionTrustProvider,
                       ICertificationAcceptanceStrategy acceptanceStrategy) throws CMPProtocolException {
        super(connection,
                new MsgBuilder(ProtocolType.CVCPROTOCOL, cardName, recipient),
                certificationParams,
                protectionTrustProvider,
                acceptanceStrategy);
    }

    public CVCProtocol(IConnection connection,
                       EName cardName,
                       EName recipient,
                       List<ICertificationParam> certificationParams,
                       IProtectionTrustProvider protectionTrustProvider,
                       ICertificationAcceptanceStrategy acceptanceStrategy,boolean allFailOnSingleFail) throws CMPProtocolException {
        super(connection,
                new MsgBuilder(ProtocolType.CVCPROTOCOL, cardName, recipient,allFailOnSingleFail),
                certificationParams,
                protectionTrustProvider,
                acceptanceStrategy,allFailOnSingleFail);
    }
}
