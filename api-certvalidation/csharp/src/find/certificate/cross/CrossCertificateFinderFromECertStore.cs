using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.template;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.cross
{
    /**
     * Find cross certificate in ESYA Certificate Store
     */
    public class CrossCertificateFinderFromECertStore : CrossCertificateFinder
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        
        /**
         * Depoda çapraz sertifika bulur
         */
        protected override List<ECertificate> _findCrossCertificate()
        {
            List<ECertificate> certs = new List<ECertificate>();
            try
            {
                CertStore certStore = StoreFinder.createCertStore(mParameters, mParentSystem.getDefaultStorePath());
                CertStoreRootCertificateOps certStoreRootCertOps = new CertStoreRootCertificateOps(certStore);
                List<DepoKokSertifika> certStoreRootCerts = certStoreRootCertOps.listStoreRootCertificates(new CertificateSearchTemplate(), null, _guvenlikSeviyeleriniAl());
                foreach (DepoKokSertifika sertifika in certStoreRootCerts)
                {
                    try
                    {
                        ECertificate certificate = new ECertificate(sertifika.getValue());
                        certs.Add(certificate);
                    }
                    catch (Exception aEx)
                    {
                        logger.Error("Depodan alinan sertifika olusturulurken hata olustu", aEx);
                    }
                }
            }
            catch (Exception aEx)
            {
                logger.Error(aEx);
            }

            return certs;

        }


        protected /*GuvenlikSeviyesi*/SecurityLevel[] _guvenlikSeviyeleriniAl()
        {
            //tum guvenlik seviyesindekileri getir
            return null;
        }
    }
}
