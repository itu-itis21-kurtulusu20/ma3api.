package tr.gov.tubitak.uekae.esya.api.crypto;


import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;

import java.security.KeyPair;

/**
 * @author ayetgin
 */

public interface KeyPairGenerator
{
    

	/**
	 * @param aParams to be used for key pair generation
     * @throws CryptoException if anything goes wrong
	 */
    KeyPair generateKeyPair(AlgorithmParams aParams) throws CryptoException;
}
