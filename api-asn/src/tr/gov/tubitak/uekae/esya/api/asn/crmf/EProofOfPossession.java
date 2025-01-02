package tr.gov.tubitak.uekae.esya.api.asn.crmf;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.crmf.POPOPrivKey;
import tr.gov.tubitak.uekae.esya.asn.crmf.POPOSigningKey;
import tr.gov.tubitak.uekae.esya.asn.crmf.ProofOfPossession;

/**
 * User: zeldal.ozdemir
 * Date: 2/4/11
 * Time: 9:22 AM
 */
public class EProofOfPossession extends BaseASNWrapper<ProofOfPossession> {
    public final static int RAVERIFIED = ProofOfPossession._RAVERIFIED;
    public final static int SIGNATURE = ProofOfPossession._SIGNATURE;
    public final static int KEYENCIPHERMENT = ProofOfPossession._KEYENCIPHERMENT;
    public final static int KEYAGREEMENT = ProofOfPossession._KEYAGREEMENT;

    public EProofOfPossession(ProofOfPossession aObject) {
        super(aObject);
    }

    public EProofOfPossession(byte[] aBytes) throws ESYAException {
        super(aBytes, new ProofOfPossession());
    }

    public int getChoiceID() {
        return mObject.getChoiceID();
    }

    public EPOPOSigningKey getPopoSigningKey(){
        if(mObject.getElement() instanceof POPOSigningKey )
            return new EPOPOSigningKey((POPOSigningKey) mObject.getElement());
        else
            return null;
    }

    public EPOPOPrivKey getPopoPrivKey(){
        if(mObject.getElement() instanceof POPOPrivKey)
            return new EPOPOPrivKey((POPOPrivKey) mObject.getElement());
        else
            return null;
    }



}
