using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl
{
    /**
     * Base class for crl finders
     */
    public abstract class CRLFinder : Finder
    {
        protected abstract List<ECRL> _findCRL(ECertificate aCertificate);

        public List<ECRL> findCRL(ECertificate aCertificate)
        {
            return _findCRL(aCertificate);
        }
        public ItemSource<ECRL> findCRLSource(ECertificate aCertificate)
        {
            List<ECRL> crlList = _findCRL(aCertificate);
            return new ListItemSource<ECRL>(crlList);
        }
    }
}
