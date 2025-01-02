package test.esya.api.asn.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.esya.api.asn.ct.SctTest;
import test.esya.api.asn.passport.CvcCaTest;
import test.esya.api.asn.passport.SelfCvcUretTest;
import test.esya.api.asn.x509.EDocumentTypeListExtensionTest;
import test.esya.api.asn.x509.PrivateKeyUsagePeriodExtensionTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SctTest.class,
        CvcCaTest.class,
        SelfCvcUretTest.class,
        EDocumentTypeListExtensionTest.class,
        PrivateKeyUsagePeriodExtensionTest.class
})
public class ASNTestSuite {

}

