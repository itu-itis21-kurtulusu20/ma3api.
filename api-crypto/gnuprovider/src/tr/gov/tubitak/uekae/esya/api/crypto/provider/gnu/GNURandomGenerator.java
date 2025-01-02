package tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu;

import gnu.crypto.Registry;
import gnu.crypto.prng.DGKGF;
import gnu.crypto.prng.PRNGFactory;
import gnu.crypto.util.PRNG;
import tr.gov.tubitak.uekae.esya.api.common.crypto.IRandom;
import tr.gov.tubitak.uekae.esya.api.common.crypto.ISeed;
import tr.gov.tubitak.uekae.esya.api.crypto.RandomGenerator;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ayetgin
 */
public class GNURandomGenerator implements RandomGenerator
{

    static ArrayList<ISeed> seeders = new ArrayList<ISeed>();

    public void nextBytes(byte[] bytes)
    {
        PRNG.nextBytes(bytes);
    }

    public void nextBytes(byte[] bytes, int start, int len)
    {
        PRNG.nextBytes(bytes, start, len);
    }

    @Override
    public void addSeeder(ISeed seeder) throws CryptoException {

        if(seeders.isEmpty() == false)
            throw  new CryptoException("The GNURandomGenerator accept only one seeder");

        if(isSeederExist(seeder))
            throw  new CryptoException("This seeder is already added ");

        seeders.add(seeder);

        IRandom instance = PRNGFactory.getInstance(Registry.DGKGF_PRNG);
        HashMap hashMap = new HashMap();
        hashMap.put(DGKGF.SEED_GENERATOR, seeder);
        instance.init(hashMap);
        PRNG.setPRNG(instance);
    }



    @Override
    public boolean removeSeeder(ISeed seeder) throws CryptoException {
        throw new CryptoException("Not supported");
    }

    @Override
    public void removeAllSeeders() {
        seeders.clear();
        PRNG.setDefaultPRNG();
    }

    @Override
    public int getSeederCount() throws CryptoException {
        return seeders.size();
    }

    private boolean isSeederExist(ISeed seeder) {
        for (ISeed aSeed : seeders) {
            if(aSeed.equals(seeder))
                return true;
        }

        return false;
    }
}
