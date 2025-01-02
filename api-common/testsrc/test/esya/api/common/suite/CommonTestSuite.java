package test.esya.api.common.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.esya.api.common.Base64Test;
import test.esya.api.common.LicenseUtilTest;
import test.esya.api.common.LicenseValidatorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LicenseUtilTest.class,
        LicenseValidatorTest.class,
        Base64Test.class
})
public class CommonTestSuite {
}
