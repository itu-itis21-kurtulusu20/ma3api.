package tr.gov.tubitak.uekae.esya.api.crypto.provider.sun;

import tr.gov.tubitak.uekae.esya.api.common.crypto.ISeed;
import tr.gov.tubitak.uekae.esya.api.crypto.RandomGenerator;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;

import java.security.SecureRandom;

/**
 * @author ayetgin
 */
public class SUNRandomGenerator implements RandomGenerator
{
    SecureRandom mRandom;

    public SUNRandomGenerator() {
        mRandom = new SecureRandom();
    }

    public void nextBytes(byte[] aBytesToFill){
        mRandom.nextBytes(aBytesToFill);
    }

    public void nextBytes(byte[] aBytesToFill, int aOffset, int aLength)    {
        byte[] rand = new byte[aLength];
        mRandom.nextBytes(rand);
        System.arraycopy(rand, 0, aBytesToFill, aOffset, aLength);
    }

    @Override
    public void addSeeder(ISeed seeder) throws CryptoException {
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
