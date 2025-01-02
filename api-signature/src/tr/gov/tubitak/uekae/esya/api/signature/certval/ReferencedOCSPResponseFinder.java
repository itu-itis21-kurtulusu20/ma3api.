package tr.gov.tubitak.uekae.esya.api.signature.certval;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.List;

/**
 * @author ayetgin
 */
public interface ReferencedOCSPResponseFinder
{

    List<EOCSPResponse> find(OCSPSearchCriteria aCriteria) throws ESYAException;

}
