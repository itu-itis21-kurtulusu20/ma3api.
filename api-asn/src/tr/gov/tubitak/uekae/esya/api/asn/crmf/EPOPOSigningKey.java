package tr.gov.tubitak.uekae.esya.api.asn.crmf;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.crmf.POPOSigningKey;

/**
 * User: zeldal.ozdemir
 * Date: 2/4/11
 * Time: 9:47 AM
 */
public class EPOPOSigningKey extends BaseASNWrapper<POPOSigningKey>{
    public EPOPOSigningKey(POPOSigningKey aObject) {
        super(aObject);
    }

    public EPOPOSigningKey(byte[] aBytes) throws ESYAException {
        super(aBytes, new POPOSigningKey());
    }

    public boolean hasPOPOSigningKeyInput(){
        return mObject.poposkInput != null;
    }

    public byte[] getSignatureValue(){
        return mObject.signature.value;
    }

    public EAlgorithmIdentifier getAlgorithmIdentifier(){
        return new EAlgorithmIdentifier(mObject.algorithmIdentifier);
    }
}
