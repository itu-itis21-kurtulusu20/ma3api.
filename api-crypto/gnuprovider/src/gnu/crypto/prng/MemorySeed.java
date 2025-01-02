package gnu.crypto.prng;

import java.security.SecureRandom;

import tr.gov.tubitak.uekae.esya.api.common.crypto.ISeed;

public class MemorySeed implements ISeed {

    public byte[] getSeed(int aLength) {
	
	SecureRandom s = new SecureRandom();
	return s.generateSeed(aLength);
    }

}
