package tr.gov.tubitak.uekae.esya.api.asn.pkcs12;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.pkcs12.SafeBag;
import tr.gov.tubitak.uekae.esya.asn.pkcs12.SafeContents;

/**
 * User: zeldal.ozdemir
 * Date: 1/21/11
 * Time: 10:12 AM
 */
public class ESafeContents extends BaseASNWrapper<SafeContents>{
    public ESafeContents(byte[] aBytes) throws ESYAException {
        super(aBytes, new SafeContents());
    }

    public ESafeContents(ESafeBag[] safeBags) {
        super(new SafeContents());
        mObject.elements = new SafeBag[safeBags.length];
        for (int i = 0; i < safeBags.length; i++) {
            mObject.elements[i] = safeBags[i].getObject();
        }

    }
}
