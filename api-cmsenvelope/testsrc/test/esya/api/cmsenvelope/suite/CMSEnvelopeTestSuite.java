package test.esya.api.cmsenvelope.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.esya.api.cmsenvelope.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CMSEnvelopeKeyTransTest.class,
        CMSEnvelopeKeyTransTest.CMSEnvelopeKeyTrans.class,
        CMSEnvelopeKeyTransTest.DeprecatedAlgorithmsTest.class,
        CMSEnvelopeKeyAgreementTest.class,
        CMSEnvelopeAddRecipientToParserTest.class,
        MemoryGenerationStreamParseTest.class,
        StreamGenerationMemoryParseTest.class
})
public class CMSEnvelopeTestSuite {

}

