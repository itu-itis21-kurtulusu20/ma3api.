package tr.gov.tubitak.uekae.esya.api.crypto.provider.nss;

import tr.gov.tubitak.uekae.esya.api.common.crypto.ISeed;
import tr.gov.tubitak.uekae.esya.api.crypto.RandomGenerator;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;

import java.security.Provider;
import java.security.SecureRandom;

public class NSSRandomGenerator implements RandomGenerator {

	Provider p = null;
	private static final String RANDOM_ALG_NAME = "PKCS11";
	
	public NSSRandomGenerator(Provider aProvider)
	{
		p = aProvider;
	}

	public void nextBytes(byte[] aBytesToFill) {
		try
		{
			SecureRandom.getInstance(RANDOM_ALG_NAME,p).nextBytes(aBytesToFill);
		}
		catch(Exception aEx)
		{
			throw new RuntimeException(aEx);
		}
	}

	public void nextBytes(byte[] aBytesToFill, int aOffset, int aLength) {
		try
		{
			byte[] rand = new byte[aLength];
			nextBytes(rand);
	        System.arraycopy(rand, 0, aBytesToFill, aOffset, aLength);
		}
		catch(Exception aEx)
		{
			throw new RuntimeException(aEx);
		}
		
	}

	@Override
	public void addSeeder(ISeed seeder) throws CryptoException{
		throw new CryptoException("Not supported");
	}

	@Override
	public boolean removeSeeder(ISeed seeder) throws CryptoException{
		throw new CryptoException("Not supported");
	}

	@Override
	public void removeAllSeeders() throws CryptoException{
		throw new CryptoException("Not supported");
	}

	@Override
	public int getSeederCount() throws CryptoException {
		throw new CryptoException("Not supported");
	}

}
