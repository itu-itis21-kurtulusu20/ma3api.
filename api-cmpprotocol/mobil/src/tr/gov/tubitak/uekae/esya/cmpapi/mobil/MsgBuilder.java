package tr.gov.tubitak.uekae.esya.cmpapi.mobil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.asn.x509.Name;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ConfigTypeTurkcell;
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

public class MsgBuilder extends AMsgBuilder<ConfigTypeTurkcell> implements IMsgBuilder<ConfigTypeTurkcell> {
    private static final Logger logger = LoggerFactory.getLogger(MsgBuilder.class);

    public MsgBuilder(ProtocolType protocolType, EName sender, EName recipient) {
        super(protocolType,sender,recipient);
    }

}
