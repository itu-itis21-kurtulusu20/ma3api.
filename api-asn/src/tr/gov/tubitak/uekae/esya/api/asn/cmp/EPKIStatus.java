package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.asn.cmp.PKIStatus;

/**
 * User: zeldal.ozdemir
 * Date: 2/2/11
 * Time: 9:16 AM
 */
public class EPKIStatus extends BaseASNWrapper<PKIStatus> {
    public final static EPKIStatus ACCEPTED = new EPKIStatus(PKIStatus.accepted);
    public final static EPKIStatus GRANTEDWITHMODS = new EPKIStatus(PKIStatus.grantedWithMods);
    public final static EPKIStatus REJECTION = new EPKIStatus(PKIStatus.rejection);
    public final static EPKIStatus WAITING = new EPKIStatus(PKIStatus.waiting);

    public EPKIStatus(PKIStatus aObject) {
        super(aObject);
    }

    public EPKIStatus(long status) {
        super(new PKIStatus(status));
    }
}
