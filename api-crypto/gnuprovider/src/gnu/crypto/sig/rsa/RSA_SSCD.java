package gnu.crypto.sig.rsa;

import gnu.crypto.prng.JavaRandomGenerator;
import gnu.crypto.util.PRNG;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.crypto.IRandom;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Signer;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;

/**
 * secure signature creation
 * Ozet=Hash(PRND2||K_KEC||RND.TCKK||SN.TCKK)
 * ImzaVerisi=(’6A’||PRND2||K_KEC||Ozet||’BC’)
 * Imza=Sign(Private.GEM,ImzaVerisi)
 * @author orcun.ertugrul
 *
*/

public class RSA_SSCD 
{
	protected BaseSigner mSigner;
	
	protected int  blockSize;
	
	protected BigInteger mModulus;
	
	protected DigestAlg mDigestAlg;
	
	protected RSAPublicKey mPublicKey;
	
	protected RSAPrivateKey mPrivKey;
	
	
	protected static Logger LOGGER = LoggerFactory.getLogger(RSA_SSCD.class);
	
	byte [] mDataEmbedded;
	
	public static final RSA_SSCD getInstance(DigestAlg aDigestAlg, IRandom aRand)
	{
		if(aRand == null)
			return new RSA_SSCD(aDigestAlg, new JavaRandomGenerator());
		else
			return  new RSA_SSCD(aDigestAlg, aRand);
	}
	
	public static final RSA_SSCD getInstance(DigestAlg aDigestAlg)
	{
		return new RSA_SSCD(aDigestAlg, new JavaRandomGenerator());
	}
	
	private RSA_SSCD (DigestAlg aDigestAlg, IRandom irnd)
	{
		mDigestAlg = aDigestAlg;
	}

	public void setupForVerification(PublicKey key)
			throws IllegalArgumentException 
	{
		if(!(key instanceof RSAPublicKey))
			throw new IllegalArgumentException("RSAPublicKey instance expected");
		
		mPublicKey = (RSAPublicKey) key;
	}
	
	

	public void setupForSigning(PrivateKey key)
			throws IllegalArgumentException, CryptoException 
	{
		if(!(key instanceof RSAPrivateKey))
			throw new IllegalArgumentException("RSAPrivateKey instance expected");
		
		mPrivKey = (RSAPrivateKey) key;
		
		mModulus = ((RSAPrivateKey)key).getModulus();
		
		blockSize = (((RSAPrivateKey)key).getModulus().bitLength() + 7) / 8;
		
		Signer signer = Crypto.getSigner(SignatureAlg.RSA_RAW);
		signer.init(key);
		mSigner = signer;
	}
	
	
	public void setupForSigning(BaseSigner signer, BigInteger modulus)
	{
		mModulus = modulus;
		blockSize = (modulus.bitLength() + 7) / 8;
		mSigner = signer;
	}

	/**
	 * Ozet=Hash(PRND||dataToBeSigned)
	 * ImzaVerisi=(’6A’||PRND||dataToBeEmbedded||Ozet||’BC’) 
	 * Imza=Sign(Private,ImzaVerisi)
	 * ImzaMin=Min(Imza,N.GEM-Imza)
	 * @param dataToBeSigned
	 * @param dataToBeRecovered
	 * @return
	 * @throws IllegalStateException
	 * @throws ESYAException 
	 */
	public byte [] generateSignature(byte [] dataToBeSigned, byte [] dataToBeRecovered) throws IllegalStateException, ESYAException
	{
		
		byte [] toBeSigned = new byte[blockSize];
		toBeSigned[0] = 0x6A;
		
		byte [] PRND = new byte[blockSize - 2 - mDigestAlg.getDigestLength() - dataToBeRecovered.length];
		try 
		{
			PRNG.nextBytes(PRND, 0, PRND.length);
			System.arraycopy(PRND, 0, toBeSigned, 1, PRND.length);
			
			
			System.arraycopy(dataToBeRecovered, 0, toBeSigned, toBeSigned.length - 1  - mDigestAlg.getDigestLength()
					- dataToBeRecovered.length, dataToBeRecovered.length);
			
			byte [] toBeDigest = ByteUtil.concatAll(PRND, dataToBeSigned);
			byte [] digest = DigestUtil.digest(mDigestAlg, toBeDigest);
			
			System.arraycopy(digest, 0, toBeSigned, toBeSigned.length-1-digest.length, digest.length);
			
			toBeSigned[toBeSigned.length - 1] = (byte) 0xBC;
			
			byte [] sBytes = mSigner.sign(toBeSigned);
			BigInteger s = new BigInteger(1, sBytes);
			
			
			if(mModulus.divide(BigInteger.valueOf(2)).compareTo(s) == -1)
			{
				s = mModulus.subtract(s);
			}
			
			return RSA.I2OSP(s, blockSize);
			
		} 
		
		catch (CryptoException e) 
		{
			new RuntimeException("Digest problem", e);
			return null;
		}
	}

	
	/**
	 * Ozet=Hash(PRND1||dataEmbedded||aDataThatisIncludedByMe)
	 * ImzaVerisi=(’6A’||PRND1||dataEmbedded||Ozet||’BC’) 
	 * Imza=Sign(PrK.TCKK.GM,ImzaVerisi)
	 * @param signature
	 * @param aDataThatisIncludedByMe
	 * @return
	 * @throws IllegalStateException
	 */
	public boolean verifySignature(byte [] signature,  byte [] aDataThatisIncludedByMe, int aLenOfDataToBeRecovered)
			throws IllegalStateException 
	{
		final BigInteger s = new BigInteger(1, signature);

		BigInteger m;
		try
		{
			m = RSA.verify(mPublicKey, s);
		}
		catch (IllegalArgumentException x)
		{
			return false;
		}

		byte [] mBytes = m.toByteArray();

		byte CC = (byte) 0xCC;
		byte BC = (byte) 0xBC;

		boolean implicit = mBytes[mBytes.length-1] == BC ? true : false;
		boolean sha1 = (mBytes[mBytes.length-1] == CC && mBytes[mBytes.length-2] == 0x33) ? true : false;
		boolean sha256 = (mBytes[mBytes.length-1] == CC && mBytes[mBytes.length-2] == 0x34) ? true : false;


		if(!(implicit||sha1|| sha256))
		{
			m = mPublicKey.getModulus().subtract(m);
			mBytes = m.toByteArray();
			implicit = mBytes[mBytes.length-1] == BC ? true : false;
			sha1 = (mBytes[mBytes.length-1] == CC && mBytes[mBytes.length-2] == 0x33) ? true : false;
			sha256 = (mBytes[mBytes.length-1] == CC && mBytes[mBytes.length-2] == 0x34) ? true : false;
		}



		if(!(implicit||sha1|| sha256))
		{
			return false;
		}

		int endLength = 1;
		if(sha1|| sha256)
			endLength = 2;

		int digestLen = mDigestAlg.getDigestLength();

		byte [] digestValue = new byte[digestLen];
		System.arraycopy(mBytes, mBytes.length - endLength - digestLen,
				digestValue, 0, digestLen);

		byte [] otherDeviceData = new byte[mBytes.length - endLength - digestLen - 1];
		System.arraycopy(mBytes, 1, otherDeviceData, 0, otherDeviceData.length);


		byte [] toBeDigestData = ByteUtil.concatAll(otherDeviceData, aDataThatisIncludedByMe);
		byte [] calculatedDigest = null;
		try
		{
			calculatedDigest = DigestUtil.digest(mDigestAlg, toBeDigestData);
		}
		catch (CryptoException e)
		{
			LOGGER.error("Can not calculate digest", e);
			return false;
		}

		if(Arrays.equals(calculatedDigest, digestValue) == true)
		{
			mDataEmbedded = new byte[aLenOfDataToBeRecovered];
			System.arraycopy(otherDeviceData, otherDeviceData.length - aLenOfDataToBeRecovered,
					mDataEmbedded, 0, aLenOfDataToBeRecovered);
			return true;
		}
		else
			return false;
	}
	
	
	
	public byte [] getDataRecovered()
	{
		return mDataEmbedded;
	}
		
	
	
}
