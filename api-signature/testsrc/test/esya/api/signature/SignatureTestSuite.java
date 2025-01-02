package test.esya.api.signature;

import gnu.crypto.util.Base64;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.runners.Suite;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureFormat;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

/**
 * @author ayetgin
 */
@Suite.SuiteClasses({       // order is important for comment out ers!

                            ConfigTest.class,
                            BES.class ,
                            Multiple.class,
                            AttachDetach.class,
                            TurkishESigProfile.class,
                            Upgrade.class,
                            AtributesTest.class
                    })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class SignatureTestSuite
{
    public static SignatureFormat SIGNATURE_FORMAT;

    public static String ROOT = "./api-signature/testdata/";

    public SignatureTestSuite()
    {
        super();
    }

}
