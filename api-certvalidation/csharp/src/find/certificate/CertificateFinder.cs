using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate
{
    /**
     * Base class for certificate finders
     */
    public abstract class CertificateFinder : Finder
    {
        /**
         * Find issuer certificate of the input certificate
         * @param aCertificate input
         */
        public List<ECertificate> findCertificate(ECertificate aCertificate)
        {
            return _findCertificate(aCertificate);
        }

        /**
         * Verilen sertifikanın Ust SM sertifikasını bulur.
         * @param aCertificate Certificate
         * @return ECertificate SM Sertifikası
         */
        public ItemSource<ECertificate> findCertificateSource(ECertificate aCertificate)
        {
            //String subject = aCertificate.getSubject().stringValue();
            //DEBUGLOGYAZ(LOGNAME,QString("%1 sertifika SM sertifikası bulunacak").arg(subject));

            List<ECertificate> certs = _findCertificate(aCertificate);
            return new ListItemSource<ECertificate>(certs);
        }

        /**
         * Ust SM sertifikasını bulur.
         * Silin Ust SM sertifikasını bulacak olan classlar tarafından overwrite edilmeli.
         * @return
         */
        public List<ECertificate> findCertificate()
        {
            return _findCertificate();
        }

        /**
         * Verilen sertifikanın Ust SM sertifikasını bulur.
         * @return ECertificate SM Sertifikası
         */
        public ItemSource<ECertificate> findCertificateSource()
        {
            List<ECertificate> certs = _findCertificate();
            return new ListItemSource<ECertificate>(certs);
        }

        /**
         * Verilen sertifikanın Ust SM sertifikasını bulur.
         * Sertifikanın Ust SM sertifiaksını bulacak olan classlar tarafından overwrite edilmeli.
         * @param aSertifika
         * @return
         */
        protected abstract List<ECertificate> _findCertificate(ECertificate aSertifika);

        /**
         * Ust SM sertifikasını bulur.
         * Silin Ust SM sertifiaksını bulacak olan classlar tarafından overwrite edilmeli.
         * @return
         */
        protected virtual List<ECertificate> _findCertificate()
        {
            return null;
        }

        /*
        public abstract List<ECertificate> searchCertificates(CertificateSearchCriteria aCriteria);

        public CertificateSearchTemplate createSearchTemplate(CertificateSearchCriteria aCriteria)
        {
            CertificateSearchTemplate certSearchTemplate = new CertificateSearchTemplate();

            try
            {
                String subject = aCriteria.getSubject();
                if (subject != null && subject.Length > 0)
                    certSearchTemplate.setSubject(Encoding.Unicode.GetBytes(subject));

                String issuer = aCriteria.getIssuer();
                if (issuer != null && issuer.Length > 0)
                    certSearchTemplate.setIssuer(Encoding.Unicode.GetBytes(issuer));

                BigInteger serial = aCriteria.getSerial();
                if (serial != null)
                    certSearchTemplate.setSerial(serial.GetData());

                if (aCriteria.getSubjectKeyIdentifier() != null)
                    certSearchTemplate.setSubjectKeyID(aCriteria.getSubjectKeyIdentifier());

                if (aCriteria.getDigestAlg() != null)
                {
                    certSearchTemplate.setHashType(OzetTipi.fromDigestAlg(aCriteria.getDigestAlg()));
                    certSearchTemplate.setHash(aCriteria.getDigestValue());
                }
            }
            catch (CertStoreException aEx)
            {
                throw new ESYAException("Cannot create search template", aEx);
            }
            return certSearchTemplate;
        }
        //*/
    }
}
