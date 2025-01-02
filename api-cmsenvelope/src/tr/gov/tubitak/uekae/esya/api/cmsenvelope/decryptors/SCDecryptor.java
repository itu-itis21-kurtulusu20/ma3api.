package tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors;

import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_ECDH1_DERIVE_PARAMS;
import sun.security.pkcs11.wrapper.CK_RSA_PKCS_OAEP_PARAMS;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.ECDHDecryptorParams;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.IDecryptorParams;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.RSADecryptorParams;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Cipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Wrapper;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.OAEPPadding;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.KeyAgreementAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.CryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.keyfinder.PublicKeyFinderWithCertSerialNo;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ISmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme.EncryptionSchemeFactory;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme.IEncryptionScheme;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ConstantsUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ECUtil;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;

import javax.crypto.SecretKey;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.spec.MGF1ParameterSpec;
import java.util.Arrays;
import java.util.List;

public class SCDecryptor implements IDecryptorStore {

    protected ISmartCard mSmartCard = null;
    protected long mSessionID = -1;
    protected ECertificate[] mCerts;
    CryptoProvider cryptoProvider = Crypto.getProvider();


    public SCDecryptor(ISmartCard aSmartCard, long aSessionID) throws CryptoException {
        mSmartCard = aSmartCard;
        mSessionID = aSessionID;
        mCerts = _readEncryptionCertificates();
    }

    public SCDecryptor(ISmartCard aSmartCard, long aSessionID, CryptoProvider cryptoProvider, boolean isReadCertificates) throws CryptoException {
        mSmartCard = aSmartCard;
        mSessionID = aSessionID;
        this.cryptoProvider = cryptoProvider;
        if (isReadCertificates)
            mCerts = _readEncryptionCertificates();
    }

    public SCDecryptor(ISmartCard aSmartCard, long aSessionID, boolean isReadCertificates) throws CryptoException {
        mSmartCard = aSmartCard;
        mSessionID = aSessionID;
        if (isReadCertificates)
            mCerts = _readEncryptionCertificates();
    }

    /**
     * Ramazan Girgin
     * ESYA 1.2 - ESYA 2.0 gecis sonrasi cikan problemler icin duzenlendi.
     */
    public SCDecryptor(ISmartCard aSmartCard, long aSessionID, CryptoProvider cryptoProvider) throws CryptoException {
        mSmartCard = aSmartCard;
        mSessionID = aSessionID;
        this.cryptoProvider = cryptoProvider;
        mCerts = _readEncryptionCertificates();
    }

    protected boolean isFIPSMode() {
        if (cryptoProvider == null) {
            return Crypto.isFipsMode();
        } else {
            return cryptoProvider.isFipsMode();
        }
    }

    protected SecretKey unwrapSymetricKey(byte[] wrappedData, IDecryptorParams params, byte[] certSerial) throws CryptoException {
        throw new UnsupportedOperationException("Only supported in FIPS mode");
    }

    public SecretKey decrypt(ECertificate aCert, IDecryptorParams params) throws CryptoException {
        ECertificate recipientCert = null;
        for (ECertificate cert : mCerts) {
            if (cert.equals(aCert)) {
                recipientCert = aCert;
                break;
            }
        }

        if (recipientCert != null) {
            byte[] certSerial = recipientCert.getSerialNumber().toByteArray();

            if (params instanceof RSADecryptorParams) {
                RSADecryptorParams rsaDecryptorParams = (RSADecryptorParams) params;
                SecretKey secretKey = decryptRSA(rsaDecryptorParams, certSerial);
                return secretKey;
            } else if (params instanceof ECDHDecryptorParams) {
                ECDHDecryptorParams ecdhDecryptorParams = (ECDHDecryptorParams) params;
                SecretKey secretKey = decryptEC(ecdhDecryptorParams, certSerial);
                return secretKey;
            }

            throw new CryptoException("Algorithm does not support");
        }
        throw new CryptoException("Certificate not found");
    }

    private SecretKey decryptRSA(RSADecryptorParams rsaDecryptorParams, byte[] certificateSerialNumber) throws CryptoException {

        byte[] encryptedKey = rsaDecryptorParams.getEncryptedKey();

        try {
            if (isFIPSMode()) {
                return unwrapSymetricKey(encryptedKey, rsaDecryptorParams, certificateSerialNumber);
            } else {

                Pair<CipherAlg, AlgorithmParams> cipherAlg = rsaDecryptorParams.getCipherAlg();
                byte[] key;

                if (Arrays.equals(cipherAlg.first().getOID(), _algorithmsValues.id_RSAES_OAEP)) {
                    OAEPPadding paddingAlg = (OAEPPadding) cipherAlg.first().getPadding();
                    long slotID = mSmartCard.getSessionInfo(mSessionID).slotID;

                    PublicKeyFinderWithCertSerialNo kf = new PublicKeyFinderWithCertSerialNo(mSmartCard, mSessionID, certificateSerialNumber);
                    OAEPParameterSpec spec = new OAEPParameterSpec(paddingAlg.getDigestAlg().getName(), paddingAlg.getMaskGenerationFunction().getName(), MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
                    IEncryptionScheme scheme = EncryptionSchemeFactory.getEncryptionScheme(false, Algorithms.CIPHER_RSAOAEP, spec, mSmartCard, slotID, kf);

                    byte[] paddedKey = mSmartCard.decryptDataWithCertSerialNo(mSessionID, certificateSerialNumber, scheme.getMechanism(), encryptedKey);
                    key = scheme.getResult(paddedKey);

                } else {
                    CK_MECHANISM mech = new CK_MECHANISM(0L);
                    mech.mechanism = PKCS11Constants.CKM_RSA_PKCS;
                    key = mSmartCard.decryptDataWithCertSerialNo(mSessionID, certificateSerialNumber, mech, encryptedKey);
                }
                return new SecretKeySpec(key, "AES");
            }
        } catch (Exception aEx) {
            throw new CryptoException("Kartta şifre çözülürken hata oluştu", aEx);
        }
    }

    private SecretKey decryptEC(ECDHDecryptorParams ecdhDecryptorParams, byte[] certificateSerialNumber) throws CryptoException {
        // KeyAgreement to PKCS11 Constants
        KeyAgreementAlg keyAgreementAlg = KeyAgreementAlg.fromOID(ecdhDecryptorParams.getKeyEncAlgOid());
        long kdfAlg = ConstantsUtil.getKeyDerivationPKCS11Constant(keyAgreementAlg.getDerivationFunctionAlg().getDigestAlg());
        int keyLength = KeyUtil.getKeyLength(WrapAlg.fromOID(ecdhDecryptorParams.getKeyWrapAlgOid()));

        byte[] sharedInfoBytes = null;
        if (kdfAlg != PKCS11Constants.CKD_NULL) {
            // sharedInfo'nun oluşturulması.
            sharedInfoBytes = ECUtil.generateKeyAgreementSharedInfoBytes(ecdhDecryptorParams.getKeyWrapAlgOid(), keyLength, ecdhDecryptorParams.getukm());
        }

        // Alıcının private key'i ve şifreleme yapılırken oluşturulan gecici EC anahtar çiftinin public key'i kullanılarak türetilecek wrap'leme anahtarının mekanizması
        // Bu mekanizma sabit mi olacak ?
        CK_MECHANISM mech = new CK_MECHANISM(PKCS11Constants.CKM_ECDH1_DERIVE);
            /*
            -- CK_ECDH1_DERIVE_PARAMS --
            1. parametre kdf (Key Derive Function).
            2. parametre sharedData
            3. parametre publicData (Public Key)
            */
        mech.pParameter = new CK_ECDH1_DERIVE_PARAMS(kdfAlg, sharedInfoBytes, ecdhDecryptorParams.getSenderPublicKey());

        // Türetilecek wrap'leme anahtarının KeyTemplate'i
        // Template'den default olarak gelen gereksiz attribute'ler silinmeli mi ? Ex: CKA_PRIVATE, CKA_LABEL ?
        KeyTemplate keyTemplate = new AESKeyTemplate("tmp-" + RandomUtil.generateRandomHexString(4));
        keyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE_LEN, keyLength/8));
        keyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, true));

        // Sertifika seri numarasına göre, alıcının private key handle'ının bulunması.
        long privateKeyHandle;
        try {
            privateKeyHandle = mSmartCard.getPrivateKeyObjIDFromCertificateSerial(mSessionID, certificateSerialNumber);
        } catch (SmartCardException | PKCS11Exception e) {
            throw new CryptoException("An exception occurred while finding the private key by certificate serial number.", e);
        }

        // Wrap'leme anahtarının türetilmesi.
        long derivedKeyId;
        try {
            derivedKeyId = mSmartCard.deriveKey(mSessionID, mech, privateKeyHandle, keyTemplate);
        } catch (PKCS11Exception e) {
            throw new CryptoException("An exception occurred while deriving the key.", e);
        }


        if (mSmartCard.getCardType().equals(CardType.AKIS)){
            // Türetilecek anahtarın CKA_VALUE'sunu okuyabilmek için.
            // AKIS'de bunları vermeden de çalışabildim!
            //keyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, false));
            //keyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true));

            // Türetilen wrap'leme anahtarı değerinin okunması.
            CK_ATTRIBUTE[] readTemplate = {new CK_ATTRIBUTE(PKCS11Constants.CKA_VALUE)};
            try {
                mSmartCard.getAttributeValue(mSessionID, derivedKeyId, readTemplate);
            } catch (PKCS11Exception e) {
                throw new CryptoException("An exception occurred while reading the value of the derived key.", e);
            }
            byte[] wrapperKeyBytes = (byte[]) readTemplate[0].pValue;

            // Türetilen wrap'leme anahtarının akıllı karttan silinmesi.
            try {
                mSmartCard.deleteObject(mSessionID, derivedKeyId);
            } catch (PKCS11Exception e) {
                throw new CryptoException("An exception occurred while deleting the derived key.", e);
            }

            // Key wrap algoritma oid'ine göre, wrap'leme algoritmasının bulunması.
            WrapAlg wrapAlg = WrapAlg.fromOID(ecdhDecryptorParams.getKeyWrapAlgOid());

            // Türetilen wrap'leme anahtarı ile wrap'lenmiş anahtarın unwrap edilmesi.
            Wrapper unwrapper = cryptoProvider.getUnwrapper(wrapAlg);
            unwrapper.init(new SecretKeySpec(wrapperKeyBytes, "KDF"));
            Key encryptionKey = unwrapper.unwrap(ecdhDecryptorParams.getWrappedKey());

            // İçeriği şifrelerken kullanılan simetrik anahtar.
            return (SecretKey) encryptionKey;
        } else {
            CK_MECHANISM ck_mechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_KEY_WRAP);
            byte[] decryptData;
            try {
                decryptData = mSmartCard.decryptData(mSessionID, derivedKeyId, ecdhDecryptorParams.getWrappedKey(), ck_mechanism);
            } catch (ESYAException | PKCS11Exception e) {
                throw new CryptoException("Can't decrypt wrap key.", e);
            }
            finally {
                try {
                    mSmartCard.deleteObject(mSessionID, derivedKeyId);
                } catch (PKCS11Exception e) {
                    throw new CryptoException("An exception occurred while deleting the derived key.", e);
                }
            }

            return new SecretKeySpec(decryptData, "AES");
        }


    }

    private CK_RSA_PKCS_OAEP_PARAMS getOAEPMechanismParams(OAEPPadding padding) throws ESYAException {
        CK_RSA_PKCS_OAEP_PARAMS oaep_params = new CK_RSA_PKCS_OAEP_PARAMS();

        if (padding.getDigestAlg() == DigestAlg.SHA1)
            oaep_params.hashAlg = PKCS11Constants.CKM_SHA_1;
        else if (padding.getDigestAlg() == DigestAlg.SHA256)
            oaep_params.hashAlg = PKCS11Constants.CKM_SHA256;
        else
            throw new CryptoException("Unknown hash algorith: " + padding.getDigestAlg().getName());


        oaep_params.mgf = ConstantsUtil.getMGFAlgorithm(oaep_params.hashAlg);
        oaep_params.pSourceData = null;

        return oaep_params;
    }

    protected SecretKey unwrapAgreedKey(IDecryptorParams params, byte[] serial) throws CryptoException {
        throw new UnsupportedOperationException("Only supported in FIPS mode");
    }

    public ECertificate[] getEncryptionCertificates() throws CryptoException {
        return mCerts;
    }


    public byte[] decrypt(CipherAlg simAlg, AlgorithmParams simAlgParams, byte[] encryptedContent, SecretKey anahtar) throws CryptoException {
        if (isFIPSMode()) {
            try {
                CK_MECHANISM aMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD);
                if (simAlgParams instanceof ParamsWithIV)
                    aMechanism.pParameter = ((ParamsWithIV) simAlgParams).getIV();
                return mSmartCard.decryptData(mSessionID, ((KeyTemplate) anahtar).getLabel(), encryptedContent, aMechanism);

            } catch (Exception e) {
                throw new CryptoException("Error while decrypting in SmartCard:" + e.getMessage(), e);
            }
        } else {
            BufferedCipher cipher;

            if (cryptoProvider != null) {
                Cipher decryptor = cryptoProvider.getDecryptor(simAlg);
                cipher = new BufferedCipher(decryptor);
            } else {
                cipher = Crypto.getDecryptor(simAlg);
            }
            cipher.init(anahtar, simAlgParams);
            return cipher.doFinal(encryptedContent);
        }
    }

    private ECertificate[] _readEncryptionCertificates() throws CryptoException {
        ECertificate[] certs = null;
        try {
            List<byte[]> certDatas = mSmartCard.getEncryptionCertificates(mSessionID);
            certs = new ECertificate[certDatas.size()];
            for (int i = 0; i < certs.length; i++)
                certs[i] = new ECertificate(certDatas.get(i));
        } catch (PKCS11Exception | SmartCardException e) {
            throw new CryptoException("Error in getting encryption certificates", e);
        }  catch (ESYAException e) {
            throw new CryptoException("Can not decode cert", e);
        }
        return certs;
    }
}
