/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass

using tr.gov.tubitak.uekae.esya.api.common.crypto;

namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    public interface IRandomGenerator
    {
        void nextBytes(byte[] aBytesToFill);
        void nextBytes(byte[] aBytesToFill, int aOffset, int aLength);

        void addSeeder(ISeed seeder) ;

        bool removeSeeder(ISeed seeder);

        int getSeederCount();

        void removeAllSeeders();
    }
}
