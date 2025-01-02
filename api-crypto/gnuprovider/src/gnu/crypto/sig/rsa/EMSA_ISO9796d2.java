package gnu.crypto.sig.rsa;

import gnu.crypto.Registry;
import gnu.crypto.hash.HashFactory;
import gnu.crypto.hash.IMessageDigest;
import gnu.crypto.hash.RipeMD128;
import gnu.crypto.hash.RipeMD160;
import gnu.crypto.hash.Sha160;



/**
 * ISO9796d2 scheme 1
 * 
 * 
 * 
 * @author orcun.ertugrul
 * 
 * ToDo scheme 2 PSS'li olacak.
 */
public class EMSA_ISO9796d2
{
	static final public int   TRAILER_IMPLICIT    = 0xBC;
	static final public int   TRAILER_RIPEMD160   = 0x31CC;
	static final public int   TRAILER_RIPEMD128   = 0x32CC;
	static final public int   TRAILER_SHA1        = 0x33CC;

	private int trailer;

	private boolean fullMessage;
	
	private byte [] recoveredMessage = null;

	private boolean implicit;

	protected IMessageDigest md;

	protected byte [] mBuf;
	protected int messageLength;

	protected int blockLength;
	protected int modBits;

	public static final EMSA_ISO9796d2 getInstance(final String mdName) 
	{
		return getInstance(mdName, false);
	}  

	public static final EMSA_ISO9796d2 getInstance(final String mdName, boolean implicit) 
	{
		final IMessageDigest hash = HashFactory.getInstance(mdName);

		final String name = hash.name();

		if(implicit == false)
		{
			if (!(name.equals(Registry.RIPEMD128_HASH)
					|| name.equals(Registry.RIPEMD160_HASH)
					|| name.equals(Registry.SHA160_HASH))) 

				throw new UnsupportedOperationException("Unsuppoerted hash algorithm");
		}

		return new EMSA_ISO9796d2(hash,implicit);
	}


	protected EMSA_ISO9796d2(IMessageDigest hash, boolean implicit)
	{
		md = hash;
		messageLength = 0;
		this.implicit = implicit;
		if (implicit)
		{
			trailer = TRAILER_IMPLICIT;
		}
		else
		{
			if (md instanceof Sha160)
			{
				trailer = TRAILER_SHA1;
			}
			else if (md instanceof RipeMD160)
			{
				trailer = TRAILER_RIPEMD160;
			}
			else if (md instanceof RipeMD128)
			{
				trailer = TRAILER_RIPEMD128;
			}
			else
			{
				throw new IllegalArgumentException("no valid trailer for digest");
			}
		}
	}

	public void update(byte[] b, int off, int len)
	{
		md.update(b,off,len);
		if (messageLength < mBuf.length)
		{
			for (int i = 0; i < len && (i + messageLength) < mBuf.length; i++)
			{
				mBuf[messageLength + i] = b[off + i];
			}
		}
		messageLength += len;
	}

	public void update(byte b) 
	{
		md.update(b);
		if (messageLength < mBuf.length)
		{
			mBuf[messageLength] = b;
		}
		messageLength++;
	}

	public void init(int modBits)
	{
		this.modBits = modBits; 
		this.blockLength = (modBits + 7) / 8;

		if(trailer == TRAILER_IMPLICIT)
			mBuf = new byte [blockLength - md.hashSize() - 2];
		else
			mBuf = new byte [blockLength - md.hashSize() - 3];
	}

	public byte [] encode()
	{
		int digSize = md.hashSize();

		byte [] block = new byte[blockLength];

		int t = 0;
		int delta = 0;

		if (trailer == TRAILER_IMPLICIT)
		{
			t = 8;
			delta = blockLength - digSize - 1;
			byte [] digest = md.digest();
			System.arraycopy(digest, 0, block, delta, digSize);
			block[block.length - 1] = (byte)TRAILER_IMPLICIT;
		}
		else
		{
			t = 16;
			delta = block.length - digSize - 2;
			byte [] digest = md.digest();
			System.arraycopy(digest, 0, block, delta, digSize);
			block[block.length - 2] = (byte)(trailer >>> 8);
			block[block.length - 1] = (byte)trailer;
		}


		byte    header = 0;
		int     x = (digSize + messageLength) * 8 + t + 4 - modBits;

		if (x > 0)
		{
			int mR = messageLength - ((x + 7) / 8);
			header = 0x60;

			delta -= mR;

			System.arraycopy(mBuf, 0, block, delta, mR);
		}
		else
		{
			header = 0x40;
			delta -= messageLength;

			System.arraycopy(mBuf, 0, block, delta, messageLength);
		}

		if ((delta - 1) > 0)
		{
			for (int i = delta - 1; i != 0; i--)
			{
				block[i] = (byte)0xbb;
			}
			block[delta - 1] ^= (byte)0x01;
			block[0] = (byte)0x0b;
			block[0] |= header;
		}
		else
		{
			block[0] = (byte)0x0a;
			block[0] |= header;
		}

		return  block;
	}

	public boolean decode(byte [] block)
	{
		if (((block[0] & 0xC0) ^ 0x40) != 0)
		{
			clearData(mBuf);
			clearData(block);

			return false;
		}

		if (((block[block.length - 1] & 0xF) ^ 0xC) != 0)
		{
			clearData(mBuf);
			clearData(block);

			return false;
		}

		int     delta = 0;

		if (((block[block.length - 1] & 0xFF) ^ 0xBC) == 0)
		{
			delta = 1;
		}
		else
		{
			int sigTrail = ((block[block.length - 2] & 0xFF) << 8) | (block[block.length - 1] & 0xFF);

			switch (sigTrail)
			{
			case TRAILER_RIPEMD160:
				if (!(md instanceof RipeMD160))
				{
					throw new IllegalStateException("signer should be initialised with RIPEMD160");
				}
				break;
			case TRAILER_SHA1:
				if (!(md instanceof Sha160))
				{
					throw new IllegalStateException("signer should be initialised with SHA1");
				}
				break;
			case TRAILER_RIPEMD128:
				if (!(md instanceof RipeMD128))
				{
					throw new IllegalStateException("signer should be initialised with RIPEMD128");
				}
				break;
			default:
				throw new IllegalArgumentException("unrecognised hash in signature");
			}

			delta = 2;
		}

		//
		// find out how much padding we've got
		//
		int mStart = 0;

		for (mStart = 0; mStart != block.length; mStart++)
		{
			if (((block[mStart] & 0x0f) ^ 0x0a) == 0)
			{
				break;
			}
		}

		mStart++;



		int off = block.length - delta - md.hashSize();

		if ((off - mStart) <= 0)
		{
			clearData(mBuf);
			clearData(block);

			return false;
		}

		

		//
		// check hashes
		//
		if ((block[0] & 0x20) == 0)
		{
			fullMessage = true;

			md.reset();
			md.update(block, mStart, off - mStart);
			byte [] hash = md.digest();

			for (int i = 0; i != hash.length; i++)
			{
				block[off + i] ^= hash[i];
				if (block[off + i] != 0)
				{
					clearData(mBuf);
					clearData(block);

					return false;
				}
			}

			recoveredMessage = new byte[off - mStart];
			System.arraycopy(block, mStart, recoveredMessage, 0, recoveredMessage.length);
		}
		else
		{
			fullMessage = false;

			byte [] hash = md.digest();

			for (int i = 0; i != hash.length; i++)
			{
				block[off + i] ^= hash[i];
				if (block[off + i] != 0)
				{
					clearData(mBuf);
					clearData(block);

					return false;
				}
			}

			recoveredMessage = new byte[off - mStart];
			System.arraycopy(block, mStart, recoveredMessage, 0, recoveredMessage.length);
		}

		if (messageLength != 0)
		{
			if (!isSameAs(mBuf, recoveredMessage))
			{
				clearData(mBuf);
				clearData(block);

				return false;
			}
		}

		clearData(mBuf);
		clearData(block);

		return true;
	}


	private boolean isSameAs(byte[] a, byte[] b)
	{
		if (messageLength > mBuf.length)
		{
			if (mBuf.length > b.length)
			{
				return false;
			}

			for (int i = 0; i != mBuf.length; i++)
			{
				if (a[i] != b[i])
				{
					return false;
				}
			}
		}
		else
		{
			if (messageLength != b.length)
			{
				return false;
			}

			for (int i = 0; i != b.length; i++)
			{
				if (a[i] != b[i])
				{
					return false;
				}
			}
		}

		return true;
	}

	private void clearData(byte[] data)
	{
		for(int i=0; i < data.length; i++)
			data[i] = 0;
	}

	public Object clone() 
	{
		return getInstance(md.name(),implicit);
	}

	public boolean hasFullMessage()
	{
		return fullMessage;
	}
	
	public byte [] getRecoveredMessage()
	{
		return recoveredMessage;
	}
}
