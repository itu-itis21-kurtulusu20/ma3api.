package dev.esya.api.cmssignature.plugtest.validation;


import junit.framework.Test;
import junit.framework.TestSuite;
import dev.esya.api.cmssignature.plugtest.validation.ENT.BES;
import dev.esya.api.cmssignature.plugtest.validation.ENT.EPES;
import dev.esya.api.cmssignature.plugtest.validation.ENT.ESA;
import dev.esya.api.cmssignature.plugtest.validation.NEGATIVE.EST;
import dev.esya.api.cmssignature.plugtest.validation.NEGATIVE.ESX;
import dev.esya.api.cmssignature.plugtest.validation.NEGATIVE.ESXLong;


public class AllValidationTests  
{
	public static  Test suite() throws Exception
    {
		//BasicConfigurator.configure();
		
        TestSuite suit = new TestSuite();

        suit.addTest(new TestSuite(BES.class));
        suit.addTest(new TestSuite(EPES.class));
        suit.addTest(new TestSuite(ESA.class));
        
        suit.addTest(new TestSuite(dev.esya.api.cmssignature.plugtest.validation.MA3.ESA.class));
        
        suit.addTest(new TestSuite(dev.esya.api.cmssignature.plugtest.validation.NEGATIVE.BES.class));
        suit.addTest(new TestSuite(dev.esya.api.cmssignature.plugtest.validation.NEGATIVE.EPES.class));
        suit.addTest(new TestSuite(dev.esya.api.cmssignature.plugtest.validation.NEGATIVE.ESA.class));
        suit.addTest(new TestSuite(EST.class));
        suit.addTest(new TestSuite(ESX.class));
        suit.addTest(new TestSuite(ESXLong.class));
        
        return suit;
    }
}
