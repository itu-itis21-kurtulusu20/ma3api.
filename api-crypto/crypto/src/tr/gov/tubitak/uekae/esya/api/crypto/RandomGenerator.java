package tr.gov.tubitak.uekae.esya.api.crypto;


import tr.gov.tubitak.uekae.esya.api.common.crypto.ISeed;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;

/**
 * @author ayetgin
 */

public interface RandomGenerator
{
    void nextBytes(byte[] aBytesToFill);

    void nextBytes(byte[] aBytesToFill, int aOffset, int aLength);

    void addSeeder(ISeed seeder) throws CryptoException;

    boolean removeSeeder(ISeed seeder) throws CryptoException;

    void removeAllSeeders() throws CryptoException;

    int getSeederCount() throws CryptoException;

}
