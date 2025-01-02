import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tr.gov.tubitak.uekae.esya.api.testsuite.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({CAdES_Signing_Tests.class, CAdES_Validation_Tests.class,
                     //XAdES_Signing_Tests.class, XAdES_Validation_Tests.class,
                     PAdES_Signing_Tests.class, PAdES_Validation_Tests.class})
public class AcceptanceTestsRunner {

}