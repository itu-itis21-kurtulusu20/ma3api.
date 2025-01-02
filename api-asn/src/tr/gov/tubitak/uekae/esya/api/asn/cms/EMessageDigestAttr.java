package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.MessageDigest;

public class EMessageDigestAttr extends BaseASNWrapper<MessageDigest> {

    public EMessageDigestAttr(byte[] aBytes) throws ESYAException {
        super(aBytes, new MessageDigest());
    }

    public byte[] getHash() {
       return mObject.value;
    }
}
