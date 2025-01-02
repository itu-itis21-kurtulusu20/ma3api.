package dev.esya.api.xmlsignature.legacy;

import dev.esya.api.xmlsignature.legacy.bes.*;
import dev.esya.api.xmlsignature.legacy.bes.ma3.BES7;
import dev.esya.api.xmlsignature.legacy.bes.ma3.BES8;
import dev.esya.api.xmlsignature.legacy.xmldsig.MultipleSignaturesTest;
import dev.esya.api.xmlsignature.legacy.xmldsig.SignatureAlgorithmsTest;
import junit.framework.Test;
import junit.framework.TestSuite;
import dev.esya.api.xmlsignature.legacy.xmldsig.interop.baltimore.Baltimore23Test;
import dev.esya.api.xmlsignature.legacy.xmldsig.interop.iaik.IAIKCoreFeaturesTest;
import dev.esya.api.xmlsignature.legacy.xmldsig.interop.iaik.IAIKSignatureAlgorithmsTest;
import dev.esya.api.xmlsignature.legacy.xmldsig.interop.iaik.IAIKTransformsTest;
import dev.esya.api.xmlsignature.legacy.xmldsig.interop.javax.JavaxSignaturesTest;
import dev.esya.api.xmlsignature.legacy.xmldsig.interop.phaos.Phaos3Test;

/**
 * @author ahmety
 *         date: Nov 23, 2009
 */
public class AllTests
{
    
    public static Test suite() throws Exception
    {
        TestSuite suit = new TestSuite();
        // XAdES
        // BES
        suit.addTest(new TestSuite(NegativeBESTest.class));

        //suit.addTest(new TestSuite(BES10.class));
        //suit.addTest(new TestSuite(BES9.class));
        suit.addTest(new TestSuite(BES8.class));
        suit.addTest(new TestSuite(BES7.class));
        suit.addTest(new TestSuite(CounterSignatureTest.class));
        suit.addTest(new TestSuite(MultipleWithCounterSignaturesTest.class));
        suit.addTest(new TestSuite(BESSignatureAlgorithmsTest.class));
        suit.addTest(new TestSuite(SignedSignaturePropsTest.class));

        // EPES
        // todo annoying c14n test
        // suit.addTest(new TestSuite(NegativeEPESTest.class));

        // T
        // todo zd ayarlari
        //suit.addTest(new TestSuite(T1.class));

        // C
        // todo attribute cert validation!
        //suit.addTest(new TestSuite(CWithAttributeCertsTest.class));


        // XMLdSig
        suit.addTest(new TestSuite(Baltimore23Test.class));
        suit.addTest(new TestSuite(IAIKCoreFeaturesTest.class));
        suit.addTest(new TestSuite(IAIKSignatureAlgorithmsTest.class));
        suit.addTest(new TestSuite(IAIKTransformsTest.class));
        suit.addTest(new TestSuite(JavaxSignaturesTest.class));
        suit.addTest(new TestSuite(Phaos3Test.class));
        suit.addTest(new TestSuite(MultipleSignaturesTest.class));
        suit.addTest(new TestSuite(SignatureAlgorithmsTest.class));


        return suit;
    }

}
