using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.tools;
using tr.gov.tubitak.uekae.esya.api.infra.cache;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl
{
    /**
     * Finds CRL for a given certificate according to the CRL Distribution Points
     * (CDP) extension information in the certificate.
     */
    public abstract class CRLFinderFromCDP : CRLFinder
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private static FixedSizedCache<string, ECRL> CRL_CACHE = new FixedSizedCache<string, ECRL>(16, TimeSpan.FromMinutes(5));

        /**
         * Sertifikadaki CDP adresindeki sili bulur.
         */
        protected override List<ECRL> _findCRL(ECertificate aCertificate)
        {
            List<ECRL> crlList = new List<ECRL>();
            List<String> addresses = _getAddresses(aCertificate);
            if (addresses.Count == 0)
            {
                logger.Debug("Sertifika CDP uzantısından adres alınamadı");
                return crlList;
            }
            logger.Debug("Sertifika CDP uzantısından adresler alındı.");

            bool useCache = mParameters.getParameterBoolean(CACHE, true);
            foreach (String address in addresses)
            {
                try
                {
                    Chronometer c = new Chronometer("Find CRL");
                    c.start();

                    ECRL crl = null;
                    if (useCache)
                    {
                        crl = CRL_CACHE.Get(address, _getCRL);
                    }
                    else
                    {
                        crl = _getCRL(address);
                    }

                    logger.Info(c.stopSingleRun());

                    if (crl == null)
                    {
                        logger.Warn("CRL can not be read from " + address);
                    }
                    else
                    {
                        crlList.Add(crl);
                    }

                    if (mParentSystem != null)
                        mParentSystem.getSaveSystem().registerCRL(aCertificate, crl);
                }
                catch (Exception aEx)
                {
                    logger.Debug(aEx);
                }
            }
            return crlList;
        }

        protected abstract List<String> _getAddresses(ECertificate aCertificate);

        protected abstract ECRL _getCRL(String aAddress);

        protected abstract EName _getCRLIssuer(ECertificate aCertificate);
    }
}
