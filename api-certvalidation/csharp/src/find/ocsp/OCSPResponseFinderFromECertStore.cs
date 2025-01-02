using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.template;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp
{
    // todo bu class Liste dönmeli, depo gelişmeli.

    /**
     * Finds OCSP Response from the Local Certificate Store
     * @author IH
     */
    public class OCSPResponseFinderFromECertStore : OCSPResponseFinder
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        public OCSPResponseFinderFromECertStore() : base() { }

        protected override EOCSPResponse _findOCSPResponse(ECertificate aCertificate, ECertificate aIssuerCert)
        {
            OCSPSearchTemplate ocspSearchParams = new OCSPSearchTemplate();

            //ocspSearchParams.setIssuer(aCertificate.getCRLIssuer()); // Butun CRL leri almamak icin bir optimizasyon

            bool historicValidation = (mParentSystem != null) && (mParentSystem.getBaseValidationTime() != null);

            if (historicValidation)
            {
                if (mParentSystem == null) return null; // Parent yoksa calisamam..

                DateTime? baseTime = mParentSystem.getBaseValidationTime();
                DateTime? lastRevocTime = (mParentSystem.getLastRevocationTime() == null) ? aCertificate.getNotAfter() : mParentSystem.getLastRevocationTime();
                ocspSearchParams.setProducedAtAfter(baseTime);
                ocspSearchParams.setProducedAtBefore(lastRevocTime);
            }
            else
            {
                ocspSearchParams.setProducedAtAfter(DateTime.UtcNow);
            }

            EBasicOCSPResponse ocsp = null;
            CertStore certStore = null;
            try
            {
                certStore = StoreFinder.createCertStore(getParameters(), mParentSystem.getDefaultStorePath());
                CertStoreOCSPOps certStoreOcspOps = new CertStoreOCSPOps(certStore);
                ocsp = certStoreOcspOps.listOCSPResponses(ocspSearchParams);
            }
            catch (CertStoreException aEx)
            {
                logger.Error("Depodan ocsp cevabi alinirken hata olustu", aEx);
            }
            finally
            {
                try
                {
                    certStore.closeConnection();
                }
                catch (CertStoreException e)
                {
                    logger.Error("Connection couldn't closed", e);
                }
            }
            
            if (ocsp == null)
                return null;

            return EOCSPResponse.getEOCSPResponse(ocsp);

        }
    }
}
