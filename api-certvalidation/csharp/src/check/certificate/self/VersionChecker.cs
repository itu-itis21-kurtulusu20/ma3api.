using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

/**
 * Checks if the version information in the certificate is valid 
 */

//todo Annotation!
//@ApiClass

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self
{
    public class VersionChecker : CertificateSelfChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        
        public class EXP_Version
        {

            public static readonly long v1 = 0;
            public static readonly long v2 = 1;
            public static readonly long v3 = 2;
        }

        [Serializable]
        public class VersionCheckStatus : CheckStatus
        {

            public static readonly VersionCheckStatus EXTENSION_EXISTS_VERSION_VALID = new VersionCheckStatus(_enum.EXTENSION_EXISTS_VERSION_VALID);
            public static readonly VersionCheckStatus EXTENSION_EXISTS_VERSION_INVALID = new VersionCheckStatus(_enum.EXTENSION_EXISTS_VERSION_INVALID);
            public static readonly VersionCheckStatus UID_EXISTS_VERSION_VALID = new VersionCheckStatus(_enum.UID_EXISTS_VERSION_VALID);
            public static readonly VersionCheckStatus UID_EXISTS_VERSION_INVALID = new VersionCheckStatus(_enum.UID_EXISTS_VERSION_INVALID);
            public static readonly VersionCheckStatus BASIC_FIELDS_EXISTS_VERSION_VALID = new VersionCheckStatus(_enum.BASIC_FIELDS_EXISTS_VERSION_VALID);
            public static readonly VersionCheckStatus BASIC_FIELDS_EXISTS_VERSION_INVALID = new VersionCheckStatus(_enum.BASIC_FIELDS_EXISTS_VERSION_INVALID);

            enum _enum
            {
                EXTENSION_EXISTS_VERSION_VALID,
                EXTENSION_EXISTS_VERSION_INVALID,
                UID_EXISTS_VERSION_VALID,
                UID_EXISTS_VERSION_INVALID,
                BASIC_FIELDS_EXISTS_VERSION_VALID,
                BASIC_FIELDS_EXISTS_VERSION_INVALID
            }
            
            readonly _enum mValue;
            
            VersionCheckStatus(_enum aEnum)
            {
                mValue = aEnum;
            }
            
            public String getText()
            {
                switch (mValue)
                {
                    case _enum.EXTENSION_EXISTS_VERSION_VALID:
                        return Resource.message(Resource.EXTENSION_VAR_DOGRU);
                    case _enum.EXTENSION_EXISTS_VERSION_INVALID:
                        return Resource.message(Resource.EXTENSION_VAR_YANLIS);
                    case _enum.UID_EXISTS_VERSION_VALID:
                        return Resource.message(Resource.UID_VAR_DOGRU);
                    case _enum.UID_EXISTS_VERSION_INVALID:
                        return Resource.message(Resource.UID_VAR_YANLIS);
                    case _enum.BASIC_FIELDS_EXISTS_VERSION_VALID:
                        return Resource.message(Resource.BASIT_ALANLAR_VAR_DOGRU);
                    case _enum.BASIC_FIELDS_EXISTS_VERSION_INVALID:
                        return Resource.message(Resource.BASIT_ALANLAR_VAR_YANLIS);
                    default:
                        return Resource.message(Resource.KONTROL_SONUCU);
                }
            }

        }


        /**
         * Sertifikanın versiyon bilgisini kontrol eder.
         * 4.1.2.1 Version
         */
        protected override PathValidationResult _check(CertificateStatusInfo aCertStatusInfo)
        {
            ECertificate cert = aCertStatusInfo.getCertificate();
            if (logger.IsDebugEnabled) logger.Debug("Versiyon kontrolü yapılacak.");
            long version = cert.getVersion();
            //When extensions are used, as expected in this profile, version MUST be 3 (value is 2).
            EExtensions extensions = cert.getExtensions();
            if (extensions.getExtensionCount() > 0)
            {
                if (version == EXP_Version.v3)
                {
                    if (logger.IsDebugEnabled)
                        logger.Debug("Sertifikada extension var, version bilgisi doğru: " + version);
                    aCertStatusInfo.addDetail(this, VersionCheckStatus.EXTENSION_EXISTS_VERSION_VALID, true);
                    return PathValidationResult.SUCCESS;
                }
                logger.Error("Sertifikada extension var, version bilgisi yanlış: " + version);
                aCertStatusInfo.addDetail(this, VersionCheckStatus.EXTENSION_EXISTS_VERSION_INVALID, false);
                return PathValidationResult.VERSION_CONTROL_FAILURE;
            }
            // If no extensions are present, but a UniqueIdentifier is present, the version SHOULD be 2 (value is 1); however version MAY be 3.
            if ((cert.getObject().tbsCertificate.issuerUniqueID.mValue.Length > 0) || (cert.getObject().tbsCertificate.subjectUniqueID.mValue.Length > 0))
            {
                if ((version == EXP_Version.v2) || (version == EXP_Version.v3))
                {
                    if (logger.IsDebugEnabled)
                        logger.Debug("Sertifikada UniqueIdentifier var, version bilgisi doğru: " + version);
                    aCertStatusInfo.addDetail(this, VersionCheckStatus.UID_EXISTS_VERSION_VALID, true);
                    return PathValidationResult.SUCCESS;
                }
                logger.Error("Sertifikada UniqueIdentifier var, version bilgisi yanlış: " + version);
                aCertStatusInfo.addDetail(this, VersionCheckStatus.UID_EXISTS_VERSION_INVALID, false);
                return PathValidationResult.VERSION_CONTROL_FAILURE;
            }
            // If only basic fields are present, the version SHOULD be 1 (the value is omitted from the certificate as the default value); however the version MAY be 2 or 3.
            if ((version == EXP_Version.v1) || (version == EXP_Version.v2) || (version == EXP_Version.v3))
            {
                if (logger.IsDebugEnabled)
                    logger.Debug("Sertifikada sadece basit alanlar var, version bilgisi doğru: " + version);
                aCertStatusInfo.addDetail(this, VersionCheckStatus.BASIC_FIELDS_EXISTS_VERSION_VALID, true);
                return PathValidationResult.SUCCESS;
            }
            logger.Error("Sertifikada basit alanlar var, version bilgisi yanlış: " + version);
            aCertStatusInfo.addDetail(this, VersionCheckStatus.BASIC_FIELDS_EXISTS_VERSION_INVALID, false);
            return PathValidationResult.VERSION_CONTROL_FAILURE;
        }

        public override String getCheckText()
        {
            return Resource.message(Resource.VERSION);
        }

    }
}
