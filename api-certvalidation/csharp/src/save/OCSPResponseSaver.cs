using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.save
{
    public abstract class OCSPResponseSaver : Saver
    {
        public OCSPResponseSaver()
            : base()
        {

        }

        public void addOCSP(EOCSPResponse aOcspResponse, ECertificate aCert)
        {
            _addOCSP(aOcspResponse, aCert);
        }

        protected abstract void _addOCSP(EOCSPResponse aOcspResponse, ECertificate aCert);
    }
}
