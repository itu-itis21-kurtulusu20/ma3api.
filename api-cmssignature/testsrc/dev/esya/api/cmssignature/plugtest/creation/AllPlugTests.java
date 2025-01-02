package dev.esya.api.cmssignature.plugtest.creation;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllPlugTests 
{
	public static  Test suite() throws Exception
    {
        TestSuite suit = new TestSuite();

        suit.addTest(new TestSuite(BES.class));
        suit.addTest(new TestSuite(EPES.class));
     
        return suit;
    }
}
