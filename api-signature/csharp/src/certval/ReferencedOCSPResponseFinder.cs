using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;

namespace tr.gov.tubitak.uekae.esya.api.signature.certval
{
    public interface ReferencedOCSPResponseFinder
    {
        List<EOCSPResponse> find(OCSPSearchCriteria aCriteria);
    }
}
