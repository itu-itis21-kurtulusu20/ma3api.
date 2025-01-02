using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.signature.attribute
{
    public class CertValidationValues
    {
        readonly IList<ECertificate> certificates;
        readonly IList<ECRL> crls;
        readonly IList<EBasicOCSPResponse> ocspResponses;

        public CertValidationValues(IList<ECertificate> certificates, IList<ECRL> crls, IList<EBasicOCSPResponse> ocspResponses)
        {
            this.certificates = certificates;
            this.crls = crls;
            this.ocspResponses = ocspResponses;
        }

        public IList<ECertificate> getCertificates()
        {
            return certificates;
        }

        public IList<ECRL> getCrls()
        {
            return crls;
        }

        public IList<EBasicOCSPResponse> getOcspResponses()
        {
            return ocspResponses;
        }
    }
}
