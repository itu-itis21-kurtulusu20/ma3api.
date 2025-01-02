using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.exception
{
    public class CertValidationException : ESYAException
    {
        protected CertificateStatusInfo mCertificateStatusInfo;

        public CertValidationException(CertificateStatusInfo aCertificateStatusInfo)
            : base(aCertificateStatusInfo.getDetailedMessage())
        {
            mCertificateStatusInfo = aCertificateStatusInfo;
        }

        public CertificateStatusInfo getCertificateStatusInfo()
        {
            return mCertificateStatusInfo;
        }
    }
}
