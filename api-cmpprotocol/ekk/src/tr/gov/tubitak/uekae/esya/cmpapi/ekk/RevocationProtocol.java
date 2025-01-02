package tr.gov.tubitak.uekae.esya.cmpapi.ekk;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.cmpapi.base.BaseRevocationProtocol;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.IRevocationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ProtocolType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.IProtectionTrustProvider;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.IConnection;

import java.util.List;

/**
 <b>Author</b>    : zeldal.ozdemir <br>
 <b>Project</b>   : MA3   <br>
 <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 <b>Date</b>: 3/26/12 - 3:22 PM <p>
 <b>Description</b>: <br>
 */

public class RevocationProtocol extends BaseRevocationProtocol {
    public RevocationProtocol(IConnection connection,
                              EName sender, EName recipient,
                              List<IRevocationParam> revocationParams,
                              IProtectionTrustProvider protectionTrustProvider) throws CMPProtocolException {
        super(connection,
                new MsgBuilder(ProtocolType.REVOCATIONPROTOCOL, sender, recipient ),
                revocationParams,
                protectionTrustProvider);
    }
}
