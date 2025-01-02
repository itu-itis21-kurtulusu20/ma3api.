package gnu.crypto.wrapper;

import gnu.crypto.cipher.Rijndael;

/**
 * an implementation of RFC 3394
 * @author orcun.ertugrul
 *
 */
public class AESWrapper implements Wrapper 
{
	
	private static final int BLOCK_LENGTH = 16; // in bytes
	private Rijndael aesEngine;
	private byte []iv;
	
	
	
	public AESWrapper()
	{
		aesEngine = new Rijndael();
		iv = new byte []  {	(byte)0xa6, (byte)0xa6, (byte)0xa6, (byte)0xa6,
                			(byte)0xa6, (byte)0xa6, (byte)0xa6, (byte)0xa6 };
	}
	
	public AESWrapper(final byte []iv)
	{
		aesEngine = new Rijndael();
		this.iv = new byte[iv.length];
		System.arraycopy(iv, 0, this.iv, 0, iv.length);
	}

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public byte [] wrap(byte[]  in, int inOff, int inLen, byte []key) throws Exception
	{
		int     n = inLen / 8;
		if ((n * 8) != inLen)
		{
			throw new Exception("data that will be wrapped must be a multiple of 8 bytes");
		}

		byte[]  block = new byte[inLen + iv.length];
		byte[]  buf = new byte[8 + iv.length];

		System.arraycopy(iv, 0, block, 0, iv.length);		//A=iv , R = in
		System.arraycopy(in, 0, block, iv.length, inLen);	//A|R[i]

		Object keyObj = aesEngine.makeKey(key, BLOCK_LENGTH);

		for (int j = 0; j != 6; j++)
		{
			for (int i = 1; i <= n; i++)
			{
				System.arraycopy(block, 0, buf, 0, iv.length);		
				System.arraycopy(block, 8 * i, buf, iv.length, 8);	
				aesEngine.encrypt(buf, 0, buf, 0, keyObj, BLOCK_LENGTH);
				
				int t = n * j + i;
				for (int k = 1; t != 0; k++)
				{
					byte    v = (byte)t;

					buf[iv.length - k] ^= v;

					t >>>= 8;
				}

				System.arraycopy(buf, 0, block, 0, 8);
				System.arraycopy(buf, 8, block, 8 * i, 8);
			}
		}

		return block;
	}
	
	public byte [] unwrap(byte[]  in, int inOff, int inLen, byte []key) throws Exception
	{
		 
		int     n = inLen / 8;

		if ((n * 8) != inLen)
		{
			throw new Exception("data that will be wrapped must be a multiple of 8 bytes");
		}

		byte[]  block = new byte[inLen - iv.length];
		byte[]  a = new byte[iv.length];
		byte[]  buf = new byte[8 + iv.length];

		System.arraycopy(in, 0, a, 0, iv.length);
		System.arraycopy(in, iv.length, block, 0, inLen - iv.length);

		Object keyObj = aesEngine.makeKey(key, BLOCK_LENGTH);


		n = n - 1;

		for (int j = 5; j >= 0; j--)
		{
			for (int i = n; i >= 1; i--)
			{
				System.arraycopy(a, 0, buf, 0, iv.length);
				System.arraycopy(block, 8 * (i - 1), buf, iv.length, 8);

				int t = n * j + i;
				for (int k = 1; t != 0; k++)
				{
					byte    v = (byte)t;

					buf[iv.length - k] ^= v;

					t >>>= 8;
				}
				
				aesEngine.decrypt(buf, 0, buf, 0, keyObj, BLOCK_LENGTH);
				System.arraycopy(buf, 0, a, 0, 8);
				System.arraycopy(buf, 8, block, 8 * (i - 1), 8);
			}
		}

		if (!areEqual(a, iv))
		{
			throw new Exception("ivs not equal while unwrapping");
		}

		return block;
	}
	
	private  boolean areEqual(
	        byte[]  a,
	        byte[]  b)
	    {
	        if (a == b)
	        {
	            return true;
	        }

	        if (a == null || b == null)
	        {
	            return false;
	        }

	        if (a.length != b.length)
	        {
	            return false;
	        }

	        int nonEqual = 0;

	        for (int i = 0; i != a.length; i++)
	        {
	            nonEqual |= (a[i] ^ b[i]);
	        }

	        return nonEqual == 0;
	    }
}
