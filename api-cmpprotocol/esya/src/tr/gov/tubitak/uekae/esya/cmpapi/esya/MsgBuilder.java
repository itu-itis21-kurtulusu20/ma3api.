package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1ValueParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIHeader;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ConfigTypeEsya;
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

public class MsgBuilder extends AMsgBuilder<ConfigTypeEsya> implements IMsgBuilder<ConfigTypeEsya> {
    private static final Logger logger = LoggerFactory.getLogger(MsgBuilder.class);
    private String senderKID;

    public MsgBuilder(ProtocolType protocolType, EName sender, EName recipient, String senderKID) {
        super(protocolType,sender,recipient);
        this.senderKID = senderKID;
    }

    public EPKIHeader createPkiHeader()  {
        EPKIHeader pkiHeader = super.createPkiHeader();
        try {
            pkiHeader.setSenderKID( new Asn1OctetString(senderKID) );
        } catch (Asn1ValueParseException aEx) {
            throw new RuntimeException("Error While parsing Refcode to SenderKID", aEx);
        }
        return pkiHeader;
    }
    
    public EPKIHeader createPkiHeader(EPKIHeader previousHeader)  {
        EPKIHeader pkiHeader = super.createPkiHeader(previousHeader);
        try {
            pkiHeader.setSenderKID( new Asn1OctetString(senderKID) );
        } catch (Asn1ValueParseException aEx) {
            throw new RuntimeException("Error While parsing Refcode to SenderKID", aEx);
        }
        return pkiHeader;
    }
    
}
