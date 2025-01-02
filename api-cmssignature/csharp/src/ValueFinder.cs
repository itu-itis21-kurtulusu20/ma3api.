
using tr.gov.tubitak.uekae.esya.api.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature
{
    public interface ValueFinder
    {
        ECertificateValues findCertValues(ECompleteCertificateReferences aRefs);
        ERevocationValues findRevocationValues(ECompleteRevocationReferences aRefs);

    }

}