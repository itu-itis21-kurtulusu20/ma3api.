package tr.gov.tubitak.uekae.esya.api.asn.crmf;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.crmf.EncryptedValue;

/**
 * User: zeldal.ozdemir
 * Date: 2/2/11
 * Time: 11:46 AM
 */
public class EEncryptedValue extends BaseASNWrapper<EncryptedValue>{
    public EEncryptedValue(EncryptedValue aObject) {
        super(aObject);
    }

    public EEncryptedValue(byte [] bytes) throws ESYAException {
        super(bytes,new EncryptedValue());
    }
}
