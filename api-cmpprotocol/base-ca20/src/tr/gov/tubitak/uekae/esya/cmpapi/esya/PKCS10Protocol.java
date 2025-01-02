package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.BasePKCS10Protocol;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.CmpConfigType;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.IPKCS10Param;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ProtocolType;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.httplayer.IConnection;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.protection.IProtectionTrustProvider;

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
                new MsgBuilder(ProtocolType.PKCS10PROTOCOL, sender, recipient, senderKID, protectionTrustProvider),
                pkcs10Param);
    }

    public PKCS10Protocol(IConnection connection,EName sender,EName recipient, Long templateId,IPKCS10Param pkcs10Param,
                          IProtectionTrustProvider protectionTrustProvider) throws CMPProtocolException {
        super(connection,
                new MsgBuilder(ProtocolType.PKCS10PROTOCOL,sender, recipient, Long.toString(templateId), protectionTrustProvider),
                pkcs10Param);
    }



}
