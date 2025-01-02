package tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors;

import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.IDecryptorParams;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.RSADecryptorParams;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.CryptoProvider;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ISmartCard;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SCDecryptorWithKeyID extends SCDecryptor {

    long keyID;

    public SCDecryptorWithKeyID(ISmartCard aSmartCard, long aSessionID, CryptoProvider cryptoProvider, long keyID, ECertificate[] certs) throws CryptoException {
        super(aSmartCard, aSessionID, cryptoProvider, false);
        mSmartCard = aSmartCard;
        mSessionID = aSessionID;
        this.keyID = keyID;
        this.cryptoProvider = cryptoProvider;
        this.mCerts = certs;
    }

    public SCDecryptorWithKeyID(ISmartCard aSmartCard, long aSessionID, long keyID, ECertificate[] certs) throws CryptoException {
        super(aSmartCard, aSessionID, false);
        mSmartCard = aSmartCard;
        mSessionID = aSessionID;
        this.keyID = keyID;
        this.mCerts = certs;
    }

    public SecretKey decrypt(ECertificate aCert, IDecryptorParams params) throws CryptoException {
        if (params instanceof RSADecryptorParams) {
            byte[] encryptedData = ((RSADecryptorParams) params).getEncryptedKey();
            try {
                byte[] key = mSmartCard.decryptData(mSessionID, keyID, encryptedData, new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS));
                return new SecretKeySpec(key, "AES");
            } catch (Exception e) {
                throw new CryptoException("Error while Decrypting in SmartCard:" + e.getMessage(), e);
            }
        }
        throw new CryptoException("Algorithm does not support");
    }

    protected SecretKey unwrapAgreedKey(IDecryptorParams params, byte[] serial) throws CryptoException {
        throw new UnsupportedOperationException("Only supported in FIPS mode");
    }
}
