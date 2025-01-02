package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.DigestInfo;

/**
 * User: zeldal.ozdemir
 * Date: 1/21/11
 * Time: 10:04 AM
 */
public class EDigestInfo extends BaseASNWrapper<DigestInfo> {
    public EDigestInfo(DigestInfo aDigestInfo) {
        super(aDigestInfo);
    }

    public EDigestInfo(EAlgorithmIdentifier digestAlgorithm, byte[] digest) {
        super(new DigestInfo(digestAlgorithm.getObject(), digest));
    }

    public EDigestInfo(byte[] aBytes) throws ESYAException {
        super(aBytes, new DigestInfo());
    }
}
