package tr.gov.tubitak.uekae.esya.api.signature.attribute;

import tr.gov.tubitak.uekae.esya.api.signature.certval.CRLSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertificateSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.certval.OCSPSearchCriteria;

import java.util.List;

/**
 * @author ayetgin
 */
public class CertValidationReferences {

    List<CertificateSearchCriteria> certificateReferences;
    List<CRLSearchCriteria> crlReferences;
    List<OCSPSearchCriteria> ocspReferences;

    public CertValidationReferences(List<CertificateSearchCriteria> certificateReferences, List<CRLSearchCriteria> crlReferences, List<OCSPSearchCriteria> ocspReferences) {
        this.certificateReferences = certificateReferences;
        this.crlReferences = crlReferences;
        this.ocspReferences = ocspReferences;
    }

    public List<CertificateSearchCriteria> getCertificateReferences() {
        return certificateReferences;
    }

    public List<CRLSearchCriteria> getCrlReferences() {
        return crlReferences;
    }

    public List<OCSPSearchCriteria> getOcspReferences() {
        return ocspReferences;
    }
}
