using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl
{
    /**
     * Performs CRL validation. 
     */
    public class CRLController
    {
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        protected List<ECertificate> mTrustedCertificates = new List<ECertificate>();

        public CRLController()
        {
        }

        public CRLController(List<ECertificate> aTrustedCertificates)
        {
            mTrustedCertificates = aTrustedCertificates;
        }

        /**
         * SIL dogrulamasi yapar
         */
        public CRLStatusInfo check(ValidationSystem aValidationSystem, ECRL aCRL)
        {
            if (logger.IsDebugEnabled)
            {
                logger.Debug("----------------- Check CRL ---\n");
                logger.Debug(aCRL.ToString());
                logger.Debug("------------------------------ ");
            }


            CRLStatusInfo crlStatusInfo = aValidationSystem.getCRLValidationCache().getCheckResult(aCRL);

            // Daha önceden kontrol edilmiş ise hafızamdaki sonucu dönüyorum
            if (crlStatusInfo != null)
                return crlStatusInfo/*.Clone()*/;

            //durum bilgisi oluşturalım
            crlStatusInfo = new CRLStatusInfo(aCRL);

            ////////
            //crlStatusInfo.setCRLInfo(SilDurumBilgisi::VALID);
            //return crlStatusInfo;
            /////////

            FindSystem bs = aValidationSystem.getFindSystem();
            CheckSystem ks = aValidationSystem.getCheckSystem();

            // guvenilir sertifika listesini oluşturalım.
            bs.findTrustedCertificates();


            if (ks.checkCRLSelf(aCRL, crlStatusInfo) == PathValidationResult.SUCCESS)
            {
                // sili imzalayan sertifikayı bulalım
                IssuerCheckParameters icp = ks.getConstraintCheckParam();
                long kacinciSertifika = icp.getCertificateOrder();
                icp.setCertificateOrder(-1);

                ECertificate issuerCertificate = bs.findCRLIssuerCertificate(aValidationSystem, crlStatusInfo);

                icp.setCertificateOrder(kacinciSertifika);

                if (issuerCertificate != null)
                {
                    if (ks.checkCRLIssuer(aValidationSystem.getCheckSystem().getConstraintCheckParam(), aCRL, issuerCertificate, crlStatusInfo) == PathValidationResult.SUCCESS)
                    {
                        crlStatusInfo.setCRLStatus(CRLStatus.VALID);
                        issuerCertificate = null;
                        aValidationSystem.getCRLValidationCache().addCheckResult(crlStatusInfo/*.clone()*/);// TODO Burada clone gerekli mi?.

                        return crlStatusInfo;
                    }
                    else
                    {
                        crlStatusInfo.setCRLStatus(CRLStatus.CRL_INVALID);
                    }
                }
                else
                {
                    logger.Error("CRL issuing certificate invalid");
                    crlStatusInfo.setCRLStatus(CRLStatus.ISSUING_CERTIFICATE_INVALID);
                }
                issuerCertificate = null;
            }
            else
            {
                crlStatusInfo.setCRLStatus(CRLStatus.CRL_INVALID);
            }

            aValidationSystem.getCRLValidationCache().addCheckResult(crlStatusInfo/*.clone()*/); // TODO Burada clone gerekli mi
            return crlStatusInfo;
        }
    }
}
