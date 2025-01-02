using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.template;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate
{
    /**
     * Find issuer certificate of the input certificate from ESYA Certificate Store.
     *
     * @author IH
     */
    public class CertificateFinderFromECertStore : CertificateFinder
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        public CertificateFinderFromECertStore() : base() { }

        protected override List<ECertificate> _findCertificate()
        {
            return _findCertificate(null);
        }

        protected override List<ECertificate> _findCertificate(ECertificate aCertificate)
        {
            List<ECertificate> certificates = new List<ECertificate>();
            
            // depodan gelecek sonuçları kısıtlayabilmek için issuer-subject eşitliğini zorunlu tuttuk
            CertificateSearchTemplate certSearchTemplate = new CertificateSearchTemplate();
            if (aCertificate != null)
            {
                certSearchTemplate.setSubject(aCertificate.getIssuer());
            }

            ItemSource<DepoSertifika> sertifikaItemSource = null;
            DepoSertifika depoSertifika;
            CertStore certStore = null;
            try
            {
                certStore = StoreFinder.createCertStore(mParameters, mParentSystem.getDefaultStorePath());
                CertStoreCertificateOps certStoreCertOps = new CertStoreCertificateOps(certStore);
                sertifikaItemSource = certStoreCertOps.listStoreCertificate(certSearchTemplate);
                depoSertifika = sertifikaItemSource.nextItem();
                while (depoSertifika != null)
                {
                    ECertificate esertifika = new ECertificate(depoSertifika.getValue());
                    certificates.Add(esertifika);
                    depoSertifika = sertifikaItemSource.nextItem();
                }

            }
            catch (CertStoreException aEx)
            {
                logger.Error("Sertifikalar listelenirken hata olustu", aEx);
                return null;
            }
            catch (ESYAException aEx)
            {
                logger.Error("ilk depo sertifika nesnesi alinirken hata olustu", aEx);
                return null;
            }
            finally
            {
                try
                {
                    if (sertifikaItemSource != null) 
                        sertifikaItemSource.close();
                    if (certStore != null)
                        certStore.closeConnection();
                }
                catch (CertStoreException e)
                {
                    logger.Error("Connection couldn't closed", e);
                }
            }



            return certificates;
        }

        /*
        override public List<ECertificate> searchCertificates(CertificateSearchCriteria aCriteria)
        {
            if (aCriteria == null)
                throw new ArgErrorException("Expected search criteria");


            // search criteria
            CertificateSearchTemplate certSearchTemplate = createSearchTemplate(aCriteria);

            return locate(certSearchTemplate);
        }

        protected List<ECertificate> locate(CertificateSearchTemplate aSearchTemplate)
        {
            CertStore certStore;
            try
            {
                certStore = new CertStore();
            }
            catch (CertStoreException aEx)
            {
                logger.Error("Sertifika deposuna ulasilamadi", aEx);
                return null;
            }
            CertStoreCertificateOps certStoreCertOps = new CertStoreCertificateOps(certStore);

            ItemSource<DepoSertifika> sertifikaItemSource = null;
            DepoSertifika depoSertifika;

            List<ECertificate> certificates = new List<ECertificate>();
            try
            {
                sertifikaItemSource = certStoreCertOps.listStoreCertificate(aSearchTemplate);
                depoSertifika = sertifikaItemSource.nextItem();

                while (depoSertifika != null)
                {
                    try
                    {
                        ECertificate esertifika = new ECertificate(depoSertifika.getValue());
                        certificates.Add(esertifika);
                    }
                    catch (ESYAException aEx)
                    {
                        logger.Warn("Depodan alinan sertifika olusturulurken hata olustu", aEx);
                    }
                    depoSertifika = sertifikaItemSource.nextItem();
                }
            }
            catch (CertStoreException aEx)
            {
                logger.Error("Sertifikalar listelenirken hata olustu", aEx);
                return null;
            }
            catch (ESYAException aEx)
            {
                logger.Error("Ilk depo sertifika nesnesi alinirken hata olustu", aEx);
                return null;
            }
            finally
            {
                if (sertifikaItemSource != null) sertifikaItemSource.close();
                try
                {
                    certStore.closeConnection();
                }
                catch (CertStoreException e)
                {
                    logger.Error("Connection couldn't closed", e);
                }            
            }
            return certificates;
        }
        //*/
    }
}
