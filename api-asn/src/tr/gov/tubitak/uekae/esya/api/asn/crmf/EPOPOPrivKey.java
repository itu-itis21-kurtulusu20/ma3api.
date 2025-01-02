package tr.gov.tubitak.uekae.esya.api.asn.crmf;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.crmf.POPOPrivKey;
import tr.gov.tubitak.uekae.esya.asn.crmf.SubsequentMessage;

/**
 * User: zeldal.ozdemir
 * Date: 2/4/11
 * Time: 9:49 AM
 */
public class EPOPOPrivKey extends BaseASNWrapper<POPOPrivKey>{
    public EPOPOPrivKey(POPOPrivKey aObject) {
        super(aObject);
    }

    public EPOPOPrivKey(byte[] aBytes) throws ESYAException {
        super(aBytes, new POPOPrivKey());
    }

    public ESubsequentMessage getSubsequentMessage(){
        if(mObject.getChoiceID() == POPOPrivKey._SUBSEQUENTMESSAGE)
            return new ESubsequentMessage((SubsequentMessage) mObject.getElement());
        else
            return null;
    }

}
