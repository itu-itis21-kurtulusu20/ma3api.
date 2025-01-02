using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl.delta
{
    /**
     * Base Class for delta-CRL finders
     * @author IH
     */
    public abstract class DeltaCRLFinder : Finder
    {
        public List<ECRL> findDeltaCRL(ECertificate aCertificate)
        {
            return _findDeltaCRL(aCertificate);
        }

        public List<ECRL> findDeltaCRL(ECRL aBaseCRL)
        {
            return _findDeltaCRL(aBaseCRL);
        }

        protected abstract List<ECRL> _findDeltaCRL(ECertificate aCertificate);

        protected abstract List<ECRL> _findDeltaCRL(ECRL aBaseCRL);
    }
}
