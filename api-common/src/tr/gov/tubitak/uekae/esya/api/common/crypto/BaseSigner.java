package tr.gov.tubitak.uekae.esya.api.common.crypto;

/**
 * Interface for signature implementations. 
 */

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.security.spec.AlgorithmParameterSpec;


public interface BaseSigner {
    
    /**
     * Returns the signature bytes computed from the given data. The format of the signature depends on the underlying signature scheme.
     * @param aData Data to be signed
     * @return Signature bytes. The format of the signature depends on the underlying signature scheme.
     * @throws ESYAException
     */
    byte[] sign(byte[] aData) throws ESYAException;

    
    /**
     * Returns the name of signature algorithm. To avoid ambiguity, names must be the same with the ones in {@link Algorithms} class.  
     * @return the name of signature algorithm used
     */
    public String getSignatureAlgorithmStr();

    /**
     * Returns the parameter specification used if the signature algorithm requires parameters. 
     * @return the parameter specification used if the signature algorithm requires parameters
     */
    public AlgorithmParameterSpec getAlgorithmParameterSpec();
}
