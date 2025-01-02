package dev.esya.api.crypto.provider.gnu;

import gnu.crypto.Registry;
import gnu.crypto.sig.rsa.RSAISO9796d2Signature;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import org.junit.Ignore;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;

@Ignore("Development tests")
public class RSAISO9796d2SignatureTest extends TestCase
{
	public void test1() throws Exception
	{
		BigInteger modulus = new BigInteger(1, StringUtil.toByteArray("FAA8ED34EEF1CE38D29814B6EEAA154DC060BB37EB1A51E8AB0398DDADDFD334CB9BE20C087B1DDF1F78A39762B5F20A7A73008630913CD2EE60183DE249DD169CA4EB3AE0420E5113D730504A73A926BEFBFF32C89858DE5E5B3899FEC5252104933163625F29635AB8FAA7AA14C4F3C0DD2470DEFCEB392429110A0149A771"));
		BigInteger privExponent = new BigInteger(1,StringUtil.toByteArray("0A71B48CDF4A13425E1BAB879F47163892AEB277A9CBC369B1CAD1093C93FE2233267EC0805A7693F6A506D0F9723F6B1A6F755AECB0B7DE1F44010294186936316AAC4BF39B37BF6105DFA0AEA60B82C17306F2179F2ED4704D5A6FBCB141C0C9380F5A500823CE67E8ED817F8A510059E9541B498C91F41ABE8C106220E72B"));
		KeySpec privKeySpec = new RSAPrivateKeySpec(modulus, privExponent);
		KeySpec pubKeySpec = new RSAPublicKeySpec(modulus, BigInteger.valueOf(3));
		
		
		RSAPrivateKey privkey =  (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(privKeySpec);
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(pubKeySpec);
		
		byte [] data = "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopqopqrpqrs".getBytes("ASCII");
		byte [] signed = StringUtil.toByteArray("D63692206E1FE0A57DF603C1E5EE6025B4EF2E693E8C3C9EBA00057B40860A35FCA66D8833795AC191191515FE852CADC80F315C86142051ED3227759F307934421D615F39792C401319F233CFFD18D012D17A02442E5BBFB17DCFC5654BEF595F500A15365CD5D0BD27948EC938F7C3BA775982472E89217424A74B868B63A8");
		
		//24725B1480D1ED9354A210F508BBB5280B718CCEAC8E1549F10393626D59C8FECEF57483D501C31D8E5F8E816430C55CB263CF29AA7D1C81012DF0C8431963E25A8789DBA6C8E21100BD3E1C7A769056AC2A85308469FD1EACDD68D4997935C7A543274E2C0253929D916618E0DBCD300665CAEE97CE6217B00469BE7ABE43C9
		//D63692206E1FE0A57DF603C1E5EE6025B4EF2E693E8C3C9EBA00057B40860A35FCA66D8833795AC191191515FE852CADC80F315C86142051ED3227759F307934421D615F39792C401319F233CFFD18D012D17A02442E5BBFB17DCFC5654BEF595F500A15365CD5D0BD27948EC938F7C3BA775982472E89217424A74B868B63A8
		
		sign(data, Registry.SHA160_HASH, false, privkey,signed);
		verify(signed, Registry.SHA160_HASH, false, pubKey, data);
	}
	
	
// 	RSA anahtarları ikiye bölünerek verildiğinden bizim altyapıda çalışmadı.
//	public void test2() throws Exception
//	{
//		BigInteger publicExponent = BigInteger.valueOf(2);	
//		
//		while(true)
//		{
//			
//			try
//			{
//				BigInteger modulus = new BigInteger(1, StringUtil.toByteArray("BCEB2EB02E1C8E9999BC9603F8F91DA6084EA6E7C75BD18DD0CDBEDB21DA29F19E7311259DB0D190B1920186A8126B582D13ABA69958763ADA8F79F162C7379D6109D2C94AA2E041B383A74BBF17FFCC145760AA8B58BE3C00C52BA3BD05A9D0BE5BA503E6721FC4066D37A89BF072C97BABB26CF6B29633043DB4746F9D2175"));
//				BigInteger privExponent = new BigInteger(1,StringUtil.toByteArray("029FB5FB55F949177777F3DC7FE703F7A3ABC25170FDB83E6A02DB8A2794CECE05C1992085BEE67757CCB1CC8972089A1D120D0CFB04C8C0D141FE235A42C453F0883D5E73742EB598435B52B393B491F053C59CA8950D48CA990ADF888C6DE4085CEB5D6B02AEABBCC2D543B4C9F9953FE165722F4E08469AD92248D8622DEA")).multiply(BigInteger.valueOf(2)).subtract(modulus);
//				KeySpec privKeySpec = new RSAPrivateKeySpec(modulus, privExponent);
//				KeySpec pubKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
//				
//				RSAPrivateKey privkey =  (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(privKeySpec);
//				RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(pubKeySpec);
//				
//				byte [] data = StringUtil.toByteArray("FEDCBA9876543210FEDCBA9876543210FEDCBA9876543210FEDCBA9876543210FEDCBA9876543210FEDCBA9876543210");
//				byte [] signed = StringUtil.toByteArray("0C0C62D3523F2DA3972679D0348D9A5038E93AE3D19E97DF875DCC046B2637DBCE7D4CCC5967529AB96D27B6D9B41F5456E65EEA328FDB7DAE6F4E7DA0CFC1CFF8AB5A80CC7C9B9F487EC2B590CBC2F31AFDC5CF9C3478B93C46D575A0E08D21D965A9C4FCAFE3562D64B1C30706AF0D43288156DA3FF990CB040D5C0863F262");
//					
//				sign(data, Registry.SHA160_HASH, true, privkey,signed);
//				verify(signed, Registry.SHA160_HASH, true, pubKey, data);
//				
//			}
//			catch(Throwable e)
//			{
//				publicExponent = publicExponent.add(BigInteger.ONE);
//				System.out.println(publicExponent);
//				continue;
//			}
//			System.out.println(publicExponent);
//			break;
//		}
//	}
//	
//	public void test3() throws Exception
//	{
//		BigInteger modulus = new BigInteger(1, StringUtil.toByteArray("BCEB2EB02E1C8E9999BC9603F8F91DA6084EA6E7C75BD18DD0CDBEDB21DA29F19E7311259DB0D190B1920186A8126B582D13ABA69958763ADA8F79F162C7379D6109D2C94AA2E041B383A74BBF17FFCC145760AA8B58BE3C00C52BA3BD05A9D0BE5BA503E6721FC4066D37A89BF072C97BABB26CF6B29633043DB4746F9D2175"));
//		BigInteger privExponent = new BigInteger(1,StringUtil.toByteArray("029FB5FB55F949177777F3DC7FE703F7A3ABC25170FDB83E6A02DB8A2794CECE05C1992085BEE67757CCB1CC8972089A1D120D0CFB04C8C0D141FE235A42C453F0883D5E73742EB598435B52B393B491F053C59CA8950D48CA990ADF888C6DE4085CEB5D6B02AEABBCC2D543B4C9F9953FE165722F4E08469AD92248D8622DEA"));
//		KeySpec privKeySpec = new RSAPrivateKeySpec(modulus, privExponent);
//		KeySpec pubKeySpec = new RSAPublicKeySpec(modulus, BigInteger.valueOf(2));
//		
//		
//		RSAPrivateKey privkey =  (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(privKeySpec);
//		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(pubKeySpec);
//		
//		byte [] data = "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopqopqrpqrsqrstrstustuvtuvwuvwxvwxywxyzxyzayzabzabcabcdbcde".getBytes("ASCII");
//		byte [] signed = StringUtil.toByteArray("AD83029DFB27EC14E5F8FDC08E10481F35CB879F62BC2180A17D84DE9C65FF91728DEAC06848885AAC99863F0B9370462FF8C5ED33DA98CBE75D54BA59CC12BFE9AF94B780E31542A96FD25A4B1AD9960032373A208D49650870EEB0A771F30274B0838995F0B131F0CE526F679B1618A1BCAAAB45AE4669421339D36398C111");
//		
//		sign(data, Registry.SHA160_HASH, false, privkey,signed);
//		verify(signed, Registry.SHA160_HASH, false, pubKey, data);
//	}

	

	private void sign(byte[] data, String digestAlg, boolean implicit,
			RSAPrivateKey privkey, byte[] signed) 
	{
		Map attributes = new HashMap<String, Object>();
		attributes.put(RSAISO9796d2Signature.SIGNER_KEY, privkey);
		
		RSAISO9796d2Signature signature = new RSAISO9796d2Signature(digestAlg, implicit);
		signature.setupSign(attributes);
		signature.update(data, 0, data.length);
		byte [] sign = (byte[]) signature.sign();
		
		boolean equal = Arrays.equals(sign, signed);
		
		assertEquals(true, equal);
	}
	
	private void verify(byte[] signed, String digestAlg, boolean implicit,
			RSAPublicKey pubKey, byte[] data) 
	{
		Map attributes = new HashMap<String, Object>();
		attributes.put(RSAISO9796d2Signature.VERIFIER_KEY, pubKey);
		
		RSAISO9796d2Signature signature = new RSAISO9796d2Signature(digestAlg, implicit);
		signature.setupVerify(attributes);
		signature.update(data, 0, data.length);
		boolean verified = signature.verify(signed);
		
		assertEquals(true, verified);
	}
	
	
	
}
