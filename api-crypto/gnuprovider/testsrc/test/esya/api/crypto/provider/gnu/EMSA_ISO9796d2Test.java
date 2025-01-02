package test.esya.api.crypto.provider.gnu;

import gnu.crypto.Registry;
import gnu.crypto.sig.rsa.EMSA_ISO9796d2;

import java.util.Arrays;

import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;

public class EMSA_ISO9796d2Test extends TestCase
{
	protected int KEY_SIZE = 1024; 
	
	
	public void test1() throws Exception
	{
		byte [] data = "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopqopqrpqrs".getBytes("ASCII");
		byte [] encoded = StringUtil.toByteArray("4BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBA6162636462636465636465666465666765666768666768696768696A68696A6B696A6B6C6A6B6C6D6B6C6D6E6C6D6E6F6D6E6F706E6F70716F7071727071727379EA0C76F0056373FFD6A5AAD389DD908B0C0E9433CC");
		
		encodeTest(data, Registry.SHA160_HASH, false, encoded);
		decodeTest(data, Registry.SHA160_HASH, false, encoded);
	}
	
	public void test2() throws Exception
	{
		byte [] data = StringUtil.toByteArray("FEDCBA9876543210FEDCBA9876543210FEDCBA9876543210FEDCBA9876543210FEDCBA9876543210FEDCBA9876543210");
		byte [] encoded = StringUtil.toByteArray("4BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBAFEDCBA9876543210FEDCBA9876543210FEDCBA9876543210FEDCBA9876543210FEDCBA9876543210FEDCBA987654321085DCC7FC513716375A059D025439FCD925C828ACBC");
		
		encodeTest(data, Registry.SHA160_HASH, true, encoded);
		decodeTest(data, Registry.SHA160_HASH, true, encoded);
	}
	
	public void test3() throws Exception
	{
		byte [] data = "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopqopqrpqrsqrstrstustuvtuvwuvwxvwxywxyzxyzayzabzabcabcdbcde".getBytes("ASCII");
		byte [] encoded = StringUtil.toByteArray("6A6162636462636465636465666465666765666768666768696768696A68696A6B696A6B6C6A6B6C6D6B6C6D6E6C6D6E6F6D6E6F706E6F70716F707172707172737172737472737475737475767475767775767778767778797778797A78797A61797A61627A616263611CF7A9974518E555C1802CB810A23C274FCFAA7333CC");
		
		encodeTest(data, Registry.SHA160_HASH, false, encoded);
		decodeTest(data, Registry.SHA160_HASH, false, encoded);
	}
	
	
	
	
	public void encodeTest(byte [] data, String digestAlg, boolean implicit, byte [] expected) throws Exception
	{
		EMSA_ISO9796d2 iso9796d2 = EMSA_ISO9796d2.getInstance(digestAlg, implicit);
		
		iso9796d2.init(KEY_SIZE);
		iso9796d2.update(data, 0, data.length);
		
		byte [] encoded = iso9796d2.encode();
		
		boolean equal = Arrays.equals(expected, encoded);
		

		assertEquals(true, equal);
	}
	
	
	
	
	public void decodeTest(byte [] data, String digestAlg, boolean implicit, byte [] encoded) throws Exception
	{
		EMSA_ISO9796d2 iso9796d2 = EMSA_ISO9796d2.getInstance(digestAlg, implicit);
		
		iso9796d2.init(KEY_SIZE);
		iso9796d2.update(data, 0, data.length);
		
		boolean success = iso9796d2.decode(encoded);
		
		assertEquals(true, success);
	}
}
