using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.save;
using tr.gov.tubitak.uekae.esya.api.infra.cache;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate
{
    /**
     * Finds issuer certificate according to Authority Info Access (AIA) extension
     * information. 
     */
    public abstract class CertificateFinderFromAIA : CertificateFinder
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        private static FixedSizedCache<string, ECertificate> CERT_CACHE = new FixedSizedCache<string, ECertificate>(32, TimeSpan.FromHours(2));
        
        /**
         * Finds issuer certificate of input certificate from AuthorityInfoAccess extension.
         */
        protected override List<ECertificate> _findCertificate(ECertificate eCertificate)
        {
            if (eCertificate == null)
            {
                return null;
            }
            logger.Debug("AIA icin gelen sertifika " + eCertificate.getSubject().stringValue());

            
            List<String> addresses = _getAddresses(eCertificate);
            if (addresses == null || addresses.Count == 0)
            {
                logger.Error("Sertifika AIA uzantisindan adres alinamadi");
                return null;
            }

            bool useCache = mParameters.getParameterBoolean(CACHE, true);
            List<ECertificate> certificates = new List<ECertificate>();
            foreach (string address in addresses)
            {
                ECertificate certificate = null;
                if (useCache)
                {
                    certificate = CERT_CACHE.Get(address, _getCertificate);
                }
                else
                {
                    certificate = _getCertificate(address);
                }

                if (certificate == null)
                {
                    logger.Warn("Certificate can not be read from " + address);
                }
                else
                {
                    certificates.Add(certificate);
                }
            }
            return certificates;
        }

        protected abstract List<String> _getAddresses(ECertificate aCertificate);

        //protected abstract byte[] _getCertificateData(String aAddress);
        protected abstract ECertificate _getCertificate(String aAddress);

    }
}
