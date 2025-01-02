/**
 * 
 */
package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;

import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp._in;


public class ECDSA_SS implements ISignatureScheme {

	private static HashMap<String, Long> algorithmMap = new HashMap<>();

	static {
		algorithmMap.put(Algorithms.SIGNATURE_ECDSA, PKCS11Constants.CKM_ECDSA);
		algorithmMap.put(Algorithms.SIGNATURE_ECDSA_SHA1, PKCS11Constants.CKM_ECDSA_SHA1);
		algorithmMap.put(Algorithms.SIGNATURE_ECDSA_SHA224, PKCS11Constants.CKM_ECDSA_SHA224);
		algorithmMap.put(Algorithms.SIGNATURE_ECDSA_SHA256, PKCS11Constants.CKM_ECDSA_SHA256);
		algorithmMap.put(Algorithms.SIGNATURE_ECDSA_SHA384, PKCS11Constants.CKM_ECDSA_SHA384);
		algorithmMap.put(Algorithms.SIGNATURE_ECDSA_SHA512, PKCS11Constants.CKM_ECDSA_SHA512);
	}

	String mSignatureAlg;
	long[] mMechanism;
	boolean mIsSigning;

	public ECDSA_SS(String aSigningAlg, long[] aMechanismList) 
	{
		mSignatureAlg = aSigningAlg;
		mMechanism = aMechanismList;
	}

	public P11SignParameters getSignParameters(byte[] aMessage) throws SmartCardException
	{
		CK_MECHANISM mech = new CK_MECHANISM(0);

		if(_in(PKCS11Constants.CKM_ECDSA, mMechanism))
		{
			byte[] hash = getHash(mSignatureAlg, aMessage);
			mech.mechanism = PKCS11Constants.CKM_ECDSA;
			return new P11SignParameters(mech, hash);
		}

		mech.mechanism = algorithmMap.get(mSignatureAlg);
		if(_in(mech.mechanism, mMechanism))
		{
			return new P11SignParameters(mech, aMessage);
		}

		throw new SmartCardException("Unsupported algorithm: " + mSignatureAlg);
	}

	private byte [] getHash(String signatureAlg, byte [] data) throws SmartCardException
	{
		if(signatureAlg.equals(Algorithms.SIGNATURE_ECDSA))
			return data;

		String digestAlg = null;
		try
		{
			digestAlg = Algorithms.getDigestAlgOfSignatureAlg(signatureAlg);
		}
		catch(ESYAException aException)
		{
			throw new SmartCardException(aException);
		}

		try
		{
			MessageDigest digester = MessageDigest.getInstance(digestAlg);
			byte [] digest = digester.digest(data);
			return digest;
		}
		catch(NoSuchAlgorithmException ex)
		{
			throw new SmartCardException(ex);
		}
	}

	public void init(boolean aIsSigning) 
	{
		mIsSigning = aIsSigning;
	}

}
