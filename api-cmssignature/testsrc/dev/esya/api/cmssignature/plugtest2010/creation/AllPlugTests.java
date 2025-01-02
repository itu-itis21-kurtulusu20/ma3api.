package dev.esya.api.cmssignature.plugtest2010.creation;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllPlugTests 
{
	public static  Test suite() throws Exception
    {
        TestSuite suit = new TestSuite();

        
        
        suit.addTest(new TestSuite(BES.class));
        suit.addTest(new TestSuite(EPES.class));
        suit.addTest(new TestSuite(ESA.class));
        suit.addTest(new TestSuite(ESC.class));
        suit.addTest(new TestSuite(EST.class));
        suit.addTest(new TestSuite(ESX.class));
        suit.addTest(new TestSuite(ESXLong.class));
        
        return suit;
    }
}
