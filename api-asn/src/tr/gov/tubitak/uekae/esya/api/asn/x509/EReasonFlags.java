package tr.gov.tubitak.uekae.esya.api.asn.x509;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.x509.ReasonFlags;

/**
 * User: zeldal.ozdemir
 * Date: 2/2/11
 * Time: 11:23 AM
 */
public class EReasonFlags extends BaseASNWrapper<ReasonFlags>{
    public EReasonFlags(ReasonFlags aObject) {
        super(aObject);
    }

    public EReasonFlags(boolean[] bitValues) {
        super(new ReasonFlags(bitValues));
    }

    public EReasonFlags(int numbits, byte[] data) {
        super(new ReasonFlags(numbits, data));
    }

    public EReasonFlags(byte[] aBytes) throws ESYAException {
        super(aBytes, new ReasonFlags());
    }

}
