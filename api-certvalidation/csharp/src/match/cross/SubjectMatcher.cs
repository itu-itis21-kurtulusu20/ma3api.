using tr.gov.tubitak.uekae.esya.api.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match.cross
{
    /**
     * Matches certificates and its cross certificates if their subject information
     * matches 
     *
     * @author IH
     */
    public class SubjectMatcher : CrossCertificateMatcher
    {
        /**
         * Sertifikanın çaprazıyla Subject alanlarını eşleştirir
         */
        protected override bool _matchCrossCertificate(ECertificate aSertifika, ECertificate aCaprazSertifika)
        {
            EName sertifikaSubject = aSertifika.getSubject();
            EName caprazSubject = aCaprazSertifika.getSubject();
            return sertifikaSubject.Equals(caprazSubject);
        }
    }
}
