using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted
{
    /**
     * Finds trusted certificates from local Certificate Store.
     *
     * @author isilh
     */
    public class TrustedCertificateFinderFromECertStore : TrustedCertificateFinder
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        protected override List<ECertificate> _findTrustedCertificate()
        {
            List<ECertificate> certificates = new List<ECertificate>();
        
            List<DepoKokSertifika> ds;
            // herhangibir kısıtlama yok, tüm kök sertifikaları getir
            try
            {
                String defaultStorePath = null;
                if (mParentSystem != null)
                    defaultStorePath = mParentSystem.getDefaultStorePath();
                CertStore certStore = StoreFinder.createCertStore(mParameters, defaultStorePath);
                CertStoreRootCertificateOps certStoreRootCertOps = new CertStoreRootCertificateOps(certStore);
                SecurityLevel[] guvenlikSeviyeleri = getSecurityLevel().ToArray();
                ds = certStoreRootCertOps.listStoreRootCertificates(null, null, guvenlikSeviyeleri);
            }
            catch (CertStoreException aEx)
            {
                logger.Error("Sertifikalar listelenirken hata olustu", aEx);
                return certificates;
            }
            foreach (DepoKokSertifika sertifika in ds)
            {
                try
                {
                    ECertificate certificate = new ECertificate(sertifika.getValue());
                    certificates.Add(certificate);
                }
                catch (Exception aEx)
                {
                    logger.Warn("Depodan alinan sertifika olusturulurken hata olustu", aEx);
                }
            }

            return certificates;
        }
    }
}
