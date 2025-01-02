using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.save
{
    public class CertStoreOCSPResponseSaver : OCSPResponseSaver
    {

        public CertStoreOCSPResponseSaver()
            : base()
        {

        }
        
        protected override void _addOCSP(EOCSPResponse aOcspResponse, ECertificate aCert)
        {
            CertStore depo;
            try
            {
                depo = new CertStore();
            }
            catch (CertStoreException aEx)
            {
                LOGGER.Error("Sertifika deposuna ulaşılamadı", aEx);
                throw new ESYAException("Sertifika deposuna ulaşılamadı", aEx);
            }

            CertStoreOCSPOps ocspOps = new CertStoreOCSPOps(depo);

            try
            {
                ocspOps.writeOCSPResponseAndCertificate(aOcspResponse, aCert);
            }
            catch (CertStoreException aEx)
            {
                LOGGER.Error("Depoya sertifika yazılırken hata oluştu", aEx);
                throw new ESYAException("Depoya sertifika yazılırken hata oluştu", aEx);
            }
        }
    }
}
