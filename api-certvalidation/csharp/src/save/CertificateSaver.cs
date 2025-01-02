using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.save
{
    /**
     * Base class for certificate saver classes 
     */
    public abstract class CertificateSaver : Saver
    {
        /**
        * Sertifikayı alt sınıflarda belirtildiği şekilde kaydeder
        */
        public void addCertificate(ECertificate aCertificate)
        {
            _addCertificate(aCertificate);
        }

        protected abstract void _addCertificate(ECertificate aCertificate);

    }
}
