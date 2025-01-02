using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer
{
    /**
     * Checks if the SubjectKeyIdentifier extension of the CA certificate and
     * AuthorityKeyIdentifier extension of the certificate matches 
     */
    public class KeyIdentifierChecker : IssuerChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        [Serializable]
        public class KeyIdentifierCheckStatus : CheckStatus
        {
            public static readonly KeyIdentifierCheckStatus NO_CERTIFICATE_AUTHORITY_KEY_IDENTIFIER = new KeyIdentifierCheckStatus(_enum.NO_CERTIFICATE_AUTHORITY_KEY_IDENTIFIER);
            public static readonly KeyIdentifierCheckStatus NO_ISSUER_CERTIFICATE_SUBJECT_KEY_IDENTIFIER = new KeyIdentifierCheckStatus(_enum.NO_ISSUER_CERTIFICATE_SUBJECT_KEY_IDENTIFIER);
            public static readonly KeyIdentifierCheckStatus AUTHORITY_SUBJECT_KEY_IDENTIFIER_MISMATCH = new KeyIdentifierCheckStatus(_enum.AUTHORITY_SUBJECT_KEY_IDENTIFIER_MISMATCH);
            public static readonly KeyIdentifierCheckStatus AUTHORITY_SUBJECT_KEY_IDENTIFIER_MATCH_OK = new KeyIdentifierCheckStatus(_enum.AUTHORITY_SUBJECT_KEY_IDENTIFIER_MATCH_OK);
            enum _enum
            {
                NO_CERTIFICATE_AUTHORITY_KEY_IDENTIFIER,
                NO_ISSUER_CERTIFICATE_SUBJECT_KEY_IDENTIFIER,
                AUTHORITY_SUBJECT_KEY_IDENTIFIER_MISMATCH,
                AUTHORITY_SUBJECT_KEY_IDENTIFIER_MATCH_OK
            }
            readonly _enum mValue;
            KeyIdentifierCheckStatus(_enum aEnum)
            {
                mValue = aEnum;
            }
            public String getText()
            {
                switch (mValue)
                {
                    case _enum.NO_CERTIFICATE_AUTHORITY_KEY_IDENTIFIER:
                        return Resource.message(Resource.SERTIFIKA_AUTHORITY_KEY_IDENTIFIER_YOK);
                    case _enum.NO_ISSUER_CERTIFICATE_SUBJECT_KEY_IDENTIFIER:
                        return Resource.message(Resource.SMSERTIFIKA_SUBJECT_KEY_IDENTIFIER_YOK);
                    case _enum.AUTHORITY_SUBJECT_KEY_IDENTIFIER_MISMATCH:
                        return Resource.message(Resource.AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMSUZ);
                    case _enum.AUTHORITY_SUBJECT_KEY_IDENTIFIER_MATCH_OK:
                        return Resource.message(Resource.AUTHORITY_SUBJECT_KEY_IDENTIFIER_UYUMLU);

                    default:
                        return Resource.message(Resource.KONTROL_SONUCU);
                }
            }
        }


        /**
         * Sertifika Yetkili Anahtar Tanımlayıcısı ile SM sertifikası Anahtar tanımlayıcısı uyuşuyormu kontrol eder.
         */
        protected override PathValidationResult _check(IssuerCheckParameters aIssuerCheckParameters,
                                              ECertificate aIssuerCertificate, ECertificate aCertificate,
                                              CertificateStatusInfo aCertStatusInfo)
        {
            //sertifika AuthorityKeyIdentifier alalım
            EAuthorityKeyIdentifier aki = aCertificate.getExtensions().getAuthorityKeyIdentifier();
            if (aki == null)
            {
                logger.Error("Sertifikada AKI yok");
                aCertStatusInfo.addDetail(this, KeyIdentifierCheckStatus.NO_CERTIFICATE_AUTHORITY_KEY_IDENTIFIER, false);
                return PathValidationResult.KEYID_CONTROL_FAILURE;
            }

            //sm SubjectKeyIdentifier alalım
            ESubjectKeyIdentifier ski = aIssuerCertificate.getExtensions().getSubjectKeyIdentifier();
            if (ski == null)
            {
                logger.Error("Sertifikada SKI yok");
                aCertStatusInfo.addDetail(this, KeyIdentifierCheckStatus.NO_ISSUER_CERTIFICATE_SUBJECT_KEY_IDENTIFIER, false);
                return PathValidationResult.KEYID_CONTROL_FAILURE;
            }

            if (!UtilEsitlikler.esitMi(aki.getKeyIdentifier(), ski.getValue()))
            {
                logger.Error("Authority Key Identifier ve Subject Key Identifier değerleri aynı değil");
                aCertStatusInfo.addDetail(this, KeyIdentifierCheckStatus.AUTHORITY_SUBJECT_KEY_IDENTIFIER_MISMATCH, false);
                return PathValidationResult.KEYID_CONTROL_FAILURE;
            }
            else
            {
                if (logger.IsDebugEnabled)
                    logger.Debug("Authority Key Identifier ve Subject Key Identifier değerleri aynı");
                aCertStatusInfo.addDetail(this, KeyIdentifierCheckStatus.AUTHORITY_SUBJECT_KEY_IDENTIFIER_MATCH_OK, true);
                return PathValidationResult.SUCCESS;
            }
        }

        public override String getCheckText()
        {
            return Resource.message(Resource.KEY_IDENTIFIER_KONTROLU);
        }
    }
}
