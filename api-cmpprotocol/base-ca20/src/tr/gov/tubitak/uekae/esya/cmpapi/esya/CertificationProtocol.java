package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.BaseCertificationProtocol;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ICertificationAcceptanceStrategy;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ICertificationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ProtocolType;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.httplayer.IConnection;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.protection.IProtectionTrustProvider;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 2, 2010
 * Time: 11:20:34 AM
 * To change this template use File | Settings | File Templates.
 */

public class CertificationProtocol extends BaseCertificationProtocol {
    public CertificationProtocol(IConnection connection,
                                 EName sender, EName recipient, String senderKID,
                                 List<ICertificationParam> certificationParams,
                                 IProtectionTrustProvider protectionTrustProvider,
                                 ICertificationAcceptanceStrategy acceptanceStrategy) throws CMPProtocolException {
        super(  connection,
                new MsgBuilder(ProtocolType.CERTIFICATIONPROTOCOL, sender, recipient, senderKID, protectionTrustProvider),
                certificationParams, acceptanceStrategy);
    }

    public CertificationProtocol(IConnection connection,
                                  EName sender, EName recipient, String senderKID,
                                  List<ICertificationParam> certificationParams,
                                  IProtectionTrustProvider protectionTrustProvider,
                                  long cmpResponseTimeout,
                                  ICertificationAcceptanceStrategy acceptanceStrategy) throws CMPProtocolException {
        super(  connection,
                new MsgBuilder(ProtocolType.CERTIFICATIONPROTOCOL, sender, recipient, senderKID, protectionTrustProvider),
                certificationParams, acceptanceStrategy);
        msgBuilder.setCmpResponseTimeout(cmpResponseTimeout);
    }
}
