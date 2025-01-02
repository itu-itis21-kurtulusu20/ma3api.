using System;
using System.Collections.Generic;
using System.Linq;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate
{
    /**
     * Find issuer certificate of the input certificate from TSL document.
     *
     * @author BY
     */
    public class CertificateFinderFromTSL : CertificateFinder
    {
        private List<ECertificate> _certificates;
        private DateTime _readTime = DateTime.UtcNow;
        private CertificateFinder _certfinder;

        public static readonly String PROVIDER = "tr.gov.tubitak.uekae.esya.api.tsl.CertificateFinder.CertificateFinderFromTSL";
        public static readonly String ASSEMBLY_NAME = "ma3api-tsl";
         protected override List<ECertificate> _findCertificate()
        {
            return _findCertificate(null);
        }

        protected override List<ECertificate> _findCertificate(ECertificate aSertifika)
        {
            EName name = null;

            if (aSertifika != null)
            {
                name = aSertifika.getIssuer();
            }
            return searchCertificates(name);
        }

        protected List<ECertificate> searchCertificates(EName issuer)
        {
            DateTime now = DateTime.UtcNow;

            DateTime oneDayLaterAfterReading = _readTime.AddDays(1) /*.Clone()*/;

            if (now > oneDayLaterAfterReading)
            {
                _certificates = null;
            }


            if (_certificates == null)
            {
                _certfinder = (CertificateFinder) Activator.CreateInstance(ASSEMBLY_NAME, PROVIDER).Unwrap();
                _certificates = _certfinder.findCertificate(null);
                _readTime = DateTime.UtcNow;
            }
            List<ECertificate> properCerts = new List<ECertificate>(_certificates);
            foreach (ECertificate cert in _certificates)
            {
                byte[] subject = issuer.getEncoded();
                if (subject != null)
                {
                    if (cert.getSubject().getEncoded().SequenceEqual(subject))
                        properCerts.Remove(cert);
                }
            }

            return properCerts;
        }
    }
}
