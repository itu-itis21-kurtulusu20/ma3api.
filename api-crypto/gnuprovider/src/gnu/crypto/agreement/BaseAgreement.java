package gnu.crypto.agreement;

import java.math.BigInteger;
import java.security.Key;

/**
 * The basic interface that Diffie-Hellman implementations
 * conforms to.
 */

public interface BaseAgreement {
	 /**
     * initialize the agreement engine.
     */
    public void init(Key key);

    /**
     * given a public key from a given party calculate the next
     * message in the agreement sequence. 
     */
    public byte[] calculateAgreement(Key pubKey);
}
