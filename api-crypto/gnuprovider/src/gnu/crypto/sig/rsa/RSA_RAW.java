package gnu.crypto.sig.rsa;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

import gnu.crypto.Registry;
import gnu.crypto.hash.HashFactory;
import gnu.crypto.sig.BaseSignature;

public class RSA_RAW extends BaseSignature
{
	
	ByteArrayOutputStream dataToBeSigned;
	int modulusByteLen;

	public RSA_RAW() 
	{
		//dummy message digest
		super(Registry.RSA_RAW, HashFactory.getInstance("SHA1"));
		dataToBeSigned = new ByteArrayOutputStream();
	}

	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setupForVerification(PublicKey key)
			throws IllegalArgumentException {
		 
			if (!(key instanceof RSAPublicKey)) {
	         throw new IllegalArgumentException();
	      }
	      publicKey = key;
	      modulusByteLen = (((RSAPublicKey)publicKey).getModulus().bitLength() + 7) / 8;
		
	}

	@Override
	protected void setupForSigning(PrivateKey key)
			throws IllegalArgumentException {
		 
		if (!(key instanceof RSAPrivateKey)) {
	         throw new IllegalArgumentException();
	      }
	      privateKey = key;
	      modulusByteLen = (((RSAPrivateKey)privateKey).getModulus().bitLength() + 7) / 8;
	}

	@Override
	protected Object generateSignature() throws IllegalStateException 
	{
		if(dataToBeSigned.size() > modulusByteLen)
			throw new IllegalStateException("Data is larger than modulus");
		BigInteger s = RSA.sign(privateKey, new BigInteger(1, dataToBeSigned.toByteArray()));
		return RSA.I2OSP(s, modulusByteLen);
	}
	
	@Override
	public void update(byte[] b, int off, int len)
	{
		dataToBeSigned.write(b, off, len);
	}

	@Override
	public void update(byte b) 
	{
		dataToBeSigned.write(b);
	}

	@Override
	protected boolean verifySignature(Object signature)
			throws IllegalStateException 
	{
		if(dataToBeSigned.size() > modulusByteLen)
			throw new RuntimeException("Data is larger than modulus");
		
		BigInteger s = new BigInteger(1, (byte []) signature);
		BigInteger m = RSA.verify(publicKey, s);
		byte [] EM = RSA.I2OSP(m, modulusByteLen);
		if(Arrays.equals(EM, dataToBeSigned.toByteArray()))
			return true;
		else
			return false;
	}

}
