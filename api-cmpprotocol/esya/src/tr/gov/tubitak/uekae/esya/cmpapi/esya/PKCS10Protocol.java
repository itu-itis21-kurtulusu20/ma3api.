package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.cmpapi.base.BasePKCS10Protocol;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.IPKCS10Param;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ProtocolType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.IProtectionTrustProvider;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.IConnection;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Dec 7, 2010
 * Time: 11:04:26 AM
 * To change this template use File | Settings | File Templates.
 */

public class PKCS10Protocol extends BasePKCS10Protocol {
    public PKCS10Protocol(IConnection connection,
                          EName sender, EName recipient, String senderKID,
                          IPKCS10Param pkcs10Param,
                          IProtectionTrustProvider protectionTrustProvider) throws CMPProtocolException {
        super(connection,
                new MsgBuilder(ProtocolType.PKCS10PROTOCOL, sender, recipient, senderKID),
                pkcs10Param, protectionTrustProvider);
    }
}
