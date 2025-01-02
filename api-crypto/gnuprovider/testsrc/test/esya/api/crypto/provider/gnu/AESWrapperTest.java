package test.esya.api.crypto.provider.gnu;

import gnu.crypto.wrapper.AESWrapper;
import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;

public class AESWrapperTest extends TestCase
{
	byte [] kek;
	byte [] keyData;
	//expected result
	byte [] eResult;

	AESWrapper wrapper;
	//actual result
	byte [] aResult;

	public void test192bitKeyData192bitKEK() throws Exception
	{
		kek = StringUtil.toByteArray("000102030405060708090A0B0C0D0E0F1011121314151617");
		keyData = StringUtil.toByteArray("00112233445566778899AABBCCDDEEFF0001020304050607");
		eResult = StringUtil.toByteArray("031D33264E15D33268F24EC260743EDCE1C6C7DDEE725A936BA814915C6762D2");

		testWrapper(kek,keyData,eResult);

	}

	private void testWrapper(byte[] kek, byte[] keyData, byte[] eResult) throws Exception
	{
		wrapper = new AESWrapper();
		aResult = wrapper.wrap(keyData, 0, keyData.length, kek);
		
		assertEquals(StringUtil.toString(eResult), 
				StringUtil.toString(aResult));

		wrapper = new AESWrapper();
		aResult = wrapper.unwrap(eResult, 0, eResult.length, kek);

		assertEquals(StringUtil.toString(keyData), 
				StringUtil.toString(aResult));
		
	}
}
