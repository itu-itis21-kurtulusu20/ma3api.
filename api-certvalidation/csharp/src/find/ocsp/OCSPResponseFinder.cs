using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp
{
    /**
     * Base class for OCSP Response Finders.
     *
     * @author IH
     */
    public abstract class OCSPResponseFinder : Finder
    {
        protected abstract EOCSPResponse _findOCSPResponse(ECertificate aSertifika, ECertificate aIssuerCert);

        public EOCSPResponse findOCSPResponse(ECertificate aCertificate, ECertificate aIssuerCert)
        {
            return _findOCSPResponse(aCertificate, aIssuerCert);
        }

        public ItemSource<EOCSPResponse> findOCSPSource(ECertificate aCertificate, ECertificate aIssuerCert)
        {
            return
                new ListItemSource<EOCSPResponse>(
                    new List<EOCSPResponse>(new[] {_findOCSPResponse(aCertificate, aIssuerCert)}));
        }
    }
}