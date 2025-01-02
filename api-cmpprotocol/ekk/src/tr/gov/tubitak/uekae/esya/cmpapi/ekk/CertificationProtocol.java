package tr.gov.tubitak.uekae.esya.cmpapi.ekk;

import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;
import tr.gov.tubitak.uekae.esya.cmpapi.base.BaseCertificationProtocol;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.CMPProtocolException;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificationAcceptanceStrategy;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ICertificationParam;
import tr.gov.tubitak.uekae.esya.cmpapi.base.common.ProtocolType;
import tr.gov.tubitak.uekae.esya.cmpapi.base.protection.IProtectionTrustProvider;
import tr.gov.tubitak.uekae.esya.cmpapi.base.tcplayer.IConnection;

import java.util.List;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Dec 2, 2010 - 11:20:34 AM <p>
 * <b>Description</b>: <br>
 */

public class CertificationProtocol extends BaseCertificationProtocol {
    public CertificationProtocol(IConnection connection,
                                 EName sender, EName recipient,
                                 List<ICertificationParam> certificationParams,
                                 IProtectionTrustProvider protectionTrustProvider, 
                                 ICertificationAcceptanceStrategy acceptanceStrategy) throws CMPProtocolException {
        super(  connection,
                new MsgBuilder(ProtocolType.CERTIFICATIONPROTOCOL, sender, recipient),
                certificationParams, protectionTrustProvider, acceptanceStrategy);
    }
}
