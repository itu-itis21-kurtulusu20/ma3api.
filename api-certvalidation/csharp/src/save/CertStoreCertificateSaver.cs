using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.save
{
    /**
     * Saves the certificates found during Certificate/CrL Validation operation to
     * the local Certificate Store.
     *
     * @author IH
     */
    public class CertStoreCertificateSaver : CertificateSaver
    {
        public CertStoreCertificateSaver() : base() { }

        protected override void _addCertificate(ECertificate aCertificate)
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
            CertStoreCertificateOps si = new CertStoreCertificateOps(depo);

            try
            {
                // depo dizin numarasını 1 yaptım ama??
                si.writeCertificate(aCertificate, 1L);
            }
            catch (CertStoreException aEx)
            {
                LOGGER.Error("Depoya sertifika yazılırken hata oluştu", aEx);
                throw new ESYAException("Depoya sertifika yazılırken hata oluştu", aEx);
            }
        }
    }
}
