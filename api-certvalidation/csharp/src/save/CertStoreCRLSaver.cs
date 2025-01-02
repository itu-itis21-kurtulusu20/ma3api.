using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.save
{
    public class CertStoreCRLSaver : CRLSaver
    {
        public CertStoreCRLSaver()
            : base()
        {
            
        }

        protected override void _addCRL(ECRL sil)
        {
            CertStore store;
            try
            {
                store = StoreFinder.createCertStore(mParameters, mParentSystem.getDefaultStorePath());
            }
            catch (CertStoreException aEx)
            {
                LOGGER.Error("Sertifika deposuna ulaşılamadı", aEx);
                throw new ESYAException("Sertifika deposuna ulaşılamadı", aEx);
            }

            CertStoreCRLOps crlOps = new CertStoreCRLOps(store);

            try
            {
                // depo dizin numarasını 1 yaptım ama??
                crlOps.writeCRL(sil, 1L);
            }
            catch (CertStoreException aEx)
            {
                LOGGER.Error("Depoya sertifika yazılırken hata oluştu", aEx);
                throw new ESYAException("Depoya sertifika yazılırken hata oluştu", aEx);
            }

            try
            {
                store.closeConnection();
            }
            catch (CertStoreException e)
            {
                LOGGER.Error("Connection couldn't closed", e);
            }
        }
    }
}
