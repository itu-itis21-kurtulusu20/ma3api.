using System;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self
{
    /**
     * @author ayetgin
     */
    public class ExtendedKeyUsageOIDChecker : CertificateSelfChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private static readonly String PARAM_EKUOID = "oid";

        public class ExtendedKeyUsageCheckStatus : CheckStatus
        {
            public static readonly ExtendedKeyUsageCheckStatus NO_EXTENDED_KEY_USAGE = new ExtendedKeyUsageCheckStatus();
            public static readonly ExtendedKeyUsageCheckStatus INVALID_EXTENDED_KEY_USAGE = new ExtendedKeyUsageCheckStatus();
            public static readonly ExtendedKeyUsageCheckStatus VALID = new ExtendedKeyUsageCheckStatus();

            private ExtendedKeyUsageCheckStatus()
            {

            }

            public String getText()
            {

                if (this.Equals(NO_EXTENDED_KEY_USAGE))
                    return Resource.EXTENDEDKEY_USAGE_YOK;
                else if (this.Equals(INVALID_EXTENDED_KEY_USAGE))
                    return Resource.EXTENDEDKEY_USAGE_BOZUK;
                else if (this.Equals(VALID))
                    return Resource.EXTENDEDKEY_USAGE_GECERLI;
                else
                    return null;
            }
        }
        
        protected override PathValidationResult _check(CertificateStatusInfo aCertStatusInfo)
        {
            logger.Debug("Extended Key Usage kontrolü yapılacak.");

            ECertificate certificate = aCertStatusInfo.getCertificate();
            if (certificate.isCACertificate())
            {
                logger.Debug("CA certificate.");
                aCertStatusInfo.addDetail(this, ExtendedKeyUsageCheckStatus.VALID, true);
                return PathValidationResult.SUCCESS;
            }
            if (certificate.isOCSPSigningCertificate())
            {
                logger.Debug("OCSP Signing certificate.");
                aCertStatusInfo.addDetail(this, ExtendedKeyUsageCheckStatus.VALID, true);
                return PathValidationResult.SUCCESS;
            }
            if (certificate.isTimeStampingCertificate())
            {
                logger.Debug("TimeStamping certificate.");
                aCertStatusInfo.addDetail(this, ExtendedKeyUsageCheckStatus.VALID, true);
                return PathValidationResult.SUCCESS;
            }

            String oidParam = mCheckParams.getParameterAsString(PARAM_EKUOID);
            Asn1ObjectIdentifier oid = new Asn1ObjectIdentifier(OIDUtil.parse(oidParam));

            EExtendedKeyUsage eku = certificate.getExtensions().getExtendedKeyUsage();

            if (eku == null)
            {
                aCertStatusInfo.addDetail(this, ExtendedKeyUsageCheckStatus.NO_EXTENDED_KEY_USAGE, false);
                return PathValidationResult.EXTENDED_KEYUSAGE_CONTROL_FAILURE;
            }

            if (!eku.hasElement(oid))
            {
                aCertStatusInfo.addDetail(this, ExtensionCheckStatus.INVALID_EXTENSION, false);
                return PathValidationResult.EXTENDED_KEYUSAGE_CONTROL_FAILURE;
            }
            aCertStatusInfo.addDetail(this, ExtendedKeyUsageCheckStatus.VALID, true);
            return PathValidationResult.SUCCESS;
        }

        public override String getCheckText()
        {
            return Resource.SERTIFIKA_EXTENDED_KEY_USAGE_KONTROLU;
        }
    }
}
