package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;

import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import static tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp._in;

/**
 * Created by IntelliJ IDEA.
 * User: bilen.ogretmen
 * Date: 5/17/11
 * Time: 8:55 AM
 */
public class Rsa_SS implements ISignatureScheme {


	private static HashMap<String, Long> algorithmMap = new HashMap<>();

	static {
		algorithmMap.put(Algorithms.SIGNATURE_RSA, PKCS11Constants.CKM_RSA_PKCS);
		algorithmMap.put(Algorithms.SIGNATURE_RSA_MD5, PKCS11Constants.CKM_MD5_RSA_PKCS);
		algorithmMap.put(Algorithms.SIGNATURE_RSA_SHA1, PKCS11Constants.CKM_SHA1_RSA_PKCS);
		algorithmMap.put(Algorithms.SIGNATURE_RSA_SHA224, PKCS11Constants.CKM_SHA224_RSA_PKCS);
		algorithmMap.put(Algorithms.SIGNATURE_RSA_SHA256, PKCS11Constants.CKM_SHA256_RSA_PKCS);
		algorithmMap.put(Algorithms.SIGNATURE_RSA_SHA512, PKCS11Constants.CKM_SHA512_RSA_PKCS);

	}

	String _signatureAlg;
	long[] _mechanisms;

	boolean _isSigning;


	public Rsa_SS(String aSigningAlg, long[] aMechanismList) {
		_signatureAlg = aSigningAlg;
		_mechanisms = aMechanismList;
	}

	public P11SignParameters getSignParameters(byte[] aMessage) throws SmartCardException
	{
		CK_MECHANISM mech = new CK_MECHANISM(0L);

		//Özellikle HSM destekliyor mu diye bakılmadı. Eğer desteklemiyorsa imza atılırken hata alınacak.
		//Hem denenmiş olacak, hem de hata daha anlışılır olur.
		if(_signatureAlg.equals(Algorithms.SIGNATURE_RSA_RAW)) {
			mech.mechanism = PKCS11Constants.CKM_RSA_X_509;
			return new P11SignParameters(mech, aMessage);
		}

		/*
		 * Thales FIPS modunda CKM_RSA_PKCS mekanizması yerine özet içeren mekanizmaların kullanılmasını istiyor.
		 * (Kaynak: NOTE When the HSM is in FIPS mode, this mechanism cannot be used to sign data using less than 224 bits. This algorithm must be combined with
		 * a FIPS-approved hash algorithm to be FIPS compliant. from https://thalesdocs.com/gphsm/luna/7/docs/network/Content/sdk/mechanisms/CKM_RSA_PKCS.htm)
		 * PC tarafını scale etmek daha kolay olduğu ve network maliyetini düşürmek için mümkün olduğunda özetin PC tarafında alınması performans açısından daha avantajlı.
		 * Burada Thales FIPS modunun isteği ile bizim tercih ettiğimiz yöntem çelişiyor.
		 * Thales FIPS modundaki kullanıcılar SmartCard sınıfının setMechanismsToBeRemoved methodunu kullunarak CKM_RSA_PKCS mekanizmasını disable edebilirler.
		 * **/
		if(_in(PKCS11Constants.CKM_RSA_PKCS, _mechanisms))
		{
			mech.mechanism = PKCS11Constants.CKM_RSA_PKCS;
			byte[] hash = getHashStruct(_signatureAlg, aMessage);
			return new P11SignParameters(mech, hash);
		}

		mech.mechanism = algorithmMap.get(_signatureAlg);
		if(_in(mech.mechanism, _mechanisms))
		{
			return new P11SignParameters(mech, aMessage);
		}

		throw new SmartCardException("Mechanism is not supported. SignatureAlg: "
				+ _signatureAlg + " Supported Mechanisms: " + Arrays.toString(_mechanisms));

	}

	private byte[] getHashStruct(String signatureAlg, byte[] aMessage) throws SmartCardException {

		if(signatureAlg.equals(Algorithms.SIGNATURE_RSA))
			return aMessage;

		MessageDigest ozetci = null;
		byte[] hashPrefix = null;
		String digestAlg = null;
		try
		{
			digestAlg = Algorithms.getDigestAlgOfSignatureAlg(signatureAlg);
			ozetci = MessageDigest.getInstance(digestAlg);
			hashPrefix = getPrefixForDigestAlg(digestAlg);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new SmartCardException(digestAlg + " algorithm is not supported", e);
		}
		catch (ESYAException e)
		{
			throw new SmartCardException("UnKnown Digest Algorithm", e);
		}

		ozetci.update(aMessage);
		byte[] messageHash = ozetci.digest();

		return ByteUtil.concatAll(hashPrefix, messageHash);
	}


	private byte[] getPrefixForDigestAlg(String aDigestAlg) throws NoSuchAlgorithmException
	{
		if(aDigestAlg.equals(Algorithms.DIGEST_SHA1))
			return sha1Prefix;
		else if(aDigestAlg.equals(Algorithms.DIGEST_SHA256))
			return sha256Prefix;
		if(aDigestAlg.equals(Algorithms.DIGEST_SHA384))
			return sha384Prefix;
		if(aDigestAlg.toUpperCase().equals(Algorithms.DIGEST_SHA512))
			return sha512Prefix;

		throw new NoSuchAlgorithmException(aDigestAlg + " UnKnown digest algorithm");
	}

	private static byte [] sha1Prefix = new byte[] { (byte) 0x30, (byte) 0x21, (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x05, (byte) 0x2B,
		(byte) 0x0E, (byte) 0x03, (byte) 0x02, (byte) 0x1A, (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x14 };

	private static byte [] sha256Prefix = new byte[] { (byte) 0x30, (byte) 0x31, (byte) 0x30, (byte) 0x0d, (byte) 0x06, (byte) 0x09, (byte) 0x60,
		(byte) 0x86, (byte) 0x48,(byte) 0x01,(byte) 0x65,(byte) 0x03,(byte) 0x04, (byte) 0x02, (byte) 0x01, (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x20 };

	private static byte [] sha384Prefix = new byte[] { (byte) 0x30, (byte) 0x41, (byte) 0x30, (byte) 0x0d, (byte) 0x06, (byte) 0x09, (byte) 0x60,
		(byte) 0x86, (byte) 0x48,(byte) 0x01,(byte) 0x65,(byte) 0x03,(byte) 0x04, (byte) 0x02, (byte) 0x02, (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x30 };

	private static byte [] sha512Prefix = new byte[] { (byte) 0x30, (byte) 0x51, (byte) 0x30, (byte) 0x0d, (byte) 0x06, (byte) 0x09, (byte) 0x60,
		(byte) 0x86, (byte) 0x48,(byte) 0x01,(byte) 0x65,(byte) 0x03,(byte) 0x04, (byte) 0x02, (byte) 0x03, (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x40 };

	
	public void init(boolean aIsSigning)
	{
		_isSigning = aIsSigning;
	}
}
