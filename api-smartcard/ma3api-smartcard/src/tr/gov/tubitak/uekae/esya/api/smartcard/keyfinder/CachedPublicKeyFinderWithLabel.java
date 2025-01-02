package tr.gov.tubitak.uekae.esya.api.smartcard.keyfinder;

import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.security.spec.KeySpec;

public class CachedPublicKeyFinderWithLabel extends PublicKeyFinderWithLabel  {
    KeySpec publicKey;
    int keyLenght;

    public CachedPublicKeyFinderWithLabel(SmartCard smartCard, long sessionId, String keyLabel) throws SmartCardException, PKCS11Exception {
        super(smartCard, sessionId, keyLabel);
        publicKey = super.getKeySpec();
        keyLenght = super.getKeyLength();
    }

    @Override
    public KeySpec getKeySpec() throws SmartCardException, PKCS11Exception {
        return publicKey;
    }

    @Override
    public int getKeyLength() throws SmartCardException, PKCS11Exception {
        return keyLenght;
    }
}
