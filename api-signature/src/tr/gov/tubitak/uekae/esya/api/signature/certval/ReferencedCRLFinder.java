package tr.gov.tubitak.uekae.esya.api.signature.certval;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.List;

/**
 * @author ayetgin
 */
public interface ReferencedCRLFinder
{
    List<ECRL> find(CRLSearchCriteria aCriteria) throws ESYAException;

}
