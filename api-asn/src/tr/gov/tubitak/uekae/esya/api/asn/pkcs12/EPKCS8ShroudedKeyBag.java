package tr.gov.tubitak.uekae.esya.api.asn.pkcs12;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.pkcs12.PKCS8ShroudedKeyBag;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

/**
 * User: zeldal.ozdemir
 * Date: 1/21/11
 * Time: 10:11 AM
 */
public class EPKCS8ShroudedKeyBag extends BaseASNWrapper<PKCS8ShroudedKeyBag>{
    public EPKCS8ShroudedKeyBag(byte[] aBytes) throws ESYAException {
        super(aBytes, new PKCS8ShroudedKeyBag());
    }

    public EPKCS8ShroudedKeyBag(EAlgorithmIdentifier encryptionAlgorithm, byte[] encryptedData) {
        super(new PKCS8ShroudedKeyBag(encryptionAlgorithm.getObject(), encryptedData));
    }
}
