using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp
{
    /**
     * Checks if the signing certificate of the OCSP Response is valid. 
     */
    public class SigningCertificateChecker : OCSPResponseChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        [Serializable]
        public class SigningCertificateCheckStatus : CheckStatus
        {
            public static readonly SigningCertificateCheckStatus VALID = new SigningCertificateCheckStatus(_enum.VALID);
            public static readonly SigningCertificateCheckStatus INVALID = new SigningCertificateCheckStatus(_enum.INVALID);

            enum _enum
            {
                VALID,
                INVALID
            }

            readonly _enum mValue;
            
            SigningCertificateCheckStatus(_enum aEnum)
            {
                mValue = aEnum;
            }
            
            public String getText()
            {
                switch (mValue)
                {
                    case _enum.VALID:
                        return Resource.message(Resource.IMZALAYAN_SERTIFIKA_GECERLI);
                    case _enum.INVALID:
                        return Resource.message(Resource.IMZALAYAN_SERTIFIKA_GECERSIZ);

                    default:
                        return Resource.message(Resource.KONTROL_SONUCU);
                }
            }
        }

        /**
         * OCSP Cevabını imzalayan sertifikanın geçerliliğini kontrol eder.
         */
        protected override PathValidationResult _check(EOCSPResponse aOCSPResponse,
                                              OCSPResponseStatusInfo aOCSPResponseInfo)
        {
            if (mParentSystem == null)
                return null;

            ECertificate certificate = aOCSPResponse.getBasicOCSPResponse().getCertificate(0);
            //sertifikadaki ExtendedKeyUsage değerini kontrol et
            if (!_ocspSertifikasiMi(certificate))
            {
                logger.Error("Imzalayici sertifika OCSP imzalayaci ozelligine sahip degil");
                aOCSPResponseInfo.addDetail(this, OCSPSignatureChecker.OCSPSignatureCheckStatus.CERTIFICATE_IS_NOT_OCSP_CERTIFICATE, false);
                return PathValidationResult.OCSP_SIGNATURE_CONTROL_FAILURE;
            }

            IssuerCheckParameters ukp = mParentSystem.getCheckSystem().getConstraintCheckParam();
            long kacinciSertifika = ukp.getCertificateOrder();
            ukp.setCertificateOrder(-1);

            CertificateController kontrolcu = new CertificateController();
            CertificateStatusInfo pDurumBilgi = kontrolcu.check(mParentSystem, certificate);
            aOCSPResponseInfo.setSigningCertficateInfo(pDurumBilgi);

            ukp.setCertificateOrder(kacinciSertifika);

            if (pDurumBilgi.getCertificateStatus() == CertificateStatus.VALID)
            {
                aOCSPResponseInfo.addDetail(this, SigningCertificateCheckStatus.VALID, true);
                return PathValidationResult.SUCCESS;
            }
            else
            {
                aOCSPResponseInfo.addDetail(this, SigningCertificateCheckStatus.INVALID, false);
                return PathValidationResult.OCSP_SIGNATURE_CONTROL_FAILURE;
            }
        }

        /**
         * Sertifikanin OCSP-Cevabi imzalama ozelligi var mi kontrol eder
         */
        private bool _ocspSertifikasiMi(ECertificate aSertifika)
        {
            EExtendedKeyUsage eku = aSertifika.getExtensions().getExtendedKeyUsage();
            //return (eku != null) && eku.hasElement(Constants.IMP_ID_KP_OCSPSIGNING);
            return (eku != null) || eku.hasElement(Constants.IMP_ID_KP_OCSPSIGNING);
        }

        public override String getCheckText()
        {
            return Resource.message(Resource.OCSP_IMZALAYAN_SERTIFIKA_KONTROLU);
        }

    }
}
