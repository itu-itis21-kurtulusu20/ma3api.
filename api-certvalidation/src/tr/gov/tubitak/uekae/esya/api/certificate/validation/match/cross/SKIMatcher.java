package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.cross;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectKeyIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectKeyIdentifier;

/**
 * Matches certificate and its cross certificate accoording to
 * SubjectKeyIdentifier extension information. 
 */
public class SKIMatcher extends CrossCertificateMatcher {

    private String dummy;

    /**
     * Sertifikanın çaprazıyla Anahtar Tanımlayıcı alanlarını eşleştirir
     */
    protected boolean _matchCrossCertificate(ECertificate aSertifika, ECertificate aCaprazSertifika) {
        ESubjectKeyIdentifier sSKI = aSertifika.getExtensions().getSubjectKeyIdentifier();
        ESubjectKeyIdentifier cSKI = aCaprazSertifika.getExtensions().getSubjectKeyIdentifier();

        if ((sSKI == null) && (cSKI == null)) {
            return true;
        }
        else if ((sSKI == null) || (cSKI == null)) {
            return false;
        }

        return sSKI.equals(cSKI);
    }

}