using tr.gov.tubitak.uekae.esya.api.asn.x509;
using System;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.match.cross
{
    /**
     * Matches certificate and its cross certificate accoording to
     * SubjectKeyIdentifier extension information. 
     */
    public class SKIMatcher:CrossCertificateMatcher
    {
        private String dummy;

        /**
         * Sertifikanın çaprazıyla Anahtar Tanımlayıcı alanlarını eşleştirir
         */
        protected override bool _matchCrossCertificate(ECertificate aSertifika, ECertificate aCaprazSertifika)
        {
            ESubjectKeyIdentifier sSKI = aSertifika.getExtensions().getSubjectKeyIdentifier();
            ESubjectKeyIdentifier cSKI = aCaprazSertifika.getExtensions().getSubjectKeyIdentifier();

            if ((sSKI == null) && (cSKI == null))
            {
                return true;
            }
            else if ((sSKI == null) || (cSKI == null))
            {
                return false;
            }

            return sSKI.Equals(cSKI);
            //return UtilEsitlikler.esitMi(sSKI, cSKI);
        }
    }
}
