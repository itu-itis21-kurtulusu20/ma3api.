

using System;
using test.esya.api.signature;
using tr.gov.tubitak.uekae.esya.api.signature;

namespace test.esya.api.signature
{

    //  @Suite.SuiteClasses({       /* order is important for comment out ers! */
    /*                    ConfigTest.class,
                       BES.class,
                       Multiple.class ,
                       AttachDetach.class,
                       TurkishESigProfile.class
                       Upgrade.class
               })
*/
    //@FixMethodOrder(MethodSorters.NAME_ASCENDING)
    public abstract class SignatureTestSuite
    {
        public static SignatureFormat SIGNATURE_FORMAT;

        public static String ROOT = "E:\\ahmet\\prj\\MA3API\\MA3\\api-signature\\testdata\\";

        public SignatureTestSuite()
            : base()
        {
        }
    }
}
