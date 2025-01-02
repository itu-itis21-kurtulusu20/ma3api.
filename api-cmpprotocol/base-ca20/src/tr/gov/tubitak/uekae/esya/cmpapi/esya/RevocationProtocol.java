package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.BaseRevocationProtocol;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.IRevocationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ProtocolType;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.httplayer.IConnection;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.protection.IProtectionTrustProvider;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 13, 2010
 * Time: 3:32:52 PM
 * To change this template use File | Settings | File Templates.
 */

public class RevocationProtocol extends BaseRevocationProtocol {
    public RevocationProtocol(IConnection connection,
                              EName sender, EName recipient, String senderKID,
                              List<IRevocationParam> revocationParams,
                              IProtectionTrustProvider protectionTrustProvider) throws CMPProtocolException {
        super(connection,
                new MsgBuilder(ProtocolType.REVOCATIONPROTOCOL,sender,recipient, senderKID, protectionTrustProvider),
                revocationParams);
    }
}
