using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.common.crypto;

namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncyRandomGenerator: IRandomGenerator
    {
        static ArrayList seeders = new ArrayList();

        public void nextBytes(byte[] bytes)
        {
            SecureRandom rng = new SecureRandom();
            foreach (ISeed aSeeder in seeders)
            {
                rng.SetSeed(aSeeder.getSeed(16));
            }
            rng.NextBytes(bytes);
        }

        public void nextBytes(byte[] bytes, int start, int len)
        {
            SecureRandom rng = new SecureRandom();

            foreach (ISeed aSeeder in seeders)
            {
                rng.SetSeed(aSeeder.getSeed(16));
            }

            rng.NextBytes(bytes, start, len);
        }

        public void addSeeder(ISeed seeder)
        {
            if (isSeederExist(seeder))
                throw new CryptoException("This seeder is already added ");

            seeders.Add(seeder);
        }

        public bool removeSeeder(ISeed seeder)
        {
            if (seeders.Contains(seeder))
            {
                seeders.Remove(seeder);
                return true;
            }

            return false;
        }

        public void removeAllSeeders()
        {
            seeders.Clear();
        }

        public int getSeederCount()
        {
            return seeders.Count;
        }

        private bool isSeederExist(ISeed seeder)
        {
            foreach (ISeed aSeed in seeders)
            {
                if (aSeed.Equals(seeder))
                    return true;
            }

            return false;
        }


    }
}
