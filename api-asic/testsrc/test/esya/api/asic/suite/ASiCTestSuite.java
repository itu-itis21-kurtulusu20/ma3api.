package test.esya.api.asic.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.esya.api.asic.ExtendedTest;
import test.esya.api.asic.GenerationTest;
import test.esya.api.asic.OverWriteTest;
import test.esya.api.asic.UpgradeTest;

/**
 * @uathor ayetgin
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({       // order is important for comment out ers!
        GenerationTest.class,
        ExtendedTest.class ,
        UpgradeTest.class,
        OverWriteTest.class
})
public class ASiCTestSuite {

}

