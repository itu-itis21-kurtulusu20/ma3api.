using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.cross
{
    /**
     * <p>Base class for finding Cross Certificates</p>
     * @author IH
     */
    public abstract class CrossCertificateFinder : Finder
    {
        public List<ECertificate> findCrossCertificate()
        {
            return _findCrossCertificate();
        }

        protected abstract List<ECertificate> _findCrossCertificate();

    }
}
