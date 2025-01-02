using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match.crl
{
    /**
     * Matches a CRL and a certificate according to the Authority Key Identifier
     * (AKI) extensions of both the crl and the certificate. 
     */
    public class CRLKeyIDMatcher : CRLMatcher
    {
        private static ILog logger = LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

        /**
        * Sertifika-SIL AuthorityKeyIdentifier uzanti eslestirmesini yapar
        */
        protected override bool _matchCRL(ECertificate aCertificate, ECRL aCRL)
        {

            if (aCertificate.hasIndirectCRL())
                return true; // Indirect CRL var. bu sekilde match edemeyiz.

            // If issuer and crl distributor is different, Do not control.
            if (aCertificate.getCRLIssuer().Equals(aCertificate.getIssuer()) == false)
                return true;

            EAuthorityKeyIdentifier sertifikaAki = aCertificate.getExtensions().getAuthorityKeyIdentifier();
            if (sertifikaAki == null)
            {
                logger.Debug("Sertifikadan AKI alınamadı");
                return false;
            }

            EAuthorityKeyIdentifier silAki = aCRL.getCRLExtensions().getAuthorityKeyIdentifier();
            if (silAki == null)
            {
                logger.Debug("Silden AKI alınamadı");
                return false;
            }
            return sertifikaAki.Equals(silAki);
            // return UtilEsitlikler.esitMi(sertifikaAki, silAki);
        }
    }
}
