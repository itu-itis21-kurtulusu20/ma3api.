using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.signature.certval;

namespace tr.gov.tubitak.uekae.esya.api.signature.attribute
{
    public class CertValidationReferences
    {
        readonly List<CertificateSearchCriteria> certificateReferences;
        readonly List<CRLSearchCriteria> crlReferences;
        readonly List<OCSPSearchCriteria> ocspReferences;

        public CertValidationReferences(List<CertificateSearchCriteria> certificateReferences, List<CRLSearchCriteria> crlReferences, List<OCSPSearchCriteria> ocspReferences)
        {
            this.certificateReferences = certificateReferences;
            this.crlReferences = crlReferences;
            this.ocspReferences = ocspReferences;
        }

        public List<CertificateSearchCriteria> getCertificateReferences()
        {
            return certificateReferences;
        }

        public List<CRLSearchCriteria> getCrlReferences()
        {
            return crlReferences;
        }

        public List<OCSPSearchCriteria> getOcspReferences()
        {
            return ocspReferences;
        }
    }
}
