using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer
{
    /**
     * Checks whether the 'issuer certificate' of the certificate has 
     * CertificateSigning key usage.
     */
    public class CertificateKeyUsageChecker : IssuerChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        [Serializable]
        public class KeyUsageCheckStatus : CheckStatus
        {
            public static readonly KeyUsageCheckStatus NO_KEY_USAGE = new KeyUsageCheckStatus(_enum.NO_KEY_USAGE);
            public static readonly KeyUsageCheckStatus INVALID_KEY_USAGE = new KeyUsageCheckStatus(_enum.INVALID_KEY_USAGE);
            public static readonly KeyUsageCheckStatus KEY_USAGE_ISNOT_CERT_SIGN = new KeyUsageCheckStatus(_enum.KEY_USAGE_ISNOT_CERT_SIGN);
            public static readonly KeyUsageCheckStatus KEY_USAGE_CERT_SIGN_OK = new KeyUsageCheckStatus(_enum.KEY_USAGE_CERT_SIGN_OK);
            public static readonly CheckStatus KEY_USAGE_CHECK_ENCRYPTION_OK = new KeyUsageCheckStatus(_enum.KEY_USAGE_CHECK_ENCRYPTION_OK);


            enum _enum
            {
                NO_KEY_USAGE,
                INVALID_KEY_USAGE,
                KEY_USAGE_ISNOT_CERT_SIGN,
                KEY_USAGE_CERT_SIGN_OK,
                KEY_USAGE_CHECK_ENCRYPTION_OK
            }
            readonly _enum mValue;
           

            KeyUsageCheckStatus(_enum aEnum)
            {
                mValue = aEnum;
            }
            public String getText()
            {
                switch (mValue)
                {
                    case _enum.NO_KEY_USAGE:
                        return Resource.message(Resource.KEY_USAGE_YOK);
                    case _enum.INVALID_KEY_USAGE:
                        return Resource.message(Resource.KEY_USAGE_BOZUK);
                    case _enum.KEY_USAGE_ISNOT_CERT_SIGN:
                        return Resource.message(Resource.KEY_USAGE_SERTIFIKA_IMZALAYICI_DEGIL);
                    case _enum.KEY_USAGE_CERT_SIGN_OK:
                        return Resource.message(Resource.KEY_USAGE_SERTIFIKA_IMZALAYICI);
                    case _enum.KEY_USAGE_CHECK_ENCRYPTION_OK:
                        return Resource.message(Resource.KEY_USAGE_SERTIFIKA_SIFRELEME);
                    default:
                        return Resource.message(Resource.KONTROL_SONUCU);
                }
            }
        }

        /**
         * Ust SM sertifikası sertifika imzalayıcı özelliğe sahip olmalı
         */
        protected override PathValidationResult _check(IssuerCheckParameters aConstraintCheckParam,
                                              ECertificate aIssuercertificate, ECertificate aCertificate,
                                              CertificateStatusInfo aCertStatusInfo)
        {
            //sertifika KeyUsage alalım
            EKeyUsage ku = aIssuercertificate.getExtensions().getKeyUsage();

            //int	res = ExtensionGenerator::getKeyUsageExtension( aUstSertifika.getTBSCertificate().getExtensions(),ku);
            if (ku == null)
            {
                logger.Error("Sertifikada Key Usage yok");
                aCertStatusInfo.addDetail(this, KeyUsageCheckStatus.NO_KEY_USAGE, false);
                return PathValidationResult.KEYUSAGE_CONTROL_FAILURE;
            }

            if (ku.isKeyCertSign())
            {
                aCertStatusInfo.addDetail(this, KeyUsageCheckStatus.KEY_USAGE_CERT_SIGN_OK, true);
                return PathValidationResult.SUCCESS;
            }
            else
            {
                logger.Error("Sertifikada Key Usage cert sign değil");
                aCertStatusInfo.addDetail(this, KeyUsageCheckStatus.KEY_USAGE_ISNOT_CERT_SIGN, false);
                return PathValidationResult.KEYUSAGE_CONTROL_FAILURE;
            }
        }


        public override String getCheckText()
        {
            return Resource.message(Resource.SERTIFIKA_KEY_USAGE_KONTROLU);
        }

    }
}
