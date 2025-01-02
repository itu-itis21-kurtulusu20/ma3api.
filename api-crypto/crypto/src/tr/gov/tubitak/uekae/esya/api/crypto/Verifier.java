package tr.gov.tubitak.uekae.esya.api.crypto;

import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

import java.security.PublicKey;

/**
 * @author ayetgin
 */

public interface Verifier
{
    void init(PublicKey aPublicKey) throws CryptoException;
    void init(PublicKey aPublicKey, AlgorithmParams aParams) throws CryptoException;
    
    void reset() throws CryptoException;

    void update(byte[] aData);
    void update(byte[] aData, int aOffset, int aLength);

    boolean verifySignature(byte[] aSignature);
}
