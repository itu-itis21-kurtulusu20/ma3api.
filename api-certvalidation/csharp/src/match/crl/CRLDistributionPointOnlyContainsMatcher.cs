using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match.crl
{
    /**
     * Matches a Certificate and a CRL according to the
     * CRLDistributionPointOnlyContains extension information.
     *
     * @author IH
     */
    public class CRLDistributionPointOnlyContainsMatcher : CRLMatcher
    {
        private static readonly ILog logger = LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

        /**
         * Sil Issuing Distribution Point eklentisindeki onlyContains özellikleri ile
         * Sertifika  BasicConstraint eklentisini eşleştirir
         */
        protected override bool _matchCRL(ECertificate aCertificate, ECRL aCRL)
        {
            EIssuingDistributionPoint idp = aCRL.getCRLExtensions().getIssuingDistributionPoint();
            if (idp == null)
            {
                logger.Debug("Silde Issuing Distribution Point uzantısı yok");
                return true;
            }

            EBasicConstraints bc = aCertificate.getExtensions().getBasicConstraints();
            if (bc != null && bc.isCA() && idp.isOnlyContainsUserCerts())
            {
                logger.Debug("Sertifika SM sertifikası, Sil onlyContainsUserCerts");
                return false;
            }
            if (!(bc != null && bc.isCA()) && idp.isOnlyContainsCACerts())
            {
                logger.Debug("Sertifika SM sertifikası değil, Sil onlyContainsCACerts");
                return false;
            }
            return true;
        }
    }
}
