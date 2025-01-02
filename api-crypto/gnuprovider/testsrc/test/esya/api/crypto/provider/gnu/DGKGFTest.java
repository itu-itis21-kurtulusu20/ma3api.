package test.esya.api.crypto.provider.gnu;

/**
 * DGKGF (Donanimsal Gurultu Kaynagi Genisletme Fonksiyonu icin kripto grubundan verilen test verileri)
 * 
 */

import gnu.crypto.Registry;
import gnu.crypto.prng.DGKGF;
import gnu.crypto.prng.PRNGFactory;

import java.util.HashMap;

import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.common.crypto.IRandom;
import tr.gov.tubitak.uekae.esya.api.common.crypto.ISeed;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;

public class DGKGFTest extends TestCase{

    
    public static void testTestVector1()
    throws Exception
    {
	String D = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
	String G = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000DC95C078A2408989AD48A2149284208708C374848C228233C2B34F332BD2E9D3";
	
	
	int L = 768;
	
	boolean result = _test(L, D, G);
	
	
	assertEquals(true, result);
    }
    
    
    public static void testTestVector2()
    throws Exception
    {
	String D = "36363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636";
	String G = "363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636367BC0BAC87CA5842F0C9B59802DCC02D81914BAA126F449742DC69C4CA4B71A7890E10073E3A5A062F2846B53AA7963C9CCE5F382463CDFC184A07E96F22863B7";
	int L = 1024;
	
	boolean result = _test(L, D, G);
	
	assertEquals(true, result);

    }
    
    
    public static void testTestVector3()
    throws Exception
    {
	String D = "000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f202122232425262728292a2b2c2d2e2f303132333435363738393a3b3c3d3e3f";
	String G = "000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3FFC48088F96DB248C5CC6F19ADE315E0C3D4665482C1CDEC7945F300FB65798D5648AD88037BC93F0E15126CCDF6F8BF8DC8413EC3BAC4C1D0B4D4841727D4C044BD196DB9DE8FCD94D926A9E3130486EFF8CA743F0C9C07058053ED4FC226A89D9E766688806F71C28AE72994178A77AB755894C39AFCD9334C986B4952ADED805A29AF8B9F042D83848CCD23852382A808A86A4B809CBE3F5F1D1ABE553DC60BC38D7EA588E64DB390F837AE620C6D9A1B90CCE6F2BC6757562BA855D08BCFC";
	int L = 2048;
	
	boolean result = _test(L, D, G);
	
	assertEquals(true, result);
    }
    
    private static boolean _test(int aOutputLength,String aInitialSeed,String aExpectedOutputValue)
    throws Exception
    {
	IRandom r = PRNGFactory.getInstance(Registry.DGKGF_PRNG);
	
	
	HashMap<String, Object> map = new HashMap<String, Object>();
	map.put(DGKGF.SEED_GENERATOR, new DGKGFTest().new TestSeed(StringUtil.toByteArray(aInitialSeed)));
	
	
	r.init(map);
	
	byte[] output = new byte[aOutputLength/8];
	
	r.nextBytes(output, 0, aOutputLength/8);
	
	return aExpectedOutputValue.equalsIgnoreCase(StringUtil.toString(output));
    }
    
    public static void main(String[] args) {
	try
	{
	    testTestVector1();
	    testTestVector2();
	    testTestVector3();
	}
	catch(Exception aEx)
	{
	    aEx.printStackTrace();
	}
    }
    
    class TestSeed implements ISeed{

	byte[] seed = null;
	
	public TestSeed(byte[] aSeed) {
	    seed = aSeed;
	}
	
	public byte[] getSeed(int aLength) {
	   return seed;
	}
	

	
    }
    
}
