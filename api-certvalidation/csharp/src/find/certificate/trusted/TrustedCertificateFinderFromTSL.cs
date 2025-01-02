using System;
using System.Collections.Generic;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.trusted
{
    public class TrustedCertificateFinderFromTSL : TrustedCertificateFinder
    {
        private List<ECertificate> _trustedCertificates;
        private DateTime _readTime = DateTime.UtcNow;
        private TrustedCertificateFinder _trustCertfinder;

        public static readonly String PROVIDER = "tr.gov.tubitak.uekae.esya.api.tsl.CertificateFinder.TrustedCertificateFinderFromTSL";
        public static readonly String ASSEMBLY_NAME = "ma3api-tsl";

        protected override List<ECertificate> _findTrustedCertificate()
        {
            DateTime now = DateTime.UtcNow;

            DateTime oneDayLaterAfterReading = _readTime.AddDays(1); //.clone();

            // if one day has passed since we read trusted certificates, read again
            if (now > oneDayLaterAfterReading)
            {
                _trustedCertificates = null;
            }


            if (_trustedCertificates == null)
            {
                _trustCertfinder = (TrustedCertificateFinder) Activator.CreateInstance(ASSEMBLY_NAME, PROVIDER).Unwrap();
                _trustedCertificates = _trustCertfinder.findTrustedCertificate();
                _readTime = DateTime.UtcNow;
            }
            return _trustedCertificates;
        }
    }
}
