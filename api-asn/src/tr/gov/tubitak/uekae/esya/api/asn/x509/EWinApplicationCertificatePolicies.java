package tr.gov.tubitak.uekae.esya.api.asn.x509;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.CertificatePolicies;

public class EWinApplicationCertificatePolicies extends ECertificatePolicies{

    public EWinApplicationCertificatePolicies(CertificatePolicies aObject) {
        super(aObject);
    }

    @Override
    public EExtension toExtension(boolean aCritic) throws ESYAException {
        return new EExtension(EExtensions.oid_win_application_cert_policies, aCritic, this);
    }

}
