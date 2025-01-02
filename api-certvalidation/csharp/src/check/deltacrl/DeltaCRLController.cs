using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.deltacrl
{
    /**
     * Performs delta-CRL validation. 
     */
    public class DeltaCRLController
    {
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        readonly List<ECertificate> mGuvenilirSertifikalar = new List<ECertificate>();

        public DeltaCRLController()
        {
        }

        public DeltaCRLController(List<ECertificate> aTrustedCertificates)
        {
            mGuvenilirSertifikalar = aTrustedCertificates;
        }

        /**
         * delta-SİL'in geçerliliğini kontrol eder
         */
        public CRLStatusInfo check(ValidationSystem aValidationSystem, ECRL aCRL)
        {
            //durum bilgisi oluşturalım
            CRLStatusInfo statusInfo = new CRLStatusInfo(aCRL);

            String issuer = aCRL.getIssuer().stringValue();
            if (logger.IsDebugEnabled)
                logger.Debug(issuer + " issuerlu sil kontrolü yapılacak");

            FindSystem bs = aValidationSystem.getFindSystem();
            CheckSystem ks = aValidationSystem.getCheckSystem();

            if (ks.checkDeltaCRL(aCRL, statusInfo) == PathValidationResult.SUCCESS)
            {
                if (logger.IsDebugEnabled)
                    logger.Debug(issuer + " : Delta sil kontrolleri tamam");
                //sili imzalayan sertifikayı bulalım
                long certificateOrder = ks.getConstraintCheckParam().getCertificateOrder();
                ks.getConstraintCheckParam().setCertificateOrder(-1);

                ECertificate issuingCertificate = bs.findCRLIssuerCertificate(aValidationSystem, statusInfo);

                ks.getConstraintCheckParam().setCertificateOrder(certificateOrder);

                if (issuingCertificate != null)
                {
                    //geçerliyse imzalayan sertifika ile ilgili geçerlilik kontrollerini yapalım
                    if (ks.checkCRLIssuer(aValidationSystem.getCheckSystem().getConstraintCheckParam(), aCRL, issuingCertificate, statusInfo) == PathValidationResult.SUCCESS)
                    {
                        logger.Debug(issuer + " : Delta sil durumu gecerli");
                        statusInfo.setCRLStatus(CRLStatus.VALID);
                    }
                }
                else
                {
                    logger.Error(issuer + " : Sil imzalayan sertifika bulunamadı");
                    statusInfo.setCRLStatus(CRLStatus.ISSUING_CERTIFICATE_INVALID);
                }
            }
            else
            {
                logger.Error(issuer + " : Tek sil kontrolleri sorunlu");
                statusInfo.setCRLStatus(CRLStatus.CRL_INVALID);
            }
            return statusInfo;
        }
    }
}
