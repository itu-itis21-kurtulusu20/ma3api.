package tr.gov.tubitak.uekae.esya.cmpapi.esya;

import com.objsys.asn1j.runtime.Asn1OctetString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cmp.EPKIHeader;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ConfigTypeEsya;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.common.ProtocolType;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.content.AMsgBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.content.IMsgBuilder;
import tr.gov.tubitak.uekae.esya.cmpapi.base20.protection.IProtectionTrustProvider;

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

    public MsgBuilder(ProtocolType protocolType, EName sender, EName recipient, String senderKID,  IProtectionTrustProvider protectionTrustProvider) {
        super(protocolType,sender,recipient, protectionTrustProvider);
        this.senderKID = senderKID;
    }

    public MsgBuilder(ProtocolType protocolType, EName sender, EName recipient, String senderKID,  IProtectionTrustProvider protectionTrustProvider,boolean allFailOnSingleFail) {
        super(protocolType,sender,recipient, protectionTrustProvider,allFailOnSingleFail);
        this.senderKID = senderKID;
    }

    public MsgBuilder(ProtocolType protocolType, EName sender, EName recipient,IProtectionTrustProvider protectionTrustProvider) {
        super(protocolType,sender,recipient,protectionTrustProvider);
    }

    public MsgBuilder(ProtocolType protocolType, EName sender, EName recipient,IProtectionTrustProvider protectionTrustProvider,boolean allFailOnSingleFail) {
        super(protocolType,sender,recipient,protectionTrustProvider,allFailOnSingleFail);
    }

    public EPKIHeader createPkiHeader() {
        EPKIHeader pkiHeader;
        try {
            pkiHeader = super.createPkiHeader();
            pkiHeader.setSenderKID(new Asn1OctetString(senderKID));
        } catch (Exception aEx) {
            throw new ESYARuntimeException("Error While parsing Refcode to SenderKID", aEx);
        }
        return pkiHeader;
    }
    
    public EPKIHeader createPkiHeader(EPKIHeader previousHeader) {
        EPKIHeader pkiHeader;
        try {
            pkiHeader = super.createPkiHeader(previousHeader);
            pkiHeader.setSenderKID(new Asn1OctetString(senderKID));
        } catch (Exception aEx) {
            throw new ESYARuntimeException("Error While parsing Refcode to SenderKID", aEx);
        }
        return pkiHeader;
    }
    
}
