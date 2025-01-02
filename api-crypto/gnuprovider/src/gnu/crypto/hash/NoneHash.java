package gnu.crypto.hash;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;

class NoneHash implements IMessageDigest 
{
	protected int maxblockSize;
	protected ByteArrayOutputStream data;

	public NoneHash(int aMaxBlockSize)
	{
		data = new ByteArrayOutputStream();
		maxblockSize = aMaxBlockSize;
	}

	public NoneHash()
	{
		data = new ByteArrayOutputStream();
		maxblockSize = 16384;
	}

	public String name() 
	{
		return "None";
	}

	public int hashSize() 
	{
		return data.size();
	}

	public int blockSize() 
	{
		return data.size();
	}

	public void update(byte b) 
	{
		data.write(b);
		if(data.size() > maxblockSize)
			throw new ESYARuntimeException("Block size exceeded");
	}

	public void update(byte[] in, int offset, int length) 
	{
		data.write(in, offset, length);
		if(data.size() > maxblockSize)
			throw new ESYARuntimeException("Block size exceeded");
	}

	public byte[] digest() 
	{
		return data.toByteArray();
	}

	public void reset() 
	{
		data = new ByteArrayOutputStream();
	}

	// IDigestMessage methods
	public Object clone()
	{
		return new NoneHash(maxblockSize);
	}


	public boolean selfTest()
	{
		byte [] expected = {0x61, 0x62, 0x63};
		NoneHash md = new NoneHash();
		md.update((byte) 0x61); // a
		md.update((byte) 0x62); // b
		md.update((byte) 0x63); // c
		return Arrays.equals(expected, md.digest());
	}

}
