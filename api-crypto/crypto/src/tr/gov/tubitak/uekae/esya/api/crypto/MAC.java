package tr.gov.tubitak.uekae.esya.api.crypto;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

import java.security.Key;

/**
 * @author ayetgin
 */
public interface MAC
{
    void init(Key aKey, AlgorithmParams aParams) throws CryptoException;
    void init(byte[] aKey, AlgorithmParams aParams) throws CryptoException;

    void process(byte[] aData) throws CryptoException;

    byte[] doFinal(byte[] aData) throws CryptoException;

    MACAlg getMACAlgorithm();    

    int getMACSize();

}
