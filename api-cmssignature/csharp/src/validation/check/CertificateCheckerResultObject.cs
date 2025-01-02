using System;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    [Serializable]
    public class CertificateCheckerResultObject
    {
        readonly CertificateStatusInfo certStatusInfo;
        readonly DateTime? signingTime;

        public CertificateCheckerResultObject(CertificateStatusInfo aCertStatusInfo, DateTime? aSigningTime)
        {
            certStatusInfo = aCertStatusInfo;
            signingTime = aSigningTime;
        }

        public DateTime? getSigningTime()
        {
            return signingTime;
        }

        public CertificateStatusInfo getCertStatusInfo()
        {
            return certStatusInfo;
        }
    }
}
