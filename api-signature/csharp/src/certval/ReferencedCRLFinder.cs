using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.signature.certval
{
    public interface ReferencedCRLFinder
    {
        List<ECRL> find(CRLSearchCriteria aCriteria);
    }
}
