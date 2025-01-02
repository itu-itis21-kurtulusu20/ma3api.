package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.cmpapi.base.BaseKeyRecoveryProtocol;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.IKeyRecoveryParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ProtocolType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.content.IMsgBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.IProtectionTrustProvider;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.IConnection;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 13, 2010
 * Time: 10:20:11 AM
 * To change this template use File | Settings | File Templates.
 */

public class KeyRecoveryProtocol extends BaseKeyRecoveryProtocol {
    public KeyRecoveryProtocol(IConnection connection,
                               EName sender, EName recipient, String senderKID,
                               List<? extends IKeyRecoveryParam> keyRecoveryParams,
                               IProtectionTrustProvider protectionTrustProvider) throws CMPProtocolException {
        super(connection,
                new MsgBuilder(ProtocolType.KEYRECOVERYPROTOCOL, sender, recipient, senderKID), 
                keyRecoveryParams, protectionTrustProvider);
    }
}
