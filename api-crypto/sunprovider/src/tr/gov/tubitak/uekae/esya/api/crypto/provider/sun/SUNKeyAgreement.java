package tr.gov.tubitak.uekae.esya.api.crypto.provider.sun;

import tr.gov.tubitak.uekae.esya.api.crypto.KeyAgreement;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Algorithm;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.KeyAgreementAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

import javax.crypto.SecretKey;
import java.security.*;

/**
 * @author ayetgin
 */
public class SUNKeyAgreement implements KeyAgreement {

    private javax.crypto.KeyAgreement keyAgreement;

    public SUNKeyAgreement(KeyAgreementAlg aKeyAgreementAlg) throws CryptoException {
        this(aKeyAgreementAlg, null);
    }

    public SUNKeyAgreement(KeyAgreementAlg aKeyAgreementAlg, Provider provider) throws CryptoException {
        try {
            if (provider != null)
                keyAgreement = javax.crypto.KeyAgreement.getInstance(aKeyAgreementAlg.getName(), provider);
            else
                keyAgreement = javax.crypto.KeyAgreement.getInstance(aKeyAgreementAlg.getName());
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("Cannnot Iinitialize KeyAgreement for:"+aKeyAgreementAlg+" Provider:"+provider);
        }
    }

    public void init(Key aKey, AlgorithmParams aParams) throws CryptoException {
        try {
            keyAgreement.init(aKey,aParams);
        } catch (Exception e) {
            throw new CryptoException("Error while Initing KeyAgreement:"+e.getMessage(),e);
        }
    }

/*    public byte[] generateKey(Key aKey, Algorithm alg) {
        return keyAgreement.generateSecret();
    }  */

    public SecretKey generateKey(Key aKey, Algorithm alg) throws CryptoException {
        try {
            return keyAgreement.generateSecret(alg.getName());
        } catch (Exception e) {
            throw new CryptoException("Cannot generate Secret:"+e.getMessage(),e);
        }
    }
}
