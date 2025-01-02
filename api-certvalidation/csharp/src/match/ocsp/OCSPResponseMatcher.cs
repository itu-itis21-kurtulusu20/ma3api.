using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match.ocsp
{
    /**
     * @author ayetgin
     */
    public abstract class OCSPResponseMatcher : Matcher
    {
        protected abstract bool _matchOCSPResponse(ECertificate aCertificate, ECertificate aIssuer,
                                                   EOCSPResponse aOCSPResponse);

        public bool matchOCSPResponse(ECertificate aCertificate, ECertificate aIssuer, EOCSPResponse aOCSPResponse)
        {
            return (_matchOCSPResponse(aCertificate, aIssuer, aOCSPResponse));
        }
    }
}