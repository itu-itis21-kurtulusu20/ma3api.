package tr.gov.tubitak.uekae.esya.cmpapi.base.common;

import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIStatusInfo;
import tr.gov.tubitak.uekae.esya.asn.cmp.RevDetails;
import tr.gov.tubitak.uekae.esya.asn.crmf.CertId;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2011 <br>
 * <b>Date</b>: Dec 9, 2010 - 3:59:38 PM <p>
 * <b>Description</b>: <br>
 *     Parameters and extractors for Revocation Based protocols
 *     @see tr.gov.tubitak.uekae.esya.cmpapi.base.BaseRevocationProtocol
 */

public interface IRevocationParam {

    void addSpecificOperations(RevDetails revDetails);

    void setRevDetails(RevDetails revDetails);

    RevDetails getRevDetails();

    void extractResponse(Pair<CertId, PKIStatusInfo> revokedCerts) throws CMPProtocolException;

    PKIStatusInfo getPkiStatusInfo();
}
