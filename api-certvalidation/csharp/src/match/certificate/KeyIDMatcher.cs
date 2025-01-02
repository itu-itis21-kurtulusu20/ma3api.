using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.tools;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match.certificate
{
    /**
     * Matches the certificate and the CA certificate according to the
     * SubjectKeyIdentifier extension of the CA certificate and
     * AuthorityKeyIdentifier extension of the certificate 
     */
    public class KeyIDMatcher : CertificateMatcher
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        /**
         * Sertifika ve verilen sertifika eşleşiyor mu Yetkili Anahtar Tanımlayıcı
         * eklentilerine bakarak kontrol eder
         */
        protected override bool _matchCertificate(ECertificate aCertificate)
        {
            EAuthorityKeyIdentifier aki = aCertificate.getExtensions().getAuthorityKeyIdentifier();
            if (aki == null)
                return false;

            return _esitMi(aki);
        }

        /**
         * Sertifika ile verilen SİL'in imzalayan sertifikaları eşleşiyor mu
         * Yetkili Anahtar Tanımlayıcı eklentilerine bakarak kontrol eder
         */
        protected override bool _matchCertificate(ECRL aCRL)
        {
            EAuthorityKeyIdentifier aki = aCRL.getCRLExtensions().getAuthorityKeyIdentifier();
            if (aki == null)
            {
                logger.Debug("Authority key Id not found!");
                return false;
            }
            return _esitMi(aki);
        }

        private bool _esitMi(EAuthorityKeyIdentifier aAKI)
        {
            ESubjectKeyIdentifier smSki = mFoundCertificate.getExtensions().getSubjectKeyIdentifier();
            if (smSki == null)
            {
                logger.Debug("Found cert Authority key Id not found!");
                return false;
            }
            bool akiMatchesSKi = UtilEsitlikler.esitMi(aAKI.getKeyIdentifier(), smSki.getValue());
            if (!akiMatchesSKi)
            {
                logger.Trace("Autority Key Id does not match Subject Key Id!");
            }
            return akiMatchesSKi;

        }

    }
}
