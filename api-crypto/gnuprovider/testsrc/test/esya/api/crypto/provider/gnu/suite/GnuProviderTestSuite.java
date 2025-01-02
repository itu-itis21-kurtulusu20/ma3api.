package test.esya.api.crypto.provider.gnu.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.esya.api.crypto.provider.gnu.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AESWrapperTest.class,
        DGKGFTest.class,
        EME_OAEPTest.class,
        EMSA_ISO9796d2Test.class,
        GCMRandomTests.class,
        GCMTest.class,
        GNUSymmetricCipherTest.class,
        NISTGCMTest.class,
        RSAPKCS1V1_5SignatureTest.class,
        RSASha256PaddingBrokenAlgIDNullParameterTest.class,
        SUNGNUInteropTest.class
})
public class GnuProviderTestSuite {
}
