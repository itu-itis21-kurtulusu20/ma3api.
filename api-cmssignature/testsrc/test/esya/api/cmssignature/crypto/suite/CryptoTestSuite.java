package test.esya.api.cmssignature.crypto.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.esya.api.cmssignature.crypto.PfxParserTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        PfxParserTest.class
})
public class CryptoTestSuite {
}
