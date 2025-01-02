package tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors;

import com.objsys.asn1j.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.ECDHDecryptorParams;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.IDecryptorParams;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.RSADecryptorParams;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.*;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.KeyAgreementAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithSharedInfo;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.CryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ECUtil;
import tr.gov.tubitak.uekae.esya.asn.pkcs1pkcs8.PrivateKeyInfo;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectPublicKeyInfo;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;


public class MemoryDecryptor implements IDecryptorStore
{

    private static Logger logger = LoggerFactory.getLogger(MemoryDecryptor.class);

    Map<ECertificate, PrivateKey>  mDecryptors;
    CryptoProvider mCryptoProvider;
    /**
     *
     * @param decryptors Private key.
     */
    public MemoryDecryptor(Pair<ECertificate, PrivateKey>...decryptors)
    {
        mDecryptors = new HashMap<ECertificate, PrivateKey>();
        for(int i=0; i < decryptors.length; i++)
        {
            mDecryptors.put(decryptors[i].getObject1(), decryptors[i].getObject2());
        }
    }

    public MemoryDecryptor(CryptoProvider cryptoProvider, Pair<ECertificate, PrivateKey>...decryptors)
    {
        this(decryptors);
        mCryptoProvider = cryptoProvider;
    }

    public SecretKey decrypt(ECertificate aCert,IDecryptorParams aParams) throws CryptoException {
        PrivateKey privKey = mDecryptors.get(aCert);
        Key symmetricKey = null;

        if(aParams instanceof ECDHDecryptorParams)
        {
            ECDHDecryptorParams ecdhParams = (ECDHDecryptorParams)aParams;

            PublicKey senderPublicKey = _getPublicKey(privKey, ecdhParams.getSenderPublicKey());
            int keyLength = KeyUtil.getKeyLength(WrapAlg.fromOID(ecdhParams.getKeyWrapAlgOid()));
            byte []sharedInfoBytes = ECUtil.generateKeyAgreementSharedInfoBytes(ecdhParams.getKeyWrapAlgOid(), keyLength, ecdhParams.getukm());

            WrapAlg wrapAlg = WrapAlg.fromOID(ecdhParams.getKeyWrapAlgOid());
            KeyAgreementAlg agreementAlg = KeyAgreementAlg.fromOID(ecdhParams.getKeyEncAlgOid());
            AlgorithmParams sharedInfoParam = new ParamsWithSharedInfo(sharedInfoBytes);

            KeyAgreement agreement = getCryptoProvider().getKeyAgreement(agreementAlg);
            agreement.init(privKey, sharedInfoParam);
            SecretKey secretKeyBytes = agreement.generateKey(senderPublicKey, wrapAlg);

            try
            {
                byte [] wrappedKey = ecdhParams.getWrappedKey();
                Wrapper wrapper = getCryptoProvider().getUnwrapper(wrapAlg);
                wrapper.init(secretKeyBytes);
                symmetricKey = wrapper.unwrap(wrappedKey);
            }
            catch(Exception e)
            {
                throw new CryptoException("Can not unwrap key",e);
            }
        }
        else if(aParams instanceof RSADecryptorParams)
        {
            byte [] key= null;
            RSADecryptorParams rsaParams = (RSADecryptorParams) aParams;
            key = decryptRSA(rsaParams.getEncryptedKey(), privKey, rsaParams.getAlgorithmIdentifier());

            symmetricKey = new SecretKeySpec(key, "AES"); // fix...

        }
        else
        {
            throw new CryptoException("Unknown parameter type");
        }
        return (SecretKey) symmetricKey;
    }

    public byte[] decryptRSA(byte[] aData, PrivateKey aKey, EAlgorithmIdentifier algorithmIdentifier) throws CryptoException {

        Pair<CipherAlg, AlgorithmParams> cipherAlg = CipherAlg.fromAlgorithmIdentifier(algorithmIdentifier);
        BufferedCipher cipher = new BufferedCipher(getCryptoProvider().getDecryptor(cipherAlg.first()));
        cipher.init(aKey, cipherAlg.second());
        return cipher.doFinal(aData);
    }

    //Generate sender public key from receiver private key algorithm identifier and sender public key bytes
    private PublicKey _getPublicKey(PrivateKey receiverPrivate, byte[] senderPublicKeyBytes) throws CryptoException
    {
        PublicKey senderPublicKey = null;
        try
        {
            PrivateKeyInfo privInfo = new PrivateKeyInfo();
            privInfo = (PrivateKeyInfo) AsnIO.arraydenOku(privInfo, receiverPrivate.getEncoded());

            Asn1BitString subjectPublicKey = new Asn1BitString(senderPublicKeyBytes.length * 8, senderPublicKeyBytes);
            SubjectPublicKeyInfo publicKeyInfo = new SubjectPublicKeyInfo(privInfo.privateKeyAlgorithm, subjectPublicKey);
            Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
            publicKeyInfo.encode(buff);

            KeyFactory keyFactory = getCryptoProvider().getKeyFactory();
            senderPublicKey = keyFactory.decodePublicKey(AsymmetricAlg.ECDSA, buff.getMsgCopy());

        }
        catch(Exception e)
        {
            throw new CryptoException("Can not encode PublicKey", e);
        }
        return senderPublicKey;
    }

    public ECertificate[] getEncryptionCertificates() throws CMSException
    {
        return mDecryptors.keySet().toArray(new ECertificate[0]);
    }

    public byte[] decrypt(CipherAlg simAlg, AlgorithmParams simAlgParams, byte[] encryptedContent, SecretKey anahtar) throws CryptoException {

        BufferedCipher cipher = new BufferedCipher(getCryptoProvider().getDecryptor(simAlg));
        cipher.init(anahtar, simAlgParams);
        return cipher.doFinal(encryptedContent);
    }

    public CryptoProvider getCryptoProvider() {
        if(mCryptoProvider != null)
            return mCryptoProvider;
        else
            return Crypto.getProvider();
    }
}
