package gnu.crypto.derivationFunctions;

import gnu.crypto.hash.IMessageDigest;

/**
 * Sec1 Section 3.6.1
 * Standards for Efficient Cryptography
 * @author orcun.ertugrul
 *
 */

public class X9_63KeyDerivation implements DerivationFunction {

	private byte []z;
	private IMessageDigest hash;
	private byte [] sharedInfo;
	
	private int hashLen;
	
	
	
	public X9_63KeyDerivation(IMessageDigest hash)
	{
		this.hash = hash;
	}
	
	public void init(byte [] seed, DerivationFuncParams params)
	{
		this.z = seed;
		this.hashLen = hash.hashSize();
		this.sharedInfo = ((DerivationFuncParamsWithSharedInfo)params).getSharedInfo();
	}
	
	
	/**
	 * 
	 * @param len the desired data length in bytes.
	 * @return
	 */
	public byte[] generateBytes(int len) {
		if (len > ((2L << 32) - 1))
        {
            throw new IllegalArgumentException("Output length too large");
        }
		byte []output = new byte[len];
		int loop = (len + hashLen - 1) / hashLen;
		int counter = 1;
		
		int offset = 0;
		for(int i=0; i < loop; i++)
		{
			hash.update(z, 0, z.length);
			
			hash.update((byte)(counter >> 24));
			hash.update((byte)(counter >> 16));
			hash.update((byte)(counter >> 8));
			hash.update((byte)counter);
			
			if(sharedInfo != null)
				hash.update(sharedInfo, 0, sharedInfo.length);
			
			byte [] dig = hash.digest();
			
			if(len > hashLen)
			{
				System.arraycopy(dig,0,output,offset,hashLen);
				offset += hashLen;
				len -= hashLen;
			}
			else
			{
				System.arraycopy(dig,0,output,offset,len);
			}		
			
			counter++;
		}
		
		return output;
	}

}
