package tr.gov.tubitak.uekae.esya.api.common.crypto;

/**
 * Algorithms class contains the String name values of signature algorithms, digest algorithms, and cipher algorithms. It also includes 
 * several utility methods. 
 */

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.HashMap;

public class Algorithms
{
	public static final String ASYM_ALGO_RSA = "RSA";
	public static final String ASYM_ALGO_ECDSA = "ECDSA";
	public static final String ASYM_ALGO_EC = "EC";
	public static final String ASYM_ALGO_DSA = "DSA";


	public static final String CIPHER_RSA_PKCS1 = "RSA/NONE/PKCS1";
	public static final String CIPHER_RSAOAEP = "RSA/NONE/OAEP";
	public static final String CIPHER_RSA_RAW = "RSA/NONE/NONE";


	public static final String CIPHER_AES128_CBC = "AES128/CBC/PKCS7";
	public static final String CIPHER_AES128_CBC_NOPADDING = "AES128/CBC/NONE";
	public static final String CIPHER_AES128_CFB = "AES128/CFB/PKCS7";
	public static final String CIPHER_AES128_ECB = "AES128/ECB/PKCS7";
	public static final String CIPHER_AES128_ECB_NOPADDING = "AES128/ECB/NONE";
	public static final String CIPHER_AES128_OFB = "AES128/OFB/PKCS7";
	public static final String CIPHER_AES128_GCM = "AES128/GCM/NONE";

	public static final String CIPHER_AES192_CBC = "AES192/CBC/PKCS7";
	public static final String CIPHER_AES192_CBC_NOPADDING = "AES192/CBC/NONE";
	public static final String CIPHER_AES192_CFB = "AES192/CFB/PKCS7";
	public static final String CIPHER_AES192_ECB = "AES192/ECB/PKCS7";
	public static final String CIPHER_AES192_ECB_NOPADDING = "AES192/ECB/NONE";
	public static final String CIPHER_AES192_OFB = "AES192/OFB/PKCS7";
	public static final String CIPHER_AES192_GCM = "AES192/GCM/NONE";


	public static final String CIPHER_AES256_CBC = "AES256/CBC/PKCS7";
	public static final String CIPHER_AES256_CBC_NOPADDING = "AES256/CBC/NONE";
	public static final String CIPHER_AES256_CFB = "AES256/CFB/PKCS7";
	public static final String CIPHER_AES256_ECB = "AES256/ECB/PKCS7";
	public static final String CIPHER_AES256_ECB_NOPADDING = "AES256/ECB/NONE";
	public static final String CIPHER_AES256_OFB = "AES256/OFB/PKCS7";
	public static final String CIPHER_AES256_GCM = "AES256/GCM/NONE";

	public static final String CIPHER_DES_CBC = "DES/CBC/PKCS7";
	public static final String CIPHER_DES_CBC_NOPADDING = "DES/CBC/NONE";
	public static final String CIPHER_DES_ECB = "DES/ECB/PKCS7";
	public static final String CIPHER_DES_ECB_NOPADDING = "DES/ECB/NONE";

	public static final String CIPHER_DES_EDE3_CBC = "3DES/CBC/PKCS7";
	public static final String CIPHER_DES_EDE3_CBC_NOPADDING = "3DES/CBC/NONE";
	public static final String CIPHER_DES_EDE3_ECB = "3DES/ECB/PKCS7";
	public static final String CIPHER_DES_EDE3_ECB_NOPADDING = "3DES/ECB/NONE";

	public static final String SIGNATURE_RSA = "RSA-with-NONE";
	public static final String SIGNATURE_RSA_RAW = "RSA-RAW";
	public static final String SIGNATURE_RSA_MD5 = "RSA-with-MD5";
	public static final String SIGNATURE_RSA_SHA1 = "RSA-with-SHA1";
	public static final String SIGNATURE_RSA_SHA224 = "RSA-with-SHA224";
	public static final String SIGNATURE_RSA_SHA256 = "RSA-with-SHA256";
	public static final String SIGNATURE_RSA_SHA384 = "RSA-with-SHA384";
	public static final String SIGNATURE_RSA_SHA512 = "RSA-with-SHA512";

	public static final String SIGNATURE_RSA_PSS = "RSAPSS";

	public static final String SIGNATURE_RSA_ISO9796_2_SHA1 = "RSA-ISO9796-2-with-SHA1";
	public static final String SIGNATURE_RSA_ISO9796_2_SHA224 = "RSA-ISO9796-2-with-SHA224";
	public static final String SIGNATURE_RSA_ISO9796_2_SHA256 = "RSA-ISO9796-2-with-SHA256";
	public static final String SIGNATURE_RSA_ISO9796_2_SHA384 = "RSA-ISO9796-2-with-SHA384";
	public static final String SIGNATURE_RSA_ISO9796_2_SHA512 = "RSA-ISO9796-2-with-SHA512";


	public static final String SIGNATURE_ECDSA = "ECDSA";
	public static final String SIGNATURE_ECDSA_SHA1 = "ECDSA-with-SHA1";
	public static final String SIGNATURE_ECDSA_SHA224 = "ECDSA-with-SHA224";
	public static final String SIGNATURE_ECDSA_SHA256 = "ECDSA-with-SHA256";
	public static final String SIGNATURE_ECDSA_SHA384 = "ECDSA-with-SHA384";
	public static final String SIGNATURE_ECDSA_SHA512 = "ECDSA-with-SHA512";

	public static final String SIGNATURE_DSA = "DSA";
	public static final String SIGNATURE_DSA_SHA1 = "DSA-with-SHA1";
	public static final String SIGNATURE_DSA_SHA256 = "DSA-with-SHA256";


	public static final String DIGEST_MD5 = "MD5";
	public static final String DIGEST_SHA1 = "SHA-1";
	public static final String DIGEST_SHA224 = "SHA-224";
	public static final String DIGEST_SHA256 = "SHA-256";
	public static final String DIGEST_SHA384 = "SHA-384";
	public static final String DIGEST_SHA512 = "SHA-512";
	public static final String DIGEST_RIPEMD160 = "RIPEMD";


	private static final HashMap<String,Integer> DIGEST_LENGTH_MAP = new HashMap<String, Integer>();

	static
	{
		DIGEST_LENGTH_MAP.put(DIGEST_MD5, 16);
		DIGEST_LENGTH_MAP.put(DIGEST_SHA1, 20);
		DIGEST_LENGTH_MAP.put(DIGEST_SHA224, 28);
		DIGEST_LENGTH_MAP.put(DIGEST_SHA256, 32);
		DIGEST_LENGTH_MAP.put(DIGEST_SHA384, 48);
		DIGEST_LENGTH_MAP.put(DIGEST_SHA512, 64);
	}


	/**
	 * Returns the name of digest algorithm for the given signature algorithm. 
	 * @param aSignatureAlg Signature algorithm 
	 * @return Digest algorithm
	 * @throws ESYAException
	 */
	public static final String getDigestAlgOfSignatureAlg(String aSignatureAlg) throws ESYAException
	{
		if(aSignatureAlg.contains(DIGEST_MD5))
			return DIGEST_MD5;

		if(aSignatureAlg.contains("SHA1"))
			return DIGEST_SHA1;

		if(aSignatureAlg.contains("SHA224"))
			return DIGEST_SHA224;

		if(aSignatureAlg.contains("SHA256"))
			return DIGEST_SHA256;

		if(aSignatureAlg.contains("SHA384"))
			return DIGEST_SHA384;

		if(aSignatureAlg.contains("SHA512"))
			return DIGEST_SHA512;

		if(aSignatureAlg.contains("RIPEMD"))
			return DIGEST_RIPEMD160;

		if(aSignatureAlg.contains("NONE"))
			return null;


		throw new ESYAException("UnKnown Algorithm: " + aSignatureAlg);
	}


	/**
	 * Returns signature algorithm without digest algorithm. For example, if RSA-with-SHA1 is given as parameter, this method returns RSA. 
	 * @param aSignatureAlg Name of the signature algorithm
	 * @return signature algorithm without digest algorithm
	 * @throws ESYAException If given signature algorithm is unknown for this class
	 */
	public static final String getAsymAlgOfSignatureAlg(String aSignatureAlg) throws ESYAException
	{
		if(aSignatureAlg.contains(ASYM_ALGO_RSA))
			return ASYM_ALGO_RSA;

		if(aSignatureAlg.contains(ASYM_ALGO_ECDSA))
			return ASYM_ALGO_ECDSA;

		if(aSignatureAlg.contains(ASYM_ALGO_DSA))
			return ASYM_ALGO_DSA;

		throw new ESYAException("UnKnown Algorithm: " + aSignatureAlg);
	}


	/**
	 * Returns the length of the digest in bytes 
	 * @param aDigestAlg Digest algorithm. 
	 * @return The length of the digest in bytes. If the given digest algorithm is unknown for this class, 0 is returned.
	 */
	public static final int getLengthofDigestAlg(String aDigestAlg)
	{
		Integer l = DIGEST_LENGTH_MAP.get(aDigestAlg);
		if(l!=null)
			return l;
		return 0;
	}

}
