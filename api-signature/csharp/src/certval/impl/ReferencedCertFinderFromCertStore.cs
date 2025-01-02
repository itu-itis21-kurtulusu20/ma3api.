using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper;
using Logger = log4net.ILog;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.model;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.template;
using tr.gov.tubitak.uekae.esya.api.signature.certval;

namespace tr.gov.tubitak.uekae.esya.api.signature.certval.impl
{
    /**
     * @author ayetgin
     * Finds the referenced certificate from the certificate store.
     */
    public class ReferencedCertFinderFromCertStore : ReferencedCertificateFinder
    {
        private static Logger logger = log4net.LogManager.GetLogger(typeof(ReferencedCertFinderFromCertStore));
        /**
         * Creates a certificate search template using given criteria. If digest algorithm is present,
         * then it is used for creating template; otherwise both serial and subject key identifier is
         * used if they are present.
         * @param aCriteria criteria to be used to create template
         * @return a certificate search template to be used for finding in certificate store
         */
        public CertificateSearchTemplate createSearchTemplate(CertificateSearchCriteria aCriteria)
        {
            CertificateSearchTemplate certSearchTemplate = new CertificateSearchTemplate();

            if (aCriteria.getDigestAlg() != null) {
                certSearchTemplate.setHashType(OzetTipi.fromDigestAlg(aCriteria.getDigestAlg()));
                certSearchTemplate.setHash(aCriteria.getDigestValue());
            }
            else { //*/
                /*String subject = aCriteria.getSubject();
                if (subject != null && subject.length() > 0){
                    try {
                    EName name = new EName(UtilName.string2Name(subject, true));
                    certSearchTemplate.setSubject(name.getEncoded());
                    }
                    catch (Exception e) {
                        logger.error("Error decoding certificate subject",e);
                    }
                }*/
                /*
                String issuer = aCriteria.getIssuer();
                if (issuer != null && issuer.length() > 0){
                    try {
                    EName name = new EName(UtilName.string2Name(issuer, true));
                    certSearchTemplate.setIssuer(name.getEncoded());
                    }
                    catch (Exception e) {
                        logger.error("Error decoding certificate issuer",e);
                    }
                }
                //*/
                BigInteger serial = aCriteria.getSerial();
                if (serial != null)
                    certSearchTemplate.setSerial(serial.GetData());

                if (aCriteria.getSubjectKeyIdentifier() != null)
                    certSearchTemplate.setSubjectKeyID(aCriteria.getSubjectKeyIdentifier());
            }

            return certSearchTemplate;
        }

        public List<ECertificate> find(CertificateSearchCriteria aCriteria)
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
            try {
                certStore = new CertStore();
            } catch (
                    CertStoreException aEx) {
                logger.Error("Sertifika deposuna ulaşılamadı", aEx);
                return null;
            }

            List<ECertificate> certificates = new List<ECertificate>();
            CertStoreCertificateOps certStoreCertOps = new CertStoreCertificateOps(certStore);
            try {
                certificates = certStoreCertOps.listCertificates(aSearchTemplate);
            } catch (CertStoreException e) {
                logger.Error("depodan sertifikalar listelenirken hata olustu");
                Console.WriteLine(e.StackTrace);
            }

            /* burasi zaten kodlanmis
            ItemSource<DepoSertifika> sertifikaItemSource = null;
            DepoSertifika depoSertifika;

            List<ECertificate> certificates = new ArrayList<ECertificate>();
            try {
                sertifikaItemSource = certStoreCertOps.listStoreCertificate(aSearchTemplate);
                depoSertifika = sertifikaItemSource.nextItem();

                while (depoSertifika != null) {
                    try {
                        ECertificate certificate = new ECertificate(depoSertifika.getValue());
                        certificates.add(certificate);
                    } catch (ESYAException aEx) {
                        logger.warn("Depodan alınan sertifika oluşturulurken hata oluştu", aEx);
                    }
                    depoSertifika = sertifikaItemSource.nextItem();
                }

            } catch (CertStoreException aEx) {
                logger.error("Sertifikalar listelenirken hata oluştu", aEx);
                return null;
            } catch (ESYAException aEx) {
                logger.error("İlk depo sertifika nesnesi alınırken hata oluştu", aEx);
                return null;
            } finally {
                if (sertifikaItemSource != null) sertifikaItemSource.close();
                /*try {
                    certStore.closeConnection();
                } catch (CertStoreException e) {
                    logger.error("Connection couldn't closed", e);
                }/
            }
            //*/


            // eger sertifika tablosunda bulunamadiysa koksertifika tablosuna bak
            if(certificates.Count == 0) {
                
                CertStoreRootCertificateOps certStoreRootCertificateOps = new CertStoreRootCertificateOps(certStore);
                List<DepoKokSertifika> depoKokSertifikaList = new List<DepoKokSertifika>();
                try {
                    depoKokSertifikaList = certStoreRootCertificateOps.listStoreRootCertificates(aSearchTemplate,null,null);
                }
                catch (CertStoreException aEx) {
                    logger.Error("Sertifikalar listelenirken hata oluştu", aEx);
                }
                foreach (DepoKokSertifika depoKokSertifika in depoKokSertifikaList) {
                    try {
                        ECertificate certificate = new ECertificate(depoKokSertifika.getValue());
                        certificates.Add(certificate);
                    }
                    catch (ESYAException aEx) {
                        logger.Warn("Depodan alınan sertifika oluşturulurken hata oluştu", aEx);
                    }

                }
            }

            try {
                certStore.closeConnection();
            } catch (CertStoreException e) {
                logger.Error("Connection couldn't closed", e);
            }

            return certificates;
        }
    }
}
