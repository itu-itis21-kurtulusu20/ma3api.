package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.cross;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EName;

/**
 * Matches certificates and its corss certificates if their subject information
 * matches 
 *
 * @author IH
 */
public class SubjectMatcher extends CrossCertificateMatcher {

    /**
     * Sertifikanın çaprazıyla Subject alanlarını eşleştirir
     */
    protected boolean _matchCrossCertificate(ECertificate aSertifika, ECertificate aCaprazSertifika) {
        EName sertifikaSubject = aSertifika.getSubject();
        EName caprazSubject = aCaprazSertifika.getSubject();
        return sertifikaSubject.equals(caprazSubject);
    }

}
