package dev.esya.api.xmlsignature.legacy.plugtests2010;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author ayetgin
 */
public class AllTests
{

    public static Test suite() throws Exception
    {
        TestSuite suit = new TestSuite();

        // verify
        suit.addTest(new TestSuite(ALBTest.class));
        suit.addTest(new TestSuite(ARDTest.class));
        suit.addTest(new TestSuite(ASCTest.class));
        suit.addTest(new TestSuite(BULLTest.class));
        suit.addTest(new TestSuite(CERTest.class));
        suit.addTest(new TestSuite(CRYTest.class));
        suit.addTest(new TestSuite(EGRTest.class));
        suit.addTest(new TestSuite(ELDTest.class));
        suit.addTest(new TestSuite(ENITest.class));
        suit.addTest(new TestSuite(FEDTest.class));
        suit.addTest(new TestSuite(IAIKTest.class));
        suit.addTest(new TestSuite(IBMTest.class));
        suit.addTest(new TestSuite(IPSTest.class));
        suit.addTest(new TestSuite(LUXTest.class));
        suit.addTest(new TestSuite(MICRTest.class));
        suit.addTest(new TestSuite(MITTest.class));
        suit.addTest(new TestSuite(MSECTest.class));
        suit.addTest(new TestSuite(NTTTest.class));
        suit.addTest(new TestSuite(POLTest.class));
        suit.addTest(new TestSuite(PSPWTest.class));
        suit.addTest(new TestSuite(SSCTest.class));
        //suit.addTest(new TestSuite(TUTest.class));
        suit.addTest(new TestSuite(TWTest.class));
        suit.addTest(new TestSuite(UPCTest.class));


        return suit;
    }

}
