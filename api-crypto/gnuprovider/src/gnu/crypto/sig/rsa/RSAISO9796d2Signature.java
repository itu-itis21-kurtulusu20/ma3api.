package gnu.crypto.sig.rsa;

import gnu.crypto.Registry;
import gnu.crypto.hash.HashFactory;
import gnu.crypto.sig.BaseSignature;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * ISO9796d2 scheme 1
 * @author orcun.ertugrul
 *
 */
public class RSAISO9796d2Signature extends BaseSignature
{
	private EMSA_ISO9796d2 iso9796d2;
	
	public RSAISO9796d2Signature()
	{
		this(Registry.SHA160_HASH, false);
	}

	public RSAISO9796d2Signature(String mdName, boolean implicit) 
	{
		super(Registry.RSA_ISO9796d2, HashFactory.getInstance(mdName));
		iso9796d2 = EMSA_ISO9796d2.getInstance(mdName, implicit);
	}

	@Override
	public Object clone() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setupForVerification(PublicKey key)
	throws IllegalArgumentException 
	{
		if (!(key instanceof RSAPublicKey)) 
		{
			throw new IllegalArgumentException();
		}
		publicKey = key;
		
		final int modBits = ((RSAPublicKey) publicKey).getModulus().bitLength();

		iso9796d2.init(modBits);
	}

	@Override
	protected void setupForSigning(PrivateKey key)
	throws IllegalArgumentException 
	{
		if (!(key instanceof RSAPrivateKey)) 
		{
			throw new IllegalArgumentException();
		}
		privateKey = key;

		final int modBits = ((RSAPrivateKey) privateKey).getModulus().bitLength();

		iso9796d2.init(modBits);

	}

	@Override
	protected Object generateSignature() throws IllegalStateException 
	{
		byte [] block = iso9796d2.encode();

		BigInteger m = new BigInteger(1,block);
		BigInteger s = RSA.sign(privateKey, m);
		
		
		if(((RSAPrivateKey)privateKey).getModulus().divide(BigInteger.valueOf(2)).compareTo(s) == -1)
		{
			s = ((RSAPrivateKey)privateKey).getModulus().subtract(s);
		}

		clearData(block);
		clearData(m);

		return RSA.I2OSP(s, block.length);
	}

	@Override
	protected boolean verifySignature(Object sig)
	throws IllegalStateException 
	{
		RSAPublicKey rsaPubKey = (RSAPublicKey) publicKey;
		
		final byte[] S = (byte[]) sig;
		final int modBits = rsaPubKey.getModulus().bitLength();
		final int k = (modBits + 7) / 8;
		if (S.length != k) {
			return false;
		}
		
		
		BigInteger s = new BigInteger(1, S);
		
		//odd
		if(rsaPubKey.getPublicExponent().getLowestSetBit() == 0)
		{
			if(s.remainder(BigInteger.valueOf(16)) != BigInteger.valueOf(12))
			{
				s = rsaPubKey.getModulus().subtract(s);
			}
		}
		else
		{
			if(s.remainder(BigInteger.valueOf(8)) == BigInteger.valueOf(1))
			{
				s = rsaPubKey.getModulus().subtract(s);
			}
		}
		

		final BigInteger m;
		try {
			m = RSA.verify(publicKey, s);
		} catch (IllegalArgumentException x) {
			return false;
		}

		final byte[] block;
		try {
			block = RSA.I2OSP(m, k);
		} catch (IllegalArgumentException x) {
			return false;
		}

		return iso9796d2.decode(block);
	}

	@Override
	public void update(byte[] b, int off, int len)
	{
		super.update(b, off, len);
		iso9796d2.update(b, off, len);
	}

	@Override
	public void update(byte b) 
	{
		super.update(b);
		iso9796d2.update(b);
	}
	
	
	public byte [] getRecoveredMessage()
	{
		return iso9796d2.getRecoveredMessage();
	}
}
