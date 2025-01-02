package tr.gov.tubitak.uekae.esya.cmpapi.ekk;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ConfigTypeEKK;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ProtocolType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.content.AMsgBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base.content.IMsgBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: zeldal.ozdemir
 * Date: Nov 8, 2010
 * Time: 9:11:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class MsgBuilder extends AMsgBuilder<ConfigTypeEKK> implements IMsgBuilder<ConfigTypeEKK> {
    public MsgBuilder(ProtocolType protocolType, EName sender, EName recipient,boolean allFailOnSingleFail) {
        super(protocolType,sender,recipient,allFailOnSingleFail);
    }
    public MsgBuilder(ProtocolType protocolType, EName sender, EName recipient) {
        super(protocolType,sender,recipient);
    }
}
