package tr.gov.tubitak.uekae.esya.api.signature.certval;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.List;

/**
 * @author ayetgin
 */
public interface ReferencedCertificateFinder
{
    List<ECertificate> find(CertificateSearchCriteria aCriteria) throws ESYAException;

}
