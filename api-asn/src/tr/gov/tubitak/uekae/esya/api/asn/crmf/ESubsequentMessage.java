package tr.gov.tubitak.uekae.esya.api.asn.crmf;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.crmf.SubsequentMessage;

/**
 * User: zeldal.ozdemir
 * Date: 2/4/11
 * Time: 10:23 AM
 */
public class ESubsequentMessage extends BaseASNWrapper<SubsequentMessage> {
    public final static int encrCert = SubsequentMessage.encrCert;
    public final static int challengeResp = SubsequentMessage.challengeResp;

    public ESubsequentMessage(SubsequentMessage aObject) {
        super(aObject);
    }

    public ESubsequentMessage(byte[] aBytes) throws ESYAException {
        super(aBytes, new SubsequentMessage());
    }

    public long getValue(){
        return mObject.value;
    }
}
