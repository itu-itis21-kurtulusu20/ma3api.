package tr.gov.tubitak.uekae.esya.api.asn.cmp;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cmp.PBMParameter;

/**
 * User: zeldal.ozdemir
 * Date: 2/1/11
 * Time: 3:49 PM
 */
public class EPBMParameter extends BaseASNWrapper<PBMParameter>{

    public EPBMParameter(PBMParameter aObject) {
        super(aObject);
    }

    public EPBMParameter(byte[] aBytes) throws ESYAException {
        super(aBytes, new PBMParameter());
    }

    public EPBMParameter() {
        super(new PBMParameter());
    }
}
