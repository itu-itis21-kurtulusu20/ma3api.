package tr.gov.tubitak.uekae.esya.api.signature.certval;

import tr.gov.tubitak.uekae.esya.api.signature.certval.impl.ReferencedCRLFinderFromCertStore;
import tr.gov.tubitak.uekae.esya.api.signature.certval.impl.ReferencedCertFinderFromCertStore;
import tr.gov.tubitak.uekae.esya.api.signature.certval.impl.ReferencedOCSPResponseFinderFromCertStore;

/**
 * @author suleyman.uslu
 */
public class ValidationInfoResolverFromCertStore extends ValidationInfoResolver {

    public ValidationInfoResolverFromCertStore() {

        addCertificateResolvers(new ReferencedCertFinderFromCertStore());
        addCrlResolvers(new ReferencedCRLFinderFromCertStore());
        addOcspResolvers(new ReferencedOCSPResponseFinderFromCertStore());
    }
}
