using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.issuer
{
    /**
     * Checks whether crl issuer certificate has CRLSigning key usage. 
     */
    public class CRLKeyUsageChecker : CRLIssuerChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        [Serializable]
        public class KeyUsageCheckStatus : CheckStatus
        {
            public static readonly KeyUsageCheckStatus KEY_USAGE_NOT_EXISTS = new KeyUsageCheckStatus(_enum.KEY_USAGE_NOT_EXISTS);
            public static readonly KeyUsageCheckStatus KEY_USAGE_INVALID = new KeyUsageCheckStatus(_enum.KEY_USAGE_INVALID);
            public static readonly KeyUsageCheckStatus KEY_USAGE_NOT_CRL_SIGN = new KeyUsageCheckStatus(_enum.KEY_USAGE_NOT_CRL_SIGN);
            public static readonly KeyUsageCheckStatus KEY_USAGE_CRL_SIGN = new KeyUsageCheckStatus(_enum.KEY_USAGE_CRL_SIGN);

            enum _enum
            {
                KEY_USAGE_NOT_EXISTS,
                KEY_USAGE_INVALID,
                KEY_USAGE_NOT_CRL_SIGN,
                KEY_USAGE_CRL_SIGN
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
                    case _enum.KEY_USAGE_NOT_EXISTS:
                        return Resource.message(Resource.KEY_USAGE_YOK);
                    case _enum.KEY_USAGE_INVALID:
                        return Resource.message(Resource.KEY_USAGE_BOZUK);
                    case _enum.KEY_USAGE_NOT_CRL_SIGN:
                        return Resource.message(Resource.KEY_USAGE_SIL_IMZALAYICI_DEGIL);
                    case _enum.KEY_USAGE_CRL_SIGN:
                        return Resource.message(Resource.KEY_USAGE_SIL_IMZALAYICI);

                    default:
                        return Resource.message(Resource.KONTROL_SONUCU);
                }
            }
        }

        /**
         * SİL imzalayan Sertifikasının S�L imzalama özelliği olup olmadığını kontrol eder.
         */
       protected override PathValidationResult _check(IssuerCheckParameters aConstraint,
                                              ECRL aSil, ECertificate aIssuerCertificate,
                                              CRLStatusInfo aCRLStatusInfo)
        {
            EKeyUsage ku = aIssuerCertificate.getExtensions().getKeyUsage();

            try
            {
                if (ku == null)
                {
                    logger.Error("Sertifikada Key Usage yok");
                    aCRLStatusInfo.addDetail(this, KeyUsageCheckStatus.KEY_USAGE_NOT_EXISTS, false);
                    return PathValidationResult.CRL_KEYUSAGE_CONTROL_FAILURE;
                }
            }
            catch (Exception x)
            {
                logger.Error("Sertifika Key Usage decode edilirken hata olustu", x);
                aCRLStatusInfo.addDetail(this, KeyUsageCheckStatus.KEY_USAGE_INVALID, false);
                return PathValidationResult.CRL_KEYUSAGE_CONTROL_FAILURE;
            }
            if (ku.isCRLSign())
            {
                aCRLStatusInfo.addDetail(this, KeyUsageCheckStatus.KEY_USAGE_CRL_SIGN, true);
                return PathValidationResult.SUCCESS;
            }
            else
            {
                logger.Error("CRL issuer'da Key usage crlSign set edilmemiş.");
                aCRLStatusInfo.addDetail(this, KeyUsageCheckStatus.KEY_USAGE_NOT_CRL_SIGN, false);
                return PathValidationResult.CRL_KEYUSAGE_CONTROL_FAILURE;
            }
        }

        public override String getCheckText()
        {
            return Resource.message(Resource.SIL_KEY_USAGE_KONTROLU);
        }
    }
}
