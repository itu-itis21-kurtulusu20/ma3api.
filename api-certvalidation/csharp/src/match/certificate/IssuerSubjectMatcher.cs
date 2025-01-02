using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.tools;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match.certificate
{
    /**
     * Matches the certificate and the CA certificate according to the subject
     * field of the CA certificate and the issuer field of the certificate
     */
    public class IssuerSubjectMatcher : CertificateMatcher
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /**
         * Bulunan sertifikayı verilen sertifikayla issuer-sbuject ilişkisiyle eşleştirir
         */
        protected override bool _matchCertificate(ECertificate aCertificate)
        {
            return aCertificate.getIssuer().Equals(mFoundCertificate.getSubject());
        }

        protected override bool _matchCertificate(ECRL aCRL)
        {
            bool match = aCRL.getIssuer().Equals(mFoundCertificate.getSubject());
            if (logger.IsDebugEnabled)
            {
                logger.Debug("match ? " + aCRL.getIssuer().stringValue());
                if (match)
                    logger.Debug("Found matching certificate!");
                else
                    logger.Trace(mFoundCertificate.getSubject().stringValue() + " does not match!");     
            }
            return match;

        }
    }
}
