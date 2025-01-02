package tr.gov.tubitak.uekae.esya.api.asn.pkcs12;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentInfo;
import tr.gov.tubitak.uekae.esya.asn.pkcs12.AuthenticatedSafe;

/**
 * User: zeldal.ozdemir
 * Date: 1/21/11
 * Time: 10:07 AM
 */
public class EAuthenticatedSafe extends BaseASNWrapper<AuthenticatedSafe>{
    public EAuthenticatedSafe(byte[] aBytes) throws ESYAException {
        super(aBytes, new AuthenticatedSafe());
    }

    public EAuthenticatedSafe(EContentInfo[] contentInfos) {
        super(new AuthenticatedSafe());
        this.getObject().elements = new ContentInfo[contentInfos.length];
        for (int i = 0; i < contentInfos.length; i++) {
            this.getObject().elements[i] = contentInfos[i].getObject();
        }
    }
}
