package tr.gov.tubitak.uekae.esya.api.asn.pkcs12;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.pkcs12.PFX;
import tr.gov.tubitak.uekae.esya.asn.pkcs12.PFX_version;

/**
 * User: zeldal.ozdemir
 * Date: 1/21/11
 * Time: 10:09 AM
 */
public class EPFX extends BaseASNWrapper<PFX>{
    public EPFX(byte[] aBytes) throws ESYAException {
        super(aBytes,new PFX());
    }

    public EPFX(EContentInfo authSafe, EMacData macData) {
        super(new PFX(PFX_version.v3, authSafe.getObject(), macData.getObject()));
    }
}
